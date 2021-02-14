package com.kafview.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafview.kafka.config.Deserializers;
import com.kafview.kafka.config.KafviewConfig;
import com.kafview.kafka.config.MessageDeserializer;
import com.kafview.kafka.config.MessageFormat;
import com.kafview.kafka.service.AvroMessageDeserializer;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

public class Util {

  public static List<Message> buildMessage(ConsumerRecords<byte[], byte[]> records, Deserializers deserializers) {
    List<Message> messageList = new ArrayList<>(records.count());
    records.forEach(record -> messageList.add(Message.builder()
        .key(deserialize(record.key(), deserializers.getKeyDeserializer()))
        .value(deserialize(record.value(), deserializers.getValueDeserializer()))
        .header(Util.buildHeader(record.headers()))
        .offset(record.offset())
        .partition(record.partition())
        .timestamp(
            LocalDateTime.ofEpochSecond(record.timestamp()/1000, 0, ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME))
        .build()));
    return messageList;
  }

  public static Map<String, String> buildHeader(Headers headers) {
    final Map<String, String> headerMap = new HashMap<>();
    for (Header header : headers) {
      headerMap.put(header.key(), new String(header.value()));
    }
    return headerMap;
  }

  public static MessageDeserializer getDeserializer(String topicName, MessageFormat format,
                                                    KafviewConfig kafviewConfig) {
    final MessageDeserializer deserializer;
    if (format == MessageFormat.AVRO) {
      deserializer = new AvroMessageDeserializer(topicName, kafviewConfig.getSchemaRegistryUrl(), null);
    } else {
      deserializer = new DefaultMessageDeserializer();
    }

    return deserializer;
  }

  public static String deserialize(byte[] bytes, MessageDeserializer deserializer) {
    return bytes != null ? deserializer.deserializeMessage(ByteBuffer.wrap(bytes)) : "empty";
  }

  public static ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

}
