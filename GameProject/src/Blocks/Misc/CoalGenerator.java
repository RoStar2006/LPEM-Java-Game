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
 * @author RupRo053
 */
public class CoalGenerator extends Generator {

    public CoalGenerator(int x, int y) {
        super(x, y);
        initBlock("Coal Generator", 0.75, 175, 1, 1, Color.GREEN, Color.GRAY);
        addFuel(ItemType.Coal, 1, 1.5);
        setGeneratedEnergy(7.5);
        addNeededItem(ItemType.CircuitBoard, 5);
        addNeededItem(ItemType.IronSheet, 25);
        addNeededItem(ItemType.IronRod, 15);
        addNeededItem(ItemType.Screws, 20);
    }
    
}
