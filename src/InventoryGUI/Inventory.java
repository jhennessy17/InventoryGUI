package InventoryGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Jeremy Hennessy
 * Creates an Inventory of parts and products using the part and product classes
 */
public class Inventory{
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @param newPart to add to allProducts
     */
    public void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     *
     * @param newProduct to add to allProducts
     */
    public void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     *Looks up the given part associated with the
     * given partId
     * @param partId partId of given part to lookup
     * @return the part with the given partId or null if not found
     */
    public Part lookupPart(int partId){
        //find part and return
        for(Part part : allParts){
            if(part.getId() == partId)
                return part;
        }
        return null;
    }

    /**
     *Looks up the given product associated with the
     * given partId
     * @param productId productId of given product to lookup
     * @return the product with the given partId or null if not found
     */
    public Product lookupProduct(int productId){
        //find product and return
        for(Product product: allProducts){
            if(product.getId() == productId)
                return product;
        }
        return null;
    }


    /**
     * Accepts a string and finds all parts that begin with that string or
     * are equivalent to that string
     * @param partName the given partName used to find the part
     * @return Observable list with all parts that have a name that begins with the
     * string partName or is equal to the string partName
     */
    public ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> parts = FXCollections.observableArrayList();
        for(Part part : allParts){
            if(part.getName().toLowerCase().startsWith(partName))
                parts.add(part);
        }
        return parts;
    }

    /**
     * Accepts a string and finds all products that begin with that string or
     * are equivalent to that string
     * @param productName the given productName used to find the product
     * @return Observable list with all products that have a name that begins with the
     * string productName or is equal to the string productName
     */
    public ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> products = FXCollections.observableArrayList();
        for(Product product : allProducts){
            if(product.getName().toLowerCase().startsWith(productName))
                products.add(product);
        }
        return products;
    }

    /**
     * Updates a parts contents
     * FUTURE ENHANCEMENT Find a way to not use the ObservableList set method to solve this function
     * LOGICAL ERROR I had difficulty making sure the part could be switched from InHouse to Outsourced and vice versa
     * @param partId used to find the part to update
     * @param newPart used to update the part
     */
    public void updatePart(int partId, Part newPart){
        /*
        This code operates correctly when allParts.set(i, newPart) is used instead of allParts.set(partId, newPart)
        Doesn't make sense to me since both values would be the same
        I assume the other commented out code is the preferred solution here but I was not able modify whether the product
        was inHouse or Outsourced with that code
        */
        for(int i = 0; i < allParts.size(); i++){
            if(allParts.get(i).getId() == partId){
                allParts.set(i, newPart);
                break;
            }
        }
        /*
        for(Part part : allParts){
            if(part.getId() == partId){
                part.setName(newPart.getName());
                part.setPrice(newPart.getPrice());
                part.setStock(newPart.getStock());
                part.setMax(newPart.getMax());
                part.setMin(newPart.getMin());

                try{
                    ((Outsourced)part).setCompanyName(((Outsourced)newPart).getCompanyName());
                }catch(Exception ignored){}
                try{
                    ((InHouse)part).setMachineId(((InHouse)newPart).getMachineId());
                }catch(Exception ignored){ }

                break;
            }
        }*/
    }

    /**
     * Update the contents of a product
     * @param productId of the product to update
     * @param newProduct contains the contents of the product that needs updated
     */
    public void updateProduct(int productId, Product newProduct){
        for(Product product : allProducts){
            if(product.getId() == productId){
                product.setName(newProduct.getName());
                product.setPrice(newProduct.getPrice());
                product.setStock(newProduct.getStock());
                product.setMax(newProduct.getMax());
                product.setMin(newProduct.getMin());
                break;
            }
        }

    }


    /**
     * Deletes the part from the inventory
     * @param selectedPart the part to be deleted
     */
    public void deletePart(Part selectedPart){
        allParts.remove(selectedPart);
    }

    /**
     * Deletes the product from the inventory
     * @param selectedProduct the product to be deleted
     */
    public void deleteProduct(Product selectedProduct){
        allProducts.remove(selectedProduct);
    }

    /**
     *
     * @return allParts from the inventory
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     *
     * @return allProducts from the inventory
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
