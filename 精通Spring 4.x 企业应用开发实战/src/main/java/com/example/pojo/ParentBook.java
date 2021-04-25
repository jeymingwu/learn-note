package com.example.pojo;

/**
 * @author : jeymingwu
 * @date : 2021-04-25
 **/

public class ParentBook {

    private Book book;

    public ParentBook() {
    }

    public ParentBook(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "ParentBook{" +
                "book=" + book +
                '}';
    }
}
