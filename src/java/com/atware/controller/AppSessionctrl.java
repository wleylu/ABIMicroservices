/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.atware.bean.ConfigInfos;

/**
 *
 * @author Melarga.COULIBALY
 */
public class AppSessionctrl {
    public ConfigInfos config = null;
    
    @SuppressWarnings("CallToPrintStackTrace")
    public AppSessionctrl() {
        try {
            config = new ConfigInfos();
            System.out.println("ConfigInfos() successfully called from init AppController");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }            
    }
        
}
