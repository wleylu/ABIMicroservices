/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abi.security;

import com.atware.utils.AppLogger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.xml.ws.developer.JAXWSProperties;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.atware.bean.ConfigInfos;
import org.atware.bean.DBCredential;

/**
 *
 * @author melarga.coulibaly
 */
public class Authorization {
    @Resource
    WebServiceContext webServiceContext;

    public Authorization() {
    }
    
    public boolean checkCredentials(ConfigInfos config,AppLogger logger,String filiale,String remoteAddr,String serviceid,String apikey){
            boolean result=false;
            
            
            return result;
    } 
    
}
