/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Core.Camera;
import Items.ItemType;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class Block {
    protected int x, y;
    protected int width, height;
    protected Color color;
    private String name = "Block";
    private ArrayList<Class> alAllowedNodes = new ArrayList<>();
    private double health;
    private double maxHealth;
    private HashMap<ItemType, Integer> hmNeededItems = new HashMap<>();
    
    private long drawID;
    private boolean deleted = false;
    private final double healthBarHeight = 5.0 / WorldManager.BLOCK_SIZE;
    
    private boolean isBuilding = false;
    private double buildAnimTime = 1.0;
    private Color buildColor = new Color(64, 224, 208, 128);
    private boolean isDestructing = false;
    private double destructAnimTime = 0;
    private Color destructColor = new Color(224, 64, 80, 128);
    
    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 1;
        this.height = 1;
        this.color = Color.MAGENTA;
    }
    
    public void draw(Camera c) {
        if(checkIfDrawn(c)) {
            return;
        }
        
        drawID = c.getDrawID();
        c.fillRect(x, y, width, height, color);
        
        if(maxHealth > 0) {
            c.fillRect(x, y-healthBarHeight, width, healthBarHeight, Color.GRAY);
            c.fillRect(x, y-healthBarHeight, width * (health/(double)maxHealth), healthBarHeight, Color.RED);
        }
        
        if(isDestructing) {
            drawDestructOverlay(c);
        } else if(isBuilding) {
            drawBuildingOverlay(c);
        }
    }
    
    public void drawBuildingOverlay(Camera c) {
        buildAnimTime -= c.getDrawDeltaTime();
        
        c.fillRect(x + width/2.0*(1 - buildAnimTime), y + height/2.0*(1 - buildAnimTime), width * buildAnimTime, height * buildAnimTime, buildColor);
    }
    
    public void drawDestructOverlay(Camera c) {
        destructAnimTime += c.getDrawDeltaTime();
        
        c.fillRect(x + width/2.0*(1 - destructAnimTime), y + height/2.0*(1 - destructAnimTime), width * destructAnimTime, height * destructAnimTime, destructColor);
    }
    
    public void drawIcon(Graphics g, int x, int y, int size) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, size/2, size/2);
        g.fillRect(x+size/2, y+size/2, size/2, size/2);
        g.setColor(Color.MAGENTA);
        g.fillRect(x+size/2, y, size/2, size/2);
        g.fillRect(x, y+size/2, size/2, size/2);
    }
    
    public void damage(double amount, World world) {
        health -= amount;
        
        if(health <= 0) {
            health = 0;
            world.removeBlock(x, y, false);
        }
    }
    
    public void regenerate(double amount) {
        health += amount;
        
        if(health > maxHealth) {
            health = maxHealth;
        }
    }
    
    /**
     * This need to be called if a block is being deleted 
     */
    public void deleteBlock() {
        deleted = true;
    }
    
    /**
     * Add a node to the allowed list, which forces this block to be placed on this node.
     * @param pClass 
     */
    protected void addAllowedNode(Class pClass) {
        alAllowedNodes.add(pClass);
    }
    
    protected void initBlock(String name, double maxHealth, int width, int height, Color color) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.height = height;
        this.width = width;
        this.color = color;
    }
    
    protected void addNeededItem(ItemType itemType, int quantity) {
        if(quantity > 0) {
            hmNeededItems.put(itemType, quantity);
        }
    }
    
    /**
     * This method should only be used by the menu manager, to either draw a preview block or place a block!
     * @param x
     * @param y 
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * 
     * @param block
     * @return Returns a boolean indicating if this block can be placed on another block.
     */
    public boolean isPlacableOn(Block block) {
        return alAllowedNodes.contains(block.getClass());
    }
    
    public boolean isNodesOnly() {
        return !alAllowedNodes.isEmpty();
    }
    
    /**
     * This checks if a block was already drawn in that frame.
     * @param c
     * @return 
     */
    protected boolean checkIfDrawn(Camera c) {
        return c.getDrawID() == drawID;
    }
    
    public void completeBuilding() {
        this.isBuilding = false;
    }
    
    public void build() {
        this.isBuilding = true;
    }

    public void destruct() {
        this.isDestructing = true;
    }

    public boolean isDestructing() {
        return isDestructing;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public HashMap<ItemType, Integer> getHmNeededItems() {
        return hmNeededItems;
    }
}
