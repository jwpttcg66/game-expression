package com.snowcattle.game.expression.impl;


/**
 * 取模操作。
 */
public class ModExpression extends BinaryOperationExpression {

    private static final long serialVersionUID = 128643126283846133L;

    @Override
    public long getValue(long key) {
        return left.getValue(key) % right.getValue(key);
    }

    @Override
    public int getPriority() {
        return 2;
    }

}
