package com.kafview.kafka.config;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface MessageDeserializer {
  String deserializeMessage(ByteBuffer buffer);
}
