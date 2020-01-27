/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author Dev
 */
public class MainScreenController implements Initializable {

    Stage stage;
    Parent scene;
    Inventory inv;
    public static Part modPart = null;
    public static Product modProd = null;


    @FXML
    private Button mainPartsSearch;
    @FXML
    private Button mainPartsAdd;
    @FXML
    private Button mainPartsModify;
    @FXML
    private Button mainPartsDelete;
    @FXML
    private Button mainExit;
    @FXML
    private TextField mainPartsSearchCriteria;
    @FXML
    private TableView<Part> mainPartsTable;
    @FXML
    private Button mainProdSearch;
    @FXML
    private TextField mainProdSearchCriteria;
    @FXML
    private TableView<Product> mainProdTable;
    @FXML
    private Button mainProdAdd;
    @FXML
    private Button mainProdModify;
    @FXML
    private Button mainProdDelete;
    @FXML
    private TableColumn<Part, Integer> mainPartTablePartID;
    @FXML
    private TableColumn<Part, String> mainPartTablePartName;
    @FXML
    private TableColumn<Part, Integer> mainPartTableInvLevel;
    @FXML
    private TableColumn<Part, Double> mainPartTablePriceCost;
    @FXML
    private TableColumn<Product, Integer> MainProdTableProdID;
    @FXML
    private TableColumn<Product, String> mainProdTableProdName;
    @FXML
    private TableColumn<Product, Integer> mainProdTableInvLevel;
    @FXML
    private TableColumn<Product, Double> mainProdTablePriceCost;
    
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Product> prodInventory = FXCollections.observableArrayList();
    private ObservableList<Product> prodInventorySearch = FXCollections.observableArrayList();
    
    public MainScreenController(Inventory inv){
        this.inv = inv;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePartsTable();
        generateProdTable();
    }  
    
    private void generatePartsTable(){
        partInventory.setAll(inv.getAllParts());
        System.out.println(inv);
        mainPartsTable.getColumns();
        mainPartsTable.setItems(partInventory);
        mainPartsTable.refresh();
    }
    
    private void generateProdTable(){
        prodInventory.setAll(inv.getAllProducts());
        mainProdTable.getColumns();
        mainProdTable.setItems(prodInventory);
        mainProdTable.refresh();
    }



    @FXML
    private void mainPartsAdd(MouseEvent event) throws IOException { 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddPartScreen.fxml"));

            AddPartScreenController controller = new AddPartScreenController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
    }

    @FXML
    private void mainPartsModify(MouseEvent event) throws IOException {
        modPart = mainPartsTable.getSelectionModel().getSelectedItem();
        if (modPart == null){
            alertMessage("part");
        } else {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPartScreen.fxml"));

            ModifyPartScreenController controller = new ModifyPartScreenController(inv,modPart);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML
    private void mainPartsDelete(MouseEvent event) {
        if(confirmMessage("Are you sure you want to delete this part?") == true){
            
        } else {
            return;
        }
        Part deletePart = mainPartsTable.getSelectionModel().getSelectedItem();
        inv.deletePart(deletePart);
        partInventory.remove(deletePart);
        mainPartsTable.refresh();
    }

        @FXML
    private void mainPartsSearch(MouseEvent event) {
        if (!mainPartsSearchCriteria.getText().trim().isEmpty()) {
                partInventorySearch.clear();
                for (Part p : inv.getAllParts()) {
                        if (p.getName().contains(mainPartsSearchCriteria.getText().trim())) {
                                partInventorySearch.add(p);
                        }
                }
                mainPartsTable.setItems(partInventorySearch);
                mainPartsTable.refresh();
        }
    }
    
    @FXML
    private void mainProdSearch(MouseEvent event) {
            if (!mainProdSearchCriteria.getText().trim().isEmpty()) {
                    prodInventorySearch.clear();
                    for (Product p : inv.getAllProducts()) {
                            if (p.getName().contains(mainProdSearchCriteria.getText().trim())) {
                                    prodInventorySearch.add(p);
                            }
                    }
                    mainProdTable.setItems(prodInventorySearch);
                    mainProdTable.refresh();
            }
    }
    
    @FXML
    void clearText(MouseEvent event) {
            Object source = event.getSource();
            TextField field = (TextField) source;
            field.setText("");
            if (mainPartsSearchCriteria == field) {
                    if (partInventory.size() !=0) {
                            mainPartsTable.setItems(partInventory);
                    }
            }
            if (mainProdSearchCriteria == field) {
                    if (prodInventory.size() !=0) {
                            mainProdTable.setItems(prodInventory);
                    }
            }
    }

    @FXML
    private void mainProductsAdd(MouseEvent event) throws IOException {
        
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddProductScreen.fxml"));
         AddProductScreenController controller = new AddProductScreenController(inv);
         loader.setController(controller);
         Parent root = loader.load();
         Scene scene = new Scene(root);
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         stage.setScene(scene);
         stage.setResizable(false);
         stage.show();
    }

    @FXML
    private void mainProductsModify(MouseEvent event) throws IOException {
        modProd = mainProdTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProductScreen.fxml"));
         ModifyProductScreenController controller = new ModifyProductScreenController(inv,modProd);
         loader.setController(controller);
         Parent root = loader.load();
         Scene scene = new Scene(root);
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         stage.setScene(scene);
         stage.setResizable(false);
         stage.show();
    }

    @FXML
    private void mainProductsDelete(MouseEvent event) {
        if(confirmMessage("Are you sure you want to delete this product?") == true){
            
        } else {
            return;
        }        
        Product deleteProd = mainProdTable.getSelectionModel().getSelectedItem();
        inv.deleteProduct(deleteProd);
        prodInventory.remove(deleteProd);
        mainProdTable.refresh();        
    }
    
    @FXML
    private void alertMessage(String field){
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Unable to Modify " + field);
           alert.setHeaderText("Selection required to modify " + field);
           alert.setContentText("Choose an option from the " + field + " table.");
           alert.showAndWait();
    }
    @FXML
    private boolean confirmMessage(String field){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setResult(ButtonType.CANCEL);
        alert.setTitle("Confirm");
        alert.setHeaderText(field);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
     }

    @FXML
    private void mainExit(MouseEvent event) {
        System.exit(0);
    }
    
}
