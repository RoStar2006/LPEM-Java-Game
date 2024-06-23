/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Tier_3;

import Blocks.Production.Tier_2.Smelter;
import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class AdvancedSmelter extends Smelter {
    
    public AdvancedSmelter(int x, int y) {
        super(x, y);
        initBlock("Advanced Smelter", 3, 300, 1, 1, Color.ORANGE, new Color(204, 0, 51));
        resetNeededItems();
        modifyAllRecipeDuration(0.5);
        addNeededItem(ItemType.CopperCable, 20);
        addNeededItem(ItemType.TitaniumSheet, 25);
        addNeededItem(ItemType.TitaniumRod, 10);
        addNeededItem(ItemType.Screws, 20);
        addNeededItem(ItemType.CircuitBoard, 10);
    }
    
}
