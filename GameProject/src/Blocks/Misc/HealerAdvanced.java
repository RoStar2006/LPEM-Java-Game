/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Misc;

import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class HealerAdvanced extends Healer {
    
    public HealerAdvanced(int x, int y) {
        super(x, y);
        initBlock("Advanced Healer", 4, 250, 1, 1, Color.GREEN, Color.MAGENTA);
        setRequiredEnergy(5);
        setRange(12);
        resetNeededItems();
        addNeededItem(ItemType.CircuitBoard, 40);
        addNeededItem(ItemType.TitaniumSheet, 15);
        addNeededItem(ItemType.IronRod, 10);
        addNeededItem(ItemType.Screws, 20);
    }
    
}
