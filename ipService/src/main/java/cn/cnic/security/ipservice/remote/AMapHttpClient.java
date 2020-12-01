package cn.cnic.security.ipservice.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 高德api请求客户端
 *
 * @author xu
 */
@Component
@ConfigurationProperties(prefix = "ip-api-config.amap")
@Slf4j
public class AMapHttpClient {

    private String url;

    private List<String> keys;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 执行查询
     *
     * @param ip
     * @return
     */
    public ResponseEntity<String> execute(String ip) {
        return restTemplate.getForEntity(url, String.class, ip, keys.get(0));
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
