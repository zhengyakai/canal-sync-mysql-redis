package cn.zhengyk.sync.schedul;

import cn.zhengyk.sync.handler.InsertHandler;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/12/16
 * @description:
 * @version: 1.0
 */
@Slf4j
@Component
public class CanalSchedul{

    @Autowired
    private CanalConnector canalConnector;

    @Autowired
    private InsertHandler insertHandler;

    @Async("canal")
    @Scheduled(fixedDelay = 200)  //每200毫秒拉取一次数据
    public void run() {
        int batchSize = 1000;
        try {
            Message message = canalConnector.getWithoutAck(batchSize);
            long batchId = message.getId();
            log.debug("batchId={}" , batchId);
            try {
                List<Entry> entries = message.getEntries();
                if (batchId != -1 && entries.size() > 0) {
                    entries.forEach(entry -> {
                        if (entry.getEntryType() == EntryType.ROWDATA) {
                            insertHandler.handleMessage(entry);
                        }
                    });
                }
                canalConnector.ack(batchId);
            } catch (Exception e) {
                log.error("批量获取 mysql 同步信息失败，batchId回滚,batchId=" + batchId, e);
                canalConnector.rollback(batchId);
            }
        } catch (Exception e) {
            log.error("Canal定时任务异常！", e);
        }
    }
}
