/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.bean;

/**
 *
 * @author Melarga.COULIBALY
 */

import java.beans.*;
import java.io.PrintStream;
import java.lang.reflect.Method;

// Referenced classes of package org.patware.bean:
//            Property

public class BeanInfoFactory
{

    public BeanInfoFactory()
    {
    }

    public static BeanInfo getBeanInfo(Class cls)
    {
        BeanInfo beanInfo = null;
        try
        {
            beanInfo = Introspector.getBeanInfo(cls);
        }
        catch(IntrospectionException ex)
        {
            ex.printStackTrace();
        }
        return beanInfo;
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class type)
    {
        BeanInfo info = getBeanInfo(type);
        return info.getPropertyDescriptors();
    }

    public static PropertyDescriptor getPropertyDescriptor(Class type, String propertyName)
    {
        PropertyDescriptor propertyDescriptors[] = getPropertyDescriptors(type);
        for(int i = 0; i < propertyDescriptors.length; i++)
        {
            PropertyDescriptor pd = propertyDescriptors[i];
            if(propertyName.equals(pd.getName()))
            {
                return pd;
            }
        }

        return null;
    }

    public static Method[] getReadMethods(Class type)
    {
        PropertyDescriptor propertyDescriptors[] = getPropertyDescriptors(type);
        Method methods[] = new Method[propertyDescriptors.length];
        for(int i = 0; i < methods.length; i++)
        {
            methods[i] = propertyDescriptors[i].getReadMethod();
        }

        return methods;
    }

    public static Method[] getWriteMethods(Class type)
    {
        PropertyDescriptor propertyDescriptors[] = getPropertyDescriptors(type);
        Method methods[] = new Method[propertyDescriptors.length];
        for(int i = 0; i < methods.length; i++)
        {
            methods[i] = propertyDescriptors[i].getWriteMethod();
        }

        return methods;
    }

    public static Property[] getPropertiesWithoutClass(Object object)
    {
        Method methods[] = getReadMethods(object.getClass());
        PropertyDescriptor propertyDescriptors[] = getPropertyDescriptors(object.getClass());
        Property result[] = new Property[methods.length - 1];
        int i = 0;
        int j = 0;
        while(i < methods.length) 
        {
            try
            {
                if(!propertyDescriptors[i].getName().equals("class"))
                {
                    result[j] = new Property(propertyDescriptors[i].getName(), methods[i].invoke(object, (Object[])null));
                    j++;
                }
                i++;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Property[] getProperties(Object object)
    {
        Method methods[] = getReadMethods(object.getClass());
        PropertyDescriptor propertyDescriptors[] = getPropertyDescriptors(object.getClass());
        Property result[] = new Property[methods.length];
        for(int i = 0; i < methods.length; i++)
        {
            try
            {
                result[i] = new Property(propertyDescriptors[i].getName(), methods[i].invoke(object, (Object[])null));
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        return result;
    }

    public static Object setObjectPropertyValue(Object object, Property property)
    {
        try
        {
            PropertyDescriptor pd = getPropertyDescriptor(object.getClass(), property.getPropertyName());
            Method method = pd.getWriteMethod();
            if(method != null)
            {
                method.invoke(object, new Object[] {
                    property.getPropertyValue()
                });
            } else
            {
                System.out.println((new StringBuilder()).append(property.getPropertyName()).append(" Setter Method not found in Class!: ").append(object.getClass()).toString());
            }
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder()).append("Property = ").append(property.getPropertyName()).toString());
            System.out.println((new StringBuilder()).append("Property Type= ").append(property.getPropertyValue().getClass().getName()).toString());
            e.printStackTrace();
        }
        return object;
    }
}
