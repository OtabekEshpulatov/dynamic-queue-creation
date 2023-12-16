package org.example.dynamicqueues;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.dynamicqueues.service.RabbitQueueService;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: otabek
 * Date: 16/12/23 13:55
 */

@Component
@RequiredArgsConstructor
@Log4j2
public class Manager {


    public static final String EXCHANGE_LISTENER = "jb.exchange";


    private final RabbitQueueService rabbitQueueService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;

    String[] queueNames = {"jb.korzinka", "jb.havas", "jb.uzum", "jb.samoy", "jb.kors", "jb.hors", "jb.market", "jb.merkel"};
    String[] queueRoutings = {"jb.korzinka.routing", "jb.havas.routing", "jb.uzum.routing", "jb.samoy.routing", "jb.kors.routing", "jb.hors.routing", "jb.market.routing", "jb.merkel.routing"};

    public String[] allQueues() {
        List<String> queues = new ArrayList<>();

        for (int i = 0; i < queueRoutings.length; i++) {
            String queueName=queueNames[i];
            String routing= queueRoutings[i];

            if (rabbitAdmin.getQueueProperties(queueName) != null) {
                queues.add(queueName);
            } else {
                rabbitQueueService.addNewQueue(queueName, EXCHANGE_LISTENER, routing);
            }
        }

        log.info("running queues: " + queues);
        return queues.toArray(new String[]{});
    }


    @Scheduled(cron = "*/10 * * * * *")
    public void sendMessage() {
        for (int i = 0; i < queueRoutings.length; i++) {
            String routing=queueRoutings [i];
            rabbitTemplate.convertAndSend(EXCHANGE_LISTENER, routing, routing.substring(routing.lastIndexOf(".")+1));

        }
    }
}
