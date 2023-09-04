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
        BufferedImage subimage = screenshot.getSubimage(CF.IN_COMBAT[0], CF.IN_COMBAT[1], CF.IN_COMBAT[2], CF.IN_COMBAT[3]);
        String fileContent = ImgUtils.getString(subimage);

        return StringUtils.isNotBlank(fileContent) && (fileContent.contains("加入战斗") || fileContent.contains("准备战斗") || fileContent.contains("淮备战斗"));
    }

    public static boolean jiaRu(BufferedImage screenshot) throws InterruptedException {
        if (CF.TUPIAN_NEIRONG.contains("正在更新车库") || CF.TUPIAN_NEIRONG.contains("在线等待玩家")) return false;
        if (CF.getBooleanConfig("ZHU_DUI", false) && CF.TUPIAN_NEIRONG.contains("战斗结果") && CF.TUPIAN_NEIRONG.contains("个人战绩") && CF.TUPIAN_NEIRONG.contains("团队战绩") && CF.TUPIAN_NEIRONG.contains("细战绩")) {
            CF.robot.keyPress(KeyEvent.VK_ESCAPE);
            Thread.sleep(50);
            CF.robot.keyRelease(KeyEvent.VK_ESCAPE);
        }

        BufferedImage subimage = screenshot.getSubimage(733, 300, 450, 460);
        String fileContent = ImgUtils.getString(subimage);
        if (StringUtils.isNotBlank(fileContent) && fileContent.contains("处罚")) MouseUtils.mouseDianJi(1090 + (int)(Math.random() * 90), 733 + (int)(Math.random() * 18));
        logger.debug("当前是加入战斗界面");
        int x, y, i;
        for (int j = 0; j < CF.CHE_XU.size(); j++) {
            i = CF.CHE_XU.get(j) - 1;
            x = CF.TANK_ADDR[0] + CF.TANK_ADDR[2] * (i / 2);
            y = CF.TANK_ADDR[1] + CF.TANK_ADDR[3] * (i % 2);
            subimage = screenshot.getSubimage(x, y, CF.TANK_ADDR[2], CF.TANK_ADDR[3]);
            fileContent = ImgUtils.getString(subimage);

            logger.debug("第{}辆车：{}，当前坐标：{}-{}，（120, 772, 175, 112）", i, fileContent, x, y);
            if (fileContent.contains("购买")) {
                logger.debug("没有选中车，请添加车辆到车库, 如已在排队则忽略");
                break;       //如果多次在加入界面识别不出来车辆，则自动选中第一辆加入
            }
            if (fileContent.contains("战斗中") || fileContent.contains("战头中") || fileContent.contains("车组乘员不足")) continue;

            if (!jiarujiemian(ImgUtils.screenshot())) break;

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
            if (StringUtils.isNotBlank(fileContent)) tankName = fileContent;
        }
        return true;
    }

}
