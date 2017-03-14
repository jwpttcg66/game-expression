package com.snowcattle.game;

import com.snowcattle.game.expression.Expression;
import com.snowcattle.game.expression.ExpressionUtil;

/**
 * Created by jiangwenping on 17/3/13.
 */
public class ExpressionTest {

    public static void main(String[] args) throws Exception {
        Expression expression = ExpressionUtil.buildExpression("$%10");
        System.out.println(expression.getValue(12));
    }
}
