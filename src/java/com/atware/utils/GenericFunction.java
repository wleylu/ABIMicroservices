/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author melarga.coulibaly
 * Bibliothèque Générique pour la fourniture de fonctions utilitaires diverses
 * 27/11/2011 : v1.0 -> premières fonctions
 */
public class GenericFunction {
    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
    //Declaration
    Calendar calendar = null;
    //GregorianCalendar calendar = new GregorianCalendar();
    //gc.setTime(new java.util.Date(value.getTime()));
    
    private String DayMonthYearSeparator = "/";
    public String DateFormatte = "dd" + DayMonthYearSeparator + "MM" + DayMonthYearSeparator + "yyyy" + " " + "hh" + ":" + "mm" + ":" + "ss";

    public String[] comboTab;
    public int nbreElt = 0;

    public NumberFormat formatter = NumberFormat.getCurrencyInstance();
    public DecimalFormat decFormatter;

    public GenericFunction() {
        if (formatter instanceof DecimalFormat){
            decFormatter = (DecimalFormat) formatter;
            decFormatter.applyPattern("#,##0");
        }
    }

    public static String getEncodedPassword(String key) {
         byte[] uniqueKey = key.getBytes();
         byte[] hash = null;
         try {
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
         } catch (NoSuchAlgorithmException e) {
            throw new Error("no MD5 support in this VM");
         }
         StringBuilder hashString = new StringBuilder();
         for ( int i = 0; i < hash.length; ++i ) {
             String hex = Integer.toHexString(hash[i]);
             if ( hex.length() == 1 ) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length()-1));
             } else {
                hashString.append(hex.substring(hex.length()-2));
             }
         }
         return hashString.toString();
    }

    public static boolean testPassword(String clearTextTestPassword, String encodedActualPassword) throws NoSuchAlgorithmException
    {
        String encodedTestPassword = MD5Password.getEncodedPassword(clearTextTestPassword);
        return (encodedTestPassword.equals(encodedActualPassword));
    }

    public String formatNumberToString(int number,int tailleSting){
        String numFormated = Integer.toString(number);
        int numlength = tailleSting - numFormated.length();

        for (int i = 0; i<=numlength; ++i) {
             numFormated = "0"+numFormated;
        }

        return numFormated;
    }

    public boolean validate_email(String email) {
        int apos = -1;
        int dotpos = -1;

        apos = email.indexOf("@");
        dotpos = email.lastIndexOf(".");

        if (apos < 1 || dotpos - apos < 2) {
            return false;
        } else {
            return true;
        }

    }

    /*
     * Retour Année de la Date Systeme avec un ecart
     * exemple si ecart = -1 et ke l'année encours est 2011 retourne 2010
     * si ecart = 3 et ke l'année encours est 2011 retourne 2014
     *
     */
    public String getYearToStr(int ecart) {
        Date vDate ;
        String dateResult = null;
        calendar = Calendar.getInstance();
        java.util.Date trialTime = new java.util.Date();
        calendar.setTime(trialTime);

        try {
            dateResult = getDayOfMonth() + "/" + getMonthInt() + "/" + (getYear()+ecart) ;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            dateFormat.setLenient(false);
            vDate = dateFormat.parse(dateResult);
            //System.out.println("Date Systeme Sortie "+vDate.toString());
            String dateRetour= dateToStr(vDate);
            //System.out.println("Date  de la Conversion Sortie(dateRetour) "+dateRetour.toString());
            return dateRetour.substring(6);

        } catch (Exception E) {

            return null;

        }

    }

    public String currentDateToStr() {
        Date vDate ;
        String dateResult = null;
        calendar = Calendar.getInstance();
        java.util.Date trialTime = new java.util.Date();
        calendar.setTime(trialTime);

        try {
            dateResult = getDayOfMonth() + "/" + getMonthInt() + "/" + getYear() ;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            dateFormat.setLenient(false);
            vDate = dateFormat.parse(dateResult);
            //System.out.println("Date Systeme Sortie "+vDate.toString());
            String dateRetour= dateToStr(vDate);
            //System.out.println("Date  de la Conversion Sortie(dateRetour) "+dateRetour.toString());
            return dateRetour;

        } catch (Exception E) {

            return null;

        }

    }

    public String currentDatetimeToStr() {


        Date vDate ;
        String dateResult = null;
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        java.util.Date trialTime = new java.util.Date();
        calendar.setTime(trialTime);

        try {
           
            String heure = String.valueOf(getHour());
            if (heure.length()==1) {
                heure = "0"+heure;
                //System.out.println("heure : "+heure);
            }

            String minute = String.valueOf(getMinute());
            if (minute.length()==1){
                minute = "0"+minute;
            }

            String seconde = String.valueOf(getSecond());
            if (seconde.length()==1) {
                seconde = "0"+seconde;
            }

            dateResult = getDayOfMonth() + "/" + getMonthInt() + "/" + getYear() + " " + getHour() + ":" + getMinute() + ":" + getSecond();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            dateFormat.setLenient(false);
            vDate = dateFormat.parse(dateResult);

            //System.out.println("Date Systeme Sortie "+vDate.toString());
           String dateRetour = datetimeToStr(vDate);
           
            return dateRetour;

        } catch (Exception E) {
            return null;
        }

    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonthInt() {        
        return 1 + calendar.get(Calendar.MONTH);
    }

    public int getDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    public int getSecond() {
        return calendar.get(Calendar.SECOND);
    }
//    public String getHour() {
//
//        return calendar.get(Calendar.HOUR_OF_DAY);
//    }
//
//    public String getMinute() {
//        return calendar.get(Calendar.MINUTE);
//    }
//
//    public String getSecond() {
//        return calendar.get(Calendar.SECOND);
//    }

    public Date strToDate(String value) {

        Date vdate = null;

        try {

//            SimpleDateFormat df = new SimpleDateFormat(DateFormatte);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            dateFormat.setLenient(true);

            vdate = dateFormat.parse(value);
            //return new Date(df.parse(value).getTime());
            return vdate;

        } catch (Exception E) {

            return null;

        }

    }

    public String datetimeToStr(Date value) {

        String day = "";
        String month = "";
        String year = "";
        String minute = "";
        String heure = "";
        String seconde = "";


        if (value == null) {

            return "";

        } else {

            GregorianCalendar gc = new GregorianCalendar();

            gc.setTime(new java.util.Date(value.getTime()));

            day = String.valueOf(gc.get(Calendar.DATE));

            month = String.valueOf(gc.get(Calendar.MONTH) + 1);

            year = String.valueOf(gc.get(Calendar.YEAR));

            heure = String.valueOf(gc.get(Calendar.HOUR_OF_DAY));

            minute = String.valueOf(gc.get(Calendar.MINUTE));

            seconde = String.valueOf(gc.get(Calendar.SECOND));

            if (day.length() == 1) {
                day = "0" + day;
            }

            if (month.length() == 1) {
                month = "0" + month;
            }
            if (heure.length() == 1) {
                heure = "0" + heure;
            }
            if (minute.length() == 1) {
                minute = "0" + minute;
            }
            if (seconde.length() == 1) {
                seconde = "0" + seconde;
            }

            String vDate = day + DayMonthYearSeparator + month + DayMonthYearSeparator + year + " " + heure + ":" + minute + ":" + seconde;

            return vDate;

        }

    }

    public String dateToStr(Date value) {

        String day = "";
        String month = "";
        String year = "";
        String minute = "";
        String heure = "";
        String seconde = "";


        if (value == null) {

            return "";

        } else {

            GregorianCalendar gc = new GregorianCalendar();

            gc.setTime(new java.util.Date(value.getTime()));

            day = String.valueOf(gc.get(Calendar.DATE));

            month = String.valueOf(gc.get(Calendar.MONTH) + 1);

            year = String.valueOf(gc.get(Calendar.YEAR));

            heure = String.valueOf(gc.get(Calendar.HOUR_OF_DAY));

            minute = String.valueOf(gc.get(Calendar.MINUTE));

            seconde = String.valueOf(gc.get(Calendar.SECOND));

            if (day.length() == 1) {
                day = "0" + day;
            }

            if (month.length() == 1) {
                month = "0" + month;
            }

            String vDate = day + DayMonthYearSeparator + month + DayMonthYearSeparator + year ;

            return vDate;

        }

    }

    public static String convertDateToString() {
        String result;
        DateFormat formatter;
        int styles = DateFormat.SHORT;
        Date today = new Date();
        formatter = DateFormat.getDateTimeInstance(
                styles, styles, Locale.FRENCH);
        result = formatter.format(today);
        return result;
    }

    public static String formatDateToString(String dateValue,String FormatOUT) {
        String dateStr = "";
        
        if(FormatOUT.equalsIgnoreCase("yyyymmdd")){ 
            if(dateValue.length()==10){                
                dateStr = dateValue.substring(6,10)+dateValue.substring(3,5)+dateValue.substring(0,2);
            }
        }
        return dateStr;
    }
    /***
     * Teste si un élément est contenu dans un tableau
     * @param needle : l'élément à rechercher
     * @param haystack : la tableau
     * @return true si présent, sinon false
     */
    public static boolean in_array(Object needle, Object[] haystack) {
            return (Arrays.binarySearch(haystack, needle) >= 0);
    }
/**<br>
* Met le premier caractere d'une chaine en majuscule, le reste en minscule<br>
* @author Valentin<br>
* @param chaINe<br>
* @return Chaine<br>
*/
public static String ucfirst(String chaine){   
    return chaine.substring(0, 1).toUpperCase()+ chaine.substring(1).toLowerCase();
}        
    public String[] getComboTab() {
        return comboTab;
    }

    public void setComboTab(String[] comboTab) {
        this.comboTab = comboTab;
    }

    public int getNbreElt() {
        return nbreElt;
    }

    public void setNbreElt(int nbreElt) {
        this.nbreElt = nbreElt;
    }

    
}

