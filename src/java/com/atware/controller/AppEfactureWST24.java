/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.controller;

import com.atware.utils.ServiceResultatJson;
import java.text.ParseException;
import org.atware.bean.Client;
import org.atware.bean.TransactionT24;

/**
 *
 * @author yacou.kone
 */
public class AppEfactureWST24 {
    private InterfaceEfactureWS serviceT24;
    private ServiceResultatJson traitementJson;
    
    public AppEfactureWST24() throws ParseException{
        serviceT24 = new InterfaceEfactureWS();
        traitementJson = new ServiceResultatJson();
    }
    
    // methose de signaletique
    public String getInfoClient(String clt) throws ParseException{
        String infoClient = serviceT24.getSiganaletique(clt);        
       return traitementJson.getClientInfos(infoClient);
    }
    
    //methose de comptabilisation transaction
    public String setComptaTransaction(TransactionT24 paiement) throws ParseException{
        String opers = serviceT24.setCompta(paiement);
        return traitementJson.getRefTransaction(opers);
    }
    
    
}
