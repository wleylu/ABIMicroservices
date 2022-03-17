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
public class RegFacture implements Serializable{
    private String refContrat;
    private String refFacture;
    private String dateReglement;
    private String heureReglement;
    private String montantReglement;
    private String numeroRecu;
    private String typecanal;
    private String codeOperateur;

    public RegFacture() {
    }

    public RegFacture(String refContrat, String refFacture, String dateReglement, String montantReglement, String numeroRecu, String typecanal) {
        this.refContrat = refContrat;
        this.refFacture = refFacture;
        this.dateReglement = dateReglement;
        this.montantReglement = montantReglement;
        this.numeroRecu = numeroRecu;
        this.typecanal = typecanal;
        
    }

    public String getCodeOperateur() {
        return codeOperateur;
    }

    public void setCodeOperateur(String codeOperateur) {
        this.codeOperateur = codeOperateur;
    }

  

    
    
    public String getHeureReglement() {
        return heureReglement;
    }

    public void setHeureReglement(String heureReglement) {
        this.heureReglement = heureReglement;
    }

    
    
    public String getRefContrat() {
        return refContrat;
    }

    public void setRefContrat(String refContrat) {
        this.refContrat = refContrat;
    }

    public String getRefFacture() {
        return refFacture;
    }

    public void setRefFacture(String refFacture) {
        this.refFacture = refFacture;
    }

    public String getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(String dateReglement) {
        this.dateReglement = dateReglement;
    }

    public String getMontantReglement() {
        return montantReglement;
    }

    public void setMontantReglement(String montantReglement) {
        this.montantReglement = montantReglement;
    }

    public String getNumeroRecu() {
        return numeroRecu;
    }

    public void setNumeroRecu(String numeroRecu) {
        this.numeroRecu = numeroRecu;
    }

    public String getTypecanal() {
        return typecanal;
    }

    public void setTypecanal(String typecanal) {
        this.typecanal = typecanal;
    }

    
    
    
    
}
