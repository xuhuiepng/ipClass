package cn.cnic.security.ipservice.service;

import cn.cnic.security.ipcore.IpQuery2qqwry;
import cn.cnic.security.ipcore.IpQuery2region;
import cn.cnic.security.ipcore.IpQueryByGeoip2;
import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.common.utils.IPUtils;
import cn.cnic.security.ipservice.common.utils.R;
import cn.cnic.security.ipservice.model.IpSegmentResponse;
import cn.cnic.security.ipservice.service.impl.AMapSearchServiceImpl;
import cn.cnic.security.ipservice.service.impl.IPAPISearchServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ip定位
 */
@Service("simpleIpLocationService")
@Slf4j
public class SimpleIpLocationService {

    @Autowired
    protected IpQuery2region ipQuery2region;
    @Autowired
    protected IpQuery2qqwry ipQuery2qqwry;
    @Autowired
    protected IpQueryByGeoip2 ipQueryByGeoip2;
    @Autowired
    protected AMapSearchServiceImpl aMapSearchService;
    @Autowired
    protected IPAPISearchServiceImpl ipApiSearchService;


    /**
     * @param ip
     * @param type
     * @return
     * @throws Exception
     */
    //@Cacheable(cacheNames = "query",key = "#ip+'-'+#type")
    public IpLocation query(String ip, String type) {
        if (StringUtils.isEmpty(type)) {
            type = "ip2region";
        }
        IpLocation location = null;
        switch (type) {
            case "ip2region":
                try {
                    if(IPUtils.isIPv4(ip)){
                        location = ipQuery2region.formatData(ipQuery2region.localSearch(ip));
                    }else{
                        location = new IpLocation();
                    }
                } catch (Exception e) {
                    log.error("ip2region查询出错");
                    location = new IpLocation();
                }

                location.setSource("ip2region");
                location.setQuery(ip);
                break;
            case "qqwry":
                try {
                    if(IPUtils.isIPv4(ip)){
                        location = ipQuery2qqwry.formatData(ipQuery2qqwry.localSearch(ip));
                    }else{
                        location = new IpLocation();
                    }
                } catch (Exception e) {
                    log.error("qqwry查询出错");
                    location = new IpLocation();
                }

                location.setSource("纯真");
                location.setQuery(ip);
                break;
            case "geoLite2":
                try {
                    if(IPUtils.isIPv4(ip)){
                        location = ipQueryByGeoip2.searchAndFormat(ip);
                    }else{
                        location = new IpLocation();
                    }
                } catch (Exception e) {
                    log.error("查询出错/ip不在geoLite2库内");
                    location = new IpLocation();
                }
                location.setSource("geoLite2");
                location.setQuery(ip);
                break;
            case "aMap":
                //对于高德 ip格式出错或国外ip直接返回空，无需判断
                location = aMapSearchService.quickAccess(ip);
                location.setSource("高德");
                location.setQuery(ip);
                break;
            case "ip-api" :
                //对于ip-api ip格式出错直接返回空，无需判断
                location = ipApiSearchService.quickAccess(ip);
                location.setSource("ip-api.com");
                location.setQuery(ip);
                break;
            default:
                try {
                    if(IPUtils.isIPv4(ip)){
                        location = ipQuery2region.formatData(ipQuery2region.localSearch(ip));
                    }else{
                        location = new IpLocation();
                    }
                } catch (Exception e) {
                    log.error("查询出错 ip={}",ip);
                    location = new IpLocation();
                }
                location.setSource("ip2region");
                location.setQuery(ip);
                break;

        }
        return location;
    }

}
