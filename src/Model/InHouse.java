package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/*
-this is a subclass of Part
-its purpose is to add a machine ID to a part
*/
public class InHouse extends Part {
    private IntegerProperty machineID;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);

        this.machineID = new SimpleIntegerProperty(machineID);
    }
    public void setMachineID (int machineID) {
        this.machineID.set(machineID);
    }
    public IntegerProperty getMachineID() {
        return machineID;
    }
}
