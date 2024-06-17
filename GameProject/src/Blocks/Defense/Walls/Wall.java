/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Defense.Walls;

import Blocks.Base.Block;
import Blocks.Base.BlockType;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Romeo
 */
public class Wall extends Block {
    
    public Wall(int x, int y) {
        super(x, y);
        initBlock("Wall", 0.75, 400, 1, 1, Color.BLUE, Color.LIGHT_GRAY);
        setBlockType(BlockType.Wall);
    }
}
