package cn.cnic.security.ipservice.controller;

import cn.cnic.security.ipservice.common.utils.R;
import cn.cnic.security.ipservice.model.IpApiResponseEntity;
import cn.cnic.security.ipservice.model.IpRangeEntity;
import cn.cnic.security.ipservice.model.IpSegment;
import cn.cnic.security.ipservice.model.IpSegmentResponse;
import cn.cnic.security.ipservice.service.IpExcelService;
import cn.cnic.security.ipservice.service.IpRangeService;
import cn.cnic.security.ipservice.service.InstituteInquireService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.cnic.security.ipservice.common.utils.TXTUtils.txt2String;

/**
 * ip所属研究所查询
 *
 * @author wq
 * @date 2020-08-03 15:24
 */
@RestController
@Slf4j
public class InstituteInquireController {

    @Autowired
    private IpRangeService ipRangeService;

    @Autowired
    private InstituteInquireService instituteInquireService;

    @Autowired
    private IpExcelService ipExcelService;


    /**
     * 单点归属研究所查询
     *
     * @param ip
     * @return
     */
    @RequestMapping(value = "/institute_inquire/{ip}", method = RequestMethod.GET)
    public R queryIp2(@PathVariable String ip) {
        IpSegment ipSegment = instituteInquireService.searchOrg(ip);
        if (ipSegment == null)
            return R.error(1, "该IP非研究所IP");

        return R.ok().put("org", new IpSegmentResponse(ip, ipSegment.getOrgName()));
    }

    /**
     * 归属批量查询-list查询模式（返回json数据）
     *
     * @param ips
     * @return
     */
    @RequestMapping(value = "/institute_inquire/group", method = RequestMethod.POST, consumes = "application/json")
    public R queryIpSegmentByList(@RequestBody List<String> ips) {
        List<IpSegmentResponse> ipSegmentResponseList;
        try {
            ipSegmentResponseList = instituteInquireService.searchOrgByList(ips);
        } catch (Exception e) {
            return R.error();
        }
        return R.ok().put("ipSegmentResponseList", ipSegmentResponseList);
    }


    /**
     * 归属批量查询-文件上传模式（返回文件下载流）
     *
     * @param file
     * @param response
     */
    @RequestMapping(value = "/institute_inquire/group", method = RequestMethod.POST, consumes = "multipart/form-data")
    public void queryIpSegmentByFile(@RequestBody MultipartFile file, HttpServletResponse response) {
        try {
            if (file == null || file.isEmpty()) {
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(R.error(1, "文件不存在")));
                writer.flush();
                writer.close();
            } else {
                try {
//
                    List<String> ips = ipExcelService.excel2IpListNotFilter(file);
                    response.setContentType("application/vnd.ms-excel");
                    response.setCharacterEncoding("utf-8");
                    String[] heads = {"查询ip", "归属机构"};
                    List<List<String>> headList = ipExcelService.getIpLocationHead(heads);
                    List<IpSegmentResponse> ipSegmentResponseList = instituteInquireService.searchOrgByList(ips);

                    log.info("开始写文件···");
                    String ipSegmentFileName = URLEncoder.encode("ipSegmentSearchRes", "UTF-8");
                    response.setHeader("Content-disposition", "attachment;filename=" + ipSegmentFileName + ".xlsx");
                    EasyExcel.write(response.getOutputStream(), IpApiResponseEntity.class).sheet().head(headList).doWrite(ipSegmentResponseList);
                    log.info("写入成功");
                } catch (Exception e) {
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(JSON.toJSONString(R.error(1, "文件读取/写入失败")));
                    writer.flush();
                    writer.close();
                    log.error("文件读取/写入失败");
                    e.printStackTrace();
                }

            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    /**
     * 查询ip所属研究所（弃用）
     *
     * @param ip
     * @return
     */
    //@GetMapping("/queryIpBeLong/{ip}")
    @Deprecated
    public R queryIp(@PathVariable String ip) {
        IpRangeEntity ipRangeEntity = ipRangeService.QudgeIPRange(ip);
        if (ipRangeEntity.getIpRange() != null) {
            return R.ok().put("ipRangeEntity", ipRangeEntity);
        }


        return R.error().put("msg", "库中未查到ip");
    }


    /**
     * 批量查询ips所属研究所（弃用）
     * ips来源txt文件
     *
     * @return
     */
    //@GetMapping("/queryIpsBeLong")
    @Deprecated
    public R queryIp() {
        String[] ips = txt2String(new File("D:\\1Project\\ipClass\\ipService\\src\\main\\resources\\without_repetition_IP.20200727.txt"));
        int count = ips.length;

        List<IpRangeEntity> ipRangeEntitiyList = new ArrayList<>();
        for (String ip : ips) {
            //System.out.println(ip);
            IpRangeEntity ipRangeEntity = ipRangeService.QudgeIPRange(ip);
            //System.out.println(ipRangeEntity);
            ipRangeEntitiyList.add(ipRangeEntity);
        }
        return R.ok().put("ipRangeEntitiyList", ipRangeEntitiyList);
    }
}
