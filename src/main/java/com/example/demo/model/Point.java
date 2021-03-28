/**
 * 
 */
package com.example.demo.model;

/**
 * Model for single Geo Point
 */
public class Point extends GeoPoint {

  private final String name;

  public Point(String name, double latitude, double longitude) {
    super(latitude, longitude);
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "name: " + name + "; " + super.toString();
  }
}
