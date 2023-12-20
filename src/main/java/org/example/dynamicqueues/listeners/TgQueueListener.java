package org.example.dynamicqueues.listeners;

import lombok.extern.log4j.Log4j2;
import org.example.dynamicqueues.dto.TgPojo;

/**
 * Author: otabek
 * Date: 18/12/23 14:25
 */
@Log4j2
public class TgQueueListener extends AbstractJbQueueListener<TgPojo> {

    @Override
    protected void listen(TgPojo tgPojo) {
        System.out.println(tgPojo.getText());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
