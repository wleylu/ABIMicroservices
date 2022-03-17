/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.utils;


import org.atware.bean.AccessContectUser;
import com.atware.log.AppLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.atware.bean.ConfigSys;
import org.atware.bean.ConfigPay;

/**
 *
 * @author yacou.kone
 */
@SuppressWarnings("FieldMayBeFinal")
public class AppAuthorisation {
        private AppLogger appLoger;        
        private ConfigPay infoFacturier;
        private ConfigSys configSys;
        private GenerateToken gesToken;

//    public ConfigPay getInfoFacturier() {
//        return infoFacturier;
//    }
        
        
    
        public AppAuthorisation() throws Exception{
            appLoger= new AppLogger();
            infoFacturier = new ConfigPay(("CIE"));
            configSys = new ConfigSys();
            gesToken = new GenerateToken();
        }
    
      //gestion de l'authentification des comptes utilisateurs
        @SuppressWarnings({"UnusedAssignment", "UseSpecificCatch", "CallToPrintStackTrace"})
       public String appConnexionuUser(AccessContectUser user) throws Exception{
           AccessContectUser users = new AccessContectUser();
           users.setLogin(user.getLogin());           
           
           
           String  retourTrt="{\"codeTrt\":false,\"message\":\"\"}";
           
           Connection conex = null;
           PreparedStatement ps,psInsert,psUpdate = null;
           ResultSet rs=null;
           String sql=String.format("select login,token_valid,dateaccess,heureaccess from connecte_access where login=?");
           String sqlInsert =String.format("INSERT INTO connecte_access(login, adip, token_valid, dateaccess, heureaccess, blockedlogin) "
                   + "VALUES (?, ?, ?, ?, ?, ?)");
           
           String sqlupd =String.format("UPDATE connecte_access SET adip=?, token_valid=?, dateaccess=?, heureaccess=?,blockedlogin=?"
                   + "	WHERE login=?");
            if (user.getLogin() != null && (user.getLogin().length() > 2))
               {
                      conex=maConnexion();
                     if (getUserConnect(user.getLogin(),conex)){ 
                    
                                users.setBlockedLogin(true);
                                users.setAdip(CrytageDonnees.getInfoDonnes(user.getAdip(), 0, user.getLogin()));
                                users.setTokenValid(gesToken.generateToken(100));
                                users.setDateAccess(appLoger.formatDateFacture(1));
                                users.setHeureAcesses(appLoger.formatDateFacture(2));

                                try {
                                               
                                                     ps = conex.prepareStatement(sql);
                                                     ps.setString(1,users.getLogin());
                                                     rs=ps.executeQuery();
                                                     if (rs.next()){

                                                              psUpdate =conex.prepareStatement(sqlupd);
                                                              psUpdate.setString(6, users.getLogin());
                                                              psUpdate.setString(1, users.getAdip());
                                                              psUpdate.setString(2, users.getTokenValid());
                                                              psUpdate.setInt(3, users.getDateAccess());
                                                              psUpdate.setInt(4, users.getHeureAcesses()+configSys.getTemps());
                                                              psUpdate.setBoolean(5, false);

                                                              psUpdate.executeUpdate();


                                                              retourTrt="{\"codeTrt\":true,\"message\":\""+users.getTokenValid()+"\"}";
                                                     }
                                                     else
                                                     {

                                                         psInsert =conex.prepareStatement(sqlInsert);
                                                         psInsert.setString(1, users.getLogin());
                                                         psInsert.setString(2, users.getAdip());
                                                         psInsert.setString(3, users.getTokenValid());
                                                         psInsert.setInt(4, users.getDateAccess());
                                                         psInsert.setInt(5, users.getHeureAcesses()+configSys.getTemps());
                                                         psInsert.setBoolean(6, false);

                                                         psInsert.execute();

                                                         retourTrt="{\"codeTrt\":true,\"message\":\""+users.getTokenValid()+"\"}";
                                                     }
                                                   conex.close();
                                        }


                                        catch (Exception e) {
                                                     e.printStackTrace();
                                                     retourTrt="{\"codeTrt\":false,\"message\":\"\"}";
                                                 }
                                    
                    }
                     else
                     {
                         retourTrt="{\"codeTrt\":false,\"message\":\"\"}";
                     }
               }
                         else
                            {
                               retourTrt="{\"codeTrt\":false,\"message\":\"\"}";
                            }
        
           return retourTrt;
       }
       ///consultation et appel de service
       
