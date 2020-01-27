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
public class AddProductScreenController implements Initializable {
    
    Stage stage;
    Parent scene;
    Inventory inv;
    int idCounter = 0;

    @FXML
    private TextField addProdID;
    @FXML
    private TextField addProdName;
    @FXML
    private TextField addProdInv;
    @FXML
    private TextField addProdPrice;
    @FXML
    private TextField addProdMax;
    @FXML
    private TextField addProdMin;
    @FXML
    private Button addProdSave;
    @FXML
    private Button addProdCancel;
    @FXML
    private Button addProdSearch;
    @FXML
    private TextField addProdSearchCriteria;
    @FXML
    private TableView<Part> addProdTable1;
    @FXML
    private Button addProdAdd;
    @FXML
    private TableView<Part> addProdTable2;
    
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public AddProductScreenController(Inventory inv) {
        this.inv = inv;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //add product ID
        for (Product p : inv.getAllProducts()) {
            if (p.getId() > idCounter){
                idCounter = p.getId();
            }
        } 
        idCounter +=1;
        addProdID.setText(String.valueOf(idCounter));
        generatePartsTable();
    }

    private void generatePartsTable(){
        partInventory.setAll(inv.getAllParts());
        addProdTable1.getColumns();
        addProdTable1.setItems(partInventory);
        addProdTable1.refresh();
    }

    @FXML
    private void addProdSave(MouseEvent event) throws IOException {
        if (addProdID.getText().length() == 0){
            alertMessage("ID cannot be blank");
            return;
        }
        if(addProdName.getText().trim().length() == 0){
            alertMessage("Name cannot be blank.");
            return;
        }
        if(addProdPrice.getText().length() == 0) {
            alertMessage("Price cannot be blank.");
            return;
        }
        if(!addProdPrice.getText().trim().matches("^(0*[1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
                alertMessage("Price must be a positive number.");
                return;
        }
        if(Double.parseDouble(addProdPrice.getText().trim()) <= 0){
            alertMessage("Price must be greater than zero.");
            return;
        }
        if(Integer.parseInt(addProdMin.getText().trim()) >= Integer.parseInt(addProdMax.getText().trim())){
                alertMessage("Min must be less than max.");
                return;
        }
        if(Integer.parseInt(addProdMax.getText().trim()) <= Integer.parseInt(addProdMin.getText().trim())){
                alertMessage("Max must be greater than min.");
                return;
        }
        if (addProdInv.getText().length() == 0) {
            alertMessage("Inv cannot be blank");
            return;
        }
        if(!addProdInv.getText().trim().matches("[0-9]*")){
                alertMessage("Inv must be a positive integer.");
                return;
        }
        if(!addProdMin.getText().trim().matches("[0-9]*")){
                alertMessage("Inv must be a positive integer.");
                return;
        }
        if(Integer.parseInt(addProdInv.getText().trim()) > Integer.parseInt(addProdMax.getText().trim()) || Integer.parseInt(addProdInv.getText().trim()) < Integer.parseInt(addProdMin.getText().trim())){
            alertMessage("Part Inv must be between min and max values.");
            return;
        }
        if (addProdMin.getText().length() == 0) {
            
            alertMessage("Min cannot be blank.");
            return;
        }
        if(!addProdMin.getText().trim().matches("[0-9]*")){
                alertMessage("Min must be an integer.");
                return;
        }
        if (addProdMax.getText().length() == 0) {
            alertMessage("Max cannot be blank.");
            return;
        }
        if(!addProdMax.getText().trim().matches("[0-9]*")){
                alertMessage("Max must be an integer.");
                return;
        }
        
        
        Product newProduct = new Product(Integer.parseInt(addProdID.getText().trim()),addProdName.getText().trim(),Double.parseDouble(addProdPrice.getText().trim()),Integer.parseInt(addProdInv.getText().trim()),Integer.parseInt(addProdMin.getText().trim()),Integer.parseInt(addProdMax.getText().trim()));
        
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
        inv.addProduct(newProduct);
        mainScreen(event);
    }

    @FXML
    private void addProdCancel(MouseEvent event) throws IOException {
            addProdID.clear();
            addProdName.clear();
            addProdInv.clear();
            addProdPrice.clear();
            addProdMax.clear();
            addProdMin.clear();
        mainScreen(event);
    }

    @FXML
    private void addProdSearch(MouseEvent event) {
        if (!addProdSearchCriteria.getText().trim().isEmpty()) {
                partInventorySearch.clear();
                for (Part p : inv.getAllParts()) {
                        if (p.getName().contains(addProdSearchCriteria.getText().trim())) {
                                partInventorySearch.add(p);
                        }
                }
                addProdTable1.setItems(partInventorySearch);
                addProdTable1.refresh();
        }
    }
    @FXML
    void clearText(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (addProdSearchCriteria == field) {
                if (partInventory.size() !=0) {
                        addProdTable1.setItems(partInventory);
                }
        }
    }
    

    @FXML
    private void addProdAdd(MouseEvent event) {
        Part partToAdd = addProdTable1.getSelectionModel().getSelectedItem();
        associatedParts.add(partToAdd);
        addProdTable2.setItems(associatedParts);
        addProdTable2.refresh();
        
    }

    @FXML
    private void addProdDelete(MouseEvent event) {
        if(confirmMessage("Are you sure you want to delete this part?") == true){
            
        } else {
            return;
        }

        Part partToRemove = addProdTable2.getSelectionModel().getSelectedItem();
        associatedParts.remove(partToRemove);
        addProdTable2.setItems(associatedParts);
        addProdTable2.refresh();
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
        alert.setTitle("Unable to add product");
        alert.setHeaderText(field);
        alert.setContentText("All fields are required to add product.");
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
    
}
