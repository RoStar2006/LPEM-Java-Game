/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Base;

import Items.ItemType;
import World.World;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class Core extends UpdatedBlock {
    
    public Core(int x, int y) {
        super(x, y);
        initBlock("Core", 0, 1000, 3, 3, Color.BLUE, Color.CYAN);
        createInventory(4000, 0, false);
        forceAddItem(ItemType.CopperOre, 20);
        forceAddItem(ItemType.IronOre, 20);
    }

    @Override
    public void update(double deltaTime, World world) {
        
    }
    
}
