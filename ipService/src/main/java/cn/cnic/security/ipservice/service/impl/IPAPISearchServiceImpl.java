package cn.cnic.security.ipservice.service.impl;


import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.common.utils.JacksonUtils;
import cn.cnic.security.ipservice.remote.IPAPIHttpClient;
import cn.cnic.security.ipservice.service.RemoteIpSearch;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * https://ip-api.com/json 查询ip
 */
@Service
@Slf4j
public class IPAPISearchServiceImpl implements RemoteIpSearch {
    @Autowired
    private IPAPIHttpClient httpClient;

    @Override
    public String query(String ip) {

        ResponseEntity<String> response = httpClient.execute(ip);
        return response.getBody();
    }

    @Override
    public IpLocation formatJsonResult(String line) {

        IpLocation ipLocation = JacksonUtils.string2Obj(line,IpLocation.class);
        JSONObject jsonObject = JSON.parseObject(line);
        if(ipLocation!=null){
            ipLocation.setProvince((String)jsonObject.get("regionName"));
            ipLocation.setQuery((String)jsonObject.get("query"));
            ipLocation.setSource("IpApi");
        }
        return ipLocation;
    }

    @Override
    @Cacheable(cacheNames = "ipApiQuickAccess",key = "#ip")
    public IpLocation quickAccess(String ip) {
        ResponseEntity<String> response = httpClient.execute(ip);
        //log.info("ip = {} {}",ip,response.getBody());
        IpLocation ipLocation = formatJsonResult(response.getBody());
        return ipLocation;
    }
}
