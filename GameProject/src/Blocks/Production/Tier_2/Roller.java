/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Tier_2;

import Blocks.Production.Tier_1.BasicRoller;
import Items.ItemType;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class Roller extends BasicRoller {
    
    public Roller(int x, int y) {
        super(x, y);
        initBlock("Roller", 1.75, 200, 1, 1, Color.ORANGE, new Color(153, 0, 204));
        resetNeededItems();
        modifyAllRecipeDuration(0.75);
        addRecipe(new HashMap<>(){{put(ItemType.TitaniumIngot, 1);}}, new HashMap<>(){{put(ItemType.TitaniumRod, 2);}}, 3);
        addNeededItem(ItemType.CopperCable, 20);
        addNeededItem(ItemType.IronSheet, 25);
        addNeededItem(ItemType.SteelIngot, 5);
        addNeededItem(ItemType.Screws, 20);
    }
    
}
