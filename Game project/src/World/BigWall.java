/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

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
        initBlock("Big Wall", 250, 2, 2, Color.DARK_GRAY);
        addNeededItem(ItemType.IronOre, 4);
    }

    @Override
    public void drawIcon(Graphics g, int x, int y, int size) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, size/2, size/2);
        g.fillRect(x+size/2, y+size/2, size/2, size/2);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x+size/2, y, size/2, size/2);
        g.fillRect(x, y+size/2, size/2, size/2);
    }
    
}
