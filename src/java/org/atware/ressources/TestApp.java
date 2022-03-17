/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.ressources;


import com.abi.microservices.ServicesResource;
import com.atware.controller.AppGestToken;
import com.atware.controller.AppNmpf;
import com.atware.log.AppLogger;
import com.atware.utils.AppAuthorisation;
import com.atware.utils.CrytageDonnees;
import static com.atware.utils.CrytageDonnees.createSecretKey;
import static com.atware.utils.CrytageDonnees.decrypt;
import static com.atware.utils.CrytageDonnees.encrypt;
import com.atware.utils.GenerateToken;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import org.atware.bean.AccessContectUser;
import org.atware.bean.ConfigPay;
import org.atware.bean.TransactionT24;
import org.json.JSONArray;
import org.json.JSONObject;



/**
 *
 * @author yacou.kone
 */
public class TestApp {

    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
//        AppAuthorisation userConnect = new AppAuthorisation();
//        AccessContectUser user = new AccessContectUser();
//       // user.setLogin("Kone");
//        user.setLogin("");
//        user.setAdip("10.100.10.6");     
//        user.setTokenValid("BFuuxh3pygbs2LgcI3dnztU4kF-8NySoryKu7hARpiuY1j0VsxzFqYEOsh_QaYP2z_EB1JCBwzA9OMKfKDZJiLEq9i8Rova483BuF3ZEFGBuZJE_37PiZYK5N7cQOOGE9rYkhw==");
//        
//        //CrytageDonnees crt = new CrytageDonnees();
//        
//        System.out.println("Le resultat de traitement est : "+userConnect.appConnexionuUser(user));
//      


/*
        ServicesResource serv_sorc = new ServicesResource();
        String txt= "{\n" +
"\"compteDebit\":\"145116420013\",\n" +
"\"client\": \"14511642\",\n" +
"\"facturier\":\"CIE\",\n" +
"\"identifiantFacture\":\"CIE0001\",\n" +
"\"mntOper\":1003,\n" +
"\"mntFacture\":1003,\n" +
"\"mntFrais\":0,\n" +
"\"mntFraisMarchand\":0,\n" +
"\"mntTimbre\":0\n" +
"}";
        
        TransactionT24 trans = new TransactionT24();
        trans = serv_sorc.convertJsonTransaction(txt);
        
        System.out.println(trans);
*/

        String txt = "FT22007HQN6Z|FT22007T5VXZ";
        int t = txt.indexOf("|");
        String txt1 = txt.substring(0, t);
        String txt2 = txt.substring(0, txt.indexOf("|"));
        System.out.println(txt2);
    
    }
    
}
