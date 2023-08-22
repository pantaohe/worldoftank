package com.pt.gm.tank.jr;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.mouse.MouseUtils;
import com.pt.gm.tank.util.ImgUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;

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
        BufferedImage subimage = screenshot.getSubimage(StartMain.IN_COMBAT[0], StartMain.IN_COMBAT[1], StartMain.IN_COMBAT[2], StartMain.IN_COMBAT[3]);
        String fileContent = ImgUtils.getString(subimage);

        return StringUtils.isNotBlank(fileContent) && fileContent.contains("加入战斗");
    }

    public static boolean jiaRu(BufferedImage screenshot) throws InterruptedException {
        if (StartMain.TUPIAN_NEIRONG.contains("正在更新车库")) return false;
        BufferedImage subimage = screenshot.getSubimage(733, 300, 450, 460);
        String fileContent = ImgUtils.getString(subimage);
        if (StringUtils.isNotBlank(fileContent) && fileContent.contains("处罚")) MouseUtils.mouseDianJi(1090 + (int)(Math.random() * 90), 733 + (int)(Math.random() * 18));
        logger.debug("当前是加入战斗界面");
        int x, y, i;
        for (int j = 0; j < StartMain.CHE_XU.size(); j++) {
            i = StartMain.CHE_XU.get(j) - 1;
            x = StartMain.TANK_ADDR[0] + StartMain.TANK_ADDR[2] * (i / 2);
            y = StartMain.TANK_ADDR[1] + StartMain.TANK_ADDR[3] * (i % 2);
            subimage = screenshot.getSubimage(x, y, StartMain.TANK_ADDR[2], StartMain.TANK_ADDR[3]);
            fileContent = ImgUtils.getString(subimage);

            logger.debug("第{}辆车：{}，当前坐标：{}-{}，（120, 772, 175, 112）", i, fileContent, x, y);
            if (fileContent.contains("购买")) {
                logger.debug("没有选中车，请添加车辆到车库, 如已在排队则忽略");
                break;       //如果多次在加入界面识别不出来车辆，则自动选中第一辆加入
            }
            if (fileContent.contains("战斗中") || fileContent.contains("战头中") || fileContent.contains("车组乘员不足")) continue;

            MouseUtils.mouseDianJi(x + (int) (Math.random() * StartMain.TANK_ADDR[2]), y + (int) (Math.random() * StartMain.TANK_ADDR[3]));
            Thread.sleep(1000);
            MouseUtils.mouseDianJi(StartMain.IN_COMBAT[0] + (int)(Math.random() * StartMain.IN_COMBAT[2]), StartMain.IN_COMBAT[1] + (int)(Math.random() * StartMain.IN_COMBAT[3]));

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
