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
public class MinerMK1 extends NodeCollector {
    
    public MinerMK1(int x, int y) {
        super(x, y);
        initBlock("Miner MK1", 1, 200, 1, 1, Color.RED, Color.LIGHT_GRAY);
        setMiningSpeed(60);
    }
    
}
