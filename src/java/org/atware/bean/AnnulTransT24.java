/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.bean;

/**
 *
 * @author yacou.kone
 */
public class AnnulTransT24 {
    private String facturier;
    private String dateOper;
    private String refExterne;
    private String refFTSib;

    public AnnulTransT24() {
    }

    public AnnulTransT24(String facturier, String dateOper, String refExterne, String refFTSib) {
        this.facturier = facturier;
        this.dateOper = dateOper;
        this.refExterne = refExterne;
        this.refFTSib = refFTSib;
    }

    @Override
    public String toString() {
        return "AnnulTransT24{" + "facturier=" + facturier + ", dateOper=" + dateOper + ", refExterne=" + refExterne + ", refFTSib=" + refFTSib + '}';
    }
    
    

    public String getFacturier() {
        return facturier;
    }

    public void setFacturier(String facturier) {
        this.facturier = facturier;
    }

    public String getDateOper() {
        return dateOper;
    }

    public void setDateOper(String dateOper) {
        this.dateOper = dateOper;
    }

    public String getRefExterne() {
        return refExterne;
    }

    public void setRefExterne(String refExterne) {
        this.refExterne = refExterne;
    }

    public String getRefFTSib() {
        return refFTSib;
    }

    public void setRefFTSib(String refFTSib) {
        this.refFTSib = refFTSib;
    }
    
    
    
}
