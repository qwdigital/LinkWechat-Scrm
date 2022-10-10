package com.linkwechat.common.utils.wecom;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * @author danmo
 * @description
 * @date 2020/12/7 23:37
 **/
public class RSAUtil {
    static{
        try{
            Security.addProvider(new BouncyCastleProvider());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * RSA pkcs1 2048bit 解密工具,
     * 获取私钥PrivateKey
     *
     * @param privKeyPEM 2048bit pkcs1格式,base64编码后的RSA字符串
     * @return PrivateKey, 用于解密 decryptRSA
     * @throws IOException 异常
     */
    public static PrivateKey getPrivateKey(String privKeyPEM) throws IOException {
        PrivateKey privateKey = null;
        Reader privateKeyReader = new StringReader(privKeyPEM);
        PEMParser privatePemParser = new PEMParser(privateKeyReader);
        Object privateObject = privatePemParser.readObject();
        if (privateObject instanceof PEMKeyPair) {
            PEMKeyPair pemKeyPair = (PEMKeyPair) privateObject;
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            privateKey = converter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
        }
        return privateKey;
    }

    /**
     * RSA pkcs1 2048bit 解密工具,
     *
     * @param str        被解密的字符串
     * @param privateKey 私钥对象 从 getPrivateKey 获取
     * @return 解密后数据
     * @throws NoSuchPaddingException    异常
     * @throws NoSuchAlgorithmException  异常
     * @throws InvalidKeyException       异常
     * @throws BadPaddingException       异常
     * @throws IllegalBlockSizeException 异常
     */
    public static String decryptRSA(String str, PrivateKey privateKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        rsa.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] utf8 = rsa.doFinal(Base64.getDecoder().decode(str));
        return new String(utf8, StandardCharsets.UTF_8);
    }
}
