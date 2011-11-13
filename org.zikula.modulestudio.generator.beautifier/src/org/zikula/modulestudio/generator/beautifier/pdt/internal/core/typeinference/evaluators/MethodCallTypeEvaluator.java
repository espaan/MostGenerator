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

import java.util.Arrays;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.evaluation.types.UnknownType;
import org.eclipse.dltk.ti.GoalState;
import org.eclipse.dltk.ti.goals.ExpressionTypeGoal;
import org.eclipse.dltk.ti.goals.GoalEvaluator;
import org.eclipse.dltk.ti.goals.IGoal;
import org.eclipse.dltk.ti.types.IEvaluatedType;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.typeinference.PHPTypeInferenceUtils;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.typeinference.goals.MethodElementReturnTypeGoal;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.typeinference.goals.phpdoc.PHPDocMethodReturnTypeGoal;

public class MethodCallTypeEvaluator extends GoalEvaluator {

    private final static int STATE_INIT = 0;
    private final static int STATE_WAITING_RECEIVER = 1;
    private final static int STATE_GOT_RECEIVER = 2;
    private final static int STATE_WAITING_METHOD_PHPDOC = 3;
    private final static int STATE_WAITING_METHOD = 4;

    private int state = STATE_INIT;
    private IEvaluatedType receiverType;
    private IEvaluatedType result;

    public MethodCallTypeEvaluator(ExpressionTypeGoal goal) {
        super(goal);
    }

    private IGoal produceNextSubgoal(IGoal previousGoal,
            IEvaluatedType previousResult, GoalState goalState) {

        final ExpressionTypeGoal typedGoal = (ExpressionTypeGoal) goal;
        final CallExpression expression = (CallExpression) typedGoal
                .getExpression();

        // just starting to evaluate method, evaluate method receiver first:
        if (state == STATE_INIT) {
            final ASTNode receiver = expression.getReceiver();
            if (receiver == null) {
                state = STATE_GOT_RECEIVER;
            }
            else {
                state = STATE_WAITING_RECEIVER;
                return new ExpressionTypeGoal(goal.getContext(), receiver);
            }
        }

        // receiver must been evaluated now, lets evaluate method return type:
        if (state == STATE_WAITING_RECEIVER) {
            receiverType = previousResult;
            previousResult = null;
            if (receiverType == null) {
                return null;
            }
            state = STATE_GOT_RECEIVER;
        }

        // we've evaluated receiver, lets evaluate the method return type now
        // (using PHP Doc first):
        if (state == STATE_GOT_RECEIVER) {
            state = STATE_WAITING_METHOD_PHPDOC;
            return new PHPDocMethodReturnTypeGoal(typedGoal.getContext(),
                    receiverType, expression.getName());
        }

        // PHPDoc logic is done, start evaluating 'return' statements here:
        if (state == STATE_WAITING_METHOD_PHPDOC) {
            if (goalState != GoalState.PRUNED && previousResult != null
                    && previousResult != UnknownType.INSTANCE) {
                result = previousResult;
                previousResult = null;
            }
            state = STATE_WAITING_METHOD;
            return new MethodElementReturnTypeGoal(typedGoal.getContext(),
                    receiverType, expression.getName());
        }

        if (state == STATE_WAITING_METHOD) {
            if (goalState != GoalState.PRUNED && previousResult != null
                    && previousResult != UnknownType.INSTANCE) {
                if (result != null) {
                    result = PHPTypeInferenceUtils.combineTypes(Arrays
                            .asList(new IEvaluatedType[] { result,
                                    previousResult }));
                }
                else {
                    result = previousResult;
                    previousResult = null;
                }
            }
        }
        return null;
    }

    @Override
    public Object produceResult() {
        return result;
    }

    @Override
    public IGoal[] init() {
        final IGoal goal = produceNextSubgoal(null, null, null);
        if (goal != null) {
            return new IGoal[] { goal };
        }
        return IGoal.NO_GOALS;
    }

    @Override
    public IGoal[] subGoalDone(IGoal subgoal, Object result, GoalState state) {
        final IGoal goal = produceNextSubgoal(subgoal, (IEvaluatedType) result,
                state);
        if (goal != null) {
            return new IGoal[] { goal };
        }
        return IGoal.NO_GOALS;
    }

}