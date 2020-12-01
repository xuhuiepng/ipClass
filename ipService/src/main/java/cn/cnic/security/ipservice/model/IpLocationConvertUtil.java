package cn.cnic.security.ipservice.model;

import cn.cnic.security.ipcore.model.IpLocation;

/**
 * @author baokang
 * @date 2020/8/18 12:39
 */
public class IpLocationConvertUtil {

    public static IpLocation ipApi2IpLocation(IpApiResponseEntity ipApiResponseEntity){
        IpLocation ipLocation = new IpLocation();
        ipLocation.setSource("ip-api");
        ipLocation.setQuery(ipApiResponseEntity.getQuery());
        if(ipApiResponseEntity.getStatus().equals("fail")){
            return ipLocation;
        }
        ipLocation.setCountry(ipApiResponseEntity.getCountry());
        ipLocation.setProvince(ipApiResponseEntity.getRegionName());
        ipLocation.setCity(ipApiResponseEntity.getCity());
        ipLocation.setLon(ipApiResponseEntity.getLon());
        ipLocation.setLat(ipApiResponseEntity.getLat());
        ipLocation.setIsp(ipApiResponseEntity.getIsp());


        return ipLocation;
    }
}
