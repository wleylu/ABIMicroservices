/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Melarga.COULIBALY
 */
public class ConfigInfos {
    private String httpuserbaci;
    private String httpuserbabn;
    private String httpuserbatg;
    private String httpuserbabf;
    private String httpuserbane;
    private String httpuserbaml;
    private String httpuserbasn;
    private String httpsecretbaci;
    private String httpsecretbabn;
    private String httpsecretbatg;
    private String httpsecretbabf;
    private String httpsecretbane;
    private String httpsecretbaml;
    private String httpsecretbasn;
    private String logtype;
    private String applogdir;
    private String reportpath;
    private String httphostname;
    private String httpsport;
    private String httpport;
    private String security;
    private String kspath;
    private String kspassword;
    private String ssldebug;
    private String wspath;
    private String defaultimgpath;
    private String driver;
    private String datasource;
    private String mode;
    private String baciurl;
    private String baciuser;
    private String bacipassword;
    private String biadriver;
    private String biaurl;
    private String biauser;
    private String biapassword;    
    private String dburl;
    private String dbuser;
    private String dbpassword;
    private String mmgdbdriver;
    private String mmgdburl;
    private String mmgdbuser;
    private String mmgdbpassword;
    private String babnurl;
    private String babnuser;
    private String babnpassword;
    private String batgurl;
    private String batguser;
    private String batgpassword;
    private String babfurl;
    private String babfuser;
    private String babfpassword;
    private String baneurl;
    private String baneuser;
    private String banepassword;
    private String bamlurl;
    private String bamluser;
    private String bamlpassword;
    private String basnurl;
    private String basnuser;
    private String basnpassword;
    private String bagburl;
    private String bagbuser;
    private String bagbpassword;
    private String bacmurl;
    private String bacmuser;
    private String bacmpassword;
    private String reportdesignpath;
    private String reportoutputpath;
    private String reportdbdriver;
    private String reportdburl;
    private String reportdbuser;
    private String reportdbpass;
    private String extraitmailcontent;
    private String relevemailcontent;
    private String bacigwurl;
    private String baciusernamekey;
    private String baciusernamevalue;
    private String bacipwdkey;
    private String bacipwdvalue;
    private String baciextraparams;
    private String bacmgwurl;
    private String bacmusernamekey;
    private final String bacmusernamevalue="";
    private final String bacmpwdkey;
    private final String bacmpwdvalue;
    private final String bacmextraparams;
    private final String smscbaci;
    private final String smscbabn;
    private final String smscbatg;
    private final String smscbabf;
    private final String smscbane;
    private final String smscbaml;
    private final String smscbasn;
    private final String smscbacm;
    private final String smscbagb;
    
