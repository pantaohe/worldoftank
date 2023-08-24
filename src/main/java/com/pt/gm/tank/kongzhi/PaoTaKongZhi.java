package com.pt.gm.tank.kongzhi;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.map.MinMapLX;
import com.pt.gm.tank.mouse.MouseUtils;
import com.pt.gm.tank.po.DifangTank;
import com.pt.gm.tank.zhandou.TankUtils;
import com.pt.gm.tank.zhandou.ZhanDou;
import com.pt.gm.tank.zhandou.ZhanDouFun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

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

            DifangTank difang = getDifangAddr();
            if (difang == null) return;
            int mbjd = ZhanDouFun.jiaodu(myAddr, difang.getAddr());

            int xzjd = (paojd - mbjd + 360) % 360;       //自己向右旋转的度数

            int x = (xzjd < 180 ? (xzjd + 180) : (xzjd - 180)) * StartMain.SCRN_SIZE[0] / 360;
            logger.debug("炮角度{}，目标角度{}，x像素{}", paojd, mbjd, x);
            StartMain.robot = new Robot();
            StartMain.robot.mouseMove(x, 460);
//            BufferedImage subimage = minMap.getSubimage(0, 0, 40, 40);
//            ZhanDouFun.prinRGB(subimage);
//            System.out.println(123);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private DifangTank getDifangAddr() {
        int x0 = myAddr[0];
        int y0 = myAddr[1];
        DifangTank tank;
        for (int i = 10; i < 150; i++) {
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
        for (int i = -4; i <= 4; i++) {
            for (int j = -4; j <= 4; j++) {
                tankT = getTank(x + i, y + j);
                if (tankT != null && maxTank.getWaiquan() < tankT.getWaiquan()) maxTank = tankT;
            }
        }
        return maxTank;
    }

    private DifangTank getTank(int x, int y) {
        int type,tk = 0,sum = 0,tkn = 0,sumn = 0;double dd;
        for (int i = -19; i < 19; i++) {
            for (int j = -19; j < 19; j++) {
                dd = Math.pow(i, 2) + Math.pow(j, 2);
                if (dd < 160) {
                    type = getXian(x + i, y + j);
                    if (0 != type) {
                        sumn++;
                        if (2 == type) tkn++;
                    }
                }else if (dd < 362) {      //坦克边框
                    type = getXian(x + i, y + j);
                    if (0 != type) {
                        sum++;
                        if (2 == type) tk++;
                    }
                }
            }
        }

        double waiquan = tk * 1.0 / sum;
        double neiquan = tkn * 1.0 / sumn;
        if (waiquan > 0.53){
//            logger.debug("边框比例大于0.53:{}-{}--{}-{}",x,y,waiquan,neiquan);
            return new DifangTank(x, y, waiquan, neiquan);
        }
        return null;
    }

    private int[] getXian() {
        int length = StartMain.SCRN_SIZE[0] - StartMain.MAP_START[0] - 1;
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
        if (x < 0 || 490 < x || y < 0 || 490 < y) return 0;
        int rgb = minMap.getRGB(x, y);
        int i1 = rgb >> 16 & 0xff;
        int i2 = rgb >> 8 & 0xff;
        int i3 = rgb & 0xff;
        if (240<i1 && i2<30 && i3<10
                ||  (21<i1&&i1<41 && 11<i2&&i2<31 && 22<i3&&i3<42)) return 2;     //敌方坦克
        return 1;
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
        StartMain.LU_XIAN = MinMapLX.LU_BIE_KE;
        StartMain.FEN_BIAN_LV = "1920*1080";
        StartMain.parLoad();
//        BufferedImage screenCapture = ImageIO.read(new File("\\\\192.168.0.165\\cx\\procedure\\photo\\1692651526393.png"));
        BufferedImage screenCapture = ImageIO.read(new File("D:\\work\\java\\idea\\workspace\\worldoftank\\target\\photo\\1692735726678.png"));
        BufferedImage minMap = screenCapture.getSubimage(StartMain.MAP_START[0], StartMain.MAP_START[1], ZhanDou.MIN_MAP_W, StartMain.SCRN_SIZE[1] - StartMain.MAP_START[1] + StartMain.SCRN_SIZE[2]);
        int[] myAddr = ZhanDouFun.myAddr(minMap);

        new Thread(new PaoTaKongZhi(minMap, myAddr)).start();
    }
}
