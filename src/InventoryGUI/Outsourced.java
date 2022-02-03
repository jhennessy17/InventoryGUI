package InventoryGUI;

/**
 * @author Jeremy Hennessy
 */
public class Outsourced extends Part {
    private String companyName;

    /**
     * Constructor
     * @param id to be set
     * @param name to be set
     * @param price to be set
     * @param stock to be set
     * @param min to be set
     * @param max to be set
     * @param companyName to be set
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     *
     * @param companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     *
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }
}
