/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import java.awt.event.KeyEvent;

/**
 * This manages all of the game's user inputs, by redirecting the inputs to their correct methods.
 * @author Romeo
 */
public class Controls {
    private Camera camera;

    /**
     * @param camera 
     * The camera is needed to be able to redirect the mouse and keyboard inputs.
     */
    public Controls(Camera camera) {
        this.camera = camera;
    }
    
    /**
     * This manages the keyboard inputs.
     * @param keyCode
     * @param released 
     * This indicates whether a key is currently being pressed ({@code false}) or released ({@code true}).
     */
    public void keyInput(int keyCode, boolean released) {
        switch(keyCode) {
            case KeyEvent.VK_W -> camera.changeDir(0, !released);
            case KeyEvent.VK_S -> camera.changeDir(1, !released);
            case KeyEvent.VK_A -> camera.changeDir(2, !released);
            case KeyEvent.VK_D -> camera.changeDir(3, !released);
        }
    }
    
    /**
     * This manages the mouse wheel inputs.
     * @param dir 
     * (1) scrolling mouse wheel up (backwards) and (-1) scrolling mouse wheel down (forwards) 
     */
    public void wheelInput(int dir) {
        camera.changeZoom(dir);
    }
}
