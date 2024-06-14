/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Base;

import Blocks.Base.BlockType;
import Blocks.Base.UpdatedBlock;
import Core.Camera;
import Items.ItemDrawer;
import Items.ItemType;
import World.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 *
 * @author Romeo
 */
public class ConveyorBelt extends UpdatedBlock {
    private final int maxItems = 4;
    private ArrayList<ItemType> alItemOrder = new ArrayList<>();
    private ArrayList<Double> alItemTime = new ArrayList<>();
    private double maxCooldown = 60.0 / 180; // seconds/item
    private double traverseTime = maxCooldown*maxItems;
    private double curCooldown = 0;
    
    public ConveyorBelt(int x, int y) {
        super(x, y);
        setDirection(new Point(0, -1));
        initBlock("Conveyor belt", 15, 1, 1, Color.GREEN, Color.MAGENTA);
        setBlockType(BlockType.ItemLogistic);
        createInventory(maxItems, maxItems, false);
    }
    
    protected final void setTraverseTime(int itemPerMinute) {
        maxCooldown = 60.0/itemPerMinute; // seconds/item
        traverseTime = maxCooldown*maxItems;
    }

    @Override
    public void update(double deltaTime, World world) {
        if(curCooldown > 0) {
            curCooldown -= deltaTime;
        }
        
        for(int i = 0; i < alItemTime.size(); i++) {
            double curTime = alItemTime.get(i);
            
            if(curTime > maxCooldown*i) {
                alItemTime.set(i, curTime-deltaTime);
            }
        }
        
        UpdatedBlock updatedBlock = getInputBlock(world, getDirection().x, getDirection().y);
        if(!alItemOrder.isEmpty() && alItemTime.get(0) <= 0 && updatedBlock != null) { // Check if any items needs to be transported, if the tranfer time reached 0 and a receiving block is available
            int newQuantity = updatedBlock.addItem(alItemOrder.get(0), 1);
            if(newQuantity == 0) {
                removeItem(alItemOrder.get(0), 1);
                alItemOrder.remove(0);
                alItemTime.remove(0);
            }
        }
    }

    @Override
    protected void drawIcon(Graphics g, int x, int y, int width, int height) {
        super.drawIcon(g, x, y, width, height);
    }

    @Override
    public void delayedDraw(Camera c, World world) {
        double size = 0.25;
        double cX = x+width/2.0;
        double cY = y+height/2.0;
        for(int i = alItemTime.size()-1; i >= 0; i--) {
            if (i < alItemTime.size()) {
                double offsetTime = (traverseTime/2 - alItemTime.get(i))/traverseTime;
                ItemDrawer.drawItem(c, alItemOrder.get(i), cX+getDirection().x*offsetTime, cY+getDirection().y*offsetTime, size);
            } else {
                i = alItemOrder.size()-1;
            }
        }
    }

    @Override
    public int addItem(ItemType itemType, int quantity) {
        if(alItemOrder.size() >= 4 || curCooldown > 0) {
            return quantity;
        }
        curCooldown += maxCooldown;
        alItemOrder.add(itemType);
        alItemTime.add(traverseTime);
        
        return quantity-1 + super.addItem(itemType, 1);
    }
    
    /**
     * 
     * @return Items per minute
     */
    public double getSpeed() {
        return 1/maxCooldown;
    }
    
}
