package com.snowcattle.game.expression.impl;


import com.snowcattle.game.expression.Expression;

/**
 * 静态数值表达式。
 */
public class StaticExpression implements Expression {

    private static final long serialVersionUID = -6060431150586251801L;

    private int value;

    public StaticExpression(int value) {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see com.renren.socialgame.commons.dao.config.Expression#getValue(int)
     */
    @Override
    public long getValue(long key) {
        return value;
    }

}
