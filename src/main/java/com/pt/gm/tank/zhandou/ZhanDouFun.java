package com.pt.gm.tank.zhandou;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.kongzhi.FangXiangKongZhi;
import com.pt.gm.tank.map.MinMapLX;
import com.pt.gm.tank.mouse.MouseUtils;
import com.pt.gm.tank.po.ZuoBiao;
import com.pt.gm.tank.util.ImgUtils;
import org.apache.commons.lang3.StringUtils;
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
 * @create 2023-07-26 10:47
 */
public class ZhanDouFun {
    private static Logger logger = LoggerFactory.getLogger(ZhanDouFun.class);

    public static final double D_MAX = Math.pow(15.5, 2) + Math.pow(3.5, 2) + 1;
    public static final double D_MIN = Math.pow(13.5, 2) - 1;
    public static final double D_MIN_TANK = D_MIN - 30;
    public static boolean IS_MIAOZHUN = false;

    public static boolean jihui(BufferedImage screenshot) throws InterruptedException {

        BufferedImage subimage = screenshot.getSubimage(StartMain.TANK_CENTRE[0], StartMain.TANK_CENTRE[1] + 150, StartMain.TANK_CENTRE[2], StartMain.TANK_CENTRE[3]);
        String fileContent = ImgUtils.getString(subimage);
        if (StringUtils.isBlank(fileContent)) return false;
        if (fileContent.contains("坦克被该玩家击毁") || fileContent.contains("损毁") || fileContent.contains("坦克溺水")
                || fileContent.contains("因玩家射击而爆炸") || fileContent.contains("坦克坠毁") || fileContent.contains("着火了")
                || StartMain.TUPIAN_NEIRONG.contains("离开战斗")){

            StartMain.robot.keyPress(KeyEvent.VK_ESCAPE);
            StartMain.robot.keyRelease(KeyEvent.VK_ESCAPE);
            Thread.sleep(500);

            MouseUtils.mouseDianJi(StartMain.SCRN_SIZE[0]/2 - 145 + (int)(Math.random() * 290), StartMain.SCRN_SIZE[1]/2 - 50 + (int)(Math.random() * 50));
            Thread.sleep(1800);
            MouseUtils.mouseDianJi(StartMain.SCRN_SIZE[0]/2 - 167 + (int)(Math.random() * 160), StartMain.SCRN_SIZE[1]/2 + 63 + (int)(Math.random() * 32));
            jieshuDY();
            return true;
        }
        return false;
    }

