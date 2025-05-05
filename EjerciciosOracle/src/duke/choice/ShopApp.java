package duke.choice ;

public class ShopApp
{
    public static void main(String[] args)
    {
        double total = 0.0 ;
        double tax = 0.2 ;
        int measurement = 3 ;

        System.out.println(Clothing.tax + " " + Clothing.minPrice) ;
        System.out.println("Bienvenido a la tienda Duke Choice!") ;

        Customer c1 = new Customer("Pinky", measurement) ;
        System.out.println("Nombre del comprador: " + c1.getName()) ;

        Clothing item1 = new Clothing("", 0, "") ;
        Clothing item2 = new Clothing("", 0, "") ;
        Clothing item3 = new Clothing("", 0, "") ;
        Clothing item4 = new Clothing("", 0, "") ;

        item1.setDescription("Blue Jacket") ;
        item1.setPrice(20.9) ;
        item1.setSize("S") ;

        item2.setDescription("Orange T-Shirt") ;
        item2.setPrice(10.5) ;
        item2.setSize("S") ;

        item3.setDescription("Green Scarf") ;
        item3.setPrice(5) ;
        item3.setSize("S") ;

        item4.setDescription("Blue T-Shirt") ;
        item4.setPrice(10.5) ;
        item4.setSize("S") ;

        Clothing[] stock = { item1, item2, item3, item4 } ;

        System.out.println("--------------------------");
        System.out.println("Item 1: " + item1.getDescription() + " , talle: " + item1.getSize() + " , precio: $" + item1.getPrice()) ;
        System.out.println("Item 2: " + item2.getDescription() + " , talle: " + item2.getSize() + " , precio: $" + item2.getPrice()) ;

        for (Clothing ropita : stock) {
            if (ropita.getSize().equals(c1.getSize()))
            {
                double precioConImpuesto = ropita.getPrice();
                if (total + precioConImpuesto > 15)
                {
                    break;
                }
                System.out.println("--------------------------");
                System.out.println("Item: " + ropita.getDescription());
                System.out.println("Talle: " + ropita.getSize());
                System.out.printf("Precio: $", precioConImpuesto);

                total += precioConImpuesto;
            }
        }

        total = total * (total+tax) ;
        total = (item1.getPrice() + (item2.getPrice() * 2)) * (1+tax) ;
        System.out.println("Precio total: $" + total) ;

        c1.getTotalClothingCost(stock, Clothing.tax) ;
        
        switch (measurement)
        {
            case 1: case 2: case 3:
                c1.setSize("S") ;
                break ;
            case 4: case 5: case 6:
                c1.setSize("M") ;
                break ;
            case 7: case 8: case 9:
                c1.setSize("L") ;
                break ;
            default:
                c1.setSize("X") ;
                break ;
        }
    }
}
