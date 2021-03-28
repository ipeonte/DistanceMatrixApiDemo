package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.component.ConfigProperties;
import com.example.demo.model.GeoPoint;
import com.example.demo.model.Point;

@TestPropertySource("classpath:/test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LocationServiceTest {

  @Autowired
  private LocationService location;

  @Autowired
  private ConfigProperties props;

  @Test
  void testFindShortestDistance1() {
    List<Point> points = new ArrayList<>();
    points.add(new Point("Test5", 5, -1));
    points.add(new Point("Test2", 2, 2));
    points.add(new Point("Test1", 1, 1));

    checkFindShortestDistance(new GeoPoint(1, 1), points);
    checkFindShortestDistance(new GeoPoint(0, 0), points);
  }

  @Test
  void testFindShortestDistance2() {
    List<Point> points = new ArrayList<>();
    points.add(new Point("Test5", 5, -1));
    points.add(new Point("Test1", 1, 1));
    points.add(new Point("Test2", 2, 2));

    checkFindShortestDistance(new GeoPoint(2, 2), points);
  }

  @Test
  void testFindShortestDistance3() {
    List<Point> points = new ArrayList<>();
    points.add(new Point("Test3", 9, 9));
    points.add(new Point("Test4", 5, 10));
    points.add(new Point("Test2", 2, 2));

    checkFindShortestDistance(new GeoPoint(5, 5), points);
  }

  @Test
  void testFindShortestDistance4() {
    List<Point> points = new ArrayList<>();
    points.add(new Point("Test2", 2, 2));
    points.add(new Point("Test4", 5, 10));
    points.add(new Point("Test3", 9, 9));

    checkFindShortestDistance(new GeoPoint(45, 45), points);
  }

  @Test
  void testFindShortestDistance5() {
    List<Point> points = new ArrayList<>();
    points.add(new Point("Test4", 5, 10));
    points.add(new Point("Test3", 9, 9));
    points.add(new Point("Test6", 99, 99));

    checkFindShortestDistance(new GeoPoint(54, 54), points);
  }

  private void checkFindShortestDistance(GeoPoint geoPoint,
      List<Point> expected) {
    int len = expected.size();
    assertEquals(props.getNbPointsNum(), len,
        "Size of expected nearest points doesn't match configured.");
    List<Point> points = location.findNearestPoints(geoPoint);

    assertEquals(len, points.size(),
        "Number of returned nearest points doesn't match.");

    // Check if points returned in correct order
    for (int i = 0; i < len; i++) {
      assertEquals(expected.get(i).toString(), points.get(i).toString(),
          "Point #" + i + " doesn't match.");
    }
  }
}
