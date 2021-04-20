package com.example.Server.kafka;

import com.example.Server.model.PurchaseRecord;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

  @Value(value = "${kafka.bootstrapAddress}")
  private String bootstrapAddress;

  @Value(value = "${purchase.topic.group.id}")
  private String purchaseGroupId;

  public ConsumerFactory<String, PurchaseRecord> purchaseConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, purchaseGroupId);
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    return new DefaultKafkaConsumerFactory<>(props,
        new StringDeserializer(),
        new JsonDeserializer<>(PurchaseRecord.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, PurchaseRecord>
  purchaseKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, PurchaseRecord> factory
        = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(purchaseConsumerFactory());
    return factory;
  }
}
