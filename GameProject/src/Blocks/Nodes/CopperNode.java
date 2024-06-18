/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Nodes;

import Blocks.Base.Node;
import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class CopperNode extends Node {
    
    public CopperNode(int x, int y) {
        super(x, y);
        initNode("Copper Node", Color.ORANGE, ItemType.CopperOre);
        setSpawnsInChunk(5);
    }
    
}
