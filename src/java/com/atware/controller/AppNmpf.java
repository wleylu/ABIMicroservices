/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.controller;

import org.atware.bean.RegFacture;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.net.URI;
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

  @SuppressWarnings("UnusedAssignment")
public class AppNmpf {
    private String token;
    private  AppGestToken gesToken;
    private  ConfigPay configFact;

    public ConfigPay getConfigFact() {
        return configFact;
    }
       
    
    
    
    public AppNmpf(String facturier){
        this.configFact = new ConfigPay(facturier);
        this.gesToken = new AppGestToken(facturier);
        this.token = gesToken.obtenirToken(this.configFact);
        
      // this.token = gesToken.obtenirToken(this.configFact);
      // this.token="Nf6zN34QoVkA9_G9tpAstqMdN372fdr0C5ppDL7HRHe-w6PKtqtQevqqvKQA8WRcA4L-zAB_bhuQ4P_zCLBYRO8nMQnDwwY69I5aiFTBTmr1_YuaI6XfZRm8LBKB4DeoB0YCAMelokeUgqTXRov57OjzbsuOzY5qzbxO9842D3FXXCRCGHnsvtYIaV3BTqTeIL_J78Fet6CAeKlaWPQHFjyF7qkkEY5wBkKdEE8IpsJHsrA3tizAVtAtV588jDVbitxcp4ivXsiZ_EVbY64aRw";
        
      //  configSODECI =new ConfigPay(facturier); 
    }
         
     //consultation de facture CIE
     public String consulterFactureCIE(String facture){      
       
         String resultathttp = "{\"CodeTraitement\":1,\"MessageTraitement\":\"Echec de traitement\"}";
        
         Client client = Client.create(new DefaultClientConfig());
         URI uri = UriBuilder.fromUri(getConfigFact().getUrl()+"/api/nmpf/consultation?RefContrat="+facture).build();
         ClientResponse reponse = client.resource(uri).header("Authorization", "Bearer "+this.token).get(ClientResponse.class);  
         if(reponse.getStatus()==200){
              resultathttp = reponse.getEntity(String.class);
         }
         else
         {
             resultathttp = "{\"CodeTraitement\":1,\"MessageTraitement\":\"Echec de traitement : "+reponse.getStatus()+"==>"+reponse.getStatusInfo()+"\"}";
         }
          
        return resultathttp;
     }
     
     //fin consultation facture CIE
     
     //reglement de facture CIE
          
     public String reglementFactureCIE(String reglFact){
          String resultathttp = "{\"CodeTraitement\":1,\"MessageTraitement\":\"Echec de traitement\"}";
         RegFacture regFact = trtJsonFacture(reglFact);
          Client client = Client.create(new DefaultClientConfig());
          MultivaluedMap<String,String> map = new MultivaluedHashMap<>();
          map.add("RefContrat", regFact.getRefContrat());
          map.add("RefFacture", regFact.getRefFacture());
          map.add("DateReglement", regFact.getDateReglement());
          map.add("MontantReglement", regFact.getMontantReglement());
          map.add("HeureReglement", regFact.getHeureReglement());
          map.add("NumeroRecu", regFact.getNumeroRecu());
          map.add("Typecanal", regFact.getTypecanal());
          map.add("CodeOperateur", regFact.getCodeOperateur());
          URI uri = UriBuilder.fromUri(getConfigFact().getUrl()+"/api/nmpf/reglement").build();
          ClientResponse reponse = client.resource(uri).header("Authorization", "Bearer "+this.token).type(MediaType.APPLICATION_FORM_URLENCODED)
                 .post(ClientResponse.class, map);        
         
        if (reponse.getStatus()==200) {
              resultathttp = reponse.getEntity(String.class);
        }
        else
           resultathttp = "{\"CodeTraitement\":1,\"MessageTraitement\":\"Echec de traitement : "+reponse.getStatus()+"==>"+reponse.getStatusInfo()+"\"}";
     
         return resultathttp;
     }
     
     //fin reglement de facture CIE
     
     
     
      //consultation de facture SODECI
     public String consulterFactureSODECI(String facture){         
          String resultathttp = "{\"CodeTraitement\":1,\"MessageTraitement\":\"Echec de traitement\"}";
          Client client = Client.create(new DefaultClientConfig());
          URI uri = UriBuilder.fromUri(getConfigFact().getUrl()+"/api/nmpf/consultation?RefContrat="+facture).build();
          System.out.println(token);
          ClientResponse reponse = client.resource(uri).header("Authorization", "Bearer "+this.token).get(ClientResponse.class);   
         if(reponse.getStatus()==200){
              resultathttp = reponse.getEntity(String.class);
         }
         else
         {
             resultathttp = "{\"CodeTraitement\":1,\"MessageTraitement\":\"Echec de traitement : "+reponse.getStatus()+"==>"+reponse.getStatusInfo()+"\"}";
         }
          
         
        return resultathttp;
     }
     
     //fin consultation facture SODECI
     
     
     //reglement de facture SODECI
          
     public String reglementFactureSODECI(String reglFact){
          String resultathttp = "{\"CodeTraitement\":1,\"MessageTraitement\":\"Echec de traitement\"}";
          RegFacture regFact = trtJsonFacture(reglFact);
          Client client = Client.create(new DefaultClientConfig());
          MultivaluedMap<String,String> map = new MultivaluedHashMap<>();
          map.add("RefContrat", regFact.getRefContrat());
          map.add("RefFacture", regFact.getRefFacture());
          map.add("DateReglement", regFact.getDateReglement());
          map.add("MontantReglement", regFact.getMontantReglement());
          map.add("HeureReglement", regFact.getHeureReglement());
          map.add("NumeroRecu", regFact.getNumeroRecu());
          map.add("Typecanal", regFact.getTypecanal());
          map.add("CodeOperateur", regFact.getCodeOperateur());
          URI uri = UriBuilder.fromUri(getConfigFact().getUrl()+"/api/nmpf/reglement").build();
          ClientResponse reponse = client.resource(uri).header("Authorization", "Bearer "+this.token).type(MediaType.APPLICATION_FORM_URLENCODED)
                 .post(ClientResponse.class, map);        
         
        if (reponse.getStatus()==200) {
              resultathttp = reponse.getEntity(String.class);
        }
        else
           resultathttp = "{\"CodeTraitement\":1,\"MessageTraitement\":\"Echec de traitement : "+reponse.getStatus()+"==>"+reponse.getStatusInfo()+"\"}";
        //String txjson = new Gson().toJson(regFact);
         
         return resultathttp;
     }
     
     //fin reglement de facture SODECI
        

   //parser le json dans une classe  
     private RegFacture trtJsonFacture(String factures){
       JSONObject json = null;         
         RegFacture fact = new RegFacture();
         try {
             json = new JSONObject(factures);
             fact.setRefContrat(json.getString("RefContrat"));
             fact.setRefFacture(json.getString("RefFacture"));
             fact.setNumeroRecu(json.getString("NumeroRecu"));
             fact.setDateReglement(json.getString("DateReglement"));
             fact.setTypecanal(json.getString("TypeCanal"));
             fact.setMontantReglement(json.getString("MontantReglement"));
             fact.setCodeOperateur(json.getString("CodeOperateur"));
             fact.setHeureReglement(json.getString("HeureReglement"));
         } catch (Exception e) {
             
         }
                  
         return fact;
     }
     
     
    
}
