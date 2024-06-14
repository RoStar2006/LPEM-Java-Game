/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Projectiles.Base;

import Core.StatsManager;
import World.World;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

/**
 *
 * @author RupRo053
 */
public class ProjectilePool {
    private static final HashMap<Class<? extends Projectile>, Deque<Projectile>> hmObjectPool = new HashMap<>();
    private final static int maxSize = 1000;
    private final static Class[] paramTypesSub = { double.class, double.class, double.class, double.class, double.class, double.class, StatsManager.class, StatsManager.class }; // The costructor template for the projectile creation
    
    public static Projectile get(Class<? extends Projectile> pClass, StatsManager source, StatsManager target, double spread, double range, double damage) {
        if(!hmObjectPool.containsKey(pClass)) {
            hmObjectPool.put(pClass, new ArrayDeque<>());
        }
        
        Deque<Projectile> tempStack = hmObjectPool.get(pClass);
        Projectile p = tempStack.poll();
        
        double angle = Math.atan2(target.getCenterY() - source.getCenterY(), target.getCenterX() - source.getCenterX()) + Math.toRadians(spread);
        double xDir = Math.cos(angle);
        double yDir = Math.sin(angle);
        
        if(p == null) {
            Object[] paramValuesSub = {source.getCenterX(), source.getCenterY(), xDir, yDir, range, damage, source, target};
            
            try {
                p = pClass.getDeclaredConstructor(paramTypesSub).newInstance(paramValuesSub); 
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                System.err.println("ERROR while creating projectile!");
            }
        } else {
            p.fullInit(source.getCenterX(), source.getCenterY(), xDir, yDir, range, damage, source, target);
        }
        
        return p;
    }
    
    public static void queue(Projectile p) {
        if(hmObjectPool.containsKey(p.getClass()) && hmObjectPool.size() < maxSize) {
            hmObjectPool.get(p.getClass()).push(p);
        }
    }
}
