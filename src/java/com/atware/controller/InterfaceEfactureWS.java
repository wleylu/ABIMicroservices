/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.controller;

import com.temenos.twsefacture.T24WebServicesImpl;
import com.temenos.twsefacture.ObjectFactory;
import com.temenos.twsefacture.WebRequestCommon;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.atware.bean.ConfigInfos;
import com.atware.log.AppLogger;
import com.atware.utils.ServiceResultatJson;
import com.temenos.tnaefacturemain.TNAEFACTUREMAINType;
import com.temenos.twsefacture.EnquiryInput;
import com.temenos.twsefacture.EnquiryInputCollection;
import com.temenos.twsefacture.Status;
import com.temenos.twsefacture.SuccessIndicator;
import com.temenos.twsefacture.TNAEFACTUREMAIN;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.Holder;
import org.atware.bean.AnnulTransT24;
import org.atware.bean.ConfigPay;
import org.atware.bean.TransactionT24;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author yacou.kone
 */
public class InterfaceEfactureWS {

     private Service service;
   private T24WebServicesImpl t24port;
  // private AMOBILE orionport;
   private ObjectFactory objfact;
   private WebRequestCommon wrqc;
   private ConfigInfos config;
   public String filiale;
   private    AppLogger log;
   public String viewaccount="yes";
   public String oldaccount="yes";



 @SuppressWarnings("CallToPrintStackTrace")
    public InterfaceEfactureWS() throws ParseException {

           
              String testUrl=null;

       // Service service;
//      System.out.println("Filiale="+filiale+" , ConfigFiliales="+config.getOrionfiliales());
//     System.out.println(config.getT24wsaddress());
            try { // Call Web Service Operation
                //pour descativer la sécurité SSL

                config=new ConfigInfos();
                 testUrl=config.getT24wsaddress();
                // System.out.println("testUrl :"+testUrl);
                 if (testUrl.substring(0, 8).toUpperCase().equals("HTTPS://")){
                            sslValidation();
                    }

               // AppLogger log;
                URL url = new URL(config.getT24wsaddress());
                QName serviceName = new QName(config.getT24wsdomain(),config.getT24wsname());
                service = Service.create(url, serviceName);
                t24port = service.getPort(T24WebServicesImpl.class);
                objfact = new ObjectFactory();
                wrqc = objfact.createWebRequestCommon();
                wrqc.setUserName(config.getT24useref());wrqc.setPassword(config.getT24secretef());wrqc.setCompany(config.getCompanyt24ef());
                 log=new AppLogger("Initialisation interface WS T24 ===> "+new AppLogger().formatageDateHeure());


            } catch (Exception ex) {         
                     log=new AppLogger("Impossible de se connecter avec le service SOAP ==> methode: InterfaceEfactureWS ==>"+new AppLogger().formatageDateHeure()+"==>"+ex.getMessage());
               
            }

       }



            //descativer SSL

