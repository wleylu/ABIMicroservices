
package com.temenos.twsefacture;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.temenos.tnaefacturemain.TNAEFACTUREMAINType;


/**
 * <p>Classe Java pour TNAEFACTUREMAINResponse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="TNAEFACTUREMAINResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Status" type="{http://temenos.com/TWSEFACTURE}Status" minOccurs="0"/&gt;
 *         &lt;element name="TNAEFACTUREMAINType" type="{http://temenos.com/TNAEFACTUREMAIN}TNAEFACTUREMAINType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TNAEFACTUREMAINResponse", propOrder = {
    "status",
    "tnaefacturemainType"
})
public class TNAEFACTUREMAINResponse {

    @XmlElement(name = "Status")
    protected Status status;
    @XmlElement(name = "TNAEFACTUREMAINType")
    protected List<TNAEFACTUREMAINType> tnaefacturemainType;

    /**
     * Obtient la valeur de la propriété status.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Définit la valeur de la propriété status.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }

    /**
     * Gets the value of the tnaefacturemainType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tnaefacturemainType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTNAEFACTUREMAINType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TNAEFACTUREMAINType }
     * 
     * 
     */
    public List<TNAEFACTUREMAINType> getTNAEFACTUREMAINType() {
        if (tnaefacturemainType == null) {
            tnaefacturemainType = new ArrayList<TNAEFACTUREMAINType>();
        }
        return this.tnaefacturemainType;
    }

}
