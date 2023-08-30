package com.pt.gm.tank.kongzhi;

import com.pt.gm.tank.config.CF;
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
                int ad = AD.get();
                int time = T.get();
                if (ad != X && time != X) {

                    CF.robot.keyPress(ad);
                    Thread.sleep(time);
                    CF.robot.keyRelease(ad);
                    logger.debug("方向{}，时间{}", ad, time);
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
    public static void X() throws InterruptedException {
        logger.debug("锁定车身");
        CF.robot.keyPress(KeyEvent.VK_X);
        Thread.sleep(50);
        CF.robot.keyRelease(KeyEvent.VK_X);
    }
}
