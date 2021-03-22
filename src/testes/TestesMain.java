/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import labs.pm.data.Drink;
import labs.pm.data.Food;
import labs.pm.data.Product;
import labs.pm.data.Rating;

/**
 *
 * @author antoi
 */
public class TestesMain {
    
    public static void main(String[] args) {
        //arrayDeclaration();
        //arraysMultidimensionais();
        //metodosArrays();
        //matrizLoopExemplo();
        //breakContinueLabelLoop();
        //immutableArrayList();
        //mutableArrayList();
        //collectionRemoveIf();
        //collectionsMethods();
        //printInnerClass();
        //printLocalInnerClass("A local inner class");
        //printAnonymousInnerClass();
        //printAnonymousInnerClass2();
        //printLambdaFunction();
        //testPredicate();
        //streamsTest();
        //flatMapTest();
        //testIntermediateStreamOperators();
        testShortCircuitTerminalOperator();
    }
    
    private static void arrayDeclaration() {
        int[] array1 = new int[3];
        int array2[] = new int[3];
        Product[] produtos = new Product[] { 
            new Food(1, "batata frita", BigDecimal.valueOf(5), Rating.FIVE_STAR, LocalDate.now()), 
            new Drink(2, "milk shake", BigDecimal.valueOf(10), Rating.FIVE_STAR), 
        };
        Drink[] drinks = {
            new Drink(3, "Coca", BigDecimal.valueOf(3), Rating.NOT_RATED),
        };
        
        array1[0] = 1;
        array2[2] = 2;
        
        System.out.println("primeira posicao array1: "+array1[0]+" última posicao array2: "+array2[2]);
        System.out.println("tamanho: array1: "+array1.length+" array2: "+array2.length);
    }
    
    private static void arraysMultidimensionais() {
        int[][] matrix = new int[2][3];
        int[][] matrix2 = {{4,1},{2,0,5}}; // Permitido arrays com tamanhos diferentes
        //matrix2[0][2] = 6;
        
        imprimirArrayBidimensional("matrix", matrix);
        imprimirArrayBidimensional("matrix2", matrix2);
    }
    
    private static void imprimirArrayBidimensional(String descricao, int[][] matrix) {
        for (int linha = 0; linha < matrix.length; linha++) {
            for (int coluna = 0; coluna < matrix[linha].length; coluna++) {
                System.out.println(descricao+"["+linha+", "+coluna+"] = "+matrix[linha][coluna]);
            }
        }    
    }
    
    private static void imprimirArray(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (String item: array) {
            sb.append(item);
            sb.append(',');
        }
        System.out.println(sb);
    }
    
    private static void metodosArrays() {
        String[] array1 = {"Marcos", "Rita", "Alfredo", "Zuleica"};
        String[] array2 = {"Alfredo", "Marcos", "Rita", "Zuleica"};
        System.out.println("São iguais: "+Arrays.equals(array1, array2));
        Arrays.sort(array1);
//        imprimirArray(array1);
        System.out.println("São iguais: "+Arrays.equals(array1, array2));
        
        Arrays.sort(array2, compararPorTamanho());
        
        imprimirArray(array2);
        
        Arrays.fill(array2, 2, 4, "aaaaaaa");
        
        imprimirArray(array2);
    }
    
    private static Comparator<String> compararPorTamanho() {
        return (String o1, String o2) -> {
            if (o1.length() > o2.length()) return 1;
            if (o2.length() > o1.length()) return -1;
            return 0;
        };
    }
    