    private final String t24useref;
    private final String t24secretef;
    private final String companyt24ef;
    private final String t24wsaddress;
    private final String t24wsdomain;
    private final String t24wsname;
        
    
    public ConfigInfos() throws FileNotFoundException, IOException
    {
 
        Properties appConfig = new Properties();
        appConfig.load(ConfigInfos.class.getClassLoader().getResourceAsStream("org/atware/ressources/config.properties"));
        this.applogdir = appConfig.getProperty("APPLOGDIR");
        this.logtype = appConfig.getProperty("LOGTYPE");        
        this.ssldebug = appConfig.getProperty("SSLDEBUG");        
        this.security = appConfig.getProperty("SECURITY");        
        this.kspath = appConfig.getProperty("KEYSTOREPATH");        
        this.kspassword = appConfig.getProperty("KEYSTOREPASSWORD");        
        this.httphostname = appConfig.getProperty("HTTPHOSTNAME");
        this.httpport = appConfig.getProperty("HTTPPORT");
        this.httpsport = appConfig.getProperty("HTTPSPORT");
        this.httpuserbaci = appConfig.getProperty("HTTPUSERBACI");
        this.httpuserbabn = appConfig.getProperty("HTTPUSERBABN");
        this.httpuserbatg = appConfig.getProperty("HTTPUSERBATG");
        this.wspath = appConfig.getProperty("WSPATH");
        this.defaultimgpath = appConfig.getProperty("IMGPATH");
        // Parametres de connexion aux bases de donnees
        this.driver = appConfig.getProperty("DRIVER");
        this.datasource = appConfig.getProperty("DATASOURCE");
        this.mode = appConfig.getProperty("MODE");
        this.baciurl = appConfig.getProperty("BACIURL");
        this.baciuser = appConfig.getProperty("BACIUSER");
        this.bacipassword = appConfig.getProperty("BACIPASSWORD");
        this.biaurl = appConfig.getProperty("BIAURL");
        this.biadriver = appConfig.getProperty("BIADRIVER");
        this.biauser = appConfig.getProperty("BIAUSER");
        this.biapassword = appConfig.getProperty("BIAPASSWORD");
        this.dburl = appConfig.getProperty("DBURL");
        this.dbuser = appConfig.getProperty("DBUSER");
        this.dbpassword = appConfig.getProperty("DBPASSWORD");
        this.mmgdbdriver = appConfig.getProperty("MMGDBDRIVER");
        this.mmgdburl = appConfig.getProperty("MMGDBURL");
        this.mmgdbuser = appConfig.getProperty("MMGDBUSER");
        this.mmgdbpassword = appConfig.getProperty("MMGDBPASSWORD");
        this.babnurl = appConfig.getProperty("BABNURL");
        this.babnuser = appConfig.getProperty("BABNUSER");
        this.babnpassword = appConfig.getProperty("BABNPASSWORD");
        this.batgurl = appConfig.getProperty("BATGURL");
        this.batguser = appConfig.getProperty("BATGUSER");
        this.batgpassword = appConfig.getProperty("BATGPASSWORD");
        this.babfurl = appConfig.getProperty("BABFURL");
        this.babfuser = appConfig.getProperty("BABFUSER");
        this.babfpassword = appConfig.getProperty("BABFPASSWORD");
        this.baneurl = appConfig.getProperty("BANEURL");
        this.baneuser = appConfig.getProperty("BANEUSER");
        this.banepassword = appConfig.getProperty("BANEPASSWORD");
        this.bamlurl = appConfig.getProperty("BAMLURL");
        this.bamluser = appConfig.getProperty("BAMLUSER");
        this.bamlpassword = appConfig.getProperty("BAMLPASSWORD");
        this.basnurl = appConfig.getProperty("BASNURL");
        this.basnuser = appConfig.getProperty("BASNUSER");
        this.basnpassword = appConfig.getProperty("BASNPASSWORD");
        this.bacmurl = appConfig.getProperty("BACMURL");
        this.bacmuser = appConfig.getProperty("BACMUSER");
        this.bacmpassword = appConfig.getProperty("BACMPASSWORD");
        this.bagburl = appConfig.getProperty("BAGBURL");
        this.bagbuser = appConfig.getProperty("BAGBUSER");
        this.bagbpassword = appConfig.getProperty("BAGBPASSWORD");
        this.reportpath = appConfig.getProperty("REPORTPATH");
        this.reportdesignpath = appConfig.getProperty("REPORTDESIGNPATH");
        this.reportoutputpath = appConfig.getProperty("REPORTOUTPUTPATH");
        this.reportdbdriver = appConfig.getProperty("REPORTDBDRIVER");
        this.reportdburl = appConfig.getProperty("REPORTDBURL");
        this.reportdbuser = appConfig.getProperty("REPORTDBUSER");
        this.reportdbpass = appConfig.getProperty("REPORTDBPASS");
        this.extraitmailcontent = appConfig.getProperty("EXTRAITMAILCONTENT");
        this.relevemailcontent = appConfig.getProperty("RELEVEMAILCONTENT");
        this.bacigwurl = appConfig.getProperty("BACIGWURL");
        this.baciusernamekey = appConfig.getProperty("BACIUSERNAMEKEY");
        this.bacipwdkey = appConfig.getProperty("BACIPWDKEY");
        this.bacipwdvalue = appConfig.getProperty("BACIPWDVALUE");
        this.baciusernamevalue = appConfig.getProperty("BACIUSERNAMEVALUE");
        this.baciextraparams = appConfig.getProperty("BACIEXTRAPARAMS");
        this.bacmgwurl = appConfig.getProperty("BACMGWURL");
        this.bacmusernamekey = appConfig.getProperty("BACMUSERNAMEKEY");
        this.bacmpwdkey = appConfig.getProperty("BACMPWDKEY");
        this.bacmpwdvalue = appConfig.getProperty("BACMPWDVALUE");
        this.bacmusernamekey = appConfig.getProperty("BACMUSERNAMEKEY");
        this.bacmextraparams = appConfig.getProperty("BACMEXTRAPARAMS");
        this.smscbaci = appConfig.getProperty("SMSCBACI");
        this.smscbabn = appConfig.getProperty("SMSCBABN");
        this.smscbatg = appConfig.getProperty("SMSCBATG");
        this.smscbabf = appConfig.getProperty("SMSCBABF");
        this.smscbane = appConfig.getProperty("SMSCBANE");
        this.smscbaml = appConfig.getProperty("SMSCBAML");
        this.smscbasn = appConfig.getProperty("SMSCBASN");
        this.smscbacm = appConfig.getProperty("SMSCBACM");
        this.smscbagb = appConfig.getProperty("SMSCBAGB"); 
        
        this.t24useref=appConfig.getProperty("T24USEREF");
         this.t24secretef=appConfig.getProperty("T24SECRETEF");
         this.companyt24ef=appConfig.getProperty("COMPANYT24EF");
         this.t24wsaddress = appConfig.getProperty("T24WSADRESS");
         this.t24wsdomain = appConfig.getProperty("T24WSDOMAIN");
        this.t24wsname = appConfig.getProperty("T24WSNAME");
        
        
    }     
    
    
   

