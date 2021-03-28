var BASE_URL = "/api/v1";

// Image Source: https://developers.google.com/maps/documentation/javascript/markers#symbols
const svgMarker = {
    path:
      "M10.453 14.016l6.563-6.609-1.406-1.406-5.156 5.203-2.063-2.109-1.406 1.406zM12 2.016q2.906 0 4.945 2.039t2.039 4.945q0 1.453-0.727 3.328t-1.758 3.516-2.039 3.070-1.711 2.273l-0.75 0.797q-0.281-0.328-0.75-0.867t-1.688-2.156-2.133-3.141-1.664-3.445-0.75-3.375q0-2.906 2.039-4.945t4.945-2.039z",
    fillColor: "blue",
    fillOpacity: 0.6,
    strokeWeight: 0,
    rotation: 0,
    scale: 2,
    // anchor: new google.maps.Point(15, 30),
  };
  
function initMap() {
  map = new google.maps.Map(document.getElementById("map"), {
    center: { lat: 43.64870899587975, lng: -79.38225274418726 },
    zoom: 15,
  });
}

window.gMapsCallback = function(){
    $(window).trigger('gMapsLoaded');
}

$(document).ready((function(){
	// API Key
	var key;

	// Demo points
	var points;
	
	// Points map
	var pmap = {};
	
	// Custom marker
	var marker;
	
	// Currently bouncing marker
	var bouncing;
	
    function initialize(){
        var mapOptions = {
            zoom: 15,
            center: { lat: 43.64870899587975, lng: -79.38225274418726 },
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        
        map = new google.maps.Map(document.getElementById('map'),mapOptions);

		if (points !== undefined && points.length > 0)
			for(idx in points)
				pmap[points[idx].name] = addMarker(points[idx]);
        		
        map.addListener('click', function(e) {
    		placeMarker(map, e.latLng);
		});
    }
    
    function loadGoogleMaps(){
        var script = document.createElement('script');
        script.setAttribute("type","text/javascript");
        // script_tag.setAttribute("src","http://maps.google.com/maps/api/js?sensor=false&callback=gMapsCallback");
        script.setAttribute("src","https://maps.googleapis.com/maps/api/js?key=" + key + "&callback=gMapsCallback");
        (document.getElementsByTagName("head")[0] || document.documentElement).appendChild(script);
    }
    
    function addMarker(point) {
        return new google.maps.Marker({
            map: map,
            title: point.name,
            position: new google.maps.LatLng(point.latitude, point.longitude),
            animation: google.maps.Animation.DROP
          });
    }
    
    function loadConfig() {
    	$.ajax({
    		url: BASE_URL + "/config?keys=api_key",
    		success: function(data) {
    			key = data.api_key;
    			loadPoints();
    		},
    		error: function(error) {
    			console.log(error)
    		}
    	});
    }
    
    function loadPoints() {
    	$.ajax({
    		url: BASE_URL + "/locations",
    		success: function(data) {
    			points = data;
    			loadGoogleMaps();
    		},
    		error: function(error) {
    			console.log(error)
    		}
    	});
    }
    
    function placeMarker(map, position) {
    	// Remove previous marker
    	if (marker !== undefined)
    		marker.setMap(null);
    	
		marker = new google.maps.Marker({
    		map: map,
    		icon: "images/point.png",
    		position: position
		});
		
		map.panTo(position);
		
		findPath(map, position);
	}
    
    function findPath(map, position) {
    	$.ajax({
    		url: BASE_URL + "/find_shortest_distance",
    		method: "POST",
    		data: JSON.stringify({
    			latitude: position.lat(),
    			longitude: position.lng()
    		}),
    		contentType: "application/json",
    		success: function(data) {
    			console.log(data);
    			
    			// Find marker
    			var marker = pmap[data.name];
    			
    			if (marker === undefined) {
    				console.log("Marker " + data.name + " not found.");
    				return;
    			}
    			
    			// Check if anything bouncing
    			if (bouncing !== undefined) {
    				bouncing.setAnimation(null);
    				bouncing = undefined;
    			}
    			
    			marker.setAnimation(google.maps.Animation.BOUNCE);
    			marker.addListener("click", function() {
    				marker.setAnimation(null);
    			});
    			bouncing = marker;
    		},
    		error: function(error) {
    			console.log(error)
    		}
    	});
    }
    
    $(window).bind('gMapsLoaded', initialize);
    loadConfig();
})())

