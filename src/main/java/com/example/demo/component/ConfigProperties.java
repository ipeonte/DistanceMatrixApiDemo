package com.example.demo.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("demo")
public class ConfigProperties {

  // Google Map API Key
  private String apiKey;

  // Path to the file with locations
  private String locationFilePath;

  // Number of neighbors points to analyze
  private int nbPointsNum;

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getLocationFilePath() {
    return locationFilePath;
  }

  public void setLocationFilePath(String locationFilePath) {
    this.locationFilePath = locationFilePath;
  }

  public int getNbPointsNum() {
    return nbPointsNum;
  }

  public void setNbPointsNum(int nbPointsNum) {
    this.nbPointsNum = nbPointsNum;
  }
}
