/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.bean;

/**
 *
 * @author Melarga.COULIBALY
 */

public class Property
{

    private String propertyName;
    private Object propertyValue;

    public Property()
    {
    }

    public Property(String propertyName, Object propertyValue)
    {
        setPropertyName(propertyName);
        setPropertyValue(propertyValue);
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }

    public Object getPropertyValue()
    {
        return propertyValue;
    }

    public void setPropertyValue(Object propertyValue)
    {
        this.propertyValue = propertyValue;
    }
}
