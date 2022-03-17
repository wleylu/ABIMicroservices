/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atware.bean;

/**
 *
 * @author melarga.coulibaly
 */

/**
 *
 * @author melarga.coulibaly
 */
public final class SMSMessage {
    private String filiale;
    private String idmsg;
    private String from;
    private String to;
    private String message;

    public SMSMessage() {
    }

    public SMSMessage(String filiale, String idmsg, String from, String to, String message) {
        this.filiale = filiale;
        this.idmsg = idmsg;
        this.from = from;
        this.setTo(to);
        this.message = message;
    }

    public String getFiliale() {
        return filiale;
    }

    public void setFiliale(String filiale) {
        this.filiale = filiale;
    }

    public String getIdmsg() {
        return idmsg;
    }

    public void setIdmsg(String idmsg) {
        this.idmsg = idmsg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        
        return to;
    }

    public void setTo(String to) {
        String realnumber=to;
        if (to.startsWith("+")) realnumber=to.substring(1);
        if (to.startsWith("00")) realnumber=to.substring(2);
        if (to.length() == 8 && this.filiale.equalsIgnoreCase("BACI")) realnumber="225"+to;
        
        this.to = realnumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
}
