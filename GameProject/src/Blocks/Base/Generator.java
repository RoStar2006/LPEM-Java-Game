/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Base;

import Items.ItemType;
import World.World;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author RupRo053
 */
public class Generator extends RecipeBlock {

    private double generatedEnergy = 1;
    private boolean isProducing;
    
    public Generator(int x, int y) {
        super(x, y);
        initBlock("Generator", 1.5, 150, 1, 1, Color.GREEN, Color.RED);
        setBlockType(BlockType.Misc);
        setDirection(null);
        createInventory(100, 1, false);
    }
    
    protected final void addFuel(ItemType itemType, int amount, double burnTime) {
        addRecipe(new HashMap<>(){{put(itemType, amount);}}, new HashMap<>(), burnTime);
        addWhitelistItem(itemType);
    }

    @Override
    public void update(double deltaTime, World world) {
        updateCrafting(deltaTime, true);
        isProducing = getCurCooldown() > 0;
    }

    public double getGeneratedEnergy() {
        if(isProducing) {
            return generatedEnergy;
        } else {
            return 0;
        }
    }
    
    public double getRawGeneratedEnergy() {
        return generatedEnergy;
    }

    protected void setGeneratedEnergy(double generatedEnergy) {
        this.generatedEnergy = generatedEnergy;
    }
    
}
