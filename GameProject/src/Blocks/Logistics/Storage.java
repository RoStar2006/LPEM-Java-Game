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

/**
 *
 * @author Romeo
 */
public class Storage extends UpdatedBlock {

    public Storage(int x, int y) {
        super(x, y);
        initBlock("Storage", 1.5, 250, 2, 2, Color.MAGENTA, Color.PINK);
        setBlockType(BlockType.ItemLogistic);
        setReplacable(false);
        createInventory(200, -1, false);
        addNeededItem(ItemType.CopperIngot, 25);
        addNeededItem(ItemType.IronIngot, 25);
    }

    @Override
    public void update(double deltaTime, World world) {
    }
    
}
