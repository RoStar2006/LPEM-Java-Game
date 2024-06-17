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
public class Junction extends UpdatedBlock {
    private World world;
    
    public Junction(int x, int y) {
        super(x, y);
        setBlockType(BlockType.ItemLogistic);
        createInventory(0, 0, false);
        initBlock("Junction", 0.1, 25, 1, 1, Color.MAGENTA, Color.CYAN);
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
            }
        }
        
        return newQuantity;
    }
    
}
