/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.utils;

/**
 *
 * @author Melarga.COULIBALY
 */

import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MD5
{

    public MessageDigest md5;

    public MD5()
    {
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch(NoSuchAlgorithmException ex)
        {
            Logger.getLogger("MD5 Class Logger->").log(Level.SEVERE, null, ex);
        }
    }

    public void update(String key)
    {
        byte uniqueKey[] = key.getBytes();
        md5.update(uniqueKey);
    }

    public String digest()
    {
        byte hash[] = null;
        hash = md5.digest();
        StringBuffer hashString = new StringBuffer();
        for(int i = 0; i < hash.length; i++)
        {
            String hex = Integer.toHexString(hash[i]);
            if(hex.length() == 1)
            {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            } else
            {
                hashString.append(hex.substring(hex.length() - 2));
            }
        }

        return hashString.toString();
    }

    public static void main(String args[])
    {
        MD5 md = new MD5();
        md.update("");
        System.out.println(md.digest());
    }
}
