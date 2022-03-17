/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author yacou.kone
 */
public class ConfigPay {
        private  String url;
        private  String username;
        private  String password;
        private  String dbUrl;
        private  String dbdriver;
        private  String dbuser;
        private  String dbpassword;
        private String facturier;
        private String prefixe;
        private String compte;
        private String oper;
        private String idfacturier;
        
        public ConfigPay(String facturier){
         JSONParser jsonParser = new JSONParser();
            String chaine ="";
            try {
                InputStream inpufile = ConfigPay.class.getClassLoader().getResourceAsStream("org/atware/ressources/jsonpay.json");
                InputStreamReader ipsr=new InputStreamReader(inpufile);
                BufferedReader br=new BufferedReader(ipsr);
                String ligne;
                while ((ligne=br.readLine())!=null){
                    chaine+=ligne+"\n";
                }
                 br.close();   
                 
            
                           
             ///Parser le fichier json et lecture
                JSONObject json = new JSONObject(chaine);
                JSONArray json1 = json.getJSONArray("Paiement");
                
                 for(int i=0; i<json1.length();i++)
                    {
                          JSONObject valFacturier = json1.getJSONObject(i);
                           if(valFacturier.getString("facturier").equals(facturier))
                           {
                                this.facturier = valFacturier.getString("facturier");
                                this.dbUrl= valFacturier.getString("dbUrl");
                                this.dbdriver= valFacturier.getString("dbdriver");
                                this.dbpassword = valFacturier.getString("dbpassword");
                                this.dbuser = valFacturier.getString("dbuser");
                                this.username = valFacturier.getString("username");
                                this.url = valFacturier.getString("url");
                                this.password = valFacturier.getString("password");
                                this.prefixe =  valFacturier.getString("prefixe");
                                this.compte =  valFacturier.getString("compte");
                                this.oper =  valFacturier.getString("oper");
                                this.idfacturier= valFacturier.getString("idfacturier");
                           }  
                    }
                
               
            }             
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    public String getFacturier() {
        return facturier;
    }

        
        
    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbdriver() {
        return dbdriver;
    }

    public String getDbuser() {
        return dbuser;
    }

    public String getDbpassword() {
        return dbpassword;
    }

    public String getPrefixe() {
        return prefixe;
    }

    public String getCompte() {
        return compte;
    }

    public String getOper() {
        return oper;
    }

    public String getIdfacturier() {
        return idfacturier;
    }
        
        
        
          

    
}
