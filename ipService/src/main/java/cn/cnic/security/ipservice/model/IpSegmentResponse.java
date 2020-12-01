package cn.cnic.security.ipservice.model;

/**
 * @author baokang
 * @date 2020/8/21 10:14
 */
public class IpSegmentResponse {
    private String queryIp;
    private String orgName;

    public String getQueryIp() {
        return queryIp;
    }

    public void setQueryIp(String queryIp) {
        this.queryIp = queryIp;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public IpSegmentResponse(String queryIp, String orgName) {
        this.queryIp = queryIp;
        this.orgName = orgName;
    }

    public IpSegmentResponse() {
    }
}
