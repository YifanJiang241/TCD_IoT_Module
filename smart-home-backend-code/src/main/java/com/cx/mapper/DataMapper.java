package com.cx.mapper;

//import cn.hutool.log.Log;
import com.cx.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Mapper
public interface DataMapper {
    int insertDeviceMes(DeviceMes deviceMes);
    DeviceMes[] selectDeviceAll(@Param(value = "deviceId") String deviceId, @Param(value = "startTime") Date startTime, @Param(value = "endTime") Date endTime);
    Login selectLogin(Login login);
    Login selectByAccount(@Param(value = "account")String account);
    int insertLogin(Login login);
}