    public String getT24useref() {
        return t24useref;
    }

    public String getT24secretef() {
        return t24secretef;
    }

    public String getCompanyt24ef() {
        return companyt24ef;
    }

    public String getT24wsaddress() {
        return t24wsaddress;
    }

    public String getT24wsdomain() {
        return t24wsdomain;
    }

    public String getT24wsname() {
        return t24wsname;
    }

 
    

    public String getHttpuserbaci() {
        return httpuserbaci;
    }

    public String getHttpuserbabn() {
        return httpuserbabn;
    }

    public String getHttpuserbatg() {
        return httpuserbatg;
    }

    public String getHttpuserbabf() {
        return httpuserbabf;
    }

    public String getHttpuserbane() {
        return httpuserbane;
    }

    public String getHttpuserbaml() {
        return httpuserbaml;
    }

    public String getHttpuserbasn() {
        return httpuserbasn;
    }

    public String getHttpsecretbaci() {
        return httpsecretbaci;
    }

    public String getHttpsecretbabn() {
        return httpsecretbabn;
    }

    public String getHttpsecretbatg() {
        return httpsecretbatg;
    }

    public String getHttpsecretbabf() {
        return httpsecretbabf;
    }

    public String getHttpsecretbane() {
        return httpsecretbane;
    }

    public String getHttpsecretbaml() {
        return httpsecretbaml;
    }

    public String getHttpsecretbasn() {
        return httpsecretbasn;
    }

    public String getLogtype() {
        return logtype;
    }

    public String getApplogdir() {
        return applogdir;
    }

    public String getHttphostname() {
        return httphostname;
    }

    public String getHttpsport() {
        return httpsport;
    }

    public String getHttpport() {
        return httpport;
    }

    public String getSecurity() {
        return security;
    }

    public String getKspath() {
        return kspath;
    }

    public String getKspassword() {
        return kspassword;
    }

    public String getSsldebug() {
        return ssldebug;
    }

    public String getWspath() {
        return wspath;
    }

    public String getReportpath() {
        return reportpath;
    }

    public String getDefaultimgpath() {
        return defaultimgpath;
    }

    public String getDriver() {
        return driver;
    }

    public String getBiadriver() {
        return biadriver;
    }

    public String getDatasource() {
        return datasource;
    }

    public String getMode() {
        return mode;
    }

    public String getBaciurl() {
        return baciurl;
    }

    public String getBaciuser() {
        return baciuser;
    }

    public String getBacipassword() {
        return bacipassword;
    }

    public String getBiaurl() {
        return biaurl;
    }

    public String getBiauser() {
        return biauser;
    }

    public String getBiapassword() {
        return biapassword;
    }

    public String getDburl() {
        return dburl;
    }

    public String getDbuser() {
        return dbuser;
    }

    public String getMmgdbdriver() {
        return mmgdbdriver;
    }

    public String getMmgdburl() {
        return mmgdburl;
    }

    public String getMmgdbuser() {
        return mmgdbuser;
    }

    public String getMmgdbpassword() {
        return mmgdbpassword;
    }

    public String getDbpassword() {
        return dbpassword;
    }

