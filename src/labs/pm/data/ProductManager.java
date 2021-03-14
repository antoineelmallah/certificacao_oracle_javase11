/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labs.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author antoi
 */
public class ProductManager {
    
    private Locale locale;
    private ResourceBundle resources;
    private DateTimeFormatter dateFormat;
    private NumberFormat moneyFormat;
    
    private Product product;
    
    private Review review;

    public ProductManager(Locale locale) {
        this.locale = locale;
        this.resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
        this.dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
        this.moneyFormat = NumberFormat.getCurrencyInstance(locale);
    }
    
    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        this.product = new Food(id, name, price, rating, bestBefore);
        return this.product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        this.product = new Drink(id, name, price, rating);
        return this.product;
    }
    
    public Product reviewProduct(Product product, Rating rating, String comments) {
        review = new Review(rating, comments);
        this.product = product.applyRating(rating);
        return this.product;
    }

    public void printProductReport() {
        StringBuilder txt = new StringBuilder();
        txt.append(MessageFormat.format(resources.getString("products"), 
                product.getName(),
                moneyFormat.format(product.getPrice()),
                product.getRating(),
                dateFormat.format(product.getBestBefore())));
        
        txt.append('\n');
        
        if (review != null) {
            txt.append(MessageFormat.format(resources.getString("review"), 
                    review.getRating(),
                    review.getComments()));
        } else {
            txt.append(resources.getString("no.reviews"));
        }
        
        txt.append('\n');
        
        System.out.println(txt);
    }
}
