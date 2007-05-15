package org.mvel.block;

import org.mvel.CompileException;
import org.mvel.ExecutableStatement;
import static org.mvel.MVEL.compileExpression;
import org.mvel.Token;
import org.mvel.integration.VariableResolverFactory;

/**
 * @author Christopher Brock
 */
public class AssertToken extends Token {
    public ExecutableStatement assertion;

    public AssertToken(char[] expr, int fields) {
        super(expr, fields);
        assertion = (ExecutableStatement) compileExpression(expr);
    }

    public Object getReducedValueAccelerated(Object ctx, Object thisValue, VariableResolverFactory factory) {
        try {
            Boolean bool = (Boolean) assertion.getValue(ctx, thisValue, factory);
            if (!bool) throw new AssertionError("assertion failed in expression: " + new String(this.name));
            return bool;
        }
        catch (ClassCastException e) {
            throw new CompileException("assertion does not contain a boolean statement");
        }
    }

    public Object getReducedValue(Object ctx, Object thisValue, VariableResolverFactory factory) {
        return getReducedValueAccelerated(ctx, thisValue, factory);
    }
}