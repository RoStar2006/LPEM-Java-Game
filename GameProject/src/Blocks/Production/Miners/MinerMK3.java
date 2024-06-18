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
public class MinerMK3 extends NodeCollector {
    
    public MinerMK3(int x, int y) {
        super(x, y);
        initBlock("Miner MK3", 1, 200, 1, 1, Color.RED, new Color(255, 255, 85));
        setMiningSpeed(180);
    }
    
}
