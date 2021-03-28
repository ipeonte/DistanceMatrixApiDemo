package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.component.ConfigProperties;
import com.example.demo.model.GeoPoint;
import com.example.demo.model.Point;
import com.example.demo.shared.Constants;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;

@Service
public class LocationService {
  private static final Logger log = LoggerFactory.getLogger(Constants.LOG_NAME);

  @Autowired
  private ConfigProperties props;

  // Array with points
  private final List<Point> points = new ArrayList<>();

  public List<Point> getPoints() {
    return points;
  }

  /**
   * Find shortest path between given point and location points from list
   * 
   * @param point Geo point
   * @return Location point
   * @throws IOException 
   * @throws InterruptedException 
   * @throws ApiException 
   */
  public Point findShortestDistance(GeoPoint geoPoint) throws Exception {
    // Find 
    log.info("Finding shortest distance for {}", geoPoint);

    List<Point> points = findNearestPoints(geoPoint);
    log.debug("Found the next points in shortest radius {}", points.toString());

    // Convert points into destinations
    // String[] dest = new String[points.size()];
    String[] dest = points.stream().map(point -> point.flat())
        .toArray(size -> new String[size]);

    GeoApiContext context =
        new GeoApiContext.Builder().apiKey(props.getApiKey()).build();

    DistanceMatrixApiRequest api = DistanceMatrixApi.getDistanceMatrix(context,
        new String[] { geoPoint.flat() }, dest);

    DistanceMatrix matrix = api.await();

    // Check if return number of rows matches input
    if (matrix.rows.length == 0)
      throw new Exception(
          "Received empty DistanceMatrix rows result from GoogleApi");

    DistanceMatrixElement[] elements = matrix.rows[0].elements;
    int len = elements.length;
    if (points.size() != len)
      log.warn(
          "Returned number of row elements {} in DistanceMatrix row  doesn't corresponds the input size {}",
          len, points.size());

    // Find the shortest distance
    int idx = 0;
    long min = elements[0].distance.inMeters;
    log.trace("Distance from orig to {} is {}", points.get(0).getName(),
        elements[0].distance.humanReadable);

    for (int i = 1; i < len; i++) {
      Distance distance = elements[i].distance;
      log.trace("Distance from orig to {} is {}", points.get(i).getName(),
          distance.humanReadable);

      long dist = distance.inMeters;
      if (dist < min) {
        idx = i;
        min = dist;
      }
    }

    Point result = points.get(idx);
    log.info("Found shortest path to point: {}", result);
    return result;
  }

  /**
   * Find set of points from list with shortest straight distance from given point
   * 
   * @param point Geo point
   * @return Location point
   */
  List<Point> findNearestPoints(GeoPoint geoPoint) {
    // Number of elements in result array
    int num = props.getNbPointsNum();

    // Max distance
    double max = Double.MAX_VALUE;

    // Return list
    List<DistPoint> result = new ArrayList<DistPoint>();

    for (Point point : points) {
      double dist = getDistance(geoPoint, point);
      if (dist < max) {
        // Add element into result list and sort it
        // It's the easiest implementation algorithm based on the returning size of the list (3-5 points)
        result.add(new DistPoint(point, dist));

        // Sort list in ascending order
        Collections.sort(result);

        // Remove first element (longest), if any
        if (result.size() > num) {
          result.remove(0);

          // If result list is full adjust max distance
          if (result.size() == num)
            max = result.get(0).getDistance();
        }
      }
    }

    return result.stream().map(distPoint -> distPoint.getPoint())
        .collect(Collectors.toList());
  }

  private double getDistance(GeoPoint a, GeoPoint b) {
    double distLattitude = a.getLatitude() - b.getLatitude();
    double distLongitude = a.getLongitude() - b.getLongitude();

    return Math
        .sqrt(distLongitude * distLongitude + distLattitude * distLattitude);
  }

  @PostConstruct
  public void init() {
    File f = new File(props.getLocationFilePath());
    if (!f.exists()) {
      log.warn("File {} not found.", f.getAbsolutePath());
      return;
    }

    List<String> lines;
    try {
      lines = Files.readAllLines(f.toPath());
    } catch (IOException e) {
      log.error("Error reading file {}", f.getAbsolutePath());
      return;
    }

    if (lines.size() <= 1) {
      log.warn("Invalid size for csv file {}", f.getAbsolutePath());
      return;
    }

    // Skip first line with header
    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      String[] parts = line.split(",");
      if (parts.length != 3) {
        log.warn("Invalid line #{} with point coordinates '{}'", i, line);
        continue;
      }

      double[] coords = new double[2];

      for (int j = 0; j < 2; j++) {
        int idx = j + 1;
        try {
          coords[j] = Double.parseDouble(parts[idx]);
        } catch (NumberFormatException e) {
          log.error("Error parsing value {} from line #{}", parts[idx], i);
        }
      }

      points.add(new Point(parts[0], coords[0], coords[1]));
    }

    log.info("Successfully1 completed loading file {}", f.getAbsoluteFile());
    log.trace("{}: {}", f.getName(), points.toString());
  }
}

class DistPoint implements Comparable<DistPoint> {
  // Pointer on point
  private final Point point;

  // Distance from some given point
  private final double distance;

  public DistPoint(Point point, double distance) {
    this.point = point;
    this.distance = distance;
  }

  public Point getPoint() {
    return point;
  }

  public double getDistance() {
    return distance;
  }

  @Override
  public int compareTo(DistPoint point) {
    return Double.compare(point.getDistance(), distance);
  }
}
