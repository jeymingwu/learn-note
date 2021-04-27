package com.example.pojo;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author : jeymingwu
 * @date : 2021-04-27
 **/

public class BookFactoryBean implements FactoryBean<Book> {

    private String info;

    public BookFactoryBean() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public Book getObject() throws Exception {
        Book book = new Book();
        String[] infos = info.split(",");
        book.setId(Integer.parseInt(infos[0]));
        book.setName(infos[1]);
        book.setPrice(Double.parseDouble(infos[2]));
        return book;
    }

    @Override
    public Class<Book> getObjectType() {
        return Book.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
