package com.pt.gm.tank.kongzhi;

import com.pt.gm.tank.config.CF;
import com.pt.gm.tank.map.MinMapLX;
import com.pt.gm.tank.po.DifangTank;
import com.pt.gm.tank.zhandou.ZhanDou;
import com.pt.gm.tank.zhandou.ZhanDouFun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-21 15:59
 */
public class PaoTaKongZhi implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(ZhanDouFun.class);
    private BufferedImage minMap;
    private int[] myAddr;
    private Map<Integer, Integer> zbMap = new HashMap<>();

    private static int JGSJ = 100;

    public PaoTaKongZhi(BufferedImage minMap, int[] myAddr) {
        this.minMap = minMap;
        this.myAddr = myAddr;
    }

    @Override
    public void run() {

        try {
            logger.debug("进入鼠标线程");
            int[] xian = getXian();
            int paojd = ZhanDouFun.jiaodu(myAddr, xian);

            long l = System.currentTimeMillis();
            DifangTank difang = getDifangAddr();
            if (difang == null) return;
            int mbjd = ZhanDouFun.jiaodu(myAddr, difang.getAddr());

            int xzjd = (paojd - mbjd + 360) % 360;       //自己向右旋转的度数

            int x = (xzjd < 180 ? (xzjd + 180) : (xzjd - 180)) * CF.SCRN_SIZE[0] / 360;
            logger.debug("计算敌方坐标耗时：{}，自己坐标：{}-{}，地方坐标：{}-{}",System.currentTimeMillis() - l, myAddr[0], myAddr[1], difang.getX(), difang.getY());
            logger.debug("炮角度{}，目标角度{}，向右选择度数{}，x像素{}-960", paojd, mbjd, xzjd, x);
            CF.robot = new Robot();


            int speed = 4, centerX = CF.SCRN_SIZE[0] / 2;
            CF.robot.mouseMove(x, CF.CENTER_Y_PAO);

            Thread.sleep(JGSJ);
            dianji(3);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    CF.robot.mouseMove(centerX + i * speed, CF.CENTER_Y_PAO + j * speed);
                    Thread.sleep(JGSJ);
                    dianji(3);
                }
            }

            Thread.sleep(JGSJ);
            dianji(1);

