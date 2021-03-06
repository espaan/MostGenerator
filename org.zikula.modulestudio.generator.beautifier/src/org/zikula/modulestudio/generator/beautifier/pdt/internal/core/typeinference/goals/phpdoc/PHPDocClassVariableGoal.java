package org.zikula.modulestudio.generator.beautifier.pdt.internal.core.typeinference.goals.phpdoc;

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
 * Based on package org.eclipse.php.internal.core.typeinference.goals.phpdoc;
 * 
 *******************************************************************************/

import org.eclipse.dltk.ti.goals.AbstractTypeGoal;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.typeinference.context.TypeContext;

public class PHPDocClassVariableGoal extends AbstractTypeGoal {

    private final String variableName;

    public PHPDocClassVariableGoal(TypeContext context, String variableName) {
        super(context);
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((variableName == null) ? 0 : variableName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PHPDocClassVariableGoal other = (PHPDocClassVariableGoal) obj;
        if (variableName == null) {
            if (other.variableName != null) {
                return false;
            }
        }
        else if (!variableName.equals(other.variableName)) {
            return false;
        }
        return true;
    }
}
