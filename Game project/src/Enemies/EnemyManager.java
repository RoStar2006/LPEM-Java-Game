/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Enemies;

import Core.Camera;
import Core.Utils;
import Blocks.Core;
import World.World;
import World.WorldManager;
import java.util.ArrayList;

/**
 *
 * @author Romeo
 */
public class EnemyManager {
    private ArrayList<Enemy> alEnemies = new ArrayList<>();
    private ArrayList<PlannedEnemy> alPlannedEnemies = new ArrayList<>();
    private ArrayList<SpawnRegion> alSpawnRegions;
    
    public void update(double deltaTime, World world) {
        for(int i = alPlannedEnemies.size()-1; i >= 0; i--) {
            if(alPlannedEnemies.get(i).update(deltaTime)) {
                alEnemies.add(alPlannedEnemies.get(i).getEnemy());
                alPlannedEnemies.remove(i);
            }
        }
        
        for(int i = alEnemies.size()-1; i >= 0; i--) {
            Enemy enemy = alEnemies.get(i);
            enemy.move(deltaTime, world);
        }
    }
    
    public void spawnEnemies(Core core) {
        if(alSpawnRegions == null)  {
            return;
        }
        
        for(SpawnRegion spawnRegion : alSpawnRegions) {
            for(int i = 0; i < Utils.nextInt(3, 10); i++) {
                Enemy enemy = new Enemy(spawnRegion.getSpawnX(), spawnRegion.getSpawnY(), core);
                alPlannedEnemies.add(new PlannedEnemy(enemy, i*0.2));
            }
        }
    }

    public void setAlSpawnRegions(ArrayList<SpawnRegion> alSpawnRegions) {
        this.alSpawnRegions = alSpawnRegions;
    }
    
    public boolean isEnemyOnPos(int x, int y) {
        for(Enemy enemy : alEnemies) {
            if(enemy.getX() >= x - WorldManager.ENEMY_BUILD_DIST && enemy.getX() + enemy.getSize() <= x+1 + WorldManager.ENEMY_BUILD_DIST && 
                enemy.getY() >= y - WorldManager.ENEMY_BUILD_DIST && enemy.getY() + enemy.getSize() <= y+1 + WorldManager.ENEMY_BUILD_DIST ) {
                return true;
            }
        }
        
        return false;
    }
    
    public void draw(Camera c) {
        for(int i = alEnemies.size()-1; i >= 0; i--) {
            alEnemies.get(i).draw(c);
        }
    }
}
