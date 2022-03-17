package com.atware.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.atware.bean.ConfigInfos;
import org.atware.bean.DBCredential;
import org.atware.jdbc.Database;
import org.atware.bean.SMSMessage;
import org.json.JSONException;
import org.json.JSONObject;

public final class AppController {

//    private Database db;
    MailingSessions sessionMailBean;
    public ConfigInfos config = null;
    
    @SuppressWarnings("CallToPrintStackTrace")
    public AppController() {
        try {
            config = new ConfigInfos();
            System.out.println("ConfigInfos() successfully called from init AppController");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }            
    }
    
    public DBCredential getDBCredential(String filiale){
        DBCredential d=new DBCredential();String url = "",user = "",pass="";
         if (filiale.equalsIgnoreCase("BACI")){
            url = config.getBaciurl();user = config.getBaciuser();pass=config.getBacipassword();
        }
        if (filiale.equalsIgnoreCase("BABN")){
            url = config.getBabnurl();user = config.getBabnuser();pass=config.getBabnpassword();
        }
        if (filiale.equalsIgnoreCase("BATG")){
            url = config.getBatgurl();user = config.getBatguser();pass=config.getBatgpassword();
        }
        if (filiale.equalsIgnoreCase("BABF")){
            url = config.getBabfurl();user = config.getBabfuser();pass=config.getBabfpassword();
        }
        if (filiale.equalsIgnoreCase("BANE")){
            url = config.getBaneurl();user = config.getBaneuser();pass=config.getBanepassword();
        }
        if (filiale.equalsIgnoreCase("BAML")){
            url = config.getBamlurl();user = config.getBamluser();pass=config.getBamlpassword();
        }
        if (filiale.equalsIgnoreCase("BASN")){
            url = config.getBasnurl();user = config.getBasnuser();pass=config.getBasnpassword();
        }
        if (filiale.equalsIgnoreCase("BACM")){
            url = config.getBacmurl();user = config.getBacmuser();pass=config.getBacmpassword();
        }
//        if (filiale.equalsIgnoreCase("BIA0")){
//            url = config.getBacmurl();user = config.getBacmuser();pass=config.getBacmpassword();
//        }        
        d.setUrl(url);d.setUsername(user);d.setPassword(pass);
        return d;
    
    }
    
    @SuppressWarnings({"CallToThreadDumpStack", "CallToPrintStackTrace"})
    public  String receiveSmsAndTransmit1(SMSMessage sms) {
        // Formatter la destination
        // Creer le fichier
        String httpurl="";@SuppressWarnings("UnusedAssignment") String resp = " ";
        String endPoint = config.getBacigwurl().trim();
        String sendsmsuser = config.getBaciusernamevalue().trim();
        String sendsmspwd = config.getBacipwdvalue().trim();
        try {
            
            httpurl += config.getBaciusernamekey()+"="+sendsmsuser+"&";
            httpurl += config.getBacipwdkey()+"="+sendsmspwd+"&";
            httpurl += "from="+sms.getFrom()+"&";
            httpurl += "to="+sms.getTo().trim()+"&";
            httpurl += "text="+URLEncoder.encode(sms.getMessage(), "ISO-8859-15")+"&id="+sms.getIdmsg().trim()+"&";
            httpurl += config.getBaciextraparams();
            resp = sendGetRequest(endPoint,httpurl);
            if (resp.contains("0")) 
                resp="00";
            else if (resp.contains("3")) 
                resp="00";
            else if (resp.toUpperCase().contains("ROUT")) 
                resp="01";
            else resp="99";
            
            System.out.println("New message received for transmission !!!");
        } catch(UnsupportedEncodingException ex){
            ex.printStackTrace();
            resp="98";
        }
        return resp;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String receiveSmsAndTransmit(SMSMessage sms) {
        String result="00";
            Database dbsession = new Database(config.getMmgdbdriver());PreparedStatement ps=null;
        try
        {
            dbsession.open(config.getMmgdburl(),config.getMmgdbuser(),config.getMmgdbpassword());
        }
        catch(Exception e)
        {
            e.printStackTrace(); return "99";
        }
//        String sql="INSERT INTO send_sms (time,account,momt, sender, receiver, msgdata, sms_type,dlr_mask,smsc_id) VALUES (extract('epoch' from CURRENT_TIMESTAMP)::bigint,?,'MT', ?, ?, ?, 2,31,?)";
        try {
            String expediteur = "SMS-"+sms.getFiliale().toUpperCase();
            String smsc = "IB"+sms.getFiliale();
            String tablename = "smsout"+sms.getFiliale().trim().toLowerCase();
//            String sql = (new StringBuilder()).append("insert into ").append(tablename).append("(filiale,datoper,sender,receiver,msgdata,service,account,smsc_id,dlrstatus) values(?,?,?,?,?,?,?,?,'NOK')").toString();
            String sql = (new StringBuilder()).append("insert into ").append(tablename).append("(filiale,datoper,sender,receiver,msgdata,service,account,smsc_id,dlrstatus,id_msgdlr) values(?,?,?,?,?,?,?,?,'NOK',?)").toString();
            ps = dbsession.getPreparedstatement(sql);
            ps.setString(1,sms.getFiliale().toUpperCase());
            ps.setString(2, this.getCurdatetime("/"));
            ps.setString(3, expediteur);
            ps.setString(4, sms.getTo());
            ps.setString(5, sms.getMessage());
            ps.setString(6, "WSPROD");
            ps.setString(7, "PCA");
            ps.setString(8, config.getSmsc(sms.getFiliale()));
            ps.setString(9, sms.getIdmsg().trim());
           
//            ps = dbsession.getPreparedstatement(sql);
//            ps.setString(1, sms.getFiliale());
//            ps.setString(2, sms.getFrom());
//            ps.setString(3, sms.getTo());
//            ps.setString(4, sms.getMessage());
//            ps.setString(5, config.getSmsc(sms.getFiliale()));
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();return "98";
        }        
        
        return result;
    
    }
    public String receiveSmsAndTransmit2(SMSMessage sms) {
        String result="00";
            Database dbsession = new Database(config.getMmgdbdriver());PreparedStatement ps=null;
        try
        {
            dbsession.open(config.getMmgdburl(),config.getMmgdbuser(),config.getMmgdbpassword());
        }
        catch(Exception e)
        {
            e.printStackTrace(); return "99";
        }
        String sql="INSERT INTO send_sms (time,account,momt, sender, receiver, msgdata, sms_type,dlr_mask,smsc_id) VALUES (extract('epoch' from CURRENT_TIMESTAMP)::bigint,?,'MT', ?, ?, ?, 2,31,?)";
        try {
            ps = dbsession.getPreparedstatement(sql);
            ps.setString(1, sms.getFiliale());
            ps.setString(2, sms.getFrom());
            ps.setString(3, sms.getTo());
            ps.setString(4, sms.getMessage());
            ps.setString(5, config.getSmsc(sms.getFiliale()));
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();return "98";
        }        
        
        return result;
    
    }
    @SuppressWarnings({"ConvertToTryWithResources", "CallToThreadDumpStack", "UseSpecificCatch", "CallToPrintStackTrace"})
public String sendGetRequest(String endpoint, String requestParameters){

    String result = null;
    // Construct data
    StringBuilder data = new StringBuilder();
    // Send data
    String urlStr = endpoint; StringBuilder sb = new StringBuilder();
    if (requestParameters != null && requestParameters.length () > 0)
    {
        if (endpoint.contains("?")) 
            urlStr += requestParameters;
        else
            urlStr += "?" + requestParameters;
        
        System.out.println("URL="+urlStr);
    }

    if (endpoint.startsWith("http://"))
    {

    // Send a GET request to the servlet

    try 
    {

    URL url = new URL(urlStr);
    URLConnection conn = url.openConnection();
    // Get the response
    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));   
    String line;

    while ((line = rd.readLine()) != null)
    {
        sb.append(line);
    }
    rd.close();
    result = sb.toString();
    
    } catch (Exception e)
    {
        e.printStackTrace();return  "97";
    }

    } else if (endpoint.startsWith("https://")) {
        URL url;
        try {

               url = new URL(urlStr);
	    //for localhost testing only
              sslValidation();

               HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
               if(con!=null){

                try {
                   System.out.println("****** Content of the URL ********");			
                   BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                   String input;
                   while ((input = br.readLine()) != null){
                     sb.append(input);
                   }
                   br.close();
                   result = sb.toString();
                } catch (IOException e) {
                   e.printStackTrace();return  "97";
                }

               } else return  "96";
        
        } catch (MalformedURLException e) {
               e.printStackTrace();return  "95";
        } catch (IOException e) {
               e.printStackTrace();return  "94";
        }
        
    }

