/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Miners;

import Blocks.Base.NodeCollector;
import Blocks.Nodes.CopperNode;
import Blocks.Nodes.IronNode;
import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class MinerMK2 extends NodeCollector {
    
    public MinerMK2(int x, int y) {
        super(x, y);
        initBlock("Miner MK2", 1, 175, 1, 1, Color.RED, new Color(255, 255, 170));
        setMiningSpeed(120);
        addNeededItem(ItemType.CopperIngot, 25);
        addNeededItem(ItemType.IronIngot, 35);
        addAllowedNode(IronNode.class);
        addAllowedNode(CopperNode.class);
    }
    
}
