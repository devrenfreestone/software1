/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InventoryProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.Product;

/**
 *
 * @author Dev
 */
public class InventoryProject extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Inventory inv = new Inventory();
        addTestData(inv);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
        controller.MainScreenController controller = new controller.MainScreenController(inv);
        loader.setController(controller);
        Parent root = loader.load();                
        Scene scene = new Scene(root);        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    void addTestData(Inventory inv){
        //add test parts, both InHouse and Outsourced
        Part part1 = new InHouse(1,"part1",8.50,9,3,15,1000);
        inv.addPart(part1);
        Part part2 = new InHouse(2,"part2",7.25,4,1,25,1001);
        inv.addPart(part2);
        Part part3 = new InHouse(3,"part3",7.75,17,10,250,1002);
        inv.addPart(part3);
        inv.addPart(new InHouse(4,"part4",13.40,10,9,100,1003));
        inv.addPart(new InHouse(5,"part5",19.30,3,1,5,1004));
        Part part6 = new Outsourced(6,"part6",25.10,15,10,20,"MasheenIt");
        inv.addPart(part6);
        Part part7 = new Outsourced(7,"part7",23.50,4,2,6,"Metalworkers Inc.");
        inv.addPart(part7);
        inv.addPart(new Outsourced(8,"part8",29.75,8,5,10,"MasheenIt"));
        inv.addPart(new Outsourced(9,"part9",34.00,19,10,75,"Electric Everything"));
        inv.addPart(new Outsourced(10,"part10",72.25,2,1,3,"Wired"));
        //add test products
        Product prod1 = new Product(1,"prod1",230.00,7,2,10);
        prod1.addAssociatedPart(part1);
        prod1.addAssociatedPart(part3);
        inv.addProduct(prod1);
        Product prod2 = new Product(2,"prod2",325.50,10,5,15);
        prod2.addAssociatedPart(part7);
        inv.addProduct(prod2);
        Product prod3 = new Product(3,"prod3",175.50,4,2,6);
        prod3.addAssociatedPart(part2);
        prod3.addAssociatedPart(part6);
        inv.addProduct(prod3);
        inv.addProduct(new Product(4,"prod4",260.98,10,2,20));
        inv.addProduct(new Product(5,"prod5",245.00,12,8,16));       
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
        
    }
    
}



