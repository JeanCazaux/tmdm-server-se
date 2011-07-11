// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.webapp.itemsbrowser2.server;

import java.io.Serializable;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.talend.mdm.commmon.util.core.EDBType;
import org.talend.mdm.commmon.util.core.MDMConfiguration;
import org.talend.mdm.commmon.util.datamodel.management.BusinessConcept;
import org.talend.mdm.commmon.util.datamodel.management.ReusableType;
import org.talend.mdm.commmon.util.webapp.XSystemObjects;
import org.talend.mdm.webapp.itemsbrowser2.client.i18n.MessagesFactory;
import org.talend.mdm.webapp.itemsbrowser2.client.model.DataTypeConstants;
import org.talend.mdm.webapp.itemsbrowser2.client.model.ForeignKeyBean;
import org.talend.mdm.webapp.itemsbrowser2.client.model.ForeignKeyDrawer;
import org.talend.mdm.webapp.itemsbrowser2.client.model.ItemBaseModel;
import org.talend.mdm.webapp.itemsbrowser2.client.model.ItemBasePageLoadResult;
import org.talend.mdm.webapp.itemsbrowser2.client.model.ItemBean;
import org.talend.mdm.webapp.itemsbrowser2.client.model.ItemResult;
import org.talend.mdm.webapp.itemsbrowser2.client.model.QueryModel;
import org.talend.mdm.webapp.itemsbrowser2.client.model.Restriction;
import org.talend.mdm.webapp.itemsbrowser2.client.model.SearchTemplate;
import org.talend.mdm.webapp.itemsbrowser2.client.model.SubTypeBean;
import org.talend.mdm.webapp.itemsbrowser2.server.bizhelpers.DataModelHelper;
import org.talend.mdm.webapp.itemsbrowser2.server.bizhelpers.ItemHelper;
import org.talend.mdm.webapp.itemsbrowser2.server.bizhelpers.RoleHelper;
import org.talend.mdm.webapp.itemsbrowser2.server.bizhelpers.ViewHelper;
import org.talend.mdm.webapp.itemsbrowser2.server.util.CommonUtil;
import org.talend.mdm.webapp.itemsbrowser2.server.util.XmlUtil;
import org.talend.mdm.webapp.itemsbrowser2.shared.AppHeader;
import org.talend.mdm.webapp.itemsbrowser2.shared.DownloadBaseModel;
import org.talend.mdm.webapp.itemsbrowser2.shared.DownloadTable;
import org.talend.mdm.webapp.itemsbrowser2.shared.EntityModel;
import org.talend.mdm.webapp.itemsbrowser2.shared.TypeModel;
import org.talend.mdm.webapp.itemsbrowser2.shared.ViewBean;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.amalto.core.ejb.ItemPOJO;
import com.amalto.core.ejb.ItemPOJOPK;
import com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK;
import com.amalto.webapp.core.bean.UpdateReportItem;
import com.amalto.webapp.core.dmagent.SchemaWebAgent;
import com.amalto.webapp.core.util.XtentisWebappException;
import com.amalto.webapp.util.webservices.WSBoolean;
import com.amalto.webapp.util.webservices.WSCount;
import com.amalto.webapp.util.webservices.WSCountItemsByCustomFKFilters;
import com.amalto.webapp.util.webservices.WSDataClusterPK;
import com.amalto.webapp.util.webservices.WSDataModelPK;
import com.amalto.webapp.util.webservices.WSDeleteBusinessConcept;
import com.amalto.webapp.util.webservices.WSDeleteItem;
import com.amalto.webapp.util.webservices.WSDeleteItems;
import com.amalto.webapp.util.webservices.WSDropItem;
import com.amalto.webapp.util.webservices.WSDroppedItemPK;
import com.amalto.webapp.util.webservices.WSExistsItem;
import com.amalto.webapp.util.webservices.WSGetBusinessConceptKey;
import com.amalto.webapp.util.webservices.WSGetBusinessConcepts;
import com.amalto.webapp.util.webservices.WSGetDataModel;
import com.amalto.webapp.util.webservices.WSGetItem;
import com.amalto.webapp.util.webservices.WSGetItems;
import com.amalto.webapp.util.webservices.WSGetItemsByCustomFKFilters;
import com.amalto.webapp.util.webservices.WSGetView;
import com.amalto.webapp.util.webservices.WSGetViewPKs;
import com.amalto.webapp.util.webservices.WSItem;
import com.amalto.webapp.util.webservices.WSItemPK;
import com.amalto.webapp.util.webservices.WSPutBusinessConceptSchema;
import com.amalto.webapp.util.webservices.WSPutItem;
import com.amalto.webapp.util.webservices.WSPutItemWithReport;
import com.amalto.webapp.util.webservices.WSRouteItemV2;
import com.amalto.webapp.util.webservices.WSStringArray;
import com.amalto.webapp.util.webservices.WSStringPredicate;
import com.amalto.webapp.util.webservices.WSView;
import com.amalto.webapp.util.webservices.WSViewPK;
import com.amalto.webapp.util.webservices.WSViewSearch;
import com.amalto.webapp.util.webservices.WSWhereAnd;
import com.amalto.webapp.util.webservices.WSWhereCondition;
import com.amalto.webapp.util.webservices.WSWhereItem;
import com.amalto.webapp.util.webservices.WSWhereOperator;
import com.amalto.webapp.util.webservices.WSWhereOr;
import com.amalto.webapp.util.webservices.WSXPathsSearch;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

public class ItemServiceCommonHandler extends ItemsServiceImpl {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(ItemServiceCommonHandler.class);

    private static final Pattern extractIdPattern = Pattern.compile("\\[.*?\\]"); //$NON-NLS-1$

