package com.kafview.kafka.service;

import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * Kafka consumer service.
 */
public class kafkaConsumerServiceImpl implements kafkaConsumerService {

  KafkaConsumerClient kafkaConsumerClient;

  public kafkaConsumerServiceImpl(KafkaConsumerClient kafkaConsumerClient) {
    this.kafkaConsumerClient = kafkaConsumerClient; // new KafkaConsumerClient(new KafkaConfig(ConfigUtil
    // .loadKafviewConfig()));

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
