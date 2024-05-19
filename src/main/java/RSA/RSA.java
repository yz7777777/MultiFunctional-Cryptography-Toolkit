package RSA;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


public class RSA {
    private static Map<Integer,String> keyMap = new HashMap<>();


    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        KeyPair keyPair = getKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String publicKeyString=new String(Base64.encodeBase64(publicKey.getEncoded()));
        String privateKeyString=new String(Base64.encodeBase64(privateKey.getEncoded()));

        //加密字符串
        String str="Hello RSA";
        System.out.println("随机生成的公钥：" + publicKeyString);
        System.out.println("随机生成的私钥：" + privateKeyString);
        String encStr = encrypt(str,publicKeyString);
        System.out.println(str);
        System.out.println("加密后的字符串：" + encStr);
        String decStr = decrypt(encStr,privateKeyString);
        System.out.println("解密后的字符串："+ decStr);

        String str2 = "Hello Signature";
        String signature = sign(str2, privateKeyString);
        System.out.println("RSA签名: " + signature);
        System.out.println(verify(str2, publicKeyString, signature));
    }

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair getKeyPair() throws Exception {
        //KeyPairGenerator类用于生成公钥和密钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        //初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(1024,new SecureRandom());
        //生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
//        PrivateKey privateKey = keyPair.getPrivate();//得到私钥
//        PublicKey publicKey = keyPair.getPublic();//得到公钥
//        //得到公钥字符串
//        String publicKeyString=new String(Base64.encodeBase64(publicKey.getEncoded()));
//        //得到私钥字符串
//        String privateKeyString=new String(Base64.encodeBase64(privateKey.getEncoded()));
//        //将公钥和私钥保存到Map
//        keyMap.put(0,publicKeyString);//0表示公钥
//        keyMap.put(1,privateKeyString);//1表示私钥
    }




    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt(String str,String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey= (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,pubKey);
        String outStr=Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }


    public static String decrypt(String str,String privateKey) throws Exception {
        //Base64解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //Base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,priKey);
        String outStr=new String(cipher.doFinal(inputByte));
        return outStr;
    }


    public static String sign(String data, String privateKey) {
        try {
            // 入参数据body字节数组
            byte[] dataBytes = data.getBytes();
            // 获取私钥秘钥字节数组
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            // 使用给定的编码密钥创建一个新的PKCS8EncodedKeySpec。
            // PKCS8EncodedKeySpec 是 PKCS#8标准作为密钥规范管理的编码格式
            // 实例化KeyFactory,指定为加密算法 为 RSA
            // 获得PrivateKey对象
            PrivateKey privateKey1= KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
            // 用私钥对信息生成数字签名，指定签名算法为 MD5withRSA
            Signature signature = Signature.getInstance("MD5withRSA");
            // 初始化签名
            signature.initSign(privateKey1);
            // 数据body带入
            signature.update(dataBytes);
            // 对签名进行Base64编码
            return Base64.encodeBase64String(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verify(String data, String publicKey, String sign) {
        try {
            // 入参数据body字节数组
            byte[] dataBytes = data.getBytes("UTF-8");
            // 获取公钥秘钥字节数组
            byte[] keyBytes = Base64.decodeBase64(publicKey);
            // 使用给定的编码密钥创建一个新的X509EncodedKeySpec
            // X509EncodedKeySpec是基于X.509证书提前的公钥，一种java秘钥规范
            // 实例化KeyFactory,指定为加密算法 为 RSA
            // 获取publicKey对象
            PublicKey publicKey1 = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
            // 用私钥对信息生成数字签名，指定签名算法为 MD5withRSA
            Signature signature = Signature.getInstance("MD5withRSA");
            // 带入公钥进行验证
            signature.initVerify(publicKey1);
            // 数据body带入
            signature.update(dataBytes);
            // 验证签名
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }





}
