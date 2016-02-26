package com.dianping.zebra.dao.entity;

import java.util.Date;

public class HeartbeatEntity {
    private int id;

    private String app_name;

    private String ip;

    private String datasource_bean_name;

    private String database_name;

    private String database_type;

    private String username;

    private String datasource_bean_class;

    private boolean replaced;

    private String jdbc_url;

    private int init_pool_size;

    private int max_pool_size;

    private int min_pool_size;

    private String version;

    private java.util.Date create_time;

    private java.util.Date update_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDatasource_bean_name() {
        return datasource_bean_name;
    }

    public void setDatasource_bean_name(String datasource_bean_name) {
        this.datasource_bean_name = datasource_bean_name;
    }

    public String getDatabase_name() {
        return database_name;
    }

    public void setDatabase_name(String database_name) {
        this.database_name = database_name;
    }

    public String getDatabase_type() {
        return database_type;
    }

    public void setDatabase_type(String database_type) {
        this.database_type = database_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatasource_bean_class() {
        return datasource_bean_class;
    }

    public void setDatasource_bean_class(String datasource_bean_class) {
        this.datasource_bean_class = datasource_bean_class;
    }

    public boolean isReplaced() {
        return replaced;
    }

    public void setReplaced(boolean replaced) {
        this.replaced = replaced;
    }

    public String getJdbc_url() {
        return jdbc_url;
    }

    public void setJdbc_url(String jdbc_url) {
        this.jdbc_url = jdbc_url;
    }

    public int getInit_pool_size() {
        return init_pool_size;
    }

    public void setInit_pool_size(int init_pool_size) {
        this.init_pool_size = init_pool_size;
    }

    public int getMax_pool_size() {
        return max_pool_size;
    }

    public void setMax_pool_size(int max_pool_size) {
        this.max_pool_size = max_pool_size;
    }

    public int getMin_pool_size() {
        return min_pool_size;
    }

    public void setMin_pool_size(int min_pool_size) {
        this.min_pool_size = min_pool_size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
