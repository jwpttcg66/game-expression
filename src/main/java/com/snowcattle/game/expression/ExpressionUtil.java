package com.snowcattle.game.expression;

import com.snowcattle.game.expression.impl.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;




/**
 * 表达式工具类，可以将表达式字符串转换成表达式对象，然后根据具体参数值求值。
 *
 * @see Expression
 */
public class ExpressionUtil {

    private static final char ADD = '+';
    private static final char SUBTRACT = '-';
    private static final char MULTIPLE = '*';
    private static final char DIVIDE = '/';
    private static final char MOD = '%';
    private static final char LEFT_PARENTHESIS = '(';
    private static final String LEFT_PARENTHESIS_STRING = "(";
    private static final char RIGHT_PARENTHESIS = ')';
    private static final String RIGHT_PARENTHESIS_STRING = ")";
    private static final char VARIABLE = '$';
    private static final String VARIABLE_STRING = "$";

    private static final List<Character> OPERATORS =
        Arrays.asList(new Character[]{ADD, SUBTRACT, MULTIPLE, DIVIDE,
                MOD,LEFT_PARENTHESIS, RIGHT_PARENTHESIS, VARIABLE});

    private static final List<String> VALID_OPERATORS =
        Arrays.asList(new String[]{String.valueOf(ADD), String.valueOf(SUBTRACT),
                String.valueOf(MULTIPLE), String.valueOf(DIVIDE), String.valueOf(MOD)});

    /**
     * 根据传入的表达式字符串构建一个表达式对象。
     * @param expressionRule 表达式字符串
     * @return 返回构建好的表达式对象
     * @throws Exception
     */
    public static Expression buildExpression(String expressionRule) throws Exception {
        return buildExpression(parseOperators(expressionRule));
    }

    private static LinkedList<String> parseOperators(String expressionRule) {
        StringBuilder s = null;
        LinkedList<String> operationParts = new LinkedList<String>();

        for (char c : expressionRule.trim().toCharArray()) {
            if (OPERATORS.contains(c)) {
                if (null != s && s.length() > 0) {
                    operationParts.add(s.toString());
                    s = null;
                }

                operationParts.add(String.valueOf(c));
            } else if (Character.isWhitespace(c)) {
                if (null != s && s.length() > 0) {
                    operationParts.add(s.toString());
                    s = null;
                }
            } else if (Character.isDigit(c)) {
                if (null == s) {
                    s = new StringBuilder();
                }

                s.append(c);
            }
        }

        if (null != s && s.length() > 0) {
            operationParts.add(s.toString());
        }

        return operationParts;
    }