    return result;
}
    @SuppressWarnings({"CallToThreadDumpStack", "CallToPrintStackTrace"})
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
                System.out.println("caller hostname="+hostname);
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
        e.printStackTrace();
    }
} 
   
    @SuppressWarnings("CallToPrintStackTrace")
    public int insertRoutedMessage(String smsc,String account,String service, String msgid,String from, String to, String msg, String response){
    int res = 0;
    Database dbsession = new Database(config.getDriver());PreparedStatement ps=null;
    try
    {
        dbsession.open(config.getDburl(),config.getDbuser(),config.getDbpassword());
    }
    catch(Exception e)
    {
        e.printStackTrace(); return -1;
    }
    String sql = "insert into smsdlr(smsc,ts,destination,source,datoper,message,account,resp,service) values(?,?,?,?,?,?,?,?,?)";
        try {
            ps = dbsession.getPreparedstatement(sql);
            ps.setString(1, smsc);
            ps.setString(2, msgid);
            ps.setString(3, to);
            ps.setString(4, from);
            ps.setString(5, this.getCurdatetime("/"));
            ps.setString(6, msg);
            ps.setString(7, account);if (response.length()>255) response = response.substring(0,255);
            ps.setString(8, response);
            ps.setString(9, service);
            res = ps.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();return -2;
        }
    dbsession.close();
    return res;
}

    @SuppressWarnings("CallToPrintStackTrace")
    public String getORIONClientinfos(String filiale, String compte, String agence) {
        @SuppressWarnings("UnusedAssignment")
        String result = "99";
        String url = "", user = "", pass = "";
        StringBuilder sb = new StringBuilder();
        Database dbsession;PreparedStatement ps;
        ResultSet lst=null;  Connection cn;CallableStatement cstmt;int vnbre=0;
        
        if (filiale.equalsIgnoreCase("000343")) {
            url = config.getBaciurl();
            user = config.getBaciuser();
            pass = config.getBacipassword();
        }
        if (filiale.equalsIgnoreCase("101151")) {
            url = config.getBabnurl();
            user = config.getBabnuser();
            pass = config.getBabnpassword();
        }
        if (filiale.equalsIgnoreCase("901388")) {
            url = config.getBatgurl();
            user = config.getBatguser();
            pass = config.getBatgpassword();
        }
        if (filiale.equalsIgnoreCase("201342")) {
            url = config.getBabfurl();
            user = config.getBabfuser();
            pass = config.getBabfpassword();
        }
        if (filiale.equalsIgnoreCase("701366")) {
            url = config.getBaneurl();
            user = config.getBaneuser();
            pass = config.getBanepassword();
        }
        if (filiale.equalsIgnoreCase("301355")) {
            url = config.getBamlurl();
            user = config.getBamluser();
            pass = config.getBamlpassword();
        }
        if (filiale.equalsIgnoreCase("901377")) {
            url = config.getBasnurl();
            user = config.getBasnuser();
            pass = config.getBasnpassword();
        }
        if (filiale.equalsIgnoreCase("000697")) {
            url = config.getBacmurl();
            user = config.getBacmuser();
            pass = config.getBacmpassword();
        }
        if (filiale.equalsIgnoreCase("691954")) {
            url = config.getBagburl();
            user = config.getBagbuser();
            pass = config.getBagbpassword();
        }
        dbsession = new Database(config.getDriver());
        ResultSet rs = null;
        try {
//            System.out.println("user="+user+" url="+url+" pass="+pass);
            dbsession.open(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            result = "98";
            return result;
        }
            String sql = (new StringBuilder()).append("{? = call HPS_ACCOUNTINFOS(?,?)}").toString();
            
            try {
                System.out.println("SQL = "+sql);
                cn = dbsession.getConn();
                cstmt = cn.prepareCall(sql);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.setString(3, agence);
                boolean execute = cstmt.execute();
                result = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                result = "97";
                return result;
            }
        dbsession.close();
        System.out.println("result=" + result);
        
        return result;
    }
    @SuppressWarnings("CallToPrintStackTrace")
public String getInfosClient(String filiale, String compte) {
        @SuppressWarnings("UnusedAssignment")
        String result = "99";
        String url = "", user = "", pass = "";
        StringBuilder sb = new StringBuilder();
        Database dbsession;PreparedStatement ps;
        Connection cn;CallableStatement cstmt;
        
        if (filiale.equalsIgnoreCase("BACI")) {
            url = config.getBaciurl();
            user = config.getBaciuser();
            pass = config.getBacipassword();
        }
        if (filiale.equalsIgnoreCase("BABN")) {
            url = config.getBabnurl();
            user = config.getBabnuser();
            pass = config.getBabnpassword();
        }
        if (filiale.equalsIgnoreCase("BATG")) {
            url = config.getBatgurl();
            user = config.getBatguser();
            pass = config.getBatgpassword();
        }
        if (filiale.equalsIgnoreCase("BABF")) {
            url = config.getBabfurl();
            user = config.getBabfuser();
            pass = config.getBabfpassword();
        }
        if (filiale.equalsIgnoreCase("BANE")) {
            url = config.getBaneurl();
            user = config.getBaneuser();
            pass = config.getBanepassword();
        }
        if (filiale.equalsIgnoreCase("BAML")) {
            url = config.getBamlurl();
            user = config.getBamluser();
            pass = config.getBamlpassword();
        }
        if (filiale.equalsIgnoreCase("BASN")) {
            url = config.getBasnurl();
            user = config.getBasnuser();
            pass = config.getBasnpassword();
        }
        if (filiale.equalsIgnoreCase("BACM")) {
            url = config.getBacmurl();
            user = config.getBacmuser();
            pass = config.getBacmpassword();
        }
        if (filiale.equalsIgnoreCase("BAGB")) {
            url = config.getBagburl();
            user = config.getBagbuser();
            pass = config.getBagbpassword();
        }
        dbsession = new Database(config.getDriver());
        ResultSet rs = null;
        try {
            dbsession.open(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            result = "98";
            return result;
        }
            String sql = (new StringBuilder()).append("{? = call W_ED_MBK_INFOS_CLIENT(?)}").toString();
            
            try {
                System.out.println("SQL = "+sql);
                cn = dbsession.getConn();
                cstmt = cn.prepareCall(sql);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                boolean execute = cstmt.execute();
                result = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                result = "97";
                return result;
            }
        dbsession.close();
        System.out.println("result=" + result);
        
        return result;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String makeWiretransfert(String filiale, String codeclient,String dattransact,String codopsc,String reftransact,String compted,String comptec,String mntd,String mntc,String mntcion1,String mntcion2,String mntctax,String libtransact,String listrefrel,String comptecion,String CnxKey) {
        @SuppressWarnings("UnusedAssignment")
        String result = "99";
        String url = "", user = "", pass = "";
        StringBuilder sb = new StringBuilder();
        Database dbsession;PreparedStatement ps;
        Connection cn;CallableStatement cstmt;
        
        if (filiale.equalsIgnoreCase("BACI")) {
            url = config.getBaciurl();
            user = config.getBaciuser();
            pass = config.getBacipassword();
        }
        if (filiale.equalsIgnoreCase("BABN")) {
            url = config.getBabnurl();
            user = config.getBabnuser();
            pass = config.getBabnpassword();
        }
        if (filiale.equalsIgnoreCase("BATG")) {
            url = config.getBatgurl();
            user = config.getBatguser();
            pass = config.getBatgpassword();
        }
        if (filiale.equalsIgnoreCase("BABF")) {
            url = config.getBabfurl();
            user = config.getBabfuser();
            pass = config.getBabfpassword();
        }
        if (filiale.equalsIgnoreCase("BANE")) {
            url = config.getBaneurl();
            user = config.getBaneuser();
            pass = config.getBanepassword();
        }
        if (filiale.equalsIgnoreCase("BAML")) {
            url = config.getBamlurl();
            user = config.getBamluser();
            pass = config.getBamlpassword();
        }
        if (filiale.equalsIgnoreCase("BASN")) {
            url = config.getBasnurl();
            user = config.getBasnuser();
            pass = config.getBasnpassword();
        }
        if (filiale.equalsIgnoreCase("BACM")) {
            url = config.getBacmurl();
            user = config.getBacmuser();
            pass = config.getBacmpassword();
        }
        if (filiale.equalsIgnoreCase("BAGB")) {
            url = config.getBagburl();
            user = config.getBagbuser();
            pass = config.getBagbpassword();
        }
        dbsession = new Database(config.getDriver());
        ResultSet rs = null;
        try {
            dbsession.open(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            result = "98";
            return result;
        }
            String sql = (new StringBuilder()).append("{? = call MLFORION.MLF_POSTPAIEMENT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}").toString();
// dattransact,String reftransact,String compted,String comptec,String mntd,String mntc,String mntcion1,String mntcion2,String mntctax,String libtransact,String listrefrel,String comptecion,String CnxKey            

try {
                System.out.println("SQL = "+sql);
                cn = dbsession.getConn();
                cstmt = cn.prepareCall(sql);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, codeclient);
                cstmt.setString(3, dattransact);
                cstmt.setString(4, codopsc);
                cstmt.setString(5, reftransact);
                cstmt.setString(6, compted);
                cstmt.setString(7, comptec);
                cstmt.setString(8, mntd);
                cstmt.setString(9, mntc);
                cstmt.setString(10, comptecion);
                cstmt.setString(11, mntcion1);
                cstmt.setString(12, mntcion2);
                cstmt.setString(13, mntctax);
                cstmt.setString(14, libtransact);
                cstmt.setString(15, listrefrel);
                cstmt.setString(16, CnxKey);
                cstmt.execute();
                result = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                result = "97";
                return result;
            }
        dbsession.close();
        System.out.println("result=" + result);
        
        return result;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String proc_icaisse(String filiale, String facturier,String caisse,String dhtransact,String codoper,String refrel,String cptd,String cptc,String mntd,String mntc,String mntc1,String mntc2,String mntt,String narratif) {
        @SuppressWarnings("UnusedAssignment")
        String result = "99";
        String url = "", user = "", pass = "";
        StringBuilder sb;
        sb = new StringBuilder();
        Database dbsession;PreparedStatement ps;
        Connection cn;CallableStatement cstmt;Clob pcData;
        
        if (filiale.equalsIgnoreCase("BACI")) {
            url = config.getBaciurl();
            user = config.getBaciuser();
            pass = config.getBacipassword();
        }
        if (filiale.equalsIgnoreCase("BABN")) {
            url = config.getBabnurl();
            user = config.getBabnuser();
            pass = config.getBabnpassword();
        }
        if (filiale.equalsIgnoreCase("BATG")) {
            url = config.getBatgurl();
            user = config.getBatguser();
            pass = config.getBatgpassword();
        }
        if (filiale.equalsIgnoreCase("BABF")) {
            url = config.getBabfurl();
            user = config.getBabfuser();
            pass = config.getBabfpassword();
        }
        if (filiale.equalsIgnoreCase("BANE")) {
            url = config.getBaneurl();
            user = config.getBaneuser();
            pass = config.getBanepassword();
        }
        if (filiale.equalsIgnoreCase("BAML")) {
            url = config.getBamlurl();
            user = config.getBamluser();
            pass = config.getBamlpassword();
        }
        if (filiale.equalsIgnoreCase("BASN")) {
            url = config.getBasnurl();
            user = config.getBasnuser();
            pass = config.getBasnpassword();
        }
        if (filiale.equalsIgnoreCase("BACM")) {
            url = config.getBacmurl();
            user = config.getBacmuser();
            pass = config.getBacmpassword();
        }
        if (filiale.equalsIgnoreCase("BAGB")) {
            url = config.getBagburl();
            user = config.getBagbuser();
            pass = config.getBagbpassword();
        }
        dbsession = new Database(config.getDriver());
        ResultSet rs = null;
        try {
            dbsession.open(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            result = "98";
            return result;
        }
            String sql = (new StringBuilder()).append("{? = call ICAISSE.IC_PostPaiement(?,?,?,?,?,?,?,?,?,?,?,?,?)}").toString();
// facturier,caisse,dhtransact,codoper,refrel,cptd,cptc,mntd,mntc,mntc1,mntc2,mntt,narratif IC_PostPaiement

try {
                System.out.println("SQL = "+sql);
                cn = dbsession.getConn();
                cstmt = cn.prepareCall(sql);
                cstmt.registerOutParameter(1, Types.CLOB);
                cstmt.setString(2, facturier);
                cstmt.setString(3, caisse);
                cstmt.setString(4, dhtransact);
                cstmt.setString(5, codoper);
                cstmt.setString(6, refrel);
                cstmt.setString(7, cptd);
                cstmt.setString(8, cptc);
                cstmt.setString(9, mntd);
                cstmt.setString(10, mntc);
                cstmt.setString(11, mntc1);
                cstmt.setString(12, mntc2);
                cstmt.setString(13, mntt);
                cstmt.setString(14, narratif);
                cstmt.execute();
                pcData = cstmt.getClob(1);
                sb = this.getStringBuilderFromClob(pcData); 
                result = sb.toString();
            } catch (SQLException ex) {
                ex.printStackTrace();
                result = "97";
                return result;
            }
        dbsession.close();
        System.out.println("result=" + result);
        
        return result;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String getORIONVersement(String filiale, String agence, String nooper) {
        String result = "99";
        String url = "", user = "", pass = "";
        StringBuilder sb = new StringBuilder();
        Database dbsession;PreparedStatement ps;
        ResultSet lst=null;  Connection cn;CallableStatement cstmt;int vnbre=0;
        
        if (filiale.equalsIgnoreCase("000343")) {
            url = config.getBaciurl();
            user = config.getBaciuser();
            pass = config.getBacipassword();
        }
        if (filiale.equalsIgnoreCase("101151")) {
            url = config.getBabnurl();
            user = config.getBabnuser();
            pass = config.getBabnpassword();
        }
        if (filiale.equalsIgnoreCase("901388")) {
            url = config.getBatgurl();
            user = config.getBatguser();
            pass = config.getBatgpassword();
        }
        if (filiale.equalsIgnoreCase("201342")) {
            url = config.getBabfurl();
            user = config.getBabfuser();
            pass = config.getBabfpassword();
        }
        if (filiale.equalsIgnoreCase("701366")) {
            url = config.getBaneurl();
            user = config.getBaneuser();
            pass = config.getBanepassword();
        }
        if (filiale.equalsIgnoreCase("301355")) {
            url = config.getBamlurl();
            user = config.getBamluser();
            pass = config.getBamlpassword();
        }
        if (filiale.equalsIgnoreCase("901377")) {
            url = config.getBasnurl();
            user = config.getBasnuser();
            pass = config.getBasnpassword();
        }
        if (filiale.equalsIgnoreCase("000697")) {
            url = config.getBacmurl();
            user = config.getBacmuser();
            pass = config.getBacmpassword();
        }
        if (filiale.equalsIgnoreCase("691954")) {
            url = config.getBagburl();
            user = config.getBagbuser();
            pass = config.getBagbpassword();
        }
        if (filiale.equalsIgnoreCase("000000")) {
            url = config.getBiaurl();
            user = config.getBiauser();
            pass = config.getBiapassword();
            dbsession = new Database(config.getBiadriver());
            
        } else  dbsession = new Database(config.getDriver());
        
        ResultSet rs = null;
        try {
//            System.out.println("user="+user+" url="+url+" pass="+pass);
            dbsession.open(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            result = "98";
            return result;
        }
            String sql = (new StringBuilder()).append("{? = call PWC_OP_VERIFICATION(?,?)}").toString();
            
            try {
                System.out.println("SQL = "+sql);
                cn = dbsession.getConn();
                cstmt = cn.prepareCall(sql);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, agence);
                cstmt.setString(3, nooper);
                boolean execute = cstmt.execute();
                result = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                result = "97";
                return result;
            }
        dbsession.close();
        System.out.println("result=" + result);
        
        return result;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String getClientvalidation(String filiale,String racine,String agence)
    {
        //double balance=0;
        String status="";boolean find = false; String result="99";String url="",user="",pass="";
      
        try {
            Database dbsession = new Database(config.getDriver());ResultSet rs=null;
            try
            {
            DBCredential crdt = getDBCredential(filiale);
             dbsession.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                result="98";
                return result;
            }
            String sql = (new StringBuilder()).append("SELECT 'OK' STATUS FROM CLI WHERE CLIENT='").append(racine).append("' AND AGENCE='").append(agence).append("' AND DATFRM IS NULL").toString();
//            String sql = (new StringBuilder()).append("SELECT 'OK' STATUS FROM CLI WHERE CLIENT='").append(racine).append("' AND DATFRM IS NULL").toString();
            try {
                System.out.println("Connexion bd ok , execution de la requette...");
                 rs = dbsession.GetResult(sql);
                 if (rs.next()){
                    result = rs.getString("STATUS");
                    System.out.println("Execution Ok, donnée trouvée.result="+result);
                 } else System.out.println("Execution NOk, donnée NON trouvée.result="+result);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                result="97";
                return result;
            }
            if (rs.next()) {
                status = rs.getString("STATUS");
                find = true;
            }
            dbsession.close();
        } catch (SQLException ex) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
            result="96";
            return result;            
        }
        
     return result;     
    }
    
    public boolean isNumber(String number) {

        try {
            double d = Double.parseDouble(number);

        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public String getNomville(String filiale, String codeville) {
        //double balance=0;
        String status = "";
        boolean find = false;
        String result = "99";
        String url = "", user = "", pass = "";
        StringBuilder sb = new StringBuilder();
        if (filiale.equalsIgnoreCase("000343")) {
            url = config.getBaciurl();
            user = config.getBaciuser();
            pass = config.getBacipassword();
        }
        if (filiale.equalsIgnoreCase("101151")) {
            url = config.getBabnurl();
            user = config.getBabnuser();
            pass = config.getBabnpassword();
        }
        if (filiale.equalsIgnoreCase("901388")) {
            url = config.getBatgurl();
            user = config.getBatguser();
            pass = config.getBatgpassword();
        }
        if (filiale.equalsIgnoreCase("201342")) {
            url = config.getBabfurl();
            user = config.getBabfuser();
            pass = config.getBabfpassword();
        }
        if (filiale.equalsIgnoreCase("701366")) {
            url = config.getBaneurl();
            user = config.getBaneuser();
            pass = config.getBanepassword();
        }
        if (filiale.equalsIgnoreCase("301355")) {
            url = config.getBamlurl();
            user = config.getBamluser();
            pass = config.getBamlpassword();
        }
        if (filiale.equalsIgnoreCase("901377")) {
            url = config.getBasnurl();
            user = config.getBasnuser();
            pass = config.getBasnpassword();
        }
        if (filiale.equalsIgnoreCase("BACM")) {
            url = config.getBacmurl();
            user = config.getBacmuser();
            pass = config.getBacmpassword();
        }
        Database db = new Database(config.getDriver());
        ResultSet rs = null;
        try {
            db.open(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            result = " ";
            return result;
        }
        String sql = "select y1 from fx5y8 where tname='TITU' and model='DEPNAIS' and x1='" + codeville + "'";
        try {
            rs = db.GetResult(sql);
            // Verifier le type de compte (societe ou personne physique)
            result = " ";
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (NullPointerException | SQLException e) {
            e.printStackTrace();
            result = " ";
            return result;
        }
        db.close();

        return result;
    }

    public String getISOPays(String filiale, String codepays) {
        //double balance=0;
        String status = "";
        boolean find = false;
        String result = "99";
        String url = "", user = "", pass = "";
        StringBuilder sb = new StringBuilder();
        if (filiale.equalsIgnoreCase("000343")) {
            url = config.getBaciurl();
            user = config.getBaciuser();
            pass = config.getBacipassword();
        }
        if (filiale.equalsIgnoreCase("101151")) {
            url = config.getBabnurl();
            user = config.getBabnuser();
            pass = config.getBabnpassword();
        }
        if (filiale.equalsIgnoreCase("901388")) {
            url = config.getBatgurl();
            user = config.getBatguser();
            pass = config.getBatgpassword();
        }
        if (filiale.equalsIgnoreCase("201342")) {
            url = config.getBabfurl();
            user = config.getBabfuser();
            pass = config.getBabfpassword();
        }
        if (filiale.equalsIgnoreCase("701366")) {
            url = config.getBaneurl();
            user = config.getBaneuser();
            pass = config.getBanepassword();
        }
        if (filiale.equalsIgnoreCase("301355")) {
            url = config.getBamlurl();
            user = config.getBamluser();
            pass = config.getBamlpassword();
        }
        if (filiale.equalsIgnoreCase("901377")) {
            url = config.getBasnurl();
            user = config.getBasnuser();
            pass = config.getBasnpassword();
        }
        if (filiale.equalsIgnoreCase("BACM")) {
            url = config.getBacmurl();
            user = config.getBacmuser();
            pass = config.getBacmpassword();
        }
        Database db = new Database(config.getDriver());
        ResultSet rs = null;
        try {
            db.open(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            result = "000";
            return result;
        }
        String sql = "select NVL(ISO3N,'000') from pays where pays='" + codepays + "'";
        try {
            rs = db.GetResult(sql);
            // Verifier le type de compte (societe ou personne physique)
            result = "000";
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "000";
            return result;
        }

        db.close();
        return result;
    }

    public String getpin() {
        int code = 0;
        code = 1000000000 + (int) (Math.random() * 9000000000D);
        code = code < 0 ? -code : code;
        String SecureCode = (new StringBuilder()).append("92").append(code).toString();
        return SecureCode;
    }
    // paramstr
    @SuppressWarnings("CallToPrintStackTrace")
    public String getDatoper(String filiale)
    {
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1"; 
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_BANKDATOPER()}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    
        public String getDatoper2(String filiale)
    {
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_BANKDATOPER2()}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    public String getClientinfos(String filiale,String racine){
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_GET_INFOS_CLIENT(?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, racine);
                boolean execute = cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String getClientinfos2(String filiale,String racine){
        String list = "";String myreq="",strResult="";;JSONObject resp = new JSONObject();
        Connection cn=null;CallableStatement cstmt;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"99\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                return strResult;
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_GET_INFOS_CLIENT2(?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, racine);
                boolean execute = cstmt.execute();
                strResult = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"994\",\"result\": \"SQL_GENERAL_ERROR\" }";
                return strResult;
            }
            db.close();
        } catch (Exception ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"995\",\"result\": \"GENERAL_EXCEPTION_RAISED\" }";
                return strResult;
        }
             
        return strResult;
    }
    public String getComptesclient(String filiale,String racine){
        String list = "";String myreq="",strResult="";;JSONObject resp = new JSONObject();
        Connection cn=null;CallableStatement cstmt;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"99\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                return strResult;
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_GET_COMPTES_CLIENT(?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, racine);
                boolean execute = cstmt.execute();
                strResult = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"994\",\"result\": \"SQL_GENERAL_ERROR\" }";
                return strResult;
            }
            db.close();
        } catch (Exception ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"995\",\"result\": \"GENERAL_EXCEPTION_RAISED\" }";
                return strResult;
        }
             
        return strResult;
    }

    public String getSolde2(String filiale,String compte){
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        String strResult="{ \"status\": 0,\"err_code\":\"98\",\"result\": \"INVALID_PARAMETER_STRING\" }";
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_GET_SOLDE_MULTI(?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                boolean execute = cstmt.execute();
                strResult = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return strResult;
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return strResult;
        }
             
        return strResult;
    }
    
    public String getComptesclient2(String filiale,String racine){
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call MBS_GETCOMPTES(?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, racine);
                boolean execute = cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    public String getAccountUpdates(String filiale){
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="",strResult="";
        Connection cn=null;CallableStatement cstmt;
        JSONObject resp = new JSONObject();
        Clob pcData;        
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"99\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                return strResult;
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_GETACCOUNTS_UPDATES}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.CLOB);
                boolean execute = cstmt.execute();
                pcData = cstmt.getClob(1);
                StringBuilder sb = this.getStringBuilderFromClob(pcData);
                resp = resp.put("status", 1);
                resp = resp.put("err_code", "OK");
                resp = resp.put("result",sb.toString());
                strResult = resp.toString();                
               db.close();                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"995\",\"result\": \"GENERAL_ERROR\" }";
                return strResult;
            }
            db.close();
        } catch (JSONException ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"995\",\"result\": \"FORMAT_JSON_ERROR\" }";
                return strResult;
        }
             
        return strResult;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String getClientUpdates(String filiale){
        String list = "";
        StringBuffer sql;sql = new StringBuffer();String myreq="",strResult="";
        Connection cn=null;CallableStatement cstmt;
        Clob pcData;JSONObject resp = new JSONObject();
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"99\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                return strResult;
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_GETCLIENT_UPDATES}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.CLOB);
                boolean execute = cstmt.execute();
                pcData = cstmt.getClob(1);
                StringBuilder sb = this.getStringBuilderFromClob(pcData);
                resp = resp.put("status", 1);
                resp = resp.put("err_code", "OK");
                resp = resp.put("result",sb.toString());
                strResult = resp.toString();                
                db.close(); 
               
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"98\",\"result\": \"ERROR2\" }";
            }
            db.close();
        } catch (JSONException ex) {
            ex.printStackTrace();
            strResult="{ \"status\": 0,\"err_code\":\"997\",\"result\": \"FORMAT_JSON_ERROR\" }";
        }
             
        return strResult;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String getAccountstatus(String filiale,String compte){
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call MBS_CHECKCPTSTATE(?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String callAlertmvt(String filiale)
    {
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_SMS()}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
   public String callAlertmvt2(String filiale)
    {
        String list = "";String myreq="",strResult="";
        Connection cn=null;CallableStatement cstmt;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"ERROR1\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                return strResult;
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_MVTDALERTES2}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.execute();
                strResult = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"ERROR2\",\"result\": \"SQL_ERROR_RAISED\" }";
                return strResult;
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            strResult="{ \"status\": 0,\"err_code\":\"997\",\"result\": \"GENERIC_EXCEPTION\" }";
            return strResult;
        }
             
        return strResult;
    }
    
    
    @SuppressWarnings("CallToPrintStackTrace")
    public String getAlertmvt(String filiale)
    {
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_GETALERTES()}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    
    public String getAlertmvt2(String filiale)
    {
        String list = "";String myreq="",strResult="";
        Connection cn=null;CallableStatement cstmt;
        JSONObject resp = new JSONObject();Clob pcData; 
        
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"ERROR1\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                return strResult;
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_GETALERTES2()}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.CLOB);
                boolean execute = cstmt.execute();
                pcData = cstmt.getClob(1);
                StringBuilder sb = this.getStringFromClob(pcData);
                if (sb.length() <=15) {
                    resp = resp.put("status", 0);
                    resp = resp.put("err_code", "NOK");
                    resp = resp.put("result"," ");                
                } else {
                    String newsb = sb.toString().split(":;:", -1)[2];
//                    sb = this.StringBuilderReplace((new StringBuilder()).append(newsb),"||",System.getProperty("line.separator"));
                    resp = resp.put("status", 1);
                    resp = resp.put("err_code", "OK");
                    resp = resp.put("result",newsb);                
                }

                strResult = resp.toString();                
               db.close();
               
            } catch (SQLException ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"ERROR2\",\"result\": \"SQL_ERROR_RAISED\" }";
                return strResult;
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            strResult="{ \"status\": 0,\"err_code\":\"997\",\"result\": \"GENERIC_EXCEPTION\" }";
            return strResult;
        }
             
        return strResult;
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    public String getsolde(String filiale,String compte)
    {
        String list = "";int nb=0,taille=0;int wtotlig=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_GET_SOLDE(?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    // 
    public String getShortstatement(String filiale,String compte,int numligne)
    {
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        String numligpad,signe,numlig="",ligreleve="";
        String datereleve="";String amountpad="",amount="",ligamount="",libelle="",sign="",lieu="";
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_MBK_MRELEVE(?,?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.setInt(3, numligne);
                boolean execute = cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String getShortstatement2(String filiale,String compte,int numligne)
    {
        String strResult="";StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"99\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                return strResult;
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_MBK_MRELEVE2(?,?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.setInt(3, numligne);
                boolean execute = cstmt.execute();
                strResult = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"997\",\"result\": \"GENERAL_SQL_EXCEPT_ERROR\" }";
                return strResult;
            }
            db.close();
        } catch (Exception ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"996\",\"result\": \"FORMAT_JSON_ERROR\" }";
                return strResult;
        }
             
        return strResult;
    }
    
    public String getSBSMinireleve(String filiale,String compte,int numligne)
    {
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        String numligpad,signe,numlig="",ligreleve="";
        String datereleve="";String amountpad="",amount="",ligamount="",libelle="",sign="",lieu="";
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_SBS_MRELEVE(?,?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.setInt(3, numligne);
                boolean execute = cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String getDetailoperation(String filiale,String compte,String nooper)
    {
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        String numligpad,signe,numlig="",ligreleve="";
        String datereleve="";String amountpad="",amount="",ligamount="",libelle="",sign="",lieu="";
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_GET_OPDETAIL(?,?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.setString(3, nooper);
                cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    @SuppressWarnings("CallToPrintStackTrace")
        public String getDetailoperation2(String filiale,String compte,String nooper)
    {
        String list = "",myreq="",strResult; Connection cn=null;CallableStatement cstmt;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"99\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                return strResult;
            }
            myreq = (new StringBuilder()).append("{? = call W_ED_GET_OPDETAIL2(?,?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.setString(3, nooper);
                cstmt.execute();
                strResult = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"997\",\"result\": \"SQL_GENERAL_ERROR\" }";
                return strResult;
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            strResult="{ \"status\": 0,\"err_code\":\"996\",\"result\": \"FORMAT_JSON_ERROR\" }";
            return strResult;
        }
             
        return strResult;
    }
    
    public String getAccountingcurmonth(String filiale,String compte,String date1, String date2){
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        String numligpad,signe,numlig="",ligreleve="";
        String datereleve="";String amountpad="",amount="",ligamount="",libelle="",sign="",lieu="";
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call MBS_EXTRAIT_COMPTE(?,?,?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.setString(3, date1);
                cstmt.setString(4, date2);
                boolean execute = cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    public String getAccounting(String filiale,String compte,String datefin){
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        String numligpad,signe,numlig="",ligreleve="";
        String datereleve="";String amountpad="",amount="",ligamount="",libelle="",sign="",lieu="";
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        @SuppressWarnings("UnusedAssignment")
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call MBS_RELEVE_MENSUEL(?,?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.setString(3, datefin);
                boolean execute = cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    public String findAlertechequier(String filiale,String compte){
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        @SuppressWarnings("UnusedAssignment")
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call MBS_ALERTECHEQUIER(?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                boolean execute = cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String findAlertedat(String filiale,String compte){
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call MBS_ALERTEDAT(?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                boolean execute = cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }
    public String findAlertecarte(String filiale,String compte){
        String list = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        try {
            DBCredential crdt = getDBCredential(filiale);
            Database db = new Database(config.getDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call MBS_ALERTECARTE(?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                boolean execute = cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";return list;
        }
             
        return list;
    }

    public String encypteCompte(String compte) {
        String cpt1 = "", cpt2 = "", displaycpt = "";
        String stamp = this.getCurtimestamp();
        cpt1 = compte.substring(0, 2);
        cpt2 = compte.substring(7, 11);
        displaycpt = (new StringBuilder()).append(cpt1).append("XXXXX").append(cpt2).toString();
        return displaycpt;
    }
    public String getCurtimestamp() {
        String strDatetime = "";
        String jour = "", mois = "", annee = "", hh = "", mm = "", ss = "";
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

        strDatetime = (new StringBuilder()).append(hhstr).append(mmstr).append(ssstr).toString();

        return strDatetime;
    }
    public String generatePDFAccounting(String compte,String pin,String date1, String date2){
       String filepath="";
           try {
               String dbdriver = config.getReportdbdriver();
               String dburl= config.getReportdburl();
               String dbuser = config.getReportdbuser();
               String dbpass = config.getReportdbpass();
               //Etablissement de la connexion pour le generateur d'etat
               Class.forName(dbdriver);
               Connection conn = DriverManager.getConnection(dburl, dbuser, dbpass);
               System.out.println("Génération de l'extrait ...");
               //Creation des parametres de l'etat
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("numCompte", compte);
                parameters.put("startDate", date1);
                parameters.put("endDate", date2);
               
                String stamp = this.getCurtimestamp();
                //Chargement et compilation jasperdesign
                String encryptedacct = encypteCompte(compte);
                String designfile = (new StringBuilder()).append(config.getReportdesignpath()).append("extrait.jrxml").toString();
                filepath = (new StringBuilder()).append(config.getReportoutputpath()).append("Extrait_").append(encryptedacct).append("_").append(stamp).append(".pdf").toString();
                JasperDesign jasperDesign = JRXmlLoader.load(designfile);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                
                //Export the report
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filepath);
                exporter.setParameter(JRPdfExporterParameter.OWNER_PASSWORD, pin);
                exporter.setParameter(JRPdfExporterParameter.USER_PASSWORD, pin);
                exporter.setParameter(JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE);
                exporter.exportReport();
                
                //Verifier que le fichier est bien cree
                File f =  new File(filepath);
                if (!f.exists()) {
                    filepath="";
                }
           
           } catch(Exception ex) {
               ex.printStackTrace();
           }
           
           return filepath;
       }
    public String generatePDFReleve(String compte,String pin,String date1, String date2){
       String filepath="";
           try {
               String dbdriver = config.getReportdbdriver();
               String dburl= config.getReportdburl();
               String dbuser = config.getReportdbuser();
               String dbpass = config.getReportdbpass();
               //Etablissement de la connexion pour le generateur d'etat
               Class.forName(dbdriver);
               Connection conn = DriverManager.getConnection(dburl, dbuser, dbpass);
               
               //Creation des parametres de l'etat
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("numCompte", compte);
                parameters.put("startDate", date1);
                parameters.put("endDate", date2);
               
                //Chargement et compilation jasperdesign
                String encryptedacct = encypteCompte(compte);
                String stamp = this.getCurtimestamp();
                String designfile = (new StringBuilder()).append(config.getReportdesignpath()).append("releve.jrxml").toString();
                filepath = (new StringBuilder()).append(config.getReportoutputpath()).append("Releve_").append(encryptedacct).append("_").append(stamp).append(".pdf").toString();
                JasperDesign jasperDesign = JRXmlLoader.load(designfile);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                
                //Export the report
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filepath);
                exporter.setParameter(JRPdfExporterParameter.OWNER_PASSWORD, pin);
                exporter.setParameter(JRPdfExporterParameter.USER_PASSWORD, pin);
                exporter.setParameter(JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE);
                exporter.exportReport();
                
                //Verifier que le fichier est bien cree
                File f =  new File(filepath);
                if (!f.exists()) {
                    filepath="";
                }
           
           } catch(Exception ex) {
               ex.printStackTrace();
           }
           
           return filepath;
       }
    public String generateXLSAccounting(String compte,String date1, String date2){
           String filepath="";
           try {
               String dbdriver = config.getReportdbdriver();
               String dburl= config.getReportdburl();
               String dbuser = config.getReportdbuser();
               String dbpass = config.getReportdbpass();
               //Etablissement de la connexion pour le generateur d'etat
               Class.forName(dbdriver);
               Connection conn = DriverManager.getConnection(dburl, dbuser, dbpass);
               
               //Creation des parametres de l'etat
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("numCompte", compte);
                parameters.put("startDate", date1);
                parameters.put("endDate", date2);
               
                //Chargement et compilation jasperdesign
                String encryptedacct = encypteCompte(compte);
                String designfile = (new StringBuilder()).append(config.getReportdesignpath()).append("extrait.jrxml").toString();
                filepath = (new StringBuilder()).append(config.getReportoutputpath()).append(encryptedacct).append(".xls").toString();
                JasperDesign jasperDesign = JRXmlLoader.load(designfile);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                
                //Export the report
                JRXlsExporter exporter = new JRXlsExporter();
                exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, filepath);
                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.FALSE);
                exporter.exportReport();
                
                //Verifier que le fichier est bien cree
                File f =  new File(filepath);
                if (!f.exists()) {
                    filepath="";
                }
           
           } catch(Exception ex) {
               ex.printStackTrace();
           }
           return filepath;
       }
    public String genererExtrait(String compte,String pin,String format,String datedeb, String datefin){
           
           String filepath="";
           
           if (format.equalsIgnoreCase("PDF")){
               filepath = generatePDFAccounting( compte, pin, datedeb,  datefin);
           }
           if (format.equalsIgnoreCase("XLS")){
               filepath = generateXLSAccounting( compte, datedeb,  datefin);
           }
       
           return filepath;
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
    public String sendPeriodicaccounting(String filiale,String compte,String email,String format,String pin,String datedeb,String datefin){
            String strResult = "";int nb=0,taille=0;int wtotlig=0;int  montant=0;
            ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
            Connection cn=null;CallableStatement cstmt; String datet=getCurdate("/");
            try {
                DBCredential crdt = getDBCredential(filiale);
                Database db = new Database(config.getDriver());
                try
                {
                    db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
                }
                catch(Exception e)
                {
                    e.printStackTrace(); return "99";
                }           
                myreq = (new StringBuilder()).append("{? = call SMBS_EXTRAIT_COMPTE(?,?,?)}").toString();

                cn = db.getConn(); 
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.setString(3, datedeb);
                cstmt.setString(4, datefin);
                boolean execute = cstmt.execute();
                strResult = cstmt.getString(1);
                db.close();
            }  catch(Exception ex) {
                ex.printStackTrace();return "98";
            } 
     // Generer l'extrait de compte periodique si pas d'erreur
            if(strResult.equalsIgnoreCase("OK")){
                //Generer le rapport selon le format
                strResult = "";
                if (format.equalsIgnoreCase("PDF") || format.equalsIgnoreCase("XLS")) {
                    strResult = this.genererExtrait(compte, pin, format, datedeb, datefin);
                } 
                if (strResult.length() > 0) {
                    //Le fichier a ete bien genere, envoyer par email
                    String content = config.getExtraitmailcontent();
                    strResult = this.emailDocument(email, "Votre Extrait de compte periodique",content, strResult);
                    
                }else{
                    System.out.println("Fichier Jasper non généré");
                } 
                
            
            } else {
                return strResult;
            }
            
       return strResult;
     }
    public String sendAccounting(String filiale,String compte,String email,String pin,String datedeb,String datefin){
            String strResult = "";int nb=0,taille=0;int wtotlig=0;int  montant=0;
            ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
            Connection cn=null;CallableStatement cstmt; String datet=getCurdate("/");
            try {
                DBCredential crdt = getDBCredential(filiale);
                Database db = new Database(config.getDriver());
                try
                {
                    db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
                }
                catch(Exception e)
                {
                    e.printStackTrace(); return "99";
                }           
                myreq = (new StringBuilder()).append("{? = call SMBS_RELEVE_MENSUEL(?,?)}").toString();

                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.setString(3, datefin);
               // cstmt.setString(4, datefin);
                boolean execute = cstmt.execute();
                strResult = cstmt.getString(1);
               db.close();
            }  catch(Exception ex) {
                ex.printStackTrace();return "98";
            } 
            
            // Generer le releve si pas d'erreur
            if(strResult.equalsIgnoreCase("OK")){
                //Generer le rapport selon le format PDF
                strResult = "";
                strResult = this.generatePDFReleve(compte, pin, datedeb, datefin);
                if (strResult.length() > 0) {
                    //Le fichier a ete bien genere, envoyer par email
                    String content = config.getRelevemailcontent();
                    strResult = this.emailDocument(email, "Votre Releve de Compte Mensuel",content, strResult);
                    
                } 
                
            
            } else {
                return strResult;
            }
           
       return strResult;
     }
    @SuppressWarnings("CallToPrintStackTrace")
    public String generateAccountingfile(String filiale,String client,String compte,String datedeb,String datefin){
            String strResult = "";int nb=0,taille=0;int wtotlig=0;int  montant=0;
            ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
            Connection cn=null;CallableStatement cstmt; String datet=getCurdate("/");
            JSONObject resp = new JSONObject();
            Clob pcData;
            try {
                DBCredential crdt = getDBCredential(filiale);
                Database db = new Database(config.getDriver());
                try
                {
                    db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    strResult="{ \"status\": 0,\"err_code\":\"99\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                    return strResult;
                }           
                myreq = (new StringBuilder()).append("{? = call W_ED_GET_RELEVE(?,?,?,?)}").toString();

                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.CLOB);
                cstmt.setString(2, client);
                cstmt.setString(3, compte);
                cstmt.setString(4, datedeb);
                cstmt.setString(5, datefin);
                boolean execute = cstmt.execute();
                pcData = cstmt.getClob(1);
                StringBuilder sb = this.getStringBuilderFromClob(pcData);
                int length = sb.length();
                
                if (length <= 6) {
                     //Erreur générée
                    resp = resp.put("status", 0);
                    resp = resp.put("err_code", "NOK");
                    resp = resp.put("result", sb.toString());
                    strResult = resp.toString();
                    System.out.println(strResult);                        

                } else {
                    // Generation du fichier
                   boolean status =  genReportFile ("releve",sb,client,compte);
                   if (status==true) {
                        String msg =" Ligne(s) de fichier généré à l'emplacement par défaut";
                        resp = resp.put("status", 1);
                        resp = resp.put("err_code", "OK");
                        resp = resp.put("result",msg);
                        strResult = resp.toString();
                        System.out.println(strResult);                    
                   } else {
                        resp = resp.put("status", 0);
                        resp = resp.put("err_code", "NOK");
                        resp = resp.put("result", "FILE_GENERATION_ERROR");
                        strResult = resp.toString();
                        System.out.println(strResult);                        
                   }
                }
               db.close();
            }  catch(Exception ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"995\",\"result\": \"GENERAL_ERROR\" }";
                return strResult;
            } 
       return strResult;
     }
    public String generateAccountingfile2(String filiale,String client,String compte,String datedeb,String datefin){
            String strResult = "";int nb=0,taille=0;int wtotlig=0;int  montant=0;
            ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
            Connection cn=null;CallableStatement cstmt; String datet=getCurdate("/");
            JSONObject resp = new JSONObject();
            Clob pcData;
            try {
                DBCredential crdt = getDBCredential(filiale);
                Database db = new Database(config.getDriver());
                try
                {
                    db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    strResult="{ \"status\": 0,\"err_code\":\"99\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                    return strResult;
                }           
                myreq = (new StringBuilder()).append("{? = call W_ED_GET_EXTRAIT(?,?,?,?)}").toString();

                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.CLOB);
                cstmt.setString(2, client);
                cstmt.setString(3, compte);
                cstmt.setString(4, datedeb);
                cstmt.setString(5, datefin);
                boolean execute = cstmt.execute();
                pcData = cstmt.getClob(1);
                StringBuilder sb = this.getStringBuilderFromClob(pcData);
                int length = sb.length();
                
                if (length <= 6) {
                     //Erreur générée
                    resp = resp.put("status", 0);
                    resp = resp.put("err_code", "NOK");
                    resp = resp.put("result", sb.toString());
                    strResult = resp.toString();
                    System.out.println(strResult);                        

                } else {
                    // Generation du fichier
                   boolean status =  genReportFile ("extrait",sb,client,compte);
                   if (status==true) {
                        String msg =" Ligne(s) de fichier généré à l'emplacement par défaut";
                        resp = resp.put("status", 1);
                        resp = resp.put("err_code", "OK");
                        resp = resp.put("result",msg);
                        strResult = resp.toString();
                        System.out.println(strResult);                    
                   } else {
                        resp = resp.put("status", 0);
                        resp = resp.put("err_code", "NOK");
                        resp = resp.put("result", "FILE_GENERATION_ERROR");
                        strResult = resp.toString();
                        System.out.println(strResult);                        
                   }
                }
               db.close();
            }  catch(Exception ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"995\",\"result\": \"GENERAL_ERROR\" }";
                return strResult;
            } 
       return strResult;
     }
    public String getContacts(String filiale){
            String strResult = "";
           StringBuffer sql;sql = new StringBuffer();String myreq="";
            Connection cn=null;CallableStatement cstmt; 
            JSONObject resp = new JSONObject();
            Clob pcData;
            try {
                DBCredential crdt = getDBCredential(filiale);
                Database db = new Database(config.getDriver());
                try
                {
                    db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    strResult="{ \"status\": 0,\"err_code\":\"99\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                    return strResult;
                }           
                myreq = (new StringBuilder()).append("{? = call W_ED_GETCONTACTS_UPDATES}").toString();

                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.CLOB);
                
                boolean execute = cstmt.execute();
                pcData = cstmt.getClob(1);
                StringBuilder sb = this.getStringBuilderFromClob(pcData);
                resp = resp.put("status", 1);
                resp = resp.put("err_code", "OK");
                resp = resp.put("result",sb.toString());
                strResult = resp.toString();                
               db.close();
            }  catch(Exception ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"995\",\"result\": \"GENERAL_ERROR\" }";
                return strResult;
            } 
       return strResult;
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
   
    public String testmail(){
       
           String strResult = this.emailDocument("c.melarga@gmail.com", "Votre Extrait de compte periodique","<h1> Bonjour</h1> <br> Merci de trouver en attache&acute; votre extrait de compte.<br> La banque vous remercie pour votre confiance. <br><br> Cordialement,<br>", "C:\\Projets\\SMBServices\\Reportdesigns\\extrait.jasper");
       
           return strResult;
       }
    @SuppressWarnings("CallToPrintStackTrace")
    public String insertNewclient(String filiale,String vclient, String vcompte,String vnom,String vformule ,String vtarif)
    {
        String strResult = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt; String datet=getCurdate("/");
        try {
                DBCredential crdt = getDBCredential(filiale);
                Database db = new Database(config.getDriver());
                try
                {
                    db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
                }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }           
            myreq = (new StringBuilder()).append("{? = call W_ED_INSERTCLIENT(?,?,?,?,?)}").toString();
            
            try {
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, vclient);
                cstmt.setString(3, vcompte);
                cstmt.setString(4, vnom);
                cstmt.setString(5, vformule);
                cstmt.setString(6, vtarif);
                cstmt.execute();
                strResult = cstmt.getString(1);
                 
            } catch (SQLException ex) {
               ex.printStackTrace();
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            strResult = "997";return strResult;
        }
             
        return strResult;
    }
    @SuppressWarnings("CallToPrintStackTrace")
        public String insertNewclient2(String filiale,String vclient, String vcompte,String vnom,String vformule ,String vtarif)
    {
        String strResult = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt; String datet=getCurdate("/");
        try {
                DBCredential crdt = getDBCredential(filiale);
                Database db = new Database(config.getDriver());
                try
                {
                    db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
                }
            catch(Exception e)
            {
                e.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"99\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                return strResult;
            }           
            myreq = (new StringBuilder()).append("{? = call W_ED_INSERTCLIENT2(?,?,?,?,?)}").toString();
            
            try {
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, vclient);
                cstmt.setString(3, vcompte);
                cstmt.setString(4, vnom);
                cstmt.setString(5, vformule);
                cstmt.setString(6, vtarif);
                cstmt.execute();
                strResult = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"997\",\"result\": \"SQL_GENERAL_ERROR\" }";
                return strResult;
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            strResult="{ \"status\": 0,\"err_code\":\"996\",\"result\": \"GENERAL_EXCEPTION_RAISED\" }";
            return strResult;
        }
             
        return strResult;
    }
    
    
    @SuppressWarnings({"CallToPrintStackTrace", "UseSpecificCatch"})
    public String updateAccount(String filiale,String vcompte,String vformule ,String vtarif)
    {
        String strResult = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;
        try {
                DBCredential crdt = getDBCredential(filiale);
                Database db = new Database(config.getDriver());
                try
                {
                    db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
                }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
           
            myreq = (new StringBuilder()).append("{? = call W_ED_UPDATECLIENT(?,?,?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, vcompte);
                cstmt.setString(3, vformule);
                cstmt.setString(4, vtarif);
                cstmt.execute();
                strResult = cstmt.getString(1);
                 
            } catch (SQLException ex) {
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            strResult = "997";return strResult;
        }
             
        return strResult;
    }

    @SuppressWarnings({"CallToPrintStackTrace", "UseSpecificCatch"})
    public String updateAccount2(String filiale,String vcompte,String vnom,String vformule ,String vtarif)
    {
        String strResult = "";String myreq="";
        Connection cn=null;CallableStatement cstmt;
        try {
                DBCredential crdt = getDBCredential(filiale);
                Database db = new Database(config.getDriver());
                try
                {
                    db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
                }
            catch(Exception e)
            {
                e.printStackTrace(); 
                strResult="{ \"status\": 0,\"err_code\":\"ERROR1\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                return strResult;
                
            }
           
            myreq = (new StringBuilder()).append("{? = call W_ED_UPDATECLIENT2(?,?,?,?)}").toString();
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, vcompte);
                cstmt.setString(3, vnom);
                cstmt.setString(4, vformule);
                cstmt.setString(5, vtarif);
                cstmt.execute();
                strResult = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"ERROR2\",\"result\": \"SQL_EXCEPTION_RAISED\" }";
                return strResult;
            }
            db.close();
        } catch (Exception ex) {
            strResult="{ \"status\": 0,\"err_code\":\"997\",\"result\": \"GENERAL_ERROR\" }";
            return strResult;
        }
             
        return strResult;
    }
    
    @SuppressWarnings({"CallToThreadDumpStack", "CallToPrintStackTrace"})
    public String changeAccountstate(String filiale,String vcompte,String status)
    {
        String strResult = "";int nb=0,taille=0;int wtotlig=0;
        int  montant=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt; String datet=getCurdatetime("/");
        try {
                DBCredential crdt = getDBCredential(filiale);
                Database db = new Database(config.getDriver());
                try
                {
                    db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
                }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
           
            myreq = (new StringBuilder()).append("{? = call W_ED_UPDATESTATUS(?,?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, vcompte);
                cstmt.setString(3, status);
                cstmt.execute();
                strResult = cstmt.getString(1);
                 
            } catch (SQLException ex) {
               return "ERROR2";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            strResult = "997";return strResult;
        }
             
        return strResult;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public String changeAccountstate2(String filiale,String vcompte,String status)
    {
        String strResult = "";String myreq="";
        Connection cn=null;CallableStatement cstmt;
        try {
                DBCredential crdt = getDBCredential(filiale);
                Database db = new Database(config.getDriver());
                try
                {
                    db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
                }
            catch(Exception e)
            {
                e.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"ERROR1\",\"result\": \"DATA_BASE_CONNEXION_ERROR\" }";
                return strResult;
            }
           
            myreq = (new StringBuilder()).append("{? = call W_ED_UPDATESTATUS2(?,?)}").toString();
            
            try {
                System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, vcompte);
                cstmt.setString(3, status);
                cstmt.execute();
                strResult = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
                strResult="{ \"status\": 0,\"err_code\":\"ERROR2\",\"result\": \"SQL_GENERAL_ERROR\" }";
                return strResult;
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            strResult="{ \"status\": 0,\"err_code\":\"997\",\"result\": \"GENERIC_EXCEPTION_RAISED\" }";
            return strResult;
        }
             
        return strResult;
    }
        
    public String getBalfilename(String filiale) {
        // Sample = MD-BALFILE-1-20121129154550.dat
        String filename = "";
        @SuppressWarnings("UnusedAssignment")
                String jour = "";
        String mois = "", annee = "", hh = "", mm = "", ss = "";

        try {
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

            filename = (new StringBuilder()).append("MD-BALFILE-").append(filiale).append("-").append(year).append(moisstr).append(jourstr).append(hhstr).append(mmstr).append(ssstr).toString();
        } catch (Exception ex) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }

        return filename;
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
   
}
