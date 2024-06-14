/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;

import Blocks.Base.RecipeBlock;
import Blocks.Base.UpdatedBlock;
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
    private UpdatedBlock updatedBlock;
    private final static Color COLOR = new Color(0, 0, 0, 64);
    
    public InventoryMenu(int x, int y, UpdatedBlock updatedBlock) {
        this.x = x;
        this.y = y;
        this.updatedBlock = updatedBlock;
    }

    public void draw(Camera c, Graphics g) {
        int count = 0;
        String[] strArr = formatList(updatedBlock.getInventory());
        String[] resultArr = (updatedBlock instanceof RecipeBlock recipeBlock) ? formatList(recipeBlock.getResultInventory()) : new String[0];
        int maxWidth = 0;
        Font font = c.getRawFont();
        
        for(String str : strArr) {
            maxWidth = Math.max(maxWidth, g.getFontMetrics(font).stringWidth(str));
        }
        
        for(String str : resultArr) {
            maxWidth = Math.max(maxWidth, g.getFontMetrics(font).stringWidth(str));
        }
        
        if(maxWidth > 0) {
            g.setColor(COLOR);
            double height = strArr.length + resultArr.length;
            if(resultArr.length > 0 && strArr.length > 0) { // Account for the line
                height++;
            }
            g.fillRect(c.worldToScreenPosX(x), c.worldToScreenPosY(y), maxWidth, (int)((height+0.25)*WorldManager.FONT_SIZE));
        }
        
        for(String str : strArr) {
            c.drawRawListString(str, x, y, count, Color.WHITE);
            count++;
        }
        
        if(resultArr.length > 0) {
            if(count > 0) { // Draw a line to seperate the results from the rest
                int lY = (int)Math.round(c.worldToScreenPosY(y)+(count+0.5)*font.getSize());
                g.setColor(Color.WHITE);
                g.drawLine(c.worldToScreenPosX(x), lY, c.worldToScreenPosX(x)+maxWidth, lY);
                count++;
            }
            
            for(String str : resultArr) {
                c.drawRawListString(str, x, y, count, Color.WHITE);
                count++;
            }
        }
    }
    
}
