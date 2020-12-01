package cn.cnic.security.ipservice.service.impl;

import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.common.utils.JacksonUtils;
import cn.cnic.security.ipservice.remote.AMapHttpClient;
import cn.cnic.security.ipservice.service.RemoteIpSearch;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

/**
 * 高德地图api查询
 *
 * @author xu
 */
@Service
@Slf4j
public class AMapSearchServiceImpl implements RemoteIpSearch {

    @Autowired
    private AMapHttpClient aMapClient;

    /**
     * 将高德api查询ip得到的信息进行补充
     * 补充字段：query(查询ip)
     * lat/lon(经纬度)
     *
     * @param ip
     * @return
     */
    @Override
    public String query(String ip) {
        ResponseEntity<String> response = aMapClient.execute(ip);
        //增加query字段
        JSONObject jsonObject = JSON.parseObject(response.getBody());
        jsonObject.put("query", ip);
        return jsonObject.toJSONString();
    }


    @Override
    public IpLocation formatJsonResult(String line) {
        JSONObject jsonObject = JSON.parseObject(line);
        //将矩形地图坐标转为经纬度
        String rectangle = jsonObject.get("rectangle").toString();
        if (rectangle.indexOf("[]") != 0) {
            String[] coordinates = rectangle.split(";");
            String[] upLeft = coordinates[0].split(",");
            String[] lowerRight = coordinates[1].split(",");
            Double lon = (Double.parseDouble(upLeft[0]) + Double.parseDouble(lowerRight[0])) / 2;
            Double lat = (Double.parseDouble(upLeft[1]) + Double.parseDouble(lowerRight[1])) / 2;
            DecimalFormat dcmFmt = new DecimalFormat("0.0000000");
            String formatlat = dcmFmt.format(lat);
            String formatlon = dcmFmt.format(lon);
            jsonObject.put("lat", formatlat);
            jsonObject.put("lon", formatlon);
        } else {
            jsonObject.put("lat", "[]");
            jsonObject.put("lon", "[]");
        }
        //运营商
        String city = String.valueOf(jsonObject.get("city"));

        if (!"null".equals(city) && !"市".equals(city.substring(city.length() - 1))) {
            jsonObject.put("isp", jsonObject.get("city"));
        }else if(!"null".equals(city)){
            jsonObject.put("country", "中国");
        }

        String res = jsonObject.toJSONString().replaceAll("\\[\\]", "null");
        IpLocation ipLocation = JacksonUtils.string2Obj(res, IpLocation.class);
        ipLocation.setSource("高德");

        return ipLocation;
    }

    @Override
    @Cacheable(cacheNames = "AMapQuickAccess", key = "#ip")
    public IpLocation quickAccess(String ip) {
        ResponseEntity<String> response = aMapClient.execute(ip);

        log.info("ip = {} {}", ip, response.getBody());
        return formatJsonResult(response.getBody());
    }


}
