package com.pt.gm.tank.zd;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.jr.JiaRuZD;
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
    public static boolean xuyaoS = false;

    public static void kongzhi(int i) throws InterruptedException {

        int[] myAddr;
        int[] mubiao = StartMain.LU_XIAN.get(i);    double ddt1 = 0, ddt2 = 0;

        while (true) {
            logger.debug("进入方向控制循环");

            do {
                BufferedImage screenshot = ImgUtils.screenshot();
                String neirong = ImgUtils.getString(screenshot);
                if (neirong.contains("获得贴花") || JiaRuZD.jiarujiemian(screenshot)) {   //没有在咱都界面
                    ZhanDouFun.jieshuDY();
                    return;
                }
                if (ZhanDouFun.jihui(screenshot)) return;       //战车被毁退出

                BufferedImage minMap = screenshot.getSubimage(StartMain.MAP_START[0], StartMain.MAP_START[1], ZhanDou.MIN_MAP_W, StartMain.SCRN_SIZE[1] - StartMain.MAP_START[1] + StartMain.SCRN_SIZE[2]);
                myAddr = ZhanDouFun.myAddr(minMap);
                if (myAddr == null){
                    if (Math.random() < 0.5) {xuyaoW = false; xuyaoS = true;}
                    else {xuyaoS = false; xuyaoW = true;}
                    Thread.sleep(100);
                }
            }while (myAddr == null);

            int lxjd = ZhanDouFun.jiaodu(myAddr, mubiao);
            int jdc = (myAddr[2] - lxjd + 360) % 360;
            int zxjd = jdc > 180 ? (360 - jdc) : jdc;
            int millis = zxjd * 15;   // jdc 360-jdc

            double dd = ZhanDouFun.dd2(myAddr, mubiao);
            logger.debug("目标点{}-{}，当前点{}-{}距离平方{}", mubiao[0], mubiao[1], myAddr[0], myAddr[1], dd);
            if (Math.abs(ddt2 - dd) < 10) {        //被房子等卡住了，随机向左或向右2秒
                logger.debug("被房子等卡住了，随机向左或向右2.5秒");
                int vkX = Math.random() < 0.5 ? KeyEvent.VK_D : KeyEvent.VK_A;
                StartMain.robot.keyPress(vkX);
                Thread.sleep(2500);
                StartMain.robot.keyRelease(vkX);
                X();
            }
            if (dd > 800) {
                logger.debug("按下前进w");
                xuyaoS = false; xuyaoW = true;
                if (ddt1 == 0) Thread.sleep(200);        //前进控制的刷新率只有5hz
            }else if(dd > 150){
                logger.debug("松开前进s/w,距离比较近手动操作");
                xuyaoS = false; xuyaoW = false;     //距离比较近手动操作
                StartMain.robot.keyPress(KeyEvent.VK_W);
                Thread.sleep(2000);
                StartMain.robot.keyRelease(KeyEvent.VK_W);
                X();
            }else {
                xuyaoS = false; xuyaoW = false;
                return;     //达到小目标退出
            }
            ddt2 = ddt1; ddt1 = dd;
            logger.debug("方向分别为自己{}度-目标{}度", myAddr[2], lxjd);
            if (0 < jdc && jdc < 180) { //向右转往
                logger.debug("向右角度{}转{}ms", zxjd, millis);
                StartMain.robot.keyPress(KeyEvent.VK_D);
                Thread.sleep(millis);
                StartMain.robot.keyRelease(KeyEvent.VK_D);
            } else {     //向左转往
                logger.debug("向左角度{}转{}ms", zxjd, millis);
                StartMain.robot.keyPress(KeyEvent.VK_A);
                Thread.sleep(millis);
                StartMain.robot.keyRelease(KeyEvent.VK_A);
            }
            X();
        }

    }

    /**
     * 锁定车身
     */
    private static void X() {
        logger.debug("锁定车身");
        StartMain.robot.keyPress(KeyEvent.VK_X);
        StartMain.robot.keyRelease(KeyEvent.VK_X);
    }


    @Override
    public void run() {
        boolean anxiaW = false;
        boolean anxiaS = false;
        while (true){
            if (anxiaW != xuyaoW) {
                if (xuyaoW){
                    logger.debug("开始前进");
                    StartMain.robot.keyRelease(KeyEvent.VK_S);
                    anxiaS = false;

                    StartMain.robot.keyPress(KeyEvent.VK_W);
                    anxiaW = true;
                }else{
                    logger.debug("停止前进");
                    StartMain.robot.keyRelease(KeyEvent.VK_W);
                    anxiaW = false;
                    X();
                }
            }

            if (anxiaS != xuyaoS) {
                if (xuyaoS){
                    logger.debug("开始后退");
                    StartMain.robot.keyRelease(KeyEvent.VK_W);
                    anxiaW = false;

                    StartMain.robot.keyPress(KeyEvent.VK_S);
                    anxiaS = true;
                }else {
                    logger.debug("停止后退");
                    StartMain.robot.keyRelease(KeyEvent.VK_S);
                    anxiaS = false;
                    X();
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