    private Object[] getItemBeans(String dataClusterPK, ViewBean viewBean, EntityModel entityModel, String criteria, int skip,
            int max, String sortDir, String sortCol, String language) {

        int totalSize = 0;
        String dateFormat = "yyyy-MM-dd"; //$NON-NLS-1$ 
        String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss"; //$NON-NLS-1$ 

        List<ItemBean> itemBeans = new ArrayList<ItemBean>();
        String concept = ViewHelper.getConceptFromDefaultViewName(viewBean.getViewPK());
        Map<String, String[]> formatMap = this.checkDisplayFormat(entityModel, language);

        try {
            WSWhereItem wi = CommonUtil.buildWhereItems(criteria);
            String[] results = CommonUtil
                    .getPort()
                    .viewSearch(
                            new WSViewSearch(new WSDataClusterPK(dataClusterPK), new WSViewPK(viewBean.getViewPK()), wi, -1,
                                    skip, max, sortCol, sortDir)).getStrings();

            // TODO change ids to array?
            List<String> idsArray = new ArrayList<String>();
            for (int i = 0; i < results.length; i++) {

                if (i == 0) {
                    try {
                        // Qizx doesn't wrap the count in a XML element, so try to parse it
                        totalSize = Integer.parseInt(results[i]);
                    } catch (NumberFormatException e) {
                        totalSize = Integer.parseInt(com.amalto.webapp.core.util.Util.parse(results[i]).getDocumentElement()
                                .getTextContent());
                    }
                    continue;
                }

                Document doc = parseResultDocument(results[i], "result"); //$NON-NLS-1$

                idsArray.clear();
                for (String key : entityModel.getKeys()) {
                    Node idNode = XmlUtil.queryNode(doc, key.replaceFirst(concept + "/", "result/")); //$NON-NLS-1$ //$NON-NLS-2$ 
                    if (idNode == null)
                        continue;
                    String id = idNode.getText();
                    if (id != null)
                        idsArray.add(id);
                }

                Set<String> keySet = formatMap.keySet();
                SimpleDateFormat sdf = null;

                for (String key : keySet) {
                    String[] value = formatMap.get(key);
                    Node dateNode = XmlUtil.queryNode(doc, key.replaceFirst(concept + "/", "result/")); //$NON-NLS-1$ //$NON-NLS-2$
                    if (dateNode == null)
                        continue;
                    String dateText = dateNode.getText();

                    if (dateText != null) {
                        if (dateText.trim().length() != 0) {
                            if (value[1].equalsIgnoreCase("DATE")) { //$NON-NLS-1$
                                sdf = new SimpleDateFormat(dateFormat, java.util.Locale.ENGLISH);
                            } else if (value[1].equalsIgnoreCase("DATETIME")) { //$NON-NLS-1$
                                sdf = new SimpleDateFormat(dateTimeFormat, java.util.Locale.ENGLISH);
                            }
                            Date date = sdf.parse(dateText.trim());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            String formatValue = com.amalto.webapp.core.util.Util.formatDate(value[0], calendar);
                            XmlUtil.queryNode(doc, key.replaceFirst(concept + "/", "result/")).setText(formatValue); //$NON-NLS-1$ //$NON-NLS-2$
                        }
                    }
                }

                ItemBean itemBean = new ItemBean(concept, CommonUtil.joinStrings(idsArray, "."), doc.asXML());//$NON-NLS-1$ 
                dynamicAssembleByResultOrder(itemBean, viewBean, entityModel);
                itemBeans.add(itemBean);
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return new Object[] { itemBeans, totalSize };
    }

    protected Document parseResultDocument(String result, String expectedRootElement) throws DocumentException {
        Document doc = XmlUtil.parseText(result);
        Element rootElement = doc.getRootElement();
        if (!rootElement.getName().equals(expectedRootElement)) {
            // When there is a null value in fields, the viewable fields sequence is not enclosed by expected element
            // FIXME Better to find out a solution at the underlying stage
            rootElement.detach();
            Element resultElement = doc.addElement(expectedRootElement);
            resultElement.add(rootElement);
        }
        return doc;
    }

    private Map<String, String[]> checkDisplayFormat(EntityModel entityModel, String language) {
        Map<String, TypeModel> metaData = entityModel.getMetaDataTypes();
        Map<String, String[]> formatMap = new HashMap<String, String[]>();
        String languageStr = "format_" + language.toLowerCase(); //$NON-NLS-1$
        if (metaData == null)
            return formatMap;

        Set<String> keySet = metaData.keySet();
        for (String key : keySet) {
            TypeModel typeModel = metaData.get(key);
            if (typeModel.getType().getTypeName().equalsIgnoreCase("DATE") //$NON-NLS-1$
                    || typeModel.getType().getTypeName().equalsIgnoreCase("DATETIME")) { //$NON-NLS-1$
                if (typeModel.getDisplayFomats() != null) {
                    if (typeModel.getDisplayFomats().size() > 0) {
                        if (typeModel.getDisplayFomats().containsKey(languageStr)) {
                            formatMap.put(key, new String[] { typeModel.getDisplayFomats().get(languageStr),
                                    typeModel.getType().getTypeName() });
                        }
                    }
                }
            }
        }
        return formatMap;
    }

    public void dynamicAssembleByResultOrder(ItemBean itemBean, ViewBean viewBean, EntityModel entityModel) throws Exception {
        if (itemBean.getItemXml() != null) {
            Document docXml = XmlUtil.parseText(itemBean.getItemXml());
            HashMap<String, Integer> countMap = new HashMap<String, Integer>();
            for (String path : viewBean.getViewableXpaths()) {
                String leafPath = path.substring(path.lastIndexOf('/') + 1);
                List<?> nodes = XmlUtil.getValuesFromXPath(docXml, leafPath);
                if (nodes.size() > 1) {
                    // result has same name nodes
                    if (countMap.containsKey(leafPath)) {
                        int count = Integer.valueOf(countMap.get(leafPath).toString());
                        itemBean.set(path, ((Node) nodes.get(count)).getText());
                        countMap.put(leafPath, count + 1);
                    } else {
                        itemBean.set(path, ((Node) nodes.get(0)).getText());
                        countMap.put(leafPath, 1);
                    }
                } else if (nodes.size() == 1) {
                    Node value = (Node) nodes.get(0);
                    TypeModel typeModel = entityModel.getMetaDataTypes().get(path);

                    if (typeModel != null && typeModel.getForeignkey() != null) {
                        itemBean.set(path, path + "-" + value.getText()); //$NON-NLS-1$
                        itemBean.setForeignkeyDesc(path + "-" + value.getText(), getForeignKeyDesc(typeModel, value.getText())); //$NON-NLS-1$
                    } else {
                        itemBean.set(path, value.getText());
                    }
                }
            }
        }
    }

    public void dynamicAssemble(ItemBean itemBean, EntityModel entityModel) throws DocumentException {
        if (itemBean.getItemXml() != null) {
            Document docXml = XmlUtil.parseText(itemBean.getItemXml());
            Map<String, TypeModel> types = entityModel.getMetaDataTypes();
            Set<String> xpaths = types.keySet();
            for (String path : xpaths) {
                TypeModel typeModel = types.get(path);
                if (typeModel.isSimpleType()) {
                    List<?> nodes = XmlUtil.getValuesFromXPath(docXml, path.substring(path.lastIndexOf('/') + 1));
                    if (nodes.size() > 0) {
                        Node value = (Node) nodes.get(0);
                        if (typeModel.isMultiOccurrence()) {
                            List<Serializable> list = new ArrayList<Serializable>();
                            for (Object node : nodes) {
                                list.add(((Node) node).getText());
                            }
                            itemBean.set(path, list);
                        } else {

                            if (typeModel.getForeignkey() != null) {
                                itemBean.set(path, path + "-" + value.getText()); //$NON-NLS-1$
                                itemBean.setForeignkeyDesc(
                                        path + "-" + value.getText(), getForeignKeyDesc(typeModel, value.getText())); //$NON-NLS-1$
                            } else {
                                itemBean.set(path, value.getText());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * DOC HSHU Comment method "getView".
     */
    @Override
    public ViewBean getView(String viewPk, String language) {
        try {

            ViewBean vb = new ViewBean();
            vb.setViewPK(viewPk);

            // get WSView
            WSView wsView = CommonUtil.getPort().getView(new WSGetView(new WSViewPK(viewPk)));

            // bind entity model
            String model = getCurrentDataModel();
            String concept = ViewHelper.getConceptFromDefaultViewName(viewPk);
            EntityModel entityModel = new EntityModel();
            DataModelHelper.parseSchema(model, concept, entityModel, RoleHelper.getUserRoles());
            vb.setBindingEntityModel(entityModel);

            // viewables
            String[] viewables = ViewHelper.getViewables(wsView);
            // FIXME remove viewableXpath
            if (viewables != null) {
                for (String viewable : viewables) {
                    vb.addViewableXpath(viewable);
                }
            }
            vb.setViewables(viewables);

            // searchables
            vb.setSearchables(ViewHelper.getSearchables(wsView, model, language, entityModel));

            return vb;
        } catch (XtentisWebappException e) {
            LOG.error(e.getMessage(), e);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public ItemResult saveItemBean(ItemBean item) {
        try {
            String message = null;
            int status = ItemResult.FAILURE;

            // if update, check the item is modified by others?
            WSPutItemWithReport wsPutItemWithReport = new WSPutItemWithReport(new WSPutItem(new WSDataClusterPK(
                    getCurrentDataCluster()), item.getItemXml(), new WSDataModelPK(getCurrentDataModel()), true),
                    "genericUI", true); //$NON-NLS-1$
            CommonUtil.getPort().putItemWithReport(wsPutItemWithReport);

            if (com.amalto.webapp.core.util.Util.isTransformerExist("beforeSaving_" + item.getConcept())) { //$NON-NLS-1$
                String outputErrorMessage = wsPutItemWithReport.getSource();
                String errorCode = null;
                if (outputErrorMessage != null) {
                    org.w3c.dom.Document doc = com.amalto.webapp.core.util.Util.parse(outputErrorMessage);
                    // TODO what if multiple error nodes ?
                    String xpath = "/report/message"; //$NON-NLS-1$
                    org.w3c.dom.NodeList checkList = com.amalto.webapp.core.util.Util.getNodeList(doc, xpath);
                    org.w3c.dom.Node errorNode = null;
                    if (checkList != null && checkList.getLength() > 0)
                        errorNode = checkList.item(0);
                    if (errorNode != null && errorNode instanceof org.w3c.dom.Element) {
                        org.w3c.dom.Element errorElement = (org.w3c.dom.Element) errorNode;
                        errorCode = errorElement.getAttribute("type"); //$NON-NLS-1$
                        org.w3c.dom.Node child = errorElement.getFirstChild();
                        if (child instanceof org.w3c.dom.Text) {
                            message = child.getTextContent();
                        }
                    }
                }

                if ("info".equals(errorCode)) { //$NON-NLS-1$
                    if (message == null || message.length() == 0)
                        message = MessagesFactory.getMessages().save_process_validation_success();
                    status = ItemResult.SUCCESS;
                } else {
                    // Anything but 0 is unsuccessful
                    if (message == null || message.length() == 0)
                        message = MessagesFactory.getMessages().save_process_validation_failure();
                    status = ItemResult.FAILURE;
                }
            } else {
                message = MessagesFactory.getMessages().save_record_success();
                status = ItemResult.SUCCESS;
            }
            return new ItemResult(status, message);
        } catch (Exception e) {
            ItemResult result;
            // TODO UGLY!!!! to be refactored
            if (e.getLocalizedMessage().indexOf("routing failed:") == 0) {//$NON-NLS-1$ 
                String saveSUCCE = "Save item '" + item.getConcept() + "."//$NON-NLS-1$ //$NON-NLS-2$ 
                        + com.amalto.webapp.core.util.Util.joinStrings(extractIdWithBrackets(item.getIds()), ".")//$NON-NLS-1$
                        + "' successfully, But " + e.getLocalizedMessage();//$NON-NLS-1$ 
                result = new ItemResult(ItemResult.FAILURE, saveSUCCE);
            } else {
                String err = "Unable to save item '" + item.getConcept() + "."//$NON-NLS-1$ //$NON-NLS-2$ 
                        + com.amalto.webapp.core.util.Util.joinStrings(extractIdWithBrackets(item.getIds()), ".") + "'"//$NON-NLS-1$ //$NON-NLS-2$
                        + e.getLocalizedMessage();
                if (e.getLocalizedMessage().indexOf("ERROR_3:") == 0) {//$NON-NLS-1$
                    err = e.getLocalizedMessage();
                }
                result = new ItemResult(ItemResult.FAILURE, err);
            }
            return result;
        }
    }

    @Override
    public ItemResult deleteItemBean(ItemBean item) {
        try {
            String dataClusterPK = getCurrentDataCluster();
            String concept = item.getConcept();
            String[] ids = extractIdWithDots(item.getIds());
            String outputErrorMessage = com.amalto.core.util.Util.beforeDeleting(dataClusterPK, concept, ids);

            String message = null;
            String errorCode = null;
            if (outputErrorMessage != null) {
                Document doc = XmlUtil.parseText(outputErrorMessage);
                // TODO what if multiple error nodes ?
                String xpath = "/report/message"; //$NON-NLS-1$
                Node errorNode = doc.selectSingleNode(xpath);
                if (errorNode instanceof Element) {
                    Element errorElement = (Element) errorNode;
                    errorCode = errorElement.attributeValue("type"); //$NON-NLS-1$
                    message = errorElement.getText();
                }
            }

            int status;
            if (outputErrorMessage == null || "info".equals(errorCode)) { //$NON-NLS-1$               
                WSItemPK wsItem = CommonUtil.getPort().deleteItem(
                        new WSDeleteItem(new WSItemPK(new WSDataClusterPK(dataClusterPK), concept, ids)));
                if (wsItem != null) {
                    status = ItemResult.SUCCESS;
                    pushUpdateReport(ids, concept, "PHYSICAL_DELETE", true); //$NON-NLS-1$
                    if (message == null || message.length() == 0)
                        message = MessagesFactory.getMessages().delete_record_success();
                } else {
                    status = ItemResult.FAILURE;
                    message = MessagesFactory.getMessages().delete_record_failure();
                }
            } else {
                // Anything but 0 is unsuccessful
                status = ItemResult.FAILURE;
                if (message == null || message.length() == 0)
                    message = MessagesFactory.getMessages().delete_process_validation_failure();
            }

            return new ItemResult(status, message);

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ItemResult(ItemResult.FAILURE, e.getLocalizedMessage());
        }
    }

    @Override
    public List<ItemResult> deleteItemBeans(List<ItemBean> items) {
        List<ItemResult> itemResults = new ArrayList<ItemResult>();
        for (ItemBean item : items) {
            ItemResult itemResult = deleteItemBean(item);
            itemResults.add(itemResult);
        }
        return itemResults;
    }

    @Override
    public List<ItemResult> logicalDeleteItems(List<ItemBean> items, String path) {
        List<ItemResult> itemResults = new ArrayList<ItemResult>();
        for (ItemBean item : items) {
            ItemResult itemResult = logicalDeleteItem(item, path);
            itemResults.add(itemResult);
        }
        return itemResults;
    }

    @Override
    public ItemResult logicalDeleteItem(ItemBean item, String path) {
        try {
            String dataClusterPK = getCurrentDataCluster();

            String concept = item.getConcept();
            String[] ids = extractIdWithDots(item.getIds());
            WSItem item1 = CommonUtil.getPort().getItem(
                    new WSGetItem(new WSItemPK(new WSDataClusterPK(dataClusterPK), concept, ids)));
            String xml = item1.getContent();

            WSDroppedItemPK wsItem = CommonUtil.getPort().dropItem(
                    new WSDropItem(new WSItemPK(new WSDataClusterPK(dataClusterPK), concept, ids), path));

            if (wsItem != null && xml != null)
                if ("/".equalsIgnoreCase(path)) { //$NON-NLS-1$
                    pushUpdateReport(ids, concept, "LOGIC_DELETE"); //$NON-NLS-1$
                }
                // TODO updatereport

                else
                    return new ItemResult(ItemResult.FAILURE, "ERROR - dropItem is NULL");//$NON-NLS-1$ 

            return new ItemResult(ItemResult.SUCCESS, "OK");//$NON-NLS-1$ 

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ItemResult(ItemResult.FAILURE, "ERROR -" + e.getLocalizedMessage());//$NON-NLS-1$ 
        }
    }

    private String pushUpdateReport(String[] ids, String concept, String operationType) throws Exception {
        return pushUpdateReport(ids, concept, operationType, false);
    }

    private String pushUpdateReport(String[] ids, String concept, String operationType, boolean routeAfterSaving)
            throws Exception {
        if (LOG.isTraceEnabled())
            LOG.trace("pushUpdateReport() concept " + concept + " operation " + operationType);//$NON-NLS-1$ //$NON-NLS-2$ 

        // TODO check updatedPath
        HashMap<String, UpdateReportItem> updatedPath = null;
        if (!("PHYSICAL_DELETE".equals(operationType) || "LOGIC_DELETE".equals(operationType)) && updatedPath == null) { //$NON-NLS-1$ //$NON-NLS-2$
            return "ERROR_2";//$NON-NLS-1$ 
        }

        String xml2 = createUpdateReport(ids, concept, operationType, updatedPath);

        if (LOG.isDebugEnabled())
            LOG.debug("pushUpdateReport() " + xml2);//$NON-NLS-1$ 

        // TODO routeAfterSaving is true
        return persistentUpdateReport(xml2, routeAfterSaving);
    }

    private String createUpdateReport(String[] ids, String concept, String operationType,
            HashMap<String, UpdateReportItem> updatedPath) throws Exception {

        String revisionId = null;
        String dataModelPK = getCurrentDataModel() == null ? "" : getCurrentDataModel();//$NON-NLS-1$ 
        String dataClusterPK = getCurrentDataCluster() == null ? "" : getCurrentDataCluster();//$NON-NLS-1$ 

        String username = com.amalto.webapp.core.util.Util.getLoginUserName();
        String universename = com.amalto.webapp.core.util.Util.getLoginUniverse();
        if (universename != null && universename.length() > 0)
            revisionId = com.amalto.webapp.core.util.Util.getRevisionIdFromUniverse(universename, concept);

        StringBuilder keyBuilder = new StringBuilder();
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                keyBuilder.append(ids[i]);
                if (i != ids.length - 1)
                    keyBuilder.append("."); //$NON-NLS-1$
            }
        }
        String key = keyBuilder.length() == 0 ? "null" : keyBuilder.toString(); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        // TODO what is StringEscapeUtils.escapeXml used for
        sb.append("<Update><UserName>").append(username).append("</UserName><Source>genericUI</Source><TimeInMillis>") //$NON-NLS-1$ //$NON-NLS-2$ 
                .append(System.currentTimeMillis()).append("</TimeInMillis><OperationType>") //$NON-NLS-1$
                .append(operationType).append("</OperationType><RevisionID>").append(revisionId) //$NON-NLS-1$
                .append("</RevisionID><DataCluster>").append(dataClusterPK).append("</DataCluster><DataModel>") //$NON-NLS-1$ //$NON-NLS-2$ 
                .append(dataModelPK).append("</DataModel><Concept>").append(concept) //$NON-NLS-1$
                .append("</Concept><Key>").append(key).append("</Key>"); //$NON-NLS-1$ //$NON-NLS-2$ 

        if ("UPDATE".equals(operationType)) { //$NON-NLS-1$
            Collection<UpdateReportItem> list = updatedPath.values();
            boolean isUpdate = false;
            for (UpdateReportItem item : list) {
                String oldValue = item.getOldValue() == null ? "" : item.getOldValue();//$NON-NLS-1$
                String newValue = item.getNewValue() == null ? "" : item.getNewValue();//$NON-NLS-1$
                if (newValue.equals(oldValue))
                    continue;
                sb.append("<Item>   <path>").append(item.getPath()).append("</path>   <oldValue>")//$NON-NLS-1$ //$NON-NLS-2$
                        .append(oldValue).append("</oldValue>   <newValue>")//$NON-NLS-1$
                        .append(newValue).append("</newValue></Item>");//$NON-NLS-1$
                isUpdate = true;
            }
            if (!isUpdate)
                return null;
        }
        sb.append("</Update>");//$NON-NLS-1$
        return sb.toString();
    }

    private static String persistentUpdateReport(String xml2, boolean routeAfterSaving) throws Exception {
        if (xml2 == null)
            return "OK";//$NON-NLS-1$

        WSItemPK itemPK = CommonUtil.getPort().putItem(
                new WSPutItem(new WSDataClusterPK("UpdateReport"), xml2, new WSDataModelPK("UpdateReport"), false)); //$NON-NLS-1$ //$NON-NLS-2$

        if (routeAfterSaving)
            CommonUtil.getPort().routeItemV2(new WSRouteItemV2(itemPK));

        return "OK";//$NON-NLS-1$ 
    }

    @Override
    public ItemBasePageLoadResult<ItemBean> queryItemBeans(QueryModel config) {
        PagingLoadConfig pagingLoad = config.getPagingLoadConfig();
        String sortDir = null;
        if (SortDir.ASC.equals(pagingLoad.getSortDir())) {
            sortDir = ItemHelper.SEARCH_DIRECTION_ASC;
        }
        if (SortDir.DESC.equals(pagingLoad.getSortDir())) {
            sortDir = ItemHelper.SEARCH_DIRECTION_DESC;
        }
        Map<String, TypeModel> types = config.getModel().getMetaDataTypes();
        TypeModel typeModel = types.get(pagingLoad.getSortField());

        if (typeModel != null) {
            if (DataTypeConstants.INTEGER.getTypeName().equals(typeModel.getType().getBaseTypeName())
                    || DataTypeConstants.INT.getTypeName().equals(typeModel.getType().getBaseTypeName())
                    || DataTypeConstants.LONG.getTypeName().equals(typeModel.getType().getBaseTypeName())
                    || DataTypeConstants.DECIMAL.getTypeName().equals(typeModel.getType().getBaseTypeName())
                    || DataTypeConstants.FLOAT.getTypeName().equals(typeModel.getType().getBaseTypeName())
                    || DataTypeConstants.DOUBLE.getTypeName().equals(typeModel.getType().getBaseTypeName())) {
                sortDir = "NUMBER:" + sortDir; //$NON-NLS-1$
            }
        }
        Object[] result = getItemBeans(config.getDataClusterPK(), config.getView(), config.getModel(), config.getCriteria(),
                pagingLoad.getOffset(), pagingLoad.getLimit(), sortDir, pagingLoad.getSortField(), config.getLanguage());
        @SuppressWarnings("unchecked")
        List<ItemBean> itemBeans = (List<ItemBean>) result[0];
        int totalSize = (Integer) result[1];
        return new ItemBasePageLoadResult<ItemBean>(itemBeans, pagingLoad.getOffset(), totalSize);
    }

    @Override
    public List<ItemBaseModel> getViewsList(String language) {
        try {
            String model = getCurrentDataModel();
            String[] businessConcept = CommonUtil.getPort()
                    .getBusinessConcepts(new WSGetBusinessConcepts(new WSDataModelPK(model))).getStrings();
            ArrayList<String> bc = new ArrayList<String>();
            Collections.addAll(bc, businessConcept);
            WSViewPK[] wsViewsPK = CommonUtil.getPort()
                    .getViewPKs(new WSGetViewPKs(ViewHelper.DEFAULT_VIEW_PREFIX + ".*")).getWsViewPK();//$NON-NLS-1$

            // Filter view list according to current datamodel
            TreeMap<String, String> views = new TreeMap<String, String>();
            for (WSViewPK aWsViewsPK : wsViewsPK) {
                WSView wsview = CommonUtil.getPort().getView(new WSGetView(aWsViewsPK));// FIXME: Do we need get each
                // view entity here?
                String concept = ViewHelper.getConceptFromDefaultViewName(wsview.getName());
                if (bc.contains(concept)) {
                    String viewDesc = ViewHelper.getViewLabel(language, wsview);
                    views.put(wsview.getName(), viewDesc);
                }
            }
            Map<String, String> viewMap = getMapSortedByValue(views);

            List<ItemBaseModel> list = new ArrayList<ItemBaseModel>();
            for (String key : viewMap.keySet()) {
                ItemBaseModel bm = new ItemBaseModel();
                bm.set("name", viewMap.get(key));//$NON-NLS-1$ 
                bm.set("value", key);//$NON-NLS-1$ 
                list.add(bm);
            }
            return list;
        } catch (XtentisWebappException e) {
            LOG.error(e.getMessage(), e);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    private static Map<String, String> getMapSortedByValue(Map<String, String> map) {
        TreeSet<Map.Entry<String, String>> set = new TreeSet<Map.Entry<String, String>>(
                new Comparator<Map.Entry<String, String>>() {

                    public int compare(Map.Entry<String, String> obj1, Map.Entry<String, String> obj2) {
                        String obj1Value = obj1.getValue();
                        if (obj1Value != null) {
                            return obj1Value.compareTo(obj2.getValue());
                        } else { // obj1Value == null
                            return obj2.getValue() == null ? 0 : 1;
                        }
                    }
                });
        set.addAll(map.entrySet());
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : set) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private ForeignKeyBean getForeignKeyDesc(TypeModel model, String ids) {
        String xpathForeignKey = model.getForeignkey();
        if (xpathForeignKey == null) {
            return null;
        }
        if (ids == null || ids.trim().length() == 0) {
            return null;
        }

        ForeignKeyBean bean = new ForeignKeyBean();
        bean.setId(ids);
        bean.setForeignKeyPath(model.getXpath());

        if (!model.isRetrieveFKinfos()) {
            return bean;
        } else {
            try {
                ItemPOJOPK pk = new ItemPOJOPK();
                String[] itemId = extractIdWithBrackets(ids);
                pk.setIds(itemId);
                pk.setConceptName(model.getForeignkey().split("/")[0]); //$NON-NLS-1$
                pk.setDataClusterPOJOPK(new DataClusterPOJOPK(getCurrentDataCluster()));
                ItemPOJO item = com.amalto.core.util.Util.getItemCtrl2Local().getItem(pk);

                if (item != null) {
                    org.w3c.dom.Document document = item.getProjection().getOwnerDocument();
                    List<String> foreignKeyInfo = model.getForeignKeyInfo();
                    String formattedId = ""; // Id formatted using foreign key info //$NON-NLS-1$
                    for (String foreignKeyPath : foreignKeyInfo) {
                        NodeList nodes = com.amalto.core.util.Util.getNodeList(document,
                                StringUtils.substringAfter(foreignKeyPath, "/")); //$NON-NLS-1$
                        if (nodes.getLength() == 1) {
                            formattedId += nodes.item(0).getTextContent();
                        } else {
                            throw new IllegalArgumentException(MessagesFactory.getMessages().label_exception_xpath_not_match(
                                    foreignKeyPath, nodes.getLength()));
                        }
                    }

                    bean.setDisplayInfo(formattedId);
                    return bean;
                } else {
                    return null;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                return null;
            }
        }
    }

    private void initFKBean(Element ele, ForeignKeyBean bean) {
        for (Object subEle : ele.elements()) {
            Element curEle = (Element) subEle;
            bean.set(curEle.getName(), curEle.getTextTrim());
            initFKBean(curEle, bean);
        }
    }

    /*********************************************************************
     * Foreign key
     *********************************************************************/

    @Override
    public ItemBasePageLoadResult<ForeignKeyBean> getForeignKeyList(PagingLoadConfig config, TypeModel model,
            String dataClusterPK, boolean ifFKFilter, String value) {
        String xpathForeignKey = model.getForeignkey();
        // to verify
        String xpathInfoForeignKey = model.getForeignKeyInfo().toString().replaceAll("\\[", "").replaceAll("\\]", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        // in search panel, the fkFilter is empty
        String fkFilter = ""; //$NON-NLS-1$
        if (ifFKFilter)
            fkFilter = model.getFkFilter();

        if (xpathForeignKey == null)
            return null;

        List<ForeignKeyBean> fkBeans = new ArrayList<ForeignKeyBean>();
        String[] results = null;
        String count = null;

        try {
            String initxpathForeignKey = ""; //$NON-NLS-1$
            initxpathForeignKey = com.amalto.webapp.core.util.Util.getForeignPathFromPath(xpathForeignKey);

            WSWhereCondition whereCondition = com.amalto.webapp.core.util.Util.getConditionFromPath(xpathForeignKey);
            WSWhereItem whereItem = null;
            if (whereCondition != null) {
                whereItem = new WSWhereItem(whereCondition, null, null);
            }

            // get FK filter
            WSWhereItem fkFilterWi = com.amalto.webapp.core.util.Util.getConditionFromFKFilter(xpathForeignKey, xpathForeignKey,
                    fkFilter);
            if (fkFilterWi != null)
                whereItem = fkFilterWi;
            initxpathForeignKey = initxpathForeignKey.split("/")[0]; //$NON-NLS-1$

            xpathInfoForeignKey = xpathInfoForeignKey == null ? "" : xpathInfoForeignKey; //$NON-NLS-1$
            // foreign key set by business concept
            if (initxpathForeignKey.split("/").length == 1) { //$NON-NLS-1$
                String conceptName = initxpathForeignKey;
                // determine if we have xPath Infos: e.g. labels to display
                String[] xpathInfos = new String[1];
                if (!"".equals(xpathInfoForeignKey) && xpathInfoForeignKey != null)//$NON-NLS-1$
                    xpathInfos = xpathInfoForeignKey.split(","); //$NON-NLS-1$
                else
                    xpathInfos[0] = conceptName;
                value = value == null ? "" : value; //$NON-NLS-1$

                // build query - add a content condition on the pivot if we search for a particular value
                String filteredConcept = conceptName;

                if (value != null && !"".equals(value.trim()) && !".*".equals(value.trim())) { //$NON-NLS-1$ //$NON-NLS-2$
                    List<WSWhereItem> condition = new ArrayList<WSWhereItem>();
                    if (whereItem != null)
                        condition.add(whereItem);
                    WSWhereItem wc = null;
                    String strConcept = conceptName + "/. CONTAINS "; //$NON-NLS-1$

                    if (MDMConfiguration.getDBType().getName().equals(EDBType.QIZX.getName())) {
                        strConcept = conceptName + "//* CONTAINS "; //$NON-NLS-1$
                    }
                    wc = com.amalto.webapp.core.util.Util.buildWhereItem(strConcept + value);
                    condition.add(wc);
                    WSWhereAnd and = new WSWhereAnd(condition.toArray(new WSWhereItem[condition.size()]));
                    WSWhereItem whand = new WSWhereItem(null, and, null);
                    if (whand != null)
                        whereItem = whand;
                }

                // add the xPath Infos Path
                ArrayList<String> xPaths = new ArrayList<String>();
                if (model.isRetrieveFKinfos())
                    // add the xPath Infos Path
                    for (int i = 0; i < xpathInfos.length; i++) {
                        xPaths.add(com.amalto.webapp.core.util.Util.getFormatedFKInfo(
                                xpathInfos[i].replaceFirst(conceptName, filteredConcept), filteredConcept));
                    }
                // add the key paths last, since there may be multiple keys
                xPaths.add(filteredConcept + "/../../i"); //$NON-NLS-1$
                // order by
                String orderbyPath = null;
                if (!MDMConfiguration.getDBType().getName().equals(EDBType.QIZX.getName())) {
                    if (!"".equals(xpathInfoForeignKey) && xpathInfoForeignKey != null) { //$NON-NLS-1$
                        orderbyPath = com.amalto.webapp.core.util.Util.getFormatedFKInfo(
                                xpathInfos[0].replaceFirst(conceptName, filteredConcept), filteredConcept);
                    }
                }

                // Run the query
                if (!com.amalto.webapp.core.util.Util.isCustomFilter(fkFilter)) {

                    results = CommonUtil
                            .getPort()
                            .xPathsSearch(
                                    new WSXPathsSearch(new WSDataClusterPK(dataClusterPK), null, new WSStringArray(xPaths
                                            .toArray(new String[xPaths.size()])), whereItem, -1, config.getOffset(), config
                                            .getLimit(), orderbyPath, null)).getStrings();
                    count = CommonUtil.getPort()
                            .count(new WSCount(new WSDataClusterPK(dataClusterPK), conceptName, whereItem, -1)).getValue();

                } else {

                    String injectedXpath = com.amalto.webapp.core.util.Util.getInjectedXpath(fkFilter);
                    results = CommonUtil
                            .getPort()
                            .getItemsByCustomFKFilters(
                                    new WSGetItemsByCustomFKFilters(new WSDataClusterPK(dataClusterPK), conceptName,
                                            new WSStringArray(xPaths.toArray(new String[xPaths.size()])), injectedXpath, config
                                                    .getOffset(), config.getLimit(), orderbyPath, null)).getStrings();

                    count = CommonUtil
                            .getPort()
                            .countItemsByCustomFKFilters(
                                    new WSCountItemsByCustomFKFilters(new WSDataClusterPK(dataClusterPK), conceptName,
                                            injectedXpath)).getValue();
                }
            }

            if (results != null) {
                for (String result : results) {
                    ForeignKeyBean bean = new ForeignKeyBean();
                    String id = ""; //$NON-NLS-1$
                    @SuppressWarnings("unchecked")
                    List<Node> nodes = XmlUtil.getValuesFromXPath(XmlUtil.parseText(result), "//i"); //$NON-NLS-1$
                    if (nodes != null) {
                        for (Node node : nodes) {
                            id += "[" + (node.getText() == null ? "" : node.getText()) + "]"; //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
                        }
                    }
                    bean.setId(id);
                    if (result != null) {
                        Element root = XmlUtil.parseText(result).getRootElement();
                        if (root.getName().equals("result"))//$NON-NLS-1$
                            initFKBean(root, bean);
                        else
                            bean.set(root.getName(), root.getTextTrim());
                    }
                    fkBeans.add(bean);
                }
            }

            return new ItemBasePageLoadResult<ForeignKeyBean>(fkBeans, config.getOffset(), Integer.valueOf(count));
        } catch (XtentisWebappException e) {
            LOG.error(e.getMessage(), e);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * ****************************************************************** Bookmark management
     *********************************************************************/

    @Override
    public boolean isExistCriteria(String dataObjectLabel, String id) {
        try {
            WSItemPK wsItemPK = new WSItemPK();
            wsItemPK.setConceptName("BrowseItem");//$NON-NLS-1$

            WSDataClusterPK wsDataClusterPK = new WSDataClusterPK();
            wsDataClusterPK.setPk(XSystemObjects.DC_SEARCHTEMPLATE.getName());
            wsItemPK.setWsDataClusterPK(wsDataClusterPK);

            String[] ids = new String[1];
            ids[0] = id;
            wsItemPK.setIds(ids);

            WSExistsItem wsExistsItem = new WSExistsItem(wsItemPK);
            WSBoolean wsBoolean = CommonUtil.getPort().existsItem(wsExistsItem);
            return wsBoolean.is_true();
        } catch (XtentisWebappException e) {
            LOG.error(e.getMessage(), e);
            return false;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String saveCriteria(String viewPK, String templateName, boolean isShared, String criteriaString) {
        String returnString = "OK";//$NON-NLS-1$
        try {
            String owner = com.amalto.webapp.core.util.Util.getLoginUserName();
            SearchTemplate searchTemplate = new SearchTemplate();
            searchTemplate.setViewPK(viewPK);
            searchTemplate.setCriteriaName(templateName);
            searchTemplate.setShared(isShared);
            searchTemplate.setOwner(owner);
            searchTemplate.setCriteria(criteriaString);

            WSItemPK pk = CommonUtil.getPort().putItem(
                    new WSPutItem(new WSDataClusterPK(XSystemObjects.DC_SEARCHTEMPLATE.getName()), searchTemplate
                            .marshal2String(), new WSDataModelPK(XSystemObjects.DM_SEARCHTEMPLATE.getName()), false));

            if (pk != null)
                returnString = "OK";//$NON-NLS-1$
            else
                returnString = null;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            returnString = e.getMessage();
        } finally {
            return returnString;
        }
    }

    @Override
    public PagingLoadResult<ItemBaseModel> querySearchTemplates(String view, boolean isShared, PagingLoadConfig load) {
        List<String> results = Arrays.asList(getSearchTemplateNames(view, isShared, load.getOffset(), load.getLimit()));
        List<ItemBaseModel> list = new ArrayList<ItemBaseModel>();
        for (String result : results) {
            ItemBaseModel bm = new ItemBaseModel();
            try {
                org.w3c.dom.Node resultNode = com.amalto.webapp.core.util.Util.parse(result).getFirstChild();
                for (int i = 0; i < resultNode.getChildNodes().getLength(); i++) {
                    if (resultNode.getChildNodes().item(i) instanceof org.w3c.dom.Element) {
                        if (resultNode.getChildNodes().item(i).getNodeName().equals("CriteriaName")) { //$NON-NLS-1$
                            bm.set("name", resultNode.getChildNodes().item(i).getFirstChild().getTextContent());//$NON-NLS-1$
                            bm.set("value", resultNode.getChildNodes().item(i).getFirstChild().getTextContent());//$NON-NLS-1$
                        } else if (resultNode.getChildNodes().item(i).getNodeName().equals("Shared")) { //$NON-NLS-1$
                            bm.set("shared", resultNode.getChildNodes().item(i).getFirstChild().getTextContent()); //$NON-NLS-1$
                        }
                    }
                }
                list.add(bm);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        int totalSize = results.size();
        return new BasePagingLoadResult<ItemBaseModel>(list, load.getOffset(), totalSize);
    }

    @Override
    public List<ItemBaseModel> getUserCriterias(String view) {
        String[] results = getSearchTemplateNames(view, false, 0, 0);
        List<ItemBaseModel> list = new ArrayList<ItemBaseModel>();

        for (String result : results) {
            ItemBaseModel bm = new ItemBaseModel();

            try {
                org.w3c.dom.Node resultNode = com.amalto.webapp.core.util.Util.parse(result).getFirstChild();
                for (int i = 0; i < resultNode.getChildNodes().getLength(); i++) {
                    if (resultNode.getChildNodes().item(i) instanceof org.w3c.dom.Element) {
                        if (resultNode.getChildNodes().item(i).getNodeName().equals("CriteriaName")) { //$NON-NLS-1$
                            bm.set("name", resultNode.getChildNodes().item(i).getFirstChild().getTextContent());//$NON-NLS-1$
                            bm.set("value", resultNode.getChildNodes().item(i).getFirstChild().getTextContent());//$NON-NLS-1$
                        } else if (resultNode.getChildNodes().item(i).getNodeName().equals("Shared")) { //$NON-NLS-1$
                            bm.set("shared", resultNode.getChildNodes().item(i).getFirstChild().getTextContent()); //$NON-NLS-1$
                        }
                    }
                }
                list.add(bm);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return list;
    }

    private String[] getSearchTemplateNames(String view, boolean isShared, int start, int limit) {
        try {
            int localStart = 0;
            int localLimit = 0;
            if (start == limit && limit == 0) {
                localStart = 0;
                localLimit = Integer.MAX_VALUE;
            } else {
                localStart = start;
                localLimit = limit;

            }
            WSWhereItem wi = new WSWhereItem();

            WSWhereCondition wc1 = new WSWhereCondition("BrowseItem/ViewPK", WSWhereOperator.EQUALS, view,//$NON-NLS-1$
                    WSStringPredicate.NONE, false);

            WSWhereCondition wc3 = new WSWhereCondition("BrowseItem/Owner", WSWhereOperator.EQUALS,//$NON-NLS-1$
                    RoleHelper.getCurrentUserName(), WSStringPredicate.OR, false);
            WSWhereCondition wc4;
            WSWhereOr or = new WSWhereOr();
            if (isShared) {
                wc4 = new WSWhereCondition("BrowseItem/Shared", WSWhereOperator.EQUALS, "true", WSStringPredicate.NONE, false);//$NON-NLS-1$ //$NON-NLS-2$

                or = new WSWhereOr(new WSWhereItem[] { new WSWhereItem(wc3, null, null), new WSWhereItem(wc4, null, null) });
            } else {
                or = new WSWhereOr(new WSWhereItem[] { new WSWhereItem(wc3, null, null) });
            }

            WSWhereAnd and = new WSWhereAnd(new WSWhereItem[] { new WSWhereItem(wc1, null, null),

            new WSWhereItem(null, null, or) });

            wi = new WSWhereItem(null, and, null);

            String[] results = CommonUtil
                    .getPort()
                    .xPathsSearch(
                            new WSXPathsSearch(
                                    new WSDataClusterPK(XSystemObjects.DC_SEARCHTEMPLATE.getName()),
                                    null,// pivot
                                    new WSStringArray(new String[] { "BrowseItem/CriteriaName", "BrowseItem/Shared" }), wi, -1, localStart, localLimit, null, // order //$NON-NLS-1$ //$NON-NLS-2$
                                    // by
                                    null // direction
                            )).getStrings();
            return results;

        } catch (XtentisWebappException e) {
            LOG.error(e.getMessage(), e);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new String[] {};
    }

    @Override
    public String deleteSearchTemplate(String id) {
        try {
            String[] ids = { id };
            String concept = "BrowseItem";//$NON-NLS-1$
            String dataClusterPK = XSystemObjects.DC_SEARCHTEMPLATE.getName();
            if (ids != null) {
                WSItemPK wsItem = CommonUtil.getPort().deleteItem(
                        new WSDeleteItem(new WSItemPK(new WSDataClusterPK(dataClusterPK), concept, ids)));

                if (wsItem == null)
                    return MessagesFactory.getMessages().label_error_delete_template_null();
                return "OK";//$NON-NLS-1$
            } else {
                return "OK";//$NON-NLS-1$
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "ERROR -" + e.getLocalizedMessage();//$NON-NLS-1$
        }
    }

    @Override
    public String getCriteriaByBookmark(String bookmark) {
        try {
            String criteria = "";//$NON-NLS-1$
            String result = CommonUtil
                    .getPort()
                    .getItem(
                            new WSGetItem(new WSItemPK(new WSDataClusterPK(XSystemObjects.DC_SEARCHTEMPLATE.getName()),
                                    "BrowseItem",//$NON-NLS-1$
                                    new String[] { bookmark }))).getContent().trim();
            if (result != null) {
                if (result.indexOf("<SearchCriteria>") != -1)//$NON-NLS-1$
                    criteria = result.substring(result.indexOf("<SearchCriteria>") + 16, result.indexOf("</SearchCriteria>"));//$NON-NLS-1$ //$NON-NLS-2$
            }
            return criteria;
        } catch (XtentisWebappException e) {
            LOG.error(e.getMessage(), e);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public AppHeader getAppHeader() throws Exception {

        AppHeader header = new AppHeader();
        header.setDatacluster(getCurrentDataCluster());
        header.setDatamodel(getCurrentDataModel());
        header.setStandAloneMode(ItemsBrowserConfiguration.isStandalone());
        header.setUsingDefaultForm(ItemsBrowserConfiguration.isUsingDefaultForm());
        return header;

    }

    @Override
    public ItemBean getItem(ItemBean itemBean, EntityModel entityModel) throws Exception {
        String dataCluster = getCurrentDataCluster();
        String dataModel = getCurrentDataModel();
        String concept = itemBean.getConcept();
        // get item
        WSDataClusterPK wsDataClusterPK = new WSDataClusterPK(dataCluster);
        String[] ids = itemBean.getIds() == null ? null : itemBean.getIds().split("\\.");//$NON-NLS-1$
        WSItem wsItem = CommonUtil.getPort().getItem(new WSGetItem(new WSItemPK(wsDataClusterPK, itemBean.getConcept(), ids)));
        itemBean.setItemXml(wsItem.getContent());
        // parse schema
        DataModelHelper.parseSchema(dataModel, concept, entityModel, RoleHelper.getUserRoles());
        // dynamic Assemble
        dynamicAssemble(itemBean, entityModel);

        return itemBean;
    }

    /**
     * @param ids Expect a id like "[value0][value1][value2]"
     * @return Returns an array with ["value0", "value1", "value2"]
     */
    private static String[] extractIdWithBrackets(String ids) {
        List<String> idList = new ArrayList<String>();
        Matcher matcher = extractIdPattern.matcher(ids);
        boolean hasMatchedOnce = false;
        while (matcher.find()) {
            String id = matcher.group();
            id = id.replaceAll("\\[", "").replaceAll("\\]", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            idList.add(id);
            hasMatchedOnce = true;
        }

        if (!hasMatchedOnce) {
            throw new IllegalArgumentException(MessagesFactory.getMessages().label_exception_id_malform(ids));
        }

        return idList.toArray(new String[idList.size()]);
    }

    /**
     * @param ids Expect a id like "value0.value1.value2"
     * @return Returns an array with ["value0", "value1", "value2"]
     */
    private static String[] extractIdWithDots(String ids) {
        List<String> idList = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(ids, "."); //$NON-NLS-1$
        if (!tokenizer.hasMoreTokens()) {
            throw new IllegalArgumentException(MessagesFactory.getMessages().label_exception_id_malform(ids));
        }

        while (tokenizer.hasMoreTokens()) {
            idList.add(tokenizer.nextToken());
        }
        return idList.toArray(new String[idList.size()]);
    }

    @Override
    public List<ItemBaseModel> getUploadTableNames(String value) {
        try {
            String[] result = CommonUtil.getPort()
                    .getBusinessConcepts(new WSGetBusinessConcepts(new WSDataModelPK(this.getCurrentDataModel())))
                    .getStrings();

            List<ItemBaseModel> list = new ArrayList<ItemBaseModel>();
            for (String str : result) {
                ItemBaseModel model = new ItemBaseModel();
                model.set("label", str); //$NON-NLS-1$
                model.set("key", str); //$NON-NLS-1$
                list.add(model);
            }
            return list;

        } catch (XtentisWebappException e) {
            LOG.error(e.getMessage(), e);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Map<String, List<String>> getUploadTableDescription(String tableName) throws Exception {
        try {
            String[] tableKeys = CommonUtil.getPort()
                    .getBusinessConceptKey(new WSGetBusinessConceptKey(new WSDataModelPK(this.getCurrentDataModel()), tableName))
                    .getFields();
            String schema = CommonUtil.getPort().getDataModel(new WSGetDataModel(new WSDataModelPK(this.getCurrentDataModel())))
                    .getXsdSchema();
            XSOMParser parser = new XSOMParser();
            parser.parse(new StringReader(schema));
            XSSchemaSet xss = parser.getResult();
            XSElementDecl decl;
            decl = xss.getElementDecl("", tableName); //$NON-NLS-1$
            if (decl == null) {
                throw new XtentisWebappException(MessagesFactory.getMessages().label_exception_upload_table_not_found(tableName));
            }
            XSComplexType type = (XSComplexType) decl.getType();
            XSParticle[] xsp = type.getContentType().asParticle().getTerm().asModelGroup().getChildren();
            ArrayList<String> fieldNames = new ArrayList<String>();
            for (int i = 0; i < xsp.length; i++) {
                fieldNames.add(xsp[i].getTerm().asElementDecl().getName());
            }
            String[] fields = fieldNames.toArray(new String[fieldNames.size()]);

            Map<String, List<String>> map = new HashMap<String, List<String>>();
            List<String> keyList = new ArrayList<String>();
            for (String str : tableKeys) {
                keyList.add(str);
            }
            map.put("keys", keyList); //$NON-NLS-1$

            List<String> fieldList = new ArrayList<String>();
            for (String str : fields) {
                fieldList.add(str);
            }
            map.put("fields", fieldList); //$NON-NLS-1$

            List<String> tableList = new ArrayList<String>();
            tableList.add(tableName);
            map.put("name", tableList); //$NON-NLS-1$

            return map;

        } catch (RemoteException e) {
            LOG.error(e.getMessage(), e);
        } catch (XtentisWebappException e) {
            LOG.error(e.getMessage(), e);
        } catch (SAXException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<ItemBaseModel> deleteItemsBrowserTable(String tableName) throws Exception {
        tableName = tableName.replaceAll("\\s+", ""); //$NON-NLS-1$ //$NON-NLS-2$

        try {
            /* deletes item */
            CommonUtil.getPort().deleteItems(
                    new WSDeleteItems(new WSDataClusterPK(this.getCurrentDataCluster()), tableName, null, -1));

            /* deletes concept */
            CommonUtil.getPort().deleteBusinessConcept(
                    new WSDeleteBusinessConcept(new WSDataModelPK(this.getCurrentDataModel()), tableName));
        } catch (RemoteException e) {
            LOG.error(e.getMessage(), e);
        } catch (XtentisWebappException e) {
            LOG.error(e.getMessage(), e);
        }
        return this.getUploadTableNames(""); //$NON-NLS-1$
    }

    @Override
    public void addNewTable(String concept, String[] fields, String[] keys) throws Exception {
        concept = concept.replaceAll(" ", "_");//$NON-NLS-1$//$NON-NLS-2$

        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].replaceAll(" ", "_");//$NON-NLS-1$//$NON-NLS-2$
        }

        CommonUtil.getPort().putBusinessConceptSchema(
                new WSPutBusinessConceptSchema(new WSDataModelPK(this.getCurrentDataModel()), createXsd(concept, fields, keys)));
    }

    private String createXsd(String concept, String[] fields, String[] keys) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<xsd:element name=\"").append(concept).append("\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");//$NON-NLS-1$//$NON-NLS-2$
        buffer.append("<xsd:complexType>");//$NON-NLS-1$
        buffer.append("<xsd:sequence maxOccurs=\"1\" minOccurs=\"0\">");//$NON-NLS-1$
        for (int i = 0; i < fields.length; i++)
            if (!fields[i].equals("")) {//$NON-NLS-1$
                buffer.append("<xsd:element maxOccurs=\"1\" ");//$NON-NLS-1$
                if (keys[i].equals("true"))//$NON-NLS-1$
                    buffer.append("minOccurs=\"1\" ");//$NON-NLS-1$
                else
                    buffer.append("minOccurs=\"0\" ");//$NON-NLS-1$
                buffer.append("name=\"").append(fields[i]).append("\" ");//$NON-NLS-1$//$NON-NLS-2$
                if (keys[i].equals("false"))//$NON-NLS-1$
                    buffer.append(" nillable=\"true\" ");//$NON-NLS-1$
                buffer.append("type=\"xsd:string\"/>");//$NON-NLS-1$
            }
        buffer.append("</xsd:sequence>");//$NON-NLS-1$
        buffer.append("</xsd:complexType>");//$NON-NLS-1$
        buffer.append("<xsd:unique name=\"").append(concept).append("\">");//$NON-NLS-1$//$NON-NLS-2$
        buffer.append("<xsd:selector xpath=\".\"/>");//$NON-NLS-1$
        for (int i = 0; i < keys.length; i++)
            if (!fields[i].equals("") && keys[i].equals("true"))//$NON-NLS-1$ //$NON-NLS-2$
                buffer.append("<xsd:field xpath=\"").append(fields[i]).append("\"/>");//$NON-NLS-1$//$NON-NLS-2$
        buffer.append("</xsd:unique>");//$NON-NLS-1$
        buffer.append("</xsd:element>");//$NON-NLS-1$

        return buffer.toString();
    }

    public String[] getTableKeys(String tableName) throws Exception {
        try {
            return CommonUtil.getPort()
                    .getBusinessConceptKey(new WSGetBusinessConceptKey(new WSDataModelPK(this.getCurrentDataModel()), tableName))
                    .getFields();
        } catch (Exception e) {
            throw new Exception("", e); //$NON-NLS-1$
        }
    }

    public String[] getTableFieldNames(String tableName) throws Exception {
        try {
            // grab the table fileds (e.g. the concept sub-elements)
            String schema = CommonUtil.getPort().getDataModel(new WSGetDataModel(new WSDataModelPK(this.getCurrentDataModel())))
                    .getXsdSchema();

            XSOMParser parser = new XSOMParser();
            parser.parse(new StringReader(schema));
            XSSchemaSet xss = parser.getResult();

            XSElementDecl decl;
            decl = xss.getElementDecl("", tableName);//$NON-NLS-1$
            if (decl == null) {
                throw new Exception(""); //$NON-NLS-1$
            }
            XSComplexType type = (XSComplexType) decl.getType();
            XSParticle[] xsp = type.getContentType().asParticle().getTerm().asModelGroup().getChildren();
            ArrayList<String> fieldNames = new ArrayList<String>();
            for (int i = 0; i < xsp.length; i++) {
                fieldNames.add(xsp[i].getTerm().asElementDecl().getName());
            }
            return fieldNames.toArray(new String[fieldNames.size()]);
        } catch (Exception e) {
            throw new Exception("", e); //$NON-NLS-1$

        }
    }
    @Override
    public void deleteDocument(String concept, DownloadBaseModel model) throws Exception {
        try {
            String[] keys = getTableKeys(concept);
            String[] fields = getTableFieldNames(concept);
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < keys.length; i++) {
                for (int j = 0; j < fields.length; j++) {
                    if (keys[i].equals(fields[j])) {
                        if (model.get(fields[j]) != null)
                            list.add(model.get(fields[j]).toString());
                    }
                }
            }
            if (!list.isEmpty())
                CommonUtil.getPort().deleteItem(
                        new WSDeleteItem(new WSItemPK(new WSDataClusterPK(this.getCurrentDataCluster()), concept, list
                                .toArray(new String[list.size()]))));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void updateDocument(String concept, List<DownloadBaseModel> models) throws Exception {
        try {
            String[] fieldNames = getTableFieldNames(concept);
            for (DownloadBaseModel model : models) {
                StringBuffer xml = new StringBuffer();
                xml.append("<" + concept + ">");//$NON-NLS-1$//$NON-NLS-2$
                for (int i = 0; i < fieldNames.length; i++) {
                    xml.append("<" + fieldNames[i] + ">");//$NON-NLS-1$//$NON-NLS-2$
                    xml.append(model.get(fieldNames[i]) != null ? model.get(fieldNames[i]) : "");//$NON-NLS-1$
                    xml.append("</" + fieldNames[i] + ">");//$NON-NLS-1$//$NON-NLS-2$
                }
                xml.append("</" + concept + ">");//$NON-NLS-1$//$NON-NLS-2$
                CommonUtil.getPort().putItem(
                        new WSPutItem(new WSDataClusterPK(this.getCurrentDataCluster()), xml.toString(), new WSDataModelPK(
                                this.getCurrentDataModel()), false));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public PagingLoadResult<DownloadBaseModel> getDownloadTableContent(String tableName, PagingLoadConfig load) {
        try {
            String[] results = CommonUtil
                    .getPort()
                    .getItems(
                            new WSGetItems(new WSDataClusterPK(this.getCurrentDataCluster()), tableName, null, -1, load
                                    .getOffset(), load.getLimit())).getStrings();

            int count = CommonUtil.getPort()
                    .getItems(new WSGetItems(new WSDataClusterPK(this.getCurrentDataCluster()), tableName, null, -1, 0, -1))
                    .getStrings().length;
            // TODO why result is wrong when directly using count WS
            // String count = Util.getPort().count(new WSCount(new WSDataClusterPK(Configuration.datacluster),
            // tableName, null, -1))
            // .getValue();

            String[] fieldNames = getTableFieldNames(tableName);

            List<DownloadBaseModel> list = new ArrayList<DownloadBaseModel>();

            for (int i = 0; i < results.length; i++) {
                DownloadBaseModel model = new DownloadBaseModel();
                Element element = DocumentHelper.parseText(results[i]).getRootElement();

                for (int k = 0; k < fieldNames.length; k++) {
                    Node node = element.element(fieldNames[k]);
                    String value = null;
                    if (node != null)
                        value = node.getText();
                    model.set(fieldNames[k], value != null ? value : "");//$NON-NLS-1$
                }
                list.add(model);
            }
            return new BasePagingLoadResult<DownloadBaseModel>(list, load.getOffset(), count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public DownloadTable getDownloadTable(String tableName) throws Exception {
        return new DownloadTable(tableName, getTableKeys(tableName), getTableFieldNames(tableName));
    }

    /**
     * DOC HSHU Comment method "getForeignKeyPolymTypeList".
     * 
     * @param value
     * @param xpathForeignKey
     * @param docIndex
     * @param nodeId
     * @return
     * @throws Exception
     */
    public List<Restriction> getForeignKeyPolymTypeList(String xpathForeignKey, String language) throws Exception {

        String fkEntityType = null;
        ReusableType entityReusableType = null;
        List<SubTypeBean> derivedTypes = new ArrayList<SubTypeBean>();

        if (xpathForeignKey != null && xpathForeignKey.length() > 0) {
            if (xpathForeignKey.startsWith("/"))//$NON-NLS-1$
                xpathForeignKey = xpathForeignKey.substring(1);
            String fkEntity = "";//$NON-NLS-1$
            if (xpathForeignKey.indexOf("/") != -1) {//$NON-NLS-1$
                fkEntity = xpathForeignKey.substring(0, xpathForeignKey.indexOf("/"));//$NON-NLS-1$
            } else {
                fkEntity = xpathForeignKey;
            }

            fkEntityType = SchemaWebAgent.getInstance().getBusinessConcept(fkEntity).getCorrespondTypeName();
            entityReusableType = SchemaWebAgent.getInstance().getReusableType(fkEntityType);
            entityReusableType.load();

            List<ReusableType> subtypes = SchemaWebAgent.getInstance().getMySubtypes(fkEntityType, true);
            for (ReusableType reusableType : subtypes) {
                reusableType.load();
                SubTypeBean subTypeBean = new SubTypeBean();
                subTypeBean.setName(reusableType.getName());
                subTypeBean.setLabel(reusableType.getLabelMap().get(language) == null ? reusableType.getName() : reusableType
                        .getLabelMap().get(language));
                subTypeBean.setOrderValue(reusableType.getOrderValue());
                if (reusableType.isAbstract()) {
                    continue;
                }
                derivedTypes.add(subTypeBean);
            }

        }

        Collections.sort(derivedTypes);

        List<Restriction> ret = new ArrayList<Restriction>();

        if (fkEntityType != null && !entityReusableType.isAbstract()) {
            Restriction re = new Restriction();
            re.setName(entityReusableType.getName());
            re.setValue(entityReusableType.getLabelMap().get(language) == null ? entityReusableType.getName()
                    : entityReusableType.getLabelMap().get(language));
            ret.add(re);
        }

        for (SubTypeBean type : derivedTypes) {
            Restriction re = new Restriction();
            re.setName(type.getName());
            re.setValue(type.getLabel());
            ret.add(re);
        }
        return ret;
    }

    /**
     * DOC HSHU Comment method "switchForeignKeyType".
     * 
     * @param targetEntity
     * @param xpathForeignKey
     * @param xpathInfoForeignKey
     * @param fkFilter
     * @return
     * @throws Exception
     */
    public ForeignKeyDrawer switchForeignKeyType(String targetEntityType, String xpathForeignKey, String xpathInfoForeignKey,
            String fkFilter) throws Exception {
        ForeignKeyDrawer fkDrawer = new ForeignKeyDrawer();

        BusinessConcept businessConcept = SchemaWebAgent.getInstance().getFirstBusinessConceptFromRootType(targetEntityType);
        if (businessConcept == null)
            return null;
        String targetEntity = businessConcept.getName();

        if (xpathForeignKey != null && xpathForeignKey.length() > 0) {
            xpathForeignKey = replaceXpathRoot(targetEntity, xpathForeignKey);
        }

        if (xpathInfoForeignKey != null && xpathInfoForeignKey.length() > 0) {
            String[] fkInfoPaths = xpathInfoForeignKey.split(",");//$NON-NLS-1$
            xpathInfoForeignKey = "";//$NON-NLS-1$
            for (int i = 0; i < fkInfoPaths.length; i++) {
                String fkInfoPath = fkInfoPaths[i];
                String relacedFkInfoPath = replaceXpathRoot(targetEntity, fkInfoPath);
                if (relacedFkInfoPath != null && relacedFkInfoPath.length() > 0) {
                    if (xpathInfoForeignKey.length() > 0)
                        xpathInfoForeignKey += ",";//$NON-NLS-1$
                    xpathInfoForeignKey += relacedFkInfoPath;
                }
            }
        }
        fkDrawer.setXpathForeignKey(xpathForeignKey);
        fkDrawer.setXpathInfoForeignKey(xpathInfoForeignKey);
        return fkDrawer;
    }

    /**
     * DOC HSHU Comment method "replaceXpathRoot".
     * 
     * @param targetEntity
     * @param xpathForeignKey
     * @return
     */
    private String replaceXpathRoot(String targetEntity, String xpath) {
        if (xpath.indexOf("/") != -1)//$NON-NLS-1$
            xpath = targetEntity + xpath.substring(xpath.indexOf("/"));//$NON-NLS-1$
        else
            xpath = targetEntity;
        return xpath;
    }
}
