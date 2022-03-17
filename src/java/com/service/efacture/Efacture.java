/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.efacture;

import javax.jws.WebService;

/**
 *
 * @author yacou.kone
 */
@WebService(serviceName = "T24WebServicesImplService", portName = "T24WebServicesImplPort", endpointInterface = "com.temenos.twsefacture.T24WebServicesImpl", targetNamespace = "http://temenos.com/TWSEFACTURE", wsdlLocation = "WEB-INF/wsdl/Efacture/192.168.220.45_8080/TWSEFACTURE/services.wsdl")
public class Efacture {

    public void tnaefacturemain(com.temenos.twsefacture.WebRequestCommon webRequestCommon, com.temenos.twsefacture.EnquiryInput tnaefacturemainType, javax.xml.ws.Holder<com.temenos.twsefacture.Status> status, javax.xml.ws.Holder<java.util.List<com.temenos.tnaefacturemain.TNAEFACTUREMAINType>> tnaefacturemainType0) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
