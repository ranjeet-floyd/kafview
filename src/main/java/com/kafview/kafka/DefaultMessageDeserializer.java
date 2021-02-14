package com.kafview.kafka;

import com.kafview.kafka.config.MessageDeserializer;
import java.nio.ByteBuffer;

public class DefaultMessageDeserializer implements MessageDeserializer {
  @Override
  public String deserializeMessage(ByteBuffer buffer) {
    return ByteUtils.readString(buffer);
  }
}
