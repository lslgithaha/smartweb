package com.lsl.smartweb.fileup;

import com.lsl.smartweb.utils.Util;

import java.text.DecimalFormat;

/**
 * Create by LSL on 2018\6\25 0025
 * 描述：上传状态保存
 * 版本：1.0.0
 */
public class UpStatus {
    private DecimalFormat df = new DecimalFormat("#.00");
    /**
     *  进度百分比
     */
    private double percentage;
    /**
     *  已完成数
     */
    private long uploaded;
    /**
     *  文件总长度
     */
    private long totalSize;
    /**
     *  传输速率
     */
    private double speed;
    /**
     *  已用时间
     */
    private int takeTime;
    /**
     *  估计总时间
     */
    private int totalTimne;
    /**
     *  估计剩余时间
     */
    private int remainingTime;
    /**
     *  正在上传第几个文件
     */
    private int filenum;
    /**
     *  总共几个文件
     */
    private int filenums;

    /**
     *  开始时间
     */
    private long startTime;


    /**
     *  上次更新时间
     */
    private long lastTime;

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public long getUploaded() {
        return uploaded;
    }

    public void setUploaded(long uploaded) {
        long now=System.currentTimeMillis();
        this.takeTime=(int) ((now-startTime)/1000);//计算花了多时间
        if(uploaded !=0 && totalSize!=0) {
            this.percentage = Double.valueOf(df.format(uploaded * 100 / (double)totalSize));
            this.totalTimne = (int)(takeTime*100/percentage);//预估总时间
        }
        this.remainingTime=(totalTimne-takeTime);//剩余时间
        this.speed = (uploaded-this.uploaded)/(now-this.lastTime+1);//kb/s
        this.lastTime = now;
        this.uploaded = uploaded;
//        Pro_webSocket.send(Util.toJson(this));
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(int takeTime) {
        this.takeTime = takeTime;
    }

    public int getTotalTimne() {
        return totalTimne;
    }

    public void setTotalTimne(int totalTimne) {
        this.totalTimne = totalTimne;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getFilenum() {
        return filenum;
    }

    public void setFilenum(int filenum) {
        this.filenum = filenum;
    }

    public int getFilenums() {
        return filenums;
    }

    public void setFilenums(int filenums) {
        this.filenums = filenums;
    }

    @Override
    public String toString() {
        return "";
    }
}
