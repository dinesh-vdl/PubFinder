package com.mapsapi.dinesh.pubfinder;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

public class GetNearbyPubsData  extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPubsData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPubsList = null;
        //Stores nearby pubs data into a list
        DataParser dataParser = new DataParser();
        nearbyPubsList =  dataParser.parse(result);
        //To show nearby pubs on map
        ShowNearbyPubs(nearbyPubsList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    //Invoked when request is made to show nearby pubs on map
    private void ShowNearbyPubs(List<HashMap<String, String>> nearbyPubsList) {
        for (int i = 0; i < nearbyPubsList.size(); i++) {
            Log.d("onPostExecute","Entered into showing nearby pubs");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPubsList.get(i);

            //Get latitude and longitude of the pub
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            LatLng latLng = new LatLng(lat, lng);

            //Get pub info to be displayed on the marker
            String placeName = googlePlace.get("place_name");
            String rating = googlePlace.get("rating");

            //Add marker on the map
            markerOptions.position(latLng);
            markerOptions.title(placeName);
            markerOptions.snippet("Rating: " + rating);
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
    }
}
