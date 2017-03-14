package com.snowcattle.game.expression;

import java.io.Serializable;


/**
 * 计算表达式，所有四则运算以及位运算等被支持的运算方式的接口类。
 */
public interface Expression extends Serializable{

    /**
     * 传入实际的参数数值获得计算式的结果值。
     * @param key 实际参数数值
     * @return 返回计算结果
     */
    public long getValue(long key);

}
