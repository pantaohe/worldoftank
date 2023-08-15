package com.pt.gm.tank.mouse;

import com.pt.gm.tank.StartMain;

import java.awt.event.InputEvent;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-09 11:10
 */
public class MouseUtils {

    public static void mouseDianJi(int x, int y) throws InterruptedException {
        mouseDianJi(x, y, InputEvent.BUTTON1_DOWN_MASK);
    }

    public static void mouseDianJi(int x, int y, int mask) throws InterruptedException{
        mouseRand();
        StartMain.robot.mouseMove(x, y);
        mouseAnXia(mask);
        mouseRand();
    }

    public static void mouseAnXia() throws InterruptedException {
        mouseAnXia(InputEvent.BUTTON1_DOWN_MASK);
    }

    public static void mouseAnXia(int mask) throws InterruptedException {
        StartMain.robot.mousePress(mask);     //InputEvent.BUTTON1_DOWN_MASK
        Thread.sleep(160);
        StartMain.robot.mouseRelease(mask);
    }

    public static void mouseRand() throws InterruptedException {
        StartMain.robot.mouseMove((int)(Math.random() * 1920), 30 + (int)(Math.random() * 1000));
        Thread.sleep(500);
    }
}
