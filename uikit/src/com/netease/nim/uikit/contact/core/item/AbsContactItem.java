package com.netease.nim.uikit.contact.core.item;

/**
 * 通讯录数据项抽象类
 * Created by huangjun on 2015/2/10.
 */
public abstract class AbsContactItem {
    public static int compareType(int lhs, int rhs) {
        return lhs - rhs;
    }

    /**
     * 所属的类型
     *
     * @see com.netease.nim.uikit.contact.core.item.ItemTypes
     */
   // 抽象方法，待子类实现
    public abstract int getItemType();

    /**
     * 所属的分组
     */
    // 抽象方法，待子类实现
    public abstract String belongsGroup();

    // 不知道用途是什么
    protected final int compareType(AbsContactItem item) {
        return compareType(getItemType(), item.getItemType());
    }
}
