/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.utils;

/**
 *
 * @author Melarga.COULIBALY
 */

import java.util.ResourceBundle;
import javax.swing.ImageIcon;

public class ResLoader
{

    private static Class aClass;
    private static String aMessageBundle;

    public ResLoader()
    {
    }

    public static void init(Class theClass, String messageBundle)
    {
        setClass(theClass);
        setMessageBundle(messageBundle);
    }

    public static void setClass(Class theClass)
    {
        aClass = theClass;
    }

    public static void setMessageBundle(String messageBundle)
    {
        aMessageBundle = messageBundle;
    }

    public static ImageIcon getIcon(String picture)
    {
        java.net.URL url = aClass.getResource(picture);
        if(url != null)
        {
            return new ImageIcon(url);
        } else
        {
            return null;
        }
    }

    public static String getMessages(String message)
    {
        return ResourceBundle.getBundle(aMessageBundle).getString(message);
    }
}

