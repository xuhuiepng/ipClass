package cn.cnic.security.ipservice;

import cn.cnic.security.ipservice.common.utils.IntelligenceEntity;
import cn.cnic.security.ipservice.dao.VxvaultDao;
import cn.cnic.security.ipservice.service.BatchIpLocationService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IpServiceApp.class)
public class allDaoTest {

    @Autowired
    private VxvaultDao vxvaultDao;

    @Test
    public void test()
    {
        Page<IntelligenceEntity> myPage = new Page<>(1,20);
        vxvaultDao.getPublicData(myPage);
        for(IntelligenceEntity a: myPage.getRecords())
            System.out.println(a);
    }

}
