/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Logistics;

import Blocks.Base.BlockType;
import Blocks.Base.UpdatedBlock;
import Items.ItemType;
import World.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Romeo
 */
public class Splitter extends UpdatedBlock {
    private ItemType curItemType = null;
    private int rrCount = 0; // Round robin count
    private final static Point[] POSSIBLE_DIRS = { 
        new Point(0, 1),
        new Point(1, 0),
        new Point(0, -1),
        new Point(-1, 0) 
    };
    
    public Splitter(int x, int y) {
        super(x, y);
        setDirection(new Point(0, -1));
        setExitArrowStyle(false);
        initBlock("Splitter", 20, 1, 1, Color.MAGENTA, Color.DARK_GRAY);
        setBlockType(BlockType.ItemLogistic);
        createInventory(1, 1, false);
    }

    @Override
    public void update(double deltaTime, World world) {
        if(curItemType == null) {
            return;
        }
        
        for(int i = 0; i < 4; i++) { // Try every direction once
            Point p = POSSIBLE_DIRS[rrCount];
            
            if(!(p.x == -getDirection().x && p.y == -getDirection().y)) {
                UpdatedBlock updatedBlock = getInputBlock(world, p.x, p.y);
                if(updatedBlock != null) {
                    int newQuantity = updatedBlock.addItem(curItemType, 1);
                    if(newQuantity == 0) {
                        removeItem(curItemType, 1);
                        curItemType = null;
                        i = 4;
                    }
                }
            }
            
            rrCount++;
            if(rrCount >= POSSIBLE_DIRS.length) {
                rrCount = 0;
            }
        }
    }

    @Override
    protected void drawIcon(Graphics g, int x, int y, int width, int height) {
        super.drawIcon(g, x, y, width, height);
    }
    
    @Override
    public int addItem(ItemType itemType, int quantity) {
        int newQuantity = super.addItem(itemType, quantity);
        
        if(newQuantity < quantity) {
            curItemType = itemType;
        }
        
        return newQuantity;
    }
    
}
