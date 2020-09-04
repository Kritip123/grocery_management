package com.dsc.grocerymanagement.model;


public class grocerymodel {
    String name;
    String save;
    String price;
    String img;
    String price0;

    grocerymodel(){

    }

    public grocerymodel(String name, String save, String price, String image) {
        this.name = name;
        this.save = save;
        this.price = price;
        this.img = image;
    }

    public String getPrice0() { return price0; }

    public void setPrice0(String price0) { this.price0 = price0; }

    public  String getImg() { return img;  }

    public void setImg(String image) { this.img = image; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}