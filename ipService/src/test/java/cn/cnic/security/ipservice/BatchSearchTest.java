package cn.cnic.security.ipservice;

import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.dao.NtiHistoryDao;
import cn.cnic.security.ipservice.model.IpApiResponseEntity;
import cn.cnic.security.ipservice.service.BatchIpLocationService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author baokang
 * @date 2020/7/30 22:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IpServiceApp.class)
public class BatchSearchTest {
    @Autowired
    private BatchIpLocationService batchIpLocationService;

    @Autowired
    private NtiHistoryDao ntiHistoryDao;
    @Test
    public void name1() throws Exception {

        List<String> ips = new ArrayList<>();
        ips.add("14.102.128.111");
        ips.add("14.100.123.111");

        List<IpApiResponseEntity> ipApiResponseEntities = batchIpLocationService.ipApiBatchQuery(ips);
        List<IpLocation> ipLocations = batchIpLocationService.GeoipBatchIpQuery(ips);
        //ResponseEntity<String> res =  ipapiHttpClient.batchExecute(ips);
        System.out.println(JSON.toJSON(ipApiResponseEntities));
        System.out.println(JSON.toJSON(ipLocations));
    }

    @Test
    public void name2(){
        Set<String> ipSet= ntiHistoryDao.selectAllDistinctIp();

        System.out.println(ipSet.size());
    }

    @Test
    public void json(){
        List<String> ips = new ArrayList<>();
        ips.add("93.91.80.6");
        ips.add("185.152.67.14");
        //String[] ipss = {"93.91.80.6","185.152.67.14"};
        System.out.println(JSON.toJSONString(ips));
    }
}
