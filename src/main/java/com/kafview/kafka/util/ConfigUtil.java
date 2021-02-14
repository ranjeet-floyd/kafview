package com.kafview.kafka.util;

import com.kafview.kafka.Util;
import com.kafview.kafka.config.KafviewConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class ConfigUtil {

  public static KafviewConfig loadKafviewConfig() {
    try {
      String kafviewConfig = new String(Files.readAllBytes(getKafviewConfig().toPath()));
      return Util.objectMapper().readValue(kafviewConfig, KafviewConfig.class);
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  public static void saveKafviewConfig(KafviewConfig kafviewConfig) {
    try {
      Files.write(getKafviewConfig().toPath(),
          Util.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsBytes(kafviewConfig));
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  public static File getKafviewConfig() throws IOException {
    File resource = new ClassPathResource("/static/config/kafviewConfig.json").getFile();
    if (!resource.exists() || resource.isFile()) {
      boolean isCreated = resource.createNewFile();
      log.info("Created new File at location : {} status : {}", resource.toPath(), isCreated);
    }
    return resource;
  }
}
