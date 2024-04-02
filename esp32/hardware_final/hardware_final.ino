#include <ArduinoJson.h>//jSON库，用来解析后端服务器的JSO操控指令
#include <WiFi.h>
bool allFlag = false; // 或根据需要将初始值设置为true

const char* ssid     = "iQOO 11S";// 替换为你的Wi-Fi网络名称
const char* pswd = "Pedro0402";// 替换为你的Wi-Fi密码
const char* ServerAddress = "192.168.195.170";//网络调试助手设置的IP和端口，通过ipconfig获取电脑IP

const int ServerPort = 8089;                                                     //网络调试助手设置的端口号
WiFiClient client;
//温湿度，VCC接3v3，gnd接地，数据引脚sda接开发板P21，引脚scl接开发板P22
#include "DHT.h"
#define DHTPIN 23   // Digital pin connected to the DHT sensor
#define DHTTYPE DHT11 // DHT 11
DHT dht(DHTPIN, DHTTYPE);
float temp = 0;
float hum = 0;
//蜂鸣器
int pinBuzzerFlag = -1;                                                           //蜂鸣器开关
//led
#define Led 17
int lightFlag = 1; 
int SleepFlag = 0;
//光照
int lumSize[] = {476,395,302,255,159,20};
int lumValue = 476;
int lumI = 0;
//运动传感器
const int PIR_SENSOR_OUTPUT_PIN = 15;
int LeaveFlag = 0;
//伺服电机,门
#include <Servo.h>
const int servoPin = 16;
Servo myservo ;
int DoorFlag = 0;
bool DOOR = false;
//窗帘电机
Servo myservo1 ;
const int servoPin1 = 18;
int CurtainFlag = 0;
bool CURTAIN = false;
//WIFI连接函数
void WiFi_Connect()
{
  WiFi.begin(ssid,pswd);
  while (WiFi.status() != WL_CONNECTED)
  {                                 
    delay(300);
    Serial.print(".");
  }
}
//定时器传数据
#include <Ticker.h>
Ticker ticker;// 建立Ticker用于实现定时功能
void sendMes() {
  lumValue = lumSize[lumI%6];
  lumI++;
  client.print("{\"deviceType\":\"allData\",\"temp\":\"" + String(temp) + "\",\"hum\":\"" + String(hum) + "\",\"LeaveFlag\":\"" + String(LeaveFlag) + "\",\"SleepFlag\":\"" + String(SleepFlag) + "\",\"lightFlag\":\"" + String(lightFlag) + "\",\"Curtains\":\"" + String(CurtainFlag) + "\",\"lumValue\":\"" + String(lumValue) + "\",\"Door\":\"" + String(DoorFlag) + "\",\"pinBuzzerFlag\":\"" + String(pinBuzzerFlag) + "\"}");
}
void setup() {
  Serial.begin(115200);
  dht.begin();
  delay(10);
  
  //灯
  pinMode(Led, OUTPUT);                                                            //LED端口设定成“输出”，关照led
  //运动传感器
  pinMode(PIR_SENSOR_OUTPUT_PIN,INPUT);
  //电机
  myservo.attach(servoPin);
  delay(50);
  myservo1.attach(servoPin1);
  //wifi
  WiFi.mode(WIFI_STA);
  WiFi.setSleep(false);             //关闭STA模式下wifi休眠，提高响应速度,和蓝牙使用时不能关闭
  WiFi.begin(ssid, pswd);           //开始连接路由器wifi
  WiFi_Connect();
  WiFi.setAutoReconnect (true);     //Wifi设置函数,ture是真，假为false，setAutoReconnect自动重连
  if(client.connect(ServerAddress,ServerPort)){
    Serial.println("server connected");
  }
  ticker.attach_ms(2000, sendMes);  //定时器函数
  xTaskCreate(
    taskOne,   /* Task function. */
    "TaskOne", /* String with name of task. */
    3000,     /* Stack size in bytes. */
    NULL,      /* Parameter passed as input of the task */
    1,         /* Priority of the task. */
    NULL);     /* Task handle. */
 
  xTaskCreate(
    taskTwo,   /* Task function. */
    "TaskTwo", /* String with name of task. */
    3000,     /* Stack size in bytes. */
    NULL,      /* Parameter passed as input of the task */
    1,         /* Priority of the task. */
    NULL);     /* Task handle. */
  xTaskCreate(
    taskThree,   /* Task function. */
    "TaskThree", /* String with name of task. */
    3000,     /* Stack size in bytes. */
    NULL,      /* Parameter passed as input of the task */
    1,         /* Priority of the task. */
    NULL);     /* Task handle. */
}

