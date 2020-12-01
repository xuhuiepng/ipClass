package cn.cnic.security.ipcore.model;

import java.util.StringJoiner;

/**
 * ip定位信息 基本类
 */
public class IpLocation {
    //查询ip
    private String query;
    //数据源
    private String source;

    //国家 必须有
    private String country;

    //省级
    private String province;
    //城市
    private String city;

    private Double lat;

    private Double lon;
    //运行商
    private String isp;

    //ip段划分
    private String network;
    

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

    public IpLocation() {
    }

    public IpLocation(String country, String area, String isp) {
        this.country = country;
        this.city = area;
        this.isp = isp;
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", IpLocation.class.getSimpleName() + "[", "]")
                .add("ip='" + query + "'")
                .add("source='" + source + "'")
                .add("country='" + country + "'")
                .add("province='" + province + "'")
                .add("city='" + city + "'")
                .add("lat=" + lat)
                .add("lon=" + lon)
                .add("isp='" + isp + "'")
                .add("network='" + network + "'")
                .toString();
    }
}
