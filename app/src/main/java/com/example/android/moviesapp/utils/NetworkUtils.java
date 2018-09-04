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

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils
{
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String STATIC_MOVIE_URL =
            "https://api.themoviedb.org/3/movie/";

    final static String API_KEY_PARAM = "api_key";

    final static String POPULARITY = "popular";

    final static String RATING = "top_rated";

    private static final String BASE_URL_POPULAR = STATIC_MOVIE_URL + POPULARITY;

    private static final String BASE_URL_RATING = STATIC_MOVIE_URL + RATING;

    /* The format we want our API to return */
    private static final String format = "json";

    public static URL buildUrl(String key, String filter)
    {
        URL url = null;

        if(filter == "popular")
        {
            Uri builtUri = Uri.parse(BASE_URL_POPULAR).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, key)
                    .build();

            try
            {
                url = new URL(builtUri.toString());
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }

            Log.v(TAG, "Built URI " + url);
        }
        else if(filter == "top_rated")
        {
            Uri builtUri = Uri.parse(BASE_URL_RATING).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, key)
                    .build();

            try
            {
                url = new URL(builtUri.toString());
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }

            Log.v(TAG, "Built URI " + url);
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try
        {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
            {
                return scanner.next();
            }
            else
            {
                return null;
            }

        }
        finally
        {
            urlConnection.disconnect();
        }
    }
}