    public static void jieshuDY() {
        StartMain.LU_XIAN = null;
        FangXiangKongZhi.xuyaoW = false;
        FangXiangKongZhi.xuyaoS = false;
        logger.debug("坦克被击毁，退出战斗");
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
            if (entry.getValue()[3] > 0.03 && !IS_MIAOZHUN) {
//                IS_MIAOZHUN = true;
                //按住ctrl，鼠标移动到小地图敌方坦克中间，点击鼠标右键，松开ctrl
                StartMain.robot.keyPress(KeyEvent.CTRL_DOWN_MASK);
                MouseUtils.mouseDianJi(StartMain.MAP_START[0] + (int)entry.getValue()[1], StartMain.SCRN_SIZE[1] + (int)entry.getValue()[0], InputEvent.BUTTON3_DOWN_MASK);
                StartMain.robot.keyRelease(KeyEvent.CTRL_DOWN_MASK);

                Thread.sleep(7000);     //7秒后开火
                MouseUtils.mouseAnXia();
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
                if (dd < D_MIN_TANK) {      //坦克形状
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
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
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


//                double dd = Math.pow(i - 15.5, 2) + Math.pow(j - 15.5, 2);

                if (200<i1 && 200<i2 && 200<i3){
//                if (54<i1&&i1<154 && 107<i2&&i2<207 && 28<i3&&i3<128){
//                if (D_MIN < dd && dd < D_MAX){
//                if (dd < D_MIN && (87<i1&&i1<127 && 165<i2&&i2<205 && -1<i3&&i3<36)){//己方

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

    //找到自己的位置
    public static int[] myAddr(BufferedImage minMap) {
        int height = minMap.getHeight();
        int width = minMap.getWidth();
        int[] qi1 = StartMain.LU_XIAN.get(0);
        int[] qi2 = StartMain.LU_XIAN.get(1);

        boolean qi1x,qi2x;
        List<Integer> myList = new ArrayList<>();
        boolean flag = false;
        for (int tj = 0; tj < 3; tj++) {
            for (int i = 0; i < height; i++) {
                qi1x = -MinMapLX.QZB < (i - qi1[1]) && (i - qi1[1]) < MinMapLX.QZB;
                qi2x = -MinMapLX.QZB < (i - qi2[1]) && (i - qi2[1]) < MinMapLX.QZB;
                for (int j = 0; j < width; j++) {
                    if (tj == 0)  if ((qi1x && -MinMapLX.QZB < (j - qi1[0]) && (j - qi1[0]) < MinMapLX.QZB) || (qi2x && -MinMapLX.QZB < (j - qi2[0]) && (j - qi2[0]) < MinMapLX.QZB)) continue;
                    else if (tj == 1) if (qi2x && -MinMapLX.QZB < (j - qi2[0]) && (j - qi2[0]) < MinMapLX.QZB) continue;
                    else if (tj == 2) if (qi1x && -MinMapLX.QZB < (j - qi1[0]) && (j - qi1[0]) < MinMapLX.QZB) continue;
                    int rgb = minMap.getRGB(j, i);
                    int i1 = rgb >> 16 & 0xff;
                    int i2 = rgb >> 8 & 0xff;
                    int i3 = rgb & 0xff;
                    if (253 < i1 && 253 < i2 && 253 < i3) {

                        int ys = i - 15 < 0 ? 0 : i - 15;
                        int ye = i + 16 > ZhanDou.MIN_MAP_W ? ZhanDou.MIN_MAP_W : i + 16;
                        int xs = j - 15 < 0 ? 0 : j - 15;
                        int xe = j + 16 > ZhanDou.MIN_MAP_W ? ZhanDou.MIN_MAP_W : j + 16;
                        for (int k = ys; k < ye; k++) {      //左上角
                            for (int l = xs; l < xe; l++) {
                                rgb = minMap.getRGB(l, k);
                                i1 = rgb >> 16 & 0xff;
                                i2 = rgb >> 8 & 0xff;
                                i3 = rgb & 0xff;
                                if (flag = 200 < i1 && 200 < i2 && 200 < i3) {
                                    myList.add((l << 16) + k);
                                    break;
                                }
                            }
                            if (flag) break;
                        }
                        for (int k = ye - 1; k >= ys; k--) {      //右下角
                            for (int l = xe - 1; l >= xs; l--) {
                                rgb = minMap.getRGB(l, k);
                                i1 = rgb >> 16 & 0xff;
                                i2 = rgb >> 8 & 0xff;
                                i3 = rgb & 0xff;
                                if (flag = 200 < i1 && 200 < i2 && 200 < i3) {
                                    myList.add((l << 16) + k);
                                    break;
                                }
                            }
                            if (flag) break;
                        }
                        for (int k = xe - 1; k >= xs; k--) {    //右上角
                            for (int l = ys; l < ye; l++) {
                                rgb = minMap.getRGB(k, l);
                                i1 = rgb >> 16 & 0xff;
                                i2 = rgb >> 8 & 0xff;
                                i3 = rgb & 0xff;
                                if (flag = 200 < i1 && 200 < i2 && 200 < i3) {
                                    myList.add((k << 16) + l);
                                    break;
                                }
                            }
                            if (flag) break;
                        }
                        for (int k = xs; k < xe; k++) {    //左下角
                            for (int l = ye - 1; l >= ys; l--) {
                                rgb = minMap.getRGB(k, l);
                                i1 = rgb >> 16 & 0xff;
                                i2 = rgb >> 8 & 0xff;
                                i3 = rgb & 0xff;
                                if (flag = 200 < i1 && 200 < i2 && 200 < i3) {
                                    myList.add((k << 16) + l);
                                    break;
                                }
                            }
                            if (flag) break;
                        }
                        removeCF(myList);
                        if (myList.size() != 3) {
                            flag = true;
                            myList.clear();
                            logger.debug("自己位置坐标计算失败:{}-{}", myList.size(), tj);
                            break;
                        }
                        int y = 0, x = 0;
                        List<int[]> li = new ArrayList<>();
                        for (Integer mywz : myList) {
                            int x1 = mywz >> 16 & 0xffff;
                            int y1 = mywz & 0xffff;
                            x += x1;
                            y += y1;
                            li.add(new int[]{x1, y1});
                        }

                        return new int[]{x / 3, y / 3, jiaodu(li)};
                    }
                }
                if (flag) {flag = false; break;}
            }
        }
        return null;
    }

//    public static void main(String[] args) {
//        ArrayList<Integer> l = new ArrayList<>();
//        l.add((367<<16) + 261);
//        l.add((369<<16) + 273);
//        l.add((369<<16) + 268);
//        l.add((360<<16) + 272);
//        removeCF(l);
//    }

    private static void removeCF(List<Integer> myList) {
//        自己坐标：354-213 367-266 367-261 368-261
//        自己坐标：355-227 368-280 369-273 369-273
//        自己坐标：363-222 368-277 369-268 369-267
//        自己坐标：354-217 359-277 360-272 360-271              这种情况的坐标会计算失败
//        for (Integer integer : myList) logger.debug("自己坐标：{}-{}", integer >> 16 & 0xffff, integer & 0xffff);

        if (myList.size() == 3) return;
        ZuoBiao zb1,zb2;
        for (int i = 0; i < 3; i++) {
            zb1 = new ZuoBiao(myList.get(i) >> 16 & 0xffff, myList.get(i) & 0xffff);
            for (int j = i + 1; j < 4; j++) {
                zb2 = new ZuoBiao(myList.get(j) >> 16 & 0xffff, myList.get(j) & 0xffff);
//                logger.debug("坐标{}：{},坐标{}：{}",i ,zb1.toString(), j, zb2.toString());
                if (zb1.equals(zb2)) {
                    myList.remove(j);
                    return;
                }
            }
        }
        if (myList.size() == 4) {
            int sum = (int) myList.stream().mapToInt((x) -> x).summaryStatistics().getSum();
            int index = 0, xa = (sum >> 16 & 0xffff) / 4, ya = (sum & 0xffff)/4;
            double mindd = 10000, maxdd = 0;
            for (int i = 0; i < myList.size(); i++) {
                double dd = Math.pow((myList.get(i) >> 16 & 0xffff) - xa, 2) + Math.pow((myList.get(i) & 0xffff) - ya, 2);
                if (mindd > dd){
                    index = i;
                    mindd = dd;
                }
                if (dd > maxdd) maxdd = dd;
            }
            logger.debug("自己图标轮廓到自己中心点最大距离{}", maxdd);
            if (mindd < 60 && 30 < maxdd && maxdd < 100) myList.remove(index);
        }
    }

    /**
     * 根据三点计算行径角度
     * @param li
     * @return
     */
    public static int jiaodu(List<int[]> li) {
        double d01 = Math.pow(li.get(0)[0] - li.get(1)[0], 2) + Math.pow(li.get(0)[1] - li.get(1)[1], 2);
        double d02 = Math.pow(li.get(0)[0] - li.get(2)[0], 2) + Math.pow(li.get(0)[1] - li.get(2)[1], 2);
        double d12 = Math.pow(li.get(1)[0] - li.get(2)[0], 2) + Math.pow(li.get(1)[1] - li.get(2)[1], 2);
        int jiaodu = 0;
        if (d01 < d02 && d01 < d12){
            jiaodu = jiaodu(new double[]{(li.get(0)[0] + li.get(1)[0] + 0.0) / 2, (li.get(0)[1] + li.get(1)[1] + 0.0) / 2}, li.get(2));
        }else
        if (d02 < d01 && d02 < d12){
            jiaodu = jiaodu(new double[]{(li.get(0)[0] + li.get(2)[0] + 0.0) / 2, (li.get(0)[1] + li.get(2)[1] + 0.0) / 2}, li.get(1));
        }else
        if (d12 < d01 && d12 < d02){
            jiaodu = jiaodu(new double[]{(li.get(1)[0] + li.get(2)[0] + 0.0) / 2, (li.get(1)[1] + li.get(2)[1] + 0.0) / 2}, li.get(0));
        }
        return jiaodu;
    }

    public static int jiaodu(double[] doubles, int[] ints) {
        double xc = ints[0] - doubles[0];
        double yc = ints[1] - doubles[1];
        return jiaodu(xc, yc);
    }

    public static int jiaodu(double xc, double yc) {
        double degrees = Math.toDegrees(Math.atan(yc / xc));
        if (xc < 0) degrees += 180;
        else if (yc < 0) degrees += 360;

        return 360 - (int) degrees;     //因为y轴数据是上面小下面大，所以需要翻转角度
    }

    public static double dd2(int[] zb1, int[] zb2){
        return Math.pow(zb1[0] - zb2[0], 2) + Math.pow(zb1[1] - zb2[1], 2);
    }

    public static int jiaodu(int[] addrs, int[] ints) {
        return jiaodu(new double[]{addrs[0], addrs[1]}, ints);
    }
}
