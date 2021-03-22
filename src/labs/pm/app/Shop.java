/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labs.pm.app;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Locale;
import labs.pm.data.Product;
import labs.pm.data.ProductManager;
import labs.pm.data.Rating;

/**
 *
 * @author antoi
 */
public class Shop {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ProductManager pm = new ProductManager("ru-RU");
        pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
        pm.printProductReport(101);
        pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea.");
        pm.reviewProduct(101, Rating.TWO_STAR, "Rather weak tea.");
        pm.reviewProduct(101, Rating.FOUR_STAR, "Fine tea.");
        pm.reviewProduct(101, Rating.FOUR_STAR, "Good tea.");
        pm.reviewProduct(101, Rating.FIVE_STAR, "Perfect tea.");
        pm.reviewProduct(101, Rating.THREE_STAR, "Just add some lemon.");
        pm.printProductReport(101);

        pm.changeLocale("en-UK");
        pm.printProductReport(101);
        
        
        pm.createProduct(102, "Water", BigDecimal.valueOf(.9), Rating.NOT_RATED);
        pm.createProduct(103, "Coke", BigDecimal.valueOf(1.2), Rating.NOT_RATED);
        pm.createProduct(104, "Lemonade", BigDecimal.valueOf(2.9), Rating.NOT_RATED);

        pm.printProducts((p1,p2) -> p2.getPrice().compareTo(p1.getPrice()));
        
        Comparator<Product> ratingSorter = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
        Comparator<Product> priceSorter = (p1, p2) -> p2.getPrice().compareTo(p1.getPrice());
        
        //Combinando comparators
        pm.printProducts(ratingSorter.thenComparing(priceSorter).reversed());

        
//        Product p1 = pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
//        Product p2 = pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
//        Product p3 = pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//        Product p4 = pm.createProduct(105, "Cookie", BigDecimal.valueOf(3.99), Rating.TWO_STAR, LocalDate.now());
//        Product p5 = p3.applyRating(Rating.THREE_STAR);
//        Product p8 = p4.applyRating(Rating.THREE_STAR);
//        Product p9 = p1.applyRating(Rating.THREE_STAR);
//        System.out.println(p1);
//        System.out.println(p2);
//        System.out.println(p3);
//        System.out.println(p4);
//        System.out.println(p5);
//        System.out.println(p8);
//        System.out.println(p9);
//        
//        Product p6 = pm.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
//        Product p7 = pm.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//        
//        System.out.println(p3.getBestBefore());
//        System.out.println(p1.getBestBefore());
    }
    
}
