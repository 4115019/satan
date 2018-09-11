package com.satan.tools.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by huangpin on 17/3/16.
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${redis.cache.timeout:7200}")
    private Long cacheTimeout;

    @Bean(name = {"redisProperties"})
    @ConditionalOnMissingBean
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    /**
     * 可能会有这样一种情况，当你创建多个具有相同类型的 bean 时，并且想要用一个属性只为它们其中的一个进行装配，在这种情况下，你可以使用 @Qualifier 注释和 @Autowired 注释通过指定哪一个真正的 bean 将会被装配来消除混乱。
     */
    @Autowired
    @Qualifier("redisProperties")
    private RedisProperties properties;

    @Bean
    public KeyGenerator wiselyKeyGenerator(){
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };

    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(properties.getPool().getMaxActive());
        poolConfig.setMaxIdle(properties.getPool().getMaxIdle());
        poolConfig.setMaxWaitMillis(properties.getPool().getMaxWait());
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        poolConfig.setTestWhileIdle(true);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
        jedisConnectionFactory.setHostName(properties.getHost());
        if(null != properties.getPassword()){
            jedisConnectionFactory.setPassword(properties.getPassword());
        }
        jedisConnectionFactory.setPort(properties.getPort());
        jedisConnectionFactory.setUsePool(true);

        //其他配置，可再次扩展

        return jedisConnectionFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public <T> RedisTemplate<String,T> redisTemplate() {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new RedisObjectSerializer());
        return template;
    }

    @Override
    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
        redisCacheManager.setDefaultExpiration(cacheTimeout);
        return redisCacheManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redissonClient() {
        Config config = new Config();
        //sentinel
        if (properties.getSentinel() != null) {
            SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
            sentinelServersConfig.setMasterName(properties.getSentinel().getMaster());
            properties.getSentinel().getNodes();
            sentinelServersConfig.addSentinelAddress(properties.getSentinel().getNodes().split(","));
            sentinelServersConfig.setDatabase(properties.getDatabase());
            if (properties.getPassword() != null) {
                sentinelServersConfig.setPassword(properties.getPassword());
            }
        } else { //single server
            SingleServerConfig singleServerConfig = config.useSingleServer();
            singleServerConfig.setAddress(properties.getHost() + ":" + properties.getPort());
            singleServerConfig.setDatabase(properties.getDatabase());
            if (properties.getPassword() != null) {
                singleServerConfig.setPassword(properties.getPassword());
            }
        }
        return Redisson.create(config);
    }
}
