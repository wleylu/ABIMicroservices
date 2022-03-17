
package com.temenos.tnaefacturemain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour TNAEFACTUREMAINType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="TNAEFACTUREMAINType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="gTNAEFACTUREMAINDetailType" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="mTNAEFACTUREMAINDetailType" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="REQUETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="REPONSEEFACTURE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                           &lt;attribute name="m" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="g" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TNAEFACTUREMAINType", propOrder = {
    "gtnaefacturemainDetailType"
})
public class TNAEFACTUREMAINType {

    @XmlElement(name = "gTNAEFACTUREMAINDetailType")
    protected TNAEFACTUREMAINType.GTNAEFACTUREMAINDetailType gtnaefacturemainDetailType;

    /**
     * Obtient la valeur de la propriété gtnaefacturemainDetailType.
     * 
     * @return
     *     possible object is
     *     {@link TNAEFACTUREMAINType.GTNAEFACTUREMAINDetailType }
     *     
     */
    public TNAEFACTUREMAINType.GTNAEFACTUREMAINDetailType getGTNAEFACTUREMAINDetailType() {
        return gtnaefacturemainDetailType;
    }

    /**
     * Définit la valeur de la propriété gtnaefacturemainDetailType.
     * 
     * @param value
     *     allowed object is
     *     {@link TNAEFACTUREMAINType.GTNAEFACTUREMAINDetailType }
     *     
     */
    public void setGTNAEFACTUREMAINDetailType(TNAEFACTUREMAINType.GTNAEFACTUREMAINDetailType value) {
        this.gtnaefacturemainDetailType = value;
    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * 
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="mTNAEFACTUREMAINDetailType" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="REQUETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="REPONSEEFACTURE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *                 &lt;attribute name="m" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="g" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "mtnaefacturemainDetailType"
    })
    public static class GTNAEFACTUREMAINDetailType {

        @XmlElement(name = "mTNAEFACTUREMAINDetailType")
        protected List<TNAEFACTUREMAINType.GTNAEFACTUREMAINDetailType.MTNAEFACTUREMAINDetailType> mtnaefacturemainDetailType;
        @XmlAttribute(name = "g")
        @XmlSchemaType(name = "positiveInteger")
        protected BigInteger g;

        /**
         * Gets the value of the mtnaefacturemainDetailType property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the mtnaefacturemainDetailType property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMTNAEFACTUREMAINDetailType().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TNAEFACTUREMAINType.GTNAEFACTUREMAINDetailType.MTNAEFACTUREMAINDetailType }
         * 
         * 
         */
        public List<TNAEFACTUREMAINType.GTNAEFACTUREMAINDetailType.MTNAEFACTUREMAINDetailType> getMTNAEFACTUREMAINDetailType() {
            if (mtnaefacturemainDetailType == null) {
                mtnaefacturemainDetailType = new ArrayList<TNAEFACTUREMAINType.GTNAEFACTUREMAINDetailType.MTNAEFACTUREMAINDetailType>();
            }
            return this.mtnaefacturemainDetailType;
        }

        /**
         * Obtient la valeur de la propriété g.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getG() {
            return g;
        }

        /**
         * Définit la valeur de la propriété g.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setG(BigInteger value) {
            this.g = value;
        }


        /**
         * <p>Classe Java pour anonymous complex type.
         * 
         * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="REQUETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="REPONSEEFACTURE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *       &lt;/sequence&gt;
         *       &lt;attribute name="m" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "requete",
            "reponseefacture"
        })
        public static class MTNAEFACTUREMAINDetailType {

            @XmlElement(name = "REQUETE")
            protected String requete;
            @XmlElement(name = "REPONSEEFACTURE")
            protected String reponseefacture;
            @XmlAttribute(name = "m")
            @XmlSchemaType(name = "positiveInteger")
            protected BigInteger m;

            /**
             * Obtient la valeur de la propriété requete.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getREQUETE() {
                return requete;
            }

            /**
             * Définit la valeur de la propriété requete.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setREQUETE(String value) {
                this.requete = value;
            }

            /**
             * Obtient la valeur de la propriété reponseefacture.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getREPONSEEFACTURE() {
                return reponseefacture;
            }

            /**
             * Définit la valeur de la propriété reponseefacture.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setREPONSEEFACTURE(String value) {
                this.reponseefacture = value;
            }

            /**
             * Obtient la valeur de la propriété m.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getM() {
                return m;
            }

            /**
             * Définit la valeur de la propriété m.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setM(BigInteger value) {
                this.m = value;
            }

        }

    }

}
