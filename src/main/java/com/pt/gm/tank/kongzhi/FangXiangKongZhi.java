package com.pt.gm.tank.kongzhi;

import com.pt.gm.tank.config.CF;
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
        Thread paotat = null;
        int[] myAddr, myAddrt = null;
        int[] mubiao = CF.LU_XIAN.get(i);    double ddt1 = 0, ddt2 = 0, dd;
        int vkX, feiZhanDou = 0, cuowuIndex = 0, millisSC = 0;long l = 0;BufferedImage minMap;
        while (true) {
            logger.debug("进入方向控制循环：\n");
//            if (CF.KAI_PAO && paotat != null && !paotat.isInterrupted()) paotat.stop();
            do {
                l = System.currentTimeMillis();
                BufferedImage screenshot = ImgUtils.screenshot();
                if (ZhanDou.zhandouFX(screenshot) != 2) {   //没有在咱都界面
                    if ( feiZhanDou++ > 25) {
                        ZhanDouFun.jieshuDY();
                        return;
                    }
                }else feiZhanDou = 0;
                if (ZhanDouFun.jihui(screenshot)) return;       //战车被毁退出

                minMap = screenshot.getSubimage(CF.MAP_START[0], CF.MAP_START[1], ZhanDou.MIN_MAP_W, CF.SCRN_SIZE[1] - CF.MAP_START[1] + CF.SCRN_SIZE[2]);
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
            if (ddt1 != 0 && myAddrt != null && ZhanDouFun.dd2(myAddr, myAddrt) > (15000 + 10000*cuowuIndex)) {     //如果错误
                logger.debug("当前坐标点跨度太大，抛弃此次位置");
                cuowuIndex = 1;
                continue;     //如果单次变动太大，说明是错误的。
            }

            myAddrt = myAddr;
            cuowuIndex = 0;

            if (CF.KAI_PAO && (paotat == null || !paotat.isAlive())) {
                //自动开炮
                paotat = new Thread(new PaoTaKongZhi(minMap, myAddr));
                paotat.start();
            }

            if (dd < 180) {
                xuyaoS = false; xuyaoW = false;
                return;     //达到小目标退出
            }

            int mbjd = ZhanDouFun.jiaodu(myAddr, mubiao);       //目标角度
            int jdc = (myAddr[2] - mbjd + 360) % 360;       //自己向右旋转的度数
            int zxjd = jdc > 180 ? (360 - jdc) : jdc;
            int millis = zxjd * 600 / JiaRuZD.zhuanxSD; millis = millis > 2400 ? 2400 : millis;   // jdc 360-jdc


            if ((Math.abs(ddt2 -ddt1) + Math.abs(ddt1-dd)) < 10 && !isfx) {        //被房子等卡住了，随机向左或向右2秒
                logger.debug("被房子等卡住了，随机向左或向右3秒");
                vkX = Math.random() < 0.5 ? KeyEvent.VK_D : KeyEvent.VK_A;
                millis = 3000;
            }else {
                logger.debug("方向分别为自己{}度-目标{}度", myAddr[2], mbjd);
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
            if (millis == 3000) {       //时间太长
                logger.debug("前进卡住了，开始后退或前进");
                if (Math.random() < 0.9) {
                    xuyaoW = false; xuyaoS = true;
                }else {
                    xuyaoS = false; xuyaoW = true;
                }
                Thread.sleep(2200);     //下一次的方向控制无效，
            }else if (dd > 1000) {
                if (millisSC != 3000 && zxjd > CF.YI_DONG_JIA_JIAO){     //转向角度大于90，停止前进（除非上次是后退旋转）
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
                Thread.sleep(200);
                CF.robot.keyPress(KeyEvent.VK_W);
                Thread.sleep(2500);
                CF.robot.keyRelease(KeyEvent.VK_W);
            }
            millisSC = millis;
            ddt2 = ddt1; ddt1 = dd;
        }

    }


}
