package com.pt.gm.tank.kongzhi;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.map.MinMapLX;
import com.pt.gm.tank.zhandou.ZhanDou;
import com.pt.gm.tank.zhandou.ZhanDouFun;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-21 15:59
 */
public class PaoTaKongZhi implements Runnable{
    private BufferedImage minMap;
    private int[] myAddr;

    public PaoTaKongZhi(BufferedImage minMap, int[] myAddr) {
        this.minMap = minMap;
        this.myAddr = myAddr;
    }

    @Override
    public void run() {

        int[] xian = getXian();
        int jiaodu = ZhanDouFun.jiaodu(myAddr, xian);

        BufferedImage subimage = minMap.getSubimage(0, 0, 40, 40);
        ZhanDouFun.prinRGB(subimage);
        System.out.println(123);


    }

    private int[] getXian() {
        int length = StartMain.SCRN_SIZE[0] - StartMain.MAP_START[0] - 1;
        for (int i = 0; i <= length; i++) {
            for (int j = 0; j < 5; j++) {
                if (getXian(minMap.getRGB(i, j))) return new int[]{i, j};
                if (getXian(minMap.getRGB(length - j, i))) return new int[]{length - j, i};
                if (getXian(minMap.getRGB(length - i, length - j))) return new int[]{length - i, length - j};
                if (getXian(minMap.getRGB(j, length - i))) return new int[]{j, length - i};
            }
        }
        return null;
    }

    private boolean getXian(int rgb) {
        int i1 = rgb >> 16 & 0xff;
        int i2 = rgb >> 8 & 0xff;
        int i3 = rgb & 0xff;
        if (140<i1&&i1<250 && 70<i2&&i2<170 && 10<i3&&i3<110){      //鼠标线
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        StartMain.LU_XIAN = MinMapLX.AI_LI_HA_LUO_FU;
        StartMain.FEN_BIAN_LV = "1920*1080";
        StartMain.parLoad();
        BufferedImage screenCapture = ImageIO.read(new File("\\\\192.168.0.165\\cx\\procedure\\photo\\1692591598467.png"));
        BufferedImage minMap = screenCapture.getSubimage(StartMain.MAP_START[0], StartMain.MAP_START[1], ZhanDou.MIN_MAP_W, StartMain.SCRN_SIZE[1] - StartMain.MAP_START[1] + StartMain.SCRN_SIZE[2]);
        int[] myAddr = ZhanDouFun.myAddr(minMap);

        new Thread(new PaoTaKongZhi(minMap, myAddr)).start();
    }
}
