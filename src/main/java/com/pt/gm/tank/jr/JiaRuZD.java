package com.pt.gm.tank.jr;

import com.pt.gm.tank.config.CF;
import com.pt.gm.tank.mouse.MouseUtils;
import com.pt.gm.tank.util.ImgUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-08 09:13
 */
public class JiaRuZD {
    private static Logger logger = LoggerFactory.getLogger(JiaRuZD.class);
    private static long timeT = System.currentTimeMillis();
    private static String tankName;
    public static String mapName = "";

    private static int jiaruIndex;

    public static boolean jiarujiemian(BufferedImage screenshot) {

        String fileContent = ImgUtils.getString(screenshot.getSubimage(CF.IN_COMBAT[0], CF.IN_COMBAT[1], CF.IN_COMBAT[2], CF.IN_COMBAT[3]));
        if (StringUtils.isBlank(fileContent) || !(fileContent.contains("加入战斗") || fileContent.contains("准备战斗") || fileContent.contains("淮备战斗"))) return false;

        fileContent = ImgUtils.getString(screenshot.getSubimage(800, 400, 320, 280));       //中间文字
        if (fileContent.contains("正在载入界面") || fileContent.contains("正在更新车库") || fileContent.contains("退出战斗")) return false;


        fileContent = ImgUtils.getString(screenshot.getSubimage(1100, 660, 200, 200));
        if (fileContent.contains("退出战场")) return false;

        return true;
    }

    public static boolean jiaRu(BufferedImage screenshot) throws InterruptedException {
        if (juntuanJiacheng(screenshot)){
            //车库按钮
            MouseUtils.mouseDianJi(654 + 5 + (int) (Math.random() * 35), 80 + 5 + (int) (Math.random() * 20));
            logger.debug("4返回车库");
        }


        if (CF.TUPIAN_NEIRONG.contains("战斗结果") && CF.TUPIAN_NEIRONG.contains("个人战绩") && CF.TUPIAN_NEIRONG.contains("团队战绩")) {
            MouseUtils.mouseDianJi(CF.SCRN_SIZE[0]/2, CF.SCRN_SIZE[1]/2);
            CF.robot.keyPress(KeyEvent.VK_ESCAPE);
            Thread.sleep(50);
            CF.robot.keyRelease(KeyEvent.VK_ESCAPE);
        }

        BufferedImage subimage = screenshot.getSubimage(733, 300, 450, 460);
        String fileContent = ImgUtils.getString(subimage);
        if (StringUtils.isNotBlank(fileContent) && (fileContent.contains("违反了游戏规则") || fileContent.contains("无所作为")))
            MouseUtils.mouseDianJi(1090 + (int)(Math.random() * 90), 733 + (int)(Math.random() * 18));
        logger.debug("当前是加入战斗界面");
        int x, y, i;
        for (int j = 0; j < CF.CHE_XU.size(); j++) {
            i = CF.CHE_XU.get(j) - 1;
            x = CF.TANK_ADDR[0] + CF.TANK_ADDR[2] * (i / 2);
            y = CF.TANK_ADDR[1] + CF.TANK_ADDR[3] * (i % 2);
            subimage = screenshot.getSubimage(x, y, CF.TANK_ADDR[2], CF.TANK_ADDR[3]);
            fileContent = ImgUtils.getString(subimage);

            logger.debug("第{}辆车：{}，当前坐标：{}-{}，（120, 772, 175, 112）", i+1, fileContent, x, y);
            if (fileContent.contains("购买")) {
                logger.debug("没有选中车，请添加车辆到车库, 如已在排队则忽略");
                break;       //如果多次在加入界面识别不出来车辆，则自动选中第一辆加入
            }
            if (fileContent.contains("战斗中") || fileContent.contains("战头中") || fileContent.contains("车组乘员不足")) continue;

            MouseUtils.mouseDianJi(x + 5 + (int) (Math.random() * (CF.TANK_ADDR[2] - 10)), y + 5 + (int) (Math.random() * (CF.TANK_ADDR[3] - 10)));
            Thread.sleep(1000);
            if (CF.getBooleanConfig("ZHU_DUI", false)) {
                String zhunbei = ImgUtils.getString(screenshot.getSubimage(803, 722, 74, 18)).replaceAll("!|！", "");
                logger.debug("小队准备处文字：{}", zhunbei);
                if ("准备".equals(zhunbei) || "淮备".equals(zhunbei)) {
                    MouseUtils.mouseDianJi(810 + (int) (Math.random() * 60), 726 + (int) (Math.random() * 10));
                    Thread.sleep(1000);
                }
            }

            MouseUtils.mouseDianJi(CF.IN_COMBAT[0] + 10 + (int)(Math.random() * (CF.IN_COMBAT[2] - 20)), CF.IN_COMBAT[1] + 5 + (int)(Math.random() * (CF.IN_COMBAT[3] - 10)));

            long l = System.currentTimeMillis() - timeT;
            if (l > 100000) {
                logger.debug("上一场战斗总耗时：{}，车辆：{}，地图：{}", l / 1000 + "s", tankName, mapName);
                timeT = System.currentTimeMillis();
            }
            tankName = "第" + (i+1) + "号车：" + fileContent;

            Thread.sleep(500);
            if (!jiarujiemian(ImgUtils.screenshot())) break;
        }
        return true;
    }

