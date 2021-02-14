package com.kafview.kafka.service;

import com.kafview.kafka.config.KafkaConfig;
import com.kafview.kafka.util.ConfigUtil;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer service.
 */
@Service
public class kafkaConsumerServiceImpl implements kafkaConsumerService {

  KafkaConsumerClient kafkaConsumerClient;

  public kafkaConsumerServiceImpl() {
    this.kafkaConsumerClient = new KafkaConsumerClient(new KafkaConfig(ConfigUtil.loadKafviewConfig()));

  }


  public ConsumerRecords<byte[], byte[]> getLatestRecords(String topic, int timeInSecs) {
    return kafkaConsumerClient.getConsumerRecords(topic, timeInSecs);
  }

  @Override
  public ConsumerRecords<byte[], byte[]> getOldestRecords(String topic, int timeInSecs) {
    return kafkaConsumerClient.getConsumerRecords(topic, timeInSecs);
  }

  @Override
  public List<String> getTopics() {
    return kafkaConsumerClient.getTopics();
  }

  @Override
  public List<String> getBrokers() {
    return kafkaConsumerClient.getBrokers();
  }
}
