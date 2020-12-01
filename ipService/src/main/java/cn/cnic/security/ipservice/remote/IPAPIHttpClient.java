package cn.cnic.security.ipservice.remote;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author baokang
 * @date 2020/7/28 15:17
 */

@Component
@ConfigurationProperties(prefix = "ip-api-config.ip-api")
@Slf4j
public class IPAPIHttpClient {
    private String url;

    private String batchUrl;

    @Autowired
    private RestTemplate restTemplate;


    public ResponseEntity<String> execute(String ip){
        return restTemplate.getForEntity(url,String.class,ip);
    }

    public ResponseEntity<String> batchExecute(List<String> ips) throws RestClientException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> authRequest = new HttpEntity<>(JSON.toJSON(ips).toString(), headers);
        //System.out.println(JSON.toJSON(ips).toString());
        return restTemplate.postForEntity(batchUrl,authRequest,String.class);

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBatchUrl() {
        return batchUrl;
    }

    public void setBatchUrl(String batchUrl) {
        this.batchUrl = batchUrl;
    }
}
