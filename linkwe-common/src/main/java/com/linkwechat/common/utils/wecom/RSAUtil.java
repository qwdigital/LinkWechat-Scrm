package com.linkwechat.common.utils.wecom;

import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Base64;

/**
 * @author sxw
 * @description
 * @date 2020/12/7 23:37
 **/
public class RSAUtil {
    /**
     * RSA pkcs1 2048bit 解密工具,
     * 获取私钥PrivateKey
     * @param privKeyPEM 2048bit pkcs1格式,base64编码后的RSA字符串
     * @return  PrivateKey,用于解密 decryptRSA
     * @throws IOException 异常
     * @throws NoSuchAlgorithmException 异常
     * @throws InvalidKeySpecException 异常
     */
    public static PrivateKey getPrivateKey(String privKeyPEM) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String privKeyPEMnew = privKeyPEM.replaceAll("\\r\\n", "")//看看保存到数据库的这个字符是不是有\r,如果没有,那就只需要替换掉"\\n"
                .replaceAll("\n", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "");
        byte[] bytes = java.util.Base64.getDecoder().decode(privKeyPEMnew);
        DerInputStream derReader = new DerInputStream(bytes);
        DerValue[] seq = derReader.getSequence(0);
        BigInteger modulus = seq[1].getBigInteger();
        BigInteger publicExp = seq[2].getBigInteger();
        BigInteger privateExp = seq[3].getBigInteger();
        BigInteger prime1 = seq[4].getBigInteger();
        BigInteger prime2 = seq[5].getBigInteger();
        BigInteger exp1 = seq[6].getBigInteger();
        BigInteger exp2 = seq[7].getBigInteger();
        BigInteger crtCoef = seq[8].getBigInteger();
        RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * RSA pkcs1 2048bit 解密工具,
     * @param str 被解密的字符串
     * @param privateKey 私钥对象 从 getPrivateKey 获取
     * @return 解密后数据
     * @throws NoSuchPaddingException 异常
     * @throws NoSuchAlgorithmException 异常
     * @throws InvalidKeyException 异常
     * @throws BadPaddingException 异常
     * @throws IllegalBlockSizeException 异常
     */
    public static  String decryptRSA(String str, PrivateKey privateKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] randomkeybyte = Base64.getDecoder().decode(str);
        byte[] finalrandomkeybyte = cipher.doFinal(randomkeybyte);
        return new String(finalrandomkeybyte);
    }
}
