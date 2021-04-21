/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labs.pm.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    
    private Path reportsFolder = Path.of(config.getString("reports.folder"));
    private Path dataFolder = Path.of(config.getString("data.folder"));
    private Path tempFolder = Path.of(config.getString("temp.folder"));
    
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
        loadAllData();
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
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
    public void printProductReport(Product product) throws IOException {
        
        List<Review> reviews = products.get(product);
        
        //Collections.sort(reviews);
        
        Path productFile = reportsFolder.resolve(MessageFormat.format(config.getString("report.file"), product.getId()));

        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(productFile, StandardOpenOption.CREATE), "UTF-8"))) {
            
            out.append(this.formatter.formatProduct(product));

            out.append(System.lineSeparator());

            if (reviews.isEmpty()) {
                out.append(this.formatter.getText("no.reviews"));
                out.append(System.lineSeparator());
            } else {
                out.append(
                    reviews.stream()
                        .sorted()
                        .map(r -> formatter.formatReview(r) + System.lineSeparator())
                        .collect(Collectors.joining()));
            }
            
        }
                
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
    
    private Review parseReview(String text) {
        Review review = null;
        try {
            Object[] values = reviewFormat.parse(text);
            review = new Review(Rateable.convert(Integer.parseInt((String)values[0])), (String)values[1]);
        } catch (ParseException | NumberFormatException ex) {
            logger.log(Level.WARNING, "Error parsing text: '"+text+"'");
        }
        return review;
    }
    
    private Product parseProduct(String text) {
        Product product = null;
        try {
            Object[] values = productFormat.parse(text);
            int id = Integer.parseInt((String)values[1]);
            String name = (String) values[2];
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String)values[3]));
            Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));
            switch ((String)values[0]) {
                case "D":
                    product = new Drink(id, name, price, rating);
                    break;
                case "F":
                    LocalDate bestBefore = LocalDate.parse((String) values[5]);
                    product = new Food(id, name, price, rating, bestBefore);
            }
        } catch (ParseException | NumberFormatException | DateTimeParseException ex) {
            logger.log(Level.WARNING, "Error parsing product '"+text+"'.", ex);
        }
        return product;
    }
    
    private void loadAllData() {
        try {
            products = Files.list(dataFolder)
                .filter(f -> f.toString().contains("product"))
//                    .peek(f -> System.out.println(" +++ "+f))
                .map(f -> loadProduct(f))
                .filter(p -> p != null)
                .collect(Collectors.toMap(product -> product, product -> loadReviews(product)));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error loading data.", ex);
        }
    }
    
    private Product loadProduct(Path file) {
        Product product = null;
        try {
            product = parseProduct(Files.lines(dataFolder.resolve(file), Charset.forName("UTF-8"))
                    .findFirst().orElseThrow());
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Error parsing product.", ex);
        }
        return product;
    }
    
    private List<Review> loadReviews(Product product) {
        List<Review> reviews = null;
        Path file = dataFolder.resolve(MessageFormat.format(config.getString("reviews.data.file"), product.getId()));
        if (Files.notExists(file)) {
            reviews = new ArrayList<>(0);
        } else {
            try {
                reviews = Files.lines(file, Charset.forName("UTF-8"))
                        .map(text -> parseReview(text))
                        .filter(review -> review != null)
                        .collect(Collectors.toList());
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Error loading reviews.", ex);
            }
        }
        return reviews;
    }
    
    private void dumpData() {
        try {
            if (Files.notExists(tempFolder)) {
                Files.createDirectory(tempFolder);
            }
            Path tempFile = tempFolder.resolve(MessageFormat.format(config.getString("temp.file"), Instant.now().getEpochSecond()));
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(tempFile, StandardOpenOption.CREATE))) {
                out.writeObject(products);
                products = new HashMap<>();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error dumping data.", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void restoreData() {
        try {
            Path tempFile = Files.list(tempFolder)
                .filter(f -> f.getFileName().toString().endsWith("tmp"))
                .findFirst()
                .orElseThrow();
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(tempFile, StandardOpenOption.DELETE_ON_CLOSE))) {
                products = (HashMap)in.readObject();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error restoring data.", e);
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
