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
public class EliteEnemy  extends Enemy {
    
    public EliteEnemy(double x, double y, Core core) {
        super(x, y, core);
        initEnemy(75, 3, 1, 1, 5, 1.5, Color.GREEN);
        initWeight(100, 20);
    }
    
}
