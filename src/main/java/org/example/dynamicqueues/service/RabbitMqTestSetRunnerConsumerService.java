package org.example.dynamicqueues.service;

import lombok.extern.log4j.Log4j2;
import org.example.dynamicqueues.Manager;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Author: otabek
 * Date: 16/12/23 12:46
 */
@Service
@Log4j2
public class RabbitMqTestSetRunnerConsumerService {
    @RabbitListener(id = Manager.EXCHANGE_LISTENER, queues = "#{manager.allQueues()}",concurrency ="100" )
    public void receiver(String testId) {
        System.out.println("**************************** Received message: " + testId);
        try {
//            log.info("***sleeping***");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
