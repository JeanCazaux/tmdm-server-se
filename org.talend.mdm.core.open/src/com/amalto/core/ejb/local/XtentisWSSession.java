/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.ejb.local;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;

public class XtentisWSSession extends XtentisWSBean implements javax.ejb.SessionBean {

    @Override
    public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
        // do nothing
    }

    @Override
    public void ejbRemove() throws EJBException, RemoteException {
        // do nothing
    }

    @Override
    public void ejbActivate() throws EJBException, RemoteException {
        // do nothing
    }

    @Override
    public void ejbPassivate() throws EJBException, RemoteException {
        // do nothing
    }

    /**
     * Default create method
     *
     * @throws javax.ejb.CreateException
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {
    }

}
