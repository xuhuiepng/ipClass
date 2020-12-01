package cn.cnic.security.ipservice.service;

import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.model.IpApiResponseEntity;
import cn.cnic.security.ipservice.remote.IPAPIHttpClient;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author baokang
 * @date 2020/7/30 16:32
 */
@Slf4j
@Service("batchIpLocationService")
public class BatchIpLocationService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IPAPIHttpClient ipapiHttpClient;

    @Autowired
    private SimpleIpLocationService simpleIpLocationService;

    //通过geoip查询,暂时不用了
    @Deprecated
    public List<IpLocation> GeoipBatchIpQuery(List<String> ips) {

        List<IpLocation> ipLocations = new ArrayList<>();
        for (String ip : ips) {
            ipLocations.add(simpleIpLocationService.query(ip, "geoLite2"));
        }
        return ipLocations;

    }


    //以region为基础，将geoip信息加入
    public List<IpLocation> regionAndGeoipBatchIpQuery(List<String> ips) {

        List<IpLocation> ipLocations = new ArrayList<>();
        for (String ip : ips) {
            IpLocation geoLite2IpLocation = simpleIpLocationService.query(ip, "geoLite2");
            IpLocation regionIpLocation = simpleIpLocationService.query(ip, "ip2region");

            String geoipCity = geoLite2IpLocation.getCity() == null ? "":geoLite2IpLocation.getCity();
            String regionCity = regionIpLocation.getCity() == null ? "":regionIpLocation.getCity();
            String coutry = geoLite2IpLocation.getCountry() == null ? "":geoLite2IpLocation.getCountry();
            //不是中国并且2个库查询出城市不一样的就用geoip，但是运营商统一用ip2region
            if((!coutry.equals("中国")) && (!(geoipCity.contains(regionCity) || regionCity.contains(geoipCity)))){
                geoLite2IpLocation.setIsp(regionIpLocation.getIsp());
                ipLocations.add(geoLite2IpLocation);
            }else{
                regionIpLocation.setLat(geoLite2IpLocation.getLat());
                regionIpLocation.setLon(geoLite2IpLocation.getLon());
                regionIpLocation.setNetwork(geoLite2IpLocation.getNetwork());
                ipLocations.add(regionIpLocation);
            }
        }
        return ipLocations;

    }

    public List<IpApiResponseEntity> ipApiBatchQuery(List<String> ips) throws RestClientException, InterruptedException {

        List<IpApiResponseEntity> ipApiResponseEntityList = new ArrayList<>();

        //去重
        List<String> ipList = new ArrayList<>();
        for(String ip:ips){
            if(!ipList.contains(ip)) ipList.add(ip);
            if(ipList.size()>=1000) break;
        }

        List<IpApiResponseEntity> ipApiResponseEntityListTemp = getLocationByIpapi(ipList);

        //转为map
        HashMap<String,IpApiResponseEntity> map = new HashMap<>();
        for(IpApiResponseEntity ipApiResponseEntity:ipApiResponseEntityListTemp){
            map.put(ipApiResponseEntity.getQuery(),ipApiResponseEntity);
        }

        //list还原
        for(String ip:ips){
            ipApiResponseEntityList.add(map.get(ip));
        }


        return ipApiResponseEntityList;
    }

    private List<IpApiResponseEntity> getLocationByIpapi(List<String> ips) throws RestClientException, InterruptedException {
        log.info("开始查询···");
        List<IpApiResponseEntity> ipApiResponseEntityList = new ArrayList<>();
        int size = ips.size();
        int count = size / 100;
        int remainder = size % 100;
        for(int i = 0;i<count;i++){
            ResponseEntity<String> resTemp = ipapiHttpClient.batchExecute(ips.subList(i*100,i*100+100));
            //System.out.println(resTemp.getBody());
            Thread.sleep(1000);
            log.info("第"+i+"趟查询成功");
            ipApiResponseEntityList.addAll(Objects.requireNonNull(JSONObject.parseArray(resTemp.getBody(), IpApiResponseEntity.class)));
        }
        if(remainder!=0){
            ResponseEntity<String> resRemainder = ipapiHttpClient.batchExecute(ips.subList(count*100, count*100+remainder));
            log.info("第"+count+"趟查询成功");
            ipApiResponseEntityList.addAll(Objects.requireNonNull(JSONObject.parseArray(resRemainder.getBody(), IpApiResponseEntity.class)));
        }

        log.info("查询成功");
        return ipApiResponseEntityList;
    }

}
