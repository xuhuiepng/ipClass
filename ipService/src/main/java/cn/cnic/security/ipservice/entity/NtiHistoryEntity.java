package cn.cnic.security.ipservice.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 绿盟历史信息表
 * 
 * @author xuhuipeng
 * @email xuhuipeng@cnic.com
 * @date 2020-08-13 17:26:33
 */
@Data
@TableName("nti_history")
@NoArgsConstructor
public class NtiHistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@ExcelIgnore
	private Date historyDate;

	/**
	 * 前端显示的时间字段
	 */
	@ExcelProperty(value = "时间",index = 1)
	@TableField(exist=false)
	private String historyDateStr;
	/**
	 * 
	 */
	@ExcelProperty(value = "ip",index = 0)
	private String ip;
	/**
	 * 攻击类型分类码
	 */
	@ExcelIgnore
	private Integer attackType;

	/**
	 * 攻击类型中文
	 */
	@ExcelProperty(value = "攻击类型",index = 2)
	@TableField(exist=false)
	private String attackTypeCN;
	/**
	 * 
	 */
	@ExcelProperty(value = "评分",index = 3)
	private Integer credit;
	/**
	 * 
	 */
	@ExcelProperty(value = "评分等级",index = 4)
	private Integer creditLevel;
	/**
	 * 9都有,'NSFOCUS Security Labs','Open Source'
	 */
	@ExcelProperty(value = "来源",index = 5)
	private String source;


	public NtiHistoryEntity(String ip) {
		this.ip = ip;
	}
}
