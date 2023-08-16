package com.pt.gm.tank;

import com.benjaminwan.ocrlibrary.OcrEngine;
import com.benjaminwan.ocrlibrary.RapidInstance;
import com.pt.gm.tank.jr.JiaRuZD;
import com.pt.gm.tank.kongzhi.ADRun;
import com.pt.gm.tank.kongzhi.WSRun;
import com.pt.gm.tank.map.MinMapLX;
import com.pt.gm.tank.mouse.MouseUtils;
import com.pt.gm.tank.util.ImgUtils;
import com.pt.gm.tank.zhandou.ZhanDou;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
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

    public static int[] RGB_K = {104, 157, 78};
    public static int[] RGB_MAX = {154, 207, 128};
    public static int[] RGB_MIN = {54, 107, 28};

    public static List<int[]> LU_XIAN;
    public static String TUPIAN_NEIRONG;
    // 创建一个Robot对象
    public static Robot robot = null;

    public static boolean OPEN_GUA_JI = true;       //默认自动运行
    public static String FEN_BIAN_LV = "";      //默认非全屏

    public static void main(String[] args) throws Exception {
        try {
            if (args != null && args.length > 0){
                OPEN_GUA_JI = Boolean.parseBoolean(args[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (args != null && args.length > 1){
                FEN_BIAN_LV = args[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        parLoad();

        new Thread(new WSRun()).start();     //开车线程（前后）
        new Thread(new ADRun()).start();     //开车线程（左右）

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
        if (!StringUtils.isBlank(TUPIAN_NEIRONG)) {
            if (TUPIAN_NEIRONG.contains("获得") || TUPIAN_NEIRONG.contains("遵命")) MouseUtils.mouseDianJi(880 + (int) (Math.random() * 74), 741 + (int) (Math.random() * 29));
            if (TUPIAN_NEIRONG.contains("活动期间")) MouseUtils.mouseDianJi(880 + (int) (Math.random() * 74), 941 + (int) (Math.random() * 89));
            if (TUPIAN_NEIRONG.contains("服务器连接已经断开")) {
                logger.debug("点击取消，重连服务器");
                MouseUtils.mouseDianJi(1049 + (int) (Math.random() * 90), 576 + (int) (Math.random() * 18));
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }
//        MinMapLX.getXingJingLuXian(screenshot);       //收集数据
        //分析加载地图n
        if (StartMain.LU_XIAN == null && !StringUtils.isBlank(TUPIAN_NEIRONG) && TUPIAN_NEIRONG.contains("随机战")) {
            MinMapLX.getXingJingLuXian(screenshot);
            if (StartMain.LU_XIAN == null) logger.debug("地图加载失败");
            else logger.debug("地图加载成功");
        }
        if (ImgUtils.notJarStart) StartMain.LU_XIAN = MinMapLX.AN_SI_KE;

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


    private static void parLoad(){
        if (StringUtils.isBlank(FEN_BIAN_LV)) return;
        switch (FEN_BIAN_LV){
            case "1920*1080":{
                SCRN_SIZE = new int[]{1920, 1080, 0, 0};       // 23 40 y轴上下边框分辨高 23 和40
                MAP_START = new int[]{1429, 589};     //小地图起点 不带边框是491*491
                IN_COMBAT = new int[]{880, 0, 155, 40};
                TANK_ADDR = new int[]{120, 818, 175, 112};
                TANK_CENTRE  = new int[]{760, 390, 400, 150};

                RGB_K = new int[]{104, 157, 78};
                RGB_MAX = new int[]{154, 207, 128};
                RGB_MIN = new int[]{54, 107, 28};
            }
        }


    }
}
