package View_Controller;

import Model.InHouse;
import Model.Outsourced;
import Model.Part;
import Model.Inventory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class ModifyPartController {
    @FXML private ToggleGroup modifyPartToggleGroup;
    @FXML private Label idCode;
    @FXML private TextField modifyPartID;
    @FXML private TextField modifyPartName;
    @FXML private TextField modifyPartStock;
    @FXML private TextField modifyLastEntry;
    @FXML private TextField modifyPartMax;
    @FXML private TextField modifyPartMin;
    @FXML private TextField modifyPartPrice;
    @FXML private RadioButton outsourced;
    @FXML private RadioButton inHouse;
    private int partIndex;

    //populates the text fields with the selected part
    @FXML
    public void startModify(Part part) {
        this.partIndex = Inventory.getAllParts().indexOf(part);
        modifyPartID.setText(Integer.toString(part.getID()));
        modifyPartID.setEditable(false);
        modifyPartName.setText(part.getName());
        modifyPartStock.setText(Integer.toString(part.getStock()));
        modifyPartPrice.setText(Double.toString(part.getPrice()));
        modifyPartMin.setText(Integer.toString(part.getMin()));
        modifyPartMax.setText(Integer.toString(part.getMax()));

        if (part instanceof Model.InHouse) {
            inHouse.selectedProperty().set(true);
            modifyLastEntry.setText(Integer.toString(((InHouse) part).getMachineID().getValue()));
        } else {
            outsourced.selectedProperty().set(true);
            idCode.setText("Company Name");
            modifyLastEntry.setText(String.valueOf(((Outsourced) part).getCompanyName()));
        }
    }

    //toggle radio buttons
    @FXML
    public void toggleModifyPart(ActionEvent event) throws IOException {
        RadioButton selectedRadioButton = (RadioButton) modifyPartToggleGroup.getSelectedToggle();
        String radioButtonText = selectedRadioButton.getText();
        if (radioButtonText.equals("In-House")) {
            idCode.setText("Machine ID");
        } else {
            idCode.setText("Company Name");
        }
        modifyLastEntry.setText("");
    }

    //save button
    @FXML
    public void modifyPartSave(ActionEvent event) throws IOException {
        Alert errorMessage = new Alert(Alert.AlertType.ERROR);
        errorMessage.initModality(Modality.NONE);

        if (!checkInputTypes()) {
            errorMessage.setHeaderText("Error: missing information or correct value types are needed.");
            errorMessage.setContentText("Please make sure you all of your information is filled out correctly.");
            errorMessage.setTitle("Action Failed");
            errorMessage.showAndWait();
        }
        if (isValid()) {
            int id = Integer.parseInt(modifyPartID.getText());
            String name = modifyPartName.getText();
            int inv = Integer.parseInt(modifyPartStock.getText());
            double price = Double.parseDouble(modifyPartPrice.getText());
            int min = Integer.parseInt(modifyPartMin.getText());
            int max = Integer.parseInt(modifyPartMax.getText());

            RadioButton selectedRadioButton = (RadioButton) modifyPartToggleGroup.getSelectedToggle();
            String radioButtonText = selectedRadioButton.getText();
            if (radioButtonText.equals("In-House")) {
                int machineId = Integer.parseInt(modifyLastEntry.getText());
                //creating a new instance of InHouse and then updating the part that was selected
                Part updatedPart = new InHouse(id, name, price, inv, min, max, machineId);
                Inventory.updatePart(this.partIndex, updatedPart);
            } else {
                String companyName = modifyLastEntry.getText();
                //creating a new instance of Outsourced and then updating the part that was selected
                Part updatedPart = new Outsourced(id, name, price, inv, min, max, companyName);
                Inventory.updatePart(this.partIndex, updatedPart);
            }
            //takes back to main screen
            Parent mainScreenRoot = FXMLLoader.load(getClass().getResource("../resources/MainScreen.fxml"));
            Scene mainScene = new Scene(mainScreenRoot);
            Stage mainScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScene);
            mainScreenStage.show();
        } else {
            //error message for max being less than min
            if (Integer.parseInt(modifyPartMax.getText()) < Integer.parseInt(modifyPartMin.getText())) {
                errorMessage.setHeaderText("Your Minimum value is greater than your Maximum value.");
                errorMessage.setContentText("Please edit your entry.");
                errorMessage.setTitle("Action Failed");
                errorMessage.showAndWait();
            //error message for min being greater than inv
            } else if (Integer.parseInt(modifyPartMin.getText()) > Integer.parseInt(modifyPartStock.getText())) {
                errorMessage.setHeaderText("Your Minimum value is greater than what your Inventory value is.");
                errorMessage.setContentText("Please edit your entry.");
                errorMessage.setTitle("Action Failed");
                errorMessage.showAndWait();
            //error message for inv being greater than max
            } else if (Integer.parseInt(modifyPartStock.getText()) > Integer.parseInt(modifyPartMax.getText())) {
                errorMessage.setHeaderText("Your Inventory value is greater than what your Maximum value is.");
                errorMessage.setContentText("Please edit your entry.");
                errorMessage.setTitle("Action Failed");
                errorMessage.showAndWait();
            //error message for an entry of <= 0 for Price
            } else if (Double.parseDouble(modifyPartPrice.getText()) <= 0) {
                errorMessage.setHeaderText("Error: you must have a value greater than zero for your Price.");
                errorMessage.setContentText("Please edit your entry.");
                errorMessage.showAndWait();
            } else {
                errorMessage.setHeaderText("Error: missing information or correct value types are needed.");
                errorMessage.setContentText("Please make sure you all of your information is filled out correctly.");
                errorMessage.setTitle("Action Failed");
                errorMessage.showAndWait();
            }
        }
    }

    //checks the input types of each text field
    public Boolean checkInputTypes() {
        boolean broken = true;

        try {
            int testID = Integer.parseInt(modifyPartID.getText());
        } catch(NumberFormatException e) {
            broken = false;
        }
        try {
            int testStock = Integer.parseInt(modifyPartStock.getText());
        } catch (NumberFormatException e) {
            broken = false;
        }
        try {
            double testPrice = Double.parseDouble(modifyPartPrice.getText());
        } catch (NumberFormatException e) {
            broken = false;
        }
        try {
            int testMax = Integer.parseInt(modifyPartMax.getText());
        } catch (NumberFormatException e) {
            broken = false;
        }
        try {
            int testMin = Integer.parseInt(modifyPartMin.getText());
        } catch (NumberFormatException e) {
            broken = false;
        }

        return broken;
    }

    //checks each textfield to make sure they are not empty or of the value null
    public Boolean isValid() {
        if (this.modifyPartID.getText().isEmpty() || this.modifyPartID.getText() == null) {
            return false;
        } else if (this.modifyPartName.getText().isEmpty() || this.modifyPartName.getText() == null) {
            return false;
        } else if (this.modifyPartStock.getText().isEmpty() || this.modifyPartStock.getText() == null) {
            return false;
        } else if (this.modifyPartPrice.getText().isEmpty() || this.modifyPartPrice.getText() == null) {
            return false;
        } else if (Double.parseDouble(this.modifyPartPrice.getText()) <= 0) {
            return false;
        } else if (this.modifyPartMax.getText().isEmpty() || this.modifyPartMax.getText() == null) {
            return false;
        } else if (this.modifyPartMin.getText().isEmpty() || this.modifyPartMin.getText() == null) {
            return false;
        } else if (this.modifyLastEntry.getText().isEmpty() || this.modifyLastEntry.getText() == null) {
            return false;
        } else if (Integer.parseInt(this.modifyPartMin.getText()) > Integer.parseInt(this.modifyPartMax.getText())) {
            return false;
        } else if (Integer.parseInt(this.modifyPartStock.getText()) < Integer.parseInt(this.modifyPartMin.getText())
                || Integer.parseInt(this.modifyPartStock.getText()) > Integer.parseInt(this.modifyPartMax.getText())) {
            return false;
        }
        return true;
    }

    //cancel button
    @FXML
    public void modifyPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Action needs validating.");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("All unsaved information will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            //takes back to main screen
            Parent mainScreenRoot = FXMLLoader.load(getClass().getResource("../resources/MainScreen.fxml"));
            Scene mainScene = new Scene(mainScreenRoot);
            Stage mainScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScene);
            mainScreenStage.show();
        }
    }
}