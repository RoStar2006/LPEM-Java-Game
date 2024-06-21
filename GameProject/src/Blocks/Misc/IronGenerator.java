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
public class IronGenerator extends Generator {

    public IronGenerator(int x, int y) {
        super(x, y);
        initBlock("Iron Generator", 0.75, 175, 1, 1, Color.GREEN, Color.DARK_GRAY);
        addFuel(ItemType.IronOre, 1, 2);
        setGeneratedEnergy(1);
    }
    
}
