package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 4*24*60*60) // 4 days
public class SessionConfig {
    // No changes needed here, Redis handles session storage automatically
}
