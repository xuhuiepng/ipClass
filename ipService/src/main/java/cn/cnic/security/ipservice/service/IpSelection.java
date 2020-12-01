package cn.cnic.security.ipservice.service;

import cn.cnic.security.ipcore.model.IpLocation;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * IP评选
 * 通过多个数据源选择一个出现最多的“国家”
 * 补齐数据
 *
 * @author xu
 */
@Service("ipSelection")
public class IpSelection extends SimpleIpLocationService {

    /**
     * 多个数据统计国家，使用得票最多的
     * 只针对国外
     */
    public IpLocation byScore(String ip){
        Map<String, DefinitionSelectionBean> map = new LinkedHashMap<>();
        // 国家 计数
        IpLocation apiLocation = ipApiSearchService.quickAccess(ip);
        if(apiLocation != null){
            apiLocation.setSource("api");
            String country = apiLocation.getCountry();
            DefinitionSelectionBean bean = new DefinitionSelectionBean();
            bean.getList().add(apiLocation);
            bean.setCount(1);
            map.put(country,bean);
        }
        IpLocation ip2reg = ipQuery2region.searchAndFormat(ip);
        if(ip2reg != null){
            ip2reg.setSource("reg");
            String country = ip2reg.getCountry();
            DefinitionSelectionBean oldBean = map.get(country);

            if(oldBean != null){ //如果国家存在，那么就计数
                oldBean.getList().add(ip2reg);
                oldBean.setCount(oldBean.getCount()+1);
            }else {
                DefinitionSelectionBean bean = new DefinitionSelectionBean();
                bean.getList().add(ip2reg);
                bean.setCount(1);
                map.put(country,bean);
            }

        }
        //IpLocation ipGeo = ipQueryByGeoip2.searchAndFormat(ip);
        IpLocation ipGeo = this.query(ip,"geoLite2");
        if(ipGeo != null){
            ipGeo.setSource("geo");
            String country = ipGeo.getCountry();
            DefinitionSelectionBean oldBean = map.get(country);

            if(oldBean != null){ //如果国家存在，那么就计数
                oldBean.getList().add(ipGeo);
                oldBean.setCount(oldBean.getCount()+1);
            }else {
                DefinitionSelectionBean bean = new DefinitionSelectionBean();
                bean.getList().add(ipGeo);
                bean.setCount(1);
                map.put(country,bean);
            }
        }

        // 排序
        DefinitionSelectionBean maxBean = null;
        Collection<DefinitionSelectionBean> values = map.values();
        for (DefinitionSelectionBean bean : values){
            if(maxBean == null){
                maxBean = bean;
            }else {
                if(maxBean.getCount() < bean.getCount()){
                    maxBean = bean;
                }
            }
        }

        // 查信息
        ArrayList<IpLocation> list = maxBean.getList();
        IpLocation selectLocation = null;
        for (IpLocation ipLocation :list){
            if(selectLocation == null){
                selectLocation =ipLocation;
            }else{
                //查看-补全
                if(selectLocation.getCity() == null && ipLocation.getCity() != null){
                    selectLocation.setCity(ipLocation.getCity());
                }
                if(selectLocation.getLon() == 0d && ipLocation.getLon() != 0d){
                    selectLocation.setLon(ipLocation.getLon());
                }
                if(selectLocation.getLat() ==0d && ipLocation.getLat() != 0d){
                    selectLocation.setLat(ipLocation.getLat());
                }
                if(StringUtils.isEmpty(selectLocation.getIsp()) && StringUtils.isNotEmpty(ipLocation.getIsp())){
                    selectLocation.setIsp(ipLocation.getIsp());
                }
                if(StringUtils.isEmpty(selectLocation.getNetwork()) && StringUtils.isNotEmpty(ipLocation.getNetwork())){
                    selectLocation.setNetwork(ipLocation.getNetwork());
                }
            }
        }
        
        return selectLocation;

    }

    /**
     * 存放统计结果
     */
    @Data
    final class DefinitionSelectionBean {
        private int count;
        //ip库
        private String library;
        //实体
        private ArrayList<IpLocation> list = new ArrayList<>();
    }
}
