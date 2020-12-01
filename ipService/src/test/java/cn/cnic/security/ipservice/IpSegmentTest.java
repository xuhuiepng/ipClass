package cn.cnic.security.ipservice;

import cn.cnic.security.ipservice.model.IpSegment;
import cn.cnic.security.ipservice.service.InstituteInquireService;
import com.alibaba.excel.EasyExcel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author baokang
 * @date 2020/8/21 9:05
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IpServiceApp.class)
public class IpSegmentTest {

    @Autowired
    InstituteInquireService instituteInquireService;

    @Test
    public void test(){
        String ipPath = "C:\\Users\\BK\\Desktop\\cnic_git\\ipClass\\ipService\\src\\test\\resources\\ip列表.xlsx";
        List<Map<Integer,String>> res =  EasyExcel.read(ipPath).sheet(0).headRowNumber(0).doReadSync();
        List<String> ips = new ArrayList<>();
        String ipPattern_str = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";//IP正则表达式
        Pattern pattern = Pattern.compile(ipPattern_str);
        for(Map<Integer,String> ip:res){
            //IP过滤，去掉不是ip格式的数据
            if(pattern.matcher(ip.get(0)).matches()){
                ips.add(ip.get(0));
            }
        }
        for(String ip:ips){
            IpSegment ipSegment = instituteInquireService.searchOrg(ip);
            if(!(ipSegment.getStart()==0L&&ipSegment.getEnd()==0L))
                System.out.println(ip+"=>"+ instituteInquireService.searchOrg(ip).toString());
        }

    }

}
