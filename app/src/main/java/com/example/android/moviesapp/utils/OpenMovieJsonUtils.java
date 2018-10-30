/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.moviesapp.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.example.android.moviesapp.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.List;

public final class OpenMovieJsonUtils
{
    //parsed list of movie objects
    private List<Movie> movies;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException
    {
        /* String designating movie results from movieDB */
        final String MOV_RESULTS = "results";

        //Movie ID
        final String MOV_ID = "id";
        //Movie Title
        final String MOV_TITLE = "title";
        //Movie Thumbnail Image ID
        final String MOV_THUMB = "poster_path";
        //Movie plot synopsis
        final String MOV_PLOT = "overview";
        //Movie user rating
        final String MOV_RAT = "vote_average";
        //Movie release date
        final String MOV_DATE = "release_date";

        final String OWM_MESSAGE_CODE = "cod";

        String[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        if (movieJson.has(OWM_MESSAGE_CODE))
        {
            int errorCode = movieJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode)
            {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        return movieJsonStr;
    }

}