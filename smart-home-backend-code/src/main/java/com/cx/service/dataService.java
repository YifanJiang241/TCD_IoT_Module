package com.cx.service;


import com.cx.pojo.*;

import java.util.Date;

public interface dataService {
    int insertDeviceMes(DeviceMes deviceMes);
    DeviceMes[] selectDeviceAll(String deviceId, Date startTime, Date endTime);
    Login selectLoginMes(Login login);
    Login selectByAccount(String account);
    int insertLogin(Login login);
}
