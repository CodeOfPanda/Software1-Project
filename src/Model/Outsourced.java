package Model;

/*
-this is a subclass of Part
-its purpose is to add a company name to a part
*/

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Outsourced extends Part{
    private StringProperty companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = new SimpleStringProperty(companyName);
    }
    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }
    public String getCompanyName() {
        return this.companyName.get();
    }
}
