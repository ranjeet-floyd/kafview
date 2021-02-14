package com.kafview.kafka.contoller;

import com.kafview.kafka.config.KafviewConfig;
import com.kafview.kafka.util.ConfigUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class KafviewConfigController {


  @GetMapping("/config")
  public String configForm(Model model) {
    model.addAttribute("kafviewConfig", new KafviewConfig());
    return "config";
  }

  @PostMapping("/config")
  public String config(@ModelAttribute KafviewConfig kafviewConfig) {
    ConfigUtil.saveKafviewConfig(kafviewConfig);
    return "redirect:/topic/messages";
  }


}
