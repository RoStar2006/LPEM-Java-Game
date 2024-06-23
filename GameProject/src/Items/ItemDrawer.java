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
    public final static Color DARK_ORANGE = new Color(255, 140, 0);
    public final static Color TITANIUM_BLUE = new Color(91, 121, 142);
    public final static Color DARK_TITANIUM_BLUE = TITANIUM_BLUE.darker();
    
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
            case Coal -> {
                g.setColor(Color.BLACK);
                g.fillOval(x-size/2, y-size/2, size, size);
            }
            case TitaniumOre -> {
                g.setColor(TITANIUM_BLUE);
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
                g.setColor(DARK_ORANGE);
                g.fillRect(x-size/2, y-size/2, size/2, size/2);
            }
            case TitaniumIngot -> {
                g.setColor(TITANIUM_BLUE);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(DARK_TITANIUM_BLUE);
                g.fillRect(x-size/2, y-size/2, size/2, size/2);
            }
            case SteelIngot -> {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(Color.GRAY);
                g.fillRect(x-size/2, y-size/2, size/2, size/2);
            }
            case IronSheet -> {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(Color.DARK_GRAY);
                g.fillRect(x-size/6, y-size/2, size/3, size);
            }
            case CopperSheet -> {
                g.setColor(Color.ORANGE);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(DARK_ORANGE);
                g.fillRect(x-size/6, y-size/2, size/3, size);
            }
            case TitaniumSheet -> {
                g.setColor(TITANIUM_BLUE);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(DARK_TITANIUM_BLUE);
                g.fillRect(x-size/6, y-size/2, size/3, size);
            }
            case IronRod -> {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x-size/4, y-size/2, size/2, size);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x-size/4, y-size/2, size/2, size);
            }
            case TitaniumRod -> {
                g.setColor(TITANIUM_BLUE);
                g.fillRect(x-size/4, y-size/2, size/2, size);
                g.setColor(DARK_TITANIUM_BLUE);
                g.drawRect(x-size/4, y-size/2, size/2, size);
            }
            case CopperWire -> {
                g.setColor(Color.ORANGE);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(DARK_ORANGE);
                g.drawRect(x-size/2, y-size/2, size, size);
            }
            case CopperCable -> {
                g.setColor(DARK_ORANGE);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(Color.BLACK);
                g.drawRect(x-size/2, y-size/2, size, size);
            }
            case Screws -> {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(Color.BLUE);
                g.drawRect(x-size/2, y-size/2, size, size);
                g.fillRect(x-size/8, y-size/2, size/4, size);
            }
            case Cartridges -> {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(Color.DARK_GRAY);
                g.fillRect(x-size/12-size/4, y-size/2, size/6, size);
                g.fillRect(x-size/12+size/4, y-size/2, size/6, size);
                g.setColor(Color.BLUE);
                g.drawRect(x-size/2, y-size/2, size, size);
                g.fillRect(x-size/12, y-size/2, size/6, size);
            }
            case CircuitBoard -> {
                g.setColor(Color.GREEN);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(Color.ORANGE);
                g.fillRect(x-size/2, y-size/2, size/4, size/4*3);
                g.fillRect(x-size/2, y+size/3, size/2, size/4);
                g.fillRect(x+size/4, y-size/2, size/4, size);
            }
            case CoalPowder -> {
                g.setColor(Color.BLACK);
                g.fillOval(x-size/2, y-size/2, size, size);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x-size/3, y-size/3, size/5, size/3);
                g.fillRect(x, y, size/7, size/5);
                g.fillRect(x-size/6, y+size/2, size/4, size/6);
                g.fillRect(x+size/3, y-size/4, size/3, size/4);
            }
            case IronPellets -> {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(Color.DARK_GRAY);
                g.fillRect(x-size/12-size/4, y-size/2, size/6, size);
                g.fillRect(x-size/12+size/4, y-size/2, size/6, size);
                g.fillRect(x-size/2, y-size/12-size/4, size, size/6);
                g.fillRect(x-size/2, y-size/12+size/4, size, size/6);
                g.setColor(Color.BLUE);
                g.drawRect(x-size/2, y-size/2, size, size);
                g.fillRect(x-size/12, y-size/2, size/6, size);
            }
            case TitaniumPellets -> {
                g.setColor(TITANIUM_BLUE);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(DARK_TITANIUM_BLUE);
                g.fillRect(x-size/12-size/4, y-size/2, size/6, size);
                g.fillRect(x-size/12+size/4, y-size/2, size/6, size);
                g.fillRect(x-size/2, y-size/12-size/4, size, size/6);
                g.fillRect(x-size/2, y-size/12+size/4, size, size/6);
                g.setColor(Color.BLUE);
                g.drawRect(x-size/2, y-size/2, size, size);
                g.fillRect(x-size/12, y-size/2, size/6, size);
            }
            case Ammo -> {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(Color.BLACK);
                g.fillRect(x-size/2, y-size/2, size, size/2);
                g.setColor(Color.DARK_GRAY);
                g.fillRect(x-size/12-size/4, y-size/2, size/6, size);
                g.fillRect(x-size/12+size/4, y-size/2, size/6, size);
                g.setColor(Color.BLUE);
                g.drawRect(x-size/2, y-size/2, size, size);
                g.fillRect(x-size/12, y-size/2, size/6, size);
            }
            case AdvancedAmmo -> {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x-size/2, y-size/2, size, size);
                g.setColor(TITANIUM_BLUE);
                g.fillRect(x-size/2, y-size/2, size, size/2);
                g.setColor(Color.DARK_GRAY);
                g.fillRect(x-size/12-size/4, y-size/2, size/6, size);
                g.fillRect(x-size/12+size/4, y-size/2, size/6, size);
                g.setColor(Color.BLUE);
                g.drawRect(x-size/2, y-size/2, size, size);
                g.fillRect(x-size/12, y-size/2, size/6, size);
            }
            default -> {
                g.setColor(Color.PINK);
                g.fillOval(x-size/2, y-size/2, size, size);
            }
        }
    }
}
