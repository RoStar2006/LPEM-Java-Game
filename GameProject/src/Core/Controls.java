/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import Menus.MenuManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * This manages all of the game's user inputs, by redirecting the inputs to their correct methods.
 * @author Romeo
 */
public class Controls {
    private Camera camera;
    private MenuManager menuManager;
    
    /**
     * The camera is needed to be able to redirect the mouse and keyboard inputs.
     * @param camera 
     * @param menuManager
     */
    public Controls(Camera camera, MenuManager menuManager) {
        this.camera = camera;
        this.menuManager = menuManager;
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
            case KeyEvent.VK_ESCAPE -> {
                if(!released) {
                    menuManager.togglePause();
                }
            }
        }
    }
    
    /**
     * This manages the mouse wheel inputs. <br>
     * (1) scrolling mouse wheel up (backwards) and (-1) scrolling mouse wheel down (forwards) 
     * @param evt
     */
    public void wheelInput(MouseWheelEvent evt) {
        if(!menuManager.mouseWheelInput(evt)) {
            camera.changeZoom(evt.getWheelRotation());
        }
    }
    
    /**
     * This manages the mouse inputs.
     * @param evt
     * @param released 
     * This indicates whether a key is currently being pressed ({@code false}) or released ({@code true}).
     */
    public void mouseInput(MouseEvent evt, boolean released) {
        switch(evt.getButton()) {
            case MouseEvent.BUTTON1 -> menuManager.mouseClickInput(evt.getPoint().x, evt.getPoint().y, true, released);
            case MouseEvent.BUTTON3 -> menuManager.mouseClickInput(evt.getPoint().x, evt.getPoint().y, false, released);
        }
    }
    
    public void mouseMoved(MouseEvent evt) {
        camera.updateMousePos(evt.getX(), evt.getY());
    }
}
