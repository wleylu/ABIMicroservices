    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.utils;

/**
 *
 * @author Melarga.COULIBALY
 */

import com.atware.controller.MailingSessions;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.atware.bean.AuditInfos;
import org.atware.bean.ConfigInfos;
import org.atware.bean.DBCredential;
import org.atware.jdbc.Database;
import org.atware.xml.JDBCXmlReader;
import org.json.JSONArray;
import org.json.JSONObject;

// Referenced classes of package org.patware.utils:
//            RegValidator, ResLoader

public class Utility
{

    private static Hashtable paramsCache = new Hashtable();

    public Utility() 
    {
        try {
            config = new ConfigInfos();
            System.out.println("ConfigInfos() successfully called from init AppController");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }           
    }
    public boolean checkCredentials(String filiale,String remoteAddr,String serviceid,String apikey){
            boolean result=false;
            // Recuperer les paramètres de la base de donnée : DBCredential getDBCredential(String filiale)
            DBCredential crdt=null;
            //Connexion a la base de donnee
        try {
            crdt = getDbFiliale(filiale);
            Database dbsession = new Database(crdt.getDbDriver());ResultSet rs=null;
            try
            {              
             dbsession.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
            //executer la requete ci-dessous
            
            
            
            
            return result;
    }
    public static boolean isInteger(String chaine)
    {
        try {
            Integer.parseInt(chaine);
            return true;
        } catch(NumberFormatException nfe) {
            return false;
        }
    }
    public String getpin() {
        int code = 0;
        code = 1000000000 + (int) (Math.random() * 9000000000D);
        code = code < 0 ? -code : code;
        String SecureCode = (new StringBuilder()).append("92").append(code).toString();
        return SecureCode;
    }
      public static String removeAccent(String source)
    {
        if(source == null)
        {
            return "";
        } else
        {
            return Normalizer.normalize(source, java.text.Normalizer.Form.NFD).replaceAll("[^\\p{Alnum}]", " ").replaceAll("\\p{Punct}", " ");
        }
    }

    public static int binHexToBytes(String sBinHex, byte data[], int nSrcPos, int nDstPos, int nNumOfBytes)
    {
        int nStrLen = sBinHex.length();
        int nAvailBytes = nStrLen - nSrcPos >> 1;
        if(nAvailBytes < nNumOfBytes)
        {
            nNumOfBytes = nAvailBytes;
        }
        int nOutputCapacity = data.length - nDstPos;
        if(nNumOfBytes > nOutputCapacity)
        {
            nNumOfBytes = nOutputCapacity;
        }
        int nResult = 0;
        for(int nI = 0; nI < nNumOfBytes; nI++)
        {
            byte bActByte = 0;
            boolean blConvertOK = true;
            for(int nJ = 0; nJ < 2; nJ++)
            {
                bActByte <<= 4;
                char cActChar = sBinHex.charAt(nSrcPos++);
                if(cActChar >= 'A' && cActChar <= 'F')
                {
                    bActByte |= (byte)(cActChar - 65) + 10;
                    continue;
                }
                if(cActChar >= '0' && cActChar <= '9')
                {
                    bActByte |= (byte)(cActChar - 48);
                } else
                {
                    blConvertOK = false;
                }
            }

            if(blConvertOK)
            {
                data[nDstPos++] = bActByte;
                nResult++;
            }
        }

        return nResult;
    }

    public static String computeCleRIB(String ibanque, String iagence, String numeroCompte)
    {
        if(ibanque != null && iagence != null && numeroCompte != null && ibanque.trim().length() == 5 && iagence.trim().length() == 5 && numeroCompte.trim().length() == 12)
        {
            String iban = (new StringBuilder()).append(ibanque.trim()).append(iagence.trim()).append(numeroCompte.trim()).toString().toUpperCase();
            iban = iban.replaceAll("A|J", "1");
            iban = iban.replaceAll("B|K|S", "2");
            iban = iban.replaceAll("C|L|T", "3");
            iban = iban.replaceAll("D|M|U", "4");
            iban = iban.replaceAll("E|N|V", "5");
            iban = iban.replaceAll("F|O|W", "6");
            iban = iban.replaceAll("G|P|X", "7");
            iban = iban.replaceAll("H|Q|Y", "8");
            iban = iban.replaceAll("I|R|Z", "9");
            String temp = iban.substring(0, 5);
            long intTemp = Long.parseLong(temp);
            intTemp %= 97L;
            temp = Long.toString(intTemp);
            temp = (new StringBuilder()).append(temp).append(iban.substring(5, 10)).toString();
            intTemp = Long.parseLong(temp);
            intTemp %= 97L;
            temp = Long.toString(intTemp);
            temp = (new StringBuilder()).append(temp).append(iban.substring(10, 16)).toString();
            intTemp = Long.parseLong(temp);
            intTemp %= 97L;
            temp = Long.toString(intTemp);
            temp = (new StringBuilder()).append(temp).append(iban.substring(16, 22)).toString();
            intTemp = Long.parseLong(temp);
            intTemp %= 97L;
            long cle = ((97L - intTemp) * 100L) % 97L;
            String strCle = Long.toString(cle);
            if(strCle.length() == 1)
            {
                strCle = (new StringBuilder()).append("0").append(strCle).toString();
            }
            if(strCle.equals("00"))
            {
                strCle = "97";
            }
            return strCle;
        } else
        {
            return null;
        }
    }

    public static void clearParamsCache()
    {
        paramsCache.clear();
    }

    public static String bourrageGZero(String chaine, int longueur)
    {
        if(chaine == null)
        {
            chaine = "";
        }
        for(chaine = chaine.trim(); chaine.length() < longueur; chaine = (new StringBuilder()).append("0").append(chaine).toString()) { }
        return chaine;
    }

    public static String bourrageDZero(String chaine, int longueur)
    {
        if(chaine == null)
        {
            chaine = "";
        }
        for(chaine = chaine.trim(); chaine.length() < longueur; chaine = (new StringBuilder()).append(chaine).append("0").toString()) { }
        return chaine;
    }

    public static String bourrageGauche(String chaine, int longueur, String bourrage)
    {
        if(chaine == null)
        {
            chaine = "";
        }
        chaine = chaine.trim();
        if(chaine.length() > longueur)
        {
            chaine = chaine.substring(chaine.length() - longueur, longueur + 1);
        }
        for(; chaine.length() < longueur; chaine = (new StringBuilder()).append(bourrage).append(chaine).toString()) { }
        return chaine;
    }

    public static String bourrageDroite(String chaine, int longueur, String bourrage)
    {
        if(chaine == null)
        {
            chaine = "";
        }
        chaine = chaine.trim();
        if(chaine.length() > longueur)
        {
            chaine = chaine.substring(0, longueur);
        }
        for(; chaine.length() < longueur; chaine = (new StringBuilder()).append(chaine).append(bourrage).toString()) { }
        return chaine;
    }
/*
    public static boolean applicationIsValid(String str_after_spy, String str_before_spy, String str_date_spy)
    {
        RegValidator usingReg = new RegValidator(str_after_spy, str_before_spy, str_date_spy);
        return usingReg.testValidity();
    }
*/
    public static Date convertStringToDate(String chaine, String pattern)
    {
        if(chaine.equalsIgnoreCase("00000000") || chaine.trim().equals(""))
        {
            if(chaine.equalsIgnoreCase("00000000"))
            {
                return  null;// convertStringToDate(getParam("DATECOMPENS_NAT"), "yyyyMMdd");
            } else
            {
                return new Date(System.currentTimeMillis());
            }
        }
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        Date result = null;
        try
        {
            result = new Date(sd.parse(chaine).getTime());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return new Date(System.currentTimeMillis());
        }
        return result;
    }

    public static String convertDateToString(java.util.Date date, String pattern)
    {
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        String result = null;
        try
        {
            result = sd.format(date);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public static void printResultLine(Process p)
        throws Exception
    {
        BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while((line = is.readLine()) != null) 
        {
            System.out.println((new StringBuilder()).append((new Date(System.currentTimeMillis())).toString()).append(": ").append(line).toString());
        }
    }

    public static void executeLineInProcess(Process p, String cmdLine)
        throws Exception
    {
        BufferedWriter ous = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        ous.write(cmdLine);
        ous.newLine();
        printResultLine(p);
    }

    public static Process execute(String cmdLine)
        throws Exception
    {
        if(!cmdLine.equals(""))
        {
            Process p = Runtime.getRuntime().exec(cmdLine);
            printResultLine(p);
            return p;
        } else
        {
            return null;
        }
    }
    //methode de l'auddit des connexions
    
    public void auditConnexion(AuditInfos audit) throws IOException{
             DBCredential dbcnx=new DBCredential();
        config=new ConfigInfos();
        String url = "", user = "", pass = "";
        StringBuilder sb = new StringBuilder();
        Database dbsession;PreparedStatement ps;
        //ResultSet lst=null; 
        Connection cn;
        url=config.getDburl();
        user=config.getDbuser();
        pass=config.getDbpassword();
        //CallableStatement cstmt;int vnbre=0;
        dbcnx = getDbFiliale(audit.getFiliale());
        
        System.out.println("com.atware.utils.Utility.getServicesAcces()"+config.getMmgdbdriver().trim());
        dbsession = new Database(config.getMmgdbdriver().trim());
       
        boolean vStatutAcces=false;
        boolean rs=false;
        try {

            dbsession.open(url.trim(), user.trim(), pass.trim());
             
        } catch (Exception e) {
            e.printStackTrace();
           
        }
            String sql = (new StringBuilder()).append
                            ("INSERT INTO auditinfos(filiale, datoper, app_user, service_code, "
                                    + "api_key, hostname,ipaddress, request_data, result_data)"
                                    + "VALUES (?, ?, ?, ?, ?, ?,?, ?, ?);").toString();
            
            try {
                
                cn = dbsession.getConn();
                ps = cn.prepareStatement(sql);
                ps.setString(1, audit.getFiliale());
                ps.setString(2, audit.getDatoper());
                ps.setString(3, audit.getApp_user());
                ps.setString(4, audit.getService_code());
                ps.setString(5, audit.getApiKey());
                ps.setString(6, audit.getHostName());
                ps.setString(7, audit.getIdAddress());
                ps.setString(8, audit.getResquestData());
                ps.setString(9, audit.getResultData()); 
          
                rs=ps.execute(); 
                
                  dbsession.close();
              
            } catch (Exception ex) {
                ex.printStackTrace();
              ;
             //   return result;
            }
      
               
        
        
    }
    
    
    //paramètres de conection de la filiale
    
    public DBCredential getDbFiliale(String filiale) throws IOException{
        ConfigInfos infos=new ConfigInfos();
        JSONObject dbjson=null;
        String restul=null;
        String json=getResultatJson();
        DBCredential dbcredential=null;



                    try {


                              dbjson = new JSONObject(json );
                              JSONArray array = dbjson.getJSONArray("DBFILIALE");
                              

                               for(int i=0; i<array.length();i++)
                              {
                                     JSONObject valeurDB = array.getJSONObject(i);
                                     if(valeurDB.getString("filiale").equals(filiale)){
                                         dbcredential=new DBCredential();
                                         dbcredential.setDbDriver(valeurDB.getString("driver"));
                                         dbcredential.setUsername(valeurDB.getString("user"));
                                         dbcredential.setPassword(valeurDB.getString("password"));
                                         dbcredential.setUrl(valeurDB.getString("url"));
                                         
                                     }
                                        
                              }

                    }
                    catch (Exception e)
                    {
                      e.printStackTrace();
                    }



        return dbcredential;
    }

    

    public String getResultatJson() throws IOException{
         String chaine="";

        try{
         InputStream ips=(Utility.class.getClassLoader().getResourceAsStream("org/atware/ressources/dbConfig.json"));
         InputStreamReader ipsr=new InputStreamReader(ips);
         BufferedReader br=new BufferedReader(ipsr);
         String ligne;
         while ((ligne=br.readLine())!=null){
             chaine+=ligne+"\n";
         }
         br.close();
            }
            catch (Exception e){
               System.out.println(e.toString());
            }

        return chaine;

    }
    
    

    public static String removeFileSuffixe(File aFile, String suffixe)
    {
        return aFile.getName().substring(0, aFile.getName().lastIndexOf(ResLoader.getMessages(suffixe)));
    }

    public static String removeFileNameSuffixe(String fileName, String suffixe)
    {
        return fileName.substring(0, fileName.lastIndexOf(suffixe));
    }

    public static void tattooPicture(BufferedImage bimg, File newVersoFile, String paramTextToTattoo)
        throws IOException
    {
        String textToTattoo = ""; //getParam(paramTextToTattoo).trim();
        String tattooStyle = ResLoader.getMessages("TattooStyle") == null ? "Arial-BOLD-20" : ResLoader.getMessages("TattooStyle");
        Graphics g = bimg.getGraphics();
        g.setColor(Color.BLACK);
        g.setFont(Font.decode(tattooStyle));
        g.drawString((new StringBuilder()).append(ResLoader.getMessages("TattooPreText")).append(" ").append(textToTattoo).toString(), Integer.parseInt(ResLoader.getMessages("TattooX")), Integer.parseInt(ResLoader.getMessages("TattooY")));
    }
    public String emailDocument(String email,String subject, String corpsmsg,String filepath){
           String status="NOK";
           //sessionMailBean
           //SessionMailBean mailer = new SessionMailBean();
           try {
                sessionMailBean.sendMessage(email, subject,corpsmsg, filepath);
                status="OK";
           } catch(MessagingException mx){
               return status;
           }
           return status;
       }
    @SuppressWarnings("UseSpecificCatch")
    public StringBuilder getStringBuilderFromClob ( Clob clobData){
        StringBuilder sb = new StringBuilder();
                char[] buffer;
                int count = 0;
                int length = 0;
                String data = null;
                String[] type;
                if (clobData != null) {
                  try {
                        Reader is = clobData.getCharacterStream();
                        sb = new StringBuilder();                    
                        length = (int) clobData.length();
                        buffer = new char[length];
                        count = 0;
                        while ((count = is.read(buffer)) != -1) {sb.append(buffer); System.out.println(" sb recupere");}
                        is.close();
                        // Remplacer les || par des caracteres de fin de linge (System.getProperty("line.separator")
                        String substring="||"; int position=sb.lastIndexOf(substring);int it=0;
                        while (position> 0){
                            it++;
//                            System.out.println("Ligne "+it);
                            sb.replace(position, position + substring.length(), System.getProperty("line.separator"));
                            position=sb.lastIndexOf(substring);
                        }
                        System.out.println(" text="+sb.toString());
                    
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
              
                }        
    
        return sb;
    }
    public StringBuilder StringBuilderReplace(StringBuilder in,String from,String to){
        StringBuilder sb =  new StringBuilder();
        String substring=from; int position=in.lastIndexOf(substring);
        while (position> 0){
            in.replace(position, position + substring.length(), to);
            position=in.lastIndexOf(substring);
        }    
    
        return in;
    }
    public StringBuilder getStringFromClob ( Clob clobData){
        StringBuilder sb = new StringBuilder();
                char[] buffer;
                int count = 0;
                int length = 0;
                String data = null;
                String[] type;
                if (clobData != null) {
                  try {
                      try (Reader is = clobData.getCharacterStream()) {
                          sb = new StringBuilder();
                          length = (int) clobData.length();
                          buffer = new char[length];
                          count = 0;
                          while ((count = is.read(buffer)) != -1) {sb.append(buffer); System.out.println(" sb recupere");}
                      }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
              
                }        
    
        return sb;
    }   
    public boolean genReportFile (String typ,StringBuilder sb,String client,String compte){
        boolean status =false;
        BufferedWriter bw = null;
        FileWriter fw = null;
        String FILENAME = config.getReportpath()+typ+"_"+client+"_"+compte+".txt";
        try {
            fw = new FileWriter(FILENAME);
            bw = new BufferedWriter(fw);
            bw.write(sb.toString());
            status =true;
            bw.close();
            fw.close();
                
        } catch (IOException e) {
           e.printStackTrace();
        }    
        return status;
    }
    public String getCurdate(String separateur) {

        Calendar cal = new GregorianCalendar();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        //int day = cal.get(Calendar.DAY_OF_WEEK);
        return (new StringBuilder()).append(day).append(separateur).append(month + 1).append(separateur).append(year).toString().trim();
    }
    public String getCurdatetime(String separateur) {
        @SuppressWarnings("UnusedAssignment")
        String strDatetime = "";
        @SuppressWarnings("UnusedAssignment")
                String jour = "";
        String mois = "", annee = "", hh = "", mm = "", ss = "";
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int heure = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int secondes = cal.get(Calendar.SECOND);
        String padjour = (new StringBuilder()).append("00").append(day).toString().trim();
        jour = (new StringBuilder()).append(day).toString().trim();
        String jourstr = (new StringBuilder()).append(padjour.substring(jour.length())).toString().trim();

        String padmois = (new StringBuilder()).append("00").append(month).toString().trim();
        mois = (new StringBuilder()).append(month).toString().trim();
        String moisstr = (new StringBuilder()).append(padmois.substring(mois.length())).toString().trim();

        String padheure = (new StringBuilder()).append("00").append(heure).toString().trim();
        hh = (new StringBuilder()).append(heure).toString().trim();
        String hhstr = (new StringBuilder()).append(padheure.substring(hh.length())).toString().trim();

        String padmin = (new StringBuilder()).append("00").append(minutes).toString().trim();
        mm = (new StringBuilder()).append(minutes).toString().trim();
        String mmstr = (new StringBuilder()).append(padmin.substring(mm.length())).toString().trim();

        String padsec = (new StringBuilder()).append("00").append(secondes).toString().trim();
        ss = (new StringBuilder()).append(secondes).toString().trim();
        String ssstr = (new StringBuilder()).append(padsec.substring(ss.length())).toString().trim();

        strDatetime = (new StringBuilder()).append(jourstr).append(separateur).append(moisstr).append(separateur).append(year).append(" ").append(hhstr).append(mmstr).append(ssstr).toString();

        return strDatetime;
    }
    
    public boolean getServicesAcces(String apiKey,String filiale,String appUser,String idMethode) throws IOException{
       // DBCredential dbcnx=new DBCredential();
        config=new ConfigInfos();
        String result = "99";
        String url = "", user = "", pass = "";
        StringBuilder sb = new StringBuilder();
        Database dbsession;PreparedStatement ps;
        //ResultSet lst=null; 
        Connection cn;
        url=config.getDburl();
        user=config.getDbuser();
        pass=config.getDbpassword();
        //CallableStatement cstmt;int vnbre=0;
        
        //  dbcnx = getDbFiliale(filiale);
        
      //  System.out.println("com.atware.utils.Utility.getServicesAcces()"+config.getMmgdbdriver().trim());
        dbsession = new Database(config.getMmgdbdriver().trim());
        
        boolean vStatutAcces=false;
        ResultSet rs = null;
        try {

            dbsession.open(url.trim(), user.trim(), pass.trim());
             
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
            String sql = (new StringBuilder()).append
                       ("select distinct h.api_key,h.filiale,h.app_user from habilitations h,micro_services m" +
                        "  where m.service_code=h.app_user and  app_user=? and api_key=? " +
                        " and h.filiale=? and m.status=1 and m.function_code=h.mscode and h.status=1"
                               + " and m.function_code=?").toString();
            /*String sql = (new StringBuilder()).append
                            ("select * from habilitations  where  status=1 and app_user='ATPS'").toString();*/
            
            
            try {
                
                cn = dbsession.getConn();
                ps = cn.prepareStatement(sql);
                ps.setString(1, appUser);
                ps.setString(2, apiKey);
                ps.setString(3, filiale);
                ps.setString(4, idMethode);
          
                rs=ps.executeQuery(); 
                
                   if(rs.next()){
                      //  System.out.println("SQL = "+sql);
                       vStatutAcces = true;
                   }
                    
              
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
             //   return result;
            }
        dbsession.close();
               
        return  vStatutAcces;
    }
    //fin get Access
    
    public String formatageDateJ(){
        java.util.Date d=new java.util.Date();
        SimpleDateFormat fdate=new SimpleDateFormat("dd/MM/yyyy");
        
        String result=fdate.format(d);
        
        return result;
    }
          
    
     public ConfigInfos config = null;
     MailingSessions sessionMailBean;

}
