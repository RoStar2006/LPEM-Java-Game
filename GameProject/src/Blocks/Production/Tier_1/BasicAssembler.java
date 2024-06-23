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
public class BasicAssembler extends RecipeBlock {
    
    public BasicAssembler(int x, int y) {
        super(x, y);
        initBlock("Basic Assembler", 1.5, 175, 1, 1, Color.ORANGE, new Color(153, 255, 153));
        addRecipe(new HashMap<>(){{put(ItemType.IronIngot, 1);}}, new HashMap<>(){{put(ItemType.IronSheet, 1);}}, 2);
        addRecipe(new HashMap<>(){{put(ItemType.CopperIngot, 1);}}, new HashMap<>(){{put(ItemType.CopperSheet, 1);}}, 2);
        addRecipe(new HashMap<>(){{put(ItemType.CopperWire, 5);}}, new HashMap<>(){{put(ItemType.CopperCable, 2);}}, 2.5);
        addNeededItem(ItemType.CopperOre, 10);
        addNeededItem(ItemType.IronIngot, 20);
    }
    
}
