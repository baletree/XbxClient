package com.xbx.client.beans;

public class Version {

    private String id;
    private String versionCode;//版本
    private String versionName;//版本名称
    private String versionMsg;//版本更新内容
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionMsg() {
        return versionMsg;
    }

    public void setVersionMsg(String versionMsg) {
        this.versionMsg = versionMsg;
    }

    public Version(String id, String version, String name, String content) {
        super();
        this.id = id;
        this.versionCode = version;
        this.versionName = name;
        this.versionMsg = content;
    }

    public Version() {
        super();
    }
}
