package InventoryGUI;

/**
 * @author Jeremy Hennessy
 * class inherits part class and implements a Class for Inhouse parts
 */
public class InHouse extends Part{
    private int machineId;

    /**
     * Constructor
     * @param id to be set
     * @param name to be set
     * @param price to be set
     * @param stock to be set
     * @param min to be set
     * @param max to be set
     * @param machineId to be set
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     *
     * @param machineId to set
     *
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     *
     * @return machineId
     */
    public int getMachineId() {
        return machineId;
    }
}
