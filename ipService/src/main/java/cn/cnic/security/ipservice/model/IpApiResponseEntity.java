package cn.cnic.security.ipservice.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

public class IpApiResponseEntity {
    /**
     * 请求状态
     */

    @ExcelIgnore
    private String status;

    /**
     * 国家
     */
    @ExcelProperty(value = "国家", index = 1)
    private String country;

    /**
     * 国家编号
     */
    @ExcelIgnore
    private String countryCode;

    /**
     * 地区代号
     */
    @ExcelIgnore
    private String region;

    /**
     * 地区名
     */
    @ExcelProperty(value = "省级", index = 2)
    private String regionName;

    /**
     * 城市名
     */
    @ExcelProperty(value = "城市", index = 3)
    private String city;

    @ExcelIgnore
    private String zip;
    /**
     * 经度
     */
    @ExcelProperty(value = "经度", index = 4)
    private Double lat;
    /**
     * 纬度
     */
    @ExcelProperty(value = "纬度", index = 5)
    private Double lon;

    /**
     * 时区
     */
    @ExcelIgnore
    private String timezone;

    @ExcelProperty(value = "运营商", index = 6)
    private String isp;

    /**
     * 所属机构
     */
    @ExcelIgnore
    private String org;

    @ExcelIgnore
    private String as;

    @ExcelProperty(value = "查询ip", index = 0)
    private String query;

    /**
     * 错误信息
     */
    @ExcelIgnore
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
