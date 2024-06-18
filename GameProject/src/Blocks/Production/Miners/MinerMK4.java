/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Production.Miners;

import Blocks.Base.NodeCollector;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class MinerMK4 extends NodeCollector {
    
    public MinerMK4(int x, int y) {
        super(x, y);
        initBlock("Miner MK4", 1, 250, 1, 1, Color.RED, new Color(255, 255, 0));
        setMiningSpeed(270);
    }
    
}
