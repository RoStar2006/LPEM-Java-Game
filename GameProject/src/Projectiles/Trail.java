/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Projectiles;

import Projectiles.Base.Projectile;
import Core.Camera;
import Core.StatsManager;

/**
 *
 * @author Romeo
 */
public class Trail extends Projectile {
    
    public Trail(double startX, double startY, double xDir, double yDir, double range, double damage, StatsManager source, StatsManager target) {
        super(startX, startY, xDir, yDir, range, damage, source, target);
        
        init(-0.25, 0.25, -1);
        setFullLength(true);
    }

    @Override
    public void draw(Camera c) {
        c.drawLine(getTrailStartX()+getTrailOffsetX(), getTrailStartY()+getTrailOffsetY(), x-getTrailOffsetX(), y-getTrailOffsetY(), getTrailWidth()/3, coreColor);
        // c.drawLine(getTrailStartX(), getTrailStartY(), x, y, getTrailWidth(), trailColor);
    }
    
}
