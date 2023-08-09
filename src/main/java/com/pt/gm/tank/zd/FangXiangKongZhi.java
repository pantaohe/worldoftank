package com.pt.gm.tank.zd;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.util.ImgUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-09 15:51
 */
public class FangXiangKongZhi implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(FangXiangKongZhi.class);
    public static boolean xuyaoW = false;

    public static void kongzhi(int[] myAddr, int i) throws InterruptedException {


        int[] mubiao = StartMain.LU_XIAN.get(i);

        while (true) {
            logger.debug("进入方向控制循环");
            int lxjd = ZhanDouFun.jiaodu(myAddr, mubiao);
            int jdc = (myAddr[2] - lxjd + 360) % 360;
            int millis = (2 + (jdc % 180) / 3) * 100;

            double dd = ZhanDouFun.dd2(myAddr, mubiao);
            logger.debug("距离目标点{}-{}，当前点{}-{}距离平方{}", mubiao[0], mubiao[1], myAddr[0], myAddr[1], dd);
            if (dd > 16) {
                logger.debug("按下前进w");
                xuyaoW = true;
            }else {
                logger.debug("到达目标点，松开前进w");
                xuyaoW = false;
                return;     //达到小目标退出
            }

            logger.debug("方向分别为自己{}度-目标{}度", myAddr[2], lxjd);
            if (0 < jdc && jdc < 180) { //向右转往
                logger.debug("向右转{}ms", millis);
                StartMain.robot.keyPress(KeyEvent.VK_D);
                Thread.sleep(millis);
                StartMain.robot.keyRelease(KeyEvent.VK_D);
            } else {     //向左转往
                logger.debug("向左转{}ms", millis);
                StartMain.robot.keyPress(KeyEvent.VK_A);
                Thread.sleep(millis);
                StartMain.robot.keyRelease(KeyEvent.VK_A);
            }

//            Thread.sleep(200);
            BufferedImage screenshot = ImgUtils.screenshot();
            if (ZhanDouFun.jihui(screenshot)) return;       //战车被毁退出

            BufferedImage minMap = screenshot.getSubimage(StartMain.MAP_START[0], StartMain.MAP_START[1], ZhanDou.MIN_MAP_W, StartMain.SCRN_SIZE[1] - StartMain.MAP_START[1] + StartMain.SCRN_SIZE[2]);
            myAddr = ZhanDouFun.myAddr(minMap);
        }

    }


    @Override
    public void run() {
        boolean anxiaW = false;
        while (true){
            if (!anxiaW == xuyaoW) {
                if (xuyaoW) StartMain.robot.keyPress(KeyEvent.VK_W);
                else StartMain.robot.keyRelease(KeyEvent.VK_W);
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
