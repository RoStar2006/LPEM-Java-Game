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
public class ArcFurnace extends RecipeBlock {
    
    public ArcFurnace(int x, int y) {
        super(x, y);
        initBlock("Arc Furnace", 2.5, 300, 1, 1, Color.ORANGE, new Color(102, 0, 0));
        setRequiredEnergy(15);
        addRecipe(new HashMap<>(){{put(ItemType.IronOre, 3);put(ItemType.CoalPowder, 1);}}, new HashMap<>(){{put(ItemType.SteelIngot, 3);}}, 3);
        addNeededItem(ItemType.CircuitBoard, 5);
        addNeededItem(ItemType.IronSheet, 20);
        addNeededItem(ItemType.IronRod, 25);
        addNeededItem(ItemType.Screws, 25);
    }
    
}
