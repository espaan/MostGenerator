package org.zikula.modulestudio.generator.beautifier.pdt.internal.core.typeinference.evaluators;

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
 * Based on package org.eclipse.php.internal.core.typeinference.evaluators;
 * 
 *******************************************************************************/

import org.eclipse.dltk.ti.GoalState;
import org.eclipse.dltk.ti.goals.ExpressionTypeGoal;
import org.eclipse.dltk.ti.goals.GoalEvaluator;
import org.eclipse.dltk.ti.goals.IGoal;
import org.eclipse.dltk.ti.types.IEvaluatedType;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.compiler.ast.nodes.PrefixExpression;

public class PrefixExpressionEvaluator extends GoalEvaluator {

    private IEvaluatedType result;

    public PrefixExpressionEvaluator(IGoal goal) {
        super(goal);
    }

    @Override
    public IGoal[] init() {
        final ExpressionTypeGoal typedGoal = (ExpressionTypeGoal) goal;
        final PrefixExpression prefixExpression = (PrefixExpression) typedGoal
                .getExpression();

        // XXX: actually, we have to check the contents of the variable here,
        // since for example typeof(++"abc")=string, but typeof(++"123")=integer
        // ...
        return new IGoal[] { new ExpressionTypeGoal(goal.getContext(),
                prefixExpression.getVariable()) };
    }

    @Override
    public IGoal[] subGoalDone(IGoal subgoal, Object result, GoalState state) {
        this.result = (IEvaluatedType) result;
        return IGoal.NO_GOALS;
    }

    @Override
    public Object produceResult() {
        return result;
    }
}
