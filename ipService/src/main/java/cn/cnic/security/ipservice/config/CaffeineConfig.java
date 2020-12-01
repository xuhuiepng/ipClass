package cn.cnic.security.ipservice.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 缓存配置
 * @author xu
 */
@Configuration
@ConfigurationProperties(prefix = "spring.cache.caffeine")
@Slf4j
public class CaffeineConfig extends CachingConfigurerSupport {

    private String spec;

    @Bean(name = "cacheManager")
    @Override
    public CacheManager cacheManager() {
        log.info("spring.cache.caffeine.spec = {}",spec);
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // 不允许空值
        cacheManager.setAllowNullValues(false);
        // 传入一个CaffeineSpec定制缓存，它的好处是可以把配置方便写在配置文件里
        cacheManager.setCaffeineSpec(CaffeineSpec.parse(spec));
        // 必须要指定这个Bean，refreshAfterWrite配置属性才生效
        cacheManager.setCacheLoader(
                new CacheLoader<Object, Object>() {
                    @Override
                    public Object load(Object key) throws Exception {
                        log.info("CacheLoader load key = {}",key);
                        return null;
                    }
                    // 重写这个方法将oldValue值返回回去，进而刷新缓存
                    @Override
                    public Object reload(Object key, Object oldValue) throws Exception {
                        log.info("CacheLoader reload = {}",key);
                        return oldValue;
                    }
                });
        // 指定使用该策略的CacheNames
//        cacheManager.setCacheNames(new ArrayList<String>(Arrays.asList("amap", "ipApi")));

        return cacheManager;
    }

    @Bean
    public CacheLoader<Object, Object> cacheLoader() {
        return new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) throws Exception { return null;}
            // 重写这个方法将oldValue值返回回去，进而刷新缓存
            @Override
            public Object reload(Object key, Object oldValue) throws Exception {
                System.out.println("--refresh--:"+key);
                return oldValue;
            }
        };
    }


    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
