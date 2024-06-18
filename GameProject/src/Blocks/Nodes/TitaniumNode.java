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
public class TitaniumNode extends Node {
    
    public TitaniumNode(int x, int y) {
        super(x, y);
        initNode("Titanium Node", new Color(91, 121, 142), ItemType.TitaniumOre, 2.5);
    }
    
}
