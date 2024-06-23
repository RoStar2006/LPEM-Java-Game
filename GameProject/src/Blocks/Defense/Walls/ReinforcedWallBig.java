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
public class ReinforcedWallBig extends Block {
    
    public ReinforcedWallBig(int x, int y) {
        super(x, y);
        setBlockType(BlockType.Wall);
        initBlock("Big Reinforced Wall", 5, 3200, 2, 2, Color.BLUE, Color.GRAY);
        addNeededItem(ItemType.IronSheet, 15);
        addNeededItem(ItemType.IronRod, 8);
        addNeededItem(ItemType.Screws, 20);
    }
    
}
