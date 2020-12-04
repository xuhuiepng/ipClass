package cn.cnic.security.ipservice.common.utils;

import java.util.Date;

public class IntelligenceEntity {

    /**
     *时间
     */
    private Date time;
    /**
     *标题
     */
    private String title;

    /**
     *表名
     */
    private String tablename;

    /**
     *url
     */
    private String url;

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String table_name) {
        this.tablename = table_name;
    }

    @Override
    public String toString() {
        return "IntelligenceEntity{" +
                "time=" + time +
                ", title='" + title + '\'' +
                ", tablename='" + tablename + '\'' +
                ", url='" + url + '\'' +
                '}';
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



}
