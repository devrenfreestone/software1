/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

/**
 * FXML Controller class
 *
 * @author Dev
 */
public class AddPartScreenController implements Initializable {
    

    Stage stage;
    Parent scene; 
    Inventory inv;
    int idCounter = 0;
    int id;
    String name;
    double price;
    int inStock, minStock, maxStock;

    @FXML
    private Button saveAddPart;
    @FXML
    private RadioButton addPartInHouseRadio;
    @FXML
    private RadioButton addPartOutsourcedRadio;
    @FXML
    private TextField addPartID;
    @FXML
    private TextField addPartName;
    @FXML
    private TextField addPartInv;
    @FXML
    private TextField addPartPriceCost;
    @FXML
    private TextField addPartMax;
    @FXML
    private TextField addPartMin;
    @FXML
    private Label addPartCompNameLabel;
    @FXML
    private TextField addPartCompanyName;
    @FXML
    private ToggleGroup addPart;

    public AddPartScreenController(Inventory inv) {
        this.inv = inv;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        for (Part p : inv.getAllParts()) {
            if (p.getId() > idCounter){
                idCounter = p.getId();
            }
        } 
        idCounter +=1;
        addPartID.setText(String.valueOf(idCounter));
    }

    @FXML
    private void addPartInHouseRadio(MouseEvent event) throws IOException {
      addPartCompNameLabel.setText("Machine ID");
      addPartCompanyName.setPromptText("Machine ID");
    }
    
    @FXML
    private void addPartOutsourcedRadio(MouseEvent event) throws IOException {
      addPartCompNameLabel.setText("Company Name");
      addPartCompanyName.setPromptText("Company Name");  
    }

    @FXML
    private void saveAddPart(MouseEvent event) throws IOException {
        if (addPartID.getText().length() == 0){
            alertMessage("ID cannot be blank");
            return;
        }
        if(addPartName.getText().trim().length() == 0){
            alertMessage("Name cannot be blank.");
            return;
        }
        if(addPartPriceCost.getText().length() == 0) {
            alertMessage("Price cannot be blank.");
            return;
        }
        if(!addPartPriceCost.getText().trim().matches("^(0*[1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
                alertMessage("Price must be a positive number.");
                return;
        }
        if(Double.parseDouble(addPartPriceCost.getText().trim()) <= 0){
            alertMessage("Price must be greater than zero.");
            return;
        }
        if(Integer.parseInt(addPartMin.getText().trim()) >= Integer.parseInt(addPartMax.getText().trim())){
                alertMessage("Min must be less than max.");
                return;
        }
        if(Integer.parseInt(addPartMax.getText().trim()) <= Integer.parseInt(addPartMin.getText().trim())){
                alertMessage("Max must be greater than min.");
                return;
        }
        if (addPartInv.getText().length() == 0) {
            alertMessage("Inv cannot be blank");
            return;
        }
        if(!addPartInv.getText().trim().matches("[0-9]*")){
                alertMessage("Inv must be an integer.");
                return;
        }
        if(Integer.parseInt(addPartInv.getText().trim()) > Integer.parseInt(addPartMax.getText().trim()) || Integer.parseInt(addPartInv.getText().trim()) < Integer.parseInt(addPartMin.getText().trim())){
            alertMessage("Part Inv must be between min and max values.");
            return;
        }
        if (addPartMin.getText().length() == 0) {
            
            alertMessage("Min cannot be blank.");
            return;
        }
        if(!addPartMin.getText().trim().matches("[1-9]*")){
                alertMessage("Min must be an integer.");
                return;
        }
        if(!addPartInv.getText().trim().matches("[1-9]*")){
                alertMessage("Inv must be a positive integer.");
                return;
        }
        if (addPartMax.getText().length() == 0) {
            alertMessage("Max cannot be blank.");
            return;
        }
        if(!addPartMax.getText().trim().matches("[0-9]*")){
                alertMessage("Max must be an integer.");
                return;
        }
        mainScreen(event);
    }
    
    private void addItemInHouse() {
            inv.addPart(new InHouse(Integer.parseInt(addPartID.getText().trim()),addPartName.getText().trim(),Double.parseDouble(addPartPriceCost.getText().trim()),Integer.parseInt(addPartInv.getText().trim()),Integer.parseInt(addPartMin.getText().trim()),Integer.parseInt(addPartMax.getText().trim()),Integer.parseInt(addPartCompanyName.getText().trim())));
    }

    private void addItemOutsourced() {
            inv.addPart(new Outsourced(Integer.parseInt(addPartID.getText().trim()),addPartName.getText().trim(),Double.parseDouble(addPartPriceCost.getText().trim()),Integer.parseInt(addPartInv.getText().trim()),Integer.parseInt(addPartMin.getText().trim()),Integer.parseInt(addPartMax.getText().trim()),addPartCompanyName.getText().trim()));
    }    
    
    @FXML
    private void alertMessage(String field){
           Alert alert = new Alert(AlertType.WARNING);
           alert.setTitle("Unable to add part");
           alert.setHeaderText(field);
           alert.setContentText("All fields are required to add part.");
           alert.showAndWait();
        }

    @FXML
    private void cancelAddPart(MouseEvent event) throws IOException {
            addPartID.clear();
            addPartName.clear();
            addPartInv.clear();
            addPartPriceCost.clear();
            addPartMax.clear();
            addPartMin.clear();
            addPartCompanyName.clear();
            mainScreen(event);
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
}
