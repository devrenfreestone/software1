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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author Dev
 */
public class ModifyProductScreenController implements Initializable {
    
    Stage stage;
    Parent scene;
    Inventory inv;
    Product product;

    @FXML
    private TextField modProdID;
    @FXML
    private TextField modProdName;
    @FXML
    private TextField modProdInv;
    @FXML
    private TextField modProdPrice;
    @FXML
    private TextField modProdMax;
    @FXML
    private TextField modProdMin;
    @FXML
    private Button modProductSave;
    @FXML
    private Button modProdCancel;
    @FXML
    private Button modProdSearch;
    @FXML
    private TextField modProdSearchCriteria;
    @FXML
    private TableView<Part> modProdTable1;
    @FXML
    private Button modProdAdd;
    @FXML
    private TableView<Part> modProdTable2;
    @FXML
    private Button modProdDelete;
    
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();


    ModifyProductScreenController(Inventory inv,Product product) {
        this.inv = inv;
        this.product = product;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setData();
        generatePartsTable();
        generatePartsTable2();
    } 

    private void generatePartsTable(){
        partInventory.setAll(inv.getAllParts());
        modProdTable1.getColumns();
        modProdTable1.setItems(partInventory);
        modProdTable1.refresh();
    }

    private void generatePartsTable2(){
        associatedParts.setAll(product.getAllAssociatedParts(product));
        modProdTable2.getColumns();
        modProdTable2.setItems(associatedParts);
        modProdTable2.refresh();
    }
    
    private void setData(){
            modProdID.setText(Integer.toString(product.getId()));
            modProdName.setText(product.getName());
            modProdInv.setText(Integer.toString(product.getStock()));
            modProdPrice.setText(Double.toString(product.getPrice()));
            modProdMax.setText(Integer.toString(product.getMax()));
            modProdMin.setText(Integer.toString(product.getMin()));
            
            
            
    }
    
    @FXML
    private void modProdSave(MouseEvent event) throws IOException {
        if (modProdID.getText().length() == 0){
            alertMessage("ID cannot be blank");
            return;
        }
        if(modProdName.getText().trim().length() == 0){
            alertMessage("Name cannot be blank.");
            return;
        }
        if(modProdPrice.getText().length() == 0) {
            alertMessage("Price cannot be blank.");
            return;
        }
        if(!modProdPrice.getText().trim().matches("^(0*[1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
                alertMessage("Price must be a positive number.");
                return;
        }
        if(Double.parseDouble(modProdPrice.getText().trim()) <= 0){
            alertMessage("Price must be greater than zero.");
            return;
        }
        if(Integer.parseInt(modProdMin.getText().trim()) >= Integer.parseInt(modProdMax.getText().trim())){
                alertMessage("Min must be less than max.");
                return;
        }
        if(Integer.parseInt(modProdMax.getText().trim()) <= Integer.parseInt(modProdMin.getText().trim())){
                alertMessage("Max must be greater than min.");
                return;
        }
        if (modProdInv.getText().length() == 0) {
            alertMessage("Inv cannot be blank");
            return;
        }
        if(!modProdInv.getText().trim().matches("[0-9]*")){
                alertMessage("Inv must be a positive integer.");
                return;
        }
        if(!modProdMin.getText().trim().matches("[0-9]*")){
                alertMessage("Inv must be a positive integer.");
                return;
        }
        if(Integer.parseInt(modProdInv.getText().trim()) > Integer.parseInt(modProdMax.getText().trim()) || Integer.parseInt(modProdInv.getText().trim()) < Integer.parseInt(modProdMin.getText().trim())){
            alertMessage("Part Inv must be between min and max values.");
            return;
        }
        if (modProdMin.getText().length() == 0) {
            
            alertMessage("Min cannot be blank.");
            return;
        }
        if(!modProdMin.getText().trim().matches("[0-9]*")){
                alertMessage("Min must be an integer.");
                return;
        }
        if (modProdMax.getText().length() == 0) {
            alertMessage("Max cannot be blank.");
            return;
        }
        if(!modProdMax.getText().trim().matches("[0-9]*")){
                alertMessage("Max must be an integer.");
                return;
        }
        
        
        Product newProduct = new Product(Integer.parseInt(modProdID.getText().trim()),modProdName.getText().trim(),Double.parseDouble(modProdPrice.getText().trim()),Integer.parseInt(modProdInv.getText().trim()),Integer.parseInt(modProdMin.getText().trim()),Integer.parseInt(modProdMax.getText().trim()));
        
        int counter = 0;
        int price = 0;
        for(Part p : associatedParts){
            newProduct.addAssociatedPart(p);
            counter ++;
            price += p.getPrice();
        }     
        if(counter == 0){
            alertMessage("Product must have at least one associated part");
            return;
        }
        if(price > newProduct.getPrice()) {
            alertMessage("Product price must be greater than the sum of the price of the associated parts.");
            return;
        }
        if(confirmMessage("Are you sure you want to modify this product?") == true){
            
        } else {
            return;
        }
        inv.updateProduct(newProduct);
        
        mainScreen(event);
    }

    @FXML
    private void modProdCancel(MouseEvent event) throws IOException {
        modProdID.clear();
        modProdName.clear();
        modProdInv.clear();
        modProdPrice.clear();
        modProdMax.clear();
        modProdMin.clear();
        mainScreen(event);
    }

    @FXML
    private void modProdSearch(MouseEvent event) {
        if (!modProdSearchCriteria.getText().trim().isEmpty()) {
                partInventorySearch.clear();
                for (Part p : inv.getAllParts()) {
                        if (p.getName().contains(modProdSearchCriteria.getText().trim())) {
                                partInventorySearch.add(p);
                        }
                }
                modProdTable1.setItems(partInventorySearch);
                modProdTable1.refresh();
        }
    }
    
    @FXML
    void clearText(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (modProdSearchCriteria == field) {
                if (partInventory.size() !=0) {
                        modProdTable1.setItems(partInventory);
                }
        }
    }

    @FXML
    private void modProdAdd(MouseEvent event) {
        Part partToAdd = modProdTable1.getSelectionModel().getSelectedItem();
        associatedParts.add(partToAdd);
        modProdTable2.setItems(associatedParts);
        modProdTable2.refresh();
    }

    @FXML
    private void modProdDelete(MouseEvent event) {
        if(confirmMessage("Are you sure you want to delete this part?") == true){
            
        } else {
            return;
        }
        Part partToRemove = modProdTable2.getSelectionModel().getSelectedItem();
        associatedParts.remove(partToRemove);
        modProdTable2.setItems(associatedParts);
        modProdTable2.refresh();
    }
    
    private void mainScreen(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));

            MainScreenController controller = new MainScreenController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {

        }
    }

    @FXML
        private void alertMessage(String field){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setResult(ButtonType.CANCEL);
        alert.setTitle("Unable to add product");
        alert.setHeaderText(field);
        alert.setContentText("All fields are required to add product.");
        alert.showAndWait();
     }
    @FXML
        private boolean confirmMessage(String field){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(field);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
     }
    
}
