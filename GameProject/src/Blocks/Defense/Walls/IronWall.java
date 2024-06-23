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
public class IronWall extends Block {
    
    public IronWall(int x, int y) {
        super(x, y);
        setBlockType(BlockType.Wall);
        initBlock("Iron Wall", 1, 400, 1, 1, Color.BLUE, Color.LIGHT_GRAY);
        addNeededItem(ItemType.IronIngot, 3);
    }
    
}
