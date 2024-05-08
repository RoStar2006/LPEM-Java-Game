/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Core.Camera;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Romeo
 */
public class Block {
    protected int x, y;
    protected int width, height;
    protected Color color;
    private ArrayList<Class> alAllowedBlocks = new ArrayList<>();
    private long drawID;
    private boolean deleted = false;

    public Block(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }
    
    /**
     * Add a block to the allowed list, which allows this block to be placed on those blocks.
     * This should only be used for nodes, which are saved in an other list, separated from the blocks.
     * @param pClass 
     */
    protected void addAllowedBlock(Class pClass) {
        alAllowedBlocks.add(pClass);
    }
    
    /**
     * 
     * @param block
     * @return Returns a boolean indicating if this block can be placed on another block.
     */
    public boolean isPlacableOn(Block block) {
        return alAllowedBlocks.contains(block.getClass());
    }
    
    public void draw(Camera c) {
        if(checkIfDrawn(c)) {
            return;
        }
        drawID = c.getDrawID();
        c.fillRect(x, y, width, height, color);
    }
    
    /**
     * This checks if a block was already drawn in that frame.
     * @param c
     * @return 
     */
    protected boolean checkIfDrawn(Camera c) {
        return c.getDrawID() == drawID;
    }
    
    /**
     * This need to be called if a block is being deleted 
     */
    public void deleteBlock() {
        deleted = true;
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
}