             @SuppressWarnings("CallToPrintStackTrace")
    private static void sslValidation() {
    try
    {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
               // System.out.println("caller hostname="+hostname);
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
    catch (NoSuchAlgorithmException | KeyManagementException e) {
        e.printStackTrace();
    }
}


    //signaletique clients
    public String getSignaletique(String client) throws ParseException{
        String resultat=null;
        
        //String retour=null;



            try {
                

                EnquiryInputCollection enqColl = objfact.createEnquiryInputCollection();
                enqColl.setColumnName("EFACTURE.REQUEST");
                enqColl.setCriteriaValue("SIGNC|A|W|"+config.getT24useref()+"|"+client);
                enqColl.setOperand("EQ");
                EnquiryInput enqIn;enqIn = objfact.createEnquiryInput();
                enqIn.getEnquiryInputCollection().add(enqColl);
                Holder<List<TNAEFACTUREMAINType>>  tnaefacturemaintype0= new Holder<>();
                Holder<Status> status = new Holder<>();SuccessIndicator succes = null;

                        t24port.tnaefacturemain(wrqc, enqIn, status, tnaefacturemaintype0);
                     if(status.value.getSuccessIndicator().equals(SuccessIndicator.SUCCESS)){
                         resultat = tnaefacturemaintype0.value.get(0).getGTNAEFACTUREMAINDetailType().getMTNAEFACTUREMAINDetailType().get(0).getREPONSEEFACTURE();
                     }
                     else {
                         resultat = "{\"RESULTSET\":[{\"Statut\":\"1\",\"StatutMsg\":\"DEMANDE ACCEPTE\",\"StatutData\":[{Erreur:\"ERREUR DE TRITEMENT\"}]}]}";
                          log=new AppLogger("Problème ouverture du port WS T24 ==> méthode: InterfaceEfactureWS.getSiganaletique ===> "+new AppLogger().formatageDateHeure());
                     }


            } catch(Exception ex) {
                resultat = "{\"RESULTSET\":[{\"Statut\":\"1\",\"StatutMsg\":\"DEMANDE ACCEPTE\",\"StatutData\":[{Erreur:\"ERREUR DE TRITEMENT\"}]}]}";
                log=new AppLogger("Erreur WS T24 ==> méthode: InterfaceEfactureWS.getSiganaletique ===> "+new AppLogger().formatageDateHeure()+" ==>"+ex.getMessage());
               
            }
  
        return resultat;

    }

    //fin signaletique
    
    //comptabilisation ecriture
    
    public String setCompta(TransactionT24 oper ) throws ParseException{
    //    TransactionT24 oper = new TransactionT24();
        ConfigPay configPay = new ConfigPay(oper.getFacturier());
        AppLogger log = new AppLogger();
        String resultat=null;String retour=null;
        
        /*  String resultat=null;String retour=null;
         String trameOper = oper.getPrefixe()+oper.getFacturier()+"| EFACTURE|"+oper.getDatOper()+"|"+oper.getCodOper()+"|"+oper.getRefOld()
                 +"|"+oper.getCompteDebit()+"|"+oper.getCompteCredit()+"|"+oper.getMntOper()+"|"+oper.getMntFacture()+"|"+oper.getMntFrais()+"|"+oper.getMntFraisMarchand()
                 +"|"+oper.getMntTimbre()+"|"+oper.getLibelleOper()+"|"+oper.getIdentifiantFacture();
        */
         
          String trameOper = configPay.getPrefixe()+oper.getClient()+"|"+configPay.getIdfacturier()+"|"+"REF"+log.getrRfExtdate()+"|"+configPay.getOper()+"|"+oper.getRefOld()
                 +"|"+oper.getCompteDebit()+"|"+configPay.getCompte()+"|"+oper.getMntOper()+"|"+oper.getMntFacture()+"|"+oper.getMntFrais()+"|"+oper.getMntFraisMarchand()
                 +"|"+oper.getMntTimbre()+"|PMT FACTURE"+oper.getFacturier()+" "+configPay.getOper()+" par "+oper.getClient()+" du "+log.formatageDateHeure()+"|";
       

        //System.out.println(" Tramme nenvoyé :"+trameOper);      
       
       
       
         EnquiryInputCollection enqColl = objfact.createEnquiryInputCollection();
         enqColl.setColumnName("EFACTURE.REQUEST");
         enqColl.setCriteriaValue(trameOper);
         enqColl.setOperand("EQ");
         EnquiryInput enqIn;enqIn = objfact.createEnquiryInput();
         enqIn.getEnquiryInputCollection().add(enqColl);
         Holder<List<TNAEFACTUREMAINType>>  tnaefacturemaintype0= new Holder<>();
         Holder<Status> status = new Holder<>();SuccessIndicator succes = null;

         

           try {
                        t24port.tnaefacturemain(wrqc, enqIn, status, tnaefacturemaintype0);
                     if(status.value.getSuccessIndicator().equals(SuccessIndicator.SUCCESS)){
                         resultat = tnaefacturemaintype0.value.get(0).getGTNAEFACTUREMAINDetailType().getMTNAEFACTUREMAINDetailType().get(0).getREPONSEEFACTURE();
                         
                     }
                     else {
                           resultat = "{\"RESULTSET\":[{\"Statut\":\"1\",\"StatutMsg\":\"DEMANDE ACCEPTE\",\"StatutData\":[{Erreur:\"ERREUR DE TRITEMENT\"}]}]}";
                          log=new AppLogger("Problème ouverture du port WS T24 ==> méthode: InterfaceEfactureWS.setCompta ===> "+new AppLogger().formatageDateHeure());
                         
                     }


            } catch(Exception ex) {
                resultat = "{\"RESULTSET\":[{\"Statut\":\"1\",\"StatutMsg\":\"DEMANDE ACCEPTE\",\"StatutData\":[{Erreur:\"ERREUR DE TRITEMENT\"}]}]}";
                log=new AppLogger("Erreur WS T24 ==> méthode: InterfaceEfactureWS.setCompta ===> "+new AppLogger().formatageDateHeure()+" ==>"+ex.getMessage());
                
               
            }
         
            String resultatT24 = getRefTransaction(resultat,"REF"+log.getrRfExtdate());
           
           
        return resultatT24;

    }
    
    // fin comptabilisation
    
    
    // anniulation ecriture
     @SuppressWarnings({"UnusedAssignment", "UseSpecificCatch"})
    public String setAnnulT24(AnnulTransT24 operAnnul ) throws ParseException, IOException{
    //    TransactionT24 oper = new TransactionT24();
        ConfigPay configPay = new ConfigPay(operAnnul.getFacturier());
        ConfigInfos userT24Info = new ConfigInfos();
        AppLogger log = new AppLogger();
        String resultat=null;String retour=null;
     
          String   trameOper = "ANNUL|A|W|"+userT24Info.getT24useref()+"|"+configPay.getIdfacturier()+"|"+operAnnul.getDateOper()+"|"+operAnnul.getRefExterne()+"|"+operAnnul.getRefFTSib();
        //System.out.println(" Tramme nenvoyé :"+trameOper);      
       
       
       
         EnquiryInputCollection enqColl = objfact.createEnquiryInputCollection();
         enqColl.setColumnName("EFACTURE.REQUEST");
         enqColl.setCriteriaValue(trameOper);
         enqColl.setOperand("EQ");
         EnquiryInput enqIn;enqIn = objfact.createEnquiryInput();
         enqIn.getEnquiryInputCollection().add(enqColl);
         Holder<List<TNAEFACTUREMAINType>>  tnaefacturemaintype0= new Holder<>();
         Holder<Status> status = new Holder<>();SuccessIndicator succes = null;
         

           try {
                        t24port.tnaefacturemain(wrqc, enqIn, status, tnaefacturemaintype0);
                     if(status.value.getSuccessIndicator().equals(SuccessIndicator.SUCCESS)){
                         resultat = tnaefacturemaintype0.value.get(0).getGTNAEFACTUREMAINDetailType().getMTNAEFACTUREMAINDetailType().get(0).getREPONSEEFACTURE();
                         
                     }
                     else {
                           resultat = "{\"RESULTSET\":[{\"Statut\":\"1\",\"StatutMsg\":\"DEMANDE ACCEPTE\",\"StatutData\":[{Erreur:\"ERREUR DE TRITEMENT\"}]}]}";
                          log=new AppLogger("Problème ouverture du port WS T24 ==> méthode: InterfaceEfactureWS.setCompta ===> "+new AppLogger().formatageDateHeure());
                         
                     }


            } catch(Exception ex) {
                resultat = "{\"RESULTSET\":[{\"Statut\":\"1\",\"StatutMsg\":\"DEMANDE ACCEPTE\",\"StatutData\":[{Erreur:\"ERREUR DE TRITEMENT\"}]}]}";
                log=new AppLogger("Erreur WS T24 ==> méthode: InterfaceEfactureWS.setAnnulT24 ===> "+new AppLogger().formatageDateHeure()+" ==>"+ex.getMessage());
                
               
            }
           
        
        return resultat;

    }


    
    private String getRefTransaction(String retourJson,String refExterne) throws ParseException{
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
                    //    resultat = "{\"statut\":0,\"Nooper\":\""+refop.substring(0, refop.indexOf("|"))+"\"}";
                        resultat = "{\"statut\": 0,\"Nooper\": \""+refop.substring(0, refop.indexOf("|"))+"\",\"refExterne\": \""+refExterne+"\"}";
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


    
    
    /*
@SuppressWarnings("CallToPrintStackTrace")
    public String getClientinfos(String client){
        String resultat="",resultat2="";String requete = client;String rep="NOK";
        if (this.t24port== true) {
            @SuppressWarnings("UnusedAssignment")
            EnquiryInputCollection enqColl = objfact.createEnquiryInputCollection();
            EnquiryInput enqIn;enqIn = objfact.createEnquiryInput();
            enqColl.setColumnName("PAY.REQUEST");enqColl.setCriteriaValue(requete);enqColl.setOperand("EQ");
            enqIn.getEnquiryInputCollection().add(enqColl);
            Holder<List<TNAGETCLIENTType>> tnagetclientType0 = new Holder<>();
            Holder<List<TNAGETACCOUNTType>> tnagetaccountType0 = new Holder<>();
            Holder<Status> status = new Holder<>();SuccessIndicator succes = null;
            try {
                System.out.println("Appel du web service pour le client : "+client);
                t24port.wstnagetclient(wrqc, enqIn, status, tnagetclientType0);
                if (status.value.getSuccessIndicator().equals(SuccessIndicator.SUCCESS)){
                    resultat = tnagetclientType0.value.get(0).getGTNAGETCLIENTDetailType().getMTNAGETCLIENTDetailType().get(0).getINFORMATIONCLIENT();
                    System.out.println("Info client T24 [ Compte = "+client+" ] = "+resultat);
                    t24port.wstnagetaccount(wrqc, enqIn, status, tnagetaccountType0);
                    resultat2 = tnagetaccountType0.value.get(0).getGTNAGETACCOUNTDetailType().getMTNAGETACCOUNTDetailType().get(0).getLISTEDESCOMPTES();
                    System.out.println("Liste des compte du Client "+client+" ] = "+resultat);
                    rep=(new StringBuilder()).append(resultat).append(":CPT:").append(resultat2).toString();
                } else {
                    System.out.println(" Erreur pendant l'appel du web service ");
                    System.out.println(" Messages système="+status.value.getMessages().toString());
                }

            } catch(Exception e){
                e.printStackTrace();
            }
        } else {
            String paramstr = filiale+";"+client;
            rep = orionport.getClientinfos(paramstr);
        }
        return rep;
    }
*/
}