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
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import controller.MainScreenController;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Dev
 */
public class ModifyPartScreenController implements Initializable {
    
    Stage stage;
    Parent scene;
    Inventory inv;
    int id;
    String name;
    double price;
    int inStock, minStock, maxStock;
    int machineId;
    String companyName;
    int index = -1;
    Part part;


    @FXML
    private RadioButton modPartInHouseRadio;
    @FXML
    private RadioButton modPartOutsourcedRadio;
    @FXML
    private TextField modPartID;
    @FXML
    private TextField modPartName;
    @FXML
    private TextField modPartInv;
    @FXML
    private TextField modPartPriceCost;
    @FXML
    private TextField modPartMax;
    @FXML
    private TextField modPartMin;
    @FXML
    private Label modPartCompNameLabel;
    @FXML
    private TextField modPartCompanyName;
    @FXML
    private Button modPartSave;
    @FXML
    private Button modPartCancel;
    @FXML
    private ToggleGroup modifyPart;

    public ModifyPartScreenController(Inventory inv,Part part) {
        this.inv = inv;
        this.part = part;
    }
    
    @FXML
    private void modPartInHouseRadio(MouseEvent event) throws IOException {
      modPartCompNameLabel.setText("Machine ID");
      modPartCompanyName.setPromptText("Machine ID");
    }
    
    @FXML
    private void modPartOutsourcedRadio(MouseEvent event) throws IOException {
      modPartCompNameLabel.setText("Company Name");
      modPartCompanyName.setPromptText("Company Name");  
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            setData();
    }

    private void setData() {
            if (part instanceof InHouse){
                    InHouse part1 = (InHouse) part;
                    modPartInHouseRadio.setSelected(true);
                    modPartCompNameLabel.setText("Machine ID");
                    this.modPartName.setText(part1.getName());
                    this.modPartID.setText(Integer.toString(part1.getId()));
                    this.modPartInv.setText(Integer.toString(part1.getStock()));
                    this.modPartPriceCost.setText(Double.toString(part1.getPrice()));
                    this.modPartMin.setText(Integer.toString(part1.getMin()));
                    this.modPartMax.setText(Integer.toString(part1.getMax()));
                    this.modPartCompanyName.setText(Integer.toString(part1.getMachineID()));

            }

            if (part instanceof Outsourced){
                    Outsourced part2 = (Outsourced) part;
                    modPartOutsourcedRadio.setSelected(true);
                    modPartCompNameLabel.setText("Company Name");
                    this.modPartName.setText(part2.getName());
                    this.modPartID.setText(Integer.toString(part2.getId()));
                    this.modPartInv.setText(Integer.toString(part2.getStock()));
                    this.modPartPriceCost.setText(Double.toString(part2.getPrice()));
                    this.modPartMin.setText(Integer.toString(part2.getMin()));
                    this.modPartMax.setText(Integer.toString(part2.getMax()));
                    this.modPartCompanyName.setText(part2.getCompanyName());

            }
    }    

    @FXML
    private void modPartSave(MouseEvent event) throws IOException {
        //Begin validation
        if(modPartName.getText().trim().length() == 0){
            alertMessage("Part Name cannot be blank");
            return;
        }
        if(!modPartID.getText().trim().matches("[0-9]*")){
            alertMessage("Part ID must be an integer.");
            return;
        }
        if(Integer.parseInt(modPartMin.getText().trim()) >= Integer.parseInt(modPartMax.getText().trim())){
                alertMessage("Min must be less than max.");
                return;
        }
        if(Integer.parseInt(modPartMax.getText().trim()) <= Integer.parseInt(modPartMin.getText().trim())){
                alertMessage("Max must be greater than min.");
                return;
        }
        if(!modPartInv.getText().trim().matches("[0-9]*")){
            alertMessage("Part Inv must be a positive integer.");
            return;
        }
        if(!modPartMin.getText().trim().matches("[0-9]*")){
            alertMessage("Min must be an integer.");
            return;
        }
        if(!modPartMax.getText().trim().matches("[0-9]*")){
            alertMessage("Max must be an integer.");
            return;
        }
        if(!modPartPriceCost.getText().trim().matches("[+-]?\\d+\\.?(\\d+)?")){
            alertMessage("Price must be a number.");
        }
        if(Integer.parseInt(modPartInv.getText().trim()) > Integer.parseInt(modPartMax.getText().trim()) || Integer.parseInt(modPartInv.getText().trim()) < Integer.parseInt(modPartMin.getText().trim())){
            alertMessage("Part Inv must be between min and max values.");
            return;
        }
        
        if(confirmMessage("Are you sure you want to modify this part?") == true){
            
        } else {
            return;
        }
       //Update Part
       if(modPartInHouseRadio.isSelected()){
        updateItemInHouse();
       } else if(modPartOutsourcedRadio.isSelected()){
	updateItemOutsourced();
       } else alertMessage("InHouse or Outsourced must be selected.");
       
        if(modPartInHouseRadio.isSelected() && !modPartCompanyName.getText().trim().matches("[0-9]*")){
            alertMessage("Machine ID must be an integer");
            return;
        }
       
        mainScreen(event);
    }

    @FXML
    private void modPartCancel(MouseEvent event) throws IOException {
        
        mainScreen(event);
    }
    private void updateItemInHouse() {
            inv.updatePart(new InHouse(Integer.parseInt(modPartID.getText().trim()),modPartName.getText().trim(),Double.parseDouble(modPartPriceCost.getText().trim()),Integer.parseInt(modPartInv.getText().trim()),Integer.parseInt(modPartMin.getText().trim()),Integer.parseInt(modPartMax.getText().trim()),Integer.parseInt(modPartCompanyName.getText().trim())));
    }

    private void updateItemOutsourced() {
            inv.updatePart(new Outsourced(Integer.parseInt(modPartID.getText().trim()),modPartName.getText().trim(),Double.parseDouble(modPartPriceCost.getText().trim()),Integer.parseInt(modPartInv.getText().trim()),Integer.parseInt(modPartMin.getText().trim()),Integer.parseInt(modPartMax.getText().trim()),modPartCompanyName.getText().trim()));
    }
    
    
    
    private void alertMessage(String field){
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Unable to modify part");
           alert.setHeaderText(field);
           alert.setContentText("All fields are required to modify part.");
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
