package cn.cnic.security.ipservice.service;

import cn.cnic.security.ipservice.common.utils.IPUtils;
import cn.cnic.security.ipservice.model.IpSegment;
import cn.cnic.security.ipservice.model.IpSegmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baokang
 * @date 2020/8/20 16:00
 */
@Service
public class InstituteInquireService {

    @Autowired
    @Qualifier("ipSegmentList")
    private List<IpSegment> list;

    public IpSegment searchOrg(String ip) {
        IpSegment ipSegment = null;
        if(IPUtils.isIPv4(ip)){
            long ipL = IPUtils.ip2long(ip);
            ipSegment = binarySearchIpSegment(ipL);
        }
        return ipSegment;
    }

    public List<IpSegmentResponse> searchOrgByList(List<String> ips) {
        List<IpSegmentResponse> ipSegmentResponseList = new ArrayList<>();
        for (String ip : ips) {
            if(IPUtils.isIPv4(ip)){
                IpSegment ipSegment = binarySearchIpSegment(IPUtils.ip2long(ip));
                if (ipSegment == null)
                    ipSegmentResponseList.add(new IpSegmentResponse(ip, "该IP非研究所IP"));
                else
                    ipSegmentResponseList.add(new IpSegmentResponse(ip, ipSegment.getOrgName()));
            }else{
                ipSegmentResponseList.add(new IpSegmentResponse(ip, "该IP非研究所IP"));
            }

        }
        return ipSegmentResponseList;
    }


    private IpSegment binarySearchIpSegment(long ipL) {
        int left = 0;
        int right = this.list.size() - 1;
        int mid = (left + right) / 2;

        //二分法查找
        while (left <= right) {
            if (ipL < list.get(left).getStart() || ipL > list.get(right).getEnd()) return null;

            if (list.get(mid).getStart() <= ipL && list.get(mid).getEnd() >= ipL) {
                return list.get(mid);
            } else if (list.get(mid).getStart() > ipL) {
                right = mid - 1;
            } else if (list.get(mid).getEnd() < ipL) {
                left = mid + 1;
            }
            mid = (left + right) / 2;
        }

        return null;
    }


}
