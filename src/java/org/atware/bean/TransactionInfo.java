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
public class TransactionInfo implements Serializable {
    
        private String facturier;
        private String caisse;
        private String dhtransact;
        private String codoper;
        private String refrel;
        private String compteDebit;
        private String compteCredit;
        private String montantDebit;
        private String montantCredit;
        private String montantCom1;
        private String montantCom2;
        private String montantTaxe;
        private String narratif;
        private String libelleRefrel;
        private String cnxKey;
        private String refOper;

            public String getFacturier() {
                return facturier;
            }

            public void setFacturier(String facturier) {
                this.facturier = facturier;
            }

            public String getCaisse() {
                return caisse;
            }

            public void setCaisse(String caisse) {
                this.caisse = caisse;
            }

            public String getDhtransact() {
                return dhtransact;
            }

            public void setDhtransact(String dhtransact) {
                this.dhtransact = dhtransact;
            }

            public String getCodoper() {
                return codoper;
            }

            public void setCodoper(String codoper) {
                this.codoper = codoper;
            }

            public String getRefrel() {
                return refrel;
            }

            public void setRefrel(String refrel) {
                this.refrel = refrel;
            }

            public String getCompteDebit() {
                return compteDebit;
            }

            public void setCompteDebit(String compteDebit) {
                this.compteDebit = compteDebit;
            }

            public String getCompteCredit() {
                return compteCredit;
            }

            public void setCompteCredit(String compteCredit) {
                this.compteCredit = compteCredit;
            }

            public String getMontantDebit() {
                return montantDebit;
            }

            public void setMontantDebit(String montantDebit) {
                this.montantDebit = montantDebit;
            }

            public String getMontantCredit() {
                return montantCredit;
            }

            public void setMontantCredit(String montantCredit) {
                this.montantCredit = montantCredit;
            }

            public String getMontantCom1() {
                return montantCom1;
            }

            public void setMontantCom1(String montantCom1) {
                this.montantCom1 = montantCom1;
            }

            public String getMontantCom2() {
                return montantCom2;
            }

            public void setMontantCom2(String montantCom2) {
                this.montantCom2 = montantCom2;
            }

            public String getMontantTaxe() {
                return montantTaxe;
            }

            public void setMontantTaxe(String montantTaxe) {
                this.montantTaxe = montantTaxe;
            }

            public String getNarratif() {
                return narratif;
            }

            public void setNarratif(String narratif) {
                this.narratif = narratif;
            }                      

            public String getLibelleRefrel() {
                return libelleRefrel;
            }

            public void setLibelleRefrel(String libelleRefrel) {
                this.libelleRefrel = libelleRefrel;
            }

            public String getCnxKey() {
                return cnxKey;
            }

            public void setCnxKey(String cnxKey) {
                this.cnxKey = cnxKey;
            }

            public String getRefOper() {
                return refOper;
            }

            public void setRefOper(String refOper) {
                this.refOper = refOper;
            }

          
            
        
        
}
