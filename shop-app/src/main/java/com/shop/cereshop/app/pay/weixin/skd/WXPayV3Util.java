package com.shop.cereshop.app.pay.weixin.skd;

import com.alibaba.fastjson.JSONObject;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.ParseException;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Auther:kahu
 * @Date:2022/06/2022/6/1/10:50
 */
@Component
@Slf4j
public class WXPayV3Util {
    public static final String api_v3_placeAnOrder_url = "https://api.mch.weixin.qq.com/v3/pay/transactions/app";

    public static final String api_v3_refund_url = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";

    private String APPLICATION_JSON = "application/json";
    // 你的商户私钥
    private String privateKey;
    // 你的微信支付平台证书
    private String certificate;

    private CloseableHttpClient httpClient;

    private Verifier verifier;

    private static CertificatesManager certificatesManager;

    @Value("${weixin.mchid}")
    private String mchId;

    @Value("${weixin.apiV3Key}")
    private String v3Key;

    @Value("${weixin.mchSerialNo}")
    private String mchSerialNo;

    @Value("${weixin.platformCertSerialNo}")
    private String platformCertSerialNo;

    @Value("${weixin.privateKeyPath}")
    private String privateKeyPath;

    @Value("${weixin.platformCertPath}")
    private String platformCertPath;

