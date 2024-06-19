/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Enemies.Base;

import Core.Camera;
import Core.Utils;
import Blocks.Base.Core;
import World.World;
import World.WorldManager;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Romeo
 */
public class EnemyManager {
    private ArrayList<Enemy> alEnemies = new ArrayList<>();
    private ArrayList<PlannedEnemy> alPlannedEnemies = new ArrayList<>();
    private ArrayList<SpawnRegion> alSpawnRegions;
    
    private ArrayList<Enemy> alAvailableEnemies = new ArrayList<>();
    private ArrayList<Enemy> alAvailableEliteEnemies = new ArrayList<>();
    private final static Class[] paramTypesSub = { double.class, double.class, Core.class }; // The costructor template for the enemy creation
    private final static Object [] paramValuesSub = {0, 0, null}; // Dummy value, are replaced when the enemy object is successfully created
    private double accumulatedWeigth = 0;
    private double accumulatedEliteWeigth = 0;
    private final int eliteCostThreshhold = 10;
    /**
     * Max cost spend on non elite enemies
     */
    private final int minNonEliteCost = 50;
    private final double nonEliteRate = 0.4;
    private final int initialCost = 10;
    private final int eliteWaveCost = 25;
    private final int eliteWaveFrequency = 10;
    /**
     * This number indicates how many percent it can go over budget. <br>
     * <ul>
     * <li>0.5 meaning 50%</li>
     * <li>0.75 meaning 25%</li>
     * <li>0.1 meaning 90%</li>
     * </ul>
     */
    private final double eliteTolerance = 0.6;
    private int remainingCost = 0;
    private int remainingBasicCost = 0;
    private int curWave = 1;
    private final int maxEnemyDelay = 2; // seconds
    
    public EnemyManager() {
        initEnemies();
    }
    
    /**
     * This method uses the ClassGraph library to collect every available enemy
     * https://repo1.maven.org/maven2/io/github/classgraph/classgraph/
     */
    private void initEnemies() {
        try (ScanResult scanResult = new ClassGraph().acceptPackages("Enemies").enableClassInfo().scan()) {
            List<Class<Enemy>> alEnemyClasses = scanResult.getSubclasses(Enemy.class.getName()).loadClasses(Enemy.class);
            
            for(Class<Enemy> eC : alEnemyClasses) {
                if(!eC.getPackage().getName().contains("Base")) {
                    Enemy enemy = createEnemy(eC);
                    if(enemy != null) {
                        if(enemy.getCost() < eliteCostThreshhold) {
                            alAvailableEnemies.add(enemy);
                        } else {
                            alAvailableEliteEnemies.add(enemy);
                        }
                    }
                }
            }
        }
        
        if(alAvailableEnemies.isEmpty()) {
            System.err.println("ERROR: No enemies were found!");
        } else {
            calculateWeights();
        }
    }
    
    private void calculateWeights() {
        accumulatedWeigth = 0;
        accumulatedEliteWeigth = 0;
        
        for(Enemy enemy : alAvailableEnemies) {
            accumulatedWeigth += enemy.getChance();
            enemy.setWeight(accumulatedWeigth);
        }
        
        for(Enemy enemy : alAvailableEliteEnemies) {
            accumulatedEliteWeigth += enemy.getChance();
            enemy.setWeight(accumulatedEliteWeigth);
        }
    }
    
    public void update(double deltaTime, World world) {
        for(int i = alPlannedEnemies.size()-1; i >= 0; i--) { // Spawn planned enemies
            if(alPlannedEnemies.get(i).update(deltaTime)) {
                alEnemies.add(alPlannedEnemies.get(i).getEnemy());
                alPlannedEnemies.remove(i);
            }
        }
        
        for(int i = alEnemies.size()-1; i >= 0; i--) { // Update enemies
            Enemy enemy = alEnemies.get(i);
            if(enemy.getStatsManager().isDead()) {
                alEnemies.remove(i);
            } else {
                enemy.move(deltaTime, world);
            }
        }
    }
    
    public void draw(Camera c) {
        for(int i = alEnemies.size()-1; i >= 0; i--) {
            alEnemies.get(i).draw(c);
        }
    }
    
