package com.snowcattle.game.expression.impl;


/**
 * 减法操作。
 */
public class SubtractExpression extends BinaryOperationExpression {

    private static final long serialVersionUID = 1026380096191594448L;

    @Override
    public long getValue(long key) {
        return left.getValue(key) - right.getValue(key);
    }

    @Override
    public int getPriority() {
        return 1;
    }

}
