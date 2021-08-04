package com.example.pustakaalay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.*;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = BookActivity.class.getSimpleName();

    private String GOOGLE_BOOKS_URL = "";


    /**
     * Constant value for the book loader ID.
     * Used when using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    /**
     * Listview adapter used in creating list of books
     */
    private BookAdapter adapter;

    /**
     * search button
     */
    private Button searchButton;

    /**
     * Edit text view
     */
    private EditText editText;

    /**
     * Progress loader
     */
    private ProgressBar progressBar;

    /**
     * Empty text view for showing message
     */
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assigning the listView
        ListView bookListView = findViewById(R.id.list);

        searchButton = findViewById(R.id.button);

        editText = findViewById(R.id.editText);

        progressBar = findViewById(R.id.progressBar);

        // Disabled the loading indicator on starting the app
        progressBar.setVisibility(GONE);

        mEmptyStateTextView = findViewById(R.id.emptyView);

        // Create a new adapter that takes an empty list of books as input
        adapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the listview
        bookListView.setAdapter(adapter);

        // Checks internet connection on opening the app
        if (!isNetworkAvailable()) {
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Click listener for Search button
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {
                    updateURLString(editText.getText().toString());
                    RestartLoader();
                    Log.i(LOG_TAG, "Search Book: " + editText.getText().toString());
                }
                else {
                    // Clear the previous output or listView
                    adapter.clear();

                    mEmptyStateTextView.setVisibility(VISIBLE);

                    // Show the message no internet
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current book that was clicked on
                Book currentBook = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentBook.getUrl());

                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


    }

    /**
     * This func checks the status of the network
     * @return boolean, true or false
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * This function restart the loader on searching for more than once
     */
    private void RestartLoader() {

        progressBar.setVisibility(VISIBLE);
        mEmptyStateTextView.setVisibility(GONE);
        LoaderManager.getInstance(this).restartLoader(BOOK_LOADER_ID, null, this);
    }

    /**
     * Takes the input from user removes the spaces from it and merges with GOOGLE BOOKS API URl
     * @param searchStr user input from editText
     * @return final google books url string
     */
    private String updateURLString(String searchStr) {

        String updatedSearchStr = searchStr.replaceAll("\\s", "");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://www.googleapis.com/books/v1/volumes?q=").append(updatedSearchStr).append("&filter=ebooks&maxResults=20");
        GOOGLE_BOOKS_URL = stringBuilder.toString();
        return GOOGLE_BOOKS_URL;
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        updateURLString(editText.getText().toString());
        return new BookLoader(this, GOOGLE_BOOKS_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {

        mEmptyStateTextView.setText(R.string.no_books_found);

        progressBar.setVisibility(GONE);

        adapter.clear();

        // If there is a valid list of link books, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }

        // Make no books found visible
        else {
            mEmptyStateTextView.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
        Log.i(LOG_TAG, ": Loader reset");
    }
}