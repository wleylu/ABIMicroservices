/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.controller;

import com.atware.log.AppLogger;
import com.atware.utils.CrytageDonnees;
import com.atware.utils.GenerateToken;
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
import org.json.JSONObject;

/**
 *
 * @author yacou.kone
 */
public class AppGestToken {
    private ConfigPay infoFacturier;
    private AppLogger appLoger;
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
        String newToken = null;
        String v_token = null;
        int datefin,datj,heurefin,heurej =0;
        Connection conx = null;
        PreparedStatement ps = null; ResultSet rs= null;
        
        try {
            datj= appLoger.formatDateFacture(1);
            heurej = appLoger.formatDateFacture(2);
            conx = maConnexion();
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
    private  void ajoutToken(String facturier,String monToken,Connection cx){
        AppLogger appLoger = new AppLogger();
      //  Connection con = null;
         AppNmpf nmpf = new AppNmpf(facturier);

         String sql = String.format("INSERT INTO egestoken(token_valid, datedebut, datefin, heurefin, facturier,expires_in) "
                 + "VALUES ( ?,?, ?, ?, ?, ?)");
         String sql1 = String.format("SELECT  facturier FROM egestoken where facturier = ?");
         String sqlupdate = String.format ("UPDATE egestoken SET token_valid=?, datedebut=?, datefin=?, heurefin=?, "
                 + " expires_in=? WHERE facturier=?");
         PreparedStatement ps,ps1 = null;
         ResultSet rs = null;
         
         JSONObject json = null;
         
         try {
            json = new JSONObject(monToken);
            
          //   System.out.println(json.getString("token_type"));
           // con = maConnexion();
            ps1 = cx.prepareStatement(sql1);
            ps1.setString(1, facturier);
            rs = ps1.executeQuery();
            if (rs.next()){
                ps = cx.prepareStatement(sqlupdate);
                ps.setString(1, json.getString("access_token"));
                ps.setString(2, appLoger.formatDateFacture(1,json.getString(".issued")));
                ps.setString(3, appLoger.formatDateFacture(1,json.getString(".expires")));
                ps.setString(4, appLoger.formatDateFacture(2,json.getString(".expires")));
                ps.setInt(5, Integer.parseInt(json.getString("expires_in")));
                ps.setString(6, facturier);
                              
                ps.executeUpdate();
             
            }
            else
            {
                ps = cx.prepareStatement(sql);
                ps.setString(1, json.getString("access_token"));
                ps.setString(2, appLoger.formatDateFacture(1,json.getString(".issued")));
                ps.setString(3, appLoger.formatDateFacture(1,json.getString(".expires")));
                ps.setString(4, appLoger.formatDateFacture(2,json.getString(".expires")));
                ps.setString(5, facturier);
                ps.setInt(6, Integer.parseInt(json.getString("expires_in")));
                
                ps.execute();
                
            }
         } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //connexion à la base de données
    private Connection maConnexion(){
        Connection con =null;
        
        try {
            
            Class.forName(getInfoFacturier().getDbdriver());
            con = DriverManager.getConnection(getInfoFacturier().getDbUrl(), getInfoFacturier().getDbuser(), getInfoFacturier().getDbpassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
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
        
         
        return resultathttp;
     }
      
      //convertion du json pour ramener le token valide
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
