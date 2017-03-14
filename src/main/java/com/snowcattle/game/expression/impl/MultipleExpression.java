package com.snowcattle.game.expression.impl;


/**
 * 乘法操作。
 */
public class MultipleExpression extends BinaryOperationExpression {

    private static final long serialVersionUID = -5663414391707378304L;

    /* (non-Javadoc)
     * @see com.renren.socialgame.commons.dao.config.Expression#getValue(int)
     */
    @Override
    public long getValue(long key) {
        return left.getValue(key) * right.getValue(key);
    }

    @Override
    public int getPriority() {
        return 2;
    }

}
