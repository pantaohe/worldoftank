package com.pt.gm.tank.zd;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.util.ImgUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-07-26 10:47
 */
public class ZhanDouFun {
    private static Logger logger = LoggerFactory.getLogger(ZhanDouFun.class);

    public static final double D_MAX = Math.pow(15.5, 2) + Math.pow(3.5, 2) + 1;
    public static final double D_MIN = Math.pow(13.5, 2) - 1;
    public static final double D_MIN_TANK = D_MIN - 30;
    public static boolean IS_MIAOZHUN = false;

    public static boolean jihui(BufferedImage screenshot) throws IOException, InterruptedException {
        BufferedImage subimage = screenshot.getSubimage(StartMain.TANK_CENTRE[0], StartMain.TANK_CENTRE[1], StartMain.TANK_CENTRE[2], StartMain.TANK_CENTRE[3]);
        String fileContent = ImgUtils.getString(subimage);
        if (StringUtils.isBlank(fileContent)) return false;
        if (fileContent.contains("坦克被该玩家击毁")){

            StartMain.robot.keyPress(KeyEvent.VK_ESCAPE);
            StartMain.robot.keyRelease(KeyEvent.VK_ESCAPE);
            Thread.sleep(500);

            StartMain.robot.mouseMove(StartMain.SCRN_SIZE[0]/2, StartMain.SCRN_SIZE[1]/2 - 50);
            StartMain.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            StartMain.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(200);

            StartMain.robot.mouseMove(StartMain.SCRN_SIZE[0]/2 - 50, StartMain.SCRN_SIZE[1]/2 + 100);
            StartMain.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            StartMain.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            return true;
        }
        return false;
    }

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
                if (tankAddr[0] > 0.5) {
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
            if (entry.getValue()[3] > 0.08 && !IS_MIAOZHUN) {
                IS_MIAOZHUN = true;
                //按住ctrl，鼠标移动到小地图敌方坦克中间，点击鼠标右键，松开ctrl
                StartMain.robot.keyPress(KeyEvent.CTRL_DOWN_MASK);
                StartMain.robot.mouseMove(StartMain.MAP_START[0] + (int)entry.getValue()[1], StartMain.SCRN_SIZE[1] + (int)entry.getValue()[0]);
                StartMain.robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                StartMain.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                StartMain.robot.keyRelease(KeyEvent.CTRL_DOWN_MASK);

                Thread.sleep(7000);     //7秒后开火
                StartMain.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                StartMain.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                IS_MIAOZHUN = false;
                return null;
            }
        }

