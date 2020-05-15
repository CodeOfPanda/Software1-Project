package Model;

/*
-this class is responsible for tracking inventory
-it contains to ArrayLists one of type Part and other of type Product
-
*/

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static int partIDCount = 0;
    private static int productIDCount = 0;

    /* PARTS */
    //adding an instance of either In-House or Outsourced to the Observable list allParts
    public static void addPart(Part newPart) {
        //adds a new part onto the selected ArrayList
        allParts.add(newPart);
    }

    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public static Part lookupPartID(int partID) {
        for (Part part : allParts) {
            if (part.getID() == partID) {
                return part;
            }
        }
        return null;
    }

    public static ArrayList lookupPartName(String partName) {
       ArrayList<Part> foundPartNames = new ArrayList<>();
       for (int i = 0; i < allParts.size(); i++) {
           if (allParts.get(i).getName().contains(partName)) {
               foundPartNames.add(allParts.get(i));
           }
       }
       return foundPartNames;
    }

    public static ObservableList <Part> getAllParts() {
        return allParts;
    }

    public static int getPartIDCount() {
        return partIDCount;
    }

    public static void setPartIDCount() {
        partIDCount += 1;
    }

    /* PRODUCTS */

    //adding an instance of the class Product to the Observable list allProducts
    public static void addProduct(Product newProduct) {
        //adds a new product onto the selected ArrayList
        allProducts.add(newProduct);
    }

    //returns a bool value depending on if the product was deleted
    public static boolean deleteProduct (Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        } else {
            return false;
        }
    }
    public static Product lookupProductID(int productId) {
        for (int i=0; i<allProducts.size(); i++) {
            if (allProducts.get(i).getID() == productId) {
                return allProducts.get(i);
            }
        }
        return null;
    }
    public static ArrayList lookupProductName(String productName) {
        ArrayList<Product> foundProductNames = new ArrayList<>();
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getName().contains(productName)) {
                foundProductNames.add(allProducts.get(i));
            }
        }
        return foundProductNames;
    }
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }
    public static ObservableList <Product> getAllProducts() {
        return allProducts;
    }

    public static void setProductIDCount() {
        productIDCount++;
    }

    public static int getProductIDCount() {
        return productIDCount;
    }
}
