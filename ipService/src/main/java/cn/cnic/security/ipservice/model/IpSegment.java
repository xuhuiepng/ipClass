package cn.cnic.security.ipservice.model;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author baokang
 * @date 2020/8/20 16:08
 */
public class IpSegment implements Comparable<IpSegment> {


    @ExcelProperty(index = 0)
    private String ipRange;


    @ExcelProperty(index = 1)
    private String endStr;


    @ExcelProperty(index = 2)
    private String orgName;


    //start end 属性用于排序
    private Long start;

    private Long end;


    public String getIpRange() {
        return ipRange;
    }

    public void setIpRange(String ipRange) {
        this.ipRange = ipRange;
    }

    public String getEndStr() {
        return endStr;
    }

    public void setEndStr(String endStr) {
        this.endStr = endStr;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    @Override
    public int compareTo(IpSegment o) {

        if (this.start < o.start)
            return -1;
        else if (this.start > o.start)
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "IpSegment{" +
                "ipRange='" + ipRange + '\'' +
                ", endStr='" + endStr + '\'' +
                ", orgName='" + orgName + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
