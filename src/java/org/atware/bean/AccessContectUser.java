/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.bean;

import java.io.Serializable;

/**
 *
 * @author yacou.kone
 */
public class AccessContectUser implements Serializable{
    private String login=null;
    private String adip;
    private String tokenValid;
    private int dateAccess;
    private int heureAcesses;
    private String catchaValid;
    private boolean blockedLogin = false;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAdip() {
        return adip;
    }

    public void setAdip(String adip) {
        this.adip = adip;
    }

    public String getTokenValid() {
        return tokenValid;
    }

    public void setTokenValid(String tokenValid) {
        this.tokenValid = tokenValid;
    }

    public int getDateAccess() {
        return dateAccess;
    }

    public void setDateAccess(int dateAccess) {
        this.dateAccess = dateAccess;
    }

    public int getHeureAcesses() {
        return heureAcesses;
    }

    public void setHeureAcesses(int heureAcesses) {
        this.heureAcesses = heureAcesses;
    }

    public String getCatchaValid() {
        return catchaValid;
    }

    public void setCatchaValid(String catchaValid) {
        this.catchaValid = catchaValid;
    }

    public boolean isBlockedLogin() {
        return blockedLogin;
    }

    public void setBlockedLogin(boolean blockedLogin) {
        this.blockedLogin = blockedLogin;
    }

    public AccessContectUser() {
    }

    public AccessContectUser(String login, String adip, String tokenValid, int dateAccess, int heureAcesses, String catchaValid) {
        this.login = login;
        this.adip = adip;
        this.tokenValid = tokenValid;
        this.dateAccess = dateAccess;
        this.heureAcesses = heureAcesses;
        this.catchaValid = catchaValid;
    }
    
    
    
    
    
}
