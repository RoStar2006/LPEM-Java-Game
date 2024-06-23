/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Miners;

import Blocks.Base.NodeCollector;
import Blocks.Nodes.CoalNode;
import Blocks.Nodes.CopperNode;
import Blocks.Nodes.IronNode;
import Items.ItemType;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class MinerMK1 extends NodeCollector {
    
    public MinerMK1(int x, int y) {
        super(x, y);
        initBlock("Miner MK1", 1, 150, 1, 1, Color.RED, new Color(255, 255, 255));
        setMiningSpeed(90);
        addNeededItem(ItemType.CopperOre, 30);
        resetAllowedNodes();
        addAllowedNode(IronNode.class);
        addAllowedNode(CopperNode.class);
        addAllowedNode(CoalNode.class);
    }
    
}
