package com.pt.gm.tank.zhandou;

import com.pt.gm.tank.config.CF;
import com.pt.gm.tank.mouse.MouseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-22 10:12
 */
public class TankUtils {
    private static Logger logger = LoggerFactory.getLogger(TankUtils.class);


    public static List<int[]> dfTank(BufferedImage subimage) throws InterruptedException {
//        BufferedImage subimage1 = subimage.getSubimage(300, 165, 32, 32);
//        prinRGB(subimage1);
//        BufferedImage subimage1 = subimage.getSubimage(111, 280, 32, 32);
//        prinRGB(subimage1);

        HashMap<Integer, double[]> tankMap = new HashMap<Integer, double[]>();

        boolean flag = false; int index = 0;
        int height = subimage.getHeight();
        int width = subimage.getWidth();
        for (int i = 14; i < height; i++) {
            for (int j = 14; j < width; j++) {
                double[] tankAddr = getTankAddr(i, j, subimage);
                if (tankAddr[0] > 0.4) {
                    index ++;
                    for (Map.Entry<Integer, double[]> ent : tankMap.entrySet()) {
                        if (Math.abs(ent.getValue()[0] - i) < 4 && Math.abs(ent.getValue()[1] - j) < 4) {
                            index = ent.getKey();
                            flag = true;
                            break;
                        } else flag = false;
                    }
                    if (!flag || tankAddr[0] > tankMap.get(index)[2]) tankMap.put(index, new double[]{i, j, tankAddr[0], tankAddr[1]});
                }
            }
        }

        for (Map.Entry<Integer, double[]> entry : tankMap.entrySet()) {
            logger.debug("x:{},y:{},边框比例:{}，内部比例:{}", entry.getValue()[1], entry.getValue()[0], entry.getValue()[2],entry.getValue()[3]);
            if (entry.getValue()[3] > 0.03 && !ZhanDouFun.IS_MIAOZHUN) {
//                IS_MIAOZHUN = true;
                //按住ctrl，鼠标移动到小地图敌方坦克中间，点击鼠标右键，松开ctrl
                CF.robot.keyPress(KeyEvent.CTRL_DOWN_MASK);
                MouseUtils.mouseDianJi(CF.MAP_START[0] + (int)entry.getValue()[1], CF.SCRN_SIZE[1] + (int)entry.getValue()[0], InputEvent.BUTTON3_DOWN_MASK);
                CF.robot.keyRelease(KeyEvent.CTRL_DOWN_MASK);

                Thread.sleep(7000);     //7秒后开火
                MouseUtils.mouseAnXia();
                ZhanDouFun.IS_MIAOZHUN = false;
                return null;
            }
        }

        return null;
    }

    private static double[] getTankAddr(int i, int j, BufferedImage subimage) {
        int ksum = 0,qsum = 0,tkksum = 0,tkqsum = 0;
        for (int k = i-19; k < i+17; k++) {
            if (k < 14 || 406 < k) continue;
            for (int l = j-15; l < j+17; l++) {
                if (l < 14 || 406 < l) continue;

                double dd = Math.pow(i + 0.5 - k, 2) + Math.pow(j + 0.5 - l, 2);
                if (ZhanDouFun.D_MIN < dd && dd < ZhanDouFun.D_MAX){      //坦克边框
                    qsum ++;
                    int rgb = subimage.getRGB(k, l);
                    int i1 = rgb >> 16 & 0xff;
                    int i2 = rgb >> 8 & 0xff;
                    int i3 = rgb & 0xff;
                    if ((177<i1&&i1<197 && 143<i2&&i2<163 && 245<i3)      //敌方
                            ||  (21<i1&&i1<41 && 11<i2&&i2<31 && 22<i3&&i3<42)
//                    if ((109<i1&&i1<129 && 168<i2&&i2<188 && i3<10)        //己方
//                            ||  (11<i1&&i1<31 && 22<i2&&i2<42 && 12<i3&&i3<32)
                    ) ksum ++;
                }
                if (dd < ZhanDouFun.D_MIN_TANK) {      //坦克形状
                    tkqsum++;
                    int rgb = subimage.getRGB(k, l);
                    int i1 = rgb >> 16 & 0xff;
                    int i2 = rgb >> 8 & 0xff;
                    int i3 = rgb & 0xff;
//                    if ((155<i1&&i1<175 && 149<i2&&i2<169 && 229<i3&&i3<249)      //敌方
                    if ((155<i1&&i1<200 && 149<i2&&i2<200 && 200<i3&&i3<249)      //敌方
//                    if ((97<i1&&i1<117 && 175<i2&&i2<195 && 6<i3&&i3<26)        //己方
                    ) tkksum ++;
                }
            }
        }
        double v1 = ksum * 1.0 / qsum;
        double v2 = tkksum * 1.0 / tkqsum;
        if (v1 > 0.4)
            logger.debug("x:{},y:{},边框比例:{}、{}", j, i, v1, v2);
        return new double[]{v1, v2};
    }

    public static void anJian(int time) {
        if (CF.AN_JIAN_SZ == null || CF.AN_JIAN_SZ.size() == 0) return;
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (Map<String, Object> anjian : CF.AN_JIAN_SZ) {
                    try {
                        if (!anjian.containsKey("anWan")) anjian.put("anWan", false);
                        int shiJian = (int)anjian.get("SHI_JIAN");
                        if (!(boolean) anjian.get("anWan") && shiJian > time && Math.random() < 0.35) {      //这个时间后随机一个时间按下
                            String anJianName = (String) anjian.get("AN_JIAN");
                            switch (anJianName) {
                                case "F1": anXia(KeyEvent.VK_F1); break;
                                case "F2": anXia(KeyEvent.VK_F2); break;
                                case "F3": anXia(KeyEvent.VK_F3); break;
                                case "F4": anXia(KeyEvent.VK_F4); break;
                                case "F5": anXia(KeyEvent.VK_F5); break;
                                case "F6": anXia(KeyEvent.VK_F6); break;
                                case "F7": anXia(KeyEvent.VK_F7); break;
                                case "F8": anXia(KeyEvent.VK_F8); break;
                                case "F9": anXia(KeyEvent.VK_F9); break;
                                default: break;
                            }
                            anjian.put("anWan", true);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    private static void anXia(int key) throws InterruptedException {
        CF.robot.keyPress(key);
        Thread.sleep(200);
        CF.robot.keyRelease(key);
    }
}
