/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;

import Core.Camera;
import static Items.Inventory.formatList;
import Items.ItemType;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class InventoryMenu {
    private int x, y;
    private HashMap<ItemType, Integer> inventory;
    
    public InventoryMenu(int x, int y, HashMap<ItemType, Integer> inventory) {
        this.x = x;
        this.y = y;
        this.inventory = inventory;
    }

    public void draw(Camera c, Graphics g) {
        int count = 0;
        String[] strArr = formatList(inventory);
        
        for(String str : strArr) {
            c.drawRawListString(str, x, y, count, Color.WHITE);
            count++;
        }
    }
    
}
