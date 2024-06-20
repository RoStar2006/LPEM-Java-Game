/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Enemies;

import Blocks.Base.Core;
import Enemies.Base.Enemy;
import Projectiles.Base.Projectile;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class RangedEnemy extends Enemy {
    
    public RangedEnemy(double x, double y, Core core) {
        super(x, y, core);
        initEnemy(15, 3, 0.75, 5, 6, 0.5, Color.YELLOW);
        initWeight(50, 2);
        setProjectile(Projectile.class);
    }
    
}
