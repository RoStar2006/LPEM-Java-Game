/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Tier_2;

import Blocks.Base.RecipeBlock;
import Items.ItemType;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class Crusher extends RecipeBlock {
    
    public Crusher(int x, int y) {
        super(x, y);
        initBlock("Crusher", 1.75, 150, 1, 1, Color.ORANGE, new Color(153, 153, 153));
        addRecipe(new HashMap<>(){{put(ItemType.Coal, 1);}}, new HashMap<>(){{put(ItemType.CoalPowder, 1);}}, 1);
        addRecipe(new HashMap<>(){{put(ItemType.IronRod, 1);}}, new HashMap<>(){{put(ItemType.IronPellets, 4);}}, 2);
        addNeededItem(ItemType.IronIngot, 5);
        addNeededItem(ItemType.IronRod, 20);
        addNeededItem(ItemType.CopperSheet, 15);
        addNeededItem(ItemType.Screws, 25);
    }
    
}