    public void setup() throws Exception {
        // 获取商户
        try {
            /*InputStream apiclientKey = getClass()
                    .getClassLoader()
                    .getResourceAsStream(privateKeyPath);*/
            InputStream apiclientKey = new FileInputStream(new File(privateKeyPath));
            File appKey = new File("/tmp/apiclient_key_temp.crt");
            FileUtils.copyInputStreamToFile(apiclientKey, appKey);

            /*InputStream wecharpaytKey = getClass()
                    .getClassLoader()
                    .getResourceAsStream(privateCertPath);*/
            InputStream wecharpaytKey = new FileInputStream(new File(platformCertPath));
            File appCert = new File("/tmp/wechatpay_cert_temp.crt");
            FileUtils.copyInputStreamToFile(wecharpaytKey, appCert);


            this.privateKey = WXPayV3Util.getPrivateKeyStr(
                    appKey.getAbsolutePath()
            );
            this.certificate = WXPayV3Util.getPrivateKeyStr(
                    appCert.getAbsolutePath()
            );

            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(privateKey);

            certificatesManager = CertificatesManager.getInstance();
            // 向证书管理器增加需要自动更新平台证书的商户信息
            certificatesManager.putMerchant(mchId, new WechatPay2Credentials(mchId,
                            new PrivateKeySigner(mchSerialNo, merchantPrivateKey)),
                    v3Key.getBytes(StandardCharsets.UTF_8));
            // 从证书管理器中获取verifier
            verifier = certificatesManager.getVerifier(mchId);

            X509Certificate wechatPayCertificate = PemUtil.loadCertificate(
                    new ByteArrayInputStream(certificate.getBytes(StandardCharsets.UTF_8)));

            ArrayList<X509Certificate> listCertificates = new ArrayList<>();
            listCertificates.add(wechatPayCertificate);

            WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                    .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                    .withWechatPay(listCertificates);
            httpClient = builder.build();
        } catch (IOException e) {
            log.error("setup IOException " + e.getMessage(), e);
            throw new CoBusinessException("IO异常：" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("setup NoSuchAlgorithmException " + e.getMessage(), e);
            throw new CoBusinessException("未找到配置文件：" + e.getMessage());
        } catch (InvalidKeySpecException e) {
            log.error("InvalidKeySpecException " + e.getMessage(), e);
            throw new CoBusinessException("key生成异常：" + e.getMessage());
        }
    }

    /**
     * api_v3下单
     * 文档地址 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_1.shtml
     * @param body 请求体JSON字符串
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public JSONObject doPostWeixinV3(String url, String body) throws Exception {
        if (httpClient == null) {
            setup();
        }
        //https://api.mch.weixin.qq.com/v3/pay/transactions/app
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json;chartset=utf-8");
        httpPost.addHeader("Accept", "application/json");
        try {
            if (body == null) {
                throw new IllegalArgumentException("data参数不能为空");
            }
            StringEntity stringEntity = new StringEntity(body, "utf-8");
            httpPost.setEntity(stringEntity);
            // 直接执行execute方法，官方会自动处理签名和验签，并进行证书自动更新
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResult = EntityUtils.toString(httpEntity);
                return JSONObject.parseObject(jsonResult);
            } else {
                log.error("doPostWeixinV3 status != 200, " + EntityUtils.toString(httpEntity));
            }
        } catch (Exception e) {
            log.error("doPostWeixinV3 fail, " + e.getMessage(), e);
            throw new CoBusinessException("微信支付异常：" + e.getMessage());
        }
        return null;
    }


    /**
     * V3  SHA256withRSA 签名.
     *
     * @param appId
     * @param timestamp
     * @param nonceStr
     * @param prepayid
     * @return
     * @throws SignatureException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws URISyntaxException
     */
    public String sign(String appId, long timestamp, String nonceStr, String prepayid) throws SignatureException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, URISyntaxException {
        String signatureStr = Stream.of(appId, String.valueOf(timestamp), nonceStr, prepayid)
                .collect(Collectors.joining("\n", "", "\n"));
        Signature sign = Signature.getInstance("SHA256withRSA");
//        ClassPathResource classPathResource = new ClassPathResource("/cert/api_v3/apiclient_key.pem");
        File file = copyTempFileByResourcePath(privateKeyPath);
        sign.initSign(WXPayV3Util.getPrivateKey(file.getAbsolutePath()));
        sign.update(signatureStr.getBytes(StandardCharsets.UTF_8));
        return Base64Utils.encodeToString(sign.sign());
    }

    /**
     * 获取私钥。
     *
     * @param filename 私钥文件路径  (required)
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String content = new String(Files.readAllBytes(Paths.get(filename)), "UTF-8");
        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

    /**
     * 获取私钥字符串。
     *
     * @param filename 私钥文件路径  (required)
     * @return 私钥对象
     */
    public static String getPrivateKeyStr(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return new String(Files.readAllBytes(Paths.get(filename)), "UTF-8");
    }

    /**
     * 解密回调信息
     * @param timestamp
     * @param headerNonce
     * @param sign
     * @param body
     * @return
     * @throws Exception
     */
    public String decrypt(String timestamp, String headerNonce, String sign, String body) throws Exception {
        if (httpClient == null) {
            setup();
        }
        // 构建request，传入必要参数
        NotificationRequest request = new NotificationRequest.Builder().withSerialNumber(platformCertSerialNo)
                .withNonce(headerNonce)
                .withTimestamp(timestamp)
                .withSignature(sign)
                .withBody(body)
                .build();
        NotificationHandler handler = new NotificationHandler(verifier, v3Key.getBytes(StandardCharsets.UTF_8));
        // 验签和解析请求体
        Notification notification = handler.parse(request);
        Assert.assertNotNull(notification);

        Notification.Resource resource = notification.getResource();
        String getAssociatedData = "";
        if (resource.getAssociatedData() != null) {
            getAssociatedData = resource.getAssociatedData();
        }
        byte[] associatedData = getAssociatedData.getBytes(StandardCharsets.UTF_8);
        byte[] nonce = resource.getNonce().getBytes(StandardCharsets.UTF_8);
        String cipherText = resource.getCiphertext();
        AesUtil aesUtil = new AesUtil(v3Key.getBytes(StandardCharsets.UTF_8));
        String decryptData;
        try {
            decryptData = aesUtil.decryptToString(associatedData, nonce, cipherText);
        } catch (GeneralSecurityException e) {
            throw new ParseException("AES解密失败，resource：" + resource.toString(), e);
        }
        return decryptData;
    }

    /**
     * 使用流获取resoucre下的文件（解决线上无法获取的问题）
     * @param resourcePath 相对于resource目录的路径
     * @return
     * @throws IOException
     */
    public File copyTempFileByResourcePath(String resourcePath) throws IOException {
        InputStream inputStream = new FileInputStream(new File(resourcePath));
        File file = new File("/tmp/apiclient_key_temp.crt");
        FileUtils.copyInputStreamToFile(inputStream, file);
        return file;
    }
}

