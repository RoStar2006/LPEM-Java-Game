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
public class CopperWall extends Block {
    
    public CopperWall(int x, int y) {
        super(x, y);
        setBlockType(BlockType.Wall);
        initBlock("Copper Wall", 0.75, 300, 1, 1, Color.BLUE, Color.ORANGE);
        addNeededItem(ItemType.CopperIngot, 3);
    }
}
