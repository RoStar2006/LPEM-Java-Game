/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Items;

import Core.Camera;
import static Items.ItemType.*;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Romeo
 */
public class ItemDrawer {
    private static Color darkOrange = new Color(255, 140, 0);
    
    public static void drawItem(Camera c, ItemType itemType, double x, double y, double size) {
        if(c.isOnScreen(x, y, size, size)) {
            drawItem(c.getGraphics(), itemType, c.worldToScreenPosX(x), c.worldToScreenPosY(y), c.scaleToZoom(size));
        }
    }
    
    public static void drawItem(Graphics g, ItemType itemType, int x, int y, int size) {
        switch(itemType) {
            case IronOre -> {
                g.setColor(Color.GRAY);
                g.fillRect(x-size/2, y-size/2, size, size);
            }
            case CopperOre -> {
                g.setColor(Color.ORANGE);
                g.fillRect(x-size/2, y-size/2, size, size);
            }
            case IronIngot -> {
                g.setColor(Color.GRAY);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x-size/2, y-size/2, size/2, size/2);
            }
            case CopperIngot -> {
                g.setColor(Color.ORANGE);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(darkOrange);
                g.fillRect(x-size/2, y-size/2, size/2, size/2);
            }
            default -> {
                g.setColor(Color.PINK);
                g.fillOval(x-size/2, y-size/2, size, size);
            }
        }
    }
}
