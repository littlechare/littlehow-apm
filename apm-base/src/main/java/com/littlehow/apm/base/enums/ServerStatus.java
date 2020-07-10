package com.littlehow.apm.base.enums;

/**
 * 服务状态
 * 这里只是简单的列举了3个状态
 * 可根据业务需求细分状态
 * @author littlehow
 */
public enum ServerStatus {
    UP,  //上线
    DOWN, //下线
    HEARTBEAT_LOSE; //心跳丢失
}
