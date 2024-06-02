/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks;

import Core.Camera;
import Items.Inventory;
import Core.MainLogic;
import Items.ItemType;
import World.World;
import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public abstract class UpdatedBlock extends Block {
    private Inventory inventory;
    private long delayedDrawID;
    private Point direction = null;
    private boolean exitArrowStyle = true;
    
    public UpdatedBlock(int x, int y) {
        super(x, y);
        initBlock("Updated Block", 0, 1, 1, Color.YELLOW);
    }
    
    /**
     * This is called every {@link MainLogic#update()} tick and is the core component of an {@link UpdatedBlock}
     * @param deltaTime 
     * @param world 
     */
    public abstract void update(double deltaTime, World world);
    
    public void delayedDraw(Camera c, World world) {
        if(checkIfDelayedDrawn(c)) {
            return;
        }
        
        delayedDrawID = c.getDelayedDrawID();
    }
    
    /**
     * This method returns an {@link UpdatedBlock} if items can be put into it. If not, then it returns null
     * @param world
     * @param offsetX
     * @param offsetY
     * @return
     */
    protected UpdatedBlock getInputBlock(World world, int offsetX, int offsetY) {
        if(Math.abs(offsetX) != Math.abs(offsetY)) { // Not the corners or the center
            Block block = world.getBlock(x+offsetX, y+offsetY);

            if(block instanceof UpdatedBlock updatedBlock && !updatedBlock.isOutputOnly() && block.isActive()) { // Check if input is allowed and block is ready
                if(updatedBlock instanceof ConveyorBelt conveyorBelt) { // Check if the conveyor belt is facing into the block
                    if(!(conveyorBelt.getDirection().x == -offsetX && conveyorBelt.getDirection().y == -offsetY)) {
                        return updatedBlock;
                    }
                } else if(updatedBlock instanceof Splitter splitter) { // Check if the splitter input is facing the block
                    if(splitter.getDirection().x == offsetX && splitter.getDirection().y == offsetY) {
                        return updatedBlock;
                    }
                } else {
                    return updatedBlock;
                }
            }
        }        
        
        return null;
    }
    
    /**
     * This checks if a block was already delayed drawn in that frame.
     * @param c
     * @return 
     */
    protected boolean checkIfDelayedDrawn(Camera c) {
        return c.getDelayedDrawID() == delayedDrawID;
    }
    
    protected void createInventory(int maxStackSize, int maxSize, boolean outputOnly) {
        inventory = new Inventory(maxStackSize, maxSize, outputOnly);
    }
    
    protected void setMaxStackSize(int maxStackSize) {
        if(inventory != null) {
            inventory.setMaxStackSize(maxStackSize);
        }
    }
    
    protected void setMaxSize(int maxSize) {
        if(inventory != null) {
            inventory.setMaxSize(maxSize);
        }
    }
    
    protected void addWhitelistItem(ItemType itemType) {
        if(inventory != null) {
            inventory.addWhitelist(itemType);
        }
    }
    
    /**
     * This method accesses the {@link Inventory#addItem(Items.Item, int)} method, however it bypasses the output only.
     * @see Inventory#addItem(Items.Item, int)
     * @param itemType
     * @param quantity
     * @return The output of the {@link Inventory#addItem(Items.Item, int)} method or -1 if not inventory exists.
     */
    protected int forceAddItem(ItemType itemType, int quantity) {
        if(inventory == null) {
            return -1;
        }
        return inventory.addItem(itemType, quantity);
    }
    
    /**
     * This method accesses the {@link Inventory#addItem(Items.Item, int)} method. It only adds items if the inventory is not set to output only.
     * @see Inventory#addItem(Items.Item, int)
     * @param itemType
     * @param quantity
     * @return The output of the {@link Inventory#addItem(Items.Item, int)} method or -1 if not inventory exists.
     */
    public int addItem(ItemType itemType, int quantity) {
        if(inventory == null) {
            return -1;
        } else if(inventory.isOutputOnly()) {
            return quantity;
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
        if(inventory == null) {
            return false;
        }
        return inventory.containsItems(itemType, quantity);
    }
    
    public boolean isOutputOnly() {
        if(inventory == null || !isActive()) {
            return true;
        }
        return inventory.isOutputOnly();
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
    
    public void setDirection(Point direction) {
        this.direction = direction;
    }

    public Point getDirection() {
        return direction;
    }
    
    public boolean hasDirection() {
        return direction != null;
    }

    public boolean isExitArrowStyle() {
        return exitArrowStyle;
    }

    public void setExitArrowStyle(boolean exitArrowStyle) {
        this.exitArrowStyle = exitArrowStyle;
    }
    
}
