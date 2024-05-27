/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Items;

import Core.Camera;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class ItemDrawer {
    public static void drawItem(Camera c, ItemType itemType, double x, double y, double size) {
        switch(itemType) {
            case IronOre -> {
                c.fillRect(x-size/2, y-size/2, size, size, Color.GRAY);
            }
            case CopperOre -> {
                c.fillRect(x-size/2, y-size/2, size, size, Color.ORANGE);
            }
            default -> {
                c.fillCircle(x, y, size, Color.PINK);
            }
        }
    }
}
