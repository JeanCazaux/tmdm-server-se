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
package org.talend.mdm.webapp.journal.client.widget;

import org.talend.mdm.webapp.journal.client.i18n.MessagesFactory;
import org.talend.mdm.webapp.journal.client.util.JournalSearchUtil;
import org.talend.mdm.webapp.journal.shared.JournalGridModel;
import org.talend.mdm.webapp.journal.shared.JournalTreeModel;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

/**
 * DOC Administrator  class global comment. Detailled comment
 */
public class JournalHistoryPanel extends ContentPanel {
    
    private JournalDataPanel journalDataPanel;
    
    private JournalComparisonPanel beforePanel;
    
    private JournalComparisonPanel afterPanel;
    
    public JournalHistoryPanel(JournalTreeModel root, JournalGridModel gridModel, boolean isAuth, int width) {
        this.setFrame(false);
        this.setItemId(gridModel.getIds());
        this.setHeaderVisible(false);
        this.setHeading(MessagesFactory.getMessages().data_change_viewer());
        this.setLayout(new BorderLayout());
        
        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 200);
        northData.setCollapsible(false);
        northData.setSplit(true);
        northData.setMargins(new Margins(0, 0, 0, 0));        
        
        journalDataPanel = new JournalDataPanel(root, gridModel);
        this.add(journalDataPanel, northData);        

        BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, width);
        westData.setCollapsible(false);
        westData.setSplit(true);
        westData.setMargins(new Margins(5, 5, 0, 0));
        
        beforePanel = new JournalComparisonPanel(MessagesFactory.getMessages().before_label(),
                JournalSearchUtil.buildParameter(gridModel, "before", isAuth),journalDataPanel.getJournalGridModel(),true); //$NON-NLS-1$
        this.add(beforePanel, westData);
        
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        westData.setCollapsible(false);
        centerData.setMargins(new Margins(5, 0, 0, 0));
        centerData.setSplit(true);
        
        afterPanel = new JournalComparisonPanel(MessagesFactory.getMessages().after_label(),
                JournalSearchUtil.buildParameter(gridModel, "current", isAuth),journalDataPanel.getJournalGridModel(),false); //$NON-NLS-1$
        this.add(afterPanel, centerData);
        
        beforePanel.setOtherPanel(afterPanel);
        afterPanel.setOtherPanel(beforePanel);
    }
 
    public JournalComparisonPanel getBeforePanel() {
        return beforePanel;
    }
  
    public JournalComparisonPanel getAfterPanel() {
        return afterPanel;
    }

    public JournalDataPanel getJournalDataPanel() {
        return journalDataPanel;
    }
}
