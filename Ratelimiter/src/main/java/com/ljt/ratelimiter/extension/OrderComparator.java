package com.ljt.ratelimiter.extension;

import java.util.Comparator;

import static com.ljt.ratelimiter.extension.Order.LOWEST_PRECEDENCE;

/**
 * 根据注解{@link Order#value()} 进行排序
 * 值小的，优先级高，排在前面
 * 值大的，优先级低，排在后面
 */
public class OrderComparator implements Comparator<Object> {

    //单例模式
    public static final OrderComparator INSTANCE = new OrderComparator();

    private OrderComparator() {

    }

    @Override
    public int compare(Object o1, Object o2) {
        Order order1 = o1.getClass().getAnnotation(Order.class);
        Order order2 = o2.getClass().getAnnotation(Order.class);
        int value1 = order1 == null ? LOWEST_PRECEDENCE : order1.value();
        int value2 = order2 == null ? LOWEST_PRECEDENCE : order2.value();
        rangeCheck(value1);
        rangeCheck(value2);
        //o1 - o2 自然排序升序
        return value1 - value2;
    }

    /**
     * {@link Order#value()} 的值范围检查
     */
    private void rangeCheck(int value) {
        if (value < 0 || value > 100) {
            throw new IndexOutOfBoundsException(String.format("Order value: %d (excepted: value range [%d,%d])", value, 0, 100));
        }
    }
}
