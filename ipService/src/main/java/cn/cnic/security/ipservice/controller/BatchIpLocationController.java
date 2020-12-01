package cn.cnic.security.ipservice.controller;

import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.common.utils.R;
import cn.cnic.security.ipservice.model.IpApiResponseEntity;
import cn.cnic.security.ipservice.model.IpLocationConvertUtil;
import cn.cnic.security.ipservice.service.BatchIpLocationService;
import cn.cnic.security.ipservice.service.IpExcelService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author baokang
 * @date 2020/7/30 22:10
 */
@RestController
@Slf4j
public class BatchIpLocationController {
    @Autowired
    private BatchIpLocationService batchIpLocationService;

    @Autowired
    private IpExcelService ipExcelService;

    /**
     *批量定位查询-上传文件模式（返回文件下载流）
     *
     * @param file
     * @param type ={"remote","local"}
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/location/group", method = RequestMethod.POST, consumes="multipart/form-data")
    public void batchIpLocation(@RequestBody MultipartFile file, String type, HttpServletResponse response) throws IOException {
        if (file.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(R.error(1, "文件不存在")));
            writer.flush();
            writer.close();
        } else {
            try {
//                Map<String,List<String>> res = ipExcelService.excel2IpList(file);
//                List<String> ips = res.get("ips");
//                List<String> ipsErr = res.get("ipsErr");
                List<String> ips = ipExcelService.excel2IpListNotFilter(file);
                response.setContentType("application/vnd.ms-excel");
                response.setCharacterEncoding("utf-8");
                String[] heads = {"查询ip","数据源","国家","省级","城市","经度","纬度","运营商","ip段划分"};
                List<List<String>> headList = ipExcelService.getIpLocationHead(heads);
                if(type == null)
                    type = "local";

                switch (type) {

                    case "remote":
                        List<IpApiResponseEntity> ipApiResponseEntities = batchIpLocationService.ipApiBatchQuery(ips);
                        //ipApiResponseEntities = ipExcelService.ErrIp2IpClass(ipsErr,ipApiResponseEntities);

                        log.info("开始写文件···");
                        String ipApiFileName = URLEncoder.encode("ipLocation_remote", "UTF-8");
                        response.setHeader("Content-disposition", "attachment;filename=" + ipApiFileName + ".xlsx");
                        EasyExcel.write(response.getOutputStream(), IpApiResponseEntity.class).sheet().doWrite(ipApiResponseEntities);
                        log.info("写入成功");
                        break;
                    case "local":
                        List<IpLocation> regionIpLocations = batchIpLocationService.regionAndGeoipBatchIpQuery(ips);
                        //regionIpLocations = ipExcelService.ErrIp2IpClass(ipsErr,regionIpLocations);
                        log.info("开始写文件···");
                        String regionFileName = URLEncoder.encode("ipLocation_local", "UTF-8");
                        response.setHeader("Content-disposition", "attachment;filename=" + regionFileName + ".xlsx");
                        EasyExcel.write(response.getOutputStream(), IpLocation.class).sheet().head(headList).doWrite(regionIpLocations);
                        log.info("写入成功");
                        break;
                    default:
                        log.warn("查询源错误");
                        List<IpLocation> defaultIpLocations = batchIpLocationService.regionAndGeoipBatchIpQuery(ips);
                        log.info("开始写文件···");
                        String DefaultFileName = URLEncoder.encode("ipLocation_local", "UTF-8");
                        response.setHeader("Content-disposition", "attachment;filename=" + DefaultFileName + ".xlsx");
                        EasyExcel.write(response.getOutputStream(), IpLocation.class).sheet().head(headList).doWrite(defaultIpLocations);
                        log.info("写入成功");
                        break;

                }

            } catch (RestClientException e){
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(R.error(1, "远程查询冷却中···2分钟后再进行远程查询")));
                writer.flush();
                writer.close();
                e.printStackTrace();
                log.error("远程查询冷却中···");

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
    }

    /**
     * 批量定位查询-list查询模式（返回json数据）
     *
     * @param ips
     * @param type
     * @return
     */
    @RequestMapping(value = "/location/group", method = RequestMethod.POST, consumes="application/json")
    public R batchIpLocation2Json(@RequestBody List<String> ips,String type){
        List<String> ipList = new ArrayList<>();
        if(ips.size()>100){
            ipList.addAll(ips.subList(0,100));
        }else{
            ipList.addAll(ips);
        }

        try{
            switch (type){
                case "remote":
                    List<IpApiResponseEntity> ipApiResponseEntities = batchIpLocationService.ipApiBatchQuery(ipList);
                    List<IpLocation> ipApiIpLocations = new ArrayList<>();
                    for(IpApiResponseEntity ipApiResponseEntity:ipApiResponseEntities){
                        ipApiIpLocations.add(IpLocationConvertUtil.ipApi2IpLocation(ipApiResponseEntity));
                    }
                    return R.ok().put("ipLocations",ipApiIpLocations);
                case "local":
                    List<IpLocation> regionIpLocations = batchIpLocationService.regionAndGeoipBatchIpQuery(ipList);
                    return R.ok().put("ipLocations",regionIpLocations);
                default:
                    log.warn("查询源错误");
                    List<IpLocation> defaultIpLocations = batchIpLocationService.regionAndGeoipBatchIpQuery(ipList);
                    return R.ok().put("ipLocations",defaultIpLocations);
            }

        }catch (RestClientException e){
            e.printStackTrace();
            return R.error(-1,"远程查询冷却中···2分钟后再进行远程查询");
        }
        catch (Exception e){
            e.printStackTrace();
            return R.error(-1,"查询出错");
        }

    }


}
