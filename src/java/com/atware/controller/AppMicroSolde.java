/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.controller;

import com.atware.utils.Utility;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.atware.bean.DBCredential;
import org.atware.jdbc.Database;

/**
 *
 * @author yacou.kone
 */
public class AppMicroSolde {
    
    
     @SuppressWarnings("CallToPrintStackTrace")
    public String getsolde(String filiale,String compte)
    {
        String list = "";int nb=0,taille=0;int wtotlig=0;
        ResultSet lst=null;StringBuffer sql;sql = new StringBuffer();String myreq="";
        Connection cn=null;CallableStatement cstmt;int vnbre=0;
        Utility utility =new Utility();
        
        
        try {
            DBCredential crdt =utility.getDbFiliale(filiale);
                  Database db = new Database(crdt.getDbDriver());
            try
            {
                db.open(crdt.getUrl(), crdt.getUsername(), crdt.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace(); return "ERROR1";
            }
            myreq = (new StringBuilder()).append("{? = call EXTRACT.ABI_MSRV_SOLDE(?)}").toString();
            
            try {
               // System.out.println("SQL = "+myreq);
                cn = db.getConn();
                cstmt = cn.prepareCall(myreq);
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.setString(2, compte);
                cstmt.execute();
                list = cstmt.getString(1);
                 
            } catch (SQLException ex) {
                ex.printStackTrace();
               return "{\"code\":\"99\"\"message \":\"ERROR2\"}";
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            list = "997";
            return "{\"code\":\"997\"\"message \":\"ERROR2\"}";
        }
             
        return list;
    }
    
}
