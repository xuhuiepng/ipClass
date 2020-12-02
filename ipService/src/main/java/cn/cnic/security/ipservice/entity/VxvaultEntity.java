package cn.cnic.security.ipservice.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-11-23 16:22:15
 */
@Data
@TableName("vxvault")
public class VxvaultEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;
	/**
	 * 时间
	 */
	private Date time;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 页面URL
	 */
	private String url;
	/**
	 * 恶意IP地址
	 */
	private String ip;
	/**
	 * md5值
	 */
	private String md5str;
	/**
	 * 恶意URL
	 */
	private String dangerurl;
	/**
	 * 是否关联
	 */
	private Integer informationenable;
	/**
	 * 匹配规则ID
	 */
	private Integer rulesid;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMd5str() {
		return md5str;
	}

	public void setMd5str(String md5str) {
		this.md5str = md5str;
	}

	public String getDangerurl() {
		return dangerurl;
	}

	public void setDangerurl(String dangerurl) {
		this.dangerurl = dangerurl;
	}

	public Integer getInformationenable() {
		return informationenable;
	}

	public void setInformationenable(Integer informationenable) {
		this.informationenable = informationenable;
	}

	public Integer getRulesid() {
		return rulesid;
	}

	public void setRulesid(Integer rulesid) {
		this.rulesid = rulesid;
	}
}
