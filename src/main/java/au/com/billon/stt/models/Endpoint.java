package au.com.billon.stt.models;

import java.util.Date;

/**
 * Created by Trevor Li on 6/30/15.
 */
public class Endpoint {
    private long id;
    private String name;
    private String description;
    private String host;
    private Integer port;
    private String protocol;
    private String ctxroot;
    private Date created;
    private Date updated;

    public Endpoint() {
    }

    public Endpoint(long id, String name, String description, String host, Integer port, String protocol, String ctxroot, Date created, Date updated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.ctxroot = ctxroot;
        this.created = created;
        this.updated = updated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getCtxroot() {
        return ctxroot;
    }

    public void setCtxroot(String ctxroot) {
        this.ctxroot = ctxroot;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getAddress() {
        String address = "";

        if ("http".equalsIgnoreCase(this.protocol)) {
            address = "http://";
            if (this.host != null) {
                address = address + host;
                if (this.port != null) {
                    if (this.port != 80) {
                        address = address + ":" + this.port;
                    }
                    if (this.ctxroot != null) {
                        address = address + this.ctxroot;
                    }
                }
            }
        }

        return address;
    }

    public void setAddress(String address) { }
}
