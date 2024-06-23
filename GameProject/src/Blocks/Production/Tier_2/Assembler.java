/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Tier_2;

import Blocks.Production.Tier_1.BasicAssembler;
import Items.ItemType;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class Assembler extends BasicAssembler {
    
    public Assembler(int x, int y) {
        super(x, y);
        initBlock("Assembler", 2, 250, 1, 1, Color.ORANGE, new Color(51, 204, 0));
        resetNeededItems();
        modifyAllRecipeDuration(0.5);
        addRecipe(new HashMap<>(){{put(ItemType.IronSheet, 1);}}, new HashMap<>(){{put(ItemType.Cartridges, 1);}}, 2);
        addRecipe(new HashMap<>(){{put(ItemType.TitaniumIngot, 1);}}, new HashMap<>(){{put(ItemType.TitaniumSheet, 1);}}, 3);
        addNeededItem(ItemType.CopperWire, 25);
        addNeededItem(ItemType.IronSheet, 20);
        addNeededItem(ItemType.Screws, 15);
    }
    
}
