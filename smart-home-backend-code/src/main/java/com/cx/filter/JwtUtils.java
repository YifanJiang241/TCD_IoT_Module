package com.cx.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Map;

public class JwtUtils {



    //签名
    public static final String SING="loginToken";

    /**
     * 生成token header.payload.sing
     */

    public static String getToken(Map<String,String> map) throws UnsupportedEncodingException {
        Calendar instance = Calendar.getInstance();
//        instance.add(Calendar.SECOND,20);  //默认20秒过期
        instance.add(Calendar.DATE,7);  //默认7天过期

        //创建jwtbuilder
        JWTCreator.Builder builder = JWT.create();
        //payload
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        //sign签名
        String token = builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SING));

      /*  String token = JWT.create()
                .withClaim("loginName", user.getLoginName())
                .withClaim("password", user.getPassword())
                .withExpiresAt(instance.getTime())  //指定令牌过期时间
                .sign(Algorithm.HMAC256(SING));         //签名*/
        return token;
    }

    /**
     * 验证token的合法性
     */
    public static DecodedJWT verify(String token) throws UnsupportedEncodingException {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    /**
     * 获取token信息的方法
     */
   /* public static DecodedJWT getTOkenInfo(String token) throws UnsupportedEncodingException {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        return verify;
    }*/
}

