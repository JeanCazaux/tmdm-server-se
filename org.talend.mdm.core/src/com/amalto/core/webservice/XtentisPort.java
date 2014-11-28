// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, compilation edition R40)
// Generated source version: 1.1.2

package com.amalto.core.webservice;

import com.amalto.core.integrity.FKIntegrityCheckResult;

import java.rmi.RemoteException;

public interface XtentisPort extends java.rmi.Remote {

    public WSVersion getComponentVersion(WSGetComponentVersion wsGetComponentVersion) throws RemoteException;

    public WSString ping(WSPing wsPing) throws RemoteException;

    public WSString refreshCache(WSRefreshCache refreshCache) throws RemoteException;

    public WSString logout(WSLogout wsLogout) throws RemoteException;

    public WSInt initMDM(WSInitData initData) throws RemoteException;

    public WSDataModelPKArray getDataModelPKs(WSRegexDataModelPKs regexp) throws RemoteException;

    public WSDataModel getDataModel(WSGetDataModel wsDataModelget) throws RemoteException;

    public WSBoolean existsDataModel(WSExistsDataModel wsDataModelExists) throws RemoteException;

    public WSDataModelPK putDataModel(WSPutDataModel wsDataModel) throws RemoteException;

    public WSDataModelPK deleteDataModel(WSDeleteDataModel wsDeleteDataModel) throws RemoteException;

    public WSString checkSchema(WSCheckSchema wsSchema) throws RemoteException;

    public WSString deleteBusinessConcept(WSDeleteBusinessConcept wsDeleteBusinessConcept) throws RemoteException;

    public WSStringArray getBusinessConcepts(WSGetBusinessConcepts wsGetBusinessConcepts) throws RemoteException;

    public WSString putBusinessConcept(WSPutBusinessConcept wsPutBusinessConcept) throws RemoteException;

    public WSString putBusinessConceptSchema(WSPutBusinessConceptSchema wsPutBusinessConceptSchema) throws RemoteException;

    public WSConceptKey getBusinessConceptKey(WSGetBusinessConceptKey wsGetBusinessConceptKey) throws RemoteException;

    public WSDataClusterPKArray getDataClusterPKs(WSRegexDataClusterPKs regexp) throws RemoteException;

    public WSDataCluster getDataCluster(WSGetDataCluster wsDataClusterPK) throws RemoteException;

    public WSBoolean existsDataCluster(WSExistsDataCluster wsExistsDataCluster) throws RemoteException;

    public WSBoolean existsDBDataCluster(WSExistsDBDataCluster wsExistsDBDataCluster) throws RemoteException;

    public WSDataClusterPK putDataCluster(WSPutDataCluster wsDataCluster) throws RemoteException;

    public WSBoolean putDBDataCluster(WSPutDBDataCluster wsDataCluster) throws RemoteException;

    public WSDataClusterPK deleteDataCluster(WSDeleteDataCluster wsDeleteDataCluster) throws RemoteException;

    public WSStringArray getConceptsInDataCluster(WSGetConceptsInDataCluster wsGetConceptsInDataCluster) throws RemoteException;

    public WSConceptRevisionMap getConceptsInDataClusterWithRevisions(
            WSGetConceptsInDataClusterWithRevisions wsGetConceptsInDataClusterWithRevisions) throws RemoteException;

    public WSViewPKArray getViewPKs(WSGetViewPKs regexp) throws RemoteException;

    public WSView getView(WSGetView wsViewPK) throws RemoteException;

    public WSBoolean existsView(WSExistsView wsViewPK) throws RemoteException;

    public WSViewPK putView(WSPutView wsView) throws RemoteException;

    public WSViewPK deleteView(WSDeleteView wsViewDel) throws RemoteException;

    public WSString getBusinessConceptValue(WSGetBusinessConceptValue wsGetBusinessConceptValue) throws RemoteException;

    public WSStringArray getFullPathValues(WSGetFullPathValues wsGetFullPathValues) throws RemoteException;

    public WSItem getItem(WSGetItem wsGetItem) throws RemoteException;

    public WSBoolean existsItem(WSExistsItem wsExistsItem) throws RemoteException;

    public WSStringArray getItems(WSGetItems wsGetItems) throws RemoteException;

    public WSStringArray getItemsSort(WSGetItemsSort wsGetItemsSort) throws RemoteException;

