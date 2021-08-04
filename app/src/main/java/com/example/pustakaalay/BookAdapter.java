package com.example.pustakaalay;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = BookAdapter.class.getSimpleName();

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    /**
     * Returns a list item view that displays information about the book at the given position
     * in the list of books.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_item, parent, false);
        }

        Book currentBook = getItem(position);

        // Get the author textView and assign author from book object
        TextView Author = listItemView.findViewById(R.id.author);
        Author.setText(currentBook.getAuthor());

        // Get the title textView and assign title from book object
        TextView Title = listItemView.findViewById(R.id.book_title);
        Title.setText(currentBook.getTitle());

        // Get the price textView and assign price from book object
        TextView Price = listItemView.findViewById(R.id.price);

        // Format the price to show 2 decimal place
        String formattedPrice = formatPrice(currentBook.getPrice());

        // If book is free then set text as FREE
        // Display the Price of the current book in that TextView
        if (formattedPrice.equals("FREE")) {
            Price.setText(R.string.free);
        } else {
            Price.setText("Rs. " + formattedPrice);
        }

        Log.i(LOG_TAG, "ListView returned");

        return listItemView;
    }

    private String formatPrice(double price) {

        // set price upto two decimal
        DecimalFormat priceFormat = new DecimalFormat("0.00");

        // If price 0 then return FREE
        if (price == 0.00) {
            return "FREE";
        }
        return priceFormat.format(price);
    }

}
