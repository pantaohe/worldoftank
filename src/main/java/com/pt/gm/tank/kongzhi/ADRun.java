package com.pt.gm.tank.kongzhi;

import com.pt.gm.tank.StartMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger logger = LoggerFactory.getLogger(Runnable.class);
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
                    logger.debug("方向{}，时间{}", AD.get(), T.get());
                    X();
                    AD.set(X);
                    T.set(X);
                }
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 锁定车身
     */
    public static void X() {
        logger.debug("锁定车身");
        StartMain.robot.keyPress(KeyEvent.VK_X);
        StartMain.robot.keyRelease(KeyEvent.VK_X);
    }
}
