package View_Controller;

import Model.*;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    //parts TableView
    @FXML private TableView<Part> mainPartTable;
    @FXML private TableColumn<Part, Integer> mainPartIdColumn;
    @FXML private TableColumn<Part, String> mainPartNameColumn;
    @FXML private TableColumn<Part, Integer> mainPartInvLevelColumn;
    @FXML private TableColumn<Part, Double> mainPartPriceColumn;
    //products TableView
    @FXML private TableView<Product> mainProductTable;
    @FXML private TableColumn<Product, Integer> mainProductIdColumn;
    @FXML private TableColumn<Product, String> mainProductNameColumn;
    @FXML private TableColumn<Product, Integer> mainProductInvLevelColumn;
    @FXML private TableColumn<Product, Double> mainProductPriceColumn;
    //search
    @FXML private TextField partSearchTextField;
    @FXML private TextField productSearchField;

    /* Initializes the controller class */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPartTable.setItems(Inventory.getAllParts());
        mainPartIdColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        mainPartNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        mainPartInvLevelColumn.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        mainPartPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        mainProductTable.setItems(Inventory.getAllProducts());
        mainProductIdColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        mainProductNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        mainProductInvLevelColumn.setCellValueFactory(cellData -> cellData.getValue().productStockProperty().asObject());
        mainProductPriceColumn.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
    }

    public void sampleData() {
        Inventory.setPartIDCount();
        InHouse part1 = new InHouse(Inventory.getPartIDCount(), "Part A1", 2.99, 20, 5, 50, 001);
        Inventory.setPartIDCount();
        Outsourced part2 = new Outsourced(Inventory.getPartIDCount(), "Part O1", 5.99, 25, 5, 50, "Whole Foods");
        Inventory.addPart(part1);
        Inventory.addPart(part2);

        Inventory.setProductIDCount();
        Product product1 = new Product(Inventory.getProductIDCount(), "Product A1", 9.99, 50, 5, 100);
        product1.addAssociatedPart(part1);
        Inventory.setProductIDCount();
        Product product2 = new Product(Inventory.getProductIDCount(), "Product O1", 12.99, 20, 5, 50);
        product2.addAssociatedPart(part1);
        product2.addAssociatedPart(part2);
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);

        mainPartTable.setItems(Inventory.getAllParts());
        mainProductTable.setItems(Inventory.getAllProducts());
    }

    /* Parts Side */

    //search
    public void searchParts(String searchText) {
        ObservableList<Part> matches = FXCollections.observableArrayList();
        String[] searchWords = searchText.split(" ");
        for (int i=0; i<searchWords.length; i++) {
            try {
                String parsedWord = searchWords[i];
                ArrayList<Part> foundNamesArray = Inventory.lookupPartName(parsedWord);
                for (Part nameMatch : foundNamesArray) {
                    matches.add(nameMatch);
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error");
            }
        }
        mainPartTable.setItems(matches);
    }
    @FXML
    public void partSearchButtonClicked(ActionEvent event) throws IOException {
        String searchField = partSearchTextField.getText();
        searchParts(searchField);
    }
    @FXML
    public void partSearchOnEnter(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER")) {
            String searchField = partSearchTextField.getText();
            searchParts(searchField);
        }
    }

    //add button
    @FXML
    public void addPartsButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../resources/AddPartScreen.fxml"));
        Parent addPartRoot = loader.load();

        Scene addPartScene = new Scene(addPartRoot);
        Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AddPartController controller = loader.getController();
        controller.initPartID();

        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    //modify button
    @FXML
    public void modifyPartsButtonClicked(ActionEvent event) throws IOException {
        Part selectedPart = mainPartTable.getSelectionModel().getSelectedItem();

        if(selectedPart != null) {
            modifyPart(event, selectedPart);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Part selected");
            alert.setHeaderText("Please select a part from the part list to modify");
            alert.showAndWait();
        }
    }

    //pulls the fxml for the modify parts stage
    public void modifyPart(ActionEvent event, Part part) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../resources/ModifyPartScreen.fxml"));
        Parent part_page_parent = loader.load();

        Scene scene = new Scene(part_page_parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        ModifyPartController controller = loader.getController();
        controller.startModify(part);

        stage.hide();
        stage.setScene(scene);
        stage.show();
    }

    //delete button
    @FXML
    public void deletePartsButtonClicked(ActionEvent event) throws IOException {
        Part selectedItem = mainPartTable.getSelectionModel().getSelectedItem();
        Alert conformation = new Alert(Alert.AlertType.CONFIRMATION);
        if (selectedItem != null) {
            conformation.setHeaderText("Please confirm your action.");
            conformation.setContentText("Select OK if you wish to delete.");
            Optional<ButtonType> result = conformation.showAndWait();
            if(result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedItem);
            }
        } else {
            conformation.setTitle("No Part selected");
            conformation.setHeaderText("Please select a part from the part list to Delete");
            conformation.showAndWait();
        }
    }

    /* Product Side */

    //add button
    public void addProductButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../resources/AddProductScreen.fxml"));
        Parent addProductRoot = loader.load();

        Scene addProductScene = new Scene(addProductRoot);
        Stage addProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AddProductController controller = loader.getController();
        controller.initProductID();

        addProductStage.setScene(addProductScene);
        addProductStage.show();
    }

    //modify button
    public void modifyProductButtonClicked(ActionEvent event) throws IOException {
        Product selectedProduct = mainProductTable.getSelectionModel().getSelectedItem();

        if(selectedProduct != null) {
            modifyProduct(event, selectedProduct);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Part selected");
            alert.setHeaderText("Please select a part from the part list to modify");
            alert.showAndWait();
        }
    }

    //pulls the fxml for the modify product stage
    public void modifyProduct(ActionEvent event, Product product) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../resources/ModifyProductScreen.fxml"));
        Parent modifyProductRoot = loader.load();

        Scene scene = new Scene(modifyProductRoot);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        ModifyProductController controller = loader.getController();
        controller.startModifyProduct(product);

        stage.hide();
        stage.setScene(scene);
        stage.show();
    }

    //search button
    @FXML
    public void productSearchButtonClicked() {
        String searchField = productSearchField.getText();
        ObservableList<Product> matches = FXCollections.observableArrayList();
        String[] searchWords = searchField.split(" ");
        for (int i=0; i<searchWords.length; i++) {
            try {
                String parsedWord = searchWords[i];
                ArrayList<Product> foundNamesArray = Inventory.lookupProductName(parsedWord);
                for (Product nameMatch : foundNamesArray) {
                    matches.add(nameMatch);
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error");
            }
        }
        mainProductTable.setItems(matches);
    }
    @FXML
    public void productSearchButtonClicked(ActionEvent e) {
        productSearchButtonClicked();
    }
    @FXML
    public void productSearchOnEnter(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER")) {
            productSearchButtonClicked();
        }
    }

    //delete button
    @FXML
    public void deleteProductButtonClicked(ActionEvent event) throws IOException {
        Product selectedItem = mainProductTable.getSelectionModel().getSelectedItem();
        Alert conformation = new Alert(Alert.AlertType.CONFIRMATION);
        if (selectedItem != null) {
            conformation.setHeaderText("Please confirm your action.");
            conformation.setContentText("Select OK if you wish to delete.");
            Optional<ButtonType> result = conformation.showAndWait();
            if(result.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectedItem);
            }
        } else {
            conformation.setTitle("No Part selected");
            conformation.setHeaderText("Please select a product from the product list to Delete");
            conformation.showAndWait();
        }
    }

    //exit button
    @FXML
    public void mainExitButtonClicked(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Please confirm you want to exit.");
        alert.setContentText("Any unsaved changes will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}
