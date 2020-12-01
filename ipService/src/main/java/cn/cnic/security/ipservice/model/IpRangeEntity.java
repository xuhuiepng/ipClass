package cn.cnic.security.ipservice.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * ip所属实体
 * @author wq
 * @date 2020-07-28 10:58
 */
@Deprecated
@Data
public class IpRangeEntity {
    /**
     * ip号码段(cidr)
     */
    @ExcelProperty(index=0)
    private String ipRange;

    @ExcelProperty(index = 1)
    private String end;
    /**
     * 机构名称
     */
    @ExcelProperty(index=2)
    private String orgName;

    @Override
    public String toString() {
        return "IPRangeEntity{" +
                "ipRange='" + ipRange + '\'' +
                ", orgName='" + orgName + '\'' +
                '}';
    }
}
