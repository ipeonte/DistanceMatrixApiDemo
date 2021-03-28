package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.component.ConfigProperties;

@Service
public class ConfigService {

  @Autowired
  ConfigProperties props;

  public Map<String, String> getConfigList(List<String> keys) {
    Map<String, String> result = new HashMap<>();

    for (String key : keys) {
      switch (key) {
      case "api_key":
        result.put(key, props.getApiKey());
        break;
      }
    }

    return result;
  }
}
