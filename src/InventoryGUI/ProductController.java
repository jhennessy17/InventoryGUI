package InventoryGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Optional;
import java.util.ResourceBundle;

import static InventoryGUI.MainMenuController.*;
import static InventoryGUI.PartController.cancelButtonAction;
import static InventoryGUI.PartController.checkGoodData;

public class ProductController implements Initializable {
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partStockColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    @FXML private TableColumn<Part, Integer> associatedPartIdColumn;
    @FXML private TableColumn<Part, String> associatedPartNameColumn;
    @FXML private TableColumn<Part, Integer> associatedPartStockColumn;
    @FXML private TableColumn<Part, Double> associatedPartPriceColumn;
    @FXML private TextField Search;
    @FXML private TableView<Part> partTable;
    @FXML private TableView<Part> associatedPartTable;
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField inv;
    @FXML private TextField price;
    @FXML private TextField max;
    @FXML private TextField min;
    @FXML private Label Title;
    private ObservableList<Part> associatedTableList = FXCollections.observableArrayList();
    private int ID = 1;

    /**
     * Sets up the part table and associated part table along with the search functionality
     * @param url URL
     * @param rb ResourceBundle
     */
    public void initialize(URL url, ResourceBundle rb){
        setUpTable(partTable, partIdColumn, partNameColumn, partStockColumn, partPriceColumn);
        partTable.setItems(inventory.getAllParts());
        searchPart(partTable, Search);

        setUpTable(associatedPartTable, associatedPartIdColumn, associatedPartNameColumn, associatedPartStockColumn, associatedPartPriceColumn);
        associatedPartTable.setItems(associatedTableList);
    }

    /**
     * Adds associated part to associatedTableList
     * UPDATES associatedTableList
     * will update a products association if passed to this controller to be modified
     */
    public void addToAssociatedParts(){
        if(!partTable.getSelectionModel().isEmpty())
            associatedTableList.add(partTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Deletes data from associated part table
     * Will prompt user before executing delete
     * LOGICAL ERROR Had trouble maintaining the difference between the associatedTableList and the actual products
     * associated parts but was fixed when setting the associatedTableList to the actual associatedPartList within
     * the product passed
     * UPDATES associatedTableList
     * Will update a product association if passed to this controller to be modified
     */
    public void deleteFromAssociatedParts(){
        if(!associatedPartTable.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Product");
            alert.setHeaderText("Delete Associated Part");
            alert.setContentText("Are You Sure You Would Like To Disassociate This Part With The Product?");
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == ButtonType.OK)
                associatedTableList.remove(associatedPartTable.getSelectionModel().getSelectedItem());
        }
    }


    /**
     * Makes sure the data for the product is good
     * Checks to see if the product is new or is being updated
     * Updates or Adds the new product in the inventory
     * LOGICAL ERROR Did not initially differentiate between whether the product was being modified or added
     * UPDATES inventory
     * @param event on click
     * @throws IOException Exception
     */
    public void updateProductTable(ActionEvent event) throws IOException {
        if(checkGoodData(name, inv, price, max, min)) {
            //If the id is empty then it is a new product being added
            if(id.getText().isEmpty()) {
                //if the inventory has no products then the ID needs to be initialized to 1
                //otherwise the id is set to 1 more than the last ID in the products list
                if(!inventory.getAllProducts().isEmpty())
                    ID = inventory.getAllProducts().get(inventory.getAllProducts().size()-1).getId() + 1;
                inventory.addProduct(getNewProduct());
            }else {
                inventory.updateProduct(ID, getNewProduct());
            }

            Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
        }
    }

    /**
     * Gets a new product and populates its associatedParts list
     * with the associatedList set up in the scene
     * @return Product
     */
    public Product getNewProduct(){
        Product product = new Product(ID,
                            name.getText(),
                            Double.parseDouble(price.getText()),
                            Integer.parseInt(inv.getText()),
                            Integer.parseInt(min.getText()),
                            Integer.parseInt(max.getText()));

        for(int i = 0; i < associatedPartTable.getItems().size(); i++){
            product.addAssociatedPart(associatedPartTable.getItems().get(i));
        }
        return product;
    }

    /**
     * Passes a product so its contents can be used and updated
     * in this controller.
     * Sets data fields to the contents contained in the product
     * @param product passed to controller
     */
    public void passProduct(Product product){
        Title.setText("Modify Product");
        associatedTableList = product.getAllAssociatedParts();
        associatedPartTable.setItems(associatedTableList);
        ID = product.getId();
        id.setText(String.valueOf(product.getId()));
        name.setText(product.getName());
        inv.setText(String.valueOf(product.getStock()));
        //sets the product to be in price format
        DecimalFormat cent = new DecimalFormat("#.##");
        price.setText(cent.format(product.getPrice()));
        max.setText(String.valueOf(product.getMax()));
        min.setText(String.valueOf(product.getMin()));
    }

    /**
     * Cancels the transaction and returns to the main menu
     * @param event on click
     * @throws IOException Exception
     */
    public void cancelButton(ActionEvent event) throws IOException{
        cancelButtonAction(event);
    }



}
