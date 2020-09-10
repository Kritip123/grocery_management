package com.dsc.grocerymanagement.model;


public class groceryModel {
    String name;
    String save;
    String price;
    String img;
    String price0;

    public groceryModel() {

    }

    public groceryModel(String name, String save, String price, String image, String price0) {
        this.name = name;
        this.save = save;
        this.price = price;
        this.img = image;
        this.price0 = price0;
    }

    public String getPrice0() {
        return price0;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSave() {
        return save;
    }

    public String getPrice() {
        return price;
    }

}