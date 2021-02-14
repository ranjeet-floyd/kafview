package com.kafview.kafka.config;


import javax.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "message")
@Data
public class MessageFormatProperties {
  private MessageFormat format;

  @PostConstruct
  public void init() {
    // Set a default message format if not configured.
    if (format == null) {
      format = MessageFormat.DEFAULT;
    }
  }
}
