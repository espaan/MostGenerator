package org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.rewrite;

/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation Zend
 * Technologies
 * 
 * 
 * 
 * Based on package org.eclipse.php.internal.core.ast.rewrite;
 * 
 *******************************************************************************/

/**
 *
 */
public class NodeRewriteEvent extends RewriteEvent {

    private final Object originalValue;
    private Object newValue;

    public NodeRewriteEvent(Object originalValue, Object newValue) {
        this.originalValue = originalValue;
        this.newValue = newValue;
    }

    /**
     * @return Returns the new value.
     */
    @Override
    public Object getNewValue() {
        return this.newValue;
    }

    /**
     * @return Returns the original value.
     */
    @Override
    public Object getOriginalValue() {
        return this.originalValue;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jdt.internal.corext.dom.RewriteEvent#getChangeKind()
     */
    @Override
    public int getChangeKind() {
        if (this.originalValue == this.newValue) {
            return UNCHANGED;
        }
        if (this.originalValue == null) {
            return INSERTED;
        }
        if (this.newValue == null) {
            return REMOVED;
        }
        if (this.originalValue.equals(this.newValue)) {
            return UNCHANGED;
        }
        return REPLACED;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jdt.internal.corext.dom.RewriteEvent#isListRewrite()
     */
    @Override
    public boolean isListRewrite() {
        return false;
    }

    /*
     * Sets a new value for the new node. Internal access only.
     * @param newValue The new value to set.
     */
    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jdt.internal.corext.dom.RewriteEvent#getChildren()
     */
    @Override
    public RewriteEvent[] getChildren() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        switch (getChangeKind()) {
            case INSERTED:
                buf.append(" [inserted: "); //$NON-NLS-1$
                buf.append(getNewValue());
                buf.append(']');
                break;
            case REPLACED:
                buf.append(" [replaced: "); //$NON-NLS-1$
                buf.append(getOriginalValue());
                buf.append(" -> "); //$NON-NLS-1$
                buf.append(getNewValue());
                buf.append(']');
                break;
            case REMOVED:
                buf.append(" [removed: "); //$NON-NLS-1$
                buf.append(getOriginalValue());
                buf.append(']');
                break;
            default:
                buf.append(" [unchanged]"); //$NON-NLS-1$
        }
        return buf.toString();
    }

}
