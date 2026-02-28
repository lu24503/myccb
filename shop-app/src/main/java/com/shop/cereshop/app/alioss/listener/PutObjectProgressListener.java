/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.alioss.listener;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;

public class PutObjectProgressListener implements ProgressListener {
    private long bytesWritten = 0;
    private long totalBytes = -1;
    private boolean succeed = false;
    private long total;


    public PutObjectProgressListener(HttpServletRequest request, long total){
        this.total=total;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                System.out.println("Start to upload......");
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                if (this.totalBytes != -1) {
                    int percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
                    System.out.println(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                } else {
                    update(bytesWritten,total);
                    System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                break;
            case TRANSFER_FAILED_EVENT:
                System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                break;
            default:
                break;
        }
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void update(long pBytesRead, long total) {
        //将数据进行格式化
        //已读取数据由字节转换为M
        double readM=pBytesRead/1024.0/1024.0;
        //已读取数据由字节转换为M
        double totalM=total/1024.0/1024.0;
        //已读取百分百
        double percent=readM/totalM;
        //格式化数据
        //已读取
        String readf=dataFormat(pBytesRead);
        //总大小
        String totalf=dataFormat(total);
        //进度百分百
        NumberFormat format=NumberFormat.getPercentInstance();
        String progress=format.format(percent);

        //将信息存入session
//        session.setAttribute("progress", progress);
        //将session存入redis
//        stringRedisService.set("progress",progress);



        //打印消息到控制台
        System.out.println("pBytesRead===>"+pBytesRead);
        System.out.println("total==>"+total);
//        System.out.println("pItems===>"+pItems);
        System.out.println("readf--->"+readf);
        System.out.println("totalf--->"+totalf);
        System.out.println("progress--->"+progress);
    }
    /**
     * 格式化读取数据的显示
     * @param data 要格式化的数据 单位byte
     * @return 格式化后的数据，如果小于1M显示单位为KB，如果大于1M显示单位为M
     */
    public String dataFormat(double data){
        String formdata="";
        if (data>=1024*1024) {//大于等于1M
            formdata=Double.toString(data/1024/1024)+"M";
        }else if(data>=1024){//大于等于1KB
            formdata=Double.toString(data/1024)+"KB";
        }else{//小于1KB
            formdata=Double.toString(data)+"byte";
        }
        return formdata.substring(0, formdata.indexOf(".")+2);
    }
}
