package org.example.dynamicqueues.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * Author: otabek
 * Date: 18/12/23 14:15
 */
public abstract class AbstractJbQueueListener<T> implements MessageListener {


    @SuppressWarnings("unchecked")
    private final Class<T> persistentClass = (Class<T>) ((ParameterizedType) getClass()
            .getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public void onMessage(Message message) {
        try {
            T t = new ObjectMapper().readValue(message.getBody(), persistentClass);
            listen(t);

        } catch (IOException e) {
            throw new RuntimeException("failed while parsing data", e);
        }
    }

    protected abstract void listen(T t);
}
