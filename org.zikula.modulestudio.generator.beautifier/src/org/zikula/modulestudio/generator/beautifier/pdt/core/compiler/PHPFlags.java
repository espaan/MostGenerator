package org.zikula.modulestudio.generator.beautifier.pdt.core.compiler;

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
 * Based on package org.eclipse.php.core.compiler;
 * 
 *******************************************************************************/

import org.eclipse.dltk.core.Flags;

public class PHPFlags extends Flags implements IPHPModifiers {

    /**
     * Returns whether the given integer includes the <code>default</code>
     * modifier. That usually means that the element has no 'public',
     * 'protected' or 'private' modifiers at all.
     * 
     * @param flags
     *            the flags
     * @return <code>true</code> if the <code>default</code> modifier is
     *         included
     */
    public static boolean isDefault(int flags) {
        return !isPrivate(flags) && !isProtected(flags) && !isPublic(flags);
    }

    /**
     * Returns whether the given integer includes the <code>internal</code>
     * modifier.
     * 
     * @param flags
     *            the flags
     * @return <code>true</code> if the <code>internal</code> modifier is
     *         included
     * @deprecated
     */
    @Deprecated
    public static boolean isInternal(int flags) {
        return (flags & Internal) != 0;
    }

    /**
     * Returns whether the given integer includes the <code>namespace</code>
     * modifier.
     * 
     * @param flags
     *            the flags
     * @return <code>true</code> if the <code>namespace</code> modifier is
     *         included
     */
    public static boolean isNamespace(int flags) {
        return (flags & AccNameSpace) != 0;
    }

    /**
     * Returns whether the given integer includes the <code>constant</code>
     * modifier.
     * 
     * @param flags
     *            the flags
     * @return <code>true</code> if the <code>constant</code> modifier is
     *         included
     */
    public static boolean isConstant(int flags) {
        return (flags & AccConstant) != 0;
    }

    /**
     * Returns whether the given integer includes the <code>class</code>
     * modifier.
     * 
     * @param flags
     *            the flags
     * @return <code>true</code> if the <code>class</code> modifier is included
     */
    public static boolean isClass(int flags) {
        return !isNamespace(flags) && !isInterface(flags);
    }

    public static String toString(int mod) {
        final StringBuffer sb = new StringBuffer();

        if ((mod & AccPublic) != 0) {
            sb.append("public "); //$NON-NLS-1$
        }
        if ((mod & AccProtected) != 0) {
            sb.append("protected "); //$NON-NLS-1$
        }
        if ((mod & AccPrivate) != 0) {
            sb.append("private "); //$NON-NLS-1$
        }

        // Canonical order
        if ((mod & AccAbstract) != 0) {
            sb.append("abstract "); //$NON-NLS-1$
        }
        if ((mod & AccStatic) != 0) {
            sb.append("static "); //$NON-NLS-1$
        }
        if ((mod & AccFinal) != 0) {
            sb.append("final "); //$NON-NLS-1$
        }

        int len;
        if ((len = sb.length()) > 0) { /* trim trailing space */
            return sb.toString().substring(0, len - 1);
        }
        return ""; //$NON-NLS-1$
    }
}
