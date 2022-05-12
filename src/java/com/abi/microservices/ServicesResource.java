/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abi.microservices;

import com.atware.controller.AppEfactureWST24;
import com.atware.controller.AppMicroAnnulTransact;
import com.atware.controller.AppMicroGetBalance;
import com.atware.controller.AppMicroGetBalanceMultiple;
import com.atware.controller.AppMicroSignaletique;
import com.atware.controller.AppMicroSolde;
import com.atware.controller.AppMicroTransactions;
import com.atware.utils.AppAuthorisation;
import com.atware.utils.Utility;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.atware.bean.AccessContectUser;
import org.atware.bean.AnnulTransT24;
import org.atware.bean.AuditInfos;
import org.atware.bean.RefAnnulation;
import org.atware.bean.TransactionInfo;
import org.atware.bean.TransactionT24;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author MELARGA
 */
@Path("services")
public class ServicesResource {

    @Context
    private UriInfo context;
    private AppMicroSignaletique appCtrl;
    private HttpServletRequest httpServletRequest;
    private Utility utility;
    private   AppEfactureWST24 facture;
    
    
  
    /**
     * Creates a new instance of ServicesResource
     */
    public ServicesResource() {
        
        try {
            this.facture = new AppEfactureWST24();
        } catch (ParseException ex) {
            Logger.getLogger(ServicesResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Retrieves representation of an instance of com.abi.microservices.ServicesResource
     * @param request
     * @param serviceid
     * @param apikey
     * @param filiale
     * @param compte
     * @param agence
     * @return an instance of java.lang.String
     * @throws java.io.IOException
     */
    
    @GET
    @Path("/index")
    public String initialisation(@Context HttpServletRequest request,@HeaderParam("login") String login){   
          String text = request.getRemoteHost();
          
        return "<h1>Bienvenue dans le moteur de gestion des APIs ABI</h1><br>";
    }
    
    
    
    
    
    @POST
    @Path("/getTest")
    public String getTest(@FormParam("nom") String nom){        
        return "<h1>Bonjour "+nom+"</h1>";
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/signaletique")
    public String getSignaletique(@Context HttpServletRequest request,@HeaderParam("idservice") String serviceid,
            @HeaderParam("apikey") String apikey,@FormParam("filiale") String filiale ,
            @FormParam("compte") String compte,
            @FormParam("agence") String agence) throws IOException {
        
        String resultat=null;
        utility=new Utility();
        AuditInfos autInfo=null;
        String parmatres="filiale:"+filiale+",app_user:"+serviceid+
                ",apikey:"+apikey+",compte :"+compte+",agence:"+agence;
       
        boolean statut=utility.getServicesAcces(apikey, filiale, serviceid,"signaletique");
        
                appCtrl = new AppMicroSignaletique();
                String  testresp="";
                //TODO return proper representation object request.getParameter("prenoms") != null ? request.getParameter("prenoms") : " ";
                String remoteAddr = request.getRemoteAddr() != null ? request.getRemoteAddr() : "";
                String remoteHost = request.getRemoteHost() != null ? request.getRemoteHost() : "";
                String remoteUser= request.getRemoteUser() != null ? request.getRemoteUser() : "";
                String remoteAddrProxy = request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR") : "";
  if(statut)
        {
                //appeller la methode checkCredentials de la classe UtilitycheckCredentials(String filiale,String remoteAddr,String serviceid,String apikey)
                testresp = (new StringBuilder()).
                        append("remoteAddrProxy=").append(remoteAddrProxy).append("\n").
                        append("remoteAddr=").append(remoteAddr).append("\n").
                        append("remoteHost=").append(remoteHost).append("\n").
                        append("remoteUser=").append(remoteUser).append("\n").
                        append("serviceid=").append(serviceid).append("\n").
                        append("api_key=").append(apikey).append("\n").
                        append("agence=").append(agence).append("\n").toString();
              /*
                System.out.println("serviceid="+serviceid);
                System.out.println("serviceid="+serviceid);
                System.out.println("serviceid="+serviceid);
                System.out.println("api_key="+apikey);
                System.out.println("agence="+agence);
                System.out.println("compte="+agence);
                System.out.println("remoteAddr="+remoteAddr);
                System.out.println("remoteHost="+remoteHost);
                System.out.println("remoteUser="+remoteUser);
               */
                // Appeller ensuite la fonction signaletique si checkCredentials()=true
                // Il faudriat aussi journaliser la trnasaction

                //return testresp;
                
               resultat= appCtrl.getSignaletiqueClient(filiale, compte, agence);
               
               
                
       
        
         }
        else
        {
            resultat="{\"success\" : \"false\",\"message\":\"Acces denied\"}"; 
        }
       
        
        autInfo= new AuditInfos(filiale, utility.formatageDateJ(), serviceid, 
                               "signaletigue",apikey, remoteHost, remoteAddr, parmatres, resultat);
               utility.auditConnexion(autInfo);
        
       return resultat;
    }
    
    
    //consultation de solde de compte
     @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/solde")
    public String getSolde(@Context HttpServletRequest request,@HeaderParam("idservice") String serviceid,
            @HeaderParam("apikey") String apikey,@FormParam("filiale") String filiale ,
            @FormParam("compte") String compte) throws IOException {
        
        String resultat=null;
        utility=new Utility();
        AppMicroSolde appCtrlSolde=new AppMicroSolde();
        AuditInfos autInfo=null;
      
        String parmatres="filiale:"+filiale+",app_user:"+serviceid+
                ",apikey:"+apikey+",compte :"+compte;
      
        boolean statut=utility.getServicesAcces(apikey, filiale, serviceid,"solde");
        
      
         System.out.println("Resultat "+statut);
        
          
                String  testresp="";
                //TODO return proper representation object request.getParameter("prenoms") != null ? request.getParameter("prenoms") : " ";
                String remoteAddr = request.getRemoteAddr() != null ? request.getRemoteAddr() : "";
                String remoteHost = request.getRemoteHost() != null ? request.getRemoteHost() : "";
                String remoteUser= request.getRemoteUser() != null ? request.getRemoteUser() : "";
                String remoteAddrProxy = request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR") : "";
                
            if(statut)
        {
                                 
                
                //appeller la methode checkCredentials de la classe UtilitycheckCredentials(String filiale,String remoteAddr,String serviceid,String apikey)
                testresp = (new StringBuilder()).
                        append("remoteAddrProxy=").append(remoteAddrProxy).append("\n").
                        append("remoteAddr=").append(remoteAddr).append("\n").
                        append("remoteHost=").append(remoteHost).append("\n").
                        append("remoteUser=").append(remoteUser).append("\n").
                        append("serviceid=").append(serviceid).append("\n").
                        append("api_key=").append(apikey).append("\n").toString();
                        
              /*
                System.out.println("serviceid="+serviceid);
                System.out.println("serviceid="+serviceid);
                System.out.println("serviceid="+serviceid);
                System.out.println("api_key="+apikey);
                System.out.println("remoteAddr="+remoteAddr);
                System.out.println("remoteHost="+remoteHost);
                System.out.println("remoteUser="+remoteUser);
                System.out.println("query="+request.getQueryString());
              
               
                
                
                // Appeller ensuite la fonction signaletique si checkCredentials()=true
                // Il faudriat aussi journaliser la trnasaction

                //return testresp;
                
               */
              
             
                                
               resultat= appCtrlSolde.getsolde(filiale, compte);
               
       
        
         }
        else
        {
            resultat="{\"success\" : \"false\",\"message\":\"Acces denied\"}"; 
        }
        
        
       
               autInfo= new AuditInfos(filiale, utility.formatageDateJ(), serviceid, 
                               "solde",apikey, remoteHost, remoteAddr, parmatres, resultat);
               utility.auditConnexion(autInfo);
         
      //utility.auditConnexion(audit);
       
       return resultat;
    }
    
    
    
    
    //traitement des transactions 
     @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/transactions")
    public String getOperations(@Context HttpServletRequest request,@HeaderParam("idservice") String serviceid,
            @HeaderParam("apikey") String apikey,@FormParam("filiale") String filiale ,
            @FormParam("facturier") String facturier,
            @FormParam("caisse") String caisse,
            @FormParam("dhtransact") String dhtransact,
            @FormParam("codoper") String codoper,
            @FormParam("refrel") String refrel,
            @FormParam("compteDebit") String compteDebit,
            @FormParam("compteCredit") String compteCredit,
            @FormParam("montantDebit") String montantDebit,
            @FormParam("montantCredit") String montantCredit,
            @FormParam("montantCom1") String montantCom1,
            @FormParam("montantCom2") String montantCom2,
            @FormParam("montantTaxe") String montantTaxe,
            @FormParam("narratif") String narrartif            
            ) throws IOException {
        
        String resultat=null;
        utility=new Utility();
        AppMicroTransactions  appTransact=new AppMicroTransactions();
        AuditInfos autInfo=null;
        TransactionInfo transaction=new TransactionInfo();
        String parmatres="filiale:"+filiale+",app_user:"+serviceid+
                ",apikey:"+apikey+",facturier:"+facturier+",caisse:"+caisse+
                ",dhtransact:"+dhtransact+",codoper:"+codoper+",refrel:"+refrel+
                ",compteDebit:"+compteDebit+",compteCredit:"+compteCredit+
                ",montantDebit:"+montantDebit+",montantCredit:"+montantCredit+
                ",montantCom1:"+montantCom1+",montantCom2:"+montantCom2+
                ",montantTaxe"+montantTaxe+",narratif:"+narrartif;
               
        
        /*
         System.out.println("apikey :"+apikey);
         System.out.println("serviceid :"+serviceid);
         
         System.out.println("apikey "+apikey);
         System.out.println("filiale ici "+filiale);
         System.out.println("serviceid "+serviceid);
         */
         String parametre="filiale="+filiale;
       
        boolean statut=utility.getServicesAcces(apikey, filiale, serviceid,"transaction");
        
      
        // System.out.println("Resultat "+statut);
        
       
                
                String  testresp="";
                //TODO return proper representation object request.getParameter("prenoms") != null ? request.getParameter("prenoms") : " ";
                String remoteAddr = request.getRemoteAddr() != null ? request.getRemoteAddr() : "";
                String remoteHost = request.getRemoteHost() != null ? request.getRemoteHost() : "";
                String remoteUser= request.getRemoteUser() != null ? request.getRemoteUser() : "";
                String remoteAddrProxy = request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR") : "";
                
           if(statut)
        {                     
                
                //appeller la methode checkCredentials de la classe UtilitycheckCredentials(String filiale,String remoteAddr,String serviceid,String apikey)
                testresp = (new StringBuilder()).
                        append("remoteAddrProxy=").append(remoteAddrProxy).append("\n").
                        append("remoteAddr=").append(remoteAddr).append("\n").
                        append("remoteHost=").append(remoteHost).append("\n").
                        append("remoteUser=").append(remoteUser).append("\n").
                        append("serviceid=").append(serviceid).append("\n").
                        append("api_key=").append(apikey).append("\n").toString();
                        
              /*
                System.out.println("serviceid="+serviceid);
                System.out.println("serviceid="+serviceid);
                System.out.println("serviceid="+serviceid);
                System.out.println("api_key="+apikey);
                System.out.println("remoteAddr="+remoteAddr);
                System.out.println("remoteHost="+remoteHost);
                System.out.println("remoteUser="+remoteUser);
                System.out.println("query="+request.getQueryString());
              
                */
                
                
                // Appeller ensuite la fonction signaletique si checkCredentials()=true
                // Il faudriat aussi journaliser la trnasaction

                //return testresp;
                
               
                transaction.setFacturier(facturier);
                transaction.setCaisse(caisse);
                transaction.setDhtransact(dhtransact);
                transaction.setCodoper(codoper);
                transaction.setRefrel(refrel);
                transaction.setCompteDebit(compteDebit);
                transaction.setCompteCredit(compteCredit);
                transaction.setMontantDebit(montantDebit);
                transaction.setMontantCredit(montantCredit);
                transaction.setMontantCom1(montantCom1);
                transaction.setMontantCom2(montantCom2);
                transaction.setMontantTaxe(montantTaxe);
                transaction.setNarratif(narrartif);
                                
               resultat= appTransact.getTransactions(transaction,filiale);
               
               
       
        
         }
        else
        {
            resultat="{\"success\" : \"false\",\"message\":\"Acces denied\"}"; 
        }
        
       
     
         autInfo= new AuditInfos(filiale, utility.formatageDateJ(), serviceid, 
                               "transaction",apikey, remoteHost, remoteAddr, parmatres, resultat);
               utility.auditConnexion(autInfo);
       
       return resultat;
    }
    
    
    
    
    //annulation transactions 
     @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/annultransact")
    public String getAnnulTranact(@Context HttpServletRequest request,@HeaderParam("idservice") String serviceid,
            @HeaderParam("apikey") String apikey,@FormParam("filiale") String filiale ,
            @FormParam("facturier") String facturier,
            @FormParam("caisse") String caisse,
            @FormParam("dhtransact") String dhtransact,
            @FormParam("refrel") String refrel,
            @FormParam("refOper") String refOper            
            ) throws IOException {
        
        String resultat=null;
        utility=new Utility();
         AppMicroAnnulTransact  appAnnulTransact=new AppMicroAnnulTransact();
        AuditInfos autInfo=null;
        TransactionInfo transaction=new TransactionInfo();
        String parmatres="filiale:"+filiale+",app_user:"+serviceid+
                ",apikey:"+apikey+",facturier :"+facturier+
                ",caisse:"+caisse+",dhtransact:"+dhtransact+
                ",refrel:"+refrel+",refOper:"+refOper;
        
        /*
         System.out.println("apikey :"+apikey);
         System.out.println("serviceid :"+serviceid);
         
         System.out.println("apikey "+apikey);
         System.out.println("filiale ici "+filiale);
         System.out.println("serviceid "+serviceid);
         */
        
        
       
        boolean statut=utility.getServicesAcces(apikey, filiale, serviceid,"annul");
        
          
      //   System.out.println("Resultat "+statut);
        
       
                
                String  testresp="";
                //TODO return proper representation object request.getParameter("prenoms") != null ? request.getParameter("prenoms") : " ";
                String remoteAddr = request.getRemoteAddr() != null ? request.getRemoteAddr() : "";
                String remoteHost = request.getRemoteHost() != null ? request.getRemoteHost() : "";
                String remoteUser= request.getRemoteUser() != null ? request.getRemoteUser() : "";
                String remoteAddrProxy = request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR") : "";
                
                               
                if(statut)
        { 
                //appeller la methode checkCredentials de la classe UtilitycheckCredentials(String filiale,String remoteAddr,String serviceid,String apikey)
                testresp = (new StringBuilder()).
                        append("remoteAddrProxy=").append(remoteAddrProxy).append("\n").
                        append("remoteAddr=").append(remoteAddr).append("\n").
                        append("remoteHost=").append(remoteHost).append("\n").
                        append("remoteUser=").append(remoteUser).append("\n").
                        append("serviceid=").append(serviceid).append("\n").
                        append("api_key=").append(apikey).append("\n").toString();
                        
              /*
                System.out.println("serviceid="+serviceid);
                System.out.println("serviceid="+serviceid);
                System.out.println("serviceid="+serviceid);
                System.out.println("api_key="+apikey);
                System.out.println("remoteAddr="+remoteAddr);
                System.out.println("remoteHost="+remoteHost);
                System.out.println("remoteUser="+remoteUser);
                System.out.println("query="+request.getQueryString());
              
                */
                
                
                // Appeller ensuite la fonction signaletique si checkCredentials()=true
                // Il faudriat aussi journaliser la trnasaction

                //return testresp;
                
               
                transaction.setFacturier(facturier);
                transaction.setCaisse(caisse);
                transaction.setDhtransact(dhtransact);
                transaction.setRefrel(refrel);
                transaction.setRefOper(refOper);
                                                
               resultat= appAnnulTransact.getAnnulTransaction(transaction,filiale);
               
               
       
        
         }
        else
        {
            resultat="{\"success\" : \"false\",\"message\":\"Acces denied\"}"; 
        }
        
         autInfo= new AuditInfos(filiale, utility.formatageDateJ(), serviceid, 
                               "signaletigue",apikey, remoteHost, remoteAddr, parmatres, resultat);
               utility.auditConnexion(autInfo);
         
     // utility.auditConnexion(audit);
       
       return resultat;
    }
    
    
    //solde journalier sur une période 
    
     @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getBalance")
    public String getBalance(@Context HttpServletRequest request,@HeaderParam("idservice") String serviceid,
            @HeaderParam("apikey") String apikey,@FormParam("filiale") String filiale ,
            @FormParam("compte") String compte,
            @FormParam("dateDebut") String dateDebut,
            @FormParam("dateFin") String dateFin                    
            ) throws IOException {
        
        String resultat=null;
        utility=new Utility();
        AppMicroGetBalance balance=new AppMicroGetBalance();
        AuditInfos autInfo=null;
        String parmatres="filiale:"+filiale+",app_user:"+serviceid+
                ",apikey:"+apikey+",compte :"+compte+
                ",Date début:"+dateDebut+",Date fin:"+dateFin;
        
        System.out.println("api key  "+ apikey);
        System.out.println("app_user  "+ serviceid);
        System.out.println("filiale  "+ filiale);
        
      
        boolean statut=utility.getServicesAcces(apikey, filiale, serviceid,"balance");
             
                String  testresp="";
                //TODO return proper representation object request.getParameter("prenoms") != null ? request.getParameter("prenoms") : " ";
                String remoteAddr = request.getRemoteAddr() != null ? request.getRemoteAddr() : "";
                String remoteHost = request.getRemoteHost() != null ? request.getRemoteHost() : "";
                String remoteUser= request.getRemoteUser() != null ? request.getRemoteUser() : "";
                String remoteAddrProxy = request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR") : "";
           
                if(statut)
        { 
                //appeller la methode checkCredentials de la classe UtilitycheckCredentials(String filiale,String remoteAddr,String serviceid,String apikey)
                testresp = (new StringBuilder()).
                        append("remoteAddrProxy=").append(remoteAddrProxy).append("\n").
                        append("remoteAddr=").append(remoteAddr).append("\n").
                        append("remoteHost=").append(remoteHost).append("\n").
                        append("remoteUser=").append(remoteUser).append("\n").
                        append("serviceid=").append(serviceid).append("\n").
                        append("api_key=").append(apikey).append("\n").toString();
                        
             
               
                                                
               resultat= balance.getBalance(filiale, compte, dateDebut, dateFin);
               
               
       
        
         }
        else
        {
            resultat="{\"success\" : \"false\",\"message\":\"Acces denied\"}"; 
        }
        
         autInfo= new AuditInfos(filiale, utility.formatageDateJ(), serviceid, 
                               "balance",apikey, remoteHost, remoteAddr, parmatres, resultat);
               utility.auditConnexion(autInfo);
         
     // utility.auditConnexion(audit);
       
       return resultat;
    }
    
    
      //solde journalier sur une période ppour un client
    
     @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getSoldeClientMultiple")
    public String getSoldeClientMultiple(@Context HttpServletRequest request,@HeaderParam("idservice") String serviceid,
            @HeaderParam("apikey") String apikey,@FormParam("filiale") String filiale ,
            @FormParam("client") String client,
            @FormParam("dateDebut") String dateDebut,
            @FormParam("dateFin") String dateFin                    
            ) throws IOException {
        
        String resultat=null;
        utility=new Utility();
         AppMicroGetBalanceMultiple balanceMultiple=new AppMicroGetBalanceMultiple();
        AuditInfos autInfo=null;
        String parmatres="filiale:"+filiale+",app_user:"+serviceid+
                ",apikey:"+apikey+",compte :"+client+
                ",Date début:"+dateDebut+",Date fin:"+dateFin;
        
        System.out.println("api key  "+ apikey);
        System.out.println("app_user  "+ serviceid);
        System.out.println("filiale  "+ filiale);
        
      
        boolean statut=utility.getServicesAcces(apikey, filiale, serviceid,"balMultiple");
             
                String  testresp="";
                //TODO return proper representation object request.getParameter("prenoms") != null ? request.getParameter("prenoms") : " ";
                String remoteAddr = request.getRemoteAddr() != null ? request.getRemoteAddr() : "";
                String remoteHost = request.getRemoteHost() != null ? request.getRemoteHost() : "";
                String remoteUser= request.getRemoteUser() != null ? request.getRemoteUser() : "";
                String remoteAddrProxy = request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR") : "";
                
      
                if(statut)
        { 
                //appeller la methode checkCredentials de la classe UtilitycheckCredentials(String filiale,String remoteAddr,String serviceid,String apikey)
                testresp = (new StringBuilder()).
                        append("remoteAddrProxy=").append(remoteAddrProxy).append("\n").
                        append("remoteAddr=").append(remoteAddr).append("\n").
                        append("remoteHost=").append(remoteHost).append("\n").
                        append("remoteUser=").append(remoteUser).append("\n").
                        append("serviceid=").append(serviceid).append("\n").
                        append("api_key=").append(apikey).append("\n").toString();
                        
             
               
                                                
               resultat= balanceMultiple.getBalanceMultiple(filiale, client, dateDebut, dateFin);
               
               
       
        
         }
        else
        {
            resultat="{\"success\" : \"false\",\"message\":\"Acces denied\"}"; 
        }
        
         autInfo= new AuditInfos(filiale, utility.formatageDateJ(), serviceid, 
                               "balMultiple",apikey, remoteHost, remoteAddr, parmatres, resultat);
               utility.auditConnexion(autInfo);
         
     // utility.auditConnexion(audit);
       
       return resultat;
    }
    
    
    //bloc de services T24
    
    //methode signaletique T24
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/clientInfT24/{client}")
    public String getSignaletiqueT24(@PathParam("client") String client ) throws ParseException{
            return facture.getInfoClient(client);
   
    }
    
    //Méthode de comptabilisation
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/refTrtT24")
    @Consumes("application/json")
    public String setComptaT24(String paiement) throws ParseException{        
        TransactionT24 oper = new TransactionT24();
        oper = convertJsonTransaction(paiement);        
     
       //String retour = new Gson().toJson(oper);
         
        return facture.setComptaTransaction(oper);
    }
    
    
     //Méthode d'annulation de transaction T24
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/annulrefT24")
    @Consumes("application/json")
    public String annulrefT24(String refFT) throws ParseException, IOException{        
     RefAnnulation opeAnnul =convertJsonAnnulT24(refFT);
        return facture.setAnnulationTransact(opeAnnul);
    }
    
    
    
    
    //conversion de la trame de transaction paiement
    private TransactionT24 convertJsonTransaction(String oper){
        TransactionT24 operT24 = new TransactionT24();
        
        try {
            JSONObject oper1 = new JSONObject(oper);
            operT24.setCompteDebit(oper1.getString("compteDebit"));
            operT24.setFacturier(oper1.getString("facturier"));
            operT24.setClient(oper1.getString("client"));          
            operT24.setIdentifiantFacture(oper1.getString("identifiantFacture"));
            operT24.setMntOper(Integer.parseInt(oper1.getString("mntOper")));
            operT24.setMntFacture(Integer.parseInt(oper1.getString("mntFacture")));
            operT24.setMntFrais(Integer.parseInt(oper1.getString("mntFrais")));
            operT24.setMntFraisMarchand(Integer.parseInt(oper1.getString("mntFraisMarchand")));
            operT24.setMntTimbre(Integer.parseInt(oper1.getString("mntTimbre")));           
               
        } catch (JSONException ex) {
            ex.printStackTrace();
            Logger.getLogger(ServicesResource.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return operT24;
    }
    
    
     //conversion de la trame d'annumlation transaction paiement
    private RefAnnulation convertJsonAnnulT24(String oper){
        RefAnnulation operT24 = new RefAnnulation();
        
        try {
            JSONObject oper1 = new JSONObject(oper);
            operT24.setRefFT(oper1.getString("refFT"));
            operT24.setFacturier(oper1.getString("facturier"));                 
               
        } catch (JSONException ex) {
            ex.printStackTrace();
            Logger.getLogger(ServicesResource.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return operT24;
    }
    
    
    
    
  
   
}



