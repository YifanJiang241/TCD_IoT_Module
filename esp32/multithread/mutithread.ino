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
#define pinBuzzer  4                                                              //蜂鸣器引脚
int pinBuzzerFlag = -1;                                                           //蜂鸣器开关
//led
#define Led 13
int lightFlag = 1; 
//伺服电机
#include <Servo.h>
const int servoPin1 = 16;
double speed = 70.0;
double ke1 = 0.0;
uint8_t pos1 = 90;                    // go to position (degrees)
float ye1;
Servo myservo = Servo();
int LeaveFlag = 0;
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
  client.print("{\"deviceType\":\"allData\",\"temp\":\"" + String(0) + "\",\"hum\":\"" + String(0) + "\",\"LeaveFlag\":\"" + String(LeaveFlag) + "\",\"SleepFlag\":\"" + String(0) + "\",\"lightFlag\":\"" + String(lightFlag) + "\",\"Curtains\":\"" + String(0) + "\",\"lumValue\":\"" + String(0) + "\",\"Door\":\"" + String(LeaveFlag) + "\",\"pinBuzzerFlag\":\"" + String(0) + "\"}");
}
void setup() {
  Serial.begin(115200);
  delay(10);
  //灯
  pinMode(Led, OUTPUT);                                                            //LED端口设定成“输出”，关照led
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
}

void loop() {
  // put your main code here, to run repeatedly:
  temp = dht.readTemperature();
  hum = dht.readHumidity();
  //led灯是否关闭
  if(lightFlag){
    digitalWrite(Led, HIGH);
  }else{
    digitalWrite(Led, LOW);
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
        else if (jsonBuffer["deviceType"] == "turnOffLight" ) {                       //关闭led
          lightFlag = 0;
        }
        else if (jsonBuffer["deviceType"] == "turnOnLight" ) {                       //打开led
          lightFlag = 1;
        }
        else if (jsonBuffer["deviceType"] == "OpenDoor" ) {                       //打开门，伺服电机
          LeaveFlag = 1;
        }
        else if (jsonBuffer["deviceType"] == "CloseDoor" ) {                       //关闭门，伺服电机
          LeaveFlag = 0;
        } 
    }
  }
  delay(300);
}

void taskOne(void *parameter)
{ 
  while(1){
    //伺服电机是否转动
  if(LeaveFlag){
    speed = 70;
    pos1 = 90;
     ye1 = myservo.write(servoPin1, pos1, speed, ke1);
     delay(3000);
  }else{
    pos1 = 0;
    ye1 = myservo.write(servoPin1, pos1, speed, ke1);
    delay(3000);
    speed = 0;
  }
  
  delay(500);
  }
  
}
 
void taskTwo(void *parameter)
 
{
 
  for (int i = 0; i < 10; i++)
  {
 
    Serial.println("Hello from task 2");
 
    delay(1000);
  }
 
  Serial.println("Ending task 2");
 
  vTaskDelete(NULL);
}

