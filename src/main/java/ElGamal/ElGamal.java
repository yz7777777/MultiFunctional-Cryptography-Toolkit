package ElGamal;

import org.apache.commons.codec.binary.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.DHParameterSpec;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * EIGamal 非对称公钥加密算法　Bouncy Castle实现
 */
public class ElGamal {


    private static Map<Integer,String> keyMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        getKeyPair();
        String str = "Hello, Elgamal";
        getKeyPair();
        System.out.println("随机生成公钥: " + keyMap.get(0));
        System.out.println("随机生成私钥: " + keyMap.get(1));
        String encStr = encrypt(str, keyMap.get(0));
        String decStr = decrypt(encStr, keyMap.get(1));
        System.out.println("原始字符串: " + str);
        System.out.println("加密后的字符串: " + encStr);
        System.out.println("解密后的字符串: " + decStr);
    }

    /**
     * bouncy castle实现的ElGamal算法使用
     */

    static{
        //公钥加密，私钥解密
        Security.addProvider(new BouncyCastleProvider());
    }

    @SuppressWarnings({"all"})
    public static void getKeyPair() {
        KeyPair keyPair = null;
        try {
            //1.初始化密钥
            AlgorithmParameterGenerator algorithmParameterGenerator =
                    AlgorithmParameterGenerator.getInstance("ElGamal");
            algorithmParameterGenerator.init(256);
            AlgorithmParameters algorithmParameters = algorithmParameterGenerator.generateParameters();
            DHParameterSpec dhParameterSpec = (DHParameterSpec)algorithmParameters.getParameterSpec(DHParameterSpec.class);

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ElGamal");
            keyPairGenerator.initialize(dhParameterSpec,new SecureRandom());
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PublicKey elGamalPublicKey = keyPair.getPublic();
        PrivateKey elGamalPrivateKey = keyPair.getPrivate();

        String publicKeyString = Base64.encodeBase64String(elGamalPublicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(elGamalPrivateKey.getEncoded());

        keyMap.put(0,publicKeyString);        //0表示公钥
        keyMap.put(1,privateKeyString);       //1表示私钥
    }


    public static String encrypt(String str, String publicKeyString) throws Exception{
        //base64解码
        byte[] decoded = Base64.decodeBase64(publicKeyString);

        //公钥加密
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("ElGamal");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("ElGamal");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(str.getBytes());
        return Base64.encodeBase64String(encrypted);
    }

    public static String decrypt(String str, String privateKeyString) throws Exception{
        //base64解码
        byte[] decoded = Base64.decodeBase64(privateKeyString);

        //私钥解密
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("ElGamal");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decrypted = cipher.doFinal(Base64.decodeBase64(str));
        return new String(decrypted);
    }


//    public static void bcElGamal(){
//        try {
//
//
//
//            System.out.println("public key : " + Base64.encodeBase64String(elGamalPublicKey.getEncoded()));
//            System.out.println("private key : " + Base64.encodeBase64String(elGamalPrivateKey.getEncoded()));
//
//            //4.公钥加密，私钥解密--加密
//            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(elGamalPublicKey.getEncoded());
//            KeyFactory keyFactory = KeyFactory.getInstance("ElGamal");
//            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
//            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//            byte[] result = cipher.doFinal(src.getBytes());
//            System.out.println("公钥加密，私钥解密--加密　: " + Base64.encodeBase64String(result));
//
//            //5.公钥加密，私钥解密--解密
//            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(elGamalPrivateKey.getEncoded());
//            keyFactory = KeyFactory.getInstance("ElGamal");
//            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
//            cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//            cipher.init(Cipher.DECRYPT_MODE, privateKey);
//            result = cipher.doFinal(result);
//            System.out.println("公钥加密，私钥解密--解密 : " + new String(result));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}