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
 * This interface is base for all the PHP variables including simple variable,
 * function invocation, list, dispatch, etc.
 */
public abstract class VariableBase extends Expression {

    public VariableBase(int start, int end, AST ast) {
        super(start, end, ast);
    }

    public VariableBase(AST ast) {
        super(ast);
    }

}
