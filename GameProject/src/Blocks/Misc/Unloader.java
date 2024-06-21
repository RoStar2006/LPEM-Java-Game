/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Misc;

import Blocks.Base.Block;
import Blocks.Base.BlockType;
import Blocks.Base.UpdatedBlock;
import Items.ItemType;
import World.World;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Romeo
 */
public class Unloader extends UpdatedBlock {
    private final static Point[] POSSIBLE_DIRS = { 
        new Point(0, 1),
        new Point(1, 0),
        new Point(0, -1),
        new Point(-1, 0) 
    };
    
    public Unloader(int x, int y) {
        super(x, y);
        initBlock("Unloader", 0.5, 25, 1, 1, Color.GREEN, Color.ORANGE);
        setBlockType(BlockType.Misc);
        setReplacable(false);
        setDirection(new Point(0, -1));
        setExitArrowStyle(false);
        addNeededItem(ItemType.CopperIngot, 5);
        addNeededItem(ItemType.IronIngot, 5);
    }

    @Override
    public void update(double deltaTime, World world) {
        Block block = getInputBlock(world, -getDirection().x, -getDirection().y);
        
        if(block instanceof Storage storage) {
            for(int i = 0; i < 4; i++) { // Try every direction once
                Point p = POSSIBLE_DIRS[i];
                
                if(!(p.x == -getDirection().x && p.y == -getDirection().y)) { // Not facing into the storage
                    dumpItems(world, storage, p.x, p.y);
                }
            }
        }
    }
    
    private void dumpItems(World world, Storage storage, int xDir, int yDir) {
        UpdatedBlock lB = getInputBlock(world, xDir, yDir);
                    
        if(lB != null) {
            ArrayList<Map.Entry<ItemType, Integer>> alEntry = new ArrayList<>(storage.getInventory().entrySet());
            
            for(int i = alEntry.size()-1; i >= 0; i--) {
                Map.Entry<ItemType, Integer> entry = alEntry.get(i);
                int newQuantity = lB.addItem(entry.getKey(), entry.getValue(), this);
                storage.removeItem(entry.getKey(), entry.getValue()-newQuantity);
            }
        }
    }
    
}
