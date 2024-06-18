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
public class CopperWallBig extends Block {
    
    public CopperWallBig(int x, int y) {
        super(x, y);
        initBlock("Big Copper Wall", 2.5, 1200, 2, 2, Color.BLUE, Color.ORANGE);
        setBlockType(BlockType.Wall);
        addNeededItem(ItemType.CopperOre, 20);
    }
}
