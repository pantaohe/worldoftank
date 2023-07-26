package com.pt.gm.tank;

import com.benjaminwan.ocrlibrary.OcrEngine;
import com.benjaminwan.ocrlibrary.RapidInstance;
import com.jc.modules.recognition.FileReadUtils;
import com.jc.modules.recognition.mime.entry.FileContentEntry;
import com.pt.gm.tank.util.ImgUtils;
import com.pt.gm.tank.zd.ZhanDou;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-07-24 14:38
 */
public class StartMain {

    private static Logger logger = LoggerFactory.getLogger(StartMain.class);

    public static int[] SCRN_SIZE = {1920, 1080};
    public static int[] MAP_START = {1513, 672};     //小地图起点 带边框是407*407，不带边框是393*393
    private static int[] IN_COMBAT = {880, 8, 160, 40};
    private static int[] TANK_ADDR = {120, 812, 175, 112};
    public static int[] TANK_CENTRE  = {760, 390, 400, 150};

    public final static int[] RGB_K = {104, 157, 78};
    public final static int[] RGB_MAX = {154, 207, 128};
    public final static int[] RGB_MIN = {54, 107, 28};
    // 创建一个Robot对象
    public static Robot robot = null;

    public static void main(String[] args) throws Exception {
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
        BufferedImage subimage;
        String fileContent;

        //右上角时间
        subimage = screenshot.getSubimage(1860, 0, 50, 30);
        fileContent = ImgUtils.getString(subimage);

        //有时间表示战斗中
        if (StringUtils.isNotBlank(fileContent) && fileContent.contains(":")){
            ZhanDou.zhandou(fileContent, screenshot);
            return 1;
        }


        subimage = screenshot.getSubimage(IN_COMBAT[0], IN_COMBAT[1], IN_COMBAT[2], IN_COMBAT[3]);
        fileContent = ImgUtils.getString(subimage);

        if (StringUtils.isBlank(fileContent)) return 0;

        //加入页面
        if (fileContent.contains("加入战斗")){
            logger.debug("当前加入战斗界面");
            int index = 0;
            while(true) {
                int x = TANK_ADDR[0] + TANK_ADDR[2] * index++;
                subimage = screenshot.getSubimage(x, TANK_ADDR[1], TANK_ADDR[2], TANK_ADDR[3]);
                fileContent = ImgUtils.getString(subimage);
                if (StringUtils.isBlank(fileContent) || fileContent.contains("战斗中") || fileContent.contains("车组乘员不足")) continue;


                robot.mouseMove(x + TANK_ADDR[2]/2, TANK_ADDR[1] + TANK_ADDR[3]/2);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                logger.debug("选中：" + fileContent.replaceAll("\\s", "") + "坦克");

                robot.mouseMove(IN_COMBAT[0] + IN_COMBAT[2]/2, IN_COMBAT[1] + IN_COMBAT[3]/3);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                logger.debug("点击开始");

                return 1;
            }
        }






        System.out.println(fileContent);

        return 1;
    }




}
