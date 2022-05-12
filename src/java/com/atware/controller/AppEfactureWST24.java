/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.controller;

import com.atware.utils.ServiceResultatJson;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import org.atware.bean.AnnulTransT24;
import org.atware.bean.Client;
import org.atware.bean.RefAnnulation;
import org.atware.bean.TransactionT24;
import org.atware.jdbc.ConnexionEfacture;

/**
 *
 * @author yacou.kone
 */
public class AppEfactureWST24 {
    private InterfaceEfactureWS serviceT24;
    private ServiceResultatJson traitementJson;
    
    public AppEfactureWST24() throws ParseException{
        serviceT24 = new InterfaceEfactureWS();
        traitementJson = new ServiceResultatJson();
    }
    
    // methose de signaletique
    public String getInfoClient(String clt) throws ParseException{
        String infoClient = serviceT24.getSignaletique(clt);        
       return traitementJson.getClientInfos(infoClient);
    }
    
    //methose de comptabilisation transaction
    public String setComptaTransaction(TransactionT24 paiement) throws ParseException{
        String opers = serviceT24.setCompta(paiement);
        return opers;
        //return traitementJson.getRefTransaction(opers);
    }
    
    //methose de comptabilisation transaction
    public String setAnnulationTransact(RefAnnulation refFt) throws ParseException, IOException{
        AnnulTransT24 opers = opersAnnul(refFt);
        return serviceT24.setAnnulT24(opers);
    }
    
    
    
      //conversion de la trame d'annulation de transaction
    private AnnulTransT24 opersAnnul(RefAnnulation refFT){
        ConnexionEfacture con = new ConnexionEfacture(refFT.getFacturier());
        AnnulTransT24 operT24 = new AnnulTransT24();
        Connection connect= null;
        PreparedStatement ps; ResultSet rs;
        String sql = String.format("SELECT date_regle,facturier, reference, referenceft FROM consultation where referenceft =?");
        try {
            connect = con.maConnexion();
            ps = connect.prepareStatement(sql);
            ps.setString(1, refFT.getRefFT());
            rs = ps.executeQuery();
            
            if (rs.next()){
                operT24.setDateOper(rs.getString("date_regle"));
                operT24.setFacturier(rs.getString("facturier"));
                operT24.setRefExterne(rs.getString("reference"));
                operT24.setRefFTSib(rs.getString("referenceft"));
            }
          
            connect.close();
            
        } catch (Exception e) {
             e.printStackTrace();
        }
                
        return operT24;
    }
    
    
    
}
