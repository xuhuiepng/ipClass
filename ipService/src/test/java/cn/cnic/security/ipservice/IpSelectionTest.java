package cn.cnic.security.ipservice;

import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.service.IpSelection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IpServiceApp.class)
public class IpSelectionTest {
    @Autowired
    private IpSelection ipSelection;

    @Test
    public void name1(){
        IpLocation ipLocation = ipSelection.byScore("77.78.160.81");
        System.out.println(ipLocation);

    }

}
