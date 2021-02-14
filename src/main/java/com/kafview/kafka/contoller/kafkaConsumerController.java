package com.kafview.kafka.contoller;

import com.kafview.kafka.Message;
import com.kafview.kafka.Util;
import com.kafview.kafka.config.Deserializers;
import com.kafview.kafka.config.KafviewConfig;
import com.kafview.kafka.config.MessageFormat;
import com.kafview.kafka.service.kafkaConsumerService;
import com.kafview.kafka.util.ConfigUtil;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class kafkaConsumerController {

  private kafkaConsumerService kafkaConsumerService;
  private List<String> topics;
  private List<String> brokers;
  private KafviewConfig kafviewConfig;

  public kafkaConsumerController(kafkaConsumerService kafkaConsumerService) {
    this.kafkaConsumerService = kafkaConsumerService;
  }

  @PostConstruct
  public void init() {
    topics = kafkaConsumerService.getTopics();
    brokers = kafkaConsumerService.getBrokers();
    kafviewConfig = ConfigUtil.loadKafviewConfig();
  }


  @GetMapping("/topic/messages")
  public String messages(@NonNull @RequestParam(value = "name", required = false) String topicName,
                         @RequestParam(name = "timeInSec", required = false, defaultValue = "1000000")
                             Integer timeInSec, Model model) {
    List<Message> messageList = Collections.emptyList();
    if (StringUtils.hasText(topicName)) {
      Deserializers deserializers = new Deserializers(
          Util.getDeserializer(topicName, MessageFormat.DEFAULT, kafviewConfig),
          Util.getDeserializer(topicName, kafviewConfig.getMessageFormat(), kafviewConfig));
      ConsumerRecords<byte[], byte[]> consumerRecords = kafkaConsumerService.getLatestRecords(topicName, timeInSec);
      if (Objects.nonNull(consumerRecords)) {
        messageList = Util.buildMessage(consumerRecords, deserializers);
      }
    }
    log.info("Message List : {} ", messageList);
    model.addAttribute("messages", messageList);
    model.addAttribute("topics", topics);
    model.addAttribute("topic", topicName);
    model.addAttribute("brokers", brokers);
    return "messages";
  }

  @ModelAttribute
  public void loadKafviewConfig(Model model) {
    model.addAttribute("kafviewConfig", ConfigUtil.loadKafviewConfig());
  }


}
