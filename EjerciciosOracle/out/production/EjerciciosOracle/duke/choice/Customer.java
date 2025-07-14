package duke.choice;

public class Customer {
    private String name;
    private String size;
    private Clothing[] items;

    public Customer(String name, int measurement) {
        this.name = name;
        this.size = setSize(measurement);
    }

    public void addItems(Clothing[] newItem){
        items = newItem;
    }
    public Clothing[] getItems() {
        return items;
    }
    public void getTotalClothingCost(Clothing[] stock, double TAX){
        double total = 0;
        for (Clothing clothe: stock){
            System.out.println("Item: " + clothe.getDescription() + " , " + clothe.getSize() + " , " + clothe.getPrice());
            total += clothe.getPrice();
        }
        System.out.println("Precio final: " + (total + (total*TAX)));
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String setSize(int measurement){
        switch (measurement) {
            case 1: case 2: case 3:
                size = "S";
                break;
            case 4: case 5: case 6:
                size = "M";
                break;
            case 7: case 8: case 9:
                size = "L";
                break;
            default:
                size = "X";
                break;
        }
        return size;
    }
}