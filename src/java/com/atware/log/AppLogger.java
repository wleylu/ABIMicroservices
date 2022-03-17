/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.log;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.atware.bean.ConfigInfos;

/**
 *
 * @author Yacouba KONE
 */
@SuppressWarnings({"CallToPrintStackTrace","UnusedAssignment","UseSpecificCatch"})
public class AppLogger {
    
   
    public AppLogger(String message){
        
        
        String date=null;
        String filename=null;
        FileWriter file;
        
        try {
            ConfigInfos infos=new ConfigInfos();
            date=formatageDate();
            filename="logEfacture"+date+".log";
            file= new FileWriter(new File(infos.getApplogdir()+"/"+filename),true);
            BufferedWriter out=new BufferedWriter(file);
            out.write(message);
            out.newLine();
            
            out.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
            public AppLogger(){}

    
            public String formatageDate(){
                    Date d=new Date();
                    SimpleDateFormat sd=null;
                   
                    String s=null;
                try {                         
             
                    sd=new SimpleDateFormat("yyyMMdd");
                 s=sd.format(d);

                } catch (Exception e) {
                    s=null;
                    e.printStackTrace();
                }
                   
           
                 return s;
            }


                 public String formatageDateHeure() throws ParseException{
                 Date d=new Date();
                 SimpleDateFormat sd=new SimpleDateFormat("yyyMMdd HH:mm:ss");
                 String s=null;
                     try {
                         s=sd.format(d);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 

                 return s;
            }   
                 
                 
                 
                 public String formatDatoper() throws ParseException{
                 Date d=new Date();
                 SimpleDateFormat sd=new SimpleDateFormat("yyy-MM-dd");
                 String s=null;
                     try {
                         s=sd.format(d);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 

                 return s;
            } 
                 
             public String getrRfExterne(){
                 Date d=new Date();
                 SimpleDateFormat sd=new SimpleDateFormat("yyyMMddHHmmssMS");              
                 String s=null;
                     try {
                         s=sd.format(d);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 

                 return s;
            }  
             
             public String getrRfExtdate(){
                 Date d=new Date();
                 SimpleDateFormat sd=new SimpleDateFormat("yyyMMddHHmm");              
                 String s=null;
                     try {
                         s=sd.format(d);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 

                 return s;
            }  
             
             
             
    @SuppressWarnings("UnusedAssignment")
              public String formatDateFacture(int typeRetour,String madate) throws ParseException{
              //   String date_s = " 22-sep-2021 17:00:12"; 
           //String date_s = "Mon, 20 Sep 2021 10:04:56 GMT";

                // *** note that it's "yyyy-MM-dd hh:mm:ss" not "yyyy-mm-dd hh:mm:ss"  
                SimpleDateFormat dt = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss",Locale.ENGLISH);
                Date date = dt.parse(madate);
                 SimpleDateFormat dt1 = null;
                 String resultat = null;
                
                switch (typeRetour) {
                        case  1:
                                dt1 = new SimpleDateFormat("yyyyMMdd");
                                resultat =  dt1.format(date);
                                break;
                        case 2 : 
                                dt1 = new SimpleDateFormat("HHmm");
                                resultat =  dt1.format(date);
                                break;
                           default:
                                dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                resultat =  dt1.format(date);  
                            
                }
          
                 return resultat;
            } 
              
              
                public int formatDateFacture(int typeRetour) throws ParseException{
                               String resultat = null;
                               SimpleDateFormat dt1 = null;
                               switch (typeRetour){
                                case 1:
                                        dt1 = new SimpleDateFormat("yyyyMMdd");
                                        resultat =  dt1.format(new Date());
                                        break;
                                case 2:
                                        dt1 = new SimpleDateFormat("HHmmss");
                                    resultat =  dt1.format(new Date());
                                break;
                                   default :
                                       dt1 = new SimpleDateFormat("yyyyMMdd");
                                       resultat =  dt1.format(new Date());
                               }
                               
                                                  
          
                 return Integer.parseInt(resultat);
            } 

}
