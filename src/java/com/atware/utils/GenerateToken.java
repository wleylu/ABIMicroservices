/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.utils;

import java.security.SecureRandom;
import java.util.Base64.Encoder;
import static java.util.Base64.getUrlEncoder;


/**
 *
 * @author yacou.kone
 */
public  class GenerateToken {
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Encoder base64Encoder = getUrlEncoder(); //threadsafe
    
    public GenerateToken(){
        
    }
    public  String generateToken(int tailleBytes){
            byte[] randomBytes = new byte[tailleBytes];
            secureRandom.nextBytes(randomBytes);            
            return base64Encoder.encodeToString(randomBytes);
    }
    
}
