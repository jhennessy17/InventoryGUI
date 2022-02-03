package InventoryGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import static InventoryGUI.MainMenuController.inventory;


/**
 * @author Jeremy Hennessy
 * Class to add and modify parts of an inventory
 */
public class PartController implements Initializable {
    @FXML private RadioButton inHouseRadio;
    @FXML private RadioButton outsourcedRadio;
    @FXML private Label title;
    @FXML private Label idOrCompanyName; //Holds dependent field machineId and companyName
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField inv;
    @FXML private TextField price;
    @FXML private TextField max;
    @FXML private TextField min;
    @FXML private TextField machineIdCompanyName;
    private ToggleGroup pageToggle;
    private boolean inHouse = true; //Will be used to determine whether the part is in house or outsourced
    private int ID = 1; //The id


    /**
     * Initializes the Part Screen
     * Sets up the screen for the add part or modify part Interface
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        pageToggle = new ToggleGroup();
        this.inHouseRadio.setToggleGroup(pageToggle);
        this.outsourcedRadio.setToggleGroup(pageToggle);

        if(inHouse)
            inHouseRadio.setSelected(true);
        else
            outsourcedRadio.setSelected(true);
    }

    /**
     * Controls whether the part is inhouse or outsourced
     * Will activate inHouse and Outsourced based on radio button selection
     */
    public void togglePage(){
        if(this.pageToggle.getSelectedToggle().equals(this.inHouseRadio)) {
            idOrCompanyName.setText("Machine ID");
            inHouse = true;
        }
        if(this.pageToggle.getSelectedToggle().equals(this.outsourcedRadio)){
            idOrCompanyName.setText("Company Name");
            inHouse = false;
        }
    }

    /**
     * Accepts a part and sets the contents of the Interface to display
     * the contents of the part passed
     * RUNTIME ERROR When finding whether the part was InHouse or Outsourced, corrected with a couple try catch statements
     * @param part to be passed
     */
    public void passPart(Part part){
        //Set the scene to the parts contents
        title.setText("Modify Part");
        ID = part.getId();
        id.setText(String.valueOf(part.getId()));
        name.setText(part.getName());
        inv.setText(String.valueOf(part.getStock()));
        DecimalFormat cent = new DecimalFormat("#.##");
        price.setText(cent.format(part.getPrice()));
        max.setText(String.valueOf(part.getMax()));
        min.setText(String.valueOf(part.getMin()));

        //used to find whether the part is inHouse or Outsourced
        try {
            machineIdCompanyName.setText(String.valueOf(((InHouse) part).getMachineId()));
        }catch (Exception e){
            outsourcedRadio.setSelected(true);
            idOrCompanyName.setText("Company Name");
            inHouse = false;
        }
        try{
            machineIdCompanyName.setText(((Outsourced) part).getCompanyName());
        }catch (Exception e){
            inHouseRadio.setSelected(true);
        }
    }

    /**
     * Makes sure the data entered in each textField is good and then
     * saves the contents of the part to the inventory.
     * Will do nothing if the data is not good
     * @param event on click
     * @throws IOException Exception
     * UPDATES inventory
     */
    public void updatePartTable(ActionEvent event)throws IOException {
        if(checkGoodData(name, inv, price, max, min) && checkMachineIDCompanyNameData()) {
            //if the part has no id then it needs to be added
            if(id.getText().isEmpty()) {
                //if the inventory has no parts then the ID needs to be initialized to 1
                //otherwise the id is set to 1 more than the last ID in the parts list
                if(!inventory.getAllParts().isEmpty())
                    ID = inventory.getAllParts().get(inventory.getAllParts().size()-1).getId() + 1;
                inventory.addPart(getPart());
            }else {
                inventory.updatePart(ID, getPart());
            }

            Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
        }
    }