//            BufferedImage subimage = minMap.getSubimage(0, 0, 40, 40);
//            ZhanDouFun.prinRGB(subimage);
//            System.out.println(123);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void dianji(int type) throws InterruptedException {
        if ( CF.KAI_PAO_MO_REN_SHE_ZHI) {
            switch (type) {
                case 1:
                    CF.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    Thread.sleep(JGSJ);
                    CF.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                case 3:
                    CF.robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    Thread.sleep(JGSJ);
                    CF.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    break;
                default:
                    break;
            }
        }else {
            switch (type) {
                case 1:
                    CF.robot.keyPress(KeyEvent.VK_Q);
                    Thread.sleep(JGSJ);
                    CF.robot.keyRelease(KeyEvent.VK_Q);
                    break;
                case 3:
                    CF.robot.keyPress(KeyEvent.VK_E);
                    Thread.sleep(JGSJ);
                    CF.robot.keyRelease(KeyEvent.VK_E);
                    break;
                default:
                    break;
            }
        }

    }

    private DifangTank getDifangAddr() {
        int x0 = myAddr[0];
        int y0 = myAddr[1];
        DifangTank tank;
        for (int i = 1; i < 150; i++) {
            for (int j = -i; j <= i; j++) {
                if ((tank = getTank(x0 + j, y0 - i)) != null) return getZhouBian(tank);
                if ((tank = getTank(x0 + i, y0 + j)) != null) return getZhouBian(tank);
                if ((tank = getTank(x0 - j, y0 + i)) != null) return getZhouBian(tank);
                if ((tank = getTank(x0 - i, y0 - j)) != null) return getZhouBian(tank);
            }
        }
        return null;
    }

    private DifangTank getZhouBian(DifangTank tank) {
        DifangTank maxTank = tank; DifangTank tankT;
        int x = tank.getX(); int y = tank.getY();
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                tankT = getTank(x + i, y + j);
                if (tankT != null && maxTank.getWaiquan() < tankT.getWaiquan()) maxTank = tankT;
            }
        }
        logger.debug(maxTank.toString());
        return maxTank;
    }

    private DifangTank getTank(int x, int y) {
        if (x < 0 || 490 < x || y < 0 || 490 < y) return null;
        int type,tk = 0,sum = 0,tkn = 0,sumn = 0;double dd;
        boolean isRead1 = false, isRead2 = false;
        for (int i = -19; i < 19; i++) {
            for (int j = -19; j < 19; j++) {
                dd = Math.pow(i, 2) + Math.pow(j, 2);
                type = getXian(x + i, y + j);
                if (dd < 160) {
                    if (0 != type) {
                        sumn++;
                        if (2 == type) tkn++;
                        else if (3 == type) {
                            tkn++;
                            isRead1 = true;
                        }
                    }
                }else if (dd < 362) {      //坦克边框
                    if (0 != type) {
                        sum++;
                        if (2 == type) tk++;
                        else if (3 == type) {
                            tk++;
                            isRead2 = true;
                        }
                    }
                }
            }
        }

        double waiquan = tk * 1.0 / sum;
        double neiquan = tkn * 1.0 / sumn;
        if (waiquan > 0.4 && isRead1 && isRead2){
//            logger.debug("边框比例大于0.53:{}-{}--{}-{}",x,y,waiquan,neiquan);
            return new DifangTank(x, y, waiquan, neiquan);
        }
        return null;
    }

    private int[] getXian() {
        int length = CF.SCRN_SIZE[0] - CF.MAP_START[0] - 1;
        for (int i = 0; i <= length; i++) {
            for (int j = 0; j < 5; j++) {
                if (getXian(i, j, 1)) return new int[]{i, j};
                if (getXian(length - j, i, 1)) return new int[]{length - j, i};
                if (getXian(length - i, length - j, 1)) return new int[]{length - i, length - j};
                if (getXian(j, length - i, 1)) return new int[]{j, length - i};
            }
        }
        return null;
    }

    public int getXian(int x, int y) {
        int key = (x << 16) + y; int value = 0;
        if (zbMap.containsKey(key)) value = zbMap.get(key);
        else {
            if (x < 0 || 490 < x || y < 0 || 490 < y) value = 0;
            int rgb = minMap.getRGB(x, y);
            int i1 = rgb >> 16 & 0xff;
            int i2 = rgb >> 8 & 0xff;
            int i3 = rgb & 0xff;
            if (220 < i1 && i2 < 40 && i3 < 10) value = 3;
            else if (30 < i1 && i1 < 50 && 11 < i2 && i2 < 31 && 10 < i3 && i3 < 30) value = 2;     //敌方坦克
            zbMap.put(key, value);
        }
        return value;
    }
    public boolean getXian(int x, int y, int type) {
        if (x < 0 || 490 < x || y < 0 || 490 < y) return false;
        int rgb = minMap.getRGB(x, y);
        int i1 = rgb >> 16 & 0xff;
        int i2 = rgb >> 8 & 0xff;
        int i3 = rgb & 0xff;
        switch (type){
            case 1:
                if (140<i1&&i1<250 && 70<i2&&i2<170 && 10<i3&&i3<110) return true;     //鼠标线
            case 2:
                if (240<i1 && i2<10 && i3<10) return true;     //敌方坦克
            default: break;
        }

        return false;
    }

    public static void main(String[] args) throws IOException {
        CF.LU_XIAN = MinMapLX.MU_NI_HEI;
        CF.FEN_BIAN_LV = "1920*1017";
        CF.parLoad(args);
//        BufferedImage screenCapture = ImageIO.read(new File("\\\\192.168.0.165\\cx\\procedure\\photo\\1692651526393.png"));
        BufferedImage screenCapture = ImageIO.read(new File("\\\\192.168.0.83\\cx\\procedure\\photo\\1692875977316.png"));
//        BufferedImage screenCapture = ImageIO.read(new File("D:\\work\\java\\idea\\workspace\\worldoftank\\target\\photo\\1692735726678.png"));
        BufferedImage minMap = screenCapture.getSubimage(CF.MAP_START[0], CF.MAP_START[1], ZhanDou.MIN_MAP_W, CF.SCRN_SIZE[1] - CF.MAP_START[1] + CF.SCRN_SIZE[2]);
        int[] myAddr = ZhanDouFun.myAddr(minMap);

        new Thread(new PaoTaKongZhi(minMap, myAddr)).start();
    }
}
