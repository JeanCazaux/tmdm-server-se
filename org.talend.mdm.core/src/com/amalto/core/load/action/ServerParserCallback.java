/*
 * Copyright (C) 2006-2011 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement
 * along with this program; if not, write to Talend SA
 * 9 rue Pages 92150 Suresnes, France
 */

package com.amalto.core.load.action;

import com.amalto.core.ejb.local.XmlServerSLWrapperLocal;
import com.amalto.core.load.LoadParserCallback;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
*
*/
class ServerParserCallback implements LoadParserCallback {
	private int currentCount;
	private final XmlServerSLWrapperLocal server;
	private final String dataClusterName;

	private static final Logger log = Logger.getLogger(ServerParserCallback.class);

	public ServerParserCallback(XmlServerSLWrapperLocal server,
			String dataClusterName) {
		this.server = server;
		this.dataClusterName = dataClusterName;
		currentCount = 0;
	}

	public void flushDocument(XMLReader docReader, InputSource input) {
		try {
			server.putDocumentFromSAX(dataClusterName, docReader, input, null);
			currentCount++;

			if (currentCount % 1000 == 0) {
				if (log.isDebugEnabled()) {
					log.debug("Loaded documents: " + (currentCount / 1000) + "K."); //$NON-NLS-1$
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
