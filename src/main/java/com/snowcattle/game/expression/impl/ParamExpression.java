package com.snowcattle.game.expression.impl;


import com.snowcattle.game.expression.Expression;

/**
 * 参数占位符表达式，直接返回参数。
 */
public class ParamExpression implements Expression {

    private static final long serialVersionUID = -1691851142296564067L;

    /* (non-Javadoc)
     * @see com.renren.socialgame.commons.dao.config.Expression#getValue(int)
     */
    @Override
    public long getValue(long key) {
        return key;
    }

}
