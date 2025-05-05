package duke.choice;

public class Clothing
{
        private String description ;
        private double price ;
        private String size ;
        public static double minPrice = 10 ;
        public static double tax = 0.2 ;

       // Constructor por parametro
        public Clothing (String description, double price, String size)
        {
            this.description = description ;
            this.price = price ;
            this.size = size ;
        }

        // Getters
        public double getminPrice()
        {
            return minPrice ;
        }

        public String getDescription()
        {
            return description ;
        }

        public String getSize()
        {
            return size ;
        }

        public double getPrice()
        {
            return price * (1 + tax) ;
        }

        // Setters
        public void setDescription(String description)
        {
            this.description = description ;
        }

        public void setPrice(double price)
        {
            if (price >= minPrice)
            {
                this.price = price ;
            }
            else
            {
                System.out.println("Precio menor a 10") ;
            }
        }

        public void setSize(String size)
        {
            this.size = size ;
        }
}
