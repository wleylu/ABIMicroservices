/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.utils;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Properties;
import org.atware.bean.ConfigInfos;
import org.melware.bean.LogError;
/**
 *
 * @author romeo.kouame
 * Classe de generation des logs de l'application
 * Il est possible de creer :
 *  1 A la fois des fichiers de logs et une journalisation dans une base de donnes
 *  2 Générer uniquement des fichiers logs
 *  3 Journaliser uniquement dans une base données
 */

public class AppLogger {
    private int loggertype;
    private String filebasename;
    private String fileextension;
    private String module;
    private String logdir;
    private String dbdriver;
    private String dburl;
    private String dbuser;
    private String dbpassword;
    
                    
        public AppLogger(int type, String basename,String ext,String module)
    {
        this.loggertype = type;
        this.filebasename = basename;
        this.fileextension = ext;
        this.module = module;
        Properties appConfig = new Properties();
        try {
        appConfig.load(ConfigInfos.class.getClassLoader().getResourceAsStream("org/atware/ressources/config.properties"));
            this.logdir = appConfig.getProperty("APPLOGDIR");
            this.dbdriver = appConfig.getProperty("DRIVER");
            this.dburl = appConfig.getProperty("DBURL");
            this.dbuser = appConfig.getProperty("DBUSER");
            this.dbpassword = appConfig.getProperty("DBPASSWORD");
            
        }catch (IOException ex) {
             ex.printStackTrace();
        }        
    }
    
    public LogError info(String msg, String severity) {
        LogError logerr = new LogError();
        logerr.setErrorcode("00");logerr.setMessage("Operation successfull");
        if (loggertype !=1 && loggertype !=2 && loggertype != 3) {
            logerr.setErrorcode("99");
            logerr.setMessage("Erreur d'initiatlisation de AppLogger - Type de Log non specifié");            
        }
       if (filebasename.isEmpty()) {
           this.filebasename = "message";
       } 
       if (fileextension.isEmpty()) {
           this.fileextension = "log";
       } 
       
       if (logerr.getErrorcode().equalsIgnoreCase("00")) {
           
           if (loggertype == 1 || loggertype ==2 || loggertype ==3 ) {
                // Generer les logs dans fichier, repertoire de destination lu dans le fichier XML de parametrage
               String typevent="INFO";
               String curtime = this.getCurtime();
               String curdate = this.getCurdate("");
               String dateheure = this.getCurdatetime("-");
               String filename = (new StringBuilder()).append(logdir).append(this.filebasename).append(curdate).append(".").append(this.fileextension).toString().trim();
               StringBuilder buf = new StringBuilder();
               buf.append(curtime);
               if (severity.equalsIgnoreCase("E")) {
                   typevent = " ["+this.module+"] [ ERROR ] : ";
               }
               if (severity.equalsIgnoreCase("A")) {
                   typevent = " ["+this.module+"] [ WARNING ] : ";
               }
               if (severity.equalsIgnoreCase("I")) {
                   typevent = " ["+this.module+"] [ INFO ] : ";
               }
               buf.append(typevent);
               buf.append(msg);
               
               FileWriter FILE;
               
               try {               
                   FILE = new FileWriter(filename,true);
                   BufferedWriter bw = new BufferedWriter(FILE); 
                   bw.newLine();
                   PrintWriter pw = new PrintWriter(bw); 
                   pw.print(buf); 
                   pw.close(); 
                   
               } catch (NullPointerException ex) {
                   
                   logerr.setErrorcode("98");
                   logerr.setMessage(ex.getMessage());
                   
               } catch (IOException ex2) {
                   logerr.setErrorcode("97");
                   logerr.setMessage(ex2.getMessage());
                   
               } 
               
           }
       }
        return logerr;
    }    
    
    public String getCurdatetime(String separateur) {
            String strDatetime="";String jour="",mois="",annee="",hh="",mm="",ss="";        
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

            strDatetime = (new StringBuilder()).append(jourstr).append(separateur).append(moisstr).append(separateur).append(year).append(" ").append(hhstr).append(":").append(mmstr).append(":").append(ssstr).toString();
    
            return strDatetime;
    }
    
    public String getCurtime() {
            String strDatetime="";String hh="",mm="",ss="";        
            Calendar cal = Calendar.getInstance();

            int heure = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            int secondes = cal.get(Calendar.SECOND);
            
            String padheure = (new StringBuilder()).append("00").append(heure).toString().trim();
            hh = (new StringBuilder()).append(heure).toString().trim();
            String hhstr = (new StringBuilder()).append(padheure.substring(hh.length())).toString().trim();
            
            String padmin = (new StringBuilder()).append("00").append(minutes).toString().trim();
            mm = (new StringBuilder()).append(minutes).toString().trim();
            String mmstr = (new StringBuilder()).append(padmin.substring(mm.length())).toString().trim();
            
            String padsec = (new StringBuilder()).append("00").append(secondes).toString().trim();
            ss = (new StringBuilder()).append(secondes).toString().trim();
            String ssstr = (new StringBuilder()).append(padsec.substring(ss.length())).toString().trim();

            strDatetime = (new StringBuilder()).append(hhstr).append(":").append(mmstr).append(":").append(ssstr).toString();
    
            return strDatetime;
    }
       
   //Recuperation de la date et heure encours
    public String getCurdate(String separateur) {
            String strDatetime="";String jour="",mois="",annee="",hh="",mm="",ss="";        
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

            strDatetime = (new StringBuilder()).append(jourstr).append(separateur).append(moisstr).append(separateur).append(year).toString();
    
            return strDatetime;
    }
        
}
