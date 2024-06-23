/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Defense.Walls;

import Blocks.Base.Block;
import Blocks.Base.BlockType;
import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class IronWallBig extends Block {
    
    public IronWallBig(int x, int y) {
        super(x, y);
        setBlockType(BlockType.Wall);
        initBlock("Big Iron Wall", 4, 2000, 2, 2, Color.BLUE, Color.LIGHT_GRAY);
        addNeededItem(ItemType.IronIngot, 15);
    }
    
}
