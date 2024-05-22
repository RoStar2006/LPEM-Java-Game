/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Enemies;

/**
 *
 * @author Romeo
 */
public class PlannedEnemy {
    private Enemy enemy;
    private double curCooldown;

    public PlannedEnemy(Enemy enemy, double cooldown) {
        this.enemy = enemy;
        this.curCooldown = cooldown;
    }
    
    public boolean update(double deltaTime) {
        curCooldown -= deltaTime;
        return curCooldown <= 0;
    }

    public Enemy getEnemy() {
        return enemy;
    }
}
