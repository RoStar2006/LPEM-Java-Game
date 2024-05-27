/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Romeo
 */
public class Inventory {
    private HashMap<ItemType, Integer> hmItems = new HashMap<>();
    private ArrayList<ItemType> alWhitelist = new ArrayList<>();
    private int maxStackSize;
    private int maxSize;
    private boolean outputOnly;

    public Inventory(int maxStackSize, int maxSize, boolean outputOnly) {
        this.maxStackSize = maxStackSize;
        this.maxSize = maxSize;
        this.outputOnly = outputOnly;
    }
    
    /**
     * This method removes items from the inventory.
     * @param itemType
     * @param quantity
     * @return This return the excess of the operation, meaning everything that went under 0. <br>
     * This method returns -1 if an error occured like the given item not being in the whitelist.
     */
    public int removeItem(ItemType itemType, int quantity) {
        if(!isWhitelisted(itemType)) {
            return -1;
        }
        
        hmItems.putIfAbsent(itemType, 0);
        int curQuantity = hmItems.getOrDefault(itemType, 0);
        curQuantity -= quantity;
        quantity = 0;
        
        if(curQuantity <= 0) {
            quantity = Math.abs(curQuantity);
            curQuantity = 0;
            hmItems.remove(itemType);
        } else {
            hmItems.put(itemType, curQuantity);
        }
        return quantity;
    }
    
    /**
     * This method adds items from the inventory.
     * @param itemType
     * @param quantity
     * @return This return the excess of the operation, meaning everything that went over the stack size. <br>
     * This method returns -1 if an error occured like the given item not being in the whitelist.
     */
    public int addItem(ItemType itemType, int quantity) {
        if(!isWhitelisted(itemType) || (maxSize > 0 && hmItems.size() >= maxSize && !hmItems.containsKey(itemType))) {
            return -1;
        }
        
        hmItems.putIfAbsent(itemType, 0);
        int curQuantity = hmItems.getOrDefault(itemType, 0);
        curQuantity += quantity;
        quantity = 0;
        
        if(curQuantity > maxStackSize) {
            quantity = curQuantity - maxStackSize;
            curQuantity = maxStackSize;
        }
        
        hmItems.put(itemType, curQuantity);
        return quantity;
    }
    
    public boolean containsItems(ItemType itemType, int quantity) {
        return hmItems.getOrDefault(itemType, Integer.MIN_VALUE) >= quantity;
    }
    
    public boolean isWhitelisted(ItemType itemType) {
        if(alWhitelist.isEmpty()) {
            return true;
        } else {
            return alWhitelist.contains(itemType);
        }
    }
    
    public void addWhitelist(ItemType itemType) {
        alWhitelist.add(itemType);
        hmItems.put(itemType, 0);
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public HashMap<ItemType, Integer> getHmItems() {
        return hmItems;
    }

    /**
     * This method should be called, when an external blocks wants to input items
     * @return 
     */
    public boolean isOutputOnly() {
        return outputOnly;
    }

    @Override
    public String toString() {
        String result = "";
        String[] strArr = formatList(hmItems);
        
        for(String str : strArr) {
            result += str + "\n";
        }
        
        return result;
    }
    
    public static String[] formatList(HashMap<ItemType, Integer> inventory) {
        String[] strArr = new String[inventory.size()];
        
        int i = 0;
        for(Map.Entry<ItemType, Integer> entry : inventory.entrySet()) {
            strArr[i] = entry.getValue() + "x " + formatName(entry.getKey()); // Splits the name at capital letters
            i++;
        }
        
        return strArr;
    }
    
    public static String formatName(ItemType itemType) {
        return itemType.toString().replaceAll("(?<=[a-z])([A-Z])", " $1");
    }
    
}
