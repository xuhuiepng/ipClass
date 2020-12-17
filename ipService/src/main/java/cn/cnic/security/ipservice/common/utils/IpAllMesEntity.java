package cn.cnic.security.ipservice.common.utils;

import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipservice.entity.NtiHistoryEntity;
import cn.cnic.security.ipservice.model.IpSegmentResponse;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

public class IpAllMesEntity {
    //查询ip
    private String query;
    //数据源
    private String source;
    //国家
    private String country;
    //省级
    private String province;
    //城市
    private String city;
    //经度
    private Double lat;
    //纬度
    private Double lon;
    //运营商
    private String isp;
    //ip段划分
    private String network;
    //归属机构
    private String orgName;
    //时间
    private String historyDateStr;
    //攻击类型
    private String attackTypeCN;
    //评分
    private Integer credit;
    //评分等级
    private Integer creditLevel;
    //来源
    private String cresource;

    public String getHistoryDateStr() {
        return historyDateStr;
    }

    public void setHistoryDateStr(String historyDateStr) {
        this.historyDateStr = historyDateStr;
    }

    public String getAttackTypeCN() {
        return attackTypeCN;
    }

    public void setAttackTypeCN(String attackTypeCN) {
        this.attackTypeCN = attackTypeCN;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(Integer creditLevel) {
        this.creditLevel = creditLevel;
    }

    public String getCresource() {
        return cresource;
    }

    public void setCresource(String cresource) {
        this.cresource = cresource;
    }

    public IpAllMesEntity(IpLocation ipLocation, IpSegmentResponse ipSegmentResponse, NtiHistoryEntity ntiHistoryEntity)
    {
        setQuery(ipLocation.getQuery());
        setSource(ipLocation.getSource());
        setCountry(ipLocation.getCountry());
        setProvince(ipLocation.getProvince());
        setCity(ipLocation.getCity());
        setLat(ipLocation.getLat());
        setLon(ipLocation.getLon());
        setIsp(ipLocation.getIsp());
        setNetwork(ipLocation.getNetwork());
        setOrgName(ipSegmentResponse.getOrgName());
        setHistoryDateStr(String.valueOf(ntiHistoryEntity.getHistoryDate()));
        if(getHistoryDateStr()=="null")
            setHistoryDateStr("");
        setAttackTypeCN(String.valueOf(ntiHistoryEntity.getAttackType()));
        if(getAttackTypeCN()=="null")
            setAttackTypeCN("");
        setCredit(ntiHistoryEntity.getCredit());
        setCreditLevel(ntiHistoryEntity.getCreditLevel());
        setCresource(ntiHistoryEntity.getSource());
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
