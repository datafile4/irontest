package io.irontest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Zheng on 26/05/2016.
 */
public class MQIIBEndpointProperties extends Properties {
    private String queueManagerName;
    private String host;
    private Integer port;
    private String svrConnChannelName;

    public String getQueueManagerName() {
        return queueManagerName;
    }

    public void setQueueManagerName(String queueManagerName) {
        this.queueManagerName = queueManagerName;
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

    public String getSvrConnChannelName() {
        return svrConnChannelName;
    }

    public void setSvrConnChannelName(String svrConnChannelName) {
        this.svrConnChannelName = svrConnChannelName;
    }

    @JsonProperty
    public String getQueueManagerAddress() {
        return host + ':' + port + '/' + queueManagerName;
    }

    @JsonIgnore
    public void setQueueManagerAddress(String queueManagerAddress) {
        //  do nothing
    }
}
