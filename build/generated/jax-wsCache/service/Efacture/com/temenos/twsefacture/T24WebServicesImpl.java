
package com.temenos.twsefacture;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import com.temenos.tnaefacturemain.TNAEFACTUREMAINType;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.11-b150120.1832
 * Generated source version: 2.2
 * 
 */
@WebService(name = "T24WebServicesImpl", targetNamespace = "http://temenos.com/TWSEFACTURE")
@XmlSeeAlso({
    com.temenos.tnaefacturemain.ObjectFactory.class,
    com.temenos.twsefacture.ObjectFactory.class,
    net.java.dev.jaxb.array.ObjectFactory.class
})
public interface T24WebServicesImpl {


    /**
     * 
     * @param webRequestCommon
     * @param tnaefacturemainType0
     * @param tnaefacturemainType
     * @param status
     */
    @WebMethod(operationName = "TNAEFACTUREMAIN")
    @RequestWrapper(localName = "TNAEFACTUREMAIN", targetNamespace = "http://temenos.com/TWSEFACTURE", className = "com.temenos.twsefacture.TNAEFACTUREMAIN")
    @ResponseWrapper(localName = "TNAEFACTUREMAINResponse", targetNamespace = "http://temenos.com/TWSEFACTURE", className = "com.temenos.twsefacture.TNAEFACTUREMAINResponse")
    @Action(input = "http://temenos.com/TWSEFACTURE/T24WebServicesImpl/TNAEFACTUREMAINRequest", output = "http://temenos.com/TWSEFACTURE/T24WebServicesImpl/TNAEFACTUREMAINResponse")
    public void tnaefacturemain(
        @WebParam(name = "WebRequestCommon", targetNamespace = "")
        WebRequestCommon webRequestCommon,
        @WebParam(name = "TNAEFACTUREMAINType", targetNamespace = "")
        EnquiryInput tnaefacturemainType,
        @WebParam(name = "Status", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<Status> status,
        @WebParam(name = "TNAEFACTUREMAINType", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<List<TNAEFACTUREMAINType>> tnaefacturemainType0);

}
