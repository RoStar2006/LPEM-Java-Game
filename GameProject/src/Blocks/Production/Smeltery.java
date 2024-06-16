/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production;

import Blocks.Base.RecipeBlock;
import Items.ItemType;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class Smeltery extends RecipeBlock {
    
    public Smeltery(int x, int y) {
        super(x, y);
        initBlock("Smeltery", 1.25, 150, 1, 1, Color.ORANGE, new Color(255,114,118));
        addRecipe(new HashMap<>(){{put(ItemType.IronOre, 1);}}, new HashMap<>(){{put(ItemType.IronIngot, 1);}}, 2);
        addRecipe(new HashMap<>(){{put(ItemType.CopperOre, 1);}}, new HashMap<>(){{put(ItemType.CopperIngot, 1);}}, 2);
    }
    
}
