package com.example.android.moviesapp;

public class Movie
{
    private String mainName;
    //private List<String> alsoKnownAs = null;
    //private String placeOfOrigin;
    //private String description;
    private String image;
    private int imageID;
    //private List<String> ingredients = null;

    /**
     * No args constructor for use in serialization
     */
    public Movie()
    {

    }

    public Movie(String mainName, String image, int imgID)
    {
        this.mainName = mainName;
        //this.alsoKnownAs = alsoKnownAs;
        //this.placeOfOrigin = placeOfOrigin;
        //this.description = description;
        this.image = image;
        this.imageID = imgID;
        //this.ingredients = ingredients;
    }

    public String getMainName()
    {
        return mainName;
    }

    public void setMainName(String mainName)
    {
        this.mainName = mainName;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public int getImageID()
    {
        return imageID;
    }

    public void setImageID(int imgID)
    {
        this.imageID = imgID;
    }

}