        public boolean appGetConnexionuUser(AccessContectUser user) throws Exception{
           
           String idrip = null;
           boolean retourTrt = false;
           
           Connection conex = null;
           PreparedStatement ps,psInsert,psUpdate = null;
           ResultSet rs=null;
           String sql=String.format("select login,token_valid,dateaccess,heureaccess,adip from connecte_access where login=?");
            String sqlInsert =String.format("INSERT INTO connecte_access(login, adip, token_valid, dateaccess, heureaccess, blockedlogin) "
                   + "VALUES (?, ?, ?, ?, ?, ?)");       
           
           String sqlupd =String.format("UPDATE connecte_access SET heureaccess=? WHERE login=?");
           int dateJ= appLoger.formatDateFacture(1); 
           int heurej = appLoger.formatDateFacture(2); 
           
             if ((user.getLogin() != null) && (user.getLogin().length() > 2) 
                     && (user.getTokenValid()!= null) && (user.getTokenValid().length() > 40 )){
                 conex=maConnexion();
                if (getUserConnect(user.getLogin(),conex)){                
                        try {
                       
                            ps = conex.prepareStatement(sql);
                            ps.setString(1,user.getLogin());
                            rs=ps.executeQuery();
                            if (rs.next()){

                                idrip = CrytageDonnees.getInfoDonnes(rs.getString("adip"), 1, user.getLogin());

                                if ((idrip.equals(user.getAdip())) && (rs.getString("token_valid").equals(user.getTokenValid()))){

                                    if (dateJ > rs.getInt("dateaccess")){
                                           retourTrt= false;
                                         }
                                else if ((dateJ == rs.getInt("dateaccess")) && (heurej > rs.getInt("heureaccess"))){

                                     retourTrt= false;
                                }
                                 else if ((dateJ == rs.getInt("dateaccess")) && (heurej < rs.getInt("heureaccess"))){

                                     psUpdate =conex.prepareStatement(sqlupd);
                                     psUpdate.setString(2, user.getLogin());
                                     psUpdate.setInt(1, rs.getInt("heureaccess")+configSys.getTemps());

                                     psUpdate.executeUpdate();

                                     retourTrt= true;
                                }
                                }
                                else
                                {
                                    retourTrt= false;
                                }

                            }
                            else
                            {
                               retourTrt = false;
                            }

                         conex.close();   
                        } catch (Exception e) {
                            retourTrt = false;
                    
                }
                }
                else
                {
                  retourTrt = false;  
                }
                  
             }
             else
             {
                 retourTrt = false;
             }
           return retourTrt;
       }
       
       
        //fin gestion de l'authentification des comptes utilisateurs
        
        

     private boolean getUserConnect(String login,Connection con){
     
         String sql = String.format("select login from utilisateur where login=?");
         PreparedStatement ps = null;
         boolean retourTrt=false;
         ResultSet rs = null;
                if ((login != null) &&  (login.length() > 2 )){   
                    try {
                         //   con= maConnexion();
                            ps=con.prepareStatement(sql);
                            ps.setString(1, login);
                            rs=ps.executeQuery();
                            
                                if (rs.next()){
                                    retourTrt=true;
                                }
                          
                        } 
                    catch (Exception e) {
                         e.printStackTrace();
                         retourTrt=false;
                        }
                }
         return retourTrt;
     }
       
       
         
    //connexion à la base de données
    private Connection maConnexion(){
        Connection con =null;        
        try {
            
            Class.forName(infoFacturier.getDbdriver());
          //  System.out.println(getInfoFacturier().getDbUrl());
            con = DriverManager.getConnection(infoFacturier.getDbUrl(), infoFacturier.getDbuser(), infoFacturier.getDbpassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
    
}
