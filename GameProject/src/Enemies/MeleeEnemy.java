/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Enemies;

import Blocks.Base.Core;
import Enemies.Base.Enemy;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class MeleeEnemy extends Enemy {
    
    public MeleeEnemy(double x, double y, Core core) {
        super(x, y, core);
        initEnemy(15, 1, 1, 1, 7, 0.75, Color.RED);
        initWeight(50, 1);
    }
    
}
