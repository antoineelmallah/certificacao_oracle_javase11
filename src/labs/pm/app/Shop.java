/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labs.pm.app;

import labs.pm.data.ProductManager;

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
        pm.printProductReport(101);
        pm.printProductReport(102);
    }
    
}
