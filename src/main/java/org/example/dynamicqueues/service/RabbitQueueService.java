package org.example.dynamicqueues.service;

/**
 * Author: otabek
 * Date: 16/12/23 12:21
 */
public interface RabbitQueueService {
    void addNewQueue(String queueName, String exchangeName, String routingKey);
}

