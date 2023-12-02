package com.ljt.ratelimiter.env;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Zookeeper configuration.
 */
@Data
public class ZookeeperConfig {

    public static final String DEFAULT_PATH = "/com/eudemon/ratelimit";

    private String address;

    private String path = DEFAULT_PATH;

    public void buildFromProperties(PropertySource propertySource) {
        if (propertySource == null) {
            return;
        }

        String addr = propertySource.getPropertyStringValue(PropertyConstants
                .PROPERTY_ZOOKEEPER_ADDRESS);
        if (StringUtils.isNotBlank(addr)) {
            this.address = addr;
        }

        String path = propertySource.getPropertyStringValue(PropertyConstants
                .PROPERTY_ZOOKEEPER_RULE_PATH);
        if (StringUtils.isNotBlank(path)) {
            this.path = path;
        }
    }

}