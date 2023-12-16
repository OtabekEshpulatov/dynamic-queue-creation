package org.example.dynamicqueues.service;

/**
 * Author: otabek
 * Date: 16/12/23 12:21
 */
public interface RabbitQueueService {
    void addNewQueue(String queueName, String exchangeName, String routingKey);

    void addQueueToListener(String listenerId, String queueName);

    void removeQueueFromListener(String listenerId, String queueName);

    Boolean checkQueueExistOnListener(String listenerId, String queueName);

}
