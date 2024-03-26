package com.cx.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceMes {
    int id;
    String deviceType;
    float temp;     //温度值
    float hum;      //湿度值
    int Curtains;   //窗帘状态
    int lumValue;   //光照值
    int Door;       //自动门状态
    int lightFlag;  //灯光状态
    int pinBuzzerFlag; //蜂鸣器状态
    String nowTime;     //时间，字符串
    int LeaveFlag;//离家模式标识
    int SleepFlag;//睡眠模式标识
    Date nowTimeDate;
}
