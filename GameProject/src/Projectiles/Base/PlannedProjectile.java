/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Projectiles.Base;

/**
 *
 * @author Romeo
 */
public class PlannedProjectile {
    private Projectile projectile;
    private double cooldown;

    public PlannedProjectile(Projectile projectile, double cooldown) {
        this.projectile = projectile;
        this.cooldown = cooldown;
    }
    
    public boolean update(double deltaTime) {
        cooldown -= deltaTime;
        
        return cooldown <= 0;
    }

    public Projectile getProjectile() {
        return projectile;
    }
}
