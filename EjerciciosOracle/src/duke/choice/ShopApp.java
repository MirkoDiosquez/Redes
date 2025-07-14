package duke.choice;

import java.util.Arrays;

public class ShopApp {
    public static void main(String[] args) {
        double total = 0.0;
        // double tax = 0.2;
        int measurement = 3;
        System.out.println(Clothing.getTAX() + " " + Clothing.getMIN_PRICE());
        System.out.println("Bienvenido a la tienda Duke Choice!");
        System.out.println("Prueba de que funcionan los atributos estaticos: " + Clothing.getMIN_PRICE() + ", " + Clothing.getTAX());
        Customer c1 = new Customer("Pinky", measurement);
        // c1.setName("Pinky");
        // c1.setSize(measurement);
        System.out.println("Nombre del comprador: " + c1.getName());
        Clothing item1 = new Clothing("Blue Jacket", 20.9, "S");
        Clothing item2 = new Clothing("Orange T-Shirt", 10.5, "S");
        Clothing item3 = new Clothing("Green Scarf", 5, "S");
        Clothing item4 = new Clothing("Blue T-Shirt", 10.5, "S");
        /*item1.setDescription("Blue Jacket");
        item1.setPrice(20.9);*/
        // item2.setDescription("Orange T-Shirt");
        // item2.setPrice(10.5);
        // item2.setSize("S");
        // item3.setDescription("Green Scarf");
        // item3.setPrice(5);
        // item3.setSize("S");
        // item4.setDescription("Blue T-Shirt");
        // item4.setPrice(10.5);
        // item4.setSize("S");
        Clothing[] stock = {item1, item2, item3, item4};
        c1.addItems(stock);
        /*
        System.out.println("Item 1: " + item1.description + " , " + item1.size + " , " + item1.price);
        */
        // Antes del override
        //ahora
        System.out.println(item2);
        /*
        for (Clothing clothe: stock){
            if(total > 15){
                break;
            }
            if(clothe.getSize().equals(c1.getSize())){
                System.out.println("Item: " + clothe.getDescription() + " , " + clothe.getSize() + " , " + clothe.getPrice());
                total += clothe.getPrice();
            }
        }
        */
        // total = total * (total+tax);
        // total = (item1.price + (item2.price * 2)) * (1+tax);
        //System.out.println("Precio total: " + total);
        c1.getTotalClothingCost(stock, Clothing.getTAX());
        /*
        switch (measurement) {
            case 1: case 2: case 3:
                c1.setSize("S");
                break;
            case 4: case 5: case 6:
                c1.setSize("M");
                break;
            case 7: case 8: case 9:
                c1.setSize("L");
                break;
            default:
                c1.setSize("X");
                break;
        }
        */
        for (Clothing c : c1.getItems()){
            System.out.println("Item: " + c);
        }
        Arrays.sort(c1.getItems());
        for (Clothing c : c1.getItems()){
            System.out.println("Item: " + c);
        }
    }
}
