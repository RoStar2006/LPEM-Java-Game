/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Tier_3;

import Blocks.Production.Tier_2.Roller;
import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class AdvancedRoller  extends Roller {
    
    public AdvancedRoller(int x, int y) {
        super(x, y);
        initBlock("Advanced Roller", 2.75, 275, 1, 1, Color.ORANGE, new Color(153, 0, 102));
        resetNeededItems();
        modifyAllRecipeDuration(0.5);
        addNeededItem(ItemType.CircuitBoard, 20);
        addNeededItem(ItemType.TitaniumSheet, 25);
        addNeededItem(ItemType.TitaniumRod, 5);
        addNeededItem(ItemType.Screws, 20);
    }
    
}