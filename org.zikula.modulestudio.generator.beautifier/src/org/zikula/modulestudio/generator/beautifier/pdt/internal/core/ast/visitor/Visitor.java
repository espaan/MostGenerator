package org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.visitor;

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
 * Based on package org.eclipse.php.internal.core.ast.visitor;
 * 
 *******************************************************************************/

import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ASTError;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ASTNode;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ArrayAccess;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ArrayCreation;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ArrayElement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.Assignment;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.BackTickExpression;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.Block;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.BreakStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.CastExpression;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.CatchClause;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ClassDeclaration;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ClassInstanceCreation;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ClassName;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.CloneExpression;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.Comment;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ConditionalExpression;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ConstantDeclaration;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ContinueStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.DeclareStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.DoStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.EchoStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.EmptyStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ExpressionStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.FieldAccess;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.FieldsDeclaration;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ForEachStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ForStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.FormalParameter;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.FunctionDeclaration;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.FunctionInvocation;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.FunctionName;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.GlobalStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.GotoLabel;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.GotoStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.Identifier;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.IfStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.IgnoreError;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.InLineHtml;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.Include;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.InfixExpression;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.InstanceOfExpression;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.InterfaceDeclaration;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.LambdaFunctionDeclaration;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ListVariable;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.MethodDeclaration;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.MethodInvocation;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.NamespaceDeclaration;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.NamespaceName;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ParenthesisExpression;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.PostfixExpression;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.PrefixExpression;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.Program;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.Quote;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.Reference;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ReflectionVariable;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ReturnStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.Scalar;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.SingleFieldDeclaration;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.StaticConstantAccess;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.StaticFieldAccess;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.StaticMethodInvocation;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.StaticStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.SwitchCase;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.SwitchStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.ThrowStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.TryStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.UnaryOperation;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.UseStatement;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.UseStatementPart;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.Variable;
import org.zikula.modulestudio.generator.beautifier.pdt.internal.core.ast.nodes.WhileStatement;

/**
 * A visitor for abstract syntax trees.
 * <p>
 * For each different concrete AST node type there is a method that visits the
 * given node to perform some arbitrary operation.
 * <p>
 * Subclasses may implement this method as needed.
 * <p>
 * 
 * @author Moshe S., Roy G. ,2007
 */
public interface Visitor {

    /**
     * Visits the given AST node prior to the type-specific visit. (before
     * <code>visit</code>).
     * <p>
     * The default implementation does nothing. Subclasses may reimplement.
     * </p>
     * 
     * @param node
     *            the node to visit
     */
    public void preVisit(ASTNode node);

    /**
     * Visits the given AST node following the type-specific visit (after
     * <code>endVisit</code>).
     * <p>
     * The default implementation does nothing. Subclasses may reimplement.
     * </p>
     * 
     * @param node
     *            the node to visit
     */
    public void postVisit(ASTNode node);

    public boolean visit(ArrayAccess arrayAccess);

    public void endVisit(ArrayAccess arrayAccess);

    public boolean visit(ArrayCreation arrayCreation);

    public void endVisit(ArrayCreation arrayCreation);

    public boolean visit(ArrayElement arrayElement);

    public void endVisit(ArrayElement arrayElement);

    public boolean visit(Assignment assignment);

    public void endVisit(Assignment assignment);

    public boolean visit(ASTError astError);

    public void endVisit(ASTError astError);

    public boolean visit(BackTickExpression backTickExpression);

    public void endVisit(BackTickExpression backTickExpression);

    public boolean visit(Block block);

    public void endVisit(Block block);

    public boolean visit(BreakStatement breakStatement);

    public void endVisit(BreakStatement breakStatement);

    public boolean visit(CastExpression castExpression);

    public void endVisit(CastExpression castExpression);

    public boolean visit(CatchClause catchClause);

    public void endVisit(CatchClause catchClause);

    public boolean visit(ConstantDeclaration classConstantDeclaration);

    public void endVisit(ConstantDeclaration classConstantDeclaration);

    public boolean visit(ClassDeclaration classDeclaration);

    public void endVisit(ClassDeclaration classDeclaration);

    public boolean visit(ClassInstanceCreation classInstanceCreation);

    public void endVisit(ClassInstanceCreation classInstanceCreation);

    public boolean visit(ClassName className);

    public void endVisit(ClassName className);

    public boolean visit(CloneExpression cloneExpression);

    public void endVisit(CloneExpression cloneExpression);

    public boolean visit(Comment comment);

    public void endVisit(Comment comment);

    public boolean visit(ConditionalExpression conditionalExpression);

    public void endVisit(ConditionalExpression conditionalExpression);

    public boolean visit(ContinueStatement continueStatement);

    public void endVisit(ContinueStatement continueStatement);

    public boolean visit(DeclareStatement declareStatement);

    public void endVisit(DeclareStatement declareStatement);

    public boolean visit(DoStatement doStatement);

    public void endVisit(DoStatement doStatement);

    public boolean visit(EchoStatement echoStatement);

    public void endVisit(EchoStatement echoStatement);

    public boolean visit(EmptyStatement emptyStatement);

    public void endVisit(EmptyStatement emptyStatement);

    public boolean visit(ExpressionStatement expressionStatement);

    public void endVisit(ExpressionStatement expressionStatement);

    public boolean visit(FieldAccess fieldAccess);

