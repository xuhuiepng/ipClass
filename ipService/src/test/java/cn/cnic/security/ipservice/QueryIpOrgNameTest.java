package cn.cnic.security.ipservice;

import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.model.IpRangeEntity;
import cn.cnic.security.ipservice.service.IpRangeService;
import cn.cnic.security.ipservice.service.impl.AMapSearchServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = IpServiceApp.class)
public class QueryIpOrgNameTest {
    @Autowired
    private IpRangeService queryIpService;

    @Autowired
    private AMapSearchServiceImpl aMapSearchService;

    @Autowired
    private IpRangeService ipRangeService;


    /**
     * 1.测试从text中读取大量ip数据
     * 2.查询其所属研究所
     */
    @Test
    public void test() {
        /*long read_start = System.currentTimeMillis();

        String[] ips = txt2String(new File("D:\\1Project\\ipClass\\ipService\\src\\main\\resources\\without_repetition_IP.20200727.txt"));
        int count = 0;
        for (String ip : ips) {
            System.out.println(ip);
            count++;
        }

        long read_finish = System.currentTimeMillis();
        long readTime = read_finish - read_start;
        System.out.println("共"+count+"条记录" + ",读取ips共耗时: " +readTime + "ms" );*/

//        long select_start = System.currentTimeMillis();
//        List<IPRangeEntity> ipRangeEntities = queryIpService.QudgeIPArray(ips);
//        long select_finish = System.currentTimeMillis();
//        long selectTime = select_finish - select_start;
//        System.out.println("查询耗时: " + selectTime + "ms");


    }

    /**
     * 测试高德单条查询
     */
    @Test
    public void test2(){
        String res = aMapSearchService.query("8.8.8.8");
        //String res = aMapSearchService.query("1.24.81.240");
        //IpLocation ipLocation = aMapSearchService.formatJsonResult(res);
        System.out.println("res"+res);
        //System.out.println(ipLocation);
    }

    /**
     * 测试ip归属
     */
    @Test
    public void test3(){
        IpRangeEntity ipRangeEntity = ipRangeService.QudgeIPRange("1.24.81.240");
        System.out.println(ipRangeEntity);
    }


    /**
     * 测试replace
     */
    @Test
    public void test4(){
        String temp = "{\"province\":[],\"city\":[]," +
                "\"adcode\":[],\"query\":\"8.8.8.8\",\"isp\":\"[]\"," +
                "\"infocode\":\"10000\",\"rectangle\":[],\"status\":\"1\",\"info\":\"OK\"}";
        String aNull = temp.replaceAll("\\[\\]", "null");
        System.out.println(aNull);
    }

    /**
     * 读取txt文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String[] txt2String(File file) {
        String[] result = new String[88203];
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String str = null;
            int i = 0;
            while ((str = br.readLine()) != null) {//使用readLine方法，一次读一行
                result[i] = str;
                i++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
