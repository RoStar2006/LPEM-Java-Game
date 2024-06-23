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
public class ReinforcedWall extends Block {
    
    public ReinforcedWall(int x, int y) {
        super(x, y);
        setBlockType(BlockType.Wall);
        initBlock("Reinforced Wall", 1.25, 600, 1, 1, Color.BLUE, Color.GRAY);
        addNeededItem(ItemType.IronSheet, 3);
        addNeededItem(ItemType.IronRod, 2);
        addNeededItem(ItemType.Screws, 5);
    }
    
}