    /**
     * 根据操作字符串序列构建表达式对象。
     * @param operators 操作字符串序列
     * @return 返回构建好的表达式对象
     */
    private static Expression buildExpression(LinkedList<String> operators) throws Exception {
        Expression left = null;
        Expression right = null;
        BinaryOperationExpression expression = null;
        LinkedList<BinaryOperationExpression> stack = new LinkedList<BinaryOperationExpression>();

        while (operators.peek() != null) {
            String operator = operators.poll();

            //解析变量占位符
            if (operator.equals(VARIABLE_STRING)) {
                if (left == null) {
                    left = new ParamExpression();
                } else {
                    right = new ParamExpression();
                }
            } else if (VALID_OPERATORS.contains(operator)) {
                /*
                 * 如果解析到操作符，则将左右表达式整合起来
                 */

                //如果表达式为空，则表示还没有双位操作，或者之前的操作进栈了（遇到括号）
                if (expression == null) {
                    expression = getBinaryExpression(operator.charAt(0));
                    expression.setLeft(left);
                    continue;
                }

                //如果当前二位操作已经存在，则要比较两个操作的优先级
                BinaryOperationExpression subExpression = getBinaryExpression(operator.charAt(0));

                //如果当前的操作符低于已经存在的操作类型，则将之前的操作作为当前操作的做操作（优先计算）
                if (subExpression.getPriority() <= expression.getPriority()) {
                    expression.setRight(right);

                    //依次递归的查看堆栈中已经解析的表达式，将所有高于当前操作的表达式都提前运算
                    while (stack.peek() != null
                            && stack.peek().getPriority() >= subExpression.getPriority()) {
                        BinaryOperationExpression previousExpression = stack.poll();
                        previousExpression.setRight(expression);
                        expression = previousExpression;
                    }

                    //将之前的操作作为左表达式
                    left = expression;
                    subExpression.setLeft(left);

                    expression = subExpression;
                    right = null;
                } else {
                    //如果当前操作高于之前的操作，则将之前的操作压栈
                    stack.push(expression);
                    left = right;
                    subExpression.setLeft(left);
                    right = null;
                    expression = subExpression;
                }

            } else if (operator.equals(LEFT_PARENTHESIS_STRING)) {
                //左括号将所有之前的表达式全部压栈
                if (expression != null) {
                    stack.push(expression);
                    left = null;
                    expression = null;
                }
                stack.push(new ParenthesesExpression());
            } else if (operator.equals(RIGHT_PARENTHESIS_STRING)) {
                //右括号将触发出栈操作，直到对应的左括号被找到（第一个就是，所有其他的都已经成对处理掉了，所有右括号不进站，不存在于表达式中）
                if (right != null) {
                    expression.setRight(right);
                    right = null;
                }

                while (!(stack.peek() instanceof ParenthesesExpression)) {
                    right = expression;
                    expression = stack.poll();
                    expression.setRight(right);
                }

                stack.poll();

                //找到左括号后需要再尝试弹出左括号左边的一个操作，为之后的操作作准备
                if (stack.peek() != null) {
                    right = expression;
                    expression = stack.poll();
                    left = expression.getLeft();
                } else {
                    left = expression;
                    expression = null;
                    right = null;
                }
            } else {
                //非特殊操作符（非计算符、括号、变量）的都作为数值直接处理
                int value = Integer.valueOf(operator);

                //如果左表达式不存在，则赋值
                if (left == null) {
                    left = new StaticExpression(value);
                } else {
                    //如果左表达式存在，则赋值为由表达式
                    right = new StaticExpression(value);
                }
            }

        }

        //如果已经循环完了所有部分以后，查看最后的操作部分被添加进去
        if (right != null) {
            expression.setRight(right);

            //依次弹出堆栈里的操作符
            while (stack.peek() != null) {
                BinaryOperationExpression preExpression = stack.poll();
                preExpression.setRight(expression);
                expression = preExpression;
            }
        }

        //如果只有一个操作符的时候直接返回，特殊情况例如：$
        if (null == expression && null != left) {
            return left;
        }

        return expression;
    }

    /**
     * 根据操作符构建一个二元操作对象。
     * @param operator 操作符
     * @return 返回二元操作对象
     * @throws Exception
     */
    private static BinaryOperationExpression getBinaryExpression(char operator) throws Exception {
        switch (operator) {
            case ADD:
                return new AddExpression();
            case SUBTRACT:
                return new SubtractExpression();
            case MULTIPLE:
                return new MultipleExpression();
            case DIVIDE:
                return new DivideExpression();
            case MOD:
                return new ModExpression();
            default:
                throw new Exception("Unkown operator " + operator + " for table partition.");
        }
    }

    /**
     * 括号表达式，代表左括号的开始位置。
     * @author <a href="mailto:kun.tong@opi-corp.com">Tank.Tong</a>
     * @version 1.0 2009-11-26 15:30:19
     * @since 1.0
     */
    private static final class ParenthesesExpression extends BinaryOperationExpression {

        private static final long serialVersionUID = -5045991081974652465L;

        @Override
        public int getPriority() {
            return 3;
        }

        @Override
        public long getValue(long key) {
            return 0;
        }

    }
}