    public WSItemPKsByCriteriaResponse getItemPKsByCriteria(WSGetItemPKsByCriteria wsGetItemPKsByCriteria) throws RemoteException;

    public WSItemPKsByCriteriaResponse getItemPKsByFullCriteria(WSGetItemPKsByFullCriteria wsGetItemPKsByFullCriteria)
            throws RemoteException;

    public WSString countItemsByCustomFKFilters(WSCountItemsByCustomFKFilters wsCountItemsByCustomFKFilters)
            throws RemoteException;

    public WSStringArray getItemsByCustomFKFilters(WSGetItemsByCustomFKFilters wsGetItemsByCustomFKFilters)
            throws RemoteException;

    public WSStringArray viewSearch(WSViewSearch wsViewSearch) throws RemoteException;

    public WSStringArray xPathsSearch(WSXPathsSearch wsXPathsSearch) throws RemoteException;

    public WSString count(WSCount wsCount) throws RemoteException;

    public WSStringArray quickSearch(WSQuickSearch wsQuickSearch) throws RemoteException;

    public WSItemPK putItem(WSPutItem wsPutItem) throws RemoteException;

    public WSItemPK updateItemMetadata(WSUpdateMetadataItem wsUpdateMetadataItem) throws RemoteException;

    public WSItemPK partialPutItem(WSPartialPutItem wsPartialPutItem) throws RemoteException;

    public WSItemPKArray putItemArray(WSPutItemArray wsPutItemArray) throws RemoteException;

    public WSItemPKArray putItemWithReportArray(WSPutItemWithReportArray wsPutItemWithReportArray) throws RemoteException;

    public WSItemPK putItemWithReport(WSPutItemWithReport wsPutItemWithReport) throws RemoteException;

    public WSItemPK putItemWithCustomReport(WSPutItemWithCustomReport wsPutItemWithCustomReport) throws RemoteException;

    public WSBoolean isItemModifiedByOther(WSIsItemModifiedByOther wsItem) throws RemoteException;

    public WSPipeline extractUsingTransformer(WSExtractUsingTransformer wsExtractUsingTransformer) throws RemoteException;

    public WSPipeline extractUsingTransformerThruView(WSExtractUsingTransformerThruView wsExtractUsingTransformerThruView)
            throws RemoteException;

    public WSItemPK deleteItem(WSDeleteItem wsDeleteItem) throws RemoteException;

    public WSString deleteItemWithReport(WSDeleteItemWithReport wsDeleteItem) throws RemoteException;

    public WSInt deleteItems(WSDeleteItems wsDeleteItems) throws RemoteException;

    public WSDroppedItemPK dropItem(WSDropItem wsDropItem) throws RemoteException;

    public WSStringArray runQuery(WSRunQuery wsRunQuery) throws RemoteException;

    public WSString serviceAction(WSServiceAction wsServiceAction) throws RemoteException;

    public WSString getServiceConfiguration(WSServiceGetConfiguration wsGetConfiguration) throws RemoteException;

    public WSString putServiceConfiguration(WSServicePutConfiguration wsPutConfiguration) throws RemoteException;

    public WSServicesList getServicesList(WSGetServicesList wsGetServicesList) throws RemoteException;

    public WSServiceGetDocument getServiceDocument(WSString serviceName) throws RemoteException;

    public WSStoredProcedure getStoredProcedure(WSGetStoredProcedure wsGetStoredProcedure) throws RemoteException;

    public WSBoolean existsStoredProcedure(WSExistsStoredProcedure wsExistsStoredProcedure) throws RemoteException;

    public WSStoredProcedurePKArray getStoredProcedurePKs(WSRegexStoredProcedure regex) throws RemoteException;

    public WSStoredProcedurePK putStoredProcedure(WSPutStoredProcedure wsStoredProcedure) throws RemoteException;

    public WSStoredProcedurePK deleteStoredProcedure(WSDeleteStoredProcedure wsStoredProcedureDelete) throws RemoteException;

    public WSStringArray executeStoredProcedure(WSExecuteStoredProcedure wsExecuteStoredProcedure) throws RemoteException;

    public WSTransformer getTransformer(WSGetTransformer wsGetTransformer) throws RemoteException;