    /**
     * Spawn enemies in the spawn regions, every spawn region spawns the same enemies. <br>
     * If multiple spawn regions exist, then the enemy number in the wave is reduced a bit.
     * @param core 
     */
    public void spawnEnemies(Core core) {
        if(alSpawnRegions == null || alSpawnRegions.isEmpty())  {
            return;
        }
        
        remainingCost = initialCost + (int)Math.floor((initialCost*curWave + eliteWaveCost*(int)Math.round((curWave-eliteWaveFrequency/2.0)/eliteWaveFrequency)) * Math.sqrt(curWave/(double)eliteWaveFrequency));
        remainingBasicCost = (int)Math.max(Math.min(remainingCost, minNonEliteCost), remainingCost*nonEliteRate);
        // System.out.println("True Wave (" + curWave + "): cost = " + remainingCost);
        
        remainingCost /= alSpawnRegions.size();
        remainingBasicCost /= alSpawnRegions.size();
        /*System.out.println("Wave (" + curWave + "): cost = " + remainingCost);
        System.out.println("Basic Wave (" + curWave + "): cost = " + remainingBasicCost);
        System.out.println("Elite (" + curWave + "): cost = " + (remainingCost-remainingBasicCost));*/
        
        curWave++;
        
        int delay = 0;
        while(remainingCost > 0) {
            Class<? extends Enemy> enemyClass = getRandomEnemyClass();

            if(enemyClass != null) {
                for(SpawnRegion spawnRegion : alSpawnRegions) {
                    Enemy enemy = createEnemy(enemyClass);
                    enemy.init(spawnRegion.getRandomSpawnX(), spawnRegion.getRandomSpawnY(), core);
                    alPlannedEnemies.add(new PlannedEnemy(enemy, delay));
                }
                if(delay >= maxEnemyDelay) {
                    delay = 0;
                } else {
                    delay += 0.2;
                }   
            }
        }
    }
    
    private Class<? extends Enemy> getRandomEnemyClass() {
        return getRecursiveRandomEnemyClass(0);
    }
    
    private Class<? extends Enemy> getRecursiveRandomEnemyClass(int tries) {
        if(tries >= 15) {
            return null;
        } else if(tries == 3) {
            if(remainingBasicCost > 0) { // Failed 3 times while getting basic enemy, continue with elite
                remainingBasicCost = 0;
            } else { // Failed 3 times while getting elite enemy
                remainingBasicCost = remainingCost;     
            }
        } 
        
        if(remainingBasicCost > 0) {
            double randomWeight = Utils.nextDouble()*accumulatedWeigth;
            for(Enemy enemy : alAvailableEnemies) {
                if(enemy.getWeight() >= randomWeight && remainingBasicCost >= enemy.getCost()) {
                    remainingCost -= enemy.getCost();
                    remainingBasicCost -= enemy.getCost();
                    return enemy.getClass();
                }
            }
        } else {
            double randomWeight = Utils.nextDouble()*accumulatedEliteWeigth;
            
            for(Enemy enemy : alAvailableEliteEnemies) {
                if(enemy.getWeight() >= randomWeight && remainingCost >= enemy.getCost()*eliteTolerance) {
                    remainingCost -= enemy.getCost();
                    return enemy.getClass();
                }
            }
        }
        
        return getRecursiveRandomEnemyClass(tries+1);
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
            if(Math.round(enemy.getX()) + enemy.getRadius() >= x - WorldManager.ENEMY_BUILD_DIST && Math.round(enemy.getX()) - enemy.getRadius() <= x + WorldManager.ENEMY_BUILD_DIST && 
                Math.round(enemy.getY()) + enemy.getRadius() >= y - WorldManager.ENEMY_BUILD_DIST && Math.round(enemy.getY()) - enemy.getRadius() <= y + WorldManager.ENEMY_BUILD_DIST ) {
                return true;
            }
        }
        
        return false;
    }
    
    private static Enemy createEnemy(Class<? extends Enemy> enemyClass) {
        Enemy enemy = null;
        
        try {
            enemy = enemyClass.getDeclaredConstructor(paramTypesSub).newInstance(paramValuesSub);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.err.println("ERROR while creating enemy!");
        }
        
        return enemy;
    }

    public ArrayList<Enemy> getAlEnemies() {
        return alEnemies;
    }

    public void setAlSpawnRegions(ArrayList<SpawnRegion> alSpawnRegions) {
        this.alSpawnRegions = alSpawnRegions;
    }

    public int getCurWave() {
        return curWave;
    }
}
