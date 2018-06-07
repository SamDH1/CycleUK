package com.pul.sam.Cycle;

import android.util.Log;

import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;

import java.util.ArrayList;
import java.util.Objects;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CycleTrails extends MainActivity {



    public static double LAT_MAX = myLatitude + (0.01 * 20);
    public static double LAT_MIN = myLatitude - (0.01 * 20);
    public static double LON_MAX = myLongitude + (0.01 * 20);
    public static double LON_MIN = myLongitude - (0.01 * 20);
    public static String trailTemp;
    public static String trailWeather;
    public static String trailConditions;
    static String previousWeather;

    public static ArrayList<Trails> getTrails() {
        Log.d("trails", "Cycle trails running");
        ArrayList<Trails> trailList = new ArrayList<>();
        Trails t1 = new Trails(51.71, -3.36, "Bike Park Wales");
        Trails t2 = new Trails(51.66, -3.35, "Mountain Ash");
        Trails t3 = new Trails(50.50,-4.17, "FlyUp Downhill");
        trailList.add(t1);
        trailList.add(t2);
        trailList.add(t3);



        final ArrayList<Trails> localTrails = new ArrayList<>();
        for(final Trails trail : trailList) {
            Log.d("List running", "Local trail list");
            if (trail.lat > LAT_MIN && trail.lat < LAT_MAX && trail.lon > LON_MIN && trail.lon < LON_MAX) {
                localTrails.add(trail);
                Log.d("Calc", "Run code");


                final RequestBuilder weather = new RequestBuilder();

                final Request request = new Request();
                request.setLat(String.valueOf(trail.lat));
                Log.d("Lat used", String.valueOf(trail.lat));
                Log.d("Actual Lat", request.getLat());
                request.setLng(String.valueOf(trail.lon));
                Log.d("Lon used", String.valueOf(trail.lon));
                request.setUnits(Request.Units.UK);
                request.setLanguage(Request.Language.ENGLISH);
                request.addExcludeBlock(Request.Block.CURRENTLY);
                request.removeExcludeBlock(Request.Block.CURRENTLY);


                weather.getWeather(request, new Callback<WeatherResponse>() {
                    @Override
                    public void success(WeatherResponse weatherResponse, Response response) {
                        Log.d("Weather", "We have weather!");
                        Log.d("Location", weatherResponse.getTimezone());
                        trailTemp = String.valueOf(weatherResponse.getCurrently().getTemperature());
                        Log.d("Temperature", trailTemp);
                        trailWeather = weatherResponse.getDaily().getIcon();
                        Log.d("Weather", trailWeather);

                        previousWeather = weatherResponse.getDaily().getIcon();
                        Log.d("Yesterday weather", previousWeather);

                        if ((((Objects.equals(previousWeather, "rain")) && (Objects.equals(trailWeather, "clear-day")))) || (((Objects.equals(previousWeather, "clear-day")) && (Objects.equals(trailWeather, "rain"))))) {
                            trailConditions = "Muddy";
                            Log.d("Cond", trailConditions);

                        } else if ((Objects.equals(previousWeather, "clear-day")) && (Objects.equals(trailWeather, "clear-day"))) {
                            trailConditions = "Dry";
                            Log.d("Cond", trailConditions);

                        } else {
                            trailConditions = "Sloppy";
                            Log.d("Cond", trailConditions);

                        }


                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.d("Error while calling: ", "Weather");
                    }
                });
            }

            else {
                Log.d("Too far", "Trails too far" + trail.name);
            }
        }
        return localTrails;
    }

}
