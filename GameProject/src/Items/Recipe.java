/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Items;

import Blocks.Base.UpdatedBlock;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RupRo053
 */
public class Recipe {
    private HashMap<ItemType, Integer> hmNeeded; 
    private HashMap<ItemType, Integer> hmCrafted;
    private double duration;

    public Recipe(HashMap<ItemType, Integer> hmNeeded, HashMap<ItemType, Integer> hmCrafted, double duration) {
        this.hmNeeded = hmNeeded;
        this.hmCrafted = hmCrafted;
        this.duration = duration;
    }
    
    public boolean canCraft(HashMap<ItemType, Integer> inventory) {
        for(Map.Entry<ItemType, Integer> entry : hmNeeded.entrySet()) {
            if(!inventory.containsKey(entry.getKey()) || inventory.get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        
        return true;
    }
    
    public HashMap<ItemType, Integer> craft(UpdatedBlock updatedBlock) {
        for(Map.Entry<ItemType, Integer> entry : hmNeeded.entrySet()) {
            updatedBlock.removeItem(entry.getKey(), entry.getValue());
        }
        
        return new HashMap<>(hmCrafted);
    }
    
    public int getSize() {
        return hmNeeded.size();
    }

    public double getDuration() {
        return duration;
    }

    public String[] formattedNeededItems(boolean showDetails) {
        String[] result = Inventory.formatList(hmNeeded);
        
        if(showDetails) {
            int i = 0;
            for(Map.Entry<ItemType, Integer> entry : hmNeeded.entrySet()) {
                double value = entry.getValue()/duration;
                if(value%1 == 0) {
                    result[i] += " ~" + (int)value + "/second";
                } else {
                    result[i] += " ~" + value + "/second";
                }
                i++;
            }
        }
        
        return result;
    }
    
    public String[] formattedCraftedItems(boolean showDetails) {
        String[] result = Inventory.formatList(hmCrafted);
        
        if(showDetails) {
            int i = 0;
            for(Map.Entry<ItemType, Integer> entry : hmCrafted.entrySet()) {
                double value = entry.getValue()/duration;
                if(value%1 == 0) {
                    result[i] += " ~" + (int)value + "/second";
                } else {
                    result[i] += " ~" + value + "/second";
                }
                i++;
            }
        }
        
        return result;
    }
}
