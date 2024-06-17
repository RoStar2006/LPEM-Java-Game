/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Defense.Walls;

import Blocks.Base.Block;
import Blocks.Base.BlockType;
import Items.ItemType;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Romeo
 */
public class BigWall extends Block {
    
    public BigWall(int x, int y) {
        super(x, y);
        initBlock("Big Wall", 2.5, 1600, 2, 2, Color.BLUE, Color.DARK_GRAY);
        setBlockType(BlockType.Wall);
        addNeededItem(ItemType.IronOre, 4);
    }
}
