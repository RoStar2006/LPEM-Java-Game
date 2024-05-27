/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;

import Core.Camera;
import static Items.Inventory.formatList;
import Items.ItemType;
import World.WorldManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class InventoryMenu {
    private int x, y;
    private HashMap<ItemType, Integer> inventory;
    private final static Color COLOR = new Color(0, 0, 0, 64);
    
    public InventoryMenu(int x, int y, HashMap<ItemType, Integer> inventory) {
        this.x = x;
        this.y = y;
        this.inventory = inventory;
    }

    public void draw(Camera c, Graphics g) {
        int count = 0;
        String[] strArr = formatList(inventory);
        int maxWidth = 0;
        Font font = c.getRawFont();
        
        for(String str : strArr) {
            maxWidth = Math.max(maxWidth, g.getFontMetrics(font).stringWidth(str));
        }
        
        if(maxWidth > 0) {
            g.setColor(COLOR);
            g.fillRect(c.worldToScreenPosX(x), c.worldToScreenPosY(y), maxWidth, (int)((strArr.length+0.25)*WorldManager.FONT_SIZE));
        }
        
        for(String str : strArr) {
            c.drawRawListString(str, x, y, count, Color.WHITE);
            count++;
        }
    }
    
}
