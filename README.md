# Google Map MatrixDistance API Demo project

This project demonstrates the use of Google Map Distance Matrix API to find nearest distance from given point to set of pre-defined locations.
One of the usage of given solution could be finding the nearest store (kiosk) for registered user.

## Build
mvn clean package

## Pre-requisite
The active Google API key and project with Billing info setup is required
See more https://developers.google.com/maps/gmp-get-started#create-project

## Local setup
Create application.properties file with next parameters:\

	demo.api-key=<YOUR GOOGLE API KEY>
	demo.location-file-path=demo_points.csv

	logging.level.root=info
	logging.level.GMDemo=trace
	
File demo.csv is located in project's root folder.
In case if custom csv file is used then the parameter "demo.location-file-path" needs to be changed.
The custom file should have same number of columns as demo_points.csv
The first row in csv file is ignored.

## Run
java -jar target/distance-mx-api-demo.jar 

## Demo
Open browser and navigate on http://localhost:8080/

Click on desired location and watch for point with shortest driving distance start bouncing.
This demo does not return detailed route information because this is how Distance Matrix API works.
Route information can be obtained from the Directions API.

To stop marker bouncing click on it again or click on new desired location on the map. If existing marker keep bouncing it means it still the nearest driving destination.
