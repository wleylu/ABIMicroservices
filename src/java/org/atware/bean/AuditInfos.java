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
public class AuditInfos implements  Serializable{

            private String filiale;
            private String datoper;
            private String app_user;
            private String service_code;
            private String apiKey;
            private String hostName;
            private String idAddress;
            private String resquestData;
            private String resultData;

    public String getFiliale() {
        return filiale;
    }

    public void setFiliale(String filiale) {
        this.filiale = filiale;
    }

    public String getDatoper() {
        return datoper;
    }

    public void setDatoper(String datoper) {
        this.datoper = datoper;
    }

    public String getApp_user() {
        return app_user;
    }

    public void setApp_user(String app_user) {
        this.app_user = app_user;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

    public String getResquestData() {
        return resquestData;
    }

    public void setResquestData(String resquestData) {
        this.resquestData = resquestData;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public AuditInfos() {
    }

    public AuditInfos(String filiale, String datoper, String app_user, 
                String service_code, String apiKey, String hostName,
                String idAddress, String resquestData, String resultData) {
        this.filiale = filiale;
        this.datoper = datoper;
        this.app_user = app_user;
        this.service_code = service_code;
        this.apiKey = apiKey;
        this.hostName = hostName;
        this.idAddress = idAddress;
        this.resquestData = resquestData;
        this.resultData = resultData;
        
               
    }
            
            
    
}
