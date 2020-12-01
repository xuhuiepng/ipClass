package cn.cnic.security.ipservice;

import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.remote.IPAPIHttpClient;
import cn.cnic.security.ipservice.service.BatchIpLocationService;
import cn.cnic.security.ipservice.service.impl.AMapSearchServiceImpl;
import cn.cnic.security.ipservice.service.impl.IPAPISearchServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author xu
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IpServiceApp.class)
public class RemoteTest {

    @Autowired
    private AMapSearchServiceImpl aMapSearchService;

    @Autowired
    private IPAPISearchServiceImpl ipapiSearchService;

    @Autowired
    private IPAPIHttpClient ipapiHttpClient;

    @Autowired
    private BatchIpLocationService batchIpLocationService;

    @Test
    public void name1() {
//        String query = aMapSearchService.query("14.102.128.0");
//        System.out.println(query);

        IpLocation query1 = ipapiSearchService.quickAccess("14.102.128.0");

        System.out.println(query1);
        IpLocation query2 = ipapiSearchService.quickAccess("14.102.128.0");
        System.out.println(query2);
        IpLocation query3 = ipapiSearchService.quickAccess("14.102.128.0");

        IpLocation query4 = ipapiSearchService.quickAccess("14.102.128.111");

        System.out.println(query3);


        String ipApiQuery = ipapiSearchService.query("14.102.128.0");
        System.out.println(ipApiQuery);
        IpLocation ipLocation = ipapiSearchService.formatJsonResult(ipApiQuery);
        System.out.println(ipLocation.toString());

        System.out.println(query4.toString());

    }



}
