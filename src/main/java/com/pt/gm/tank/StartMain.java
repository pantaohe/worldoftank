package com.pt.gm.tank;

import com.benjaminwan.ocrlibrary.OcrEngine;
import com.benjaminwan.ocrlibrary.RapidInstance;
import com.pt.gm.tank.exec.RunCmd;
import com.pt.gm.tank.jr.JiaRuZD;
import com.pt.gm.tank.kongzhi.ADRun;
import com.pt.gm.tank.kongzhi.WSRun;
import com.pt.gm.tank.map.MinMapLX;
import com.pt.gm.tank.mouse.MouseUtils;
import com.pt.gm.tank.util.ImgUtils;
import com.pt.gm.tank.zhandou.ZhanDou;
import com.pt.gm.tank.zhandou.ZhanDouFun;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static int TIEHUA_HIGH = 741;
    public static int CENTER_Y_PAO = 455;

    public static int[] RGB_K = {104, 157, 78};
    public static int[] RGB_MAX = {154, 207, 128};
    public static int[] RGB_MIN = {54, 107, 28};

    public static List<int[]> LU_XIAN;
    public static String TUPIAN_NEIRONG;
    // 创建一个Robot对象
    public static Robot robot = null;

    public static boolean OPEN_GUA_JI = true;       //默认自动运行
    public static String FEN_BIAN_LV = "";      //默认非全屏
    public static List<Integer> CHE_XU = new ArrayList(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));      //车序
    public static int CENTER_Y = CENTER_Y_PAO - 50;      //鼠标移动y轴的中间
    public static boolean OPEN_KAI_PAO = true;      //是否自动开炮

    public static void main(String[] args) throws Exception {
        parLoad(args);

        new Thread(new WSRun()).start();     //开车线程（前后）
        new Thread(new ADRun()).start();     //开车线程（左右）

        robot = new Robot();
        OcrEngine instance = RapidInstance.getInstance();
        instance.setDoAngle(false);

        while (true) {
            startTank();

            System.out.println();
            Thread.sleep(500);
        }
    }


    private static int startTank() throws Exception {
        BufferedImage screenshot = ImgUtils.screenshot();
        TUPIAN_NEIRONG = ImgUtils.getString(screenshot);

        //贴花界面
        if (!StringUtils.isBlank(TUPIAN_NEIRONG)) {

            //右上角关闭
            if (TUPIAN_NEIRONG.contains("战斗通行证") || TUPIAN_NEIRONG.contains("贴花")) MouseUtils.mouseDianJi(1821 + (int) (Math.random() * 45), 52 + StartMain.SCRN_SIZE[2] + (int) (Math.random() * 9));
            if (TUPIAN_NEIRONG.contains("已获得奖励")) MouseUtils.mouseDianJi(1841 + (int) (Math.random() * 45), 30 + StartMain.SCRN_SIZE[2] + (int) (Math.random() * 9));

//            if (TUPIAN_NEIRONG.contains("贴花")) {
//                MouseUtils.mouseDianJi(880 + (int) (Math.random() * 74), TIEHUA_HIGH + (int) (Math.random() * 29));
//            }
//            if (TUPIAN_NEIRONG.contains("活动期间")) MouseUtils.mouseDianJi(880 + (int) (Math.random() * 74), 941 + (int) (Math.random() * 89));
            if (TUPIAN_NEIRONG.contains("服务器连接已经断开") || TUPIAN_NEIRONG.contains("健康游戏忠告抵制不良游戏")) {
                logger.debug("点击取消，重连服务器");
                MouseUtils.mouseDianJi(1049 + (int) (Math.random() * 90), 576 + (int) (Math.random() * 18));
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
            if (TUPIAN_NEIRONG.contains("离开战斗") || TUPIAN_NEIRONG.contains("返回车库")) ZhanDouFun.jihui();
            if (TUPIAN_NEIRONG.contains("确认购买")) {
                robot.keyPress(KeyEvent.VK_ESCAPE);
                robot.keyRelease(KeyEvent.VK_ESCAPE);
            }

        }
//        MinMapLX.getXingJingLuXian(screenshot);       //收集数据
        //分析加载地图n
        if (ImgUtils.notJarStart) StartMain.LU_XIAN = MinMapLX.WU_MONG_XIONG_SHAN;

        // 是否战斗界面分析
        int zdStatus = ZhanDou.zhandouFX(screenshot);
        if (StartMain.LU_XIAN == null && zdStatus > 0){
            MinMapLX.getXingJingLuXian(screenshot);
        }
        if (zdStatus == 2) {
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


    public static void parLoad(String[] args){
        try {
            if (args != null && args.length > 0){
                OPEN_GUA_JI = Boolean.parseBoolean(args[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("是否自动挂机：{}", OPEN_GUA_JI);

        try {
            if (args != null && args.length > 1){
                FEN_BIAN_LV = args[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("屏幕分辨率：{}", FEN_BIAN_LV);

        try {
            if (args != null && args.length > 2){
                String[] chexus = args[2].split("");
                Integer.parseInt(chexus[0]);
                CHE_XU.clear();
                for (int i = 0; i < chexus.length; i++) {
                    CHE_XU.add(Integer.parseInt(chexus[i]));
                    if (i > 9) break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("出车顺序：{}", CHE_XU);

        startTankGame(args);

        try {
            if (args != null && args.length > 4){
                OPEN_KAI_PAO = Boolean.parseBoolean(args[4]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("是否自动开炮：{}", OPEN_KAI_PAO);

        try {
            if (args != null && args.length > 5){
                CENTER_Y = Integer.parseInt(args[5]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




        if (StringUtils.isBlank(FEN_BIAN_LV)) return;
        switch (FEN_BIAN_LV){
            case "1920*1080":{
                SCRN_SIZE = new int[]{1920, 1080, 0, 0};       // 23 40 y轴上下边框分辨高 23 和40
                MAP_START = new int[]{1429, 589};     //小地图起点 不带边框是491*491
                IN_COMBAT = new int[]{880, 0, 155, 40};
                TANK_ADDR = new int[]{120, 818, 171, 112};

                TIEHUA_HIGH = 895;
                CENTER_Y_PAO = 458;
            }
        }


    }


    private static void startTankGame(String[] args) {
        try {
            if (args != null && args.length > 3){
                String tankStartPath = args[3];
                logger.debug("坦克世界启动程序目录：{}", tankStartPath);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String ls_1; boolean flag = false; Process process;
                        while (true){
                            try {
                                process = RunCmd.executeCmdGetProcess("TASKLIST /NH /FI \"IMAGENAME eq WorldOfTanks.exe\"");
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                                while ((ls_1 = bufferedReader.readLine()) != null) {
                                    if (ls_1.startsWith("WorldOfTanks.exe")) flag = true;
                                }
                                process.waitFor();
                                process.destroy();

                                if (!flag){     //程序不存在了
                                    process = RunCmd.executeCmdGetProcess(tankStartPath);
                                    logger.debug("未检测到游戏进程，开始启动游戏：{}", tankStartPath);
                                    process.waitFor();
                                    process.destroy();
                                }
                                flag = false;

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Thread.sleep(2*60*1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