    public String getBabnurl() {
        return babnurl;
    }

    public String getBabnuser() {
        return babnuser;
    }

    public String getBabnpassword() {
        return babnpassword;
    }

    public String getBatgurl() {
        return batgurl;
    }

    public String getBatguser() {
        return batguser;
    }

    public String getBatgpassword() {
        return batgpassword;
    }

    public String getBabfurl() {
        return babfurl;
    }

    public String getBabfuser() {
        return babfuser;
    }

    public String getBabfpassword() {
        return babfpassword;
    }

    public String getBaneurl() {
        return baneurl;
    }

    public String getBaneuser() {
        return baneuser;
    }

    public String getBanepassword() {
        return banepassword;
    }

    public String getBamlurl() {
        return bamlurl;
    }

    public String getBamluser() {
        return bamluser;
    }

    public String getBamlpassword() {
        return bamlpassword;
    }

    public String getBasnurl() {
        return basnurl;
    }

    public String getBasnuser() {
        return basnuser;
    }

    public String getBasnpassword() {
        return basnpassword;
    }

    public String getBagburl() {
        return bagburl;
    }

    public String getBagbuser() {
        return bagbuser;
    }

    public String getBagbpassword() {
        return bagbpassword;
    }

    public String getBacmurl() {
        return bacmurl;
    }

    public String getBacmuser() {
        return bacmuser;
    }

    public String getBacmpassword() {
        return bacmpassword;
    }

    public String getReportdesignpath() {
        return reportdesignpath;
    }

    public String getReportoutputpath() {
        return reportoutputpath;
    }

    public String getReportdbdriver() {
        return reportdbdriver;
    }

    public String getReportdburl() {
        return reportdburl;
    }

    public String getReportdbuser() {
        return reportdbuser;
    }

    public String getReportdbpass() {
        return reportdbpass;
    }

    public String getExtraitmailcontent() {
        return extraitmailcontent;
    }

    public String getRelevemailcontent() {
        return relevemailcontent;
    }

    public String getBacigwurl() {
        return bacigwurl;
    }

    public String getBaciusernamekey() {
        return baciusernamekey;
    }

    public String getBaciusernamevalue() {
        return baciusernamevalue;
    }

    public String getBacipwdkey() {
        return bacipwdkey;
    }

    public String getBacipwdvalue() {
        return bacipwdvalue;
    }

    public String getBaciextraparams() {
        return baciextraparams;
    }

    public String getBacmgwurl() {
        return bacmgwurl;
    }

    public String getBacmusernamekey() {
        return bacmusernamekey;
    }

    public String getBacmusernamevalue() {
        return bacmusernamevalue;
    }

    public String getBacmpwdkey() {
        return bacmpwdkey;
    }

    public String getBacmpwdvalue() {
        return bacmpwdvalue;
    }

    public String getBacmextraparams() {
        return bacmextraparams;
    }

    public String getSmscbaci() {
        return smscbaci;
    }

    public String getSmscbabn() {
        return smscbabn;
    }

    public String getSmscbatg() {
        return smscbatg;
    }

    public String getSmscbabf() {
        return smscbabf;
    }

    public String getSmscbane() {
        return smscbane;
    }

    public String getSmscbaml() {
        return smscbaml;
    }

    public String getSmscbasn() {
        return smscbasn;
    }

    public String getSmscbacm() {
        return smscbacm;
    }

    public String getSmscbagb() {
        return smscbagb;
    }

    
    public String getSmsc(String filiale){
        String smsc="";
        if (filiale.equalsIgnoreCase("BACI")) smsc=smscbaci;
        if (filiale.equalsIgnoreCase("BABN")) smsc=smscbabn;
        if (filiale.equalsIgnoreCase("BATG")) smsc=smscbatg;
        if (filiale.equalsIgnoreCase("BABF")) smsc=smscbabf;
        if (filiale.equalsIgnoreCase("BANE")) smsc=smscbane;
        if (filiale.equalsIgnoreCase("BAML")) smsc=smscbaml;
        if (filiale.equalsIgnoreCase("BASN")) smsc=smscbasn;
        if (filiale.equalsIgnoreCase("BACM")) smsc=smscbacm;
        if (filiale.equalsIgnoreCase("BAGB")) smsc=smscbagb;
    
        return smsc;
    }
           
    
    
}
