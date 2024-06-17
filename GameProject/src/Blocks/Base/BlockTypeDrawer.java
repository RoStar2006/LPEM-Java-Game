/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Base;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author RupRo053
 */
public class BlockTypeDrawer {
    public final static void drawBlockType(Graphics g, BlockType blockType, int x, int y, int size) {
        switch (blockType) {
            case ItemLogistic -> drawBlockIcon(g, x, y, size, Color.MAGENTA, Color.WHITE);
            case NodeCollector -> drawBlockIcon(g, x, y, size, Color.RED, Color.WHITE);
            case RecipeBlock -> drawBlockIcon(g, x, y, size, Color.ORANGE, Color.WHITE);
            case Wall -> drawBlockIcon(g, x, y, size, Color.BLUE, Color.WHITE);
            case Tower -> drawBlockIcon(g, x, y, size, Color.YELLOW, Color.WHITE);
            default -> drawBlockIcon(g, x, y, size, Color.WHITE, Color.BLACK);
        }
    }
    
    private static void drawBlockIcon(Graphics g, int x, int y, int size, Color color1, Color color2) {
        int lSize = size/2;
        
        g.setColor(color1);
        g.fillRect(x, y, lSize, lSize);
        g.fillRect(x+lSize, y+lSize, lSize, lSize);
        g.setColor(color2);
        g.fillRect(x+lSize, y, lSize, lSize);
        g.fillRect(x, y+lSize, lSize, lSize);
    }
}
