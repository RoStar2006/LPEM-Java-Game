/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Misc;

import Blocks.Base.Generator;
import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class SolarPanel extends Generator {
    
    public SolarPanel(int x, int y) {
        super(x, y);
        initBlock("Solar Panel", 2.25, 150, 2, 2, Color.GREEN, Color.CYAN);
        setGeneratedEnergy(7.5);
        deleteInventory();
        addNeededItem(ItemType.CircuitBoard, 75);
        addNeededItem(ItemType.TitaniumSheet, 15);
        addNeededItem(ItemType.CopperCable, 50);
        addNeededItem(ItemType.Screws, 45);
    }
    
}
