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

package com.amalto.core.history;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.*;

import java.util.Stack;

/**
 * A {@link com.amalto.core.history.DocumentTransformer} that marks the {@link com.amalto.core.history.Document} elements with unique identifiers that depends on their
 * position in the document.
 * For instance a document such as:
 * <p>
 * <code>
 * &lt;root&gt;<br/>
 * &lt;field1 cls="tree-node-different"&gt;<br/>
 * &lt;field&gt;newValue1&lt;/field&gt;<br/>
 * &lt;/field1&gt;<br/>
 * &lt;field2 cls="tree-node-different"&gt;<br/>
 * &lt;field&gt;newValue2&lt;/field&gt;<br/>
 * &lt;/field2&gt;<br/>
 * &lt;field3 cls="tree-node-different"&gt;<br/>
 * &lt;field&gt;newValue3&lt;/field&gt;<br/>
 * &lt;/field3&gt;<br/>
 * &lt;/root&gt;<br/>
 * </code>
 * </p>
 * Would be transformed into:
 * <p>
 * <code>
 * &lt;root id="root-1"&gt;<br/>
 * &lt;field1 cls="tree-node-different" id="1-field1-1"&gt;<br/>
 * &lt;field id="11-field-1"&gt;newValue1&lt;/field&gt;<br/>
 * &lt;/field1&gt;<br/>
 * &lt;field2 cls="tree-node-different" id="1-field2-2"&gt;<br/>
 * &lt;field id="12-field-1"&gt;newValue2&lt;/field&gt;<br/>
 * &lt;/field2&gt;<br/>
 * &lt;field3 cls="tree-node-different" id="1-field3-3"&gt;<br/>
 * &lt;field id="13-field-1"&gt;newValue3&lt;/field&gt;<br/>
 * &lt;/field3&gt;<br/>
 * &lt;/root&gt;<br/>
 * </code>
 * </p>
 */
public class UniqueIdTransformer implements DocumentTransformer {

    public static final String ID_ATTRIBUTE_NAME = "id"; //$NON-NLS-1$

    public UniqueIdTransformer() {
    }

    public Document transform(MutableDocument document) {
        org.w3c.dom.Document domDocument = document.asDOM();
        addIds(domDocument);
        return document;
    }

    private void addIds(org.w3c.dom.Document document) {
        Stack<Integer> levels = new Stack<Integer>();
        levels.push(0);
        {
            Element documentElement = document.getDocumentElement();
            if (documentElement == null) {
                throw new IllegalStateException("Record history is empty.");
            }
            _addIds(document, documentElement, levels);
        }
        levels.pop();
    }

    private void _addIds(org.w3c.dom.Document document, Node node, Stack<Integer> levels) {
        NamedNodeMap attributes = node.getAttributes();
        Attr id = document.createAttribute(ID_ATTRIBUTE_NAME);

        int thisElementId = levels.pop() + 1;
        StringBuilder builder;
        {
            builder = new StringBuilder();
            for (Integer level : levels) {
                builder.append(level);
            }
        }
        String prefix = builder.toString().isEmpty() ? StringUtils.EMPTY : builder.toString() + '-';
        id.setValue(prefix + node.getNodeName() + '-' + thisElementId);
        attributes.setNamedItem(id);

        levels.push(thisElementId);
        {
            levels.push(0);
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child instanceof Element) {
                    _addIds(document, child, levels);
                }
            }
            levels.pop();
        }
    }

}
