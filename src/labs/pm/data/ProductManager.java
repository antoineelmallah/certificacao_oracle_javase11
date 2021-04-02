/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labs.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author antoi
 */
public class ProductManager {
    
    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());
    
    private ResourceBundle config = ResourceBundle.getBundle("labs.pm.data.config");
    
    private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
    
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
        try {
            return reviewProduct(findProduct(id), rating, comments);
        } catch (ProductManagerException ex) {
            logger.log(Level.INFO, ex.getMessage(), ex);
        }
        return null;
    }
    
    public Product reviewProduct(Product product, Rating rating, String comments) {
        List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));
        
        /*
        int sum = 0;
        for (Review review: reviews) {
            sum += review.getRating().ordinal();
        }
        */

        Double average = reviews.stream()
            .mapToInt(r -> r.getRating().ordinal())
            .average()
            .orElse(0);
        
        product = product.applyRating(Rateable.convert((int) Math.round(average)));
        products.put(product, reviews);
        
        return product;
    }

    public Product findProduct(int id) throws ProductManagerException {
/*
        for (Product product: products.keySet()) {
            if (product.getId() == id)
                return product;
        }
        return null;
*/
        return products.keySet().stream()
            .filter(p -> p.getId() == id)
            .findFirst()
            //.orElseGet(() -> null)
            //.get()
            .orElseThrow(() -> new ProductManagerException("Product with id "+id+" not found."));
    }
    
    public void printProductReport(int id) {
        try {
            Product product = findProduct(id);            
            printProductReport(product);
        } catch (ProductManagerException ex) {
            logger.log(Level.INFO, ex.getMessage(), ex);
        }
    }
    
    public void printProductReport(Product product) {
        
        List<Review> reviews = products.get(product);
        
        //Collections.sort(reviews);
        
        StringBuilder txt = new StringBuilder();
        txt.append(this.formatter.formatProduct(product));
        
        txt.append('\n');
        
        /*
        for (Review review : reviews) {
            if (review == null) {
                break;
            }   
            txt.append(this.formatter.formatReview(review));
        
            txt.append('\n');
        }
*/
        
        if (reviews.isEmpty()) {
            txt.append(this.formatter.getText("no.reviews"));
            txt.append('\n');
        } else {
            txt.append(
                reviews.stream()
                    .sorted()
                    .map(r -> formatter.formatReview(r) + '\n')
                    .collect(Collectors.joining()));
        }
                
        System.out.println(txt);
    }
    
    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
    //    List<Product> prods = new ArrayList<>(this.products.keySet());
    //    prods.sort(sorter);
        StringBuilder sb = new StringBuilder();
    /*
        for (Product p: prods) {
            sb.append(formatter.formatProduct(p));
            sb.append('\n');
        }
    */
    
        this.products.keySet().stream()
            .filter(filter)
            .sorted(sorter)
            .forEach(p -> sb.append(formatter.formatProduct(p)).append('\n'));
        
        System.out.println(sb);
    }
    
    public void parseReview(String text) {
        try {
            Object[] values = reviewFormat.parse(text);
            reviewProduct(Integer.parseInt((String)values[0]), Rateable.convert(Integer.parseInt((String)values[1])), (String)values[2]);
        } catch (ParseException | NumberFormatException ex) {
            logger.log(Level.WARNING, "Error parsing text: '"+text+"'");
        }
    }
    
    public void parseProduct(String text) {
        try {
            Object[] values = productFormat.parse(text);
            int id = Integer.parseInt((String)values[1]);
            String name = (String) values[2];
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String)values[3]));
            Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));
            switch ((String)values[0]) {
                case "D":
                    createProduct(id, name, price, rating);
                    break;
                case "F":
                    LocalDate bestBefore = LocalDate.parse((String) values[5]);
                    createProduct(id, name, price, rating, bestBefore);
            }
        } catch (ParseException | NumberFormatException | DateTimeParseException ex) {
            logger.log(Level.WARNING, "Error parsing product '"+text+"'.", ex);
        }
    }
    
    public Map<String, String> getDiscounts() {
        return products.keySet().stream()
            .collect(
                Collectors.groupingBy(
                    product -> product.getRating().toString(), 
                    Collectors.collectingAndThen(
                        Collectors.summingDouble(product -> product.getDiscount().doubleValue()), 
                        discount -> formatter.moneyFormat.format(discount)
                    )
                )
            );
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
