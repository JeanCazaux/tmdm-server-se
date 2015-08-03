// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.webapp.browserecords.client.mvc;

import java.util.List;

import org.talend.mdm.webapp.base.client.SessionAwareAsyncCallback;
import org.talend.mdm.webapp.base.client.util.MultilanguageMessageParser;
import org.talend.mdm.webapp.base.client.util.XmlUtil;
import org.talend.mdm.webapp.base.client.widget.CallbackAction;
import org.talend.mdm.webapp.base.shared.EntityModel;
import org.talend.mdm.webapp.base.shared.TypeModel;
import org.talend.mdm.webapp.browserecords.client.BrowseRecords;
import org.talend.mdm.webapp.browserecords.client.BrowseRecordsEvents;
import org.talend.mdm.webapp.browserecords.client.BrowseRecordsServiceAsync;
import org.talend.mdm.webapp.browserecords.client.handler.ItemTreeHandler;
import org.talend.mdm.webapp.browserecords.client.handler.ItemTreeHandlingStatus;
import org.talend.mdm.webapp.browserecords.client.i18n.MessagesFactory;
import org.talend.mdm.webapp.browserecords.client.model.ForeignKeyModel;
import org.talend.mdm.webapp.browserecords.client.model.ItemBean;
import org.talend.mdm.webapp.browserecords.client.model.ItemNodeModel;
import org.talend.mdm.webapp.browserecords.client.model.ItemResult;
import org.talend.mdm.webapp.browserecords.client.util.Locale;
import org.talend.mdm.webapp.browserecords.client.util.UserSession;
import org.talend.mdm.webapp.browserecords.client.widget.ItemDetailToolBar;
import org.talend.mdm.webapp.browserecords.client.widget.ItemsDetailPanel;
import org.talend.mdm.webapp.browserecords.client.widget.ItemsListPanel;
import org.talend.mdm.webapp.browserecords.client.widget.ItemsMainTabPanel;
import org.talend.mdm.webapp.browserecords.shared.ViewBean;
import org.talend.mdm.webapp.browserecords.shared.VisibleRuleResult;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.widget.MessageBox;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class BrowseRecordsController extends Controller {

    private BrowseRecordsView view;

    private BrowseRecordsServiceAsync service;

    public BrowseRecordsController() {
        registerEventTypes(BrowseRecordsEvents.InitFrame);
        registerEventTypes(BrowseRecordsEvents.InitSearchContainer);
        registerEventTypes(BrowseRecordsEvents.SearchView);
        registerEventTypes(BrowseRecordsEvents.GetView);
        registerEventTypes(BrowseRecordsEvents.ViewItem);
        registerEventTypes(BrowseRecordsEvents.CreateForeignKeyView);
        registerEventTypes(BrowseRecordsEvents.SelectForeignKeyView);
        registerEventTypes(BrowseRecordsEvents.ViewForeignKey);
        registerEventTypes(BrowseRecordsEvents.SaveItem);
        registerEventTypes(BrowseRecordsEvents.UpdatePolymorphism);
        registerEventTypes(BrowseRecordsEvents.ExecuteVisibleRule);
    }

    @Override
    public void initialize() {
        service = (BrowseRecordsServiceAsync) Registry.get(BrowseRecords.BROWSERECORDS_SERVICE);
        view = new BrowseRecordsView(this);
    }

    @Override
    public void handleEvent(AppEvent event) {
        int eventTypeCode = event.getType().getEventCode();
        switch (eventTypeCode) {
        case BrowseRecordsEvents.GetViewCode:
            onGetView(event);
            break;
        case BrowseRecordsEvents.SearchViewCode:
            onSearchView(event);
            break;
        case BrowseRecordsEvents.InitFrameCode:
            forwardToView(view, event);
            break;
        case BrowseRecordsEvents.InitSearchContainerCode:
            forwardToView(view, event);
            break;
        case BrowseRecordsEvents.CreateForeignKeyViewCode:
            onCreateForeignKeyView(event);
            break;
        case BrowseRecordsEvents.SelectForeignKeyViewCode:
            onSelectForeignKeyView(event);
            break;
        case BrowseRecordsEvents.ViewItemCode:
            onViewItem(event);
            break;
        case BrowseRecordsEvents.ViewForeignKeyCode:
            onViewForeignKey(event);
            break;
        case BrowseRecordsEvents.SaveItemCode:
            onSaveItem(event);
            break;
        case BrowseRecordsEvents.UpdatePolymorphismCode:
            forwardToView(view, event);
            break;
        case BrowseRecordsEvents.ExecuteVisibleRuleCode:
            onExecuteVisibleRule(event);
            break;
        default:
            break;
        }
    }

    private void onSaveItem(AppEvent event) {
        // TODO the following code need to be refactor, it is the demo code
        final ItemNodeModel model = event.getData();
        final ViewBean viewBean = event.getData("viewBean"); //$NON-NLS-1$
        final ItemBean itemBean = event.getData("ItemBean"); //$NON-NLS-1$
        final Boolean isCreate = event.getData("isCreate"); //$NON-NLS-1$
        final Boolean isClose = event.getData("isClose"); //$NON-NLS-1$
        final ItemDetailToolBar detailToolBar = event.getData("itemDetailToolBar"); //$NON-NLS-1$
        final MessageBox progressBar = MessageBox.wait(MessagesFactory.getMessages().save_progress_bar_title(), MessagesFactory
                .getMessages().save_progress_bar_message(), MessagesFactory.getMessages().please_wait());

        service.saveItem(viewBean, itemBean.getIds(),
                (new ItemTreeHandler(model, viewBean, ItemTreeHandlingStatus.ToSave)).serializeItem(), isCreate,
                Locale.getLanguage(), new SessionAwareAsyncCallback<ItemResult>() {

                    @Override
                    protected void doOnFailure(Throwable caught) {
                        progressBar.close();
                        String err = caught.getMessage();
                        if (err != null) {
                            MessageBox
                                    .alert(MessagesFactory.getMessages().error_title(), XmlUtil.transformXmlToString(err), null)
                                    .setIcon(MessageBox.ERROR);
                        } else {
                            super.doOnFailure(caught);
                        }
                    }

                    @Override
                    public void onSuccess(ItemResult result) {
                        itemBean.setLastUpdateTime(result);
                        progressBar.close();
                        MessageBox msgBox = null;
                        if (result.getStatus() == ItemResult.FAILURE) {
                            MessageBox
                                    .alert(MessagesFactory.getMessages().error_title(),
                                            "".equals(MultilanguageMessageParser.pickOutISOMessage(result.getDescription())) ? MessagesFactory.getMessages().output_report_null() : MultilanguageMessageParser.pickOutISOMessage(result.getDescription()), null).setIcon( //$NON-NLS-1$
                                            MessageBox.ERROR);
                            return;
                        }
                        if (result.getDescription() != "") { //$NON-NLS-1$
                            msgBox = MessageBox.info(MessagesFactory.getMessages().info_title(),
                                    MultilanguageMessageParser.pickOutISOMessage(result.getDescription()), null);
                        } else {
                            msgBox = MessageBox.info(MessagesFactory.getMessages().info_title(), MessagesFactory.getMessages()
                                    .save_success(), null);
                        }
                        setTimeout(msgBox, 1000);

                        if (!detailToolBar.isOutMost() && (isClose || isCreate)) {
                            if (!ItemsListPanel.getInstance().isSaveCurrentChangeBeforeSwitching()
                                    && (isClose || !detailToolBar.isFkToolBar())) {
                                ItemsMainTabPanel.getInstance().remove(ItemsMainTabPanel.getInstance().getSelectedItem());
                            }
                        }
                        if (detailToolBar.isOutMost()) {
                            detailToolBar.refreshNodeStatus();
                        }
                        if (isClose) {
                            if (detailToolBar.isOutMost()) {
                                detailToolBar.closeOutTabPanel();
                            }
                        } else {
                            if (detailToolBar.isOutMost()) {
                                TypeModel typeModel = viewBean.getBindingEntityModel().getMetaDataTypes()
                                        .get(itemBean.getConcept());
                                String tabText = typeModel.getLabel(Locale.getLanguage()) + " " + result.getReturnValue(); //$NON-NLS-1$
                                detailToolBar.updateOutTabPanel(tabText);
                            }
                        }
                        // TMDM-3349 button 'save and close' function
                        if (!detailToolBar.isOutMost() && !detailToolBar.isHierarchyCall() && !detailToolBar.isFkToolBar()) {
                            ItemsListPanel.getInstance().setDefaultSelectionModel(!isClose);
                        }

                        // ItemsListPanel need to refresh when only fkToolBar = false and isOutMost = false and
                        // isHierarchyCall = false
                        if (!detailToolBar.isOutMost() && !detailToolBar.isFkToolBar() && !detailToolBar.isHierarchyCall()) {
                            itemBean.setIds(result.getReturnValue());
                            ItemsListPanel.getInstance().refreshGrid(itemBean);
                        }
                        // TMDM-4814, TMDM-4815 (reload data to refresh ui)
                        if ((detailToolBar.isFkToolBar() || detailToolBar.isOutMost()) && !isClose) {
                            detailToolBar.refresh(result.getReturnValue());
                        }
                        // Only Hierarchy call the next method
                        // TMDM-4112 : JavaScript Error on IE8
                        if (detailToolBar.isHierarchyCall()) {
                            CallbackAction.getInstance().doAction(CallbackAction.HIERARCHY_SAVEITEM_CALLBACK,
                                    viewBean.getBindingEntityModel().getConceptName(), result.getReturnValue(), isClose);
                        }
                    }
                });
    }

    private native void setTimeout(MessageBox msgBox, int millisecond)/*-{
		$wnd.setTimeout(function() {
			msgBox.@com.extjs.gxt.ui.client.widget.MessageBox::close()();
		}, millisecond);
    }-*/;

    private void onViewForeignKey(final AppEvent event) {

        String concept = event.getData("concept"); //$NON-NLS-1$
        String ids = event.getData("ids"); //$NON-NLS-1$
        final ItemsDetailPanel detailPanel = event.getData(BrowseRecordsView.ITEMS_DETAIL_PANEL);
        service.getForeignKeyModel(concept, ids, Locale.getLanguage(), new SessionAwareAsyncCallback<ForeignKeyModel>() {

            @Override
            public void onSuccess(ForeignKeyModel fkModel) {
                AppEvent ae = new AppEvent(event.getType(), fkModel);
                ae.setData(BrowseRecordsView.ITEMS_DETAIL_PANEL, detailPanel);
                forwardToView(view, ae);
            };
        });

    }

    private void onSelectForeignKeyView(final AppEvent event) {
        String concept = event.getData().toString();
        service.getEntityModel(concept, Locale.getLanguage(), new SessionAwareAsyncCallback<EntityModel>() {

            @Override
            public void onSuccess(EntityModel entityModel) {
                AppEvent ae = new AppEvent(event.getType(), entityModel);
                ae.setSource(event.getSource());
                ae.setData("detailPanel", event.getData("detailPanel")); //$NON-NLS-1$//$NON-NLS-2$
                forwardToView(view, ae);
            }

        });

    }

    private void onViewItem(final AppEvent event) {
        ItemBean item = (ItemBean) event.getData();
        if (item != null) {
            UserSession userSession = BrowseRecords.getSession();
            EntityModel entityModel = (EntityModel) userSession.get(UserSession.CURRENT_ENTITY_MODEL);
            ViewBean viewbean = (ViewBean) userSession.get(UserSession.CURRENT_VIEW);
            service.getItem(item, viewbean.getViewPK(), entityModel, Locale.getLanguage(),
                    new SessionAwareAsyncCallback<ItemBean>() {

                        @Override
                        public void onSuccess(ItemBean result) {
                            AppEvent ae = new AppEvent(event.getType(), result);
                            String itemsFormTarget = event.getData(BrowseRecordsView.ITEMS_FORM_TARGET);
                            if (itemsFormTarget != null) {
                                ae.setData(BrowseRecordsView.ITEMS_FORM_TARGET, itemsFormTarget);
                            }
                            forwardToView(view, ae);
                        }
                    });
        }
    }

    private void onCreateForeignKeyView(final AppEvent event) {
        String viewFkName = "Browse_items_" + event.getData().toString(); //$NON-NLS-1$
        final ItemsDetailPanel detailPanel = event.getData(BrowseRecordsView.ITEMS_DETAIL_PANEL);
        service.getView(viewFkName, Locale.getLanguage(), new SessionAwareAsyncCallback<ViewBean>() {

            @Override
            public void onSuccess(ViewBean viewBean) {
                // forward
                AppEvent ae = new AppEvent(event.getType(), viewBean);
                ae.setData(BrowseRecordsView.ITEMS_DETAIL_PANEL, detailPanel);
                forwardToView(view, ae);
            }
        });

    }

    protected void onGetView(final AppEvent event) {
        Log.info("Get view... ");//$NON-NLS-1$
        String viewName = event.getData();
        service.getView(viewName, Locale.getLanguage(), new SessionAwareAsyncCallback<ViewBean>() {

            @Override
            public void onSuccess(ViewBean viewbean) {

                // Init CURRENT_VIEW
                BrowseRecords.getSession().put(UserSession.CURRENT_VIEW, viewbean);

                // Init CURRENT_ENTITY_MODEL
                BrowseRecords.getSession().put(UserSession.CURRENT_ENTITY_MODEL, viewbean.getBindingEntityModel());

                // reset CURRENT_LINEAGE_ENTITY_LIST
                BrowseRecords.getSession().put(UserSession.CURRENT_LINEAGE_ENTITY_LIST, null);

                // reset CURRENT_RUNNABLE_PROCESS_LIST
                BrowseRecords.getSession().put(UserSession.CURRENT_RUNNABLE_PROCESS_LIST, null);

                // forward
                AppEvent ae = new AppEvent(event.getType(), viewbean);
                forwardToView(view, ae);
            }
        });
    }

    protected void onSearchView(final AppEvent event) {
        Log.info("Do view-search... ");//$NON-NLS-1$
        ViewBean viewBean = BrowseRecords.getSession().getCurrentView();
        AppEvent ae = new AppEvent(event.getType(), viewBean);
        forwardToView(view, ae);
    }

    private void onExecuteVisibleRule(final AppEvent event) {
        final ItemNodeModel model = event.getData();
        final ViewBean viewBean = event.getData("viewBean"); //$NON-NLS-1$
        final ItemsDetailPanel itemsDetailPanel = event.getData(BrowseRecordsView.ITEMS_DETAIL_PANEL);
        if (model != null) {
            EntityModel entityModel = (EntityModel) BrowseRecords.getSession().get(UserSession.CURRENT_ENTITY_MODEL);
            entityModel.getMetaDataTypes();

            service.executeVisibleRule(viewBean,
                    (new ItemTreeHandler(model, viewBean, ItemTreeHandlingStatus.InUse)).serializeItem(),
                    new SessionAwareAsyncCallback<List<VisibleRuleResult>>() {

                        @Override
                        public void onSuccess(List<VisibleRuleResult> arg0) {
                            AppEvent app = new AppEvent(BrowseRecordsEvents.ExecuteVisibleRule);
                            app.setData(arg0);
                            app.setData("viewBean", viewBean); //$NON-NLS-1$
                            app.setData(BrowseRecordsView.ITEMS_DETAIL_PANEL, itemsDetailPanel);
                            forwardToView(view, app);
                        }
                    });
        }
    }
}
