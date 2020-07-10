package com.littlehow.apm.base.enums;


/**
 * 接口或服务状态
 * 主要是接口更新后可以记录删除掉的接口的历史信息
 * @author littlehow
 */
public enum InterfaceStatus {
    USING(1),   //使用中
    DEPRECATED(2)  //过时
    ;

    public final Integer v;

    InterfaceStatus(Integer _v) {
        v = _v;
    }

    /**
     * 是否在使用中
     * @param value -
     * @return -
     */
    public static boolean isUsing(Integer value) {
        return USING.v.equals(value);
    }

    /**
     *
     * @param value -
     * @return -
     */
    public static InterfaceStatus value(Integer value) {
        for (InterfaceStatus status : InterfaceStatus.values()) {
            if (status.v.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException(value + " is not valid");
    }
}