    private static void matrizLoopExemplo() {
        int[][] matriz = { { 1,2,3 }, { 4,5,6 }, { 7,8,9 } };

        System.out.println("============================================");        
        System.out.println("Elementos da matriz com i == j:");
        System.out.println("============================================");

        for (int i = 0, j = 0; i < matriz.length && j < matriz[0].length; i++, j++) {
            System.out.println("matriz["+i+", "+j+"] = "+matriz[i][j]);
        }
        
        System.out.println("============================================");        
        System.out.println("Elementos da matriz com i + j == 2:");
        System.out.println("============================================");

        for (int i = 0, j = matriz[0].length - 1; i < matriz.length && j >= 0; i++, j--) {
            System.out.println("matriz["+i+", "+j+"] = "+matriz[i][j]+" ( i + j = "+(i+j)+" )");
        }

        System.out.println("============================================");
        System.out.println("Matriz inteira:");
        System.out.println("============================================");
        
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                System.out.println("matriz["+i+", "+j+"] = "+matriz[i][j]);
            }
        }
        
        System.out.println("============================================");
        System.out.println("Valores da matriz (foreach):");
        System.out.println("============================================");

        StringBuilder sb = new StringBuilder("Valores: ");
        
        for (int[] linha: matriz) {
            for (int valor: linha) {
                sb.append(valor).append(",");
            }
        }
        System.out.println(sb.deleteCharAt(sb.lastIndexOf(",")).append('.'));
    }
    
    private static void breakContinueLabelLoop() {
        char[][] letras = {
            {'a', 'b', 'c', 'd', 'e'}, 
            {'f', 'g', 'h', 'i', 'j'},
            {'k', 'l', 'm', 'n', 'o'},
            {'p', 'q', 'r', 's', 't'},
            {'u', 'v', 'w', 'x', 'y'},
        };
        umLabel: // O bloco pode ser colocado no inicio de um bloco de loop
        for (char[] linha: letras) {
            for (char valor: linha) {
                switch (valor) {
                    case 'c': continue;
                    case 'h': continue umLabel;
                    case 'l': break;
                    case 'q': break umLabel;
                    default: System.out.print(valor+",");
                }
            }
            System.out.println("");
        }
    }
    
    private static void immutableArrayList() {
        List<String> immutable = List.of("a", "e", "i", "o", "u");
        immutable.add("w");// ERRO! Immutable list! 
    }

    private static void mutableArrayList() {
        List<String> mutable = Arrays.asList("a", "e", "i", "o", "u");
        mutable.add("y"); // ERRO! Fixed-sized list!
    }
    
    private static void collectionRemoveIf() {
        Collection<Product> c = new ArrayList<>();
        c.add(new Drink(0, "Coca", BigDecimal.valueOf(5.0), Rating.NOT_RATED));
        c.add(new Food(1, "Hot-dog", BigDecimal.valueOf(12.5), Rating.NOT_RATED, LocalDate.now()));
        c.add(new Drink(2, "Martini", BigDecimal.valueOf(9.9), Rating.NOT_RATED));
        c.add(new Drink(3, "Wiskey", BigDecimal.valueOf(21.9), Rating.NOT_RATED));
        c.add(new Drink(4, "Water", BigDecimal.valueOf(1.9), Rating.NOT_RATED));

        c.removeIf((t) -> t.getPrice().compareTo(BigDecimal.TEN) < 0);
/*      
        c.removeIf(new Predicate<Product>() {
            @Override
            public boolean test(Product t) {
               return t.getPrice().compareTo(BigDecimal.TEN) < 0;
            }
        });
*/
        System.out.println(c);
    }
    
    private static void collectionsMethods() {
        List<Product> products = new ArrayList<>();
        products.add(new Drink(0, "Coca", BigDecimal.valueOf(5.0), Rating.NOT_RATED));
        products.add(new Food(1, "Hot-dog", BigDecimal.valueOf(12.5), Rating.NOT_RATED, LocalDate.now()));
        products.add(new Drink(2, "Martini", BigDecimal.valueOf(9.9), Rating.NOT_RATED));
        products.add(new Drink(3, "Wiskey", BigDecimal.valueOf(21.9), Rating.NOT_RATED));
        products.add(new Drink(4, "Water", BigDecimal.valueOf(1.9), Rating.NOT_RATED));
        
        Collections.sort(products, (p1,p2) -> p1.getPrice().compareTo(p2.getPrice()));
        System.out.println("Sort: " + products);
        
        Collections.reverse(products);
        System.out.println("Reverse: " + products);
       
        Collections.shuffle(products);
        System.out.println("Shuffle: " + products);
        
        Collections.fill(products, new Food(1, "Bacon", BigDecimal.ONE, Rating.NOT_RATED, LocalDate.now()));
        System.out.println("Fill: " + products);
        
    }
    
    private static void printInnerClass() {
        System.out.println("static inner class: " + new StaticInnerClass("Test"));
        //System.out.println("member inner class: " + new MemberInnerClass("Name")); // Can only be instantiated on an instance of outer class. Not from static method.
    }
    
    private final String finalInstanceVariable = "Final instance variable";
       
    private static void printLocalInnerClass(String testMethod) {
        String localInnerVariable = "Inside of method";
        class LocalInnerClass {
            private String teste;
            private void setTest(String teste) {
            //    testMethod = "a" + teste; // Local inner classes can access parameters only if they are final or effectively final
                this.teste = testMethod;
            }
            @Override
            public String toString() {
                setTest("bbb");
                return teste + " " 
                    + localInnerVariable + " " 
                    + staticVariable + " " 
                //    + instanceVariable // Can´t access instance variables from outer class.
                    ;
            }
        }
        System.out.println("Local inner class: " + new LocalInnerClass());
    }
    
    private static String staticVariable = "I'm a static variable!";
    private String instanceVariable = "I'm a instance variable";
    
    private static class StaticInnerClass {
        
        private String description;
        
        private StaticInnerClass(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description + " " 
                + staticVariable + " " // Static inner classes CAN SEE outer classe's static variables. Even private ones.
//                + instanceVariable // Static inner classes CAN'T SEE outer classe's instance variables
                ;
        }
        
    }
    
    private class MemberInnerClass {
        
        private String name;
        
        public MemberInnerClass(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " " + staticVariable + " " + instanceVariable; // Inner member classes CAN SEE outer classes static and instance variables. Even private ones. 
        }
        
    }
    
    private static void printAnonymousInnerClass() {
        System.out.println(new OneInterface() {
            @Override
            public String getNameToPrint() {
                return "Name to print!";
            }
        }.getNameToPrint());
    }
    
    private static void printAnonymousInnerClass2() {
        System.out.println(new StaticInnerClass("An string") {
            @Override
            public String toString() {
                return super.toString() + " OVERRIDING TO_STRING!!!!";
            }
        });
    }
    
    private static void printLambdaFunction() {
        List<String> strings = Arrays.asList("Caio", "Gertrudes", "Antônia", "Marcela", "Ana", "Claudia");
        
        // Declaring lambda on a variable
        Comparator<String> comp1 = (s1, s2) -> s1.charAt(0) - s2.charAt(0);
        Collections.sort(strings, comp1);
        System.out.println(strings);
        
        // Declaring directally
        Collections.sort(strings, (s1, s2) -> s1.length() - s2.length());
        System.out.println(strings);
        
        // Lambda without parameter
        OneInterface lambda = () -> "A name!";
        System.out.println(lambda.getNameToPrint());
        
        // Lambda with just one parameter dont need parenthesis
        OneInterfaceWithJustOneParameter lambda2 = n -> "My name: " + n;
        System.out.println(lambda2.printYourName("Antoine"));
        
        // If you need any modifier like final, you will have to inform the type (or 'var') and use parenthesis
        OneInterfaceWithJustOneParameter lambda3 = (final String n) -> "My name: " + n;
        System.out.println(lambda3.printYourName("Antoine final"));
        
        OneInterfaceWithJustOneParameter lambda4 = (final var n) -> "My name: " + n;
        System.out.println(lambda4.printYourName("Antoine final with var"));
        
        TextFilter filter = new TextFilter();
        List<String> mutableNotFixedLengthList = new ArrayList<>(strings);
        
        // You could use :: to reference a instance method or static method if it is SEMANTICALLY IDENTICAL to the method that lambda expression is implementing.
        // You could do that even if the class or interface didn't implements the required interface. It works just because of the params and return of the method.
        mutableNotFixedLengthList.removeIf(s -> TextFilter.removeA(s));
        mutableNotFixedLengthList.removeIf(TextFilter::removeA);
        
        Collections.sort(mutableNotFixedLengthList, (s1, s2) -> filter.sortText(s1, s2));
        Collections.sort(strings, filter::sortText);
        
        // compareToIgnoreCase is a instance method of String. This code works!
        Collections.sort(mutableNotFixedLengthList, String::compareToIgnoreCase);
        System.out.println(mutableNotFixedLengthList);
    }
    
    private static void testPredicate() {
        List<Product> menu = new ArrayList<>();
        menu.add(new Food(1,"Cake", BigDecimal.valueOf(1.99), Rating.NOT_RATED, LocalDate.now()));
        menu.add(new Food(2,"Cookie", BigDecimal.valueOf(2.99), Rating.NOT_RATED, LocalDate.now()));
        menu.add(new Drink(3,"Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED));
        menu.add(new Drink(4,"Coffee", BigDecimal.valueOf(1.99), Rating.NOT_RATED));
        
        Predicate<Product> foodFilter = p -> p instanceof Food;
        Predicate<Product> priceFilter = p -> p.getPrice().compareTo(BigDecimal.valueOf(2)) < 0;
        
        menu.removeIf(foodFilter.negate().or(priceFilter));
        menu.removeIf(Predicate.isEqual(new Food(5, "Cake", BigDecimal.valueOf(1.99), Rating.NOT_RATED, LocalDate.now())));
        
        System.out.println(menu);
    }
    
    @FunctionalInterface
    private interface OneInterface {
        String getNameToPrint();
    }
    
    @FunctionalInterface
    private interface OneInterfaceWithJustOneParameter {
        String printYourName(String name);
    }
    
    public static class TextFilter {
        
        public static boolean removeA(String s) {
            return s.equals("remove A");
        }
        
        public int sortText(String s1, String s2) {
            return s1.compareTo(s2);
        }
    }
    
    private static void streamsTest() {
        // IntStream, DoubleStream and LongStream interfaces prevent excessive boxing and unboxing operations to improve performance.
        int sum = IntStream.generate(() -> (int) (Math.random() * 10)).takeWhile(n -> n != 3).sum();
        System.out.println("sum: "+sum);
        
        // Creating stream from varargs
        Stream.of("Antoine", "El", "Mallah").forEach(i -> System.out.println("i: "+i));
        
        Product[] array = {
            new Food(1, "Hot-dog", BigDecimal.valueOf(1.99), Rating.NOT_RATED, LocalDate.now()),
            new Food(2, "Meat", BigDecimal.valueOf(3.99), Rating.NOT_RATED, LocalDate.now()),
            new Drink(3, "Juice", BigDecimal.valueOf(0.99), Rating.NOT_RATED),
            new Drink(4, "Water", BigDecimal.valueOf(.5), Rating.NOT_RATED),
        };
        List<Product> list = Arrays.asList(array);
        
        // We can get streams from lists
        Double sumDouble = list.stream().parallel().mapToDouble(p -> p.getPrice().doubleValue()).sum();
        System.out.println("sum: "+sumDouble);
        
        // We can get streams from arrays
        Arrays.stream(array).filter(p -> p instanceof Food).forEach(p -> System.out.println("Prod: "+p.getName()+". discount: "+p.getDiscount()));
        
        list.stream()
            .filter(p -> p.getDiscount().equals(BigDecimal.ZERO)) // Predicate<T>
            .peek(p -> p.applyRating(1))                          // Consumer<T>
            .map(p -> p.getPrice())                               // Function<T,R>
            .forEach(price -> price.multiply(BigDecimal.TEN));    // Consumer<T>
    }
    
    private static void flatMapTest() {
        List<Outer> fathers = Arrays.asList(
            new Outer("Pai 1").addSon(new Inner("Filho 1", 1)).addSon(new Inner("Filho 2", 2)).addSon(new Inner("Filho 3", 3)),
            new Outer("Pai 2").addSon(new Inner("Filho 4", 4)).addSon(new Inner("Filho 5", 5)).addSon(new Inner("Filho 6", 6)),
            new Outer("Pai 3").addSon(new Inner("Filho 7", 7)).addSon(new Inner("Filho 8", 8)).addSon(new Inner("Filho 9", 9)),
            new Outer("Pai 4").addSon(new Inner("Filho 10", 10)).addSon(new Inner("Filho 11", 11)).addSon(new Inner("Filho 12", 12)),
            new Outer("Pai 5").addSon(new Inner("Filho 13", 13)).addSon(new Inner("Filho 14", 12)).addSon(new Inner("Filho 15", 15))
        );
                
        fathers.stream()
            .flatMap(f -> f.getSons().stream())
            .forEach(s -> System.out.println(s));
    }
    
    private static class Outer {
        
        private String name;
        
        private List<Inner> sons;

        public Outer(String name) {
            this.name = name;
            this.sons = new ArrayList<>();
        }
        
        public Outer addSon(Inner son) {
            this.sons.add(son);
            return this;
        }

        public String getName() {
            return name;
        }

        public List<Inner> getSons() {
            return new ArrayList(sons);
        }

        @Override
        public String toString() {
            return "Father: "+this.name;
        }
        
    }
    
    private static class Inner {
        
        private String name;
        
        private int num;

        public Inner(String name, int num) {
            this.name = name;
            this.num = num;
        }

        public String getName() {
            return name;
        }

        public int getNum() {
            return num;
        }

        @Override
        public String toString() {
            return "Son: "+this.name + " num: "+ num;
        }
        
    }
    
    private static void testIntermediateStreamOperators() {
        List<String> strings = Arrays.asList("A", "B", "C", "D", "B", "D");
        
        System.out.println("Primeira:");
        strings.stream()
            .distinct()
        //    .peek(System.out::println)
            .sorted()
        //    .peek(System.out::println)
            .skip(2)
        //    .peek(System.out::println)
            .map(s -> s.toLowerCase())
            .forEach(System.out::println);
        
        System.out.println("Segunda:");
        strings.stream()
            .takeWhile(s -> !s.equals("D"))
            .dropWhile(s -> !s.equals("C"))
            .limit(2)
            .forEach(System.out::println);
    }
    
    private static void testShortCircuitTerminalOperator() {
        List<String> strings = Arrays.asList("A", "B", "C", "D", "B", "D");
    
        boolean a = strings.stream().allMatch(s -> s.equals("A"));
        boolean b = strings.stream().anyMatch(s -> s.equals("A"));
        boolean c = strings.stream().noneMatch(s -> s.equals("A"));
        Optional<String> any = strings.stream().findAny();
        Optional<String> first = strings.stream().findFirst();
        
        System.out.println("a: "+a);
        System.out.println("b: "+b);
        System.out.println("c: "+c);
        System.out.println("any: "+any.orElse("null"));
        System.out.println("first: "+first.orElse("null"));
    }
}




