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

import java.util.Collection;
import java.util.List;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.utils.CorePrinter;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.compiler.ast.visitor.ASTPrintVisitor;

/**
 * Represents the static statement
 * 
 * <pre>e.g.
 * 
 * <pre>
 * static $a
 * static $a, $b=5;
 */
public class StaticStatement extends Statement {

    private final List<? extends Expression> expressions;

    public StaticStatement(int start, int end,
            List<? extends Expression> expressions) {
        super(start, end);

        assert expressions != null;
        this.expressions = expressions;
    }

    @Override
    public void traverse(ASTVisitor visitor) throws Exception {
        final boolean visit = visitor.visit(this);
        if (visit) {
            for (final Expression expression : expressions) {
                expression.traverse(visitor);
            }
        }
        visitor.endvisit(this);
    }

    @Override
    public int getKind() {
        return ASTNodeKinds.STATIC_STATEMENT;
    }

    public Collection<? extends Expression> getExpressions() {
        return expressions;
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
