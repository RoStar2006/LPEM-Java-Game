/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Logistics;

import Blocks.Base.ConveyorBelt;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class ConveyorBeltMK1 extends ConveyorBelt {
    
    public ConveyorBeltMK1(int x, int y) {
        super(x, y);
        initBlock("Conveyor Belt MK1", 0.01, 20, 1, 1, Color.MAGENTA, Color.GREEN);
        setTravelSpeed(120);
    }
    
}
