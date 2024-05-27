/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks;

import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class CopperNode extends Node {
    
    public CopperNode(int x, int y) {
        super(x, y);
        initNode("Copper node", Color.ORANGE, ItemType.CopperOre);
    }
    
}
