package cn.cnic.security.ipservice;

import cn.cnic.security.ipcore.IpQuery2qqwry;
import cn.cnic.security.ipcore.IpQuery2region;
import cn.cnic.security.ipcore.IpQueryByGeoip2;
import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.common.utils.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 本地库测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IpServiceApp.class)
public class LocaldbTest {
    @Autowired
    private IpQuery2region ipQuery2region;
    @Autowired
    private IpQuery2qqwry ipQuery2qqwry;
    @Autowired
    private IpQueryByGeoip2 ipQueryByGeoip2;

    @Test
    public void name1(){
        String ip = "124.64.16.67";
        try {

            String line = ipQuery2region.localSearch(ip);

            System.out.println(line);
            System.out.println("--------------");
            //System.out.println(ipQueryByGeoip2.localSearch(ip));
            System.out.println(ipQueryByGeoip2.searchAndFormat(ip));
            System.out.println("--------------");
            System.out.println(ipQuery2qqwry.localSearch(ip));



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void name2(){
        String ip = "182.48.98.168";
        try {
            String res = ipQueryByGeoip2.localSearch(ip);
            System.out.println(res);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
