/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Base;

import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class Node extends Block {
    private ItemType collectableResource;
    
    public Node(int x, int y) {
        super(x, y);
        initNode("Node", Color.BLACK, ItemType.DefaultItem);
    }

    protected void initNode(String name, Color color, ItemType collectableResource) {
        super.initBlock(name, 0, 0, 1, 1, color);
        setCollectableResource(collectableResource);
    }
    
    private void setCollectableResource(ItemType itemType) {
        this.collectableResource = itemType;
    }

    public ItemType getCollectableResource() {
        return collectableResource;
    }
    
}
