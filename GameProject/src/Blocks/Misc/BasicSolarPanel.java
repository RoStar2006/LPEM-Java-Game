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
public class BasicSolarPanel extends Generator {
    
    public BasicSolarPanel(int x, int y) {
        super(x, y);
        initBlock("Basic Solar Panel", 0.75, 75, 1, 1, Color.GREEN, Color.BLUE);
        setGeneratedEnergy(1);
        deleteInventory();
        addNeededItem(ItemType.CircuitBoard, 25);
        addNeededItem(ItemType.IronSheet, 5);
        addNeededItem(ItemType.CopperCable, 15);
        addNeededItem(ItemType.Screws, 20);
    }
    
}
