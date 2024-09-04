package test;

import com.alibaba.nacos.plugin.config.ConfigChangePluginManager;
import com.alibaba.nacos.plugin.config.constants.ConfigChangePointCutTypes;
import com.alibaba.nacos.plugin.config.spi.ConfigChangePluginService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.Iterator;
import java.util.List;

/**
 * @author hedz
 * @since 2024/9/3
 */
@Slf4j
public class ConfigCheckTest {

    @Test
    public void testConfig() {
        List<ConfigChangePluginService> list = ConfigChangePluginManager
                .findPluginServicesByPointcut(ConfigChangePointCutTypes.PUBLISH_BY_HTTP);
        log.info("size: {}", list.size());
    }

    @Test
    public void testYaml() {
        String content = "logging:\n" +
                "  level:\n" +
                "    APPLICATION: info\n" +
                "app:\n" +
                "  #字段映射使用缓存（默认开启）\n" +
                "  use-field-map-cache: true\n" +
                "  php-host-list:\n" +
                "    - starth5super.ewan.cn\n" +
                "  common:\n" +
                "    enable-local-cache: true\n" +
                "common:\n" +
                "  # canary:\n" +
                "  #   enable: true\n" +
                "  gateway:\n" +
                "    log-headers: pi,ai,ci,cv,pf,field-v,x-forwarded-for,proxy-client-ip,wl-proxy-client-ip,x-real-ip\n" +
                "    # cors:\n" +
                "    #   allow-methods: GET,PUT,POST,OPTIONS,PATCH\n" +
                "    #   allowed-origin: http://notice.gzxianc.com\n" +
                "spring:\n" +
                "  cloud:\n" +
                "    gateway:\n" +
                "      default-filters:\n" +
                "        - name: Retry\n" +
                "          args:\n" +
                "            retries: 1\n" +
                "            methods: GET,PUT,POST\n" +
                "            exceptions:\n" +
                "              - reactor.netty.http.client.PrematureCloseException\n" +
                "      httpclient:\n" +
                "        connect-timeout: 1000\n" +
                "        response-timeout: 6000\n" +
                "        pool:\n" +
                "          max-idle-time: 16000\n" +
                "      discovery:\n" +
                "        locator:\n" +
                "          enabled: true\n" +
                "      routes:\n" +
                "        #安卓初始化(反向逻辑)\n" +
                "        - id: aggre-api-app-init-reverse\n" +
                "          uri: lb://aggre-api-app\n" +
                "          predicates:\n" +
                "            # - Header=x-real-ip, 59.37.3.98\n" +
                "            - Host=duoinitsupersdk.cwmob.cn,initsupersdk.cwmob.cn,duoinitsupersdk.ewan.cn,initsupersdk.ewan.cn,interface.supersdk.ewan.cn,initsupersdk.gzxianc.com\n" +
                "            - name: ReadQueryBody\n" +
                "              args:\n" +
                "                predicate: '#{@reverseQueryBodyPredicate}'\n" +
                "          filters:\n" +
                "            - OldModifyReqParam\n" +
                "            #风控过滤器，在修改请求后，在修改响应前\n" +
                "            - CommonRisk\n" +
                "            - OldModifyRespBody\n" +
                "        #安卓初始化兜底\n" +
                "        - id: supersdk-init-to-old\n" +
                "          uri: http://from-aggre-init.ewan.cn\n" +
                "          predicates:\n" +
                "            - Host=duoinitsupersdk.cwmob.cn,initsupersdk.cwmob.cn,duoinitsupersdk.ewan.cn,initsupersdk.ewan.cn,interface.supersdk.ewan.cn,initsupersdk.gzxianc.com\n" +
                "        #ios初始化\n" +
                "        - id: iossdk-init-to-new\n" +
                "          uri: lb://aggre-api-app\n" +
                "          predicates:\n" +
                "            - Host=aggre-ios-api.gzxianc.com,internal-aggre-ios.gzxianc.com\n" +
                "            - name: ReadQueryBody\n" +
                "              args:\n" +
                "                predicate: '#{@queryBodyPredicate}'\n" +
                "                regexp:\n" +
                "                  - X-Real-IP: 14.29.123.20\n" +
                "                  - X-Real-IP: 112.94.4.69\n" +
                "                  - deviceCode: '4F837442-A2C5-477E-8365-6AF888F882F2'\n" +
                "                  # 第一批灰度(神都)\n" +
                "                  - channelId: 50000\n" +
                "                    appId: 14019449\n" +
                "                  # 第二批灰度(堡垒)\n" +
                "                  - channelId: 50769\n" +
                "                    appId: 2017997\n" +
                "                    _limit: 200\n" +
                "                    # 第三批灰度（女神）\n" +
                "                    channelId: 50847\n" +
                "                    appId: 2000601\n" +
                "                    _limit: 100\n" +
                "                  # - X-Real-IP: 183.6.90.237\n" +
                "                  #   appId: 14019021\n" +
                "                  #   channelId: 50000\n" +
                "          filters:\n" +
                "            - OldModifyReqParam\n" +
                "            #风控过滤器，在修改请求后，在修改响应前\n" +
                "            - CommonRisk\n" +
                "            - OldModifyRespBody\n" +
                "        #ios初始化兜底\n" +
                "        - id: iossdk-init-to-old\n" +
                "          uri: http://from-aggre-interface.iosunionsdk.ewan.cn\n" +
                "          predicates:\n" +
                "            - Host=interfaceiossdk.internal.ewan.cn,unionsdk.whyxuan.cn,internal-aggre-ios.gzxianc.com,internal-aggre.gzxianc.com,initiossuperplay.ewan.cn,basicentra.23you.net,super.23you.net,unionsdk.etimegame.cn,interfacebak.iosunionsdk.ewan.cn,interface.iosunionsdk.ewan.cn,iosstat.1gamer.cn,iossupersdk.ewan.cn,center.23you.net,interfaceiossdk.ewan.cn,ioschannelsdk.ewan.cn,interfaceiossdk.1gamer.cn\n" +
                "        #sdk支付\n" +
                "        - id: supersdk-pay-to-aggre-app\n" +
                "          uri: lb://aggre-api-app\n" +
                "          predicates:\n" +
                "            - Path=/superv300,/super\n" +
                "            - Host=duopaysupersdk.ewan.cn,duopaysupersdk.gzxianc.com\n" +
                "          filters:\n" +
                "            - OldModifyReqParam\n" +
                "            - OldModifyRespBody\n" +
                "        #h5-supersdk-interface （来至SDK直连及PHP转发）（反向逻辑）\n" +
                "        - id: h5-supersdk-interface-to-aggre-app-reverse\n" +
                "          uri: lb://aggre-api-app\n" +
                "          predicates:\n" +
                "            - Host=inith5supersdk.ewan.cn,inith5supersdk.gzxianc.com,inith5supersdk-prod.ewan.cn\n" +
                "            - name: ReadQueryBody\n" +
                "              args:\n" +
                "                predicate: '#{@reverseQueryBodyPredicate}'\n" +
                "          filters:\n" +
                "            - OldModifyReqParam\n" +
                "            - OldModifyRespBody\n" +
                "        #h5初始化兜底\n" +
                "        - id: h5-supersdk-interface-to-old\n" +
                "          uri: https://from-aggre-inith5.ewan.cn\n" +
                "          predicates:\n" +
                "            - Host=inith5supersdk.ewan.cn,inith5supersdk.gzxianc.com,inith5supersdk-prod.ewan.cn\n" +
                "        #h5-supersdk-login  H5启动页\n" +
                "        - id: h5-supersdk-login-to-aggre-app\n" +
                "          uri: lb://aggre-api-app\n" +
                "          predicates:\n" +
                "            - Path=/login/**\n" +
                "            - Host=h5unionlogin.ewan.cn,h5unionlogin.gzxianc.com\n" +
                "          filters:\n" +
                "            - OldModifyReqParam\n" +
                "            - OldModifyRespBody\n" +
                "        #请求php，网关转新超级服务（反向逻辑）\n" +
                "        - id: h5-supersdk-interface-to-aggre-no-php-reverse\n" +
                "          uri: lb://aggre-api-app\n" +
                "          predicates:\n" +
                "            - Host=starth5super.ewan.cn\n" +
                "            - name: ReadQueryBody\n" +
                "              args:\n" +
                "                predicate: '#{@reverseQueryBodyPredicate}'\n" +
                "                regexp:\n" +
                "                  - channelId: '1643\\?isCloudGameShell=1'\n" +
                "          filters:\n" +
                "            - OldModifyReqParam\n" +
                "            - OldModifyRespBody\n" +
                "        # 请求php，网关转回旧PHP\n" +
                "        - id:  h5-supersdk-interface-bypass-php-to-old\n" +
                "          uri: https://from-aggre-h5-init.ewan.cn\n" +
                "          predicates:\n" +
                "            - Host=starth5super.ewan.cn\n" +
                "        #supersdk-login SDK 登陆\n" +
                "        - id: supersdk-login-to-aggre-app\n" +
                "          uri: lb://aggre-api-app\n" +
                "          predicates:\n" +
                "            - Path=/super,/superv300\n" +
                "            - Host=duologinsupersdk.ewan.cn,login.supersdk.ewan.cn,loginsupersdk.ewan.cn,loginsupersdk.gzxianc.com\n" +
                "          filters:\n" +
                "            - OldModifyReqParam\n" +
                "            #风控过滤器，在修改请求后，在修改响应前\n" +
                "            - CommonRisk\n" +
                "            - OldModifyRespBody\n" +
                "        #super-anti-addiction\n" +
                "        - id: super-anti-addiction-to-aggre-app\n" +
                "          uri: lb://aggre-api-app\n" +
                "          predicates:\n" +
                "            - Path=/anti\n" +
                "            - Host=fcmsupersdk.ewan.cn,fcmsupersdk.gzxianc.com,aggre-ios-api.gzxianc.com,aggre-ios3-api.gzxianc.com\n" +
                "          filters:\n" +
                "            - OldModifyReqParam\n" +
                "            - OldModifyRespBody\n" +
                "        - id: aggre-api-app\n" +
                "          uri: lb://aggre-api-app\n" +
                "          predicates:\n" +
                "            - Path=/api-app/**\n" +
                "          filters:\n" +
                "            - StripPrefix=1\n" +
                "        - id: aggre-api-aider\n" +
                "          uri: lb://aggre-api-aider\n" +
                "          predicates:\n" +
                "            - Path=/api-aider/**\n" +
                "          filters:\n" +
                "            - StripPrefix=1\n" +
                "        - id: aggre-api-cp\n" +
                "          uri: lb://aggre-api-cp\n" +
                "          predicates:\n" +
                "            - Path=/api-cp/**\n" +
                "          filters:\n" +
                "            - StripPrefix=1\n" +
                "        - id: aggre-api-channel\n" +
                "          uri: lb://aggre-api-channel\n" +
                "          predicates:\n" +
                "            - Path=/api-channel/**\n" +
                "          filters:\n" +
                "            - StripPrefix=1";

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
        }
    }

}
