// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation 
// Generated source version: 1.1.2

package com.amalto.webapp.util.webservices;


public class WSProcessBytesUsingTransformer {
    protected com.amalto.webapp.util.webservices.WSByteArray wsBytes;
    protected java.lang.String contentType;
    protected com.amalto.webapp.util.webservices.WSTransformerPK wsTransformerPK;
    protected com.amalto.webapp.util.webservices.WSOutputDecisionTable wsOutputDecisionTable;
    
    public WSProcessBytesUsingTransformer() {
    }
    
    public WSProcessBytesUsingTransformer(com.amalto.webapp.util.webservices.WSByteArray wsBytes, java.lang.String contentType, com.amalto.webapp.util.webservices.WSTransformerPK wsTransformerPK, com.amalto.webapp.util.webservices.WSOutputDecisionTable wsOutputDecisionTable) {
        this.wsBytes = wsBytes;
        this.contentType = contentType;
        this.wsTransformerPK = wsTransformerPK;
        this.wsOutputDecisionTable = wsOutputDecisionTable;
    }
    
    public com.amalto.webapp.util.webservices.WSByteArray getWsBytes() {
        return wsBytes;
    }
    
    public void setWsBytes(com.amalto.webapp.util.webservices.WSByteArray wsBytes) {
        this.wsBytes = wsBytes;
    }
    
    public java.lang.String getContentType() {
        return contentType;
    }
    
    public void setContentType(java.lang.String contentType) {
        this.contentType = contentType;
    }
    
    public com.amalto.webapp.util.webservices.WSTransformerPK getWsTransformerPK() {
        return wsTransformerPK;
    }
    
    public void setWsTransformerPK(com.amalto.webapp.util.webservices.WSTransformerPK wsTransformerPK) {
        this.wsTransformerPK = wsTransformerPK;
    }
    
    public com.amalto.webapp.util.webservices.WSOutputDecisionTable getWsOutputDecisionTable() {
        return wsOutputDecisionTable;
    }
    
    public void setWsOutputDecisionTable(com.amalto.webapp.util.webservices.WSOutputDecisionTable wsOutputDecisionTable) {
        this.wsOutputDecisionTable = wsOutputDecisionTable;
    }
}
