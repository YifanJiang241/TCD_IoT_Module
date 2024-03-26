package com.cx.controller.Utils;

import java.util.Base64;

public class Base64Util {

    /***
     * BASE64加密
     * @param
     * @return
     * @throws Exception
     */
    public static String decryBASE64(String key) throws Exception{
        return  Base64.getEncoder().encodeToString(key.getBytes());
    }

    /***
     * BASE64解密
     * @param
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String encodedString) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes);
        return decodedString;
    }
}
