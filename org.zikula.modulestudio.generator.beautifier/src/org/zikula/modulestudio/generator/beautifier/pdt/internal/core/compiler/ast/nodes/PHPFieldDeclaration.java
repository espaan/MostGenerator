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
import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.VariableReference;
import org.eclipse.dltk.utils.CorePrinter;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.compiler.ast.visitor.ASTPrintVisitor;

public class PHPFieldDeclaration extends FieldDeclaration implements
        IPHPDocAwareDeclaration {

    private final int declStart;
    private final Expression initializer;
    private final PHPDocBlock phpDoc;

    public PHPFieldDeclaration(VariableReference variable,
            Expression initializer, int start, int end, int modifiers,
            int declStart, PHPDocBlock phpDoc) {
        super(variable.getName(), variable.sourceStart(), variable.sourceEnd(),
                start, end);

        if ((modifiers & Modifiers.AccPrivate) == 0
                && (modifiers & Modifiers.AccProtected) == 0) {
            modifiers |= Modifiers.AccPublic;
        }
        setModifiers(modifiers);

        this.declStart = declStart;
        this.initializer = initializer;
        this.phpDoc = phpDoc;
    }

    @Override
    public PHPDocBlock getPHPDoc() {
        return phpDoc;
    }

    @Override
    public void traverse(ASTVisitor visitor) throws Exception {
        final boolean visit = visitor.visit(this);
        if (visit) {
            getRef().traverse(visitor);
            if (initializer != null) {
                initializer.traverse(visitor);
            }
        }
        visitor.endvisit(this);
    }

    @Override
    public int getKind() {
        return ASTNodeKinds.FIELD_DECLARATION;
    }

    public Expression getVariableValue() {
        return initializer;
    }

    public int getDeclarationStart() {
        return declStart;
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
