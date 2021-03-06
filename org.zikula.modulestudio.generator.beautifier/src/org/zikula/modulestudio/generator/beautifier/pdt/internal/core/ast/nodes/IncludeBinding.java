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

import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.filenetwork.FileNetworkUtility;

/**
 * @see IIncludeBinding
 * @author Roy 2008
 */
public class IncludeBinding implements IIncludeBinding {

    private final ISourceModule model;
    private final String name;
    private final ISourceModule includedSourceModule;

    public IncludeBinding(ISourceModule model, Include includeDeclaration) {
        super();
        final String scalars = ASTNodes.getScalars(includeDeclaration
                .getExpression());
        this.model = model;
        this.name = scalars.replace("\'", "").replace("\"", "");
        this.includedSourceModule = FileNetworkUtility.findSourceModule(
                this.model, this.name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getNameComponents() {
        return null;
    }

    @Override
    public String getKey() {
        return model.getHandleIdentifier() + "#" + name;
    }

    @Override
    public int getKind() {
        return IBinding.INCLUDE;
    }

    @Override
    public int getModifiers() {
        return 0;
    }

    /**
     * TODO handle dirname(__FILE__) or other expressions
     */
    @Override
    public IModelElement getPHPElement() {
        return this.includedSourceModule;
    }

    @Override
    public boolean isDeprecated() {
        return false;
    }
}
