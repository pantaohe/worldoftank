package com.pt.gm.tank.zhandou;

import com.pt.gm.tank.config.CF;
import com.pt.gm.tank.kongzhi.FangXiangKongZhi;
import com.pt.gm.tank.map.MinMapLX;
import com.pt.gm.tank.mouse.MouseUtils;
import com.pt.gm.tank.po.ZuoBiao;
import com.pt.gm.tank.util.ImgUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static final int RGB_MASK = 220;

    public static boolean jihui(BufferedImage screenshot) throws InterruptedException {

//        BufferedImage subimage = screenshot.getSubimage(760, 540, 400, 150);
        BufferedImage subimage = screenshot.getSubimage(760, CF.SCRN_SIZE[1] + CF.SCRN_SIZE[2] - 80, 400, 80);
        String fileContent = ImgUtils.getString(subimage);
        if (StringUtils.isBlank(fileContent)) return false;
//        if (fileContent.contains("坦克被该玩家击毁") || fileContent.contains("损毁") || fileContent.contains("坦克溺水")
//                || fileContent.contains("因玩家射击而爆炸") || fileContent.contains("坦克坠毁") || fileContent.contains("着火了")
//                || fileContent.contains("成员因")){
        if (fileContent.contains("观察视角") || fileContent.contains("返回车库")){

            CF.robot.keyPress(KeyEvent.VK_ESCAPE);
            Thread.sleep(50);
            CF.robot.keyRelease(KeyEvent.VK_ESCAPE);
            Thread.sleep(500);

            return jihui();
        }
        return false;
    }

    public static boolean jihui() throws InterruptedException {

        MouseUtils.mouseDianJi(CF.SCRN_SIZE[0]/2 - 145 + (int)(Math.random() * 290), CF.SCRN_SIZE[1]/2 - 73 + CF.SCRN_SIZE[2] + (int)(Math.random() * 40));
        Thread.sleep(2200);
        MouseUtils.mouseDianJi(CF.SCRN_SIZE[0]/2 - 167 + (int)(Math.random() * 160), CF.SCRN_SIZE[1]/2 + 40 + CF.SCRN_SIZE[2] + (int)(Math.random() * 32));
        jieshuDY();
        return true;
    }

    public static void jieshuDY() throws InterruptedException {
        CF.LU_XIAN = null;
        FangXiangKongZhi.xuyaoW = false;
        FangXiangKongZhi.xuyaoS = false;
        logger.debug("战车被毁，全体撤离");

        if (CF.AN_JIAN_SZ != null && CF.AN_JIAN_SZ.size() > 0) {
            for (Map<String, Object> anjian : CF.AN_JIAN_SZ) {
                anjian.put("anWan", false);
            }
        }
        Thread.sleep(3000);
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

        return CF.RGB_MIN[0]<i1&&i1< CF.RGB_MAX[0] && CF.RGB_MIN[1]<i2&&i2< CF.RGB_MAX[1] && CF.RGB_MIN[2]<i3&&i3< CF.RGB_MAX[2];

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

//                if (200<i1 && 200<i2 && 200<i3){
//                if (54<i1&&i1<154 && 107<i2&&i2<207 && 28<i3&&i3<128){
//                if (140<i1&&i1<250 && 70<i2&&i2<170 && 10<i3&&i3<110){      //斜线
////                if (D_MIN < dd && dd < D_MAX){
////                if (dd < D_MIN && (87<i1&&i1<127 && 165<i2&&i2<205 && -1<i3&&i3<36)){//己方
//
//                }else{
//                    i1 = i2 = i3 = 0;
//                }
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
        int[] qi1 = CF.LU_XIAN.get(0);
        int[] qi2 = CF.LU_XIAN.get(1);

        boolean qi1x,qi2x;
        List<Integer> myList = new ArrayList<>();
        boolean flag = false;
        for (int tj = 0; tj < 1; tj++) {            //对自己在旗子内的解析暂时放弃
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
                                if (flag = getBS(l, k, minMap)){
                                    myList.add((l << 16) + k);
                                    break;
                                }
                            }
                            if (flag) break;
                        }
                        for (int k = ye - 1; k >= ys; k--) {      //右下角
                            for (int l = xe - 1; l >= xs; l--) {
                                if (flag = getBS(l, k, minMap)){
                                    myList.add((l << 16) + k);
                                    break;
                                }
                            }
                            if (flag) break;
                        }
                        for (int k = xe - 1; k >= xs; k--) {    //右上角
                            for (int l = ys; l < ye; l++) {
                                if (flag = getBS(k, l, minMap)){
                                    myList.add((k << 16) + l);
                                    break;
                                }
                            }
                            if (flag) break;
                        }
                        for (int k = xs; k < xe; k++) {    //左下角
                            for (int l = ye - 1; l >= ys; l--) {
                                if (flag = getBS(k, l, minMap)){
                                    myList.add((k << 16) + l);
                                    break;
                                }
                            }
                            if (flag) break;
                        }
                        removeCF(myList);
                        if (myList.size() != 3) {
                            flag = true;
                            logger.debug("自己位置坐标计算失败,size:{}-index:{}", myList.size(), tj);
                            for (Integer integer : myList) logger.debug("自己坐标：{}-{}", integer >> 16 & 0xffff, integer & 0xffff);
                            myList.clear();
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

    public static boolean getBS(int x, int y, BufferedImage minMap){
        if (x < 0 || 490 < x || y < 0 || 490 < y) return false;
        int rgb = minMap.getRGB(x, y);
        int i1 = rgb >> 16 & 0xff;
        int i2 = rgb >> 8 & 0xff;
        int i3 = rgb & 0xff;
        return RGB_MASK < i1 && RGB_MASK < i2 && RGB_MASK < i3;
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
            if (mindd < 60 && 30 < maxdd && maxdd < 120) myList.remove(index);
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
