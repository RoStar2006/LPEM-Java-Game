/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Base;

import Core.Camera;
import Core.MainLogic;
import Items.ItemDrawer;
import Items.ItemType;
import World.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Romeo
 */
public class ConveyorBelt extends UpdatedBlock {
    private final int maxItems = 4;
    private final Color conveyorStripesColor = Color.BLACK;
    private final double conveyorStripesThickness = 0.05;
    private final Color conveyorBorderColor = Color.DARK_GRAY;
    private final double conveyorBorderThickness = conveyorStripesThickness*2;
    private ArrayList<ItemType> alItemOrder = new ArrayList<>();
    private ArrayList<Double> alItemTime = new ArrayList<>();
    private ArrayList<Point> alItemDirection = new ArrayList<>();
    private double maxCooldown = 60.0 / 180; // seconds/item
    private double traverseTime = maxCooldown*maxItems;
    private double curCooldown = 0;
    
    public ConveyorBelt(int x, int y) {
        super(x, y);
        setDirection(new Point(0, -1));
        initBlock("Conveyor belt", 0.01, 15, 1, 1, Color.GREEN, Color.MAGENTA);
        setBlockType(BlockType.ItemLogistic);
        createInventory(maxItems, maxItems, false);
    }
    
    protected final void setTravelSpeed(int itemPerMinute) {
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
            int newQuantity = updatedBlock.addItem(alItemOrder.get(0), 1, this);
            if(newQuantity == 0) {
                removeItem(alItemOrder.get(0), 1);
                alItemOrder.remove(0);
                alItemTime.remove(0);
                alItemDirection.remove(0);
            }
        }
    }

    @Override
    protected void drawIcon(Graphics g, int x, int y, int width, int height) {
        super.drawRawIcon(g, x, y, width, height);
    }

    @Override
    public void delayedDraw(Camera c, World world) {
        drawConveyorLines(c, world);
        
        double size = 0.33;
        double cX = x+width/2.0;
        double cY = y+height/2.0;
        
        for(int i = alItemTime.size()-1; i >= 0; i--) {
            if (i < alItemTime.size() && i < alItemOrder.size()) {
                ItemType itemType = alItemOrder.get(i);
                double time = alItemTime.get(i);
                
                if(!Double.isNaN(time) && itemType != null) {
                    double offsetTime = 0.5 - time/traverseTime;
                    if(offsetTime <= 0 && i < alItemDirection.size()) {
                        ItemDrawer.drawItem(c, itemType, cX+alItemDirection.get(i).x*offsetTime, cY+alItemDirection.get(i).y*offsetTime, size);
                    } else {
                        ItemDrawer.drawItem(c, itemType, cX+getDirection().x*offsetTime, cY+getDirection().y*offsetTime, size);
                    }
                }
            } else {
                i = alItemTime.size()-1;
            }
        }
    }
    
    private void drawConveyorLines(Camera c, World world) {
        double curAnimTime = (MainLogic.getCurConveyorAnim() % traverseTime/2)/(traverseTime/2);
        if(getDirection().y < 0 || getDirection().x < 0) {
            curAnimTime = 1 - curAnimTime;
        }
        double yOffset = Math.abs(getDirection().y*curAnimTime);
        double xOffset = Math.abs(getDirection().x*curAnimTime);

        c.setColor(conveyorStripesColor);
        if(yOffset != 0) {
            c.fillRect(x, y+yOffset, 1, conveyorStripesThickness);
            c.fillRect(x, y+((yOffset + 0.5) % 1), 1, conveyorStripesThickness);
            c.setColor(conveyorBorderColor);
            c.fillRect(x, y, conveyorBorderThickness, 1);
            c.fillRect(x+1-conveyorBorderThickness, y, conveyorBorderThickness, 1);
        }
        if(xOffset != 0) {
            c.fillRect(x+xOffset, y, conveyorStripesThickness, 1);
            c.fillRect(x+((xOffset + 0.5) % 1), y, conveyorStripesThickness, 1);
            c.setColor(conveyorBorderColor);
            c.fillRect(x, y, 1, conveyorBorderThickness);
            c.fillRect(x, y+1-conveyorBorderThickness, 1, conveyorBorderThickness);
        }
    }

    @Override
    public int addItem(ItemType itemType, int quantity, UpdatedBlock block) {
        if(alItemOrder.size() >= 4 || curCooldown > 0) {
            return quantity;
        }
        curCooldown += maxCooldown;
        alItemOrder.add(itemType);
        alItemTime.add(traverseTime);
        alItemDirection.add(new Point(getX()-block.getX(), getY()-block.getY()));
        
        return quantity-1 + super.addItem(itemType, 1, block);
    }
    
    /**
     * 
     * @return Items per minute
     */
    public double getSpeed() {
        return 1/maxCooldown;
    }
    
}
