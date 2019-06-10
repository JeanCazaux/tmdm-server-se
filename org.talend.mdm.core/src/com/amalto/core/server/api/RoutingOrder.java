/*
 * Copyright (C) 2006-2019 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement along with this program; if not, write to Talend SA 9 rue Pages
 * 92150 Suresnes, France
 */
/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.server.api;

import com.amalto.core.objects.routing.AbstractRoutingOrderV2POJO;
import com.amalto.core.objects.routing.AbstractRoutingOrderV2POJOPK;
import com.amalto.core.util.XtentisException;

public interface RoutingOrder {

    /**
     * Executes a Routing Order in default DELAY milliseconds
     *
     * @throws com.amalto.core.util.XtentisException
     */
    public String executeRoutingOrder(AbstractRoutingOrderV2POJO routingOrderPOJO) throws XtentisException;

    /**
     * Get Routing Order
     *
     * @throws com.amalto.core.util.XtentisException
     */
    public AbstractRoutingOrderV2POJO getRoutingOrder(AbstractRoutingOrderV2POJOPK pk) throws XtentisException;

    /**
     * Remove an item
     *
     * @throws com.amalto.core.util.XtentisException
     */
    public AbstractRoutingOrderV2POJOPK removeRoutingOrder(AbstractRoutingOrderV2POJOPK pk)
            throws com.amalto.core.util.XtentisException;

    /**
     * Get a RoutingOrder knowing its class - no exception is thrown: returns null if not found
     *
     * @throws com.amalto.core.util.XtentisException
     */
    public AbstractRoutingOrderV2POJO existsRoutingOrder(AbstractRoutingOrderV2POJOPK pk) throws XtentisException;

    /**
     * Retrieve all Completed Routing Order PKs
     *
     * @throws com.amalto.core.util.XtentisException
     */
    public java.util.Collection getCompletedRoutingOrderPKs(String regex) throws XtentisException;

    /**
     * Retrieve all Failed Routing Order PKs
     *
     * @throws com.amalto.core.util.XtentisException
     */
    public java.util.Collection getFailedRoutingOrderPKs(String regex) throws XtentisException;

    /**
     * Retrieve all RoutingOrder PKs whatever the class
     *
     * @throws com.amalto.core.util.XtentisException
     */
    public java.util.Collection getAllRoutingOrderPKs(String regex) throws XtentisException;

    /**
     * Retrieve all RoutingOrder PKs by CriteriaWithPaging
     *
     * @throws com.amalto.core.util.XtentisException
     */
    public java.util.Collection getRoutingOrderPKsByCriteriaWithPaging(
            Class<? extends AbstractRoutingOrderV2POJO> routingOrderV2POJOClass, String anyFieldContains, String name,
            long timeCreatedMin, long timeCreatedMax, long timeScheduledMin, long timeScheduledMax, long timeLastRunStartedMin,
            long timeLastRunStartedMax, long timeLastRunCompletedMin, long timeLastRunCompletedMax, String itemConceptContains,
            String itemIDsContain, String serviceJNDIContains, String serviceParametersContains, String messageContains,
            int start, int limit, boolean withTotalCount) throws XtentisException;

    /**
     * Retrieve all RoutingOrder PKs by Criteria
     *
     * @throws com.amalto.core.util.XtentisException
     */
    public java.util.Collection getRoutingOrderPKsByCriteria(Class<? extends AbstractRoutingOrderV2POJO> routingOrderV2POJOClass,
            String anyFieldContains, String name, long timeCreatedMin, long timeCreatedMax, long timeScheduledMin,
            long timeScheduledMax, long timeLastRunStartedMin, long timeLastRunStartedMax, long timeLastRunCompletedMin,
            long timeLastRunCompletedMax, String itemConceptContains, String itemIDsContain, String serviceJNDIContains,
            String serviceParametersContains, String messageContains) throws XtentisException;

}
