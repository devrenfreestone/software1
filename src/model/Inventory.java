/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author Dev
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part part){
        allParts.add(part);
    }
    public static void addProduct(Product product){
        allProducts.add(product);
    }
    
    public static Part lookupPart(int partId){
        int part = allParts.indexOf(partId);
        return allParts.get(part);
    }
    
    public static Product lookupProduct(int productId){
        int product = allProducts.indexOf(productId);
        return allProducts.get(product);
    }
    
    //Needs attention-not sure how to do this one
//    public static ObservableList<Part> lookupPart(String partName){
//        int part = allParts.indexOf(partName);
//        return allParts.get(part);
//     }
    
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
    
    public static void updatePart(Part part){
        for(int i = 0;i<allParts.size();i++){
           if(allParts.get(i).getId() == part.getId()){
               allParts.set(i, part);
           } 
        }
    }
    
    public static void updateProduct(Product product){
        for(int i = 0;i<allProducts.size();i++){
            if(allProducts.get(i).getId() == product.getId()){
                allProducts.set(i, product);
            }
        }
    }
    
    public static void deletePart(Part part) {
        allParts.remove(part);
    }
    
    public static void deleteProduct(Product product) {
        allProducts.remove(product);
    }
    
}
