/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service.impl;

import com.shop.cereshop.app.alioss.service.PictureHandlerService;
import com.shop.cereshop.app.alioss.service.UploadService;
import com.shop.cereshop.app.config.ConstantProperties;
import com.shop.cereshop.commons.utils.ImageUtil;
import com.shop.cereshop.commons.utils.PressImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

@Service
public class PictureHandlerServiceImpl implements PictureHandlerService {

    private  String endpoint= ConstantProperties.ALIOSS_END_POINT;
    private  String accessKeyId= ConstantProperties.ALIOSS_ACCESS_KEY_ID;
    private  String accessKeySecret= ConstantProperties.ALIOSS_ACCESS_KEY_SECRET;
    private  String bucketName= ConstantProperties.ALIOSS_BUCKET_NAME;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private UploadService uploadService;


    @Override
    public void pressImages(String pressImg, String targetImg, int x, int y) {
        PressImageUtils.pressImage(pressImg,targetImg,x,y);
    }

    @Override
    public void pressText(String pressText, String targetImg, String fontName, int fontStyle, int color, int fontSize, int x, int y) {
        PressImageUtils.pressText(pressText,targetImg,fontName,fontStyle,color,fontSize,x,y);
    }

    public void zoomImg(String fileUrl,InputStream imgInputStream, float coefficient){
        try {
            InputStream in=new FileInputStream(new File(fileUrl));
            InputStream inputStream = ImageUtil.zoomImg(in, 0.2f);
            String filename=fileUrl.substring(fileUrl.lastIndexOf("/")+1);
            //上传到OSS
            uploadService.UploadFile(filename,inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void compressImg(String fileUrl,InputStream imgInputStream, float quality){
        try {
            InputStream in=new FileInputStream(new File(fileUrl));
            InputStream inputStream = ImageUtil.compressImg(in, 0.2f);
            String filename=fileUrl.substring(fileUrl.lastIndexOf("/")+1);
            //上传到OSS
            uploadService.UploadFile(filename,inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
