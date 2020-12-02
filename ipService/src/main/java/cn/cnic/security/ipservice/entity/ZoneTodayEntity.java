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
@TableName("zone_today")
public class ZoneTodayEntity implements Serializable {
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
	 * 被黑网站地址
	 */
	private String ip;
	/**
	 * 被黑网站IP所属国家
	 */
	private String country;
	/**
	 * 被黑网站系统类型
	 */
	private String os;
	/**
	 * 被黑网站web组件类型
	 */
	private String webos;
	/**
	 * 被黑网站URL
	 */
	private String fameurl;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getWebos() {
		return webos;
	}

	public void setWebos(String webos) {
		this.webos = webos;
	}

	public String getFameurl() {
		return fameurl;
	}

	public void setFameurl(String fameurl) {
		this.fameurl = fameurl;
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
