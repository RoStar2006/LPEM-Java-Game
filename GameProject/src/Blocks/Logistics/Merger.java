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
import java.awt.Point;

/**
 *
 * @author Romeo
 */
public class Merger extends UpdatedBlock {
    private ItemType curItemType = null;
    
    public Merger(int x, int y) {
        super(x, y);
        setDirection(new Point(0, -1));
        initBlock("Merger", 20, 1, 1, Color.LIGHT_GRAY, Color.MAGENTA);
        setBlockType(BlockType.ItemLogistic);
        createInventory(1, 1, false);
    }

    @Override
    public void update(double deltaTime, World world) {
        if(curItemType == null) { // No item to transport
            return;
        }
        
        UpdatedBlock updatedBlock = getInputBlock(world, getDirection().x, getDirection().y);
        if(updatedBlock != null) {
            int newQuantity = updatedBlock.addItem(curItemType, 1);
            if(newQuantity == 0) {
                removeItem(curItemType, 1);
                curItemType = null;
            }
        }
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
