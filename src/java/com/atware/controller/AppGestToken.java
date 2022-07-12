/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.controller;

import com.atware.log.AppLogger;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.net.URI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import org.atware.bean.ConfigPay;
import org.atware.jdbc.ConnexionEfacture;
import org.json.JSONObject;

/**
 *
 * @author yacou.kone
 */

 @SuppressWarnings({"UseSpecificCatch", "CallToPrintStackTrace","UnusedAssignment"})
public class AppGestToken {
  
    private final ConfigPay infoFacturier;
    private final AppLogger appLoger;
//    private CrytageDonnees crypDonne;
//    private GenerateToken validToken;

    public ConfigPay getInfoFacturier() {
        return infoFacturier;
    }


    public AppGestToken(String maFacture){
        this.infoFacturier = new  ConfigPay(maFacture); 
        this.appLoger = new AppLogger();
//        crypDonne = new CrytageDonnees();
//        validToken = new GenerateToken();
    }
    
    
    
    //obtenir le token pour le traitement
   
     public String obtenirToken(ConfigPay facturier){
               
        String sql = String.format("SELECT datefin, heurefin,token_valid FROM egestoken where facturier= ?") ;
        String resultat = null;
        ConnexionEfacture conect = new ConnexionEfacture(facturier.getFacturier());
        String newToken = null;
        String v_token = null;
        int datefin,datj,heurefin,heurej =0;
        Connection conx = null;
        PreparedStatement ps = null; ResultSet rs= null;
        
        try {
            datj= appLoger.formatDateFacture(1);
            heurej = appLoger.formatDateFacture(2);
            conx = conect.maConnexion();
          
            ps = conx.prepareStatement(sql);
            ps.setString(1, facturier.getFacturier());
            
            rs =ps.executeQuery();
            if (rs.next()){            
              
                datefin = Integer.parseInt(rs.getString(1));
                heurefin = Integer.parseInt(rs.getString(2));
                v_token = rs.getString(3);
                
            
                if (datj < datefin ){
                    
                    resultat = v_token;
                    
                }
                else if ((datj==datefin) && (heurej+5 < heurefin)) {
                    
                    resultat = v_token;
                }
                else if ((datj==datefin) && (heurej+5 > heurefin)) {
                    
                    newToken = getNmpfToken();
                    ajoutToken(facturier.getFacturier(), newToken,conx);
                    resultat = getTokenJson(newToken);
                }
                else {
                     
                   newToken = getNmpfToken();
                   ajoutToken(facturier.getFacturier(), newToken,conx);
                   resultat = getTokenJson(newToken);
                }
                
                
            }
            
            else
            {
             
                  
                 newToken = getNmpfToken();
                 ajoutToken(facturier.getFacturier(), newToken,conx);                                     
                  
                   resultat = getTokenJson(newToken);
            }
        conx.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultat;
    }
    
    //enregistrement du token dans la base de données
    private  void ajoutToken(String facturier,String monToken, Connection connect){
     //   AppLogger appLoger = new AppLogger();
      //  Connection con = null;
         AppNmpf nmpf = new AppNmpf(facturier);       
       
         PreparedStatement ps,ps1,ps2 = null;
         ResultSet rs = null;
                  
         JSONObject json = null;
         
         try {
             
            json = new JSONObject(monToken);
            
             String sql1 = String.format("SELECT  facturier FROM egestoken where facturier = ?");
            ps1 = connect.prepareStatement(sql1);
            ps1.setString(1, facturier);
            rs = ps1.executeQuery();
            if (rs.next()){    
                  String sqlupdate = String.format ("UPDATE egestoken SET token_valid=?, datedebut=?, datefin=?, heurefin=?, "
                 + " expires_in=?,heuredebut=? WHERE facturier=?");
                  
                ps2 = connect.prepareStatement(sqlupdate);
                ps2.setString(1, json.getString("access_token"));
                ps2.setString(2, appLoger.formatDateFacture(1,json.getString(".issued")));
                ps2.setString(3, appLoger.formatDateFacture(1,json.getString(".expires")));
                ps2.setString(4, appLoger.formatDateFacture(2,json.getString(".expires")));
                ps2.setInt(5, Integer.parseInt(json.getString("expires_in")));
                ps2.setString(6, appLoger.formatDateFacture(2,json.getString(".issued")));
                ps2.setString(7, facturier);
                              
                ps2.executeUpdate();
             
            }
            else
            {
               String  sql = String.format("INSERT INTO egestoken(token_valid, datedebut, datefin,heuredebut,facturier, heurefin,expires_in) "
                 + "VALUES ( ?,?, ?, ?, ?, ?,?)");
                
                ps = connect.prepareStatement(sql);
                ps.setString(1, json.getString("access_token"));
                ps.setString(2, appLoger.formatDateFacture(1,json.getString(".issued")));
                ps.setString(3, appLoger.formatDateFacture(1,json.getString(".expires")));
                ps.setString(4, appLoger.formatDateFacture(2,json.getString(".issued")));
                ps.setString(5, facturier);
                ps.setString(6, appLoger.formatDateFacture(2,json.getString(".expires")));                
                ps.setString(7, json.getString("expires_in"));               
                
                ps.execute();             
                
             
            }
         } catch (Exception e) {
             System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
   
    
//génération d'un Token
      private String getNmpfToken(){
         String resultathttp = null;
        
         Client client = Client.create(new DefaultClientConfig());
         MultivaluedMap<String,String> map = new MultivaluedHashMap<>();
          map.add("Grant_type", "password");
          map.add("Username", getInfoFacturier().getUsername());
          map.add("Password", getInfoFacturier().getPassword());
          URI uri = UriBuilder.fromUri(getInfoFacturier().getUrl()+"/Authentification").build();
         ClientResponse reponse = client.resource(uri).type(MediaType.APPLICATION_FORM_URLENCODED)
                                   .post(ClientResponse.class, map);        
         
         if (reponse.getStatus()==200) {
              resultathttp = reponse.getEntity(String.class);
         }
         else
             resultathttp = "Erreur de traitement :"+reponse.getStatus();
       
         
        // resultathttp ="{\"access_token\":\"aAQyeCR2wOe3XflN0Pj1HkRMxbwXO0iKAE6XXwa9_VirbkJjtPX1K_rDOsorTG9lLGR1oKUNJG26Z7Ep3wh4bU5ARaIFwNPJrWHTbdFL4dKJ8CFoCSDcymITkkIvmM0MPYoeCtCOTFnkusT1KC0yj5jTmxxBdS0iddpUdPLMJL-av81DcccmJaudBMRrgpffN4IdxXakRabkHxrHthmF314uZxkUtM1KZhv63fKc2J5hxYV3rIEoGWtZcr2T1aBBZ3xvbMqdmyLf6THl8lepMA\",\"token_type\":\"bearer\",\"expires_in\":35999,\"userName\":\"P05\",\".issued\":\"Mon, 09 May 2022 14:59:41 GMT\",\".expires\":\"Tue, 10 May 2022 00:59:41 GMT\"}";
                  
        return resultathttp;
     }
      
      //convertion du json pour ramener le token valide
    @SuppressWarnings("StringEquality")
       private String getTokenJson(String txtjson) {
         if (txtjson != ""){
                    JSONObject json = null;
               try {
                    json=new JSONObject(txtjson);
                    String json1 = json.getString("access_token");
                    return json1;
               } catch (Exception e) {
                   e.printStackTrace();
                   return null;
               }
         }
         else
            return null;         
         
     }
       
     
    
}
