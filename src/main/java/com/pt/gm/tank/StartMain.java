package com.pt.gm.tank;

import com.benjaminwan.ocrlibrary.OcrEngine;
import com.benjaminwan.ocrlibrary.RapidInstance;
import com.pt.gm.tank.jr.JiaRuZD;
import com.pt.gm.tank.map.MinMapLX;
import com.pt.gm.tank.mouse.MouseUtils;
import com.pt.gm.tank.util.ImgUtils;
import com.pt.gm.tank.zd.FangXiangKongZhi;
import com.pt.gm.tank.zd.ZhanDou;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-07-24 14:38
 */
public class StartMain {

    private static Logger logger = LoggerFactory.getLogger(StartMain.class);

    public static int[] SCRN_SIZE = {1920, 1017, 23, 40};       // 23 40 y轴上下边框分辨高 23 和40
    public static int[] MAP_START = {1429, 549};     //小地图起点 不带边框是491*491
    public static int[] IN_COMBAT = {880, 31, 155, 40};
    public static int[] TANK_ADDR = {120, 772, 175, 112};
    public static int[] TANK_CENTRE  = {760, 390, 400, 150};

    public final static int[] RGB_K = {104, 157, 78};
    public final static int[] RGB_MAX = {154, 207, 128};
    public final static int[] RGB_MIN = {54, 107, 28};

    public static List<int[]> LU_XIAN;
    public static String TUPIAN_NEIRONG;
    // 创建一个Robot对象
    public static Robot robot = null;

    public static void main(String[] args) throws Exception {
        new Thread(new FangXiangKongZhi()).start();     //开车线程

        robot = new Robot();
        OcrEngine instance = RapidInstance.getInstance();
        instance.setDoAngle(false);

        while (true) {
            startTank();

            System.out.println();
            Thread.sleep(3000);
        }
    }


    private static int startTank() throws Exception {
        BufferedImage screenshot = ImgUtils.screenshot();
        TUPIAN_NEIRONG = ImgUtils.getString(screenshot);

        //贴花界面
        if (!StringUtils.isBlank(TUPIAN_NEIRONG) && TUPIAN_NEIRONG.contains("获得")) MouseUtils.mouseDianJi(880 + (int)(Math.random() * 74), 741 + (int)(Math.random() * 29));

//        MinMapLX.getXingJingLuXian(screenshot);       //收集数据
        //分析加载地图
        if (StartMain.LU_XIAN == null && !StringUtils.isBlank(TUPIAN_NEIRONG) && TUPIAN_NEIRONG.contains("随机战")) {
            MinMapLX.getXingJingLuXian(screenshot);
            if (StartMain.LU_XIAN == null) logger.debug("地图加载失败");
            else logger.debug("地图加载成功");
        }
        if (ImgUtils.notJarStart) StartMain.LU_XIAN = MinMapLX.LU_BIE_KE;

        // 是否战斗界面分析
        if (ZhanDou.zhandouFX(screenshot)) {
            ZhanDou.zhandou(screenshot);
            return 1;
        }

        // 分析是否加入界面
        if (JiaRuZD.jiarujiemian(screenshot)) {
            JiaRuZD.jiaRu(screenshot);
            return 2;
        }
        return 0;
    }


}