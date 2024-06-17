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
    
    public Merger(int x, int y) {
        super(x, y);
        setDirection(new Point(0, -1));
        initBlock("Merger", 0.15, 20, 1, 1, Color.MAGENTA, Color.LIGHT_GRAY);
        setBlockType(BlockType.ItemLogistic);
        createInventory(1, 1, false);
    }

    @Override
    public void update(double deltaTime, World world) {
        if(getInventory().isEmpty()) { // No item to transport
            return;
        }
        
        UpdatedBlock updatedBlock = getInputBlock(world, getDirection().x, getDirection().y);
        if(updatedBlock != null) {
            ItemType itemType = (ItemType)getInventory().keySet().toArray()[0];
            int newQuantity = updatedBlock.addItem(itemType, 1, this);
            if(newQuantity == 0) {
                removeItem(itemType, 1);
            }
        }
    }
    
}
