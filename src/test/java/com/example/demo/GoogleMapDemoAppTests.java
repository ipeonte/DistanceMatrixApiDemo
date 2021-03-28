package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.model.Point;
import com.example.demo.shared.Constants;

@TestPropertySource("classpath:/test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GoogleMapDemoAppTests {

  @Autowired
  private TestRestTemplate rest;

  // Number of test points
  private static final int POINTS_NUM = 6;

  @Test
  void testConfigController() {
    final String apiKeyName = "api_key";

    @SuppressWarnings("unchecked")
    Map<String, String> map = rest.getForObject(Constants.BASE_URL +
        Constants.CONFIG_URL + "?keys=" + apiKeyName + ",abc", Map.class);

    assertNotNull(map, "Return configuration map is NULL.");
    assertEquals(1, map.size(), "Size of returned map doesn't match.");

    // As it defined in test.properties
    assertEquals("xyz", map.get(apiKeyName), "Returned api key doesn't match");
  }

  @Test
  void testLocationController() {
    @SuppressWarnings("unchecked")
    List<Point> points = rest
        .getForObject(Constants.BASE_URL + Constants.LOCATIONS_URL, List.class);

    assertNotNull(points);
    assertEquals(POINTS_NUM, points.size(), "Number of points doesn't match.");
  }
}
