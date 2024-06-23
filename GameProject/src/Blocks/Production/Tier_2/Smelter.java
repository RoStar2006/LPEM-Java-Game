/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Tier_2;

import Blocks.Production.Tier_1.BasicSmelter;
import Items.ItemType;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class Smelter extends BasicSmelter {
    
    public Smelter(int x, int y) {
        super(x, y);
        initBlock("Smelter", 2, 225, 1, 1, Color.ORANGE, new Color(255, 51, 51));
        resetNeededItems();
        modifyAllRecipeDuration(0.5);
        addRecipe(new HashMap<>(){{put(ItemType.TitaniumOre, 1);}}, new HashMap<>(){{put(ItemType.TitaniumIngot, 1);}}, 3);
        addNeededItem(ItemType.CopperCable, 20);
        addNeededItem(ItemType.IronSheet, 25);
        addNeededItem(ItemType.SteelIngot, 10);
        addNeededItem(ItemType.Screws, 20);
    }
    
}
