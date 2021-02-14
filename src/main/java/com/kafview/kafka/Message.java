package com.kafview.kafka;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

/*
 Message .
 */
@Data
@Builder
public class Message {
  private String key;
  private String value;
  private Map<String, String> header;
  private int partition;
  private long offset;
  private String timestamp;
}
