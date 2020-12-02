package cn.cnic.security.ipservice.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-11-23 16:22:15
 */
@Data
@TableName("cnvd_spider")
public class CnvdSpiderEntity implements Serializable {
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
	 * CNVDID
	 */
	private String cnvdId;
	/**
	 * CVEID
	 */
	private String cveId;
	/**
	 * 危险级别
	 */
	private String level;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 影响范围
	 */
	private String influence;
	/**
	 * 漏洞类型
	 */
	private String leixin;
	/**
	 * 参考链接
	 */
	private String refer;
	/**
	 * 解决方案
	 */
	private String solution;
	/**
	 * 补丁链接
	 */
	private String patchurl;
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

	public String getCnvdId() {
		return cnvdId;
	}

	public void setCnvdId(String cnvdId) {
		this.cnvdId = cnvdId;
	}

	public String getCveId() {
		return cveId;
	}

	public void setCveId(String cveId) {
		this.cveId = cveId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInfluence() {
		return influence;
	}

	public void setInfluence(String influence) {
		this.influence = influence;
	}

	public String getLeixin() {
		return leixin;
	}

	public void setLeixin(String leixin) {
		this.leixin = leixin;
	}

	public String getRefer() {
		return refer;
	}

	public void setRefer(String refer) {
		this.refer = refer;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getPatchurl() {
		return patchurl;
	}

	public void setPatchurl(String patchurl) {
		this.patchurl = patchurl;
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

	@Override
	public String toString() {
		return "CnvdSpiderEntity{" +
				"id=" + id +
				", time=" + time +
				", title='" + title + '\'' +
				", url='" + url + '\'' +
				", cnvdId='" + cnvdId + '\'' +
				", cveId='" + cveId + '\'' +
				", level='" + level + '\'' +
				", description='" + description + '\'' +
				", influence='" + influence + '\'' +
				", leixin='" + leixin + '\'' +
				", refer='" + refer + '\'' +
				", solution='" + solution + '\'' +
				", patchurl='" + patchurl + '\'' +
				", informationenable=" + informationenable +
				", rulesid=" + rulesid +
				'}';
	}
}
