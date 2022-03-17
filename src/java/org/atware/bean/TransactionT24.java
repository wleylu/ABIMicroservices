/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.bean;

import com.atware.log.AppLogger;
import java.io.Serializable;
import java.text.ParseException;

/**
 *
 * @author yacou.kone
 */
public class TransactionT24 implements Serializable{
    @SuppressWarnings("FieldMayBeFinal")
    private String facturier;
    private String datOper ; 
    private String identifiantFacture;
    private String compteDebit;
    private int mntOper; //montant de la facture a débiter + frais
    private int mntFacture;  // montant da la facture sans les frais
    private int mntFrais; // montant des frais
    private int mntMarchand; // montant de la part de la banque
    private int mntFraisMarchand; // frais monta&nt part marchand
    private int mntTimbre; //fris de timbre
    private String compteCom; //compte de commision
    private String refOld;
    private String client ;  

    public TransactionT24() {
        try {
            this.datOper = getDateOper();
            this.refOld = refExterne();
            
        } catch (Exception e) {
        }
    }

  
    public String getFacturier() {
        return facturier;
    }

    public void setFacturier(String facturier) {
        this.facturier = facturier;
    }

    public String getDatOper() throws ParseException {
        
        return datOper;
    }

    public void setDatOper(String datOper) {
                
        this.datOper = datOper;
    }
   

    public String getIdentifiantFacture() {
        return identifiantFacture;
    }

    public void setIdentifiantFacture(String identifiantFacture) {
        this.identifiantFacture = identifiantFacture;
    }

    public String getCompteDebit() {
        return compteDebit;
    }

    public void setCompteDebit(String compteDebit) {
        this.compteDebit = compteDebit;
    }

   

    public int getMntOper() {
        return mntOper;
    }

    public void setMntOper(int mntOper) {
        this.mntOper = mntOper;
    }

    public int getMntFacture() {
        return mntFacture;
    }

    public void setMntFacture(int mntFacture) {
        this.mntFacture = mntFacture;
    }

    public int getMntFrais() {
        return mntFrais;
    }

    public void setMntFrais(int mntFrais) {
        this.mntFrais = mntFrais;
    }

    public int getMntMarchand() {
        return mntMarchand;
    }

    public void setMntMarchand(int mntMarchand) {
        this.mntMarchand = mntMarchand;
    }

    public int getMntFraisMarchand() {
        return mntFraisMarchand;
    }

    public void setMntFraisMarchand(int mntFraisMarchand) {
        this.mntFraisMarchand = mntFraisMarchand;
    }

    public int getMntTimbre() {
        return mntTimbre;
    }

    public void setMntTimbre(int mntTimbre) {
        this.mntTimbre = mntTimbre;
    }

   
    public String getCompteCom() {
        return compteCom;
    }

    public void setCompteCom(String compteCom) {
        this.compteCom = compteCom;
    }

    public String getRefOld() {
        return refOld;
    }

    public void setRefOld(String refOld) {
        this.refOld = refOld;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
    
    

    public TransactionT24(String facturier, String datOper, String identifiantFacture, String compteDebit,
            int mntOper, int mntFacture, int mntFrais, int mntMarchand, int mntFraisMarchand, int mntTimbre, String libelleOper, String compteCom, String refOld) {
        this.facturier = facturier;
        this.datOper = datOper;      
        this.identifiantFacture = identifiantFacture;
        this.compteDebit = compteDebit;
        this.mntOper = mntOper;
        this.mntFacture = mntFacture;
        this.mntFrais = mntFrais;
        this.mntMarchand = mntMarchand;
        this.mntFraisMarchand = mntFraisMarchand;
        this.mntTimbre = mntTimbre;
        this.refOld = refOld;
    }

   
    private String getDateOper() throws ParseException{
        
        return new AppLogger().formatDatoper();
    }
    
    private String refExterne() throws ParseException{
        
        return new AppLogger().getrRfExterne();
    }
    
    

    @Override
    public String toString() {
        return "TransactionT24{" + "prefixe=" + ", facturier=" + facturier + ", datOper=" + datOper + ", identifiantFacture=" + identifiantFacture + ", compteDebit=" + compteDebit + ", compteCredit=" + ", mntOper=" + mntOper + ", mntFacture=" + 
                mntFacture + ", mntFrais=" + mntFrais + ", mntMarchand=" + mntMarchand + ", mntFraisMarchand=" + mntFraisMarchand + ", mntTimbre=" + mntTimbre 
                + ", compteCom=" + compteCom + ", refOld=" + refOld + '}';
    }
    
    
    
    
    
}
