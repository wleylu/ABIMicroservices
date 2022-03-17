/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atware.utils;

/**
 *
 * @author Melarga.COULIBALY
 */

import java.io.File;
import java.io.FileFilter;
import java.util.Enumeration;
import java.util.Hashtable;

public class MyFileFilter
    implements FileFilter
{

    private Hashtable filters;
    private String description;
    private String fullDescription;
    private boolean useExtensionsInDescription;
    public String extension;
    public String content;
    private boolean normal;

    public MyFileFilter()
    {
        filters = null;
        description = null;
        fullDescription = null;
        useExtensionsInDescription = true;
        normal = true;
        filters = new Hashtable();
    }

    public MyFileFilter(String extension)
    {
        this(extension, ((String) (null)));
    }

    public MyFileFilter(String content, boolean normal)
    {
        filters = null;
        description = null;
        fullDescription = null;
        useExtensionsInDescription = true;
        this.normal = true;
        this.normal = normal;
        if(normal)
        {
            extension = content;
            addExtension(extension);
        } else
        {
            this.content = content;
        }
    }

    public MyFileFilter(String extension, String description)
    {
        this();
        this.extension = extension;
        if(extension != null)
        {
            addExtension(extension);
        }
        if(description != null)
        {
         //   setDescription(description);
        }
    }

    public MyFileFilter(String filters[])
    {
        this(filters, ((String) (null)));
    }

    public MyFileFilter(String filters[], String description)
    {
        this();
        for(int i = 0; i < filters.length; i++)
        {
            addExtension(filters[i]);
        }

        if(description != null)
        {
           // setDescription(description);
        }
    }

    public boolean accept(File f)
    {
        if(f != null && f.isDirectory())
        {
            return false;
        }
        if(normal)
        {
            if(f != null)
            {
                String lextension = getExtension(f);
                if(lextension != null && filters.get(getExtension(f)) != null)
                {
                    return true;
                }
            }
        } else
        if(f != null && f.getName().contains(content))
        {
            return true;
        }
        return false;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        normal = false;
        this.content = content;
    }

    public String getExtension(File f)
    {
        if(f != null)
        {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if(i > 0 && i < filename.length() - 1)
            {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    public void addExtension(String extension)
    {
        if(filters == null)
        {
            filters = new Hashtable(5);
        }
        filters.put(extension.toLowerCase(), this);
        fullDescription = null;
    }
/*
    public String getDescription()
    {
        Enumeration extensions;
        if(fullDescription != null)
        {
            //break MISSING_BLOCK_LABEL_196;
        }
        if(description != null && !isExtensionListInDescription())
        {
           // break MISSING_BLOCK_LABEL_188;
        }
        fullDescription = description != null ? (new StringBuilder()).append(description).append(" (").toString() : "(";
        extensions = filters.keys();
        if(extensions == null) goto _L2; else goto _L1
_L1:
        new StringBuilder();
        this;
        JVM INSTR dup_x1 ;
        fullDescription;
        append();
        ".";
        append();
        (String)extensions.nextElement();
        append();
        toString();
        fullDescription;
_L4:
        if(!extensions.hasMoreElements()) goto _L2; else goto _L3
_L3:
        new StringBuilder();
        this;
        JVM INSTR dup_x1 ;
        fullDescription;
        append();
        ", .";
        append();
        (String)extensions.nextElement();
        append();
        toString();
        fullDescription;
          goto _L4
_L2:
        new StringBuilder();
        this;
        JVM INSTR dup_x1 ;
        fullDescription;
        append();
        ")";
        append();
        toString();
        fullDescription;
        break MISSING_BLOCK_LABEL_196;
        fullDescription = description;
        return fullDescription;
    }

    public void setDescription(String description)
    {
        this.description = description;
        fullDescription = null;
    }

    public void setExtensionListInDescription(boolean b)
    {
        useExtensionsInDescription = b;
        fullDescription = null;
    }

    public boolean isExtensionListInDescription()
    {
        return useExtensionsInDescription;
    }
*/

}
