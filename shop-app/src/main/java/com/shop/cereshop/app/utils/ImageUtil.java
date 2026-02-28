package com.shop.cereshop.app.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage;

public class ImageUtil {

    public static String genQrCodeBase64ByUrl(String url) throws IOException, WriterException {
        //生成二维码图片
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 设置纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        // 编码类型
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
        MatrixToImageConfig config = new MatrixToImageConfig();
        BufferedImage image = toBufferedImage(bitMatrix, config);
        return genQrCodeBase64ByBufferedImage(image);
    }

    public static String genQrCodeBase64ByBufferedImage(BufferedImage image) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", stream);
        byte[] bytes = Base64.getEncoder().encode(stream.toByteArray());
        String base64 = new String(bytes, StandardCharsets.UTF_8);
        stream.flush();
        stream.close();
        return "data:image/png;base64,"+base64;
    }

}
