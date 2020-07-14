package com.littlehow.apm.eureka.server.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于心跳检测
 */
@RestController
@Slf4j
@RequestMapping("health")
public class HealthCheckController {

    @RequestMapping(value = "check")
    public void isRunning(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("=================health check is running=====================");
        response.getWriter().println("OK");
        response.getWriter().flush();
    }
}