    /**
     * Cancels current transaction and returns to main menu
     * Allows cancel to be shared between product and part
     * since both buttons do the same thing
     * @param event on click
     * @throws IOException Exception
     */
    public static void cancelButtonAction(ActionEvent event) throws IOException{
        Parent tableViewParent = FXMLLoader.load(PartController.class.getResource("MainMenu.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    /**
     * Sets up button to cancel transaction
     * @param event on click
     * @throws IOException Exception
     */
    public void cancelButton(ActionEvent event) throws IOException{
        cancelButtonAction(event);
    }

    /**
     * Checks if text can be parsed to an int
     * @param text to be checked if it can be parsed
     * @return true if the text can be parsed to an int and false otherwise
     */
    public static boolean canParseInt(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if text can be parsed to price format
     * @param text to be checked if it can be parsed to price format
     * @return true if the text can be parsed to price format and false otherwise
     */
    public static boolean canParsePrice(String text){
        try {
            Double.parseDouble(text);
            //Makes sure the price is in correct dollar cent format or just dollar format
            if(!text.contains(".") || text.substring(text.indexOf(".")+1).length()==2)
                return true;
            else
                return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     *
     * @return true if the MachineID or CompanyName data are valid
     */
    private boolean checkMachineIDCompanyNameData(){
        if(machineIdCompanyName.getText().isEmpty()){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("All Text Fields Must Contain Values");
            errorAlert.showAndWait();
            return false;
        }

        //check machine id format if necessary
        if (inHouse)
            if (!canParseInt(machineIdCompanyName.getText())) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("Number Fields Must Contain Numbers");
                errorAlert.showAndWait();
                return false;
            }

        return true;
    }

    /**
     * Makes sure the data is consistent with conditions needed to be met in the program
     * Checks each textField has data, can be parsed to int or price if needed, and sets inventory rules
     * LOGICAL ERROR Originally contained extra check for max being less than min which already isn't possible
     * when correctly checking for Inventory bounds
     * @param name name field to be checked
     * @param inv inventory field to be checked
     * @param price price field to be checked
     * @param max max inventory field to be checked
     * @param min min field to be checked
     * @return true if all data passes condition and false otherwise
     */
    public static boolean checkGoodData(TextField name, TextField inv, TextField price, TextField max, TextField min) {
        //Make sure data is not empty
        if (name.getText().isEmpty() || inv.getText().isEmpty() || price.getText().isEmpty() || max.getText().isEmpty() ||
                min.getText().isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("All Text Fields Must Contain Values");
            errorAlert.showAndWait();
            return false;
        }
        //make sure all data that should be an int is an int
        if (!(canParseInt(inv.getText()) & canParseInt(max.getText()) & canParseInt(min.getText()))) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("Number Fields Must Contain Numbers");
            errorAlert.showAndWait();
            return false;
        }

        //check price format
        if(!canParsePrice(price.getText())){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("Price Must Be In Dollar Format Or Dollar Cent Format");
            errorAlert.showAndWait();
            return false;
        }


        //make sure the inventory isn't less than the min allowed and not more than the max allowed
        if(Integer.parseInt(inv.getText()) < Integer.parseInt(min.getText()) ||
                Integer.parseInt(inv.getText()) > Integer.parseInt(max.getText())) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The Inventory Must Maintain Correct Min And Max Constraints");
            errorAlert.showAndWait();
            return false;
        }

        /*
        //make sure the max isn't less than the min
        if(Integer.parseInt(max.getText()) < Integer.parseInt(min.getText())) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("The Max Cannot Be less than the Min");
            errorAlert.showAndWait();
            return false;
        }*/

        //if none of the bad data fields are true then return that the data is good
        return true;
    }


    /**
     * Constructs a part as either InHouse or Outsourced
     * RUNTIME ERROR Had trouble getting the code to recognize whether the part was InHouse or Outsourced
     * corrected with a variable to keep track of whether the part was inhouse or outsourced based on the radio
     * button selection
     * @return Part
     */
    public Part getPart(){
        if(inHouse){
            return new InHouse(ID,
                    name.getText(),
                    Double.parseDouble(price.getText()),
                    Integer.parseInt(inv.getText()),
                    Integer.parseInt(min.getText()),
                    Integer.parseInt(max.getText()),
                    Integer.parseInt(machineIdCompanyName.getText()));
        }else{
            return new Outsourced(ID,
                    name.getText(),
                    Double.parseDouble(price.getText()),
                    Integer.parseInt(inv.getText()),
                    Integer.parseInt(min.getText()),
                    Integer.parseInt(max.getText()),
                    machineIdCompanyName.getText());
        }
    }
}
