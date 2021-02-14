package com.kafview.kafka.config;

import lombok.Getter;

@Getter
public class Deserializers {
  private final MessageDeserializer keyDeserializer;
  private final MessageDeserializer valueDeserializer;

  public Deserializers(MessageDeserializer keyDeserializer,
                       MessageDeserializer valueDeserializer) {
    this.keyDeserializer = keyDeserializer;
    this.valueDeserializer = valueDeserializer;
  }
}
