package duke.choice;

public class Clothing implements Comparable<Clothing>{
    private String description;
    private double price;
    private String size;
    public final static double MIN_PRICE = 10;
    public final static double TAX = 0.2;

    public Clothing(String description, double price, String size) {
        this.description = description;
        this.price = price;
        this.size = size;
    }
    public static double getMIN_PRICE() {
        return MIN_PRICE;
    }
    public static double getTAX() { return TAX; }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price * (1 + TAX);
    }
    public void setPrice(double price) {
        if (price >= MIN_PRICE) {
            this.price = price;
        }
        else{
            System.out.println("Precio menor a 10");
        }
    }
    @Override
    public String toString(){
        return (this.description + ", " + this.size + ", " + this.price);
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public int compareTo(Clothing c) {
        return this.description.compareTo(c.getDescription());
    }
}

