package cn.cnic.security.ipservice.controller;

import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.common.utils.R;
import cn.cnic.security.ipservice.model.IpSegmentResponse;
import cn.cnic.security.ipservice.service.InstituteInquireService;
import cn.cnic.security.ipservice.service.IpSelection;
import cn.cnic.security.ipservice.service.SimpleIpLocationService;
import cn.cnic.security.ipservice.service.impl.AMapSearchServiceImpl;
import cn.cnic.security.ipservice.service.impl.IPAPISearchServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


/**
 * ip查询
 */
@RestController
@Slf4j
public class SimpleIpLocationContorller {

    @Autowired
    @Qualifier("simpleIpLocationService")
    private SimpleIpLocationService simpleIpLocationService;

    @Autowired
    @Qualifier("ipSelection")
    private IpSelection ipSelection;

    @Autowired
    private InstituteInquireService ipRangeService;

    @Autowired
    private AMapSearchServiceImpl aMapSearchService;

    @Autowired
    private IPAPISearchServiceImpl ipapiSearchService;


    /**
     * 根据国家统计结果 返回次数最多的
     * 综合评分查询
     *
     * @param ip
     * @return
     */
    @RequestMapping(value = "/election/{ip}",method = RequestMethod.GET)
    public R queryAndSelect(@PathVariable String ip) throws Exception {
        IpLocation query = ipSelection.byScore(ip);
        if (query != null) {
            query.setQuery(ip);
        }
        R ok = R.ok();
        return R.convertBeanToR(query, ok);
    }

    /**
     * 按照ip库来查询
     *
     * @param ip
     * @param source
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/location/{ip}",method = RequestMethod.GET)
    public R queryBySource(@PathVariable String ip, String source) throws Exception {
        IpLocation query = simpleIpLocationService.query(ip, source);
        R ok = R.ok();
        return R.convertBeanToR(query, ok);
    }


    /**
     * 全部信息查询
     *
     * @param ip
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "ipInfo/{ip}",method = RequestMethod.GET)
    public R queryIpInfo(@PathVariable String ip) throws Exception {
        if (ip.length() == 0 || "0.0.0.0".equals(ip)) {
            log.info("输入ip为空或0.0.0.0，自动设为本机ip");
            InetAddress local = null;
            String localIp = "";
            try {
                local = local.getLocalHost();
                //String localname = local.getHostName();
                localIp = local.getHostAddress();
                ip = localIp;
                //log.info("本机的ip是 ：" + localIp);
            } catch (Exception e) {
                log.info("获取本机ip失败");
            }
        }

        //三种本地库方式
        IpLocation ip2region = null;
        IpLocation qqwry = null;
        IpLocation geoLite2 = null;
        try {
            ip2region = simpleIpLocationService.query(ip, "ip2region");
        } catch (Exception e) {
            log.info("ip2region查询错误 {}", e.getMessage());
        }

        try {
            qqwry = simpleIpLocationService.query(ip, "qqwry");
        } catch (Exception e) {
            log.info("qqwry查询错误{}",e.getMessage());
        }

        try {
            geoLite2 = simpleIpLocationService.query(ip, "geoLite2");
        } catch (Exception e) {
            log.info("geoLite2查询错误{}",e.getMessage());
        }

        //2种远程方式
        IpLocation aMapIpLocation = null;
        try {
            String aMap = aMapSearchService.query(ip);
            aMapIpLocation = aMapSearchService.formatJsonResult(aMap);
        } catch (Exception e) {
            log.error("高德ip查询出错！ip为国外或格式错误！{}", e.getMessage());
        }
        IpLocation ipApiLocation = null;
        try {
            String ipApi = ipapiSearchService.query(ip);
            ipApiLocation = ipapiSearchService.formatJsonResult(ipApi);
        } catch (Exception e) {
            log.error("ipApi查询出错！{}", e.getMessage());
        }


        //查询ip归属
        List<IpSegmentResponse> ipSegmentResponses = null;
        try {
            ArrayList<String> segmentList = new ArrayList<>(1);
            segmentList.add(ip);
            ipSegmentResponses = ipRangeService.searchOrgByList(segmentList);
            //如果ip归属未研究所，替换运行商
            if(!ipSegmentResponses.isEmpty()) {
                IpSegmentResponse se = ipSegmentResponses.get(0);
                if (!StringUtils.equals("该IP非研究所IP", se.getOrgName())) {
                    ip2region.setIsp(se.getOrgName());
                    qqwry.setIsp(se.getOrgName());
                    geoLite2.setIsp(se.getOrgName());
                    aMapIpLocation.setIsp(se.getOrgName());
                    ipApiLocation.setIsp(se.getOrgName());
                }
            }

        } catch (Exception e) {
            log.error("ip归属 {}", e.getMessage());
        }

        List<IpLocation> list = new ArrayList<>();
        list.add(ip2region);
        list.add(qqwry);
        list.add(geoLite2);
        list.add(aMapIpLocation);
        list.add(ipApiLocation);

        R ok = R.ok();
        ok.put("ipSegmentResponses", ipSegmentResponses);
        ok.put("list", list);

        return ok;
    }

}
