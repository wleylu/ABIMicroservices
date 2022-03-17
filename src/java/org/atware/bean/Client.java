/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yacou.kone
 */
public class Client implements Serializable{
    private String client;
    private String sexe;
    private String nomPrenom;
    private String nom;
    private String prenom;
    private String dateNais;
    private String agec;
    private String tel;
    private String pieceId;
    private String dateExpir;
    private String dateDeLivr;
    private String email;
    private List<Comptes> comptes;
    

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNais() {
        return dateNais;
    }

    public void setDateNais(String dateNais) {
        this.dateNais = dateNais;
    }

    public String getAgec() {
        return agec;
    }

    public void setAgec(String agec) {
        this.agec = agec;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPieceId() {
        return pieceId;
    }

    public void setPieceId(String pieceId) {
        this.pieceId = pieceId;
    }

    public String getDateExpir() {
        return dateExpir;
    }

    public void setDateExpir(String dateExpir) {
        this.dateExpir = dateExpir;
    }

    public String getDateDeLivr() {
        return dateDeLivr;
    }

    public void setDateDeLivr(String dateDeLivr) {
        this.dateDeLivr = dateDeLivr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Comptes> getComptes() {
        return comptes;
    }

    public void setComptes(List<Comptes> comptes) {
        this.comptes = comptes;
    }

    public Client() {
    }
    
    
       
}
