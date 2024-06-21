/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Base;

import Items.Inventory;
import Items.ItemType;
import Items.Recipe;
import World.World;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RupRo053
 */
public class RecipeBlock extends UpdatedBlock {

    private ArrayList<Recipe> alRecipes = new ArrayList<>();
    private double curCooldown = 0;
    private Inventory resultInventory;
    
    public RecipeBlock(int x, int y) {
        super(x, y);
        setBlockType(BlockType.RecipeBlock);
        setDirection(new Point(0, -1));
        initBlock("Recipe Block", 1.5, 50, 1, 1, Color.WHITE);
        createInventory(20, 1, false);
        resultInventory = new Inventory(20, 1, true);
        
        // addRecipe(new HashMap<>() {{put(ItemType.IronOre, 2);}}, new HashMap<>() {{put(ItemType.CopperOre, 1);}}, 0.5);
    }
    
    @Override
    public void update(double deltaTime, World world) {
        updateCrafting(deltaTime, false);
        
        // Distribute result items
        UpdatedBlock updatedBlock = getInputBlock(world, getDirection().x, getDirection().y);
        if(!getResultInventory().isEmpty() && updatedBlock != null) { // Check if any items needs to be transported and a receiving block is available
            for(Map.Entry<ItemType, Integer> entry : getResultInventory().entrySet()) {
                int newQuantity = updatedBlock.addItem(entry.getKey(), entry.getValue(), this);
                getRawResultInventory().removeItem(entry.getKey(), entry.getValue()-newQuantity);
            }
        }
    }
    
    /**
     * This method updates the crafting cooldown and crafts the items if possible
     * @param deltaTime
     * @param updateAndCheck 
     * If this value is true, then it updated the cooldown and checks if it can craft, else it has to wait for the next cycle
     */
    protected void updateCrafting(double deltaTime, boolean updateAndCheck) {
        boolean ready;
        if(curCooldown > 0) {
            curCooldown -= deltaTime;
            ready = curCooldown <= 0;
        } else {
            ready = true;
        }
        
        if(ready) {
            for(Recipe recipe : alRecipes) {                
                boolean crafted = false;
                
                while(recipe.canCraft(getInventory()) && curCooldown <= 0) {
                    craft(recipe);
                    crafted = true;
                }
                
                if(crafted) {
                    break;
                }
            }
        }
    }
    
    private void craft(Recipe recipe) {
        curCooldown += recipe.getDuration();
        
        HashMap<ItemType, Integer> hmCrafted = recipe.craft(this);
        for(Map.Entry<ItemType, Integer> entry : hmCrafted.entrySet()) {
            resultInventory.addItem(entry.getKey(), entry.getValue());
        }
    }
    
    protected void addRecipe(HashMap<ItemType, Integer> hmNeeded, HashMap<ItemType, Integer> hmCrafted, double duration) {
        alRecipes.add(new Recipe(hmNeeded, hmCrafted, duration));
        
        if(getMaxSize() < hmNeeded.size()) {
            setMaxSize(hmNeeded.size());
        }
        
        if(resultInventory.getMaxSize() < hmCrafted.size()) {
            resultInventory.setMaxSize(hmCrafted.size());
        }
        
        sortRecipes();
    }
    
    /**
     * Sort from biggest recipe to smallest
     */
    private void sortRecipes() {
        for(int i = 0; i < alRecipes.size(); i++) {
            int bestMatch = i;
            for(int j = i+1; j < alRecipes.size(); j++) {
                if(alRecipes.get(j).getSize() > alRecipes.get(bestMatch).getSize()) {
                    bestMatch = j;
                }
            }
            
            Recipe recipe = alRecipes.set(i, alRecipes.get(bestMatch));
            alRecipes.set(bestMatch, recipe);
        }
    }
    
    protected Inventory getRawResultInventory() {
        return resultInventory;
    }
    
    public HashMap<ItemType, Integer> getResultInventory() {
        return resultInventory.getHmItems();
    }

    protected double getCurCooldown() {
        return curCooldown;
    }
    
    public final ArrayList<String> formattedRecipeDetails() {
        ArrayList<String> strArr = new ArrayList<>();
        
        for(Recipe recipe : alRecipes) {
            strArr.add("<offset>0.5");
            String[] neededArr = recipe.formattedNeededItems(true);
            String[] cratedArr = recipe.formattedCraftedItems(true);
            int boxSize = 1+cratedArr.length+neededArr.length;
            if(neededArr.length > 0) {
                boxSize++;
            }
            if(cratedArr.length > 0) {
                boxSize++;
            }
            strArr.add("<box>" + boxSize);
            if(neededArr.length > 0) {
                strArr.add("Input:");
                for(String str : neededArr) {
                    strArr.add("<tab>" + str);
                }
            }
            if(cratedArr.length > 0) {
                strArr.add("Output:");
                for(String str : cratedArr) {
                    strArr.add("<tab>" + str);
                }
            }
            strArr.add("Time:" + recipe.getDuration() + " seconds");
        }
        /*for(Map.Entry<ItemType, Integer> entry : hmProjectileCost.entrySet()) {
            strArr.add("<itemIcon>" + Inventory.formatName(entry.getKey()));
            strArr.add("Damage:" + hmProjectileDamage.get(entry.getKey()));
            strArr.add("Needed:" + entry.getValue());
        }*/
        
        return strArr;
    }
}
