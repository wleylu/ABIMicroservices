/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.xml;

/**
 *
 * @author Melarga.COULIBALY
 */

public class JDBCXmlReader
{

    private static XMLFileReader xmlFileReader = new XMLFileReader("/org/atware/ressources/JDBC.xml");

    public JDBCXmlReader()
    {
    }

    public static String getMode()
    {
        return xmlFileReader.getListNodeValue("/JDBC/MODE")[0];
    }

    public static String getDataSource()
    {
        return xmlFileReader.getListNodeValue("/JDBC/DATASOURCE")[0];
    }

    public static String getDriver()
    {
        return xmlFileReader.getListNodeValue("/JDBC/DRIVER")[0];
    }

    public static String getUrl()
    {
        return xmlFileReader.getListNodeValue("/JDBC/URL")[0];
    }

    public static String getUser()
    {
        return xmlFileReader.getListNodeValue("/JDBC/USER")[0];
    }

    public static String getPassword()
    {
        return xmlFileReader.getListNodeValue("/JDBC/PASSWORD")[0];
    }
    public static String getTimeout()
    {
        return xmlFileReader.getListNodeValue("/JDBC/TIMEOUT")[0];
    }

    public static String getBaciurl()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BACIURL")[0];
    }

    public static String getBaciuser()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BACIUSER")[0];
    }

    public static String getBacipassword()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BACIPASSWORD")[0];
    }
    public static String getBaneurl()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BANEURL")[0];
    }

    public static String getBaneuser()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BANEUSER")[0];
    }

    public static String getBanepassword()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BANEPASSWORD")[0];
    }
    public static String getBabnurl()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BABNURL")[0];
    }

    public static String getBabnuser()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BABNUSER")[0];
    }

    public static String getBabnpassword()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BABNPASSWORD")[0];
    }
    public static String getBatgurl()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BATGURL")[0];
    }

    public static String getBatguser()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BATGUSER")[0];
    }

    public static String getBatgpassword()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BATGPASSWORD")[0];
    }
    public static String getBabfurl()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BABFURL")[0];
    }

    public static String getBabfuser()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BABFUSER")[0];
    }

    public static String getBabfpassword()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BABFPASSWORD")[0];
    }
    public static String getBamlurl()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BAMLURL")[0];
    }

    public static String getBamluser()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BAMLUSER")[0];
    }

    public static String getBamlpassword()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BAMLPASSWORD")[0];
    }
    public static String getBasnurl()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BASNURL")[0];
    }

    public static String getBasnuser()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BASNUSER")[0];
    }

    public static String getBasnpassword()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BASNPASSWORD")[0];
    }
    public static String getBacmurl()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BACMURL")[0];
    }

    public static String getBacmuser()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BACMUSER")[0];
    }

    public static String getBacmpassword()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BACMPASSWORD")[0];
    }
    // bagb params
    public static String getBagburl()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BAGBURL")[0];
    }

    public static String getBagbuser()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BAGBUSER")[0];
    }

    public static String getBagbpassword()
    {
        return xmlFileReader.getListNodeValue("/JDBC/BAGBPASSWORD")[0];
    }
    
    //
    public static String getOrionhost()
    {
        return xmlFileReader.getListNodeValue("/JDBC/ORIONHOST")[0];
    }
    public static String getMonediahost()
    {
        return xmlFileReader.getListNodeValue("/JDBC/MONEDIAHOST")[0];
    }
    public static String getOrioncertif()
    {
        return xmlFileReader.getListNodeValue("/JDBC/ORIONCERTIF")[0];
    }
    public static String getMoneycashurl()
    {
        return xmlFileReader.getListNodeValue("/JDBC/MONEYCASHURL")[0];
    }
    public static String getReportdesignpath()
    {
        return xmlFileReader.getListNodeValue("/JDBC/REPORTDESIGNPATH")[0];
    }
    public static String getReportoutputpath()
    {
        return xmlFileReader.getListNodeValue("/JDBC/REPORTOUTPUTPATH")[0];
    }
    public static String getReportdbdriver()
    {
        return xmlFileReader.getListNodeValue("/JDBC/REPORTDBDRIVER")[0];
    }
    public static String getReportdburl()
    {
        return xmlFileReader.getListNodeValue("/JDBC/REPORTDBURL")[0];
    }
    public static String getReportdbuser()
    {
        return xmlFileReader.getListNodeValue("/JDBC/REPORTDBUSER")[0];
    }
    public static String getReportdbpass()
    {
        return xmlFileReader.getListNodeValue("/JDBC/REPORTDBPASS")[0];
    }
    public static String getExtraitmailcontent()
    {
        return xmlFileReader.getListNodeValue("/JDBC/EXTRAITMAILCONTENT")[0];
    }
    public static String getRelevemailcontent()
    {
        return xmlFileReader.getListNodeValue("/JDBC/RELEVEMAILCONTENT")[0];
    }
    public static String getSmtphost()
    {
        return xmlFileReader.getListNodeValue("/JDBC/SMTPHOST")[0];
    }
    public static String getSmtpuser()
    {
        return xmlFileReader.getListNodeValue("/JDBC/SMTPUSER")[0];
    }
    public static String getSmtpuecret()
    {
        return xmlFileReader.getListNodeValue("/JDBC/SMTPSECRET")[0];
    }
    public static String getSmtpport()
    {
        return xmlFileReader.getListNodeValue("/JDBC/SMTPPORT")[0];
    }
   public static String getSmtpfrom()
    {
        return xmlFileReader.getListNodeValue("/JDBC/SMTPFROM")[0];
    }  
   public static String getSmtpAuth()
    {
        return xmlFileReader.getListNodeValue("/JDBC/SMTPAUTH")[0];
    }   
   public static String getSmtpsecurity()
    {
        return xmlFileReader.getListNodeValue("/JDBC/SMTPSECURITY")[0];
    }  
   
}
