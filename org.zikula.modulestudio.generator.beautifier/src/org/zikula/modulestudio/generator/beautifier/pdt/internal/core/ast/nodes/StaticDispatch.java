package org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes;

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
 * Based on package org.eclipse.php.internal.core.ast.nodes;
 * 
 *******************************************************************************/

/**
 * base class for all the static access
 */
public abstract class StaticDispatch extends VariableBase {

    private Expression className;

    /**
     * The structural property of this node type.
     */
    abstract ChildPropertyDescriptor getClassNameProperty();

    public StaticDispatch(int start, int end, AST ast, Expression className) {
        super(start, end, ast);

        if (className == null) {
            throw new IllegalArgumentException();
        }
        setClassName(className);
    }

    public StaticDispatch(AST ast) {
        super(ast);
    }

    public Expression getClassName() {
        return className;
    }

    /**
     * Sets the class name expression
     * 
     * @param name
     *            the class name of this dispatch
     * @exception IllegalArgumentException
     *                if:
     *                <ul>
     *                <li>the node belongs to a different AST</li>
     *                <li>the node already has a parent</li>
     *                <li>a cycle in would be created</li>
     *                </ul>
     */
    public void setClassName(Expression name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        // an Assignment may occur inside a Expression - must check cycles
        final ASTNode oldChild = this.className;
        preReplaceChild(oldChild, name, getClassNameProperty());
        this.className = name;
        postReplaceChild(oldChild, name, getClassNameProperty());
    }

    @Override
    ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property,
            boolean get, ASTNode child) {
        if (property == getClassNameProperty()) {
            if (get) {
                return getClassName();
            }
            setClassName((Expression) child);
            return null;
        }
        // allow default implementation to flag the error
        return super.internalGetSetChildProperty(property, get, child);
    }

    public abstract ASTNode getMember();
}
