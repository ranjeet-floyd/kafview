package com.kafview.kafka.service;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.kafview.kafka.config.KafkaConfig;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SslConfigs;

@Slf4j
public class KafkaConsumerClient {

  private org.apache.kafka.clients.consumer.KafkaConsumer<byte[], byte[]> consumer;
  private AdminClient adminClient;
  private KafkaConfig kafkaConfig;

  public KafkaConsumerClient(KafkaConfig kafkaConfig) {
    this.kafkaConfig = kafkaConfig;
  }

  private void initClient() {
    if (Objects.isNull(consumer) && Objects.isNull(adminClient)) {

      Properties consumerProps = new Properties();
      consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getServer());
      consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConfig.getKeyDeserializer());
      consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConfig.getValueDeserializer());

      boolean isSecure = true;
      boolean isAvroSerializer = true;
      if (isAvroSerializer) {
        consumerProps.put("schema.registry.url", kafkaConfig.getSchemaURL());
//      consumerProps.put(KafkaAvroDeserializerConfig.AUTO_REGISTER_SCHEMAS,
//          KafkaAvroDeserializerConfig.AUTO_REGISTER_SCHEMAS_DEFAULT)
      }

      if (isSecure) {
        consumerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "ssl");
        consumerProps.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, SslConfigs.DEFAULT_SSL_KEYSTORE_TYPE);
        consumerProps.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, SslConfigs.DEFAULT_SSL_KEYSTORE_TYPE);
        consumerProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, kafkaConfig.getTrustStorePath());
        consumerProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, kafkaConfig.getKeyStorePath());
        consumerProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, kafkaConfig.getTrustStorePassword());
        consumerProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, kafkaConfig.getSslKeyStorePassword());
      }
      consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
      consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // TODO: check
      consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfig.getConsumerGroup());
      consumer = new org.apache.kafka.clients.consumer.KafkaConsumer(consumerProps);
      adminClient = KafkaAdminClient.create(consumerProps);
    }
  }

  /**
   *
   * @param topicName Kafka topic name.
   * @param timeInSecs : past time in sec to seek topic data
   * @return ConsumerRecords
   */
  public ConsumerRecords<byte[], byte[]> getConsumerRecords(String topicName, int timeInSecs) {
    initClient();
    List<PartitionInfo> partitionInfos = consumer.partitionsFor(topicName);
    List<TopicPartition> topicPartitionList = partitionInfos
        .stream()
        .map(info -> new TopicPartition(topicName, info.partition()))
        .collect(Collectors.toList());
    consumer.assign(topicPartitionList);
    Map<TopicPartition, Long> timestampsToSearch = new HashMap<>();
    for (TopicPartition partition : topicPartitionList) {
      timestampsToSearch.put(partition, Instant.now().minus(timeInSecs, SECONDS).toEpochMilli());
    }
    boolean hasRecords = false;
    Map<TopicPartition, OffsetAndTimestamp> outOffsets = consumer.offsetsForTimes(timestampsToSearch);
    for (Map.Entry<TopicPartition, OffsetAndTimestamp> of : outOffsets.entrySet()) {
      if (of.getValue() != null) {
        consumer.seek(of.getKey(), of.getValue().offset());
        hasRecords = true;
      }
    }
    ConsumerRecords<byte[], byte[]> consumerRecords = null;
    if (hasRecords) {
      consumerRecords = consumer.poll(Duration.ofSeconds(10));

    }
    consumer.unsubscribe();
    return consumerRecords;
  }

  public List<String> getTopics() {
    initClient();
    return new ArrayList<>(consumer.listTopics().keySet());
  }

  public List<String> getBrokers() {
    initClient();
    DescribeClusterResult describeClusterResult = adminClient.describeCluster();
    try {
      List<Node> brokers = new ArrayList<>(describeClusterResult.nodes().get());
      return brokers.stream().map(s -> s.host() + ":" + s.port()).collect(Collectors.toList());
    } catch (InterruptedException | ExecutionException e) {
      log.error(e.getMessage(), e);
    }
    return Collections.emptyList();
  }
}
