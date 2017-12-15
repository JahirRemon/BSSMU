package com.example.mdjahirulislam.bssmu_demo.model;



/**
 * Created by user on 12/13/2017.
 */

public class BookItem {

    public String bookName;
    public String authorName;
    public String edition;
    public String category;
    public String e_bookStatus;
    public int path;


    public BookItem(String bookName, String authorName, String edition, String category, String e_bookStatus, int path) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.edition = edition;
        this.category = category;
        this.e_bookStatus = e_bookStatus;
        this.path = path;
    }

    public BookItem(String bookName, String authorName, String edition, String category, String ebookStatus) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.edition = edition;
        this.category = category;
        this.e_bookStatus = ebookStatus;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getE_bookStatus() {
        return e_bookStatus;
    }

    public void setE_bookStatus(String e_bookStatus) {
        this.e_bookStatus = e_bookStatus;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "BookItem{" +
                "bookName='" + bookName + '\'' +
                ", authorName='" + authorName + '\'' +
                ", edition='" + edition + '\'' +
                ", category='" + category + '\'' +
                ", e_bookStatus='" + e_bookStatus + '\'' +
                '}';
    }
}
