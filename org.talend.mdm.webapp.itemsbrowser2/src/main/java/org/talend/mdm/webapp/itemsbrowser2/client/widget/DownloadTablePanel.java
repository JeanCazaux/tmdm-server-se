// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.webapp.itemsbrowser2.client.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.talend.mdm.webapp.itemsbrowser2.client.ItemsServiceAsync;
import org.talend.mdm.webapp.itemsbrowser2.client.Itemsbrowser2;
import org.talend.mdm.webapp.itemsbrowser2.client.i18n.MessagesFactory;
import org.talend.mdm.webapp.itemsbrowser2.shared.DownloadBaseModel;
import org.talend.mdm.webapp.itemsbrowser2.shared.DownloadTable;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.state.StateManager;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class DownloadTablePanel extends ContentPanel {

    private ItemsServiceAsync service = (ItemsServiceAsync) Registry.get(Itemsbrowser2.ITEMS_SERVICE);

    private ContentPanel gridContainer;

    private Grid<DownloadBaseModel> grid;

    private String tableName = null;

    private final static int PAGE_SIZE = 20;

    private PagingToolBarEx pagetoolBar = null;

    private DownloadTablePanel(String name) {
        super();
        tableName = name;
        this.setLayout(new FitLayout());
        this.setHeaderVisible(false);
    }

    public static DownloadTablePanel getInstance(String name) {
        return new DownloadTablePanel(name);
    }

    private List<ColumnConfig> initColumns(DownloadTable table, int width) {
        List<ColumnConfig> ccList = new ArrayList<ColumnConfig>();
        int subWidth = (width) / table.getFields().length;
        for (String field : table.getFields()) {
            ColumnConfig cc = new ColumnConfig(field, field, subWidth);        
            ccList.add(cc);
            TextField<String> text = new TextField<String>();
            if (Arrays.asList(table.getKeys()).contains(field)) {
                text.setAllowBlank(false);
                text.setValidator(new Validator() {

                    public String validate(Field<?> field, String value) {
                        if (value == null || value.isEmpty())
                            return "Invalid"; //$NON-NLS-1$
                        return null;
                    }

                });
            }
            cc.setEditor(new CellEditor(text));
        }

        return ccList;
    }

    public void updateGrid(DownloadTable table, int width) {
        List<ColumnConfig> columnConfigList = initColumns(table, width);
        RpcProxy<PagingLoadResult<DownloadBaseModel>> proxy = new RpcProxy<PagingLoadResult<DownloadBaseModel>>() {

            protected void load(Object loadConfig, final AsyncCallback<PagingLoadResult<DownloadBaseModel>> callback) {
                service.getDownloadTableContent(tableName, (PagingLoadConfig) loadConfig,
                        new AsyncCallback<PagingLoadResult<DownloadBaseModel>>() {

                            public void onSuccess(PagingLoadResult<DownloadBaseModel> result) {
                                callback.onSuccess(new BasePagingLoadResult<DownloadBaseModel>(result.getData(), result
                                        .getOffset(), result.getTotalLength()));
                            }

                            public void onFailure(Throwable caught) {
                                MessageBox.alert(MessagesFactory.getMessages().error_title(), caught.getMessage(), null);
                                callback.onSuccess(new BasePagingLoadResult<DownloadBaseModel>(
                                        new ArrayList<DownloadBaseModel>(), 0, 0));
                            }
                        });
            }

        };

        // loader
        final PagingLoader<PagingLoadResult<DownloadBaseModel>> loader = new BasePagingLoader<PagingLoadResult<DownloadBaseModel>>(
                proxy);

        final ListStore<DownloadBaseModel> store = new ListStore<DownloadBaseModel>(loader);
        int usePageSize = PAGE_SIZE;
        if (StateManager.get().get("crossgrid") != null) //$NON-NLS-1$
            usePageSize = Integer.valueOf(((Map) StateManager.get().get("crossgrid")).get("limit").toString()); //$NON-NLS-1$ //$NON-NLS-2$
        pagetoolBar = new PagingToolBarEx(usePageSize);
        pagetoolBar.bind(loader);
        ColumnModel cm = new ColumnModel(columnConfigList);

        grid = new Grid<DownloadBaseModel>(store, cm);
        grid.setId("UpdateTableGrid"); //$NON-NLS-1$
        grid.addListener(Events.Attach, new Listener<GridEvent<DownloadBaseModel>>() {

            public void handleEvent(GridEvent<DownloadBaseModel> be) {
                PagingLoadConfig config = new BasePagingLoadConfig();
                config.setOffset(0);
                int pageSize = (Integer) pagetoolBar.getPageSize();
                config.setLimit(pageSize);
                loader.load(config);
            }
        });
        grid.setLoadMask(true);
        grid.setStateId("crossgrid");//$NON-NLS-1$
        gridContainer = new ContentPanel(new FitLayout());
        gridContainer.setBodyBorder(false);
        gridContainer.setHeaderVisible(false);
        gridContainer.setBottomComponent(pagetoolBar);
        gridContainer.add(grid);
        add(gridContainer);

        ToolBar toolBar = new ToolBar();
        Button export = new Button("Export"); //$NON-NLS-1$
        toolBar.add(export);
        export.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Window.open("/itemsbrowser2/download?tableName=" + tableName, "_parent", "location=no"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        });
        gridContainer.setTopComponent(toolBar);
        this.syncSize();
    }
}
