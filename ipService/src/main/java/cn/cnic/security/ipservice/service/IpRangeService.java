package cn.cnic.security.ipservice.service;

import cn.cnic.security.ipservice.common.utils.IPUtils;
import cn.cnic.security.ipservice.common.utils.IpDataListener;
import cn.cnic.security.ipservice.model.IpRangeEntity;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ip所属研究所查询service
 * @author wq
 * @date 2020-07-28 9:55
 */
@Service
@ConfigurationProperties(prefix = "ipfile-config.path")
@Slf4j
@Deprecated
public class IpRangeService {

    private String file;

    private static List<IpRangeEntity> datas = new ArrayList<>();

    /**
     * 查询ip所属研究所
     * @param ip
     * @return
     */
    public IpRangeEntity QudgeIPRange(String ip) {
        if(datas.size()==0||datas.isEmpty()){
            // 这里默认读取第一个sheet
            InputStream inputStream = null;
            try {

                inputStream = this.getClass().getClassLoader().getResourceAsStream(file);
                datas = EasyExcel.read(inputStream, IpRangeEntity.class, new IpDataListener()).sheet().doReadSync();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        boolean inRange = false;
        IpRangeEntity ipRangeEntity = new IpRangeEntity();
        for (int i = 0; i < datas.size(); i++) {
            try {
                ipRangeEntity = datas.get(i);
                if(IPUtils.isIPv4(ipRangeEntity.getEnd())){
                    if(ipRangeEntity.getIpRange()==null) {
                        continue;
                    }
                    if (IPUtils.isInRange(ip, ipRangeEntity.getIpRange())) {
                        inRange = true;
                        break;
                    }
                }
            } catch (Exception e) {
                log.info("查询cidr出错");
                //e.printStackTrace();
            }
        }
        if (inRange==false) {
            //没查到的情况
            ipRangeEntity.setIpRange("null");
            ipRangeEntity.setOrgName("未查到机构");
        }
        return ipRangeEntity;
    }

    public  void setFile(String file) {
        this.file = file;
    }

    public  String getFile() {
        return file;
    }
}
