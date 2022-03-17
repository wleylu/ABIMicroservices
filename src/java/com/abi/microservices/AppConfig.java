/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abi.microservices;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author MELARGA
 */
@ApplicationPath("/")
public class AppConfig extends Application {
    public void addRestfunction(Set<Class<?>> ressource){
        ressource.add(com.abi.microservices.ServicesResource.class);
        ressource.add(com.abi.microservices.ApiNMPFServices.class);
     }
    
    @Override
    public Set<Class<?>> getClasses(){
        Set<Class<?>> res = new HashSet<>();
        addRestfunction(res);
        
        return res;
    
    }
    
}
