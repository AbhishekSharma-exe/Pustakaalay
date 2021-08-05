package com.example.pustakaalay;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private QueryUtils() {
    }


    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<Book> fetchBooksData(String requestUrl) {
        Log.e(LOG_TAG, "Fetch data");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            //Thread.sleep(2000);
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        List<Book> listbooks = extractInfoFromJson(jsonResponse);

        // Return the list of {@link Book}s
        return listbooks;
    }

    private static List<Book> extractInfoFromJson(String BookJsonResponse) {

        // If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(BookJsonResponse)) {
            return null;
        }

        // Create an empty ArrayList to add books in it
        List<Book> books = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseObject = new JSONObject(BookJsonResponse);

            // Get the JsonArray with name items
            JSONArray BookArray = baseObject.getJSONArray("items");

            // traverse through the array
            for (int i = 0; i < BookArray.length(); i++) {

                // Get a single book at position i within the list of books in array
                JSONObject currentBook = BookArray.getJSONObject(i);

                // get the volumeInfo object containgn info abt book
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                //get title from key "title"
                String title = volumeInfo.getString("title");

                // Get the JsonArray of authors with name authors
                JSONArray author = volumeInfo.getJSONArray("authors");

                // get the name of first author only
                String authorName = author.getString(0);

                //get the link for further info of book on GOOGLE BOOKS website/app
                String infoLink = volumeInfo.getString("infoLink");

                //get the salesinfo object containing price
                JSONObject salesInfo = currentBook.getJSONObject("saleInfo");

                double price;

                // saleability of book is free the set price 0.00, else get the price
                if (salesInfo.getString("saleability").equals("FREE")) {

                    price = 0.00;
                } else {

                    JSONObject retailPrice = salesInfo.getJSONObject("retailPrice");
                    price = retailPrice.getDouble("amount");
                }

                // Create a new Book object and pass the parameters
                Book book = new Book(authorName, title, price, infoLink);

                // Add the object in list
                books.add(book);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }
        // Return the list of books
        return books;

    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String requestUrl) {

        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;

    }


}