    public void endVisit(FieldAccess fieldAccess);

    public boolean visit(FieldsDeclaration fieldsDeclaration);

    public void endVisit(FieldsDeclaration fieldsDeclaration);

    public boolean visit(ForEachStatement forEachStatement);

    public void endVisit(ForEachStatement forEachStatement);

    public boolean visit(FormalParameter formalParameter);

    public void endVisit(FormalParameter formalParameter);

    public boolean visit(ForStatement forStatement);

    public void endVisit(ForStatement forStatement);

    public boolean visit(FunctionDeclaration functionDeclaration);

    public void endVisit(FunctionDeclaration functionDeclaration);

    public boolean visit(FunctionInvocation functionInvocation);

    public void endVisit(FunctionInvocation functionInvocation);

    public boolean visit(FunctionName functionName);

    public void endVisit(FunctionName functionName);

    public boolean visit(GlobalStatement globalStatement);

    public void endVisit(GlobalStatement globalStatement);

    public boolean visit(GotoLabel gotoLabel);

    public void endVisit(GotoLabel gotoLabel);

    public boolean visit(GotoStatement gotoStatement);

    public void endVisit(GotoStatement gotoStatement);

    public boolean visit(Identifier identifier);

    public void endVisit(Identifier identifier);

    public boolean visit(IfStatement ifStatement);

    public void endVisit(IfStatement ifStatement);

    public boolean visit(IgnoreError ignoreError);

    public void endVisit(IgnoreError ignoreError);

    public boolean visit(Include include);

    public void endVisit(Include include);

    public boolean visit(InfixExpression infixExpression);

    public void endVisit(InfixExpression infixExpression);

    public boolean visit(InLineHtml inLineHtml);

    public void endVisit(InLineHtml inLineHtml);

    public boolean visit(InstanceOfExpression instanceOfExpression);

    public void endVisit(InstanceOfExpression instanceOfExpression);

    public boolean visit(InterfaceDeclaration interfaceDeclaration);

    public void endVisit(InterfaceDeclaration interfaceDeclaration);

    public boolean visit(LambdaFunctionDeclaration lambdaFunctionDeclaration);

    public void endVisit(LambdaFunctionDeclaration lambdaFunctionDeclaration);

    public boolean visit(ListVariable listVariable);

    public void endVisit(ListVariable listVariable);

    public boolean visit(MethodDeclaration methodDeclaration);

    public void endVisit(MethodDeclaration methodDeclaration);

    public boolean visit(MethodInvocation methodInvocation);

    public void endVisit(MethodInvocation methodInvocation);

    public boolean visit(NamespaceName namespaceName);

    public void endVisit(NamespaceName namespaceName);

    public boolean visit(NamespaceDeclaration namespaceDeclaration);

    public void endVisit(NamespaceDeclaration namespaceDeclaration);

    public boolean visit(ParenthesisExpression parenthesisExpression);

    public void endVisit(ParenthesisExpression parenthesisExpression);

    public boolean visit(PostfixExpression postfixExpression);

    public void endVisit(PostfixExpression postfixExpression);

    public boolean visit(PrefixExpression prefixExpression);

    public void endVisit(PrefixExpression prefixExpression);

    public boolean visit(Program program);

    public void endVisit(Program program);

    public boolean visit(Quote quote);

    public void endVisit(Quote quote);

    public boolean visit(Reference reference);

    public void endVisit(Reference reference);

    public boolean visit(ReflectionVariable reflectionVariable);

    public void endVisit(ReflectionVariable reflectionVariable);

    public boolean visit(ReturnStatement returnStatement);

    public void endVisit(ReturnStatement returnStatement);

    public boolean visit(Scalar scalar);

    public void endVisit(Scalar scalar);

    public boolean visit(SingleFieldDeclaration singleFieldDeclaration);

    public void endVisit(SingleFieldDeclaration singleFieldDeclaration);

    public boolean visit(StaticConstantAccess classConstantAccess);

    public void endVisit(StaticConstantAccess staticConstantAccess);

    public boolean visit(StaticFieldAccess staticFieldAccess);

    public void endVisit(StaticFieldAccess staticFieldAccess);

    public boolean visit(StaticMethodInvocation staticMethodInvocation);

    public void endVisit(StaticMethodInvocation staticMethodInvocation);

    public boolean visit(StaticStatement staticStatement);

    public void endVisit(StaticStatement staticStatement);

    public boolean visit(SwitchCase switchCase);

    public void endVisit(SwitchCase switchCase);

    public boolean visit(SwitchStatement switchStatement);

    public void endVisit(SwitchStatement switchStatement);

    public boolean visit(ThrowStatement throwStatement);

    public void endVisit(ThrowStatement throwStatement);

    public boolean visit(TryStatement tryStatement);

    public void endVisit(TryStatement tryStatement);

    public boolean visit(UnaryOperation unaryOperation);

    public void endVisit(UnaryOperation unaryOperation);

    public boolean visit(Variable variable);

    public void endVisit(Variable variable);

    public boolean visit(UseStatement useStatement);

    public void endVisit(UseStatement useStatement);

    public boolean visit(UseStatementPart useStatementPart);

    public void endVisit(UseStatementPart useStatementPart);

    public boolean visit(WhileStatement whileStatement);

    public void endVisit(WhileStatement whileStatement);

    public boolean visit(ASTNode node);

    public void endVisit(ASTNode node);
}
