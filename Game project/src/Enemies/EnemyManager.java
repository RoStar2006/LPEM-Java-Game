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
            if(enemy.getStatsManager().isDead()) {
                alEnemies.remove(i);
            } else {
                enemy.move(deltaTime, world);
            }
        }
    }
    
    public void spawnEnemies(Core core) {
        if(alSpawnRegions == null)  {
            return;
        }
        
        for(SpawnRegion spawnRegion : alSpawnRegions) {
            for(int i = 0; i < Utils.nextInt(3, 10); i++) {
                Enemy enemy = new Enemy(spawnRegion.getRandomSpawnX(), spawnRegion.getRandomSpawnY(), core);
                alPlannedEnemies.add(new PlannedEnemy(enemy, i*0.2));
            }
        }
    }
    
    public Enemy getNearestEnemy(double x, double y, double range) {
        Enemy result = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for(Enemy enemy : alEnemies) {
            if(!enemy.getStatsManager().isDead()) {
                double d = Math.sqrt(Math.pow(x-enemy.getX(), 2) + Math.pow(y-enemy.getY(), 2));
                if(d <= range && d < minDistance) {
                    minDistance = d;
                    result = enemy;
                }
            }
        }
        
        return result;
    }
    
    public ArrayList<Enemy> getNearestEnemyList(double x, double y, double range) {
        ArrayList<Enemy> result = new ArrayList<>();
        ArrayList<Double> alDistances = new ArrayList<>();
        
        for(Enemy enemy : alEnemies) {
            if(!enemy.getStatsManager().isDead()) {
                double d = Math.sqrt(Math.pow(x-enemy.getX(), 2) + Math.pow(y-enemy.getY(), 2));
                if(d <= range) {
                    int j = 0;
                    for(; j < alDistances.size(); j++) {
                        if(d <= alDistances.get(j)) {
                            break;
                        }
                    }
                    alDistances.add(j, d);
                    result.add(j, enemy);
                }
            }
        }
        
        return result;
    }
    
    public boolean isEnemyOnPos(int x, int y) {
        for(int i = alEnemies.size()-1; i >= 0; i--) {
            Enemy enemy = alEnemies.get(i);
            if(enemy.getX() >= x - WorldManager.ENEMY_BUILD_DIST && enemy.getX() + enemy.getSize() <= x+1 + WorldManager.ENEMY_BUILD_DIST && 
                enemy.getY() >= y - WorldManager.ENEMY_BUILD_DIST && enemy.getY() + enemy.getSize() <= y+1 + WorldManager.ENEMY_BUILD_DIST ) {
                return true;
            }
        }
        
        return false;
    }

    public ArrayList<Enemy> getAlEnemies() {
        return alEnemies;
    }

    public void setAlSpawnRegions(ArrayList<SpawnRegion> alSpawnRegions) {
        this.alSpawnRegions = alSpawnRegions;
    }
    
    public void draw(Camera c) {
        for(int i = alEnemies.size()-1; i >= 0; i--) {
            alEnemies.get(i).draw(c);
        }
    }
}
