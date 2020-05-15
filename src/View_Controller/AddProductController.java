package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    @FXML private TextField productID;
    @FXML private TextField productName;
    @FXML private TextField productPrice;
    @FXML private TextField productStock;
    @FXML private TextField productMax;
    @FXML private TextField productMin;
    @FXML private TextField addProductSearchField;
    //search parts table view
    @FXML private TableView<Part> searchProductTableView;
    @FXML private TableColumn<Part, Integer> partIDSearchCol;
    @FXML private TableColumn<Part, String> partNameSearchCol;
    @FXML private TableColumn<Part, Integer> partStockSearchCol;
    @FXML private TableColumn<Part, Double> partPriceSearchCol;
    //add parts table view
    @FXML private TableView<Part> productTableView;
    @FXML private TableColumn<Part, Integer> partIDCol;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Integer> partStockCol;
    @FXML private TableColumn<Part, Double> partPriceCol;
    @FXML private ObservableList<Part> newAssociatedParts = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchProductTableView.setItems(Inventory.getAllParts());
        partIDSearchCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        partNameSearchCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        partStockSearchCol.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        partPriceSearchCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());


        partIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        partNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        partStockCol.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        partPriceCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
    }

    public void initProductID() {
        productID.setText(String.valueOf(Inventory.getProductIDCount() + 1));
        productID.setEditable(false);
    }

    //add button event for the search parts table view
    @FXML
    public void partToProductAddButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        Part selectedPart = searchProductTableView.getSelectionModel().getSelectedItem();

        if(selectedPart != null) {
            if (newAssociatedParts.contains(selectedPart)) {
                alert.setHeaderText("This part has already been added.");
                alert.setContentText("Please select a different part.");
                alert.showAndWait();
            } else {
                newAssociatedParts.add(selectedPart);
                productTableView.setItems(newAssociatedParts);
            }
        } else {
            alert.setTitle("No Part selected");
            alert.setHeaderText("Please select a part from the part list to add");
            alert.showAndWait();
        }
    }

    //add product event button
    @FXML
    void addProductSearchButton(ActionEvent event) {
        String searchField = addProductSearchField.getText();
        ObservableList<Part> matches = FXCollections.observableArrayList();
        String[] searchWords = searchField.split(" ");
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
        searchProductTableView.setItems(matches);
    }

    //save button
    @FXML
    public void productSaveButtonClicked(ActionEvent event) throws IOException {
        Alert errorMessage = new Alert(Alert.AlertType.ERROR);
        errorMessage.initModality(Modality.NONE);

        if (checkInputTypes()) {
            if (isValid()) {
                int productID = Inventory.getProductIDCount() + 1;
                String productName = this.productName.getText();
                int productStock = Integer.parseInt(this.productStock.getText());
                double productPrice = Double.parseDouble(this.productPrice.getText());
                int productMin = Integer.parseInt(this.productMin.getText());
                int productMax = Integer.parseInt(this.productMax.getText());


                Product newProduct = new Product(productID, productName, productPrice, productStock, productMin, productMax);
                for (Part part: newAssociatedParts) {
                    newProduct.addAssociatedPart(part);
                }

                if (newProduct.getAllAssociatedParts().size() == 0) {
                    errorMessage.setHeaderText("Error: You must have at least one Part to make this Product.");
                    errorMessage.setContentText("Please make sure your Part has been added.");
                    errorMessage.showAndWait();
                } else if (!checkPrices(newProduct)) {
                    errorMessage.setHeaderText("Error: your product price must be greater than the price of all parts combined.");
                    errorMessage.setContentText("Please edit your entry.");
                    errorMessage.setTitle("Action Failed");
                    errorMessage.showAndWait();
                } else {
                    //set the product ID count permanently after the valid checks return true and adding product to Inv
                    Inventory.setProductIDCount();
                    //Add new product to Inventory and move to main screen
                    Inventory.addProduct(newProduct);
                    Parent mainScreenRoot = FXMLLoader.load(getClass().getResource("../resources/MainScreen.fxml"));
                    Scene mainScene = new Scene(mainScreenRoot);
                    Stage mainScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    mainScreenStage.setScene(mainScene);
                    mainScreenStage.show();
                }
            } else {
                if (productStock.getText().isEmpty() || productMin.getText().isEmpty() || productMax.getText().isEmpty() || productPrice.getText().isEmpty()) {
                    errorMessage.setHeaderText("Error: missing information or correct value types are needed.");
                    errorMessage.setContentText("Please make sure you all of your information is filled out correctly.");
                    errorMessage.setTitle("Action Failed");
                    errorMessage.showAndWait();
                } else if (Integer.parseInt(this.productMax.getText()) < Integer.parseInt(this.productMin.getText())) {
                    errorMessage.setHeaderText("Your Minimum value is greater than your Maximum value.");
                    errorMessage.setContentText("Please edit your entry.");
                    errorMessage.setTitle("Action Failed");
                    errorMessage.showAndWait();
                } else if (Integer.parseInt(this.productMin.getText()) > Integer.parseInt(this.productStock.getText())) {
                    errorMessage.setHeaderText("Your Minimum value is greater than what your Inventory value is.");
                    errorMessage.setContentText("Please edit your entry.");
                    errorMessage.setTitle("Action Failed");
                    errorMessage.showAndWait();
                } else if (Integer.parseInt(this.productStock.getText()) > Integer.parseInt(this.productMax.getText())) {
                    errorMessage.setHeaderText("Your Inventory value is greater than what your Maximum value is.");
                    errorMessage.setContentText("Please edit your entry.");
                    errorMessage.setTitle("Action Failed");
                    errorMessage.showAndWait();
                } else {
                    errorMessage.setHeaderText("Error: missing information or correct value types are needed.");
                    errorMessage.setContentText("Please make sure you all of your information is filled out correctly.");
                    errorMessage.setTitle("Action Failed");
                    errorMessage.showAndWait();
                }
            }
        } else {
            errorMessage.setContentText("Please make sure you all of your information is filled out correctly.");
            errorMessage.setTitle("Action Failed");
            errorMessage.setHeaderText("Error: missing information or correct value types are needed.");
            errorMessage.showAndWait();
        }
    }

    //checks the input types of each text field to make sure they are of the correct type
    public Boolean checkInputTypes() {
        boolean broken = true;
        try {
            int testStock = Integer.parseInt(productStock.getText());
        } catch (NumberFormatException e) {
            broken = false;
        }
        try {
            double testPrice = Double.parseDouble(productPrice.getText());
        } catch (NumberFormatException e) {
            broken = false;
        }
        try {
            int testMax = Integer.parseInt(productMax.getText());
        } catch (NumberFormatException e) {
            broken = false;
        }
        try {
            int testMin = Integer.parseInt(productMin.getText());
        } catch (NumberFormatException e) {
            broken = false;
        }
        return broken;
    }

    //checks each textfield to make sure it is not empty or of the null value
    public Boolean isValid () {
        if (productID.getText().isEmpty() || productID.getText() == null) {
            return false;
        }
        if (productName.getText().isEmpty() || productName.getText() == null) {
            return false;
        } else if (productPrice.getText().isEmpty() || productPrice.getText() == null) {
            return false;
        } else if (productStock.getText().isEmpty() || productStock.getText() == null) {
            return false;
        } else if (productMax.getText().isEmpty() || productMax.getText() == null) {
            return false;
        } else if (productMin.getText().isEmpty() || productMin.getText() == null) {
            return false;
        } else if (Integer.parseInt(this.productMin.getText()) > Integer.parseInt(this.productMax.getText())) {
            return false;
        } else if (Integer.parseInt(this.productStock.getText()) < Integer.parseInt(this.productMin.getText())
                || Integer.parseInt(this.productStock.getText()) > Integer.parseInt(this.productMax.getText())) {
            return false;
        }
        return true;
    }

    //makes sure that the Product price is greater than the combined parts price
    public Boolean checkPrices(Product newProduct) {
        double total = 0;
        for (int i=0; i<newProduct.getAllAssociatedParts().size(); i++) {
            total += newProduct.getAllAssociatedParts().get(i).getPrice();
        }
        double productPrice = Double.parseDouble(this.productPrice.getText());
        if (total > productPrice) {
            return false;
        }
        return true;
    }

    //cancel button
    @FXML
    public void addProductCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Action needs validating.");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("All unsaved information will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent mainScreenRoot = FXMLLoader.load(getClass().getResource("../resources/MainScreen.fxml"));
            Scene mainScene = new Scene(mainScreenRoot);
            Stage mainScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScene);
            mainScreenStage.show();
        }
    }

    //delete button
    @FXML
    public void productDeleteButton(ActionEvent event) {
        Part selectedItem = productTableView.getSelectionModel().getSelectedItem();

        Alert conformation = new Alert(Alert.AlertType.CONFIRMATION);
        conformation.setHeaderText("Please confirm your action.");
        conformation.setContentText("Select OK if you wish to delete.");
        Optional<ButtonType> result = conformation.showAndWait();
        if (result.get() == ButtonType.OK) {
            newAssociatedParts.remove(selectedItem);
        }
    }
}
