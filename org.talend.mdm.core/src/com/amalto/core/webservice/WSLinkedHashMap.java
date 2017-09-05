// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package com.amalto.core.webservice;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="WSLinkedHashMap")
public class WSLinkedHashMap {
    protected com.amalto.core.webservice.WSGetItemsPivotIndexPivotWithKeysTypedContentEntry[] typedContentEntry;
    
    public WSLinkedHashMap() {
    }
    
    public WSLinkedHashMap(com.amalto.core.webservice.WSGetItemsPivotIndexPivotWithKeysTypedContentEntry[] typedContentEntry) {
        this.typedContentEntry = typedContentEntry;
    }
    
    public com.amalto.core.webservice.WSGetItemsPivotIndexPivotWithKeysTypedContentEntry[] getTypedContentEntry() {
        return typedContentEntry;
    }
    
    public void setTypedContentEntry(com.amalto.core.webservice.WSGetItemsPivotIndexPivotWithKeysTypedContentEntry[] typedContentEntry) {
        this.typedContentEntry = typedContentEntry;
    }
}
