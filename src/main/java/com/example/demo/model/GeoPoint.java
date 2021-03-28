/**
 * 
 */
package com.example.demo.model;

/**
 * Model for single Geo Point
 */
public class GeoPoint {

  private final double latitude;

  private final double longitude;

  public GeoPoint(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  /**
   * Flat coordinates into simple string array
   * 
   * @return simple string array with [latitude,longitude]
   */
  public String flat() {
    return latitude + "," + longitude;
  }

  @Override
  public String toString() {
    return "latitude: " + getLatitude() + "; longitude: " + getLongitude() +
        ";";
  }
}
