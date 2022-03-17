/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.bean;


import java.util.Properties;

/**
 *
 * @author yacou.kone
 */
public class ConfigSys {
    private final int temps;
    
    
    public ConfigSys() throws Exception{
        Properties config = new Properties();
        config.load(ConfigSys.class.getClassLoader().getResourceAsStream("org/atware/ressources/configsys.properties"));
        this.temps= Integer.parseInt(config.getProperty("temps"));
    }

    public int getTemps() {
        return temps;
    }
    
    
    
}
