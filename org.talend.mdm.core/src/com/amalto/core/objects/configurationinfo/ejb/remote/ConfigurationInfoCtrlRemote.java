 /*
 * Generated by XDoclet - Do not edit!
 * this class was prodiuced by xdoclet automagically...
 */
package com.amalto.core.objects.configurationinfo.ejb.remote;

import java.util.*;

/**
 * This class is remote adapter to ConfigurationInfoCtrl. It provides convenient way to access
 * facade session bean. Inverit from this class to provide reasonable caching and event handling capabilities.
 *
 * Remote facade for ConfigurationInfoCtrl.
 * @xdoclet-generated at 28-09-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */

public class ConfigurationInfoCtrlRemote extends Observable
{
    static ConfigurationInfoCtrlRemote _instance = null;
    public static ConfigurationInfoCtrlRemote getInstance() {
        if(_instance == null) {
	   _instance = new ConfigurationInfoCtrlRemote();
	}
	return _instance;
    }

  /**
   * cached remote session interface
   */
  com.amalto.core.objects.configurationinfo.ejb.remote.ConfigurationInfoCtrl _session = null;
  /**
   * return session bean remote interface
   */
   protected com.amalto.core.objects.configurationinfo.ejb.remote.ConfigurationInfoCtrl getSession() {
      try {
   	if(_session == null) {
	   _session = com.amalto.core.objects.configurationinfo.ejb.local.ConfigurationInfoCtrlUtil.getHome().create();
	}
	return _session;
      } catch(Exception ex) {
        // just catch it here and return null.
        // somebody can provide better solution
	ex.printStackTrace();
	return null;
      }
   }

   public com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJOPK putConfigurationInfo ( com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJO configurationInfo )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJOPK retval;
       retval =  getSession().putConfigurationInfo( configurationInfo );

      return retval;

   }

   public com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJO getConfigurationInfo ( com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJO retval;
       retval =  getSession().getConfigurationInfo( pk );

      return retval;

   }

   public com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJO existsConfigurationInfo ( com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJO retval;
       retval =  getSession().existsConfigurationInfo( pk );

      return retval;

   }

   public com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJOPK removeConfigurationInfo ( com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJOPK retval;
       retval =  getSession().removeConfigurationInfo( pk );

      return retval;

   }

   public java.util.Collection getConfigurationInfoPKs ( java.lang.String regex )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.Collection retval;
       retval =  getSession().getConfigurationInfoPKs( regex );

      return retval;

   }

   public void autoUpgrade (  )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
      getSession().autoUpgrade(  );

   }

   public void autoUpgradeInBackground (  )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
      getSession().autoUpgradeInBackground(  );

   }

  /**
   * override this method to provide feedback to interested objects
   * in case collections were changed.
   */
  public void invalidate() {

  	setChanged();
	notifyObservers();
  }
}
