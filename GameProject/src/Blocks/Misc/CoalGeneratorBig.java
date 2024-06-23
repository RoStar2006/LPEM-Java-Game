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
public class CoalGeneratorBig extends Generator {

    public CoalGeneratorBig(int x, int y) {
        super(x, y);
        initBlock("Big Coal Generator", 3, 300, 2, 2, Color.GREEN, Color.DARK_GRAY);
        addFuel(ItemType.Coal, 1, 0.5);
        setGeneratedEnergy(40);
        addNeededItem(ItemType.CircuitBoard, 10);
        addNeededItem(ItemType.SteelIngot, 30);
        addNeededItem(ItemType.TitaniumRod, 25);
        addNeededItem(ItemType.Screws, 40);
    }
    
}
