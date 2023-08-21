package com.pt.gm.tank.kongzhi;

import com.pt.gm.tank.StartMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-15 10:53
 */
public class WSRun  implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(WSRun.class);



    @Override
    public void run() {
        boolean anxiaW = false;
        boolean anxiaS = false;
        while (true){
            if (anxiaW != FangXiangKongZhi.xuyaoW) {
                if (FangXiangKongZhi.xuyaoW){
                    logger.debug("开始前进");
                    StartMain.robot.keyRelease(KeyEvent.VK_S);
                    anxiaS = false;

                    StartMain.robot.keyPress(KeyEvent.VK_W);
                    anxiaW = true;
                }else{
                    logger.debug("停止前进");
                    StartMain.robot.keyRelease(KeyEvent.VK_W);
                    anxiaW = false;
                }
            }

            if (anxiaS != FangXiangKongZhi.xuyaoS) {
                if (FangXiangKongZhi.xuyaoS){
                    logger.debug("开始后退");
                    StartMain.robot.keyRelease(KeyEvent.VK_W);
                    anxiaW = false;

                    StartMain.robot.keyPress(KeyEvent.VK_S);
                    anxiaS = true;
                }else {
                    logger.debug("停止后退");
                    StartMain.robot.keyRelease(KeyEvent.VK_S);
                    anxiaS = false;
                }
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
