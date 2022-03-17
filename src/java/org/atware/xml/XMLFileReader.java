/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.xml;

/**
 *
 * @author Melarga.COULIBALY
 */

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLFileReader
{

    private Document xmlDocument;
    private String xmlFilePath;

    public XMLFileReader(File xmlFile)
    {
        xmlDocument = null;
        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            InputStream inputStream = new FileInputStream(xmlFile);
            docBuilder.setEntityResolver(null);
            xmlDocument = docBuilder.parse(inputStream);
            setXmlDocument(xmlDocument);
            inputStream.close();
        }
        catch(FactoryConfigurationError e)
        {
            e.printStackTrace();
        }
        catch(ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch(SAXException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public XMLFileReader(String xmlFilePath)
    {
        xmlDocument = null;
        setXmlFilePath(xmlFilePath);
        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            InputStream inputStream = getClass().getResourceAsStream(getXmlFilePath());
            xmlDocument = docBuilder.parse(inputStream);
            setXmlDocument(xmlDocument);
            inputStream.close();
        }
        catch(FactoryConfigurationError e)
        {
            e.printStackTrace();
        }
        catch(ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch(SAXException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public String[] getListNodeValue(String xpath)
    {
        String result[] = null;
        try
        {
            NodeList listNode = XPathAPI.selectNodeList(xmlDocument, xpath);

            if(listNode == null)
            {
                 //break MISSING_BLOCK_LABEL_118; 
                return null;
            }
            result = new String[listNode.getLength()];
            for(int i = 0; i < listNode.getLength(); i++)
            {
                Node aNode = listNode.item(i);
                if(aNode != null && aNode.hasChildNodes() && aNode.getFirstChild().getNodeType() == 3)
                {
                    result[i] = aNode.getFirstChild().getNodeValue();
                }
            }
            return result;
        } catch (TransformerException ex) {
            Logger.getLogger(XMLFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DOMException e) {
            e.printStackTrace();
           // break MISSING_BLOCK_LABEL_118; ( a elucider -- 03/12/2011)
            return null;
        }  
       return null;
    }

    public Document getXmlDocument()
    {
        return xmlDocument;
    }

    private void setXmlDocument(Document xmlDocument)
    {
        if(xmlDocument != null)
        {
            this.xmlDocument = xmlDocument;
        }
    }

    public String getXmlFilePath()
    {
        return xmlFilePath;
    }

    public void setXmlFilePath(String xmlFilePath)
    {
        if(xmlFilePath != null)
        {
            this.xmlFilePath = xmlFilePath;
        }
    }
}
