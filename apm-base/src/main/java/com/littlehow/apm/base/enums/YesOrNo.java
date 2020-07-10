package com.littlehow.apm.base.enums;


/**
 * 所有是非统一枚举
 * @author littlehow
 */
public enum YesOrNo {
    YES(1),
    NO(0);

    public final Integer v;

    YesOrNo(Integer _v) {
        v = _v;
    }

    /**
     * 是否在使用中
     * @param value -
     * @return -
     */
    public static boolean yes(Integer value) {
        return YES.v.equals(value);
    }

    public static YesOrNo value(Integer value) {
        for (YesOrNo status : YesOrNo.values()) {
            if (status.v.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException(value + " is not valid");
    }
}