    private static boolean juntuanJiacheng(BufferedImage screenshot) throws InterruptedException {
        Integer jun_tuan_1 = CF.getIntegerConfig("JUN_TUAN_1");
        Integer jun_tuan_2 = CF.getIntegerConfig("JUN_TUAN_2");

        boolean flag1 = jun_tuan_1 != null && !isOpenJC(screenshot, 1);
        boolean flag2 = jun_tuan_2 != null && !isOpenJC(screenshot, 2);
        if (!flag1 && !flag2) return jun_tuan_1 != null || jun_tuan_2 != null;       //两个加成都已经开启

        logger.debug("1开始打开军团加成");
        MouseUtils.mouseDianJi(1586+5 + (int) (Math.random() * 28) , CF.SCRN_SIZE[2]+5 + 9 + (int) (Math.random() * 27));       //点击加成页面
        Thread.sleep(3000);
        logger.debug("2所有加成页面");

        if (flag1){
            MouseUtils.mouseDianJiJuJiao(1155+5 + (int) (Math.random() * 144) , 767+5 + (int) (Math.random() * 21));
        }else {
            MouseUtils.mouseDianJiJuJiao(1155+5 + 266 + (int) (Math.random() * 144) , 767+5 + (int) (Math.random() * 21));
        }
        Thread.sleep(5000);

        long l = System.currentTimeMillis();
        while (ImgUtils.getString(ImgUtils.screenshot()).contains("正在加载内容")){
            if (System.currentTimeMillis() - l > 2*60*1000) break;
            Thread.sleep(5000);
        }
        logger.debug("3军团加成页面");

        BufferedImage screenshotT = ImgUtils.screenshot();
        if (!ImgUtils.getString(screenshotT).contains("军团战斗")) return true;

        //在小队里面
        String contentT = ImgUtils.getString(screenshotT.getSubimage(550, 280, 200, 100));
        if (contentT.contains("小队") && contentT.contains("标准模式"))
            MouseUtils.mouseDianJi(161 + 5 + (int) (Math.random() * 118), 1008 + 5 + (int) (Math.random() * 15));       //点谁


        int index;String name;
        if (flag1) {
            index = 0;
            while (true) {
                if (Math.random() * 100 < jun_tuan_1) {
                    name = "全局经验";
                    MouseUtils.mouseDianJiJuJiao(473 + 5 + (int) (Math.random() * 110), 807 + 5 + (int) (Math.random() * 110));       //点谁
                    Thread.sleep(1000);
                    MouseUtils.mouseDianJiJuJiao(716 + 5 + (int) (Math.random() * 95), 691 + 5 + (int) (Math.random() * 10));     //点启用
                } else {
                    name = "成员经验";
                    MouseUtils.mouseDianJiJuJiao(473 + 5 + 121 + (int) (Math.random() * 110), 807 + 5 + (int) (Math.random() * 110));
                    Thread.sleep(1000);
                    MouseUtils.mouseDianJiJuJiao(907 + 5 + (int) (Math.random() * 95), 691 + 5 + (int) (Math.random() * 10));
                }
                Thread.sleep(1000);
                if (isOpenJC(ImgUtils.screenshot(), 1)) {
                    logger.debug(name + "加成已开启");
                    break;
                }
                if (index++ > 3) break;
//                Thread.sleep(1000);
            }
        }

        if (flag2) {
            index = 0;
            while (true) {
                if (Math.random() * 100 < jun_tuan_2) {
                    name = "银币";
                    MouseUtils.mouseDianJiJuJiao(754 + 5 + (int) (Math.random() * 110), 807 + 5 + (int) (Math.random() * 110));
                    Thread.sleep(1000);
                    MouseUtils.mouseDianJiJuJiao(1067 + 5 + (int) (Math.random() * 95), 691 + 5 + (int) (Math.random() * 10));
                } else {
                    name = "个体经验";
                    MouseUtils.mouseDianJiJuJiao(754 + 5 + 121 + (int) (Math.random() * 110), 807 + 5 + (int) (Math.random() * 110));
                    Thread.sleep(1000);
                    MouseUtils.mouseDianJiJuJiao(1118 + 5 + (int) (Math.random() * 95), 691 + 5 + (int) (Math.random() * 10));
                }
                Thread.sleep(1000);
                if (isOpenJC(ImgUtils.screenshot(), 2)) {
                    logger.debug(name + "加成已开启");
                    break;
                }
                if (index++ > 3) break;
            }
        }

        return true;
    }

    private static boolean isOpenJC(BufferedImage screenshot, int type) {
        BufferedImage jcImg = screenshot.getSubimage(1506 + 40 * type, CF.SCRN_SIZE[2] + 9, 38, 37);
        int height = jcImg.getHeight();
        for (int i = 1; i < 8; i++) {
            for (int j = 0; j < jcImg.getWidth(); j++) {
                int rgb = jcImg.getRGB(j, height - i);
                int i1 = rgb >> 16 & 0xff;
                int i2 = rgb >> 8 & 0xff;
                int i3 = rgb & 0xff;
                if (10<i1&&i1<60 && 40<i2&&i2<90 && 0<i3&&i3<50){
                    return true;
                }
            }
        }
        return false;
    }

}
