/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import org.atware.bean.ConfigPay;

/**
 *
 * @author yacou.kone
 */
public class ConnexionEfacture {
    private ConfigPay getInfoFacturier;
    
    public ConnexionEfacture(String facturier){
        this.getInfoFacturier = new ConfigPay(facturier);
    }

    public ConfigPay getGetInfoFacturier() {
        return getInfoFacturier;
    }
    
    
     //connexion à la base de données
    public Connection maConnexion(){
        Connection con =null;
        String url;String user;String pwd; String driver;
        
        try {
            Class.forName(getInfoFacturier.getDbdriver());
            con = DriverManager.getConnection(getInfoFacturier.getDbUrl(), getInfoFacturier.getDbuser(), getInfoFacturier.getDbpassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
    
}
