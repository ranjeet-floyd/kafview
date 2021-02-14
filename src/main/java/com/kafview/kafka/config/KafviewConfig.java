package com.kafview.kafka.config;

import lombok.Data;

/**
 * Save User config
 */
@Data
public class KafviewConfig {
  private String clusterName;
  private String broker;
  private SecurityConfig securityConfig;
  private MessageFormat messageFormat;
  // for Avro
  private String schemaRegistryUrl;
  

  @Data
  public static class SecurityConfig {
    private Type type;
    private String truststore;
    private String truststorePassword;
    private String keystore;
    private String keystorePassword;
    
  }

  public static enum Type {
    SSL, PLAIN_TEXT
  }

}
