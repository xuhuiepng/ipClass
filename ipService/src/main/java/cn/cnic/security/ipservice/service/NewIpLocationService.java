package cn.cnic.security.ipservice.service;

import cn.cnic.security.ipcore.IpQuery2qqwry;
import cn.cnic.security.ipcore.IpQueryByGeoip2;
import cn.cnic.security.ipservice.model.NewIpLocationEntity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author wq
 * @date 2020-09-26 15:14
 */
@Service
@Slf4j
public class NewIpLocationService {
    @Autowired
    @Qualifier("simpleIpLocationService")
    private SimpleIpLocationService simpleIpLocationService;

    @Autowired
    private IpQueryByGeoip2 ipQueryByGeoip2;

    @Autowired
    private IpQuery2qqwry ipQuery2qqwry;

    //中文：zh-CN，英语：en
    private String language = "zh-CN";

    /**
     * @param ip
     * @return
     */
    public NewIpLocationEntity query(String ip) throws Exception {
        NewIpLocationEntity newIpLocationEntity = new NewIpLocationEntity();
        //geoLite2
        CityResponse response = ipQueryByGeoip2.search(ip);
        Country country = response.getCountry();
        Subdivision subdivision = response.getMostSpecificSubdivision();
//        City city = response.getCity();
        Location location = response.getLocation();

        //qqwry
        String qqwry = ipQuery2qqwry.localSearch(ip);
        JSONObject jsonObject = JSON.parseObject(qqwry);
        String isp = jsonObject.get("isp").toString();
        String city = jsonObject.get("city").toString();

        newIpLocationEntity.setCountryName(country.getNames().get(language));
        newIpLocationEntity.setRegion(isp);
        if(!city.equals(isp)){
            newIpLocationEntity.setCity(city + " " + isp);
        }else {
            newIpLocationEntity.setCity(city);
        }
        newIpLocationEntity.setLatitude(location.getLatitude());
        newIpLocationEntity.setLongitude(location.getLongitude());

        if(!"中国".equals(newIpLocationEntity.getCountryName())){
            newIpLocationEntity.setFullCity(newIpLocationEntity.getCountryName()+" "+newIpLocationEntity.getCity());
        }else {
            newIpLocationEntity.setFullCity(newIpLocationEntity.getCity());
        }
        newIpLocationEntity.setCountryCode(country.getIsoCode());

        //newIpLocationEntity.setArea_code(subdivision.getIsoCode());
        newIpLocationEntity.setOrgId("0");
        newIpLocationEntity.setArea_code("11");
        newIpLocationEntity.setMetro_code(newIpLocationEntity.getArea_code());
        newIpLocationEntity.setDma_code(newIpLocationEntity.getArea_code());
        newIpLocationEntity.setPostalCode("N/A");


//        //地区编码由高德获取
//        String queryAmap = aMapSearchService.query(ip);
//        JSONObject jsonObject = JSON.parseObject(queryAmap);
//        String area_code = jsonObject.get("adcode").toString();
//        newIpLocationEntity.setArea_code(area_code);

        return newIpLocationEntity;
    }
}
