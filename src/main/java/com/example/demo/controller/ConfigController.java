package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ConfigService;
import com.example.demo.shared.Constants;

@RestController
@RequestMapping(Constants.BASE_URL)
public class ConfigController {

  @Autowired
  private ConfigService config;

  @GetMapping(Constants.CONFIG_URL)
  public Map<String, String> getConfigList(@RequestParam List<String> keys) {
    return config.getConfigList(keys);
  }
}
