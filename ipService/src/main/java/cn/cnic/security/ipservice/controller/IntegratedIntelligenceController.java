package cn.cnic.security.ipservice.controller;

import cn.cnic.security.ipservice.common.utils.IntelligenceEntity;
import cn.cnic.security.ipservice.entity.*;
import cn.cnic.security.ipservice.service.ConditionQueryService;
import cn.cnic.security.ipservice.service.IntegratedIntelligenceService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 综合情报查询
 *
 * @author cxl
 * @date 2020-11-23 16:00
 */
@Controller
public class IntegratedIntelligenceController {

    @Autowired
    IntegratedIntelligenceService integratedintelligence;
    @Autowired
    ConditionQueryService conditionQueryService;
    /**
     *分页查询所有情报数据
     */
    @RequestMapping("/getAllintelligence")
    @ResponseBody
    public String getAllintelligence(@RequestParam(name = "page",required = false,defaultValue = "1") Integer pagenum,@RequestParam(name = "size",required = false,defaultValue = "20") Integer size){
        Map<String,Object> map = new HashMap<>();
        try {
            List<IntelligenceEntity> re = integratedintelligence.getAllintelligence(pagenum, size);

            map.put("data",re);
            map.put("code","0");
            map.put("msg","success");
        }
        catch (Exception e)
        {
            map.put("code","1");
            map.put("msg","failure");
        }

        return JSON.toJSONString(map);
    }
    /**
     *分页查询漏洞平台
     */
    @RequestMapping(value = "/getCnvdSpider")
    @ResponseBody
    public String getCnvdSpider(@RequestParam(value = "startTime") String startTime,@RequestParam(value = "endTime") String endTime,@RequestParam(value = "content",required=false) String content,@RequestParam(value = "level" ,required=false) String level,@RequestParam(value = "page",required = false,defaultValue = "1") String page,@RequestParam(value = "size",required = false,defaultValue = "10") String size){
        Map<String,Object> map = new HashMap<>();
        try {
            Integer pageNum = Integer.valueOf(page);
            Integer pageSize = Integer.valueOf(size);
            if(startTime == null || endTime == null){
                map.put("msg","查询时间为空");
                map.put("code","2");
            }else {
                Date st = new Date();
                Date et = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    st = formatter.parse(startTime);
                    et = formatter.parse(endTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Page<CnvdSpiderEntity> myPage = new Page<CnvdSpiderEntity>(pageNum, pageSize);
                conditionQueryService.getCnvdSpider(myPage,st,et,content,level);
                map.put("sum",myPage.getTotal());
                map.put("data", myPage.getRecords());
                map.put("msg","success");
                map.put("code","0");
            }
        }catch (Exception e){
            map.put("msg","failure");
            map.put("code","1");
        }
        return JSON.toJSONString(map);
    }
    /**
     *分页查询情报https://redqueen.tj-un.com/AllFilterIntel.html?key_word=%E6%BC%8F%E6%B4%9E%E5%88%A9%E7%94%A8&type=tag
     */
    @RequestMapping(value = "/getRedqueen")
    @ResponseBody
    public String getRedqueen(@RequestParam(value = "startTime") String startTime,@RequestParam(value = "endTime") String endTime,@RequestParam(value = "content",required=false) String content,@RequestParam(value = "tag",required=false) String tag,@RequestParam(value = "page",required = false,defaultValue = "1") String page,@RequestParam(value = "size",required = false,defaultValue = "10") String size){
        Map<String,Object> map = new HashMap<>();
        try {
            Integer pageNum = Integer.valueOf(page);
            Integer pageSize = Integer.valueOf(size);
            if(startTime == null || endTime == null){
                map.put("msg","查询时间为空");
                map.put("code","2");
            }else {
                Date st = new Date();
                Date et = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    st = formatter.parse(startTime);
                    et = formatter.parse(endTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Page<RedqueenEntity> myPage = new Page<RedqueenEntity>(pageNum, pageSize);
                conditionQueryService.getRedqueen(myPage,st,et,content,tag);
                map.put("sum",myPage.getTotal());
                map.put("data",myPage.getRecords());
                map.put("msg","success");
                map.put("code","0");
            }
        }catch (Exception e){
            map.put("msg","failure");
            map.put("code","1");
        }
        return JSON.toJSONString(map);
    }
    /**
     *分页查询漏洞平台http://www.scap.org.cn/vulns?view=global
     */
    @RequestMapping(value = "/getScapSpider")
    @ResponseBody
    public String getScapSpider(@RequestParam(value = "startTime") String startTime,@RequestParam(value = "endTime") String endTime,@RequestParam(value = "content",required = false) String content,@RequestParam(value = "page",required = false,defaultValue = "1") String page,@RequestParam(value = "size",required = false,defaultValue = "10") String size){
        Map<String,Object> map = new HashMap<>();
        try {
            Integer pageNum = Integer.valueOf(page);
            Integer pageSize = Integer.valueOf(size);
            if(startTime == null || endTime == null){
                map.put("msg","查询时间为空");
                map.put("code","2");
            }else {
                Date st = new Date();
                Date et = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    st = formatter.parse(startTime);
                    et = formatter.parse(endTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Page<ScapSpiderEntity> myPage = new Page<ScapSpiderEntity>(pageNum, pageSize);
                conditionQueryService.getScapSpider(myPage,st, et, content);
                map.put("sum",myPage.getTotal());
                map.put("data",myPage.getRecords());
                map.put("msg","success");
                map.put("code","0");
            }
        }catch (Exception e){
            map.put("msg","failure");
            map.put("code","1");
        }
        return JSON.toJSONString(map);
    }
    /**
     *分页查询漏洞公告https://security.360.cn/News/affiche
     */
    @RequestMapping(value = "/getSecuritySpider")
    @ResponseBody
    public String getSecuritySpider(@RequestParam(value = "startTime") String startTime,@RequestParam(value = "endTime") String endTime,@RequestParam(value = "content",required = false) String content,@RequestParam(value = "level",required = false) String level,@RequestParam(value = "page",required = false,defaultValue = "1") String page,@RequestParam(value = "size",required = false,defaultValue = "10") String size){
        Map<String,Object> map = new HashMap<>();
        try {
            Integer pageNum = Integer.valueOf(page);
            Integer pageSize = Integer.valueOf(size);
            if(startTime == null || endTime == null){
                map.put("msg","查询时间为空");
                map.put("code","2");
            }else {
                Date st = new Date();
                Date et = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    st = formatter.parse(startTime);
                    et = formatter.parse(endTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Page<SecuritySpiderEntity> myPage = new Page<SecuritySpiderEntity>(pageNum, pageSize);
                conditionQueryService.getSecuritySpider(myPage,st, et, content, level);
                map.put("sum",myPage.getTotal());
                map.put("data",myPage.getRecords());
                map.put("msg","success");
                map.put("code","0");
            }
        }catch (Exception e){
            map.put("msg","failure");
            map.put("code","1");
        }
        return JSON.toJSONString(map);
    }
    /**
     *分页查询漏洞平台https://www.seebug.org/vuldb/vulnerabilities?page=1
     */
    @RequestMapping(value = "/getSeebugSpider")
    @ResponseBody
    public String getSeebugSpider(@RequestParam(value = "startTime") String startTime,@RequestParam(value = "endTime") String endTime,@RequestParam(value = "content",required = false) String content,@RequestParam(value = "type",required = false) String type,@RequestParam(value = "page",required = false,defaultValue = "1") String page,@RequestParam(value = "size",required = false,defaultValue = "10") String size){
        Map<String,Object> map = new HashMap<>();
        try {
            Integer pageNum = Integer.valueOf(page);
            Integer pageSize = Integer.valueOf(size);
            if(startTime == null || endTime == null){
                map.put("msg","查询时间为空");
                map.put("code","2");
            }else {
                Date st = new Date();
                Date et = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    st = formatter.parse(startTime);
                    et = formatter.parse(endTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Page<SeebugSpiderEntity> myPage = new Page<SeebugSpiderEntity>(pageNum, pageSize);
                conditionQueryService.getSeebugSpider(myPage,st, et, content, type);
                map.put("sum",myPage.getTotal());
                map.put("data",myPage.getRecords());
                map.put("msg","success");
                map.put("code","0");
            }
        }catch (Exception e){
            map.put("msg","failure");
            map.put("code","1");
        }
        return JSON.toJSONString(map);
    }
    /**
     *分页查询恶意软件和urls的收集网站http://vxvault.net/ViriList.php?s=0&m=40
     */
    @RequestMapping(value = "/getVxvault")
    @ResponseBody
    public String getVxvault(@RequestParam(value = "startTime") String startTime,@RequestParam(value = "endTime") String endTime,@RequestParam(value = "content",required = false) String content,@RequestParam(value = "ip",required = false) String ip,@RequestParam(value = "page",required = false,defaultValue = "1") String page,@RequestParam(value = "size",required = false,defaultValue = "10") String size){
        Map<String,Object> map = new HashMap<>();
        try {
            Integer pageNum = Integer.valueOf(page);
            Integer pageSize = Integer.valueOf(size);
            if(startTime == null || endTime == null){
                map.put("msg","查询时间为空");
                map.put("code","2");
            }else {
                Date st = new Date();
                Date et = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    st = formatter.parse(startTime);
                    et = formatter.parse(endTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Page<VxvaultEntity> myPage = new Page<VxvaultEntity>(pageNum, pageSize);
                conditionQueryService.getVxvault(myPage,st, et, content, ip);
                map.put("sum",myPage.getTotal());
                map.put("data",myPage.getRecords());
                map.put("msg", "success");
                map.put("code", "0");
            }
        }catch (Exception e){
            map.put("msg","failure");
            map.put("code","1");
        }
        return JSON.toJSONString(map);
    }
    /**
     *分页查询全球被黑站点统计系统https://www.hacked.com.cn/index.php?page=1
     */
    @RequestMapping(value = "/getZoneToday")
    @ResponseBody
    public String getZoneToday(@RequestParam(value = "startTime") String startTime,@RequestParam(value = "endTime") String endTime,@RequestParam(value = "content",required = false) String content,@RequestParam(value = "country",required = false) String country,@RequestParam(value = "os",required = false) String os,@RequestParam(value = "page",required = false,defaultValue = "1") String page,@RequestParam(value = "size",required = false,defaultValue = "10") String size){
        Map<String,Object> map = new HashMap<>();
        try {
            Integer pageNum = Integer.valueOf(page);
            Integer pageSize = Integer.valueOf(size);
            if(startTime == null || endTime == null){
                map.put("msg","查询时间为空");
                map.put("code","2");
            }else {
                Date st = new Date();
                Date et = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    st = formatter.parse(startTime);
                    et = formatter.parse(endTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Page<ZoneTodayEntity> myPage = new Page<ZoneTodayEntity>(pageNum, pageSize);
                conditionQueryService.getZoneToday(myPage,st, et, content, country, os);
                map.put("sum",myPage.getTotal());
                map.put("data",myPage.getRecords());
                map.put("msg", "success");
                map.put("code", "0");
            }
        }catch (Exception e){
            map.put("msg","failure");
            map.put("code","1");
        }
        return JSON.toJSONString(map);
    }


}