void loop() {
  // put your main code here, to run repeatedly:
  temp = dht.readTemperature();
  hum = dht.readHumidity();
  //led灯是否关闭,睡眠模式灯关闭，电机开
  if(SleepFlag){
    allFlag = false;
    DoorFlag = 1;
    DOOR = true;
    CurtainFlag = 1;
    CURTAIN = true;
  }
  //自动模式，阈值关
  if(allFlag){
    if(lumValue > 300){
      digitalWrite(Led, LOW);
    }else{
      digitalWrite(Led, HIGH);
    }
  }else{
    if(lightFlag){
      digitalWrite(Led, HIGH);
    }else{
      digitalWrite(Led, LOW);
    }
  }
  if (client.available() > 0) {
    String comdata = client.readString();
    DynamicJsonDocument jsonBuffer(200);
    DeserializationError error = deserializeJson(jsonBuffer, comdata);
    //Serial.println(comdata);
    if (error) {
      //Serial.println(comdata);
      return;
    } else {
        String deviceType = jsonBuffer["deviceType"];
        if (deviceType == "allFlagClose" ) {                               //进入操作模式
          allFlag = false;
        }
        else if (deviceType == "allFlagTrue" ) {                           //进入自动模式
          allFlag = true;
        }
        else if (deviceType == "toLeave" ) {                       //离家
          LeaveFlag = 1;
        }
        else if (deviceType == "ExitLeave" ) {                       //回家
          LeaveFlag = 0;
        } 
        else if (deviceType == "toSleep" ) {                       //睡眠
          SleepFlag = 1;
        } 
        else if (deviceType == "ExitSleep" ) {                       //退出睡眠
          SleepFlag = 0;
        }
        else if (deviceType == "turnOffLight" ) {                       //关闭led
          lightFlag = 0;
        }
        else if (deviceType == "turnOnLight" ) {                       //打开led
          lightFlag = 1;
        }
        else if (deviceType == "OpenDoor" ) {                       //打开门，伺服电机
          DoorFlag = 1;
          DOOR = true;
        }
        else if (deviceType == "CloseDoor" ) {                       //关闭门，伺服电机
          DoorFlag = 0;
          DOOR = true;
        } 
        else if (deviceType == "OpenCurtains" ) {                       //打开窗帘，伺服电机
          CurtainFlag = 1;
          CURTAIN = true;
        }
        else if (deviceType == "CloseCurtains" ) {                       //关闭窗帘，伺服电机
          CurtainFlag = 0;
          CURTAIN = true;
        } 
        // else{
        //   continue;
        // }
    }
  }
  delay(500);
}

void taskOne(void *parameter)
{ 
  while(1){
    //伺服电机是否转动
    if(DOOR){
      myservo.attach(servoPin);
      if(DoorFlag){
        myservo.write(servoPin,90);
      }else{
        myservo.write(servoPin,0);
      }
      delay(2000);
      myservo.detach(servoPin);
    }
    DOOR = false;
    delay(500);
  }
}
//运动传感器
void taskTwo(void *parameter)
{
  while(1){
    if(LeaveFlag){
      int sensor_output;
      sensor_output = digitalRead(PIR_SENSOR_OUTPUT_PIN);
      if(sensor_output == LOW)
      {
        pinBuzzerFlag = 1;
      }
      else
      {
        pinBuzzerFlag = 0;
      }
    }
    delay(1000); 
  }
}
 //窗帘
void taskThree(void *parameter)
{
  while(1){
    //伺服电机是否转动
    if(CURTAIN){
      myservo1.attach(servoPin1);
      if(CurtainFlag){
        myservo1.write(servoPin1,90);
      }else{
        myservo1.write(servoPin1,0);
      }
      delay(2000);
      myservo1.detach(servoPin1);
    }
    CURTAIN = false;
    delay(500);
  }
}
 

