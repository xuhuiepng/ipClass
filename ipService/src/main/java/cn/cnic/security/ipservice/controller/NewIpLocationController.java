package cn.cnic.security.ipservice.controller;

import cn.cnic.security.ipservice.common.utils.IPUtils;
import cn.cnic.security.ipservice.common.utils.R;
import cn.cnic.security.ipservice.model.NewIpLocationEntity;
import cn.cnic.security.ipservice.service.NewIpLocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wq
 * @date 2020-09-26 17:42
 */
@RestController
@Slf4j
public class NewIpLocationController {
    @Autowired
    private NewIpLocationService newIpLocationService;

    /**
     * 按照ip库来查询
     *
     * @param ip
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/newLocation/{ip}",method = RequestMethod.GET)
    public NewIpLocationEntity queryBySource(@PathVariable String ip) throws Exception {
        if(!IPUtils.isIPv4(ip)){
            return new NewIpLocationEntity();
        }
        NewIpLocationEntity query = newIpLocationService.query(ip);
        return query;
    }
}
