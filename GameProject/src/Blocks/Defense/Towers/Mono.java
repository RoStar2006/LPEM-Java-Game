/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Defense.Towers;

import Blocks.Base.Tower;
import Items.ItemType;
import Projectiles.Base.Projectile;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class Mono extends Tower {
    
    public Mono(int x, int y) {
        super(x, y);
        initBlock("Mono", 300, 1, 1, Color.YELLOW, Color.LIGHT_GRAY);
        initTower(Projectile.class, 1, 15, 0, 1);
        addProjectileCost(ItemType.IronOre, 1, 3);
        addProjectileCost(ItemType.CopperOre, 1, 2);
    }
    
}
