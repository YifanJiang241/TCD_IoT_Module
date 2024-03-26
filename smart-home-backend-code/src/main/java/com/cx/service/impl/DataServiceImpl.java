package com.cx.service.impl;

import com.cx.mapper.DataMapper;
import com.cx.pojo.*;
import com.cx.service.dataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DataServiceImpl implements dataService {

    @Autowired
    private DataMapper dataMapper;


    @Override
    public int insertDeviceMes(DeviceMes deviceMes) {
        int i = dataMapper.insertDeviceMes(deviceMes);
        return i;
    }

    @Override
    public DeviceMes[] selectDeviceAll(String deviceId, Date startTime, Date endTime) {
        DeviceMes[] deviceMes = dataMapper.selectDeviceAll(deviceId, startTime, endTime);
        return deviceMes;
    }

    @Override
    public Login selectLoginMes(Login login) {
        Login mes = dataMapper.selectLogin(login);
        return mes;
    }

    @Override
    public Login selectByAccount(String account) {
        return dataMapper.selectByAccount(account);
    }

    @Override
    public int insertLogin(Login login) {
        return dataMapper.insertLogin(login);
    }


}
