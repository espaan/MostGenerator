package org.zikula.modulestudio.generator.beautifier.pdt.internal.core.compiler.ast.nodes;

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
 * Based on package org.eclipse.php.internal.core.compiler.ast.nodes;
 * 
 *******************************************************************************/

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.ConstantReference;

/**
 * Represnts a constant class access
 * 
 * <pre>e.g.
 * 
 * <pre> MyClass::CONST
 */
public class StaticConstantAccess extends StaticDispatch {

    private final ConstantReference constant;

    public StaticConstantAccess(int start, int end, Expression dispatcher,
            ConstantReference constant) {
        super(start, end, dispatcher);

        assert constant != null;
        this.constant = constant;
    }

    @Override
    public int getKind() {
        return ASTNodeKinds.STATIC_CONSTANT_ACCESS;
    }

    @Override
    public void traverse(ASTVisitor visitor) throws Exception {
        final boolean visit = visitor.visit(this);
        if (visit) {
            getDispatcher().traverse(visitor);
            constant.traverse(visitor);
        }
        visitor.endvisit(this);
    }

    public ConstantReference getConstant() {
        return constant;
    }
}
