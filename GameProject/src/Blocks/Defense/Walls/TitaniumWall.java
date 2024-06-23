/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Defense.Walls;

import Blocks.Base.Block;
import Blocks.Base.BlockType;
import Items.ItemDrawer;
import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class TitaniumWall extends Block {
    
    public TitaniumWall(int x, int y) {
        super(x, y);
        setBlockType(BlockType.Wall);
        initBlock("Titanium Wall", 1.5, 750, 1, 1, Color.BLUE, ItemDrawer.TITANIUM_BLUE);
        addNeededItem(ItemType.TitaniumSheet, 3);
        addNeededItem(ItemType.Screws, 5);
    }
    
}