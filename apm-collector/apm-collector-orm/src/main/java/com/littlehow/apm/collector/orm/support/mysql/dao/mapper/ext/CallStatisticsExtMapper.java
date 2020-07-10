package com.littlehow.apm.collector.orm.support.mysql.dao.mapper.ext;

import com.littlehow.apm.collector.orm.model.CallStatisticsBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 调用统计 Mapper 接口
 * </p>
 *
 * @author littlehow
 * @since 2020-06-09
 */
public interface CallStatisticsExtMapper {
    /**
     * 系统调用统计
     * @param startTime
     * @param endTime
     * @param top
     * @return
     */
    List<CallStatisticsBo> selectCallSystemStatistics(@Param("startTime") String startTime,
                                                      @Param("endTime") String endTime,
                                                      @Param("top") int top);

    /**
     * 接口调用统计
     * @param startTime
     * @param endTime
     * @param top
     * @param serverName
     * @return
     */
    List<CallStatisticsBo> selectCallInterfaceStatistics(@Param("startTime") String startTime,
                                                         @Param("endTime") String endTime,
                                                         @Param("top") int top,
                                                         @Param("serverName") String serverName);


    /**
     * 分时统计接口
     * @param startTime   -- 开始时间
     * @param endTime     -- 结束时间
     * @param serverName  -- 系统名
     * @return -
     */
    List<CallStatisticsBo> selectCallSystemDetail(@Param("startTime") String startTime,
                                                  @Param("endTime") String endTime,
                                                  @Param("serverName") String serverName);

    /**
     * 接口分时统计
     * @param startTime   -- 开始时间
     * @param endTime     -- 结束时间
     * @param uri         -- 接口名
     * @return -
     */
    List<CallStatisticsBo> selectCallInterfaceDetail(@Param("startTime") String startTime,
                                                     @Param("endTime") String endTime,
                                                     @Param("uri") String uri);
}
