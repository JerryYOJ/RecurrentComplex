/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.reccomplex.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;

/**
 * Created by lukas on 25.02.15.
 */
public class ExpressionCache<T>
{
    @Nonnull
    protected Algebra<T> algebra;

    @Nonnull
    protected String expression;
    @Nullable
    protected Algebra.Expression<T> parsedExpression;
    @Nullable
    protected ParseException parseException;

    public ExpressionCache(Algebra<T> algebra, String expression)
    {
        this.algebra = algebra;
        setExpression(expression);
    }

    protected void parseExpression()
    {
        try
        {
            parsedExpression = algebra.parse(expression);
            parseException = null;
        }
        catch (ParseException e)
        {
            parsedExpression = null;
            parseException = e;
        }
    }

    @Nonnull
    public String getExpression()
    {
        return expression;
    }

    public void setExpression(@Nonnull String expression)
    {
        this.expression = expression;
        parseExpression();
    }

    @Nonnull
    public Algebra<T> getAlgebra()
    {
        return algebra;
    }

    public void setAlgebra(@Nonnull Algebra<T> algebra)
    {
        this.algebra = algebra;
    }

    @Nonnull
    public String getDisplayString()
    {
        return getExpression();
    }

    @Nullable
    public Algebra.Expression<T> getParsedExpression()
    {
        return parsedExpression;
    }

    @Nullable
    public ParseException getParseException()
    {
        return parseException;
    }

    public boolean isExpressionValid()
    {
        return parsedExpression != null && parseException == null;
    }
}