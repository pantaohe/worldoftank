package com.pt.gm.tank.config;

import com.pt.gm.tank.exec.RunCmd;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-30 13:47
 */
public class CF {
    private static Logger logger = LoggerFactory.getLogger(CF.class);
    public static Map CF_MAP;
    public static Robot robot = null;   // 创建一个Robot对象


    public static boolean ONLY_RESTART = false;       //默认自动运行
    public static boolean OPEN_GUA_JI = true;       //默认自动运行
    public static String FEN_BIAN_LV = "";      //默认非全屏
    public static List<Integer> CHE_XU = new ArrayList(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));      //车序
    public static boolean MO_REN_KAI_PAO = true;      //是否默认开炮
    public static boolean OPEN_KAI_PAO = true;      //是否自动开炮
    public static boolean ONLY_SCREENSHOT = true;      //是否只开启截图功能
    public static long SCREENSHOT_TIME = 600 * 1000;      //截图保存间隔时间

    public static int[] SCRN_SIZE = {1920, 1017, 23, 40};       // 23 40 y轴上下边框分辨高 23 和40
    public static int[] MAP_START = {1429, 549};     //小地图起点 不带边框是491*491
    public static int[] IN_COMBAT = {880, 31, 155, 40};
    public static int[] TANK_ADDR = {120, 772, 175, 112};
    public static int TIEHUA_HIGH = 741;
    public static int CENTER_Y_PAO = 455;
    public static int CENTER_Y = CENTER_Y_PAO - 50;      //鼠标移动y轴的中间

    public static int[] RGB_MAX = {154, 207, 128};
    public static int[] RGB_MIN = {54, 107, 28};

    public static List<int[]> LU_XIAN;
    public static String TUPIAN_NEIRONG;

    public static void parLoad(String[] args){
        try {
            Yaml yaml = new Yaml();
            File file = new File("application.yml");
            if (!file.exists()) return;
            CF_MAP = (Map)yaml.load(FileUtils.openInputStream(file));

            Boolean flag = getBooleanConfig("ONLY_RESTART");
            if (flag != null) ONLY_RESTART = flag;

            flag = getBooleanConfig("OPEN_GUA_JI");
            if (flag != null) OPEN_GUA_JI = flag;

            String fen_bian_lv = getStringConfig("OPEN_GUA_JI");
            if (StringUtils.isNotBlank(fen_bian_lv)) FEN_BIAN_LV = fen_bian_lv;

            List<Integer> che_xu = getListConfig("CHE_XU");
            if (che_xu != null && che_xu.size() > 0) CHE_XU = che_xu;

            flag = getBooleanConfig("OPEN_KAI_PAO");
            if (flag != null) OPEN_KAI_PAO = flag;

            flag = getBooleanConfig("MO_REN_KAI_PAO");
            if (flag != null) MO_REN_KAI_PAO = flag;

            flag = getBooleanConfig("ONLY_SCREENSHOT");
            if (flag != null) ONLY_SCREENSHOT = flag;

            int screenshot_time = getIntegerConfig("SCREENSHOT_TIME");
            if (screenshot_time != 0) SCREENSHOT_TIME = screenshot_time * 1000;



            logger.debug("是否自动挂机：{}", OPEN_GUA_JI);
            logger.debug("屏幕分辨率：{}", FEN_BIAN_LV);
            logger.debug("出车顺序：{}", CHE_XU);
            logger.debug("是否自动开炮：{}", OPEN_KAI_PAO);
            logger.debug("设置开炮按键：{}", OPEN_KAI_PAO);

            startTankGame();
        } catch (Exception e) {
            e.printStackTrace();
        }


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

    public static void startTankGame() {
        String worldOfTanks = "WorldOfTanks.exe";
        try {
            String TANK_START_PATH = getStringConfig("TANK_START_PATH");
            if (StringUtils.isNotBlank(TANK_START_PATH)){
                logger.debug("未设置坦克世界启动目录，无闪退重启");
                return;
            }else {
                logger.debug("坦克世界启动程序目录：{}", TANK_START_PATH);
            }
            String TANK_START_FILE = TANK_START_PATH + "\\" + worldOfTanks;
            new Thread(new Runnable() {
                @Override
                public void run() {

                    String ls_1; boolean flag = false; Process process;
                    while (true){
                        try {
                            process = RunCmd.executeCmdGetProcess("TASKLIST /NH /FI \"IMAGENAME eq " + worldOfTanks + "\"");
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                            while ((ls_1 = bufferedReader.readLine()) != null) {
                                if (ls_1.startsWith(worldOfTanks)) flag = true;
                            }
                            process.waitFor();
                            process.destroy();

                            if (!flag){     //程序不存在了
                                process = RunCmd.executeCmdGetProcess(TANK_START_FILE);
                                logger.debug("未检测到游戏进程，开始启动游戏：{}", TANK_START_FILE);
                                process.waitFor();
                                process.destroy();
                            }
                            flag = false;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(60*1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getStringConfig(String key){
        return getStringConfig(key, null);
    }
    public static String getStringConfig(String key, String defaultValue){
        try {
            return (String)CF_MAP.get(key);
        } catch (Exception e) {
            logger.debug("{}获取错误。错误消息{}", key, e.getMessage());
        }
        return null;
    }
    public static Boolean getBooleanConfig(String key){
        return getBooleanConfig(key, null);
    }
    public static Boolean getBooleanConfig(String key, Boolean defaultValue){
        try {
            return (Boolean) CF_MAP.get(key);
        } catch (Exception e) {
            logger.debug("{}获取错误。错误消息{}", key, e.getMessage());
        }
        return defaultValue;
    }
    public static Integer getIntegerConfig(String key){
        return getIntegerConfig(key, null);
    }
    public static Integer getIntegerConfig(String key, Integer defaultValue){
        try {
            return (Integer) CF_MAP.get(key);
        } catch (Exception e) {
            logger.debug("{}获取错误。错误消息{}", key, e.getMessage());
        }
        return defaultValue;
    }
    public static List<Integer> getListConfig(String key){
        return getListConfig(key, null);
    }
    public static List<Integer> getListConfig(String key, List<Integer> defaultValue){
        try {
            return (List<Integer>) CF_MAP.get(key);
        } catch (Exception e) {
            logger.debug("{}获取错误。错误消息{}", key, e.getMessage());
        }
        return defaultValue;
    }
    public static List<int[]> getListIntsConfig(String key){
        return getListIntsConfig(key, null);
    }
    public static List<int[]> getListIntsConfig(String key, List<int[]> defaultValue){
        try {
            List<List<Integer>> zuobiaoQ = (List<List<Integer>>) CF_MAP.get(key);

            ArrayList<int[]> ints = new ArrayList<>();
            for (int i = 0; i < zuobiaoQ.size(); i++) {
                int[] zuobiaoS = new int[2];
                List<Integer> zuobiao = zuobiaoQ.get(i);
                for (int j = 0; j < zuobiao.size(); j++) {
                    zuobiaoS[j] = zuobiao.get(j);
                }
                ints.add(zuobiaoS);
            }
            return ints;
        } catch (Exception e) {
            logger.debug("{}获取错误。错误消息{}", key, e.getMessage());
        }
        return defaultValue;
    }
}
