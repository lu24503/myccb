/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shop.cereshop.app.param.index.LoginPhoneParam;
import com.shop.cereshop.commons.utils.EmptyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.*;

/**
 * @ClassName WechatUtil
 * @Description TODO
 * @Author eval
 * @Date 9:44 2019/3/20
 * @Version 1.0
 */
@Slf4j(topic = "WechatUtil")
@Component
public class WechatUtil {

    /**
     * 支付小程序appid
     */
    private static String appId;

    /**
     * 支付小程序秘钥
     */
    private static String secret;

    @Value("${weixin.appid}")
    public void setAppId(String appId){
        this.appId = appId;
    }

    @Value("${weixin.secret}")
    public void setSecret(String secret){
        this.secret = secret;
    }

    /**
     * 获取小程序codeid换取openid
     * @param code
     * @return
     */
    public static JSONObject getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+ appId +"&secret=" +
                secret +"&js_code="+code+"&grant_type=authorization_code";
        PrintWriter out = null;
        BufferedReader in = null;
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            // 设置通用的请求属性 设置请求格式
            //设置返回类型
            conn.setRequestProperty("contentType", "text/plain");
            //设置请求类型
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //设置超时时间
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.setDoOutput(true);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应    设置接收格式
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = JSONObject.parseObject(stringBuffer.toString());
            //log.info("jsonObject:" + JSON.toJSONString(jsonObject));
            log.info("getOpenId:{}", jsonObject.toString());
            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                log.error(ex.getMessage(), ex);
            }
        }
        return null;
    }

    /**
     * 获取小程序token
     * @return
     */
    public static String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?" +
                "appid="+ appId + "&secret=" + secret + "&grant_type=client_credential";
        PrintWriter out = null;
        BufferedReader in = null;
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            // 设置通用的请求属性 设置请求格式
            //设置返回类型
            conn.setRequestProperty("contentType", "text/plain");
            //设置请求类型
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //设置超时时间
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.setDoOutput(true);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应    设置接收格式
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = JSONObject.parseObject(stringBuffer.toString());
            return jsonObject.getString("access_token");

        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取小程序token和openid
     * @param code
     * @return
     */
    public static JSONObject getAccessTokenOrOpenid(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid="+ appId +"&secret="+ secret +"&code="+code+"&grant_type=authorization_code";
        PrintWriter out = null;
        BufferedReader in = null;
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            // 设置通用的请求属性 设置请求格式
            //设置返回类型
            conn.setRequestProperty("contentType", "text/plain");
            //设置请求类型
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //设置超时时间
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.setDoOutput(true);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应    设置接收格式
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = JSONObject.parseObject(stringBuffer.toString());
            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取小程序unionId
     * @return
     */
    public static String getUnionId(String accessToken,String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openid;
        PrintWriter out = null;
        BufferedReader in = null;
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            // 设置通用的请求属性 设置请求格式
            //设置返回类型
            conn.setRequestProperty("contentType", "text/plain");
            //设置请求类型
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //设置超时时间
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.setDoOutput(true);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应    设置接收格式
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = JSONObject.parseObject(stringBuffer.toString());
            return jsonObject.get("unionId").toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取微信小程序 session_key 和 openid
     *
     * @param code 调用微信登陆返回的Code
     * @return
     */
    public static JSONObject getSessionKeyOropenid(String code) {
        //微信端登录code值
        String wxCode = code;
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";  //请求地址 https://api.weixin.qq.com/sns/jscode2session
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("appid", appId);  //开发者设置中的appId
        requestUrlParam.put("secret", secret); //开发者设置中的appSecret
        requestUrlParam.put("js_code", wxCode); //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code");    //默认参数 authorization_code

        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject(sendPost(requestUrl, requestUrlParam));
        return jsonObject;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = "";
        Iterator<String> it = paramMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            param += key + "=" + paramMap.get(key) + "&";
        }

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static BufferedImage getWxacode(String accessToken, String path) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("path", path);
        paramMap.put("width", "120");
        String Json = JSONObject.toJSONString(paramMap);
        BufferedImage qrCodeImage = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("https://api.weixin.qq.com/wxa/getwxacode?access_token=" + accessToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestProperty("accept","*/*");
            if (!TextUtils.isEmpty(Json)) {
                byte[] writebytes = Json.getBytes();
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(Json.getBytes());
                outwritestream.flush();
                outwritestream.close();
                conn.getResponseCode();
            }
            if (conn.getResponseCode() == 200) {
                qrCodeImage = ImageIO.read(conn.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return qrCodeImage;
    }

    public static Map<String, Object> getPhoneNumber(LoginPhoneParam vo) {
        Map<String,Object> map=new HashMap<>();
        String openid= vo.getWechatOpenId();
        String session_key = vo.getSessionKey();
        if (!EmptyUtils.isEmpty(openid)) {

            if(EmptyUtils.isEmpty(session_key)){
                return null;
            }
            map.put("openid",openid);
            // 被加密的数据
            byte[] dataByte = Base64.getDecoder().decode(vo.getEncryptedData());
            // 加密秘钥
            byte[] keyByte = Base64.getDecoder().decode(session_key);
            // 偏移量
            byte[] ivByte = Base64.getDecoder().decode(vo.getIv());
            try {
                // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
                int base = 16;
                if (keyByte.length % base != 0) {
                    int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                    byte[] temp = new byte[groups * base];
                    Arrays.fill(temp, (byte) 0);
                    System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                    keyByte = temp;
                }
                // 初始化
                Security.addProvider(new BouncyCastleProvider());
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
                AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
                IvParameterSpec ivParameterSpec=new IvParameterSpec(ivByte);
                parameters.init(ivParameterSpec);
                cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
                byte[] resultByte = cipher.doFinal(dataByte);
                if (null != resultByte && resultByte.length > 0) {
                    String result = new String(resultByte, "UTF-8");
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    map.put("param",jsonObject);
                    return map;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
