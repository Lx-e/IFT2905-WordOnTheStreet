package com.example.csanchez.ift2905_wordonthestreet;

public class Source {
    String id;
    String name;
    String description;
    String url;
    String category;
    String language;
    String country;
    String smallLogo;
    String mediumLogo;
    String largeLogo;

    public Source(String _id, String _name, String _description, String _url, String _categorie, String _language, String _country, String _smallLogo, String _mediumLogo, String _largeLogo){
        this.id          = _id;
        this.name        = _name;
        this.description = _description;
        this.url         = _url;
        this.category    = _categorie;
        this.language    = _language;
        this.country     = _country;
        this.smallLogo   = _smallLogo;
        this.mediumLogo   = _mediumLogo;
        this.largeLogo  = _largeLogo;
    }
}
