/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.melware.bean;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Melarga.COULIBALY
 */
public class Signature {
    private String statuscode;
    private Image imgdata;
    
    public Signature(){
    }
    
    public Signature(String imgfullpath){
        try {
            BufferedImage bfimg;
            this.statuscode = "00";
            File image = new File(imgfullpath);
            bfimg = ImageIO.read(image);
            this.imgdata = bfimg;
        } catch (IOException ex) {
            ex.printStackTrace();
            this.statuscode = "99";
        }
    }
    
    public String getStatuscode(){
        return statuscode;
    }
    public void setStatuscode(String statuscode){
        this.statuscode=statuscode;
    }
    
    public Image getImgdata(){
        return imgdata;
    }
    public void setImgdata(Image img){
        this.imgdata=img;
    }
    
}
