/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.bean;

/**
 *
 * @author Melarga.COULIBALY
 */
public class DBCredential {
    
    private String url="";
    private String username="";
    private String password="";
    private String dbDriver=null;
    
    
    public DBCredential()
    {
    }    
    public DBCredential(String url,String username,String password,String dbDriver){
        this.url = url;
        this.username = username;
        this.password = password;  
        this.dbDriver= dbDriver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
            
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }
    
    
            
}
