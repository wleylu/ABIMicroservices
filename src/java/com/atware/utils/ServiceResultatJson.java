/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.utils;


import com.atware.log.AppLogger;

import com.atware.controller.InterfaceEfactureWS;
import com.google.gson.Gson;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.atware.bean.Client;
import org.atware.bean.Comptes;
import org.atware.bean.ConfigInfos;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author yacou.kone
 */
public class ServiceResultatJson {
    private AppLogger log;
   
    
    @SuppressWarnings({"null", "IndexOfReplaceableByContains"})
    public String getClientInfos(String json){

       
        Client client=new Client();
        List<Comptes> cpts = new ArrayList<>();
        Comptes cpt = null;
        String msgStatut = getCodeRetour(json);
        ConfigInfos config=null;
        try {
            config = new ConfigInfos();
        } catch (IOException ex) {
            Logger.getLogger(ServiceResultatJson.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
                JSONObject jsonobject=null;                

                        try {
                           
                             jsonobject = new JSONObject(json);
                             JSONArray client1 =  jsonobject.getJSONArray("RESULTSET");
                               if (msgStatut.equals("0"))
                               {
                                   JSONObject client2 = new JSONObject(client1.getString(0));
                                   JSONArray client3 = client2.getJSONArray("StatutData");                               
                                   JSONObject infosClient = new JSONObject(client3.getString(0));
                                   
                                   client.setClient(infosClient.getString("Client"));
                                   client.setAgec(infosClient.getString("Agec"));
                                   client.setDateDeLivr(infosClient.getString("DateDeLivr"));
                                   client.setDateExpir(infosClient.getString("DateExpir"));
                                   client.setDateNais(infosClient.getString("DateNais"));

                                   client.setSexe(infosClient.getString("Sexe"));
                                   client.setNomPrenom(infosClient.getString("NomPrenom"));
                                   client.setNom(infosClient.getString("NOM"));
                                   client.setPrenom(infosClient.getString("Prenom"));
                                   client.setTel(infosClient.getString("Tel"));

                                   client.setPieceId(infosClient.getString("PieceId"));
                                   client.setEmail(infosClient.getString("Email"));


                                   JSONArray comptes = new JSONArray(infosClient.getString("Comptes"));
                                   int j = comptes.length();
                                   for (int i = 0; i< comptes.length(); i++){
                                       JSONObject cpte = comptes.getJSONObject(i);                                   
                                       cpt = new Comptes();
                                       //String ncg =config.getCategory();
                                       
                                       if (config.getCategory().indexOf(cpte.getString("Ncg")) > -1 ){
                                            cpt.setCompte(cpte.getString("Compte"));
                                            cpt.setAgence(cpte.getString("Agence"));
                                            cpt.setNcg(cpte.getString("Ncg"));
                                            cpt.setLibNcg(cpte.getString("Libncg"));
                                            cpt.setCoddci(cpte.getString("Coddci"));
                                            cpt.setExpl(cpte.getString("Expl"));

                                            cpts.add(cpt);
                                       }
                                       
                                        if (config.getCategory().equals("*")){
                                            cpt.setCompte(cpte.getString("Compte"));
                                            cpt.setAgence(cpte.getString("Agence"));
                                            cpt.setNcg(cpte.getString("Ncg"));
                                            cpt.setLibNcg(cpte.getString("Libncg"));
                                            cpt.setCoddci(cpte.getString("Coddci"));
                                            cpt.setExpl(cpte.getString("Expl"));

                                            cpts.add(cpt);
                                       }
                                      
                                  
                               }    
                                   
                                      client.setComptes(cpts);
                           } 
                          
                         if (msgStatut.equals("1")){
                             client = new Client();
                         }
                            
                    } 
                        catch (JSONException e) 
                        {
                          client = new Client();
                        //  donnees = new Client();
                          e.printStackTrace();
                        }             
    
        
        String retourJson = new Gson().toJson(client);
                
        return retourJson;
        
    }
    
    //lecture du retour comptabilisation
    
    public String getRefTransaction(String retourJson) throws ParseException{
        String msgStatut = getCodeRetour(retourJson);
        String resultat = "{\"statut\":1,\"Nooper\":\"}";
        String refop = null;
            try {
                    JSONObject opers = new JSONObject(retourJson);
                    JSONArray oper1 = opers.getJSONArray("RESULTSET");
                    
                    if (msgStatut.equals("0")){  
                        JSONObject oper2 = new JSONObject(oper1.getString(0));
                        JSONArray oper3 = oper2.getJSONArray("StatutData");
                        JSONObject oper4 = new JSONObject(oper3.getString(0));
                        refop = oper4.getString("Nooper");   
                        resultat = "{\"statut\":0,\"Nooper\":\""+refop.substring(0, refop.indexOf("|"))+"\"}";
                      //  resultat = "{"\"statut\":\"1,\"\"+Nooper\": "+"\""+oper4.getString("Nooper")+"\"}";
                      }   
                    if (msgStatut.equals("1")){
                        JSONObject trans1 = new JSONObject(oper1.getString(0));                        
                      //  System.out.println(trans1);
                        resultat = "{\"statut\":1,\"Nooper\":\""+trans1.getString("StatutMsg")+"\"}";
                    }
                    
            } catch (Exception e) {
                log = new AppLogger("Erreur de traitement de la tramme retour ==> Methode: ServiceResultatJson.getRefTransaction ==> "+new AppLogger().formatageDateHeure());
                e.printStackTrace();
            }
     
  
        return resultat;
    } 
    
    private String getCodeRetour(String msgRetour){
        try {
            JSONObject msg = new JSONObject(msgRetour);
            JSONArray msgs  = msg.getJSONArray("RESULTSET");
            JSONObject rMsg = new JSONObject(msgs.getString(0));
            String resultat = rMsg.getString("Statut");
            return resultat;
        } catch (JSONException ex) {
            ex.printStackTrace();
            Logger.getLogger(ServiceResultatJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "1";
    }
    
    
}
