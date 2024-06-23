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
public class CoalNode extends Node {
    
    public CoalNode(int x, int y) {
        super(x, y);
        initNode("Coal Node", Color.BLACK, ItemType.Coal, 3);
    }
    
}
