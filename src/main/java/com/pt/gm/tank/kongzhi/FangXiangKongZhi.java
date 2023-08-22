package com.pt.gm.tank.kongzhi;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.jr.JiaRuZD;
import com.pt.gm.tank.util.ImgUtils;
import com.pt.gm.tank.zhandou.ZhanDou;
import com.pt.gm.tank.zhandou.ZhanDouFun;
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
public class FangXiangKongZhi{
    private static Logger logger = LoggerFactory.getLogger(FangXiangKongZhi.class);

    public static boolean xuyaoW = false;
    public static boolean xuyaoS = false;
    public static boolean isfx = false;

    public static void kongzhi(int i) throws InterruptedException {

        int[] myAddr;
        int[] mubiao = StartMain.LU_XIAN.get(i);    double ddt1 = 0, ddt2 = 0, dd;
        int vkX, feiZhanDou = 0;long l = 0;BufferedImage minMap;
        while (true) {
            logger.debug("进入方向控制循环：\n");

            do {
                l = System.currentTimeMillis();
                BufferedImage screenshot = ImgUtils.screenshot();
                if (!ZhanDou.zhandouFX(screenshot) ) {   //没有在咱都界面
                    if ( feiZhanDou++ > 15) {
                        ZhanDouFun.jieshuDY();
                        return;
                    }
                }else feiZhanDou = 0;
                if (ZhanDouFun.jihui(screenshot)) return;       //战车被毁退出

                minMap = screenshot.getSubimage(StartMain.MAP_START[0], StartMain.MAP_START[1], ZhanDou.MIN_MAP_W, StartMain.SCRN_SIZE[1] - StartMain.MAP_START[1] + StartMain.SCRN_SIZE[2]);
                myAddr = ZhanDouFun.myAddr(minMap);
                logger.debug("截图到计算出自己位置耗时：{}ms", System.currentTimeMillis() - l);
                if (myAddr == null){
//                    if (Math.random() < 0.5) {xuyaoW = false; xuyaoS = true;}
//                    else {xuyaoS = false; xuyaoW = true;}
                    logger.debug("未能正确获取自己坐标，前进");
                    xuyaoW = true; xuyaoS = false;
                    Thread.sleep(100);
                }
            }while (myAddr == null);

            dd = ZhanDouFun.dd2(myAddr, mubiao);
            logger.debug("目标点{}-{}，当前点{}-{}距离平方{}", mubiao[0], mubiao[1], myAddr[0], myAddr[1], dd);
            if (ddt1 != 0 && (dd - ddt1 > 10000 || ddt1 - dd > 35000)) {
                logger.debug("当前坐标点跨度太大，抛弃此次位置");
                continue;     //如果单次变动太大，说明是错误的。
            }

            //鼠标控制
//            Thread paotat = new Thread(new PaoTaKongZhi(minMap, myAddr));
//            paotat.setName("paotaThread");
//            paotat.start();

            if (dd < 180) {
                xuyaoS = false; xuyaoW = false;
                return;     //达到小目标退出
            }

            int lxjd = ZhanDouFun.jiaodu(myAddr, mubiao);
            int jdc = (myAddr[2] - lxjd + 360) % 360;
            int zxjd = jdc > 180 ? (360 - jdc) : jdc;
            int millis = zxjd * 12;   // jdc 360-jdc


            if ((Math.abs(ddt2 -ddt1) + Math.abs(ddt1-dd)) < 10 && !isfx) {        //被房子等卡住了，随机向左或向右2秒
                logger.debug("被房子等卡住了，随机向左或向右3秒");
                vkX = Math.random() < 0.5 ? KeyEvent.VK_D : KeyEvent.VK_A;
                millis = 3000;
            }else {
                logger.debug("方向分别为自己{}度-目标{}度", myAddr[2], lxjd);
                if (0 < jdc && jdc < 180) { //向右转往
                    logger.debug("向右角度{}转{}ms", zxjd, millis);
                    vkX = KeyEvent.VK_D;
                } else {     //向左转往
                    logger.debug("向左角度{}转{}ms", zxjd, millis);
                    vkX = KeyEvent.VK_A;
                }
            }
            ADRun.AD.set(vkX);
            ADRun.T.set(millis);
            if (dd > 1000) {
                if (millis == 3000) {
                    logger.debug("前进卡住了，开始后退");
                    xuyaoW = false; xuyaoS = true;
                }else if (zxjd > 60){     //转向角度大于90，停止前进
                    logger.debug("方向不对，停下前进w");
                    xuyaoS = false; xuyaoW = false; isfx = true;
                }else {
                    logger.debug("按下前进w");
                    xuyaoS = false; xuyaoW = true; isfx = false;
                }
                Thread.sleep(2500);
            }else {
                logger.debug("松开前进s/w,距离比较近手动操作");
                xuyaoS = false; xuyaoW = false;     //距离比较近手动操作
                StartMain.robot.keyPress(KeyEvent.VK_W);
                Thread.sleep(2500);
                StartMain.robot.keyRelease(KeyEvent.VK_W);
            }
            ddt2 = ddt1; ddt1 = dd;
        }

    }


}
