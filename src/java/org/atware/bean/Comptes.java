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
public class Comptes implements Serializable{
    private String compte;
    private String agence;
    private String ncg;
    private String libNcg;
    private String coddci;
    private String expl;

    public Comptes() {
    }

    public Comptes(String compte, String agence, String ncg, String libNcg, String coddci, String expl) {
        this.compte = compte;
        this.agence = agence;
        this.ncg = ncg;
        this.libNcg = libNcg;
        this.coddci = coddci;
        this.expl = expl;
    }

    public String getCompte() {
        return compte;
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public String getNcg() {
        return ncg;
    }

    public void setNcg(String ncg) {
        this.ncg = ncg;
    }

    public String getLibNcg() {
        return libNcg;
    }

    public void setLibNcg(String libNcg) {
        this.libNcg = libNcg;
    }

    public String getCoddci() {
        return coddci;
    }

    public void setCoddci(String coddci) {
        this.coddci = coddci;
    }

    public String getExpl() {
        return expl;
    }

    public void setExpl(String expl) {
        this.expl = expl;
    }
    
    
    
}
