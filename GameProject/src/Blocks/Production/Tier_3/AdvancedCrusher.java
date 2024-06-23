/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Tier_3;

import Blocks.Production.Tier_2.Crusher;
import Items.ItemType;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class AdvancedCrusher extends Crusher {
    
    public AdvancedCrusher(int x, int y) {
        super(x, y);
        initBlock("Advanced Crusher", 2.75, 275, 1, 1, Color.ORANGE, new Color(51, 51, 51));
        resetNeededItems();
        modifyAllRecipeDuration(0.5);
        addRecipe(new HashMap<>(){{put(ItemType.TitaniumRod, 1);}}, new HashMap<>(){{put(ItemType.TitaniumPellets, 2);}}, 2);
        addNeededItem(ItemType.SteelIngot, 5);
        addNeededItem(ItemType.TitaniumRod, 20);
        addNeededItem(ItemType.TitaniumSheet, 15);
        addNeededItem(ItemType.CircuitBoard, 10);
    }
    
}
