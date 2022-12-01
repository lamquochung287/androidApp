package tdtu.finalproject.appsale;

import java.io.Serializable;
// Class
// save attribute of item
public class Item implements Serializable {
    public int id;
    public String name;
    public int price;
    public String image;
    public String decription;
    public int idtype;

    public Item(int id, String name, int price, String image, String decription, int idtype) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.decription = decription;
        this.idtype = idtype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public int getIdtype() {
        return idtype;
    }

    public void setIdtype(int idtype) {
        this.idtype = idtype;
    }
}
