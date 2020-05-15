package Model;

/*
- this class is responsible for creating instances of Products
- it contains an ArrayList of the type Part which allows it to create, delete, and return any of the associated parts
*/

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.util.ArrayList;

public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private IntegerProperty productID;
    private StringProperty productName;
    private DoubleProperty productPrice;
    private IntegerProperty productStock;
    private IntegerProperty productMin;
    private IntegerProperty productMax;

    public Product(int productID, String productName, double productPrice, int productStock, int productMin, int productMax) {
        this.productID = new SimpleIntegerProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.productPrice = new SimpleDoubleProperty(productPrice);
        this.productStock = new SimpleIntegerProperty(productStock);
        this.productMin = new SimpleIntegerProperty(productMin);
        this.productMax = new SimpleIntegerProperty(productMax);
    }
    public void setId(int productID) {
        this.productID.set(productID);
    }
    public void setName(String productName) {
        this.productName.set(productName);
    }
    public void setPrice(double productPrice) {
        this.productPrice.set(productPrice);
    }
    public void setStock(int productStock) {
        this.productStock.set(productStock);
    }
    public void setMin(int productMin) {
        this.productMin.set(productMin);
    }
    public void setMax(int productMax) {
        this.productMax.set(productMax);
    }
    public int getID() {
        return this.productID.get();
    }
    public String getName() {
        return this.productName.get();
    }
    public double getPrice() {
        return this.productPrice.get();
    }
    public int getStock() {
        return this.productStock.get();
    }
    public int getMin() {
        return this.productMin.get();
    }
    public int getMax() {
        return this.productMax.get();
    }
    public IntegerProperty productIDProperty() {
        return productID;
    }
    public StringProperty productNameProperty() {
        return productName;
    }
    public DoubleProperty productPriceProperty() {
        return productPrice;
    }
    public IntegerProperty productStockProperty() {
        return productStock;
    }
    public IntegerProperty productMinProperty() {
        return productMin;
    }
    public IntegerProperty productMaxProperty() {
        return productMax;
    }

    /* using the associatedParts ArrayList */
    public void addAssociatedPart(Part newPart) {
        associatedParts.add(newPart);
    }

    public boolean deleteAssociatedPart(Part selectedApart) {
        if (associatedParts.contains(selectedApart)) {
            associatedParts.remove(selectedApart);
            return true;
        } else {
            return false;
        }
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
