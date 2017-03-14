package com.snowcattle.game.expression.impl;



/**
 * 除法操作。
 */
public class DivideExpression extends BinaryOperationExpression {

    private static final long serialVersionUID = -6210937060757444787L;

    /* (non-Javadoc)
     * @see com.renren.socialgame.commons.dao.config.Expression#getValue(int)
     */
    @Override
    public long getValue(long key) {
        return left.getValue(key) / right.getValue(key);
    }

    @Override
    public int getPriority() {
        return 2;
    }

}
