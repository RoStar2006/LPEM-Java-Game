/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Projectiles;

import Core.Camera;
import World.World;
import java.util.ArrayList;

/**
 *
 * @author Romeo
 */
public class ProjectileManager {
    private ArrayList<Projectile> alProjectiles = new ArrayList<>();
    
    public void update(double deltaTime, World world) {
        for(int i = alProjectiles.size()-1; i >= 0; i--) {
            if(alProjectiles.get(i).update(deltaTime, world)) {
                ProjectilePool.queue(alProjectiles.get(i));
                alProjectiles.remove(i);
            }
        }
    }
    
    public void draw(Camera c) {
        for (int i = alProjectiles.size() - 1; i >= 0; i--) {
            if (i < alProjectiles.size()) {
                alProjectiles.get(i).draw(c);
            } else {
                i = alProjectiles.size() - 1;
            }
        }
    }

    public boolean add(Projectile e) {
        return alProjectiles.add(e);
    }
}
