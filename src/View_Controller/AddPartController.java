package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class AddPartController {
    @FXML private ToggleGroup addPartToggleGroup;
    @FXML private Label idCode;
    @FXML private TextField addPartID;
    @FXML private TextField addPartName;
    @FXML private TextField addPartInv;
    @FXML private TextField addPartLastEntry;
    @FXML private TextField addPartMax;
    @FXML private TextField addPartMin;
    @FXML private TextField addPartPrice;

    public void initPartID() {
        addPartID.setText(String.valueOf(Inventory.getPartIDCount() + 1));
        addPartID.setEditable(false);
    }

    //toggle radio button
    public void toggleAddPart(ActionEvent event) throws IOException {
        RadioButton selectedRadioButton = (RadioButton) addPartToggleGroup.getSelectedToggle();
        String radioButtonText = selectedRadioButton.getText();
        if (radioButtonText.equals("In-House")) {
            idCode.setText("Machine ID");
        } else {
            idCode.setText("Company Name");
        }
        addPartID.setEditable(false);
        addPartLastEntry.setText("");
    }

    //save button
    @FXML
    public void addPartSave(ActionEvent event) throws IOException {
        Alert errorMessage = new Alert(Alert.AlertType.ERROR);
        errorMessage.initModality(Modality.NONE);

        if (!checkInputTypes()) {
            errorMessage.setHeaderText("Error: missing information or correct value types are needed.");
            errorMessage.setContentText("Please make sure you all of your information is filled out correctly.");
            errorMessage.setTitle("Action Failed");
            errorMessage.showAndWait();
        } else {
            if (isValid()) {
                //add one to the partID count for the new part
                Inventory.setPartIDCount();
                //Return the updated part ID count value from Inventory class
                int partID = Inventory.getPartIDCount();
                String partName = addPartName.getText();
                int partStock = Integer.parseInt(addPartInv.getText());
                double partPrice = Double.parseDouble(addPartPrice.getText());
                int partMin = Integer.parseInt(addPartMin.getText());
                int partMax = Integer.parseInt(addPartMax.getText());

                RadioButton selectedRadioButton = (RadioButton) addPartToggleGroup.getSelectedToggle();
                String radioButtonText = selectedRadioButton.getText();
                if (radioButtonText.equals("In-House")) {
                    int partMachineID = Integer.parseInt(addPartLastEntry.getText());
                    //creating new instance of InHouse and adding it to the Inventory parts list
                    InHouse addPart = new InHouse(partID, partName, partPrice, partStock, partMin, partMax, partMachineID);
                    Inventory.addPart(addPart);
                } else {
                    String companyName = addPartLastEntry.getText();
                    //creating new instance of Outsourced and adding it to the Inventory parts list
                    Outsourced addPart = new Outsourced(partID, partName, partPrice, partStock, partMin, partMax,companyName);
                    Inventory.addPart(addPart);
                }
                //takes me back to the main screen
                Parent mainScreenRoot = FXMLLoader.load(getClass().getResource("../resources/MainScreen.fxml"));
                Scene mainScene = new Scene(mainScreenRoot);
                Stage mainScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainScreenStage.setScene(mainScene);
                mainScreenStage.show();
            } else {
                //error message for max being less than min
                if (Integer.parseInt(addPartMax.getText()) < Integer.parseInt(addPartMin.getText())) {
                    errorMessage.setHeaderText("Your Minimum value is greater than your Maximum value.");
                    errorMessage.setContentText("Please edit your entry.");
                    errorMessage.setTitle("Action Failed");
                    errorMessage.showAndWait();
                //error message for min being greater than inv
                } else if (Integer.parseInt(addPartMin.getText()) > Integer.parseInt(addPartInv.getText())) {
                    errorMessage.setHeaderText("Your Minimum value is greater than what your Inventory value is.");
                    errorMessage.setContentText("Please edit your entry.");
                    errorMessage.setTitle("Action Failed");
                    errorMessage.showAndWait();
                //error message for inv being greater than max
                } else if (Integer.parseInt(addPartInv.getText()) > Integer.parseInt(addPartMax.getText())) {
                    errorMessage.setHeaderText("Your Inventory value is greater than what your Maximum value is.");
                    errorMessage.setContentText("Please edit your entry.");
                    errorMessage.setTitle("Action Failed");
                    errorMessage.showAndWait();
                //error message for an entry of <= 0 for Price
                } else if (Double.parseDouble(addPartPrice.getText()) <= 0) {
                    errorMessage.setHeaderText("Error: you must have a value greater than zero for your Price.");
                    errorMessage.setContentText("Please edit your entry.");
                    errorMessage.showAndWait();
                } else {
                    errorMessage.setHeaderText("Error: missing information or correct value types are needed.");
                    errorMessage.setContentText("Please make sure all of the information is filled out correctly");
                    errorMessage.showAndWait();
                }
            }
        }

    }

    //checks the input types of each text field
    public Boolean checkInputTypes() {
        boolean broken = true;

        try {
            int testStock = Integer.parseInt(addPartInv.getText());
        } catch (NumberFormatException e) {
            broken = false;
        }
        try {
            double testPrice = Double.parseDouble(addPartPrice.getText());
        } catch (NumberFormatException e) {
            broken = false;
        }
        try {
            int testMax = Integer.parseInt(addPartMax.getText());
        } catch (NumberFormatException e) {
            broken = false;
        }
        try {
            int testMin = Integer.parseInt(addPartMin.getText());
        } catch (NumberFormatException e) {
            broken = false;
        }

        return broken;
    }

    //checks each textfield to make sure they are not empty or of the value null
   public Boolean isValid() {
        if (this.addPartID.getText().isEmpty() || this.addPartID.getText() == null) {
            return false;
        }
        if (this.addPartName.getText().isEmpty() || this.addPartName.getText() == null) {
            return false;
        } else if (this.addPartInv.getText().isEmpty() || this.addPartInv.getText() == null) {
            return false;
        } else if (this.addPartPrice.getText().isEmpty() || this.addPartPrice.getText() == null) {
            return false;
        } else if (Double.parseDouble(this.addPartPrice.getText()) <= 0) {
            return false;
        } else if (this.addPartMax.getText().isEmpty() || this.addPartMax.getText() == null) {
            return false;
        } else if (this.addPartMin.getText().isEmpty() || this.addPartMin.getText() == null) {
            return false;
        }else if (this.addPartLastEntry.getText().isEmpty() || this.addPartLastEntry.getText() == null) {
            return false;
        } else if (Integer.parseInt(this.addPartMin.getText()) > Integer.parseInt(this.addPartMax.getText())) {
            return false;
        } else if (Integer.parseInt(this.addPartInv.getText()) < Integer.parseInt(this.addPartMin.getText())
                || Integer.parseInt(this.addPartInv.getText()) > Integer.parseInt(this.addPartMax.getText())) {
            return false;
        }
        return true;
   }

    //cancel button
    @FXML
    public void addPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Action needs validating.");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("All unsaved information will be lost.");
        Optional <ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            //once confirmed, takes me back to main screen
            Parent mainScreenRoot = FXMLLoader.load(getClass().getResource("../resources/MainScreen.fxml"));
            Scene mainScene = new Scene(mainScreenRoot);
            Stage mainScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScene);
            mainScreenStage.show();
        }
    }
}
