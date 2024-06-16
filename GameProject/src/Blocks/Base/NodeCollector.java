/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Base;

import Items.Inventory;
import Items.ItemType;
import World.World;
import java.awt.Color;
import java.util.Map;

/**
 *
 * @author Romeo
 */
public class NodeCollector extends UpdatedBlock {
    private Node node;
    private double maxCooldown = 1;
    private double curCooldown = 0;
    
    public NodeCollector(int x, int y) {
        super(x, y);
        initBlock("Node collector", 1, 250, 1, 1, Color.PINK);
        setBlockType(BlockType.NodeCollector);
        createInventory(20, 1, true);
        addAllowedNode(Node.class);
    }

    protected final void setMiningSpeed(double itemPerMinute) {
        this.maxCooldown = 60.0/itemPerMinute;
    }

    @Override
    public void update(double deltaTime, World world) {
        if(node == null) {
            return;
        }
        
        curCooldown += deltaTime;
        if(curCooldown >= maxCooldown) {
            curCooldown -= maxCooldown;
            forceAddItem(node.getCollectableResource(), 1);
        }
        
        if(!getInventory().isEmpty()) {
            for(int lX = -1; lX <= 1; lX++) {
                for(int lY = -1; lY <= 1; lY++) {
                    UpdatedBlock updatedBlock = getInputBlock(world, lX, lY);

                    if(updatedBlock  != null) {
                        for(Map.Entry<ItemType, Integer> entry : getInventory().entrySet()) {
                            int newQuantity = updatedBlock.addItem(entry.getKey(), entry.getValue());
                            if(newQuantity != -1 || newQuantity == entry.getValue()) {
                                removeItem(entry.getKey(), entry.getValue()-newQuantity);
                            }
                        }
                    }
                }
            }
        }
    }

    public void setNode(Node node) {
        this.node = node;
        setName(super.getName() + " (" + Inventory.formatName(node.getCollectableResource()) + ")");
    }
    
    /**
     * 
     * @return Returns the mining speed in items/sec
     */
    public double getMiningSpeed() {
        return 1.0/maxCooldown;
    }
    
}
