package cn.zhengyk.sync.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.google.common.collect.Lists;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/12/16
 * @description:
 * @version: 1.0
 */
@Slf4j
@Setter
@Component
@ConfigurationProperties("canal")
public class CanalConfig {

    private String host;
    private Integer port;
    private String destination;
    private String username;
    private String password;
    private String subscribe;

    @Bean
    public CanalConnector getCanalConnector() {
        CanalConnector canalConnector = CanalConnectors.newClusterConnector(Lists.newArrayList(new InetSocketAddress(host, port)), destination, username, password);
        canalConnector.connect();
        // 指定要订阅的数据库和表
        canalConnector.subscribe(subscribe);
        // 回滚到上次中断的位置
        canalConnector.rollback();
        log.info("canal客户端启动......");
        return canalConnector;
    }
}
