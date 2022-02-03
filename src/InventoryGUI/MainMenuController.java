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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * @author Jeremy Hennessy
 * Class Controller for the main menu of the InventoryGUI
 */
public class MainMenuController implements Initializable {
    @FXML private TableView<Part> partTable;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partStockColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productStockColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;
    @FXML private TextField SearchPart;
    @FXML private TextField SearchProduct;
    public static Inventory inventory = new Inventory();//Inventory will be shared with each scene


    /**
     * Initializes the main menu when opened
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override public void initialize(URL url, ResourceBundle rb) {
        setUpTable(partTable, partIdColumn, partNameColumn, partStockColumn, partPriceColumn);
        partTable.setItems(inventory.getAllParts());
        searchPart(partTable, SearchPart);

        setUpTable(productTable, productIdColumn, productNameColumn, productStockColumn, productPriceColumn);
        productTable.setItems(inventory.getAllProducts());
        searchProduct(productTable, SearchProduct);
    }


    /**
     * Searches the contents of a table and sets the tables contents to
     * show the search data entered.
     * Will populate table with corresponding ID entered or corresponding String/Substring entered
     * Made static so it can be used in the product.fxml and main menu
     * @param tableView table that with be searched
     * @param Search TextField that will contain search string
     * UPDATES table to contain elements searched for
     */
    public static void searchPart(TableView tableView, TextField Search){
        Search.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String searchTxt = Search.getText().toLowerCase();

                ObservableList<Part> searchedParts = FXCollections.observableArrayList();
                if(!searchTxt.isEmpty()) {
                    //if it can be parsed to an int then the field entered was an ID
                    if (PartController.canParseInt(searchTxt)) {
                        //check for ID
                        Part part = inventory.lookupPart(Integer.parseInt(searchTxt));
                        if (part == null) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Dialog");
                            alert.setHeaderText("Search Error");
                            alert.setContentText("No Part Was Found");

                            alert.showAndWait();
                        } else {
                            searchedParts.add(part);
                            tableView.setItems(searchedParts);
                            //Decided highlighting the row was less noticeable for
                            //big inventories so I removed this code for a tableView set to the one element
                            //tableView.getSelectionModel().select(part);
                        }
                    } else {
                        //Check for part name
                        searchedParts = inventory.lookupPart(searchTxt);
                        if (searchedParts.isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Dialog");
                            alert.setHeaderText("Search Error");
                            alert.setContentText("No Part Was Found");

                            alert.showAndWait();
                        } else {
                            tableView.setItems(searchedParts);
                        }
                    }
                }else{
                    //reset table
                    tableView.setItems(inventory.getAllParts());
                }
            }
        });

    }

    /**
     * Searches the contents of a table and sets the tables contents to
     * show the search data entered.
     * Will populate table with corresponding ID entered or corresponding String/Substring entered
     * @param tableView table that with be searched
     * @param Search TextField that will contain search string
     * UPDATES table to contain elements searched for
     */
    public void searchProduct(TableView tableView, TextField Search){
        Search.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String searchTxt = Search.getText().toLowerCase();

                ObservableList<Product> searchedProducts = FXCollections.observableArrayList();
                if(!searchTxt.isEmpty()) {
                    //if txt can be parsed to int then it is an ID
                    if (PartController.canParseInt(searchTxt)) {
                        //Find using ID
                        Product product = inventory.lookupProduct(Integer.parseInt(searchTxt));
                        if (product == null) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Dialog");
                            alert.setHeaderText("Search Error");
                            alert.setContentText("No Part Was Found");

                            alert.showAndWait();
                        } else {
                            searchedProducts.add(product);
                            tableView.setItems(searchedProducts);
                            //Decided highlighting the row was less noticeable for
                            //big inventories so I removed this code for a tableView set to the one element
                            //tableView.getSelectionModel().select(part);
                        }
                    } else {
                        searchedProducts = inventory.lookupProduct(searchTxt);
                        if (searchedProducts.isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Dialog");
                            alert.setHeaderText("Search Error");
                            alert.setContentText("No Part Was Found");

                            alert.showAndWait();
                        } else {
                            tableView.setItems(searchedProducts);
                        }
                    }
                }else{
                    //reset table
                    tableView.setItems(inventory.getAllProducts());
                }
            }
        });
    }

    /**
     * Sets up a table to have part or product data inserted into it
     * Uses generic type so that the code will not be repeated for a part table and product table
     * @param tableView table to be setup
     * @param idColumn to contain ids
     * @param nameColumn to contain names
     * @param stockColumn to contain inventory data
     * @param priceColumn to contain price data
     * @param <T> to be either a Part or Product
     */
    public static<T> void setUpTable(TableView<T> tableView, TableColumn<T, Integer> idColumn,
                                        TableColumn<T, String> nameColumn,
                                        TableColumn<T, Integer> stockColumn,
                                        TableColumn<T, Double> priceColumn){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        //format the price column to display with a dollar cent format
        priceColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }


    /**
     * Sends the application to the Part scene
     * @param event on click
     * @throws IOException Exception
     */
    public void addPartScreen(ActionEvent event) throws IOException{
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("Part.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    /**
     * Sends the application to the Product scene
     * @param event on click
     * @throws IOException Exception
     */
    public void addProductScreen(ActionEvent event) throws IOException{
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("Product.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    /**
     * Sends the application to the Part Scene and will pass the given part
     * with its included data to the scene
     * RUNTIME ERROR Did not originally check if any part was selected
     * @param event on click
     * @throws IOException Exception
     */
    public void modifyPartScreen(ActionEvent event) throws IOException{
        if(!partTable.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Part.fxml"));
            Parent tableViewParent = loader.load();

            Scene tableViewScene = new Scene(tableViewParent);

            //pass contents or the part
            PartController controller = loader.getController();
            controller.passPart(partTable.getSelectionModel().getSelectedItem());

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
        }
    }

    /**
     * Sends the application to the Product Scene and will pass the given product
     * with its included data to the scene
     * RUNTIME ERROR Did not originally check if any product was selected
     * @param event on click
     * @throws IOException Exception
     */
    public void modifyProductScreen(ActionEvent event) throws IOException{
        if(!productTable.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Product.fxml"));
            Parent tableViewParent = loader.load();

            Scene tableViewScene = new Scene(tableViewParent);

            //pass the contents of the product
            ProductController controller = loader.getController();
            controller.passProduct(productTable.getSelectionModel().getSelectedItem());

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
        }
    }

    /**
     * Deletes a selected Part
     */
    public void deletePart(){
        //only execute delete when a part is selected
        if(!partTable.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Part");
            alert.setHeaderText("Delete Part");
            alert.setContentText("Are You Sure You Would Like To Delete This Part?");
            Optional<ButtonType> action = alert.showAndWait();

            if(action.get() == ButtonType.OK)
                inventory.deletePart(partTable.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Deletes a selected Product
     */
    public void deleteProduct(){
        if(!productTable.getSelectionModel().isEmpty()){
            if(productTable.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Product");
                alert.setHeaderText("Delete Part");
                alert.setContentText("Are You Sure You Would Like To Delete This Product?");
                Optional<ButtonType> action = alert.showAndWait();

                if (action.get() == ButtonType.OK)
                    inventory.deleteProduct(productTable.getSelectionModel().getSelectedItem());
            }else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Delete Invalid");
                errorAlert.setContentText("Products With Associated Parts Cannot Be Deleted");
                errorAlert.showAndWait();
            }
        }
    }

    /**
     * Exits the program
     */
    public void exit(){
        System.exit(0);
    }

}
