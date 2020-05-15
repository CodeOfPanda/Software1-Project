package Model;

/*
-this class is the parent class to both InHouse and Outsourced
-responsible for creating new instances of parts
*/

import javafx.beans.property.*;

public abstract class Part {
    //being protected allows these instance variables to be accessed by subclasses
    protected IntegerProperty partID;
    protected StringProperty partName;
    protected DoubleProperty partPrice;
    protected IntegerProperty partStock;
    protected IntegerProperty partMin;
    protected IntegerProperty partMax;

    public Part(int partID, String partName, double partPrice, int partStock, int partMin, int partMax) {
        this.partID = new SimpleIntegerProperty(partID);
        this.partName = new SimpleStringProperty(partName);
        this.partPrice = new SimpleDoubleProperty(partPrice);
        this.partStock = new SimpleIntegerProperty(partStock);
        this.partMin = new SimpleIntegerProperty(partMin);
        this.partMax = new SimpleIntegerProperty(partMax);
    }

    public void setId(int partID) {
        this.partID.set(partID);
    }
    public void setName(String partName) {
        this.partName.set(partName);
    }
    public void setPrice(double partPrice) {
        this.partPrice.set(partPrice);
    }
    public void setStock(int partStock) {
        this.partStock.set(partStock);
    }
    public void setMin(int partMin) {
        this.partMin.set(partMin);
    }
    public void setMax(int partMax) {
        this.partMax.set(partMax);
    }
    public int getID() {
        return this.partID.get();
    }
    public String getName() {
        return this.partName.get();
    }
    public double getPrice() {
        return this.partPrice.get();
    }
    public int getStock() {
        return this.partStock.get();
    }
    public int getMin() {
        return this.partMin.get();
    }
    public int getMax() {
        return this.partMax.get();
    }
    public IntegerProperty partIDProperty() {
        return partID;
    }
    public StringProperty nameProperty() {
        return partName;
    }
    public DoubleProperty priceProperty() {
        return partPrice;
    }
    public IntegerProperty inStockProperty() { return partStock; }
    public IntegerProperty minProperty() {
        return partMin;
    }
    public IntegerProperty maxProperty() {
        return partMax;
    }
}
