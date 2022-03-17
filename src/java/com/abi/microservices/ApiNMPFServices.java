    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abi.microservices;

import com.atware.controller.AppNmpf;
import com.atware.utils.AppAuthorisation;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import static jdk.nashorn.internal.objects.NativeString.trim;
import org.atware.bean.AccessContectUser;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author yacou.kone
 */
@Path("serviceNMPF")
public class ApiNMPFServices {

    @Context
    private UriInfo context;
    
    private AppNmpf nmpf;
    private AppNmpf nmpfSodeci;
    private AppAuthorisation connectedEf;
  

    
   

    /**
     * Creates a new instance of ApiNMPFServices
     */
  

    public ApiNMPFServices() throws Exception {
        nmpf = new AppNmpf("CIE");
        nmpfSodeci= new AppNmpf("SODECI");
        connectedEf = new AppAuthorisation();
    }
    
    
     @GET
    @Path("/index")
    public String getXml() {
        
        return "<h1>Bienvenue dans l'APIs NMPF</h1><br>";
              
       
    }
    
    
    
    @POST
    @Path("/authConnect")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAutorisation(@Context HttpServletRequest request,@HeaderParam("login") String login) throws  Exception{   
          String text = request.getRemoteHost();
          AccessContectUser user = new AccessContectUser();
          user.setAdip(text);
          user.setLogin(login); 
                           
        return connectedEf.appConnexionuUser(user);
          
    }
    
    @GET
    @Path("/getfacturecie/{facture}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getfacturecie(@PathParam("facture") String facture) {   

String txt ="{\n" +
"\n" +
"\"CodeTraitement\": 0,\"MessageTraitement\": \"SUCCES\",\"ReferenceContrat\": \"024314819000 \",\"AdresseTechnique\": \"024314819000\",\"NombreFacture\": 3,\"MontantTotal\": 0,\"ListeDesFactures\": [{\"CODE_TYP_FAC\": \"EG\",\"TYP_FAC\": \"Facture d'Emission Normale\",\"PER_FAC\": \"03/2020\",\"NUM_FAC\": \"000097870103202001\",\"MONTANT_TOTAL\": 8685,\"SOLDE_FACTURE\": 6260,\"MONTANT_PENALITE\": 2425,\"SIGNE\": \"+\",\"DATE_LIMIT\": \"15/07/2020\"},{\"CODE_TYP_FAC\": \"ACC\",\"TYP_FAC\": \"Paiement par avance\",\"PER_FAC\": \"12/2018\",\"NUM_FAC\": \"000097870111201801\",\"MONTANT_TOTAL\": 8545,\"SOLDE_FACTURE\": 8545,\"MONTANT_PENALITE\": 0,\"SIGNE\": \"-\",\"DATE_LIMIT\": \"\"}]\n" +
"}";

String text2 =  "{\"CodeTraitement\":10,\"MessageTraitement\":\"Reference du contrat non fournie\"";

JSONObject json= null;

        try {
            json = new JSONObject(txt);
            String txt1 = trim(json.getString("ReferenceContrat"));
            String txt3 = trim(facture);
            
            if (txt1.equals(txt3)){
                text2 = txt;
            }
            else
            {
                text2 = "{\"CodeTraitement\":10,\"MessageTraitement\":\"Reference du contrat non fournie\"";
            }
           
        } catch (Exception e) {
            e.printStackTrace();
             text2 = "{\"CodeTraitement\":10,\"MessageTraitement\":\"Reference du contrat non fournie\"";
        }
      
  
        
         return text2;


          
    }
    
    @POST
    @Path("/authef")
    public boolean getAuth(@Context HttpServletRequest request,@HeaderParam("login") String login,@HeaderParam("tokenef") String tokenef) 
            throws Exception{   
          String text = request.getRemoteHost();
          AccessContectUser user = new AccessContectUser();
          user.setAdip(text);
          user.setTokenValid(tokenef);
          user.setLogin(login);
                    
        return connectedEf.appGetConnexionuUser(user);
          
    }
    

    
     @GET
   @Path("/consulfact/cie/{facture}")
    public String consulterFacturecie(@PathParam("facture") String facture ) throws IOException {     
     
        return nmpf.consulterFactureCIE(facture);
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reglFacture/cie")
    @Consumes("application/json")    
    public String reglementFactureCIE(String fact){
        
        return nmpf.reglementFactureCIE(fact);
    }
    
   @GET
   @Path("/consulfact/sodeci/{facture}")
    public String consulterFacturesodeci(@PathParam("facture") String facture ) throws IOException {     
     
        return nmpfSodeci.consulterFactureSODECI(facture);
    }
    
    
      
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reglFacture/sodeci")
    @Consumes("application/json")    
    public String reglementFacturesodeci(String fact){
        
        return nmpfSodeci.reglementFactureSODECI(fact);
    }

  
}
