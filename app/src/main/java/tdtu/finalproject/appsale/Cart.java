package tdtu.finalproject.appsale;
//class
public class Cart {
    public int id;
    public String nameItem;
    public long price;
    public String image;
    public int numberItem;

    public Cart(int id, String nameItem, long price, String image, int numberItem) {
        this.id = id;
        this.nameItem = nameItem;
        this.price = price;
        this.image = image;
        this.numberItem = numberItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNumberItem() {
        return numberItem;
    }

    public void setNumberItem(int numberItem) {
        this.numberItem = numberItem;
    }
}
