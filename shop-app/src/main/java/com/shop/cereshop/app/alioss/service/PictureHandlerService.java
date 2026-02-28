/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.service;

import java.io.InputStream;

public interface PictureHandlerService {

    /**
     * 图片加水印图片
     * @param pressImg -- 水印文件
     * @param targetImg -- 目标文件
     * @param x  水平边距, 就是距离图片边缘的水平距离， 这个参数只有当水印位置是左上，左中，左下， 右上，右中，右下才有意义
     * @param y  垂直边距, 就是距离图片边缘的垂直距离， 这个参数只有当水印位置是左上，中上， 右上，左下，中下，右下才有意义
     */
    public void pressImages(String pressImg, String targetImg, int x, int y);

    /**
     * 打印文字水印图片
     * @param pressText --文字
     * @param targetImg -- 目标图片
     * @param fontName -- 字体名
     * @param fontStyle -- 字体样式
     * @param color -- 字体颜色
     * @param fontSize -- 字体大小
     * @param x -- 偏移量
     * @param y
     */
    public void pressText(String pressText, String targetImg, String fontName, int fontStyle, int color, int fontSize, int x, int y);

    /**
     * 按照指定比例进行缩小和放大
     * @param fileUrl 目标文件地址
     * @param imgInputStream InputStream
     * @param coefficient    缩放系数,coefficient大于1为放大，小于1且大于0为缩小
     * @return InputStream
     */
    public void zoomImg(String fileUrl, InputStream imgInputStream, float coefficient);

    /**
     * 压缩图片文件大小,图片尺寸不变
     * @param fileUrl 目标文件地址
     * @param imgInputStream InputStream
     * @param quality        图片质量
     * @return InputStream
     */
    public void compressImg(String fileUrl, InputStream imgInputStream, float quality);
}
