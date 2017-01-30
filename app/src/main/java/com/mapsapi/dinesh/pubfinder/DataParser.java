package com.mapsapi.dinesh.pubfinder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DataParser {
    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        //Inside placesList each element(place) is a hash map containing info about the place
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        Log.d("Places", "getPlaces");

        //Iterate over the length of places and fetch required attributes from the JSON array
        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    //Place info is retrieved from the JSON array
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String placeName = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        String rating = "-NA-";
        Log.d("getPlace", "Entered");

        try {
            //Get name and rating of the place (If available)
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("rating")){
                rating = String.valueOf(googlePlaceJson.getDouble("rating"));
            }
            //Fetch latitude,longitude and reference of the place
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");
            //Store the details inside the googlePlaceMap
            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            googlePlaceMap.put("rating",rating);

            Log.d("getPlace", "Putting Places");
        } catch (JSONException e) {
            Log.d("getPlace", "Error");
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}