/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Romeo
 */
public class Wall extends Block {
    
    public Wall(int x, int y) {
        super(x, y);
        initBlock("Wall", 50, 1, 1, Color.LIGHT_GRAY);
        setBlockType(BlockType.Wall);
    }

    @Override
    public void drawIcon(Graphics g, int x, int y, int size) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, size/2, size/2);
        g.fillRect(x+size/2, y+size/2, size/2, size/2);
        g.setColor(Color.GREEN);
        g.fillRect(x+size/2, y, size/2, size/2);
        g.fillRect(x, y+size/2, size/2, size/2);
    }
}
