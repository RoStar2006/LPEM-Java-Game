/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Items.Inventory;
import Core.MainLogic;
import Items.ItemType;
import Utilities.Utils;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class UpdatedBlock extends Block {
    private Inventory inventory;
    
    public UpdatedBlock(int x, int y) {
        super(x, y);
        initBlock("Updated Block", 0, 1, 1, Color.YELLOW);
    }
    
    /*
        Test code
    */
    private double waitTime = 1;
    private double curTime = 0;
    
    /**
     * This is called every {@link MainLogic#update()} tick and is the core component of an {@link UpdatedBlock}
     * @param deltaTime 
     */
    public void update(double deltaTime) {
        curTime += deltaTime;
        
        if(curTime >= waitTime) {
            waitTime = Utils.nextDouble(0.2, 0.6);
            curTime = 0;
            color = new Color(Utils.nextInt(0, 256), Utils.nextInt(0, 256), Utils.nextInt(0, 256));
        }
    }
    
    protected void createInventory(int maxStackSize, int maxSize) {
        inventory = new Inventory(maxStackSize, maxSize);
    }
    
    protected void setMaxStackSize(int maxStackSize) {
        if(inventory != null) {
            inventory.setMaxStackSize(maxStackSize);
        }
    }
    
    protected void addWhitelistItem(ItemType itemType) {
        if(inventory != null) {
            inventory.addWhitelist(itemType);
        }
    }
    
    /**
     * This method accesses the {@link Inventory#addItem(Items.Item, int)} method.
     * @see Inventory#addItem(Items.Item, int)
     * @param itemType
     * @param quantity
     * @return The output of the {@link Inventory#addItem(Items.Item, int)} method or -1 if not inventory exists.
     */
    public int addItem(ItemType itemType, int quantity) {
        if(inventory == null) {
            return -1;
        }
        return inventory.addItem(itemType, quantity);
    }
    
    /**
     * This method accesses the {@link Inventory#removeItem(Items.Item, int)} method.
     * @see Inventory#removeItem(Items.Item, int)
     * @param itemType
     * @param quantity
     * @return The output of the {@link Inventory#removeItem(Items.Item, int)} method or -1 if not inventory exists.
     */
    public int removeItem(ItemType itemType, int quantity) {
        if(inventory == null) {
            return -1;
        }
        return inventory.removeItem(itemType, quantity);
    }
    
    /**
     * This method accesses the {@link Inventory#isWhitelisted(Items.Item)} method.
     * @see Inventory#isWhitelisted(Items.Item)
     * @param itemType
     * @return The output of the {@link Inventory#isWhitelisted(Items.Item)} method or false if not inventory exists.
     */
    public boolean isItemWhitelisted(ItemType itemType) {
        if(inventory == null) {
            return false;
        }
        return inventory.isWhitelisted(itemType);
    }
    
    public boolean containsItems(ItemType itemType, int quantity) {
        return inventory.containsItems(itemType, quantity);
    }
    
    public HashMap<ItemType, Integer> getInventory() {
        if(inventory == null) {
            return new HashMap<>();
        }
        return inventory.getHmItems();
    }
    
    public boolean hasInventory() {
        return inventory != null;
    }
    
}
