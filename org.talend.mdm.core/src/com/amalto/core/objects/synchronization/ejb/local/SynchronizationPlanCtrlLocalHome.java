/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.objects.synchronization.ejb.local;

/**
 * Local home interface for SynchronizationPlanCtrl.
 * @xdoclet-generated at 28-09-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface SynchronizationPlanCtrlLocalHome
   extends javax.ejb.EJBLocalHome
{
   public static final String COMP_NAME="java:comp/env/ejb/SynchronizationPlanCtrlLocal";
   public static final String JNDI_NAME="amalto/local/core/synchronizationPlanctrl";

   public com.amalto.core.objects.synchronization.ejb.local.SynchronizationPlanCtrlLocal create()
      throws javax.ejb.CreateException;

}
