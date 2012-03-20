/*
 * Copyright (C) 2006-2012 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement
 * along with this program; if not, write to Talend SA
 * 9 rue Pages 92150 Suresnes, France
 */

package com.amalto.core.save;

import com.amalto.core.ejb.ItemPOJO;
import com.amalto.core.ejb.local.XmlServerSLWrapperLocal;
import com.amalto.core.util.Util;
import com.amalto.core.util.XtentisException;

public class DefaultCommitter implements SaverSession.Committer {

    private final XmlServerSLWrapperLocal xmlServerCtrlLocal;

    public DefaultCommitter() {
        try {
            xmlServerCtrlLocal = Util.getXmlServerCtrlLocal();
        } catch (XtentisException e) {
            throw new RuntimeException(e);
        }
    }

    public void begin(String dataCluster) {
        try {
            if (xmlServerCtrlLocal.supportTransaction()) {
                xmlServerCtrlLocal.start(dataCluster);
            }
        } catch (XtentisException e) {
            throw new RuntimeException(e);
        }
    }

    public void commit(String dataCluster) {
        try {
            if (xmlServerCtrlLocal.supportTransaction()) {
                xmlServerCtrlLocal.commit(dataCluster);
            }
        } catch (XtentisException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(ItemPOJO item, String revisionId) {
        try {
            item.store(revisionId, true);
        } catch (XtentisException e) {
            throw new RuntimeException(e);
        }
    }
}
