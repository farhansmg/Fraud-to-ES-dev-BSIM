package com.app.kafka.consumer;

import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import com.app.kafka.consumer.KafkaIConsumer;

public class ConsumerCreator {
    public static Consumer<Long, String> createFeConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaIConsumer.KAFKA_FE_BROKERS);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, KafkaIConsumer.CLIENT_ID);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaIConsumer.GROUP_ID_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, KafkaIConsumer.MAX_POLL_RECORDS);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaIConsumer.OFFSET_RESET_LATEST);
        
        Consumer<Long, String> consumer = new KafkaConsumer<>(props);
        
        consumer.subscribe(Collections.singletonList(KafkaIConsumer.TOPIC_FE_NAME));
        return consumer;
    }
    
    public static Consumer<Long, String> createBoConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaIConsumer.KAFKA_BO_BROKERS);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, KafkaIConsumer.CLIENT_ID);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaIConsumer.GROUP_ID_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, KafkaIConsumer.MAX_POLL_RECORDS);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaIConsumer.OFFSET_RESET_LATEST);
        
        Consumer<Long, String> consumer = new KafkaConsumer<>(props);
        
        consumer.subscribe(Collections.singletonList(KafkaIConsumer.TOPIC_BO_NAME));
        return consumer;
    }
}