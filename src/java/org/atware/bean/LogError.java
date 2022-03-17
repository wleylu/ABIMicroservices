/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.melware.bean;

/**
 *
 * @author romeo.kouame
 * Java bean pour la gestion des codes d'erreurs
 */
public class LogError {
    
    private String errorcode;
    private String message;
    
    public LogError() {
    }

    public LogError(String errcode, String msg) {
        this.errorcode = errcode;
        this.message = msg;
    }    

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String code) {
        this.errorcode = code;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String mess) {
        this.message = mess;
    }
    
    
}
