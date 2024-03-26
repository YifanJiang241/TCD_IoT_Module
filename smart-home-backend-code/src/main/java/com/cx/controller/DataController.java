package com.cx.controller;

import com.cx.controller.Utils.Base64Util;
import com.cx.filter.JwtUtils;
import com.cx.pojo.DeviceMes;
import com.cx.pojo.Login;
import com.cx.pojo.ToMes;
import com.cx.service.impl.DataServiceImpl;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/allData")
public class DataController {
    @Autowired
    private DataServiceImpl dataService;

    @PostMapping("/getDeviceAll")
    public DeviceMes[] selectAll(@RequestParam Map<String, Object> params) throws ParseException {
        System.out.println(params);
        String deviceId=null;
        deviceId= String.valueOf(params.get("deviceId"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime=dateFormat.parse(params.get("startTime").toString());
        Date endTime=dateFormat.parse(params.get("endTime").toString());
        DeviceMes[] text02BookMes = dataService.selectDeviceAll(deviceId,startTime,endTime);
        return text02BookMes;
    }
    /**
     * 登陆
     * **/
    @PostMapping("/login/loginMsg")
    public String selectLogin(@RequestParam Map<String,Object> params) throws Exception {
        String account=null;
        String password=null;
        if(params.get("account")!=null){
            account= String.valueOf(params.get("account"));
        }
        if (params.get("password")!=null) {
            password= String.valueOf(params.get("password"));
        }
        Login login = new Login();
        login.setAccount(account);
        login.setPassword(Base64Util.decryBASE64(password));
        System.out.println(login);
        Login login1 = dataService.selectLoginMes(login);
        if (login1 != null){
            Map<String, String> payload = new HashMap<>();
            //生成jwt令牌
            payload.put("loginName","123");
            payload.put("password", "232");
            String token = null;
            try {
                token = JwtUtils.getToken(payload);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            return token;
        }else{
            return "fail";
        }
    }
    /**
     * 加入用户
     * */
    @PostMapping("/login/insertUser")
    public String insertUser(
            @RequestParam("userName") String userName,
            @RequestParam("userPassword") String userPassword) throws Exception {
        if (dataService.selectByAccount(userName) != null){
            return "false";
        }
        Login userData = new Login();
        userData.setAccount(userName);
        //加密存储
        userData.setPassword(Base64Util.decryBASE64(userPassword));
       val i = dataService.insertLogin(userData);
       if (i > 0) {
           Map<String, String> payload = new HashMap<>();
            //生成jwt令牌
            payload.put("loginName","123");
            payload.put("password", "232");
            String token = null;
            try {
                token = JwtUtils.getToken(payload);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            return token;
      }else return "false";
    }
    @RequestMapping("/test/check")
    public String check(){
        return "登陆成功";
    }

}
