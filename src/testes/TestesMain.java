/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
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
        metodosArrays();
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
}


