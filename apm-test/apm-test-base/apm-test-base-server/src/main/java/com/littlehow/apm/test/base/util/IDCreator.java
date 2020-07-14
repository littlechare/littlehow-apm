package com.littlehow.apm.test.base.util;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生成相同id的概率很低
 *  超级大并发可能出现碰撞(机器也扛不住这样的并发)
 *  分布式多节点部署(随机数在不同机器上的碰撞概率也低)
 *  性能 - 单机【25W个/s】左右，受机器CPU配置影响
 */
public class IDCreator {
    private static Random random = new Random();

    private static final AtomicInteger seq = new AtomicInteger(1);

    /**
     * 基于本地的id生成器
     * @return
     */
    public static String get() {
       return get("");
    }

    public static String get(String prefix) {
        //时间戳
        String current = String.valueOf(System.currentTimeMillis());
        //六位随机数
        String randomInfo = getRandomInfo();
        //三位序号
        int seqNo = seq.getAndIncrement();
        if (seqNo >= 666) {
            //多个线程设置也没有关系，这里666就重新设置，可以预留334个超级并发出现,可能在并发情况下有seqNo超过666的情况
            seq.set(1);
        }
        return prefix + current + String.format("%03d", seqNo) + randomInfo;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static String getRandomInfo() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            //这里并发导致的随机数碰撞不需要关心
            sb.append(String.format("%02d", random.nextInt(100)));
        }
        return sb.toString();
    }

}
