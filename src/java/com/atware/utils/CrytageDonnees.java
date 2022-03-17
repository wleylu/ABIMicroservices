/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author yacou.kone
 */
public class CrytageDonnees {

    public CrytageDonnees() {
    }
    
    
    public static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    public static String encrypt(String dataToEncrypt, SecretKeySpec key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(dataToEncrypt.getBytes("UTF-8"));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(String string, SecretKeySpec key)  {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        String resultat = null;
        try {
            Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
            resultat = new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
                
        return resultat;
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }
    @SuppressWarnings("UnusedAssignment")
    public static String getInfoDonnes(String texte,int typeLecture,String login) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException, IOException{
       // String codepass= "jjjjjjj";
       
     
        byte[] salt = "80".getBytes();
        int iterationCount = 50000;
        int keyLength = 128;        
        String encryptedPassword= null;
        SecretKeySpec key = createSecretKey(login.toCharArray(), salt, iterationCount, keyLength);

//        String originalPassword = "123456789";
//        System.out.println("Original password: " + originalPassword);
//        String encryptedPassword = encrypt(originalPassword, key);
//        System.out.println("Encrypted password: " + encryptedPassword);
//        String decryptedPassword = decrypt(encryptedPassword, key);
//        System.out.println("Decrypted password: " + decryptedPassword);
      
        switch (typeLecture){
            case 0:
                encryptedPassword = encrypt(texte, key);
                break;
            case 1:
                encryptedPassword = decrypt(texte, key);
                break;
            default:
                encryptedPassword = encrypt(texte, key);
        }
              
        return encryptedPassword;
        
    }
    
    
    
}
