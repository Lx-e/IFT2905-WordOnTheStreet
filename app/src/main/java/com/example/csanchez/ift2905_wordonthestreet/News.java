package com.example.csanchez.ift2905_wordonthestreet;

public class News {
    String title;
    String description;
    String source;
    String author;
    String image;
    String url;
    String date;

    public News(String _title, String _description, String _source, String _author, String _image, String _url, String _date){
        this.title       = _title;
        this.description = _description;
        this.source      = _source;
        this.author      = _author;
        this.image       = _image;
        this.url         = _url;
        this.date        = _date;
    }

}
