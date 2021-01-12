package cn.cnic.security.ipservice.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.common.utils.PageUtils;
import cn.cnic.security.ipservice.model.IpApiResponseEntity;
import cn.cnic.security.ipservice.service.IpExcelService;
import cn.cnic.security.ipservice.service.impl.NtiHistoryServiceImpl;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.cnic.security.ipservice.entity.NtiHistoryEntity;
import cn.cnic.security.ipservice.service.NtiHistoryService;
import cn.cnic.security.ipservice.common.utils.R;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * 绿盟历史信息表
 * 信誉ip查询
 *
 * @author xuhuipeng
 * @email xuhuipeng@cnic.com
 * @date 2020-08-13 17:26:33
 */
@RestController
@Slf4j
public class NtiHistoryController {

    @Autowired
    private NtiHistoryService ntiHistoryService;

    @Autowired
    private NtiHistoryServiceImpl ntiHistoryServiceImpl;

    @Autowired
    private IpExcelService ipExcelService;

    /**
     * 列表
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params){
        if(params == null || params.isEmpty()){
            params = new HashMap<>();
            params.put("currPage",1);
            params.put("pageSize",10);
        }

        PageUtils page = ntiHistoryService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 单点信誉查询
     *
     * @param ip
     * @param lib
     * @return
     */
    @RequestMapping(value = "/credit/{ip}",method = RequestMethod.GET)
    public R info(@PathVariable("ip") String ip,String lib){
        QueryWrapper<NtiHistoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ip)
                .select(NtiHistoryEntity.class,i->!i.getProperty().equals("attackTypeCN"));
        List<NtiHistoryEntity> list = ntiHistoryService.list(queryWrapper);
        List<NtiHistoryEntity> resList = new ArrayList<>();
        for (NtiHistoryEntity ntiEntity : list) {
            NtiHistoryEntity ntiHistoryEntity = ntiHistoryServiceImpl.attackCode2CN(ntiEntity);
            NtiHistoryEntity ntiHistoryEntityFormatDate = ntiHistoryServiceImpl.formatDate(ntiHistoryEntity);
            resList.add(ntiHistoryEntityFormatDate);
        }
        return R.ok().put("resList", resList);
    }


    /**
     * 批量查询信誉历史
     * @param file
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/credit/group", method = RequestMethod.POST,consumes="multipart/form-data")
    public void batchIpNtihistory(@RequestBody MultipartFile file, HttpServletResponse response) throws IOException {
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
                //未过滤读取
                List<String> ips = ipExcelService.excel2IpListNotFilter(file);
//                //去重
//                List<String> ips_distinct = ips.stream().distinct().collect(Collectors.toList());

                response.setContentType("application/vnd.ms-excel");
                response.setCharacterEncoding("utf-8");
                String[] heads = {"ip","时间","攻击类型","评分","评分等级","来源"};
                List<List<String>> headList = ipExcelService.getIpLocationHead(heads);
                List<NtiHistoryEntity> ntiHistoryEntities = ntiHistoryService.ntihistoryBatchIpQuery(ips);
                //ntiHistoryEntities = ipExcelService.ErrIp2IpClass(ipsErr,ntiHistoryEntities);
                log.info("开始写文件···");
                String DefaultFileName = URLEncoder.encode("batchIpNtihistory", "UTF-8");
                response.setHeader("Content-disposition", "attachment;filename=" + DefaultFileName + ".xlsx");
                EasyExcel.write(response.getOutputStream(), IpLocation.class).sheet().head(headList).doWrite(ntiHistoryEntities);
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
    }

}
