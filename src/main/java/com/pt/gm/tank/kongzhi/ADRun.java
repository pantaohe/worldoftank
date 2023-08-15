package com.pt.gm.tank.kongzhi;

import com.pt.gm.tank.StartMain;

import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-15 10:56
 */
public class ADRun  implements Runnable{

    public static final int X = 0;

    public static AtomicInteger AD = new AtomicInteger(X);
    public static AtomicInteger T = new AtomicInteger(X);

    @Override
    public void run() {
        while (true) {
            try {
                if (AD.get() != X && T.get() != X) {

                    StartMain.robot.keyPress(AD.get());
                    Thread.sleep(T.get());
                    StartMain.robot.keyRelease(AD.get());

                    AD.set(X);
                    T.set(X);
                }
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
