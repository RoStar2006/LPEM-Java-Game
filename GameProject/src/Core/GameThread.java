package Core;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * This class uses the {@link #runLoop()} method to call the {@link MainLogic#update()} and {@link DrawPanel#repaint()} methods. <br>
 * The game loop runs on another thread, separated from the main thread, and ensures that the game runs in a consistent deltaTime. 
 * 
 * @author Romeo
 */
public class GameThread {
    private MainLogic mainLogic;
    private DrawPanel drawPanel;
    
    private ExecutorService executorService;
    private boolean useThread = true;
    
    /**
     * FPS - Frames per second
     */
    public static int FPS = 60;
    /**
     * UPS - Updates per second
     */
    public static int UPS = 100;
    
    /**
     * The {@link MainLogic} and {@link DrawPanel} are needed for the game loop to work. <br>
     * Both will not modified in any way and the only called methods are {@link MainLogic#update()} and {@link DrawPanel#repaint()}.
     * @param mainLogic
     * @param drawPanel 
     */
    public GameThread(MainLogic mainLogic, DrawPanel drawPanel) {
        this.mainLogic = mainLogic;
        this.drawPanel = drawPanel;
        
        initExecutor();
    }
    
    /**
     * This method instantiates a new {@link Executors#newSingleThreadExecutor()} running the {@link #runLoop()} method on another thread, to prevent blocking the main thread and thus making the application unresponsive.
     */
    private void initExecutor() {
        if(executorService != null) {
            executorService.shutdownNow();
        }
        
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            runLoop();
            
            return null;
        });
    }

    /**
     * This runs a while loop, calling {@link MainLogic#update()} and {@link DrawPanel#repaint()} at a fixed delay. <br>
     * This delay is specified by the {@link #FPS} and {@link #UPS} values. <br>
     * If both methods have a large time gap until their next call, then {@link  Thread#sleep(long)} will be called, easing the load on the CPU.
     */
    private void runLoop() {
        long lastDraw = System.nanoTime();
        long lastUpdate = System.nanoTime();
        final long delay = 1500000;
        final long updateWait = 1000000000/GameThread.UPS;
        final long delayUpdateWait = updateWait - delay;
        final long drawWait = 1000000000/GameThread.FPS;
        final long delayDrawWait = drawWait - delay;
        
        while(true) {
            int sleepDelay = 100;
            
            if(useThread) {
                long updateDelta = System.nanoTime() - lastUpdate;
                if(updateDelta >= updateWait) {
                    lastUpdate = System.nanoTime();
                    mainLogic.update();
                    sleepDelay = 0;
                } else if(updateDelta >= delayUpdateWait) {
                    sleepDelay = 0;
                } else {
                    sleepDelay = (int)((delayUpdateWait - updateDelta)/1000000);
                }

                long drawDelta = System.nanoTime() - lastDraw;
                if(drawDelta >= drawWait) {
                    lastDraw = System.nanoTime();
                    drawPanel.repaint();
                    sleepDelay = 0;
                } else if(drawDelta >= delayDrawWait) {
                    sleepDelay = 0;
                } else if(sleepDelay != 0) {
                    sleepDelay = Math.min(sleepDelay, (int)((delayDrawWait - drawDelta)/1000000));
                }
            }
            
            if(sleepDelay > 0) {
                try {
                    Thread.sleep(sleepDelay);
                } catch(InterruptedException ex) {
                    Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * Toggles the {@link MainLogic#update()} and {@link DrawPanel#repaint()} calls inside the game loop on and off
     * @param state 
     */
    public void toggleUpdate(boolean state) {
        useThread = state;
    }
}
