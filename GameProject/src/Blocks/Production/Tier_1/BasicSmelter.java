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
public class BasicSmelter extends RecipeBlock {
    
    public BasicSmelter(int x, int y) {
        super(x, y);
        initBlock("Basic Smelter", 1.25, 150, 1, 1, Color.ORANGE, new Color(255,114,118));
        addRecipe(new HashMap<>(){{put(ItemType.IronOre, 1);}}, new HashMap<>(){{put(ItemType.IronIngot, 1);}}, 2);
        addRecipe(new HashMap<>(){{put(ItemType.CopperOre, 1);}}, new HashMap<>(){{put(ItemType.CopperIngot, 1);}}, 2);
        addNeededItem(ItemType.CopperOre, 5);
        addNeededItem(ItemType.IronOre, 20);
    }
    
}
