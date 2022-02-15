package qs.common;

/**
 * 这是小海子团队开发的问答系统
 * Description             : 密码的RSA加密算法，实现密码从前端到后端的加密传输
 * JDK version used        : jdk1.8
 * Date                    : 2019.6
 * Author                  : liublack
 * Version                 : 1
 * Package                 : qs.servlet
 * Copy Right Information  : Copyright  2019 liublack. All rights reserved.
 **/

import javax.crypto.Cipher;
import java.io.*;
import java.util.*;
import java.security.*;
import java.security.spec.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**g
 * 实现数据的加密与解密（使用RSA算法）
 */
public class RSAUtil {

//    private static final String KEY_DIR = "/Users/pengfei/Desktop/javaweb/";
    private static final String KEY_DIR = "/home/liublack/";            // 存放公私钥的文件夹路径
    private static final String PUBLIC_KEY_FNAME =  "public.pem";       // 存放公钥的文件名
    private static final String PRIVATE_KEY_FNAME = "private.pem";      // 存放私钥的文件名
    private static HashMap<String,Key> keys = null;
    private static Cipher cipher = null;

    static {
        try {
            cipher = Cipher.getInstance("RSA"); // RSA/ECB/NoPadding
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static HashMap<String, Key> getKeys(){
        return keys;
    }



    /**
     * 检测是否已经存在密钥
     * @return true 如果公钥文件私钥文件均存在
     */
    public static boolean hasKey(){
        File privateKey = new File(KEY_DIR + PRIVATE_KEY_FNAME);
        File publicKey = new File(KEY_DIR + PUBLIC_KEY_FNAME);
        return privateKey.exists() && publicKey.exists();
    }

    /**
     * 得到公密钥字符串（经过base64编码）
     *
     * @return
     */
    public static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(keyBytes);
    }


    /**
     *  在指定目录中生成密钥
     * @return true 如果生成密钥成功
     */
    public static boolean generateKey() {
        if(hasKey()) return true;
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);    // 指定生成1024位密钥
            KeyPair keyPair = keyPairGen.generateKeyPair();
            PublicKey publicKey = (RSAPublicKey)keyPair.getPublic();    // 公钥
            PrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();// 密钥

            /* 创建文件，存储公私钥对象 */
            ObjectOutputStream publicOut = new ObjectOutputStream(new FileOutputStream(KEY_DIR + PUBLIC_KEY_FNAME));
            ObjectOutputStream privateOut = new ObjectOutputStream(new FileOutputStream(KEY_DIR + PRIVATE_KEY_FNAME));
            publicOut.writeObject(publicKey);
            privateOut.writeObject(privateKey);
            publicOut.flush();
            publicOut.close();
            privateOut.flush();
            privateOut.close();
            keys = new HashMap<String,Key>();
            keys.put("publicKey", publicKey);
            keys.put("privateKey",privateKey);

        }catch (Exception e){
            e.printStackTrace();;
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 从文件中加载公私钥
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static void loadKey() throws FileNotFoundException, IOException,ClassNotFoundException{

        ObjectInputStream publicIn = new ObjectInputStream(new FileInputStream(KEY_DIR + PUBLIC_KEY_FNAME));
        ObjectInputStream privateIn = new ObjectInputStream(new FileInputStream(KEY_DIR + PRIVATE_KEY_FNAME));
        PublicKey publicKey = (PublicKey) publicIn.readObject();
        PrivateKey privateKey = (PrivateKey) privateIn.readObject();

        keys = new HashMap<String,Key>();
        keys.put("publicKey", publicKey);
        keys.put("privateKey", privateKey);
    }

    /**
     * 使用公钥对明文进行加密，返回BASE64编码的字符串
     * @param plainText
     * @return 加密后的字符串
     */
    public static String encrypt(String plainText){
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keys.get("publicKey"));
            byte[] enBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(enBytes);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 使用私钥进行解密
     * @param plainText
     * @return
     */
    public static String decrypt(String plainText){
        try{
            cipher.init(Cipher.DECRYPT_MODE, keys.get("privateKey"));
            byte[] deBytes = cipher.doFinal(Base64.getDecoder().decode(plainText));
            return new String(deBytes, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }
}
