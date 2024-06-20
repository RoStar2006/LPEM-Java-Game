/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Logistics;

import Blocks.Base.BlockType;
import Blocks.Base.UpdatedBlock;
import Core.Utils;
import Items.ItemType;
import World.World;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class OverflowGate extends UpdatedBlock {
    private World world;
    
    public OverflowGate(int x, int y) {
        super(x, y);
        setBlockType(BlockType.ItemLogistic);
        createInventory(0, 0, false);
        initBlock("Overflow Gate", 0.1, 25, 1, 1, Color.MAGENTA, Color.YELLOW);
        addNeededItem(ItemType.CopperIngot, 5);
    }

    @Override
    public void update(double deltaTime, World world) {
        this.world = world;
    }
    
    @Override
    public int addItem(ItemType itemType, int quantity, UpdatedBlock block) {
        int xDir = Utils.clampInt(getX() - block.getX(), -1, 1);
        int yDir = Utils.clampInt(getY() - block.getY(), -1, 1);
        int newQuantity = quantity;
        
        if(world != null) {
            UpdatedBlock lBlock = getInputBlock(world, xDir, yDir);
            if(lBlock != null) {
                newQuantity = lBlock.addItem(itemType, quantity, this);
                
                if(newQuantity > 0) { // Overflow
                    newQuantity = manageOverflow(itemType, quantity, xDir, yDir);
                }
            } else { // Overflow
                newQuantity = manageOverflow(itemType, quantity, xDir, yDir);
            }
        }
        
        return newQuantity;
    }
    
    private int manageOverflow(ItemType itemType, int quantity, int xDir, int yDir) {
        int newQuantity = quantity;
        
        if(xDir == 0) { // overflow to left or right
            UpdatedBlock lBlock = getInputBlock(world, -1, 0);
            if(lBlock != null) {
                newQuantity = lBlock.addItem(itemType, quantity, this);
            }
            
            if(newQuantity > 0 || lBlock == null) {
                lBlock = getInputBlock(world, 1, 0);
                if(lBlock != null) {
                    newQuantity = lBlock.addItem(itemType, quantity, this);
                }
            }
        } else if(yDir == 0) { // overflow to top or bottom
            UpdatedBlock lBlock = getInputBlock(world, 0, -1);
            if(lBlock != null) {
                newQuantity = lBlock.addItem(itemType, quantity, this);
            }
            if(newQuantity > 0 || lBlock == null) {
                lBlock = getInputBlock(world, 0, 1);
                if(lBlock != null) {
                    newQuantity = lBlock.addItem(itemType, quantity, this);
                }
            }
        }
        
        return newQuantity;
    }
    
}
