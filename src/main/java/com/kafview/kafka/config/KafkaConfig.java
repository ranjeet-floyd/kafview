package com.kafview.kafka.config;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import lombok.Data;
import lombok.NonNull;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.BytesDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

@Data
public class KafkaConfig {

  @NonNull
  private Class keyDeserializer;
  @NonNull
  private Class valueDeserializer;
  @NonNull
  private String schemaURL;

  @NonNull
  private String server;
  @NonNull
  private String consumerGroup;
  @NonNull
  private String trustStorePath;
  @NonNull
  private String keyStorePath;
  @NonNull
  private String trustStorePassword;
  @NonNull
  private String sslKeyStorePassword;
  
  public KafkaConfig(KafviewConfig kafviewConfig) {
    this.keyDeserializer = ByteArrayDeserializer.class;
    this.valueDeserializer = ByteArrayDeserializer.class;
    this.server = kafviewConfig.getBroker();
    this.consumerGroup = "kafview_" + hostName();
    this.trustStorePath = kafviewConfig.getSecurityConfig().getTruststore();
    this.keyStorePath = kafviewConfig.getSecurityConfig().getKeystore();

    this.trustStorePassword = kafviewConfig.getSecurityConfig().getTruststorePassword();
    this.sslKeyStorePassword = kafviewConfig.getSecurityConfig().getKeystorePassword();

    this.schemaURL = kafviewConfig.getSchemaRegistryUrl();

  }

  private static String hostName() {
    try {
      return "kafview" + InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private static String readFile(String path) {
    try {
      return new String(Files.readAllBytes(new File(path).toPath()));
    } catch (IOException e) {
      throw new RuntimeException("File not Found => " + path, e);
    }
  }


}
