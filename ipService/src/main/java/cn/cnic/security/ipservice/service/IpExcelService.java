package cn.cnic.security.ipservice.service;

import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.entity.NtiHistoryEntity;
import cn.cnic.security.ipservice.model.IpApiResponseEntity;
import cn.cnic.security.ipservice.model.IpSegmentResponse;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author baokang
 * @date 2020/7/30 22:42
 */
@Service
@Slf4j
public class IpExcelService {

    public Map<String,List<String>> excel2IpList(MultipartFile file) throws IOException {

        Map<String,List<String>> resMap = new HashMap<>();
        List<String> ips = new ArrayList<>();
        List<String> ipsErr = new ArrayList<>();

        ipsErr.add("以下是文件中出现的非法格式IP:");
        log.info("文件读取中···");

        List<Map<Integer,String>> res =  EasyExcel.read(file.getInputStream()).sheet(0).headRowNumber(0).doReadSync();
        String ipPattern_str = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";//IP正则表达式
        Pattern pattern = Pattern.compile(ipPattern_str);
        for(Map<Integer,String> ip:res){
            //IP过滤，去掉不是ip格式的数据
            if(pattern.matcher(ip.get(0)).matches()){
                ips.add(ip.get(0));
            }else{
                ipsErr.add(ip.get(0));
            }
        }
        log.info("文件读取成功");
        resMap.put("ips",ips);
        resMap.put("ipsErr",ipsErr);
        return resMap;
    }


    public List<String> excel2IpListNotFilter(MultipartFile file) throws IOException {
        List<String> ips = new ArrayList<>();
        log.info("文件读取中···");
        List<Map<Integer,String>> res =  EasyExcel.read(file.getInputStream()).sheet(0).headRowNumber(0).doReadSync();
        for(Map<Integer,String> ip:res){
            ips.add(ip.get(0));
        }
        log.info("文件读取成功");
        return ips;
    }

    public List<List<String>> getIpLocationHead(String[] heads){
        List<List<String>> headList = new ArrayList<>();
        for (String s : heads) {
            List<String> head = new ArrayList<>();
            head.add(s);
            headList.add(head);
        }
        return headList;
    }

    public <T> List<T> ErrIp2IpClass(List<String> errIps,List<T> iplist){
        if(iplist.size() == 0 || errIps.size() == 1)
            return iplist;

        if(iplist.get(0) instanceof IpLocation){
            for(String errIp:errIps){
                IpLocation ipLocation = new IpLocation();
                ipLocation.setQuery(errIp);
                iplist.add((T) ipLocation);
            }
            return iplist;

        }else if(iplist.get(0) instanceof IpApiResponseEntity){
            for(String errIp:errIps){
                IpApiResponseEntity ipApiResponseEntity = new IpApiResponseEntity();
                ipApiResponseEntity.setQuery(errIp);
                iplist.add((T) ipApiResponseEntity);
            }
            return iplist;

        }else if(iplist.get(0) instanceof IpSegmentResponse){
            for(String errIp:errIps){
                IpSegmentResponse ipSegmentResponse = new IpSegmentResponse();
                ipSegmentResponse.setQueryIp(errIp);
                iplist.add((T) ipSegmentResponse);
            }
            return iplist;
        }else if(iplist.get(0) instanceof NtiHistoryEntity){
            for(String errIp:errIps){
                NtiHistoryEntity ntiHistoryEntity = new NtiHistoryEntity();
                ntiHistoryEntity.setIp(errIp);
                iplist.add((T) ntiHistoryEntity);
            }
            return iplist;
        }

        return iplist;
    }
}

