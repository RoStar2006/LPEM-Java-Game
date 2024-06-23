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
public class TitaniumWallBig extends Block {
    
    public TitaniumWallBig(int x, int y) {
        super(x, y);
        setBlockType(BlockType.Wall);
        initBlock("Big Titanium Wall", 6, 4000, 2, 2, Color.BLUE, ItemDrawer.TITANIUM_BLUE);
        addNeededItem(ItemType.TitaniumSheet, 15);
        addNeededItem(ItemType.Screws, 20);
    }
    
}