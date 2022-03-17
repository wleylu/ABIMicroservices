
package com.temenos.twsefacture;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.temenos.twsefacture package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TNAEFACTUREMAIN_QNAME = new QName("http://temenos.com/TWSEFACTURE", "TNAEFACTUREMAIN");
    private final static QName _TNAEFACTUREMAINResponse_QNAME = new QName("http://temenos.com/TWSEFACTURE", "TNAEFACTUREMAINResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.temenos.twsefacture
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TNAEFACTUREMAIN }
     * 
     */
    public TNAEFACTUREMAIN createTNAEFACTUREMAIN() {
        return new TNAEFACTUREMAIN();
    }

    /**
     * Create an instance of {@link TNAEFACTUREMAINResponse }
     * 
     */
    public TNAEFACTUREMAINResponse createTNAEFACTUREMAINResponse() {
        return new TNAEFACTUREMAINResponse();
    }

    /**
     * Create an instance of {@link WebRequestCommon }
     * 
     */
    public WebRequestCommon createWebRequestCommon() {
        return new WebRequestCommon();
    }

    /**
     * Create an instance of {@link EnquiryInput }
     * 
     */
    public EnquiryInput createEnquiryInput() {
        return new EnquiryInput();
    }

    /**
     * Create an instance of {@link EnquiryInputCollection }
     * 
     */
    public EnquiryInputCollection createEnquiryInputCollection() {
        return new EnquiryInputCollection();
    }

    /**
     * Create an instance of {@link Status }
     * 
     */
    public Status createStatus() {
        return new Status();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TNAEFACTUREMAIN }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://temenos.com/TWSEFACTURE", name = "TNAEFACTUREMAIN")
    public JAXBElement<TNAEFACTUREMAIN> createTNAEFACTUREMAIN(TNAEFACTUREMAIN value) {
        return new JAXBElement<TNAEFACTUREMAIN>(_TNAEFACTUREMAIN_QNAME, TNAEFACTUREMAIN.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TNAEFACTUREMAINResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://temenos.com/TWSEFACTURE", name = "TNAEFACTUREMAINResponse")
    public JAXBElement<TNAEFACTUREMAINResponse> createTNAEFACTUREMAINResponse(TNAEFACTUREMAINResponse value) {
        return new JAXBElement<TNAEFACTUREMAINResponse>(_TNAEFACTUREMAINResponse_QNAME, TNAEFACTUREMAINResponse.class, null, value);
    }

}
