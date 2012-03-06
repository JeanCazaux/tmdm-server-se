// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.webapp.journal.shared;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * DOC Administrator  class global comment. Detailled comment
 */
public class JournalGridModel extends BaseModel implements IsSerializable {

    private static final long serialVersionUID = 1L;
    
    private String ids;
    
    private String dataContainer;
    
    private String dataModel;
    
    private String entity;
    
    private String key;
    
    private String revisionId;
    
    private String operationType;
    
    private String operationTime;
    
    private String source;
    
    private String userName;
    
    public JournalGridModel() {

    }
    
    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        set("ids", ids); //$NON-NLS-1$
        this.ids = ids;
    }
    
    public String getDataContainer() {
        return dataContainer;
    }
    
    public void setDataContainer(String dataContainer) {
        set("dataContainer", dataContainer); //$NON-NLS-1$
        this.dataContainer = dataContainer;
    }
    
    public String getDataModel() {
        return dataModel;
    }
   
    public void setDataModel(String dataModel) {
        set("dataModel",  dataModel); //$NON-NLS-1$
        this.dataModel = dataModel;
    }
    
    public String getEntity() {
        return entity;
    }
    
    public void setEntity(String entity) {
        set("entity",  entity); //$NON-NLS-1$
        this.entity = entity;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        set("key",  key); //$NON-NLS-1$
        this.key = key;
    }
    
    public String getRevisionId() {
        return revisionId;
    }
    
    public void setRevisionId(String revisionId) {
        set("revisionId",  revisionId); //$NON-NLS-1$
        this.revisionId = revisionId;
    }
    
    public String getOperationType() {
        return operationType;
    }
    
    public void setOperationType(String operationType) {
        set("operationType",  operationType); //$NON-NLS-1$
        this.operationType = operationType;
    }
    
    public String getOperationTime() {
        return operationTime;
    }
    
    public void setOperationTime(String operationTime) {
        set("operationTime",  operationTime); //$NON-NLS-1$
        this.operationTime = operationTime;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        set("source",  source); //$NON-NLS-1$
        this.source = source;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        set("userName",  userName); //$NON-NLS-1$
        this.userName = userName;
    }    
}