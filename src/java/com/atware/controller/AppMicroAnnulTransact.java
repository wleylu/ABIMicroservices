/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.controller;

import com.atware.utils.Utility;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.atware.bean.DBCredential;
import org.atware.bean.TransactionInfo;
import org.atware.jdbc.Database;


/**
 *
 * @author Melarga.COULIBALY
 */
public class AppMicroAnnulTransact {
    public Utility utils = null;
    private DBCredential dbcnx ;
    
    @SuppressWarnings("CallToPrintStackTrace")
    public AppMicroAnnulTransact() {
            utils = new Utility();
            dbcnx = new DBCredential();
            System.out.println("ConfigInfos() successfully called from init AppController");
         
    }
    
    public String getAnnulTransaction(TransactionInfo transact,String filiale) throws IOException {
        @SuppressWarnings("UnusedAssignment")
        String result = "99";
        String url = "", user = "", pass = "";
        StringBuilder sb = new StringBuilder();
        Database dbsession;PreparedStatement ps;
        ResultSet lst=null;  Connection cn;CallableStatement cstmt;int vnbre=0;
       // System.out.println("Filiale="+filiale);
        dbcnx = utils.getDbFiliale(filiale);
        dbsession = new Database(dbcnx.getDbDriver().trim());
        ResultSet rs = null;
        try {

            dbsession.open(dbcnx.getUrl().trim(), dbcnx.getUsername().trim(), dbcnx.getPassword().trim());
        } catch (Exception e) {
            result = "98";
            return result;
        }
            String sql = (new StringBuilder()).append("{? = call ABI_MSRV_POSTANNUL(?,?,?,?,?)}").toString();
            
            try {
            //    System.out.println("SQL = "+sql);
                cn = dbsession.getConn();
                cstmt = cn.prepareCall(sql);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, transact.getFacturier());
                 cstmt.setString(3, transact.getCaisse());
                 cstmt.setString(4, transact.getDhtransact());
                 cstmt.setString(5, transact.getRefrel());
                 cstmt.setString(6, transact.getRefOper());
                 
                boolean execute = cstmt.execute();
                result = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                result = "97";
                return result;
            }
        dbsession.close();
       // System.out.println("result=" + result);
        
        return result;
    }
    
        
}
