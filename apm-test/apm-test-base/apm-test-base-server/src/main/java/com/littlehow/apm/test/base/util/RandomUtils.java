package com.littlehow.apm.test.base.util;

import com.littlehow.apm.base.util.ApmAssert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 测试所需随机参数生成
 *
 * @author littlehow
 */
public class RandomUtils {

    private static String[] phonePrefix = {"138", "133", "135", "139", "136", "137", "152", "155", "158", "188", "186", "177"};
    // 不需要考虑线程安全问题，此处仅仅为测试
    private static Random random = new Random();

    public static int randomCount() {
        return randomCount(1, 50);
    }

    public static int randomCount(int min, int max) {
        ApmAssert.isTrue(max >= min && min >= 0, "错误参数:min={0}, max={1}", min, max);
        int value = random.nextInt(max - min + 1);
        return value + min;
    }

    public static LocalDateTime randomDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.minusMinutes(random.nextInt(200));
    }

    public static BigDecimal randomPrice() {
        double price = random.nextDouble() * randomCount(30, 300);
        String priceString = Double.valueOf(price).toString();
        // 小数点分割
        String[] infos = priceString.split("\\.");
        int count = randomCount(0, 2);
        priceString = infos[0] + (count > 0 ? "." + infos[1].substring(0, count) : "");
        return new BigDecimal(priceString).stripTrailingZeros();
    }

    public static String randomStatus() {
        return String.valueOf(random.nextInt(10) + 1);
    }

    public static String random4digtal() {
        return String.format("%04d", random.nextInt(10000));
    }

    public static String randomMobilePhone() {
        return phonePrefix[random.nextInt(phonePrefix.length)] + random4digtal() + random4digtal();
    }

}
