/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Tier_3;

import Blocks.Production.Tier_2.Assembler;
import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class AdvancedAssembler extends Assembler {
    
    public AdvancedAssembler(int x, int y) {
        super(x, y);
        initBlock("Advanced Assembler", 4, 325, 1, 1, Color.ORANGE, new Color(0, 102, 51));
        resetNeededItems();
        modifyAllRecipeDuration(0.5);
        addNeededItem(ItemType.CircuitBoard, 10);
        addNeededItem(ItemType.CopperWire, 15);
        addNeededItem(ItemType.TitaniumSheet, 15);
        addNeededItem(ItemType.Screws, 20);
    }
    
}
