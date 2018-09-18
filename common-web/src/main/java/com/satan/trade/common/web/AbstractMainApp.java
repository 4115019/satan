package com.satan.trade.common.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.apache.commons.lang3.time.StopWatch;

/**
 * Created by huangpin on 17/3/16.
 */
@Slf4j
@EnableAutoConfiguration
public class AbstractMainApp extends SpringBootServletInitializer implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments var1) throws Exception{
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("---------------------------- {} start running web with args = {} ----------------------------", this.getClass().getName(), var1);

        start(var1);

        stopWatch.stop();
        log.info("---------------------------- end running web  costTime = {} (ms) ----------------------------", stopWatch.getTime());
    }

    public void start(ApplicationArguments arguments) {
    }

    protected static ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }



    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(this.getClass());
    }
}
