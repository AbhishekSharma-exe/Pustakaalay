package com.example.pustakaalay;

public class Book {

    //  private String mImage;

    /**
     * Name of the author
     */
    private String mAuthor;

    /**
     * Title of the book
     */
    private String mTitle;

    /**
     * Price of the book
     */
    private double mPrice;

    /**
     * Url for buying the book on clicking the view
     */
    private String mUrl;

    /**
     * constructor of book object
     *
     * @param author
     * @param title
     * @param price
     * @param url
     */

    public Book(String author, String title, double price, String url) {
        mAuthor = author;
        mPrice = price;
        mTitle = title;
        mUrl = url;
    }


    /**
     * Returns the author of the book.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Returns the title of the book.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the price of the book.
     */
    public double getPrice() {
        return mPrice;
    }

    /**
     * Returns the website URL to find more information about the book.
     */
    public String getUrl() {
        return mUrl;
    }
}
