#include <ArduinoJson.h>//jSON库，用来解析后端服务器的JSO操控指令
#include <WiFi.h>
bool allFlag = false; // 或根据需要将初始值设置为true


const char* ssid = "iQOO 11S";                                                        //路由器的ID和密码
const char* pswd = "Pedro0402";
const char * ServerAddress = "172.18.192.1";                                   //网络调试助手设置的IP和端口，通过ipconfig获取电脑IP
const int ServerPort = 8089;                                                     //网络调试助手设置的端口号
WiFiClient client;
//温湿度，VCC接3v3，gnd接地，数据引脚sda接开发板P21，引脚scl接开发板P22
#include <Wire.h>
#include "Adafruit_SHT31.h"
Adafruit_SHT31 sht31 = Adafruit_SHT31();
float temp = 0;
float hum = 0;
//蜂鸣器
#define pinBuzzer  4                                                              //蜂鸣器引脚
int pinBuzzerFlag = -1;                                                           //蜂鸣器开关
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
// void sendMes() {
//   client.print("{\"deviceType\":\"allData\",\"temp\":\"" + temp + "\",\"hum\":\"" + hum + "\",\"Curtains\":\"" + 1 + "\",\"lumValue\":\"" + 100 + "\",\"Door\":\"" + 1 + "\",\"pinBuzzerFlag\":\"" + pinBuzzerFlag + "\"}");
// }

void sendMes() {
  String message = "{\"deviceType\":\"allData\",\"temp\":\"" + String(temp) + "\",\"hum\":\"" + String(hum) + "\",\"Curtains\":\"" + String(1) + "\",\"lumValue\":\"" + String(100) + "\",\"Door\":\"" + String(1) + "\",\"pinBuzzerFlag\":\"" + String(pinBuzzerFlag) + "\"}";
  client.print(message);
}


void setup() {
  Serial.begin(115200);
  delay(10);
  // 温度
  sht31.begin(0x44);
  //wifi
  WiFi.mode(WIFI_STA);
  WiFi.setSleep(false);             //关闭STA模式下wifi休眠，提高响应速度,和蓝牙使用时不能关闭
  WiFi.begin(ssid, pswd);           //开始连接路由器wifi
  WiFi_Connect();
  WiFi.setAutoReconnect (true);     //Wifi设置函数,ture是真，假为false，setAutoReconnect自动重连
  client.connect(ServerAddress, ServerPort);
  ticker.attach_ms(2000, sendMes);  //定时器函数
  //线程创建
  xTaskCreate(
    taskOne,   /* Task function. */
    "TaskOne", /* String with name of task. */
    4096,     /* Stack size in bytes. */
    NULL,      /* Parameter passed as input of the task */
    1,         /* Priority of the task. */
    NULL);     /* Task handle. */

  xTaskCreate(
    taskTwo,   /* Task function. */
    "TaskTwo", /* String with name of task. */
    4096,     /* Stack size in bytes. */
    NULL,      /* Parameter passed as input of the task */
    1,         /* Priority of the task. */
    NULL);     /* Task handle. */
}

void loop() {
  // put your main code here, to run repeatedly:
  temp = sht31.readTemperature();
  hum = sht31.readHumidity();
  if (client.available() > 0) {
    String comdata = client.readString();
    DynamicJsonDocument jsonBuffer(200);
    DeserializationError error = deserializeJson(jsonBuffer, comdata);
    //Serial.println(comdata);
    if (error) {
      //Serial.println(comdata);
      return;
    } else {
        if (jsonBuffer["deviceType"] == "allFlagClose" ) {                               //进入操作模式
          allFlag = false;
        }
        else if (jsonBuffer["deviceType"] == "allFlagTrue" ) {                           //进入自动模式
          allFlag = true;
        }
        else if (jsonBuffer["deviceType"] == "simulation" ) {                            //打开蜂鸣器
          //digitalWrite(pinBuzzer, HIGH);
          pinBuzzerFlag = 1;
        }
        else if (jsonBuffer["deviceType"] == "simulationClose" ) {                       //关闭蜂鸣器
          //digitalWrite(pinBuzzer, LOW);
          pinBuzzerFlag = -1;
        }
    }
  }
  delay(300);
}
void taskOne(void *parameter)
{
  if (pinBuzzerFlag > 0) {
    digitalWrite(pinBuzzer, HIGH);
  } else {
    digitalWrite(pinBuzzer, LOW);
  }
  delay(300);
}
 
void taskTwo(void *parameter)
{
 
  delay(300);
}

