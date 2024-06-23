/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Tier_1;

import Blocks.Base.RecipeBlock;
import Items.ItemType;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class BasicRoller extends RecipeBlock {
    
    public BasicRoller(int x, int y) {
        super(x, y);
        initBlock("Basic Roller", 1.25, 150, 1, 1, Color.ORANGE, new Color(102, 102, 255));
        addRecipe(new HashMap<>(){{put(ItemType.IronIngot, 1);}}, new HashMap<>(){{put(ItemType.IronRod, 2);}}, 2);
        addRecipe(new HashMap<>(){{put(ItemType.IronRod, 1);}}, new HashMap<>(){{put(ItemType.Screws, 5);}}, 2);
        addRecipe(new HashMap<>(){{put(ItemType.CopperIngot, 1);}}, new HashMap<>(){{put(ItemType.CopperWire, 8);}}, 4);
        addNeededItem(ItemType.CopperSheet, 5);
        addNeededItem(ItemType.IronSheet, 20);
    }
    
}
