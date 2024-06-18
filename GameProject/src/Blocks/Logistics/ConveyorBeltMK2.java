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
public class ConveyorBeltMK2 extends ConveyorBelt {

    public ConveyorBeltMK2(int x, int y) {
        super(x, y);
        initBlock("Iron Conveyor", 0.01, 20, 1, 1, Color.MAGENTA, new Color(0, 255, 85));
        setTravelSpeed(240);
        addNeededItem(ItemType.IronIngot, 2);
        addNeededItem(ItemType.CopperIngot, 1);
    }
    
}
