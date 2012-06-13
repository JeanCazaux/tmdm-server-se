/*
 * Copyright (C) 2006-2012 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement
 * along with this program; if not, write to Talend SA
 * 9 rue Pages 92150 Suresnes, France
 */

package com.amalto.core.history.accessor;

/**
 *
 */
public interface Accessor {

    /**
     * Set a value in the document. The full path of elements <b>must</b> exist (this can be ensured via a call
     * to {@link #create()} if needed.
     *
     * @param value The value to be set.
     * @see #create()
     * @see #exist()
     */
    void set(String value);

    /**
     * Get the value in the document. The full path of elements <b>must</b> exist (this can be ensured via a call
     * to {@link #create()} if needed.
     *
     * @return The value accessible via this accessor.
     * @see #create()
     * @see #exist()
     */
    String get();

    /**
     * Do not modify the accessed object but mark the accessed object in the {@link com.amalto.core.history.MutableDocument}.
     * @see {@link com.amalto.core.history.MutableDocument#getLastAccessedNode()}.
     */
    void touch();

    /**
     * Creates all the structure necessary to make {@link #set(String)} successful. It creates all
     * missing XML elements in the document.
     *
     * @see #exist()
     */
    void create();

    /**
     * Equivalent to consecutive calls of {@link #create()} and {@link #set(String)}, but implementation might provide
     * more efficient way to these two consecutive calls.
     * @param value The value to be set by the accessor.
     */
    void createAndSet(String value);
    
    /**
     * <p>
     * Deletes the underlying XML element of this accessor (might be an element or an attribute). This method does not
     * remove all intermediate elements (i.e. path1/path2/path3 will only remove path3 and not path2 nor path1).
     * </p>
     * <p>Implementation note: if element does not exist (i.e. {@link #exist()} returns <code>false</code>), implementations
     * <b>MUST</b> perform a no op.</p>
     */
    void delete();

    /**
     * @return Returns true if the field exists (i.e. the full path to the field exists). If it doesn't, a call to
     *         {@link #create()} should be able to create everything needed.
     */
    boolean exist();

    /**
     * <p>
     * Mark the underlying element as 'modified'. Modification marker may depends on the underlying element and the
     * implementation of {@link Accessor}.
     * </p>
     * <p>
     * Calling successively this method and then {@link #markUnmodified()} must be equivalent to a no op on the underlying
     * element.
     * </p>
     * <p>
     * This method is also a <i>no op</i> if {@link #exist()} returns <code>false</code>.
     * </p>
     *
     * @see #markUnmodified()
     */
    void markModified();

    /**
     * <p>
     * Mark the underlying element as 'not modified'. Modification marker may depends on the underlying element and the
     * implementation of {@link Accessor}.
     * </p>
     * <p>
     * This method is also a <i>no op</i> if {@link #exist()} returns <code>false</code>.
     * </p>
     *
     * @see #markModified()
     */
    void markUnmodified();

    /**
     * @return Number of elements contained by this accessor. If {@link #exist()} returns <code>false</code>, this method
     * must return <code>0</code>.
     */
    int size();

    /**
     * @return Returns actual type name (i.e. xsi:type attribute value) if present or empty string if not found.
     * Depending on actual implementation, this method might throw {@link UnsupportedOperationException}.
     */
    String getActualType();

}
