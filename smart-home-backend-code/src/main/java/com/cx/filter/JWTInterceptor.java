package com.cx.filter;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 拦截器
 */
public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Map<String, Object> map = new HashMap<>();
        //获取请求头中的令牌
        String token = request.getHeader("token");
        try {
            // 验证token
            DecodedJWT verify = JwtUtils.verify(token);
            return true;  //放行请求
            /*map.put("status",true);
            map.put("msg","请求成功");*/
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("msg","无效签名");
        }catch (TokenExpiredException e){
            map.put("msg","token已过期");
        }catch (AlgorithmMismatchException e){
            map.put("msg","token算法不一致");
        }catch (Exception e){
            map.put("status","token无效");
        }
        map.put("status",false);   // 设置状态
        //将map转为json  jackson
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
        return false;
    }
}