    public WSBoolean existsTransformer(WSExistsTransformer wsExistsTransformer) throws RemoteException;

    public WSTransformerPKArray getTransformerPKs(WSGetTransformerPKs regex) throws RemoteException;

    public WSTransformerPK putTransformer(WSPutTransformer wsTransformer) throws RemoteException;

    public WSPipeline processBytesUsingTransformer(WSProcessBytesUsingTransformer wsProcessBytesUsingTransformer)
            throws RemoteException;

    public WSPipeline processFileUsingTransformer(WSProcessFileUsingTransformer wsProcessFileUsingTransformer)
            throws RemoteException;

    public WSBackgroundJobPK processBytesUsingTransformerAsBackgroundJob(
            WSProcessBytesUsingTransformerAsBackgroundJob wsProcessBytesUsingTransformerAsBackgroundJob) throws RemoteException;

    public WSBackgroundJobPK processFileUsingTransformerAsBackgroundJob(
            WSProcessFileUsingTransformerAsBackgroundJob wsProcessFileUsingTransformerAsBackgroundJob) throws RemoteException;

    public WSMenu getMenu(WSGetMenu wsGetMenu) throws RemoteException;

    public WSBoolean existsMenu(WSExistsMenu wsExistsMenu) throws RemoteException;

    public WSMenuPKArray getMenuPKs(WSGetMenuPKs regex) throws RemoteException;

    public WSMenuPK putMenu(WSPutMenu wsMenu) throws RemoteException;

    public WSMenuPK deleteMenu(WSDeleteMenu wsMenuDelete) throws RemoteException;

    public WSBackgroundJobPKArray findBackgroundJobPKs(WSFindBackgroundJobPKs status) throws RemoteException;

    public WSBackgroundJob getBackgroundJob(WSGetBackgroundJob wsGetBackgroundJob) throws RemoteException;

    public WSBackgroundJobPK putBackgroundJob(WSPutBackgroundJob wsPutBackgroundJob) throws RemoteException;

    public WSUniverse getCurrentUniverse(WSGetCurrentUniverse wsGetCurrentUniverse) throws RemoteException;

    public WSItemPK recoverDroppedItem(WSRecoverDroppedItem wsRecoverDroppedItem) throws RemoteException;

    public WSDroppedItemPKArray findAllDroppedItemsPKs(WSFindAllDroppedItemsPKs regex) throws RemoteException;

    public WSDroppedItem loadDroppedItem(WSLoadDroppedItem wsLoadDroppedItem) throws RemoteException;

    public WSDroppedItemPK removeDroppedItem(WSRemoveDroppedItem wsRemoveDroppedItem) throws RemoteException;

    public WSMDMConfig getMDMConfiguration() throws RemoteException;

    public WSCheckServiceConfigResponse checkServiceConfiguration(WSCheckServiceConfigRequest serviceName) throws RemoteException;

    public WSRoutingRulePKArray getRoutingRulePKs(WSGetRoutingRulePKs regexp) throws RemoteException;

    public WSRoutingRule getRoutingRule(WSGetRoutingRule wsRoutingRulePK) throws RemoteException;

    public WSBoolean existsRoutingRule(WSExistsRoutingRule wsExistsRoutingRule) throws RemoteException;

    public WSRoutingRulePK putRoutingRule(WSPutRoutingRule wsRoutingRule) throws RemoteException;

    public WSRoutingRulePK deleteRoutingRule(WSDeleteRoutingRule wsRoutingRuleDel) throws RemoteException;

    public WSTransformerV2 getTransformerV2(WSGetTransformerV2 wsGetTransformerV2) throws RemoteException;

    public WSBoolean existsTransformerV2(WSExistsTransformerV2 wsExistsTransformerV2) throws RemoteException;

    public WSTransformerV2PKArray getTransformerV2PKs(WSGetTransformerV2PKs regex) throws RemoteException;

    public WSTransformerV2PK putTransformerV2(WSPutTransformerV2 wsTransformerV2) throws RemoteException;

    public WSTransformerV2PK deleteTransformerV2(WSDeleteTransformerV2 wsDeleteTransformerV2) throws RemoteException;

    public WSTransformerContext executeTransformerV2(WSExecuteTransformerV2 wsExecuteTransformerV2) throws RemoteException;

