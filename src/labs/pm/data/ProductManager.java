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
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 * @author antoi
 */
public class ProductManager {
    
    private ResourceFormatter formatter;
    
    private Map<String, ResourceFormatter> formatters = Map.of(
        "en-GB", new ResourceFormatter(Locale.UK),
        "en-US", new ResourceFormatter(Locale.US),
        "fr-FR", new ResourceFormatter(Locale.FRANCE),
        "ru-RU", new ResourceFormatter(new Locale("ru", "RU")),
        "zh-CN", new ResourceFormatter(Locale.CHINA)
    );
    
    private Map<Product, List<Review>> products = new HashMap<>();

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }
    
    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }
    
    public void changeLocale(String languageTag) {
        this.formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
    } 
    
    public Set<String> getSupportedLocales() {
        return formatters.keySet();
    }
    
    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }
    
    public Product reviewProduct(int id, Rating rating, String comments) {
        Product product = findProduct(id);
        if (product != null) {
            return reviewProduct(product, rating, comments);
        }
        return null;
    }
    
    public Product reviewProduct(Product product, Rating rating, String comments) {
        
        List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));
        
        int sum = 0;
        for (Review review: reviews) {
            sum += review.getRating().ordinal();
        }

        product = product.applyRating(Rateable.convert(Math.round((float)sum/reviews.size())));
        products.put(product, reviews);
        return product;
    }

    public Product findProduct(int id) {
        for (Product product: products.keySet()) {
            if (product.getId() == id)
                return product;
        }
        return null;
    }
    
    public void printProductReport(int id) {
        Product product = findProduct(id);
        if (product != null) {
            printProductReport(product);            
        }
    }
    
    public void printProductReport(Product product) {
        
        List<Review> reviews = products.get(product);
        
        Collections.sort(reviews);
        
        StringBuilder txt = new StringBuilder();
        txt.append(this.formatter.formatProduct(product));
        
        txt.append('\n');
        
        for (Review review : reviews) {
            if (review == null) {
                break;
            }   
            txt.append(this.formatter.formatReview(review));
        
            txt.append('\n');
        }
        
        if (reviews.isEmpty()) {
            txt.append(this.formatter.getText("no.reviews"));
            txt.append('\n');
        }
                
        System.out.println(txt);
    }
    
    public void printProducts(Comparator<Product> sorter) {
        List<Product> prods = new ArrayList<>(this.products.keySet());
        prods.sort(sorter);
        StringBuilder sb = new StringBuilder();
        for (Product p: prods) {
            sb.append(formatter.formatProduct(p));
            sb.append('\n');
        }
        System.out.println(sb);
    }
    
    private static class ResourceFormatter {

        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;

        public ResourceFormatter(Locale locale) {
            this.locale = locale;
            this.resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
            this.dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            this.moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }
        
        private String formatProduct(Product product) {
            return MessageFormat.format(resources.getString("products"), 
                product.getName(),
                moneyFormat.format(product.getPrice()),
                product.getRating(),
                dateFormat.format(product.getBestBefore()));
        }
        
        private String formatReview(Review review) {
            return MessageFormat.format(resources.getString("review"), 
                review.getRating(),
                review.getComments());
        }
        
        private String getText(String key) {
            return resources.getString(key);
        }
    }
}
