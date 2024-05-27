/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks;

import Core.Camera;
import Items.ItemDrawer;
import Items.ItemType;
import World.World;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Romeo
 */
public class ConveyorBelt extends UpdatedBlock {
    private final int maxItems = 4;
    private ArrayList<ItemType> alItemOrder = new ArrayList<>();
    private ArrayList<Double> alItemTime = new ArrayList<>();
    private final double traverseTime = 60.0/120*maxItems; // seconds / items -> edit second value, unit is "item/minute"
    private final double maxCooldown = traverseTime / maxItems;
    private double curCooldown = 0;
    private Point direction = new Point(0, -1); // up
    
    public ConveyorBelt(int x, int y) {
        super(x, y);
        initBlock("Conveyor belt", 100, 1, 1, Color.GREEN);
        setBlockType(BlockType.ConveyorBelt);
        createInventory(maxItems, maxItems, false);
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
        
        if(!alItemOrder.isEmpty() && alItemTime.get(0) <= 0 && world.getBlock(x+direction.x, y+direction.y) instanceof UpdatedBlock updatedBlock && !updatedBlock.isOutputOnly()) {
            int newQuantity = updatedBlock.addItem(alItemOrder.get(0), 1);
            if(newQuantity == 0) {
                removeItem(alItemOrder.get(0), 1);
                alItemOrder.remove(0);
                alItemTime.remove(0);
            }
        }
    }

    @Override
    public void draw(Camera c) {
        super.draw(c);
        c.drawLine(x+width/2.0, y+height/2.0, x+(width+direction.x)/2.0, y+(height+direction.y)/2.0, Color.BLACK);
    }

    @Override
    public void delayedDraw(Camera c, World world) {
        double size = 0.25;
        double cX = x+width/2.0;
        double cY = y+height/2.0;
        for(int i = alItemTime.size()-1; i >= 0; i--) {
            double offsetTime = (traverseTime/2 - alItemTime.get(i))/traverseTime;
            ItemDrawer.drawItem(c, alItemOrder.get(i), cX+direction.x*offsetTime, cY+direction.y*offsetTime, size);
        }
    }

    @Override
    public int addItem(ItemType itemType, int quantity) {
        if(alItemOrder.size() >= 4 || curCooldown > 0) {
            return -1;
        }
        curCooldown += maxCooldown;
        alItemOrder.add(itemType);
        alItemTime.add(traverseTime);
        
        return quantity-1 + super.addItem(itemType, 1);
    }

    public void setDirection(Point direction) {
        this.direction = direction;
    }

    public Point getDirection() {
        return direction;
    }
    
}