    public WSBackgroundJobPK executeTransformerV2AsJob(WSExecuteTransformerV2AsJob wsExecuteTransformerV2AsJob)
            throws RemoteException;

    public WSTransformerContext extractThroughTransformerV2(WSExtractThroughTransformerV2 wsExtractThroughTransformerV2)
            throws RemoteException;

    public WSBoolean existsTransformerPluginV2(WSExistsTransformerPluginV2 wsExistsTransformerPluginV2) throws RemoteException;

    public WSString getTransformerPluginV2Configuration(WSTransformerPluginV2GetConfiguration wsGetConfiguration)
            throws RemoteException;

    public WSString putTransformerPluginV2Configuration(WSTransformerPluginV2PutConfiguration wsPutConfiguration)
            throws RemoteException;

    public WSTransformerPluginV2Details getTransformerPluginV2Details(
            WSGetTransformerPluginV2Details wsGetTransformerPluginV2Details) throws RemoteException;

    public WSTransformerPluginV2SList getTransformerPluginV2SList(WSGetTransformerPluginV2SList wsGetTransformerPluginV2SList)
            throws RemoteException;

    public WSRoutingOrderV2 getRoutingOrderV2(WSGetRoutingOrderV2 wsGetRoutingOrderV2) throws RemoteException;

    public WSRoutingOrderV2 existsRoutingOrderV2(WSExistsRoutingOrderV2 wsExistsRoutingOrder) throws RemoteException;

    public WSRoutingOrderV2PK deleteRoutingOrderV2(WSDeleteRoutingOrderV2 wsDeleteRoutingOrder) throws RemoteException;

    public WSRoutingOrderV2PK executeRoutingOrderV2Asynchronously(
            WSExecuteRoutingOrderV2Asynchronously wsExecuteRoutingOrderAsynchronously) throws RemoteException;

    public WSString executeRoutingOrderV2Synchronously(WSExecuteRoutingOrderV2Synchronously wsExecuteRoutingOrderSynchronously)
            throws RemoteException;

    public WSRoutingOrderV2PKArray getRoutingOrderV2PKsByCriteria(
            WSGetRoutingOrderV2PKsByCriteria wsGetRoutingOrderV2PKsByCriteria) throws RemoteException;

    public WSRoutingOrderV2Array getRoutingOrderV2SByCriteria(WSGetRoutingOrderV2SByCriteria wsGetRoutingOrderV2SByCriteria)
            throws RemoteException;

    public WSRoutingOrderV2Array getRoutingOrderV2ByCriteriaWithPaging(
            WSGetRoutingOrderV2ByCriteriaWithPaging wsGetRoutingOrderV2ByCriteriaWithPaging) throws RemoteException;

    public WSRoutingRulePKArray routeItemV2(WSRouteItemV2 wsRouteItem) throws RemoteException;

    public WSRoutingEngineV2Status routingEngineV2Action(WSRoutingEngineV2Action wsRoutingEngineAction) throws RemoteException;

    public WSMDMJobArray getMDMJob(WSMDMNULL mdmJobRequest) throws RemoteException;

    public WSBoolean putMDMJob(WSPUTMDMJob putMDMJobRequest) throws RemoteException;

    public WSBoolean deleteMDMJob(WSDELMDMJob deleteMDMJobRequest) throws RemoteException;

    public WSCategoryData getMDMCategory(WSCategoryData wsCategoryDataRequest) throws RemoteException;

    public WSAutoIncrement getAutoIncrement(WSAutoIncrement wsAutoIncrementRequest) throws RemoteException;

    public WSDigest getDigest(WSDigestKey wsDigestKey) throws RemoteException;

    public WSLong updateDigest(WSDigest wsDigest) throws RemoteException;

    public WSRole getRole(WSGetRole wsGetRole) throws RemoteException;

    public WSBoolean isPagingAccurate(WSInt wsInt) throws RemoteException;

    public WSBoolean supportStaging(WSDataClusterPK dataClusterPK) throws RemoteException;

    public WSUniversePKArray getUniversePKs(WSGetUniversePKs wsGetUniversePKs) throws RemoteException;

    public FKIntegrityCheckResult checkFKIntegrity(WSDeleteItem deleteItem) throws RemoteException;

    public WSRolePKArray getRolePKs(WSGetRolePKs ks) throws RemoteException;
}
