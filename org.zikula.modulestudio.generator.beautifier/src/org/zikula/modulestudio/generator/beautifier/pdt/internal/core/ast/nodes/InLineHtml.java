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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.PHPVersion;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.match.ASTMatcher;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.visitor.Visitor;

/**
 * Represents an HTML blocks in the resource
 * 
 * <pre>e.g.
 * 
 * <pre>
 * <html> </html>
 * <html> <?php ?> </html> <?php ?>
 */
public class InLineHtml extends Statement {

    /**
     * A list of property descriptors (element type:
     * {@link StructuralPropertyDescriptor}), or null if uninitialized.
     */
    private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;

    static {
        final List<StructuralPropertyDescriptor> properyList = new ArrayList<StructuralPropertyDescriptor>(
                0);
        PROPERTY_DESCRIPTORS = Collections.unmodifiableList(properyList);
    }

    public InLineHtml(int start, int end, AST ast) {
        super(start, end, ast);
    }

    public InLineHtml(AST ast) {
        super(ast);
    }

    @Override
    public void accept0(Visitor visitor) {
        final boolean visit = visitor.visit(this);
        if (visit) {
            childrenAccept(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    public void childrenAccept(Visitor visitor) {
        // no children
    }

    @Override
    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    @Override
    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    @Override
    public void toString(StringBuffer buffer, String tab) {
        buffer.append(tab).append("<InLineHtml"); //$NON-NLS-1$
        appendInterval(buffer);
        buffer.append("/>"); //$NON-NLS-1$
    }

    @Override
    public int getType() {
        return ASTNode.IN_LINE_HTML;
    }

    /*
     * Method declared on ASTNode.
     */
    @Override
    public boolean subtreeMatch(ASTMatcher matcher, Object other) {
        // dispatch to correct overloaded match method
        return matcher.match(this, other);
    }

    @Override
    ASTNode clone0(AST target) {
        final ASTNode result = new InLineHtml(this.getStart(), this.getEnd(),
                target);
        return result;
    }

    @Override
    List<StructuralPropertyDescriptor> internalStructuralPropertiesForType(
            PHPVersion apiLevel) {
        return PROPERTY_DESCRIPTORS;
    }
}
