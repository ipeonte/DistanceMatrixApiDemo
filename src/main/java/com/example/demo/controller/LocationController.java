package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.GeoPoint;
import com.example.demo.model.Point;
import com.example.demo.service.LocationService;
import com.example.demo.shared.Constants;

@RestController
@RequestMapping(Constants.BASE_URL)
public class LocationController {

  @Autowired
  private LocationService location;

  @GetMapping(Constants.LOCATIONS_URL)
  public List<Point> getPoints() {
    return location.getPoints();
  }

  @PostMapping(Constants.FIND_SHORTEST_DISTANCE)
  public Point findShortestDistance(@RequestBody GeoPoint point)
      throws Exception {
    return location.findShortestDistance(point);
  }
}
