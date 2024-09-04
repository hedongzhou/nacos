package com.eworld.nacos.plugin.config.impl;

import com.alibaba.nacos.plugin.config.constants.ConfigChangeExecuteTypes;
import com.alibaba.nacos.plugin.config.constants.ConfigChangePointCutTypes;
import com.alibaba.nacos.plugin.config.model.ConfigChangeRequest;
import com.alibaba.nacos.plugin.config.model.ConfigChangeResponse;
import com.alibaba.nacos.plugin.config.spi.ConfigChangePluginService;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.Iterator;
import java.util.Optional;

/**
 * @author hedz
 * @since 2024/9/3
 */
@Slf4j
public class YamlPublishConfigCheckServiceImpl implements ConfigChangePluginService {

    @Override
    public void execute(ConfigChangeRequest configChangeRequest,
                        ConfigChangeResponse configChangeResponse) {
        log.info("***********************************: {}", configChangeRequest.getRequestType());
        String content = Optional.ofNullable(configChangeRequest.getRequestArgs().get("content"))
                .map(Object::toString)
                .orElse("");
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);
        Yaml yaml = new Yaml(options);
        try {
            Iterable<Object> result = yaml.loadAll(content);
            Iterator<Object> iter = result.iterator();
            while (iter.hasNext()) {
                iter.next();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            configChangeResponse.setSuccess(false);
            configChangeResponse.setMsg("yaml format error");
        }
    }

    @Override
    public ConfigChangeExecuteTypes executeType() {
        return ConfigChangeExecuteTypes.EXECUTE_BEFORE_TYPE;
    }

    @Override
    public String getServiceType() {
        return "config-check";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public ConfigChangePointCutTypes[] pointcutMethodNames() {
        return new ConfigChangePointCutTypes[]{ConfigChangePointCutTypes.PUBLISH_BY_HTTP};
    }

}
