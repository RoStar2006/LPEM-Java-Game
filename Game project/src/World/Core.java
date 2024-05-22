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
public class Core extends UpdatedBlock {
    
    public Core(int x, int y) {
        super(x, y);
        initBlock("Core", 1000, 3, 3, Color.CYAN);
        createInventory(100, 0);
        addItem(ItemType.CopperOre, 20);
        addItem(ItemType.IronOre, 20);
    }

    @Override
    public void update(double deltaTime) {
        
    }
    
    @Override
    public void drawIcon(Graphics g, int x, int y, int size) {
        g.setColor(Color.RED);
        g.fillRect(x, y, size/2, size/2);
        g.fillRect(x+size/2, y+size/2, size/2, size/2);
        g.setColor(Color.CYAN);
        g.fillRect(x+size/2, y, size/2, size/2);
        g.fillRect(x, y+size/2, size/2, size/2);
    }
    
}
