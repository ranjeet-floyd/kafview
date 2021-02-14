package com.kafview.kafka.service;

import com.kafview.kafka.ByteUtils;
import com.kafview.kafka.config.MessageDeserializer;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class AvroMessageDeserializer implements MessageDeserializer {
  private final String topicName;
  private final KafkaAvroDeserializer deserializer;

  public AvroMessageDeserializer(String topicName, String schemaRegistryUrl, String schemaRegistryAuth) {
    this.topicName = topicName;
    this.deserializer = getDeserializer(schemaRegistryUrl,schemaRegistryAuth);
  }

  @Override
  public String deserializeMessage(ByteBuffer buffer) {
    // Convert byte buffer to byte array
    final byte[] bytes = ByteUtils.convertToByteArray(buffer);
    return deserializer.deserialize(topicName, bytes).toString();
  }

  private static KafkaAvroDeserializer getDeserializer(String schemaRegistryUrl, String schemaRegistryAuth) {
    final Map<String, Object> config = new HashMap<String, Object>();
    config.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
    if (schemaRegistryAuth != null) {
      config.put(AbstractKafkaAvroSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE, "USER_INFO");
      config.put(AbstractKafkaAvroSerDeConfig.USER_INFO_CONFIG, schemaRegistryAuth);
    }
    final KafkaAvroDeserializer kafkaAvroDeserializer = new KafkaAvroDeserializer();
    kafkaAvroDeserializer.configure(config, false);
    return kafkaAvroDeserializer;
  }


}
