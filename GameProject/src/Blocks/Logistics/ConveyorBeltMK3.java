/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Logistics;

import Blocks.Base.ConveyorBelt;
import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author RupRo053
 */
public class ConveyorBeltMK3 extends ConveyorBelt {
    
    public ConveyorBeltMK3(int x, int y) {
        super(x, y);
        initBlock("Conveyor Belt MK3", 0.01, 20, 1, 1, Color.MAGENTA, new Color(0, 255, 170));
        setTravelSpeed(720);
        addNeededItem(ItemType.SteelIngot, 1);
        addNeededItem(ItemType.IronSheet, 1);
        addNeededItem(ItemType.Screws, 3);
    }
    
}
