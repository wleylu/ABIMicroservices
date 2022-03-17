/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.utils;

/**
 *
 * @author Melarga.COULIBALY
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


 public class MD5Password
 {
 /*
# * Encode la chaine passé en paramètre avec l'algorithme MD5
# *
# * @param key : la chaine à encoder
# *
# * @return la valeur (string) hexadécimale sur 32 bits
# */
 public static String getEncodedPassword(String key) {
     byte[] uniqueKey = key.getBytes();
     byte[] hash = null;
     try {
        hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("no MD5 support in this VM");
     }
     StringBuilder hashString = new StringBuilder();
     for ( int i = 0; i < hash.length; ++i ) {
     String hex = Integer.toHexString(hash[i]);
     if ( hex.length() == 1 ) {
     hashString.append('0');
     hashString.append(hex.charAt(hex.length()-1));
     } else {
     hashString.append(hex.substring(hex.length()-2));
     }
 }
 return hashString.toString();
 }
 
 public static String getMD5(String key) {
  return  getEncodedPassword(key);
 }

 /*
 * Test une chaine et une valeur encodé (chaine hexadécimale)
 *
 * @param clearTextTestPassword : la chaine non codé à tester
 * @param encodedActualPassword : la valeur hexa MD5 de référence
 *
 * @return true si vérifié false sinon
 */
 public static boolean testPassword(String clearTextTestPassword, String encodedActualPassword) throws NoSuchAlgorithmException
 {
    String encodedTestPassword = MD5Password.getEncodedPassword(clearTextTestPassword);
    return (encodedTestPassword.equals(encodedActualPassword));
 }

 /*
 * Un exemple bête d'utilisation
 
 public static void main(String[] args) {
     System.out.println(getEncodedPassword("mot de passe"));
     try {
         if (testPassword("mot de passe", "729f2d8b3d3d9bc07ba349faab7fdf44"))
            System.out.println("Les passwords sont vérifiés");
         } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
         }
     }
*/
 
 }
