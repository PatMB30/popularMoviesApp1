package com.example.android.moviesapp.data;

import android.content.Context;

public class MoviePreferences
{
    private static final String API_KEY = "";

    public static String getDefaultConnectionUrl(Context context)
    {
        return getAPIKey();
    }

    private static String getAPIKey()
    {
        return API_KEY;
    }
}
