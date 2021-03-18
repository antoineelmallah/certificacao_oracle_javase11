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
import java.util.function.Predicate;
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
        collectionsMethods();
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
}


