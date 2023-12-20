package org.example.dynamicqueues;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.dynamicqueues.dto.TgPojo;
import org.example.dynamicqueues.service.RabbitQueueService;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Author: otabek
 * Date: 16/12/23 13:55
 */

@Component
@RequiredArgsConstructor
@Log4j2
public class Manager implements ApplicationListener<ApplicationReadyEvent> {
    public static final String EXCHANGE_LISTENER = "jb.exchange";

    private final RabbitQueueService rabbitQueueService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;

    private static final Boolean create = true;
    private static final Boolean addListener = null;
    static String[] queueNames;
    static String[] queueRoutings;


    static {
        String[] companyNames = {
                "Apple", "Tesla", "Google", "Microsoft", "Amazon",
                "Facebook", "IBM", "Oracle", "Samsung", "Adobe",
                "Intel", "Sony", "HP", "Dell", "Cisco",
                "NVIDIA", "Panasonic", "Lenovo", "Qualcomm", "VMware",
                "Verizon", "AT&T", "Baidu", "Alibaba", "Tencent",
                "Netflix", "Spotify", "Uber", "Airbnb", "SpaceX",
                "Twitter", "LinkedIn", "Snapchat", "Pinterest", "Square",
                "Reddit", "Etsy", "Dropbox", "Slack", "Zoom",
                "PayPal", "Visa", "Mastercard", "Tesla", "Ford",
                "General Motors", "Boeing", "Lockheed Martin", "Raytheon", "General Electric",
                "Siemens", "Vodafone", "Coca-Cola", "PepsiCo", "Nestle",
                "Procter & Gamble", "Unilever", "Johnson & Johnson", "Novartis", "Merck",
                "Bayer", "Siemens", "Toyota", "Honda", "Nissan",
                "Mitsubishi", "Subaru", "Lexus", "Audi", "BMW",
                "Mercedes-Benz", "Volkswagen", "Ferrari", "Lamborghini", "Rolls-Royce",
                "Jaguar", "Land Rover", "Ford", "Chevrolet", "Cadillac",
                "Chanel", "Louis Vuitton", "Gucci", "Prada", "Hermès",
                "Rolex", "Cartier", "Tiffany & Co.", "Nike", "Adidas",
                "Puma", "Under Armour", "Reebok", "New Balance", "Converse",
                "Sony", "Samsung", "LG", "Panasonic", "Philips",
                "Sharp", "Toshiba", "Canon", "Nikon", "Sony Ericsson",
                // Add more company names as needed
        };

        int size = 1;
        queueNames = new String[size];
        queueRoutings = new String[size];

        // Fill arrays with predefined company names and corresponding routings
        for (int i = 0; i < size; i++) {
            String companyName = companyNames[i % companyNames.length];
            queueNames[i] = "jb." + companyName;
            queueRoutings[i] = "jb." + companyName + ".routing";
        }
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (create != null) {
            for (int i = 0; i < queueNames.length; i++) {
                String queueName = queueNames[i];
                String routing = queueRoutings[i];
                if (create == Boolean.TRUE) {
                    rabbitQueueService.addNewQueue(queueName, EXCHANGE_LISTENER, routing);
                } else {
                    rabbitAdmin.deleteQueue(queueName);
                }
            }
        }


        for (int i = 0; i < queueRoutings.length && i < queueNames.length; i++) {
            String queueName = queueNames[i];
            String routing = queueRoutings[i];

            for (int i1 = 0; i1 < 5000; i1++) {
                rabbitTemplate.convertAndSend(EXCHANGE_LISTENER, routing, new TgPojo(i1+"", null), message -> {
                    message.getMessageProperties().setPriority(1);
                    return message;
                });
            }

//            for (int i1 = 0; i1 < 10; i1++) {
//                rabbitTemplate.convertAndSend(EXCHANGE_LISTENER, routing, new TgPojo("i", null), message -> {
//                    message.getMessageProperties().setPriority(2);
//                    return message;
//                });
//            }
        }
    }

    private void clearQueues() {
        for (String queueName : queueNames) {
            rabbitAdmin.deleteQueue(queueName);
        }
    }
}
