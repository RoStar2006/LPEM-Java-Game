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
public class Etcher extends RecipeBlock {
    
    public Etcher(int x, int y) {
        super(x, y);
        initBlock("Etcher", 1.5, 175, 1, 1, Color.ORANGE, new Color(255, 255, 0));
        addRecipe(new HashMap<>(){{put(ItemType.CopperSheet, 1);put(ItemType.CopperWire, 1);}}, new HashMap<>(){{put(ItemType.CircuitBoard, 1);}}, 3);
        addNeededItem(ItemType.CopperCable, 25);
        addNeededItem(ItemType.IronSheet, 20);
        addNeededItem(ItemType.Screws, 15);
    }
    
}
