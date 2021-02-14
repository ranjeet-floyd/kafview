package com.kafview.kafka.service;

import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface kafkaConsumerService {


  /**
   * Get the  records from now.
   * @param topic
   * @param count
   */
  ConsumerRecords<byte[], byte[]> getLatestRecords(String topic, int timeInSecs);


  /**
   * Get the all avaiable message from topic.
   * @param topic
   * @param count
   */
  ConsumerRecords<byte[], byte[]> getOldestRecords(String topic, int timeInSecs);


  List<String> getTopics();

  List<String> getBrokers();
}
