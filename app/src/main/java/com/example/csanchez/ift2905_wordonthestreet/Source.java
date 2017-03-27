package com.example.csanchez.ift2905_wordonthestreet;

public class Source {
    String id;
    String name;
    String description;
    String url;
    String categorie;
    String language;
    String country;
    String smallLogo;
    String mediumLogo;
    String largeLogo;

    public Source(String _id, String _name, String _description, String _categorie, String _language, String _country, String _smallLogo, String _mediumLogo, String _largeLogo){
        this.id = _id;
        this.name = _name;
        this.description = _description;
        this.url = _categorie;
        this.categorie = _language;
        this.language = _country;
        this.country = _smallLogo;
        this.smallLogo = _mediumLogo;
        this.mediumLogo = _largeLogo;
    }
}
