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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.dltk.ti.GoalState;
import org.eclipse.dltk.ti.goals.ExpressionTypeGoal;
import org.eclipse.dltk.ti.goals.GoalEvaluator;
import org.eclipse.dltk.ti.goals.IGoal;
import org.eclipse.dltk.ti.types.IEvaluatedType;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.compiler.ast.nodes.ArrayCreation;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.compiler.ast.nodes.ArrayElement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.typeinference.PHPTypeInferenceUtils;

public class ArrayCreationEvaluator extends GoalEvaluator {

    private final List<IEvaluatedType> evaluated = new LinkedList<IEvaluatedType>();

    public ArrayCreationEvaluator(IGoal goal) {
        super(goal);
    }

    @Override
    public IGoal[] init() {
        final ExpressionTypeGoal typedGoal = (ExpressionTypeGoal) goal;
        final ArrayCreation arrayCreation = (ArrayCreation) typedGoal
                .getExpression();

        final List<IGoal> subGoals = new LinkedList<IGoal>();
        for (final ArrayElement arrayElement : arrayCreation.getElements()) {
            subGoals.add(new ExpressionTypeGoal(typedGoal.getContext(),
                    arrayElement.getValue()));
        }
        return subGoals.toArray(new IGoal[subGoals.size()]);
    }

    @Override
    public Object produceResult() {
        return PHPTypeInferenceUtils.combineMultiType(evaluated);
    }

    @Override
    public IGoal[] subGoalDone(IGoal subgoal, Object result, GoalState state) {
        if (state != GoalState.RECURSIVE) {
            evaluated.add((IEvaluatedType) result);
        }
        return IGoal.NO_GOALS;
    }
}
