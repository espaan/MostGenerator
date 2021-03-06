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
import org.eclipse.dltk.utils.CorePrinter;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.compiler.ast.visitor.ASTPrintVisitor;

/**
 * Represents a postfix expression
 * 
 * <pre>e.g.
 * 
 * <pre>
 * $a++,
 * foo()--
 */
public class PostfixExpression extends Expression {

    // '++'
    public static final int OP_INC = 0;
    // '--'
    public static final int OP_DEC = 1;

    private final Expression variable;
    private final int operator;

    public PostfixExpression(int start, int end, Expression variable,
            int operator) {
        super(start, end);

        assert variable != null;

        this.variable = variable;
        this.operator = operator;
    }

    @Override
    public void traverse(ASTVisitor visitor) throws Exception {
        final boolean visit = visitor.visit(this);
        if (visit) {
            variable.traverse(visitor);
        }
        visitor.endvisit(this);
    }

    @Override
    public String getOperator() {
        switch (getOperatorType()) {
            case OP_DEC:
                return "--"; //$NON-NLS-1$
            case OP_INC:
                return "++"; //$NON-NLS-1$
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getKind() {
        return ASTNodeKinds.POSTFIX_EXPRESSION;
    }

    public int getOperatorType() {
        return operator;
    }

    public Expression getVariable() {
        return variable;
    }

    /**
     * We don't print anything - we use {@link ASTPrintVisitor} instead
     */
    @Override
    public final void printNode(CorePrinter output) {
    }

    @Override
    public String toString() {
        return ASTPrintVisitor.toXMLString(this);
    }
}
