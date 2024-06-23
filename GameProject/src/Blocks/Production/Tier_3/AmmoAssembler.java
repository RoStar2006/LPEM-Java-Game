/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Tier_3;

import Blocks.Base.RecipeBlock;
import Items.ItemType;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class AmmoAssembler extends RecipeBlock {
    
    public AmmoAssembler(int x, int y) {
        super(x, y);
        initBlock("Ammo Assembler", 2.5, 250, 1, 1, Color.ORANGE, new Color(51, 0, 51));
        addRecipe(new HashMap<>(){{put(ItemType.Cartridges, 1);put(ItemType.CoalPowder, 1);put(ItemType.IronPellets, 1);}}, new HashMap<>(){{put(ItemType.Ammo, 5);}}, 1);
        addRecipe(new HashMap<>(){{put(ItemType.Cartridges, 1);put(ItemType.CoalPowder, 1);put(ItemType.TitaniumPellets, 1);}}, new HashMap<>(){{put(ItemType.AdvancedAmmo, 3);}}, 1);
        addNeededItem(ItemType.CopperWire, 25);
        addNeededItem(ItemType.CircuitBoard, 25);
        addNeededItem(ItemType.TitaniumSheet, 15);
        addNeededItem(ItemType.IronRod, 20);
    }
    
}
