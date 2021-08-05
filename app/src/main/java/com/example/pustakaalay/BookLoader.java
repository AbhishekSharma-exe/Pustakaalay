package com.example.pustakaalay;

import android.content.Context;
import android.util.Log;


import android.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {


    private static final String LOG_TAG = BookLoader.class.getName();

    /** Query URL */
    private String mUrl;


    public BookLoader( Context context,String url) {
        super(context);
        mUrl=url;
        Log.e(LOG_TAG, ": Loaded!");
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.e("On start loading", ": Force loaded!");
    }


    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of Books.
        List<Book> books = QueryUtils.fetchBooksData(mUrl);
        Log.e(LOG_TAG, ": Loaded in background!");
        return books;
    }
}