        return null;
    }

    private static double[] getTankAddr(int i, int j, BufferedImage subimage) {
        int ksum = 0,qsum = 0,tkksum = 0,tkqsum = 0;
        for (int k = i-15; k < i+17; k++) {
            if (k < 14 || 406 < k) continue;
            for (int l = j-15; l < j+17; l++) {
                if (l < 14 || 406 < l) continue;

                double dd = Math.pow(i + 0.5 - k, 2) + Math.pow(j + 0.5 - l, 2);
                if (D_MIN < dd && dd < D_MAX){      //坦克边框
                    qsum ++;
                    int rgb = subimage.getRGB(l, k);
                    int i1 = rgb >> 16 & 0xff;
                    int i2 = rgb >> 8 & 0xff;
                    int i3 = rgb & 0xff;
                    if ((177<i1&&i1<197 && 143<i2&&i2<163 && 245<i3)      //敌方
                            ||  (21<i1&&i1<41 && 11<i2&&i2<31 && 22<i3&&i3<42)
//                    if ((109<i1&&i1<129 && 168<i2&&i2<188 && i3<10)        //己方
//                            ||  (11<i1&&i1<31 && 22<i2&&i2<42 && 12<i3&&i3<32)
                    ) ksum ++;
                }
                if (dd < D_MIN_TANK) {      //坦克形状
                    tkqsum++;
                    int rgb = subimage.getRGB(l, k);
                    int i1 = rgb >> 16 & 0xff;
                    int i2 = rgb >> 8 & 0xff;
                    int i3 = rgb & 0xff;
                    if ((155<i1&&i1<175 && 149<i2&&i2<169 && 229<i3&&i3<249)      //敌方
//                    if ((97<i1&&i1<117 && 175<i2&&i2<195 && 6<i3&&i3<26)        //己方
                    ) tkksum ++;
                }
            }
        }
        double v1 = ksum * 1.0 / qsum;
        double v2 = tkksum * 1.0 / tkqsum;
//        if (v > 0.5)
//            logger.debug("x:{},y:{},边框比例:{}", j, i, v);
        return new double[]{v1, v2};
    }

    public static int[] mouseCenter(BufferedImage subimage) {
        HashMap<Integer, int[]> rMap = new HashMap<>();
        HashMap<Integer, int[]> cMap = new HashMap<>();

        int height = subimage.getHeight();
        int width = subimage.getWidth();
        int it = 0, jt = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = subimage.getRGB(j, i);
                if (isk(rgb) && (it+jt == 0 || i - it < 2 || j - jt < 2)){
                    it = i; jt =j;
                    if (!rMap.containsKey(i)) rMap.put(i, new int[]{0, 0});
                    if (!cMap.containsKey(j)) cMap.put(j, new int[]{0, 0});
                    if (rMap.get(i)[0] == 0 || j - rMap.get(i)[0] < 10) {rMap.get(i)[0] = j; rMap.get(i)[1] += 1;}
                    if (cMap.get(j)[0] == 0 || i - cMap.get(j)[0] < 10) {cMap.get(j)[0] = i; cMap.get(j)[1] += 1;}
                }
            }
        }
        List<Integer> cList = new ArrayList<>();
        List<Integer> rList = new ArrayList<>();
        for (Map.Entry<Integer, int[]> c : cMap.entrySet()) {
            if (c.getValue()[1] > 15) cList.add(c.getKey());
        }
        for (Map.Entry<Integer, int[]> r : rMap.entrySet()) {
            if (r.getValue()[1] > 30) rList.add(r.getKey());
        }

        int[] cz = new int[2];
        if (cList.size() < 3){
            if (cList.get(0) < 120) cz[0] = cList.get(0) - 50;
            if (cList.get(0) > 300) cz[0] = cList.get(0) + 50;
        }else {
            cz[0] = (int)cList.stream().mapToInt((x) ->x).summaryStatistics().getSum() / cList.size();
        }
        if (rList.size() < 3){
            if (rList.get(0) < 70) cz[1] = rList.get(0) - 50;
            if (rList.get(0) > 340) cz[1] = rList.get(0) + 50;
        }else {
            cz[1] = (int)rList.stream().mapToInt((x) ->x).summaryStatistics().getSum() / rList.size();
        }


        return cz;
    }

    public static boolean isk(int rgb) {
        int i1 = rgb >> 16 & 0xff;
        int i2 = rgb >> 8 & 0xff;
        int i3 = rgb & 0xff;

        return StartMain.RGB_MIN[0]<i1&&i1<StartMain.RGB_MAX[0] && StartMain.RGB_MIN[1]<i2&&i2<StartMain.RGB_MAX[1] && StartMain.RGB_MIN[2]<i3&&i3<StartMain.RGB_MAX[2];

    }

    public static void prinRGB(BufferedImage subimage) {
        int height = subimage.getHeight();
        int width = subimage.getWidth();


        int sumR = 0,sumG = 0,sumB = 0,index = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = subimage.getRGB(j, i);
                int i1 = rgb >> 16 & 0xff;
                int i2 = rgb >> 8 & 0xff;
                int i3 = rgb & 0xff;

//                if (
//                        ((i == 2 || i == 3 || i == 50 || i == 51 || i == 52) && (1 < j && j < 79))
//                                || ((j == 77 || j == 78) && (1 < i && i < 52))
//                ){
//                    sumR += i1;
//                    sumG += i2;
//                    sumB += i3;
//                    index++;
//
//                }


                double dd = Math.pow(i - 15.5, 2) + Math.pow(j - 15.5, 2);

//                if (54<i1&&i1<154 && 107<i2&&i2<207 && 28<i3&&i3<128){
//                if (D_MIN < dd && dd < D_MAX){
                if (dd < D_MIN && (87<i1&&i1<127 && 165<i2&&i2<205 && -1<i3&&i3<36)){//己方

                }else{
                    i1 = i2 = i3 = 0;
                }
                String s1 = addRGB(i1 + ",");
                String s2 = addRGB(i2 + ",");
                String s3 = addRGB(i3 + ",");
                System.out.print(s1 + s2 + s3 + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static String addRGB(String i1) {
        while (i1.length() < 4) i1 = "0" + i1;
        return i1;
    }
    
}
