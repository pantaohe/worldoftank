package com.pt.gm.tank;

import com.benjaminwan.ocrlibrary.OcrEngine;
import com.benjaminwan.ocrlibrary.RapidInstance;
import com.pt.gm.tank.config.CF;
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

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-07-24 14:38
 */
public class StartMain {

    private static Logger logger = LoggerFactory.getLogger(StartMain.class);

    public static void main(String[] args) throws Exception {
        CF.parLoad(args);

        if (CF.ONLY_RESTART) return;

        new Thread(new WSRun()).start();     //开车线程（前后）
        new Thread(new ADRun()).start();     //开车线程（左右）

        CF.robot = new Robot();
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

        if (CF.ONLY_SCREENSHOT) return 0;

        CF.TUPIAN_NEIRONG = ImgUtils.getString(screenshot);

        //贴花界面
        if (!StringUtils.isBlank(CF.TUPIAN_NEIRONG)) {

            //右上角关闭
            if (CF.TUPIAN_NEIRONG.contains("战斗通行证") || CF.TUPIAN_NEIRONG.contains("贴花") || CF.TUPIAN_NEIRONG.contains("史诗奖励")) MouseUtils.mouseDianJi(1821 + (int) (Math.random() * 45), 52 + CF.SCRN_SIZE[2] + (int) (Math.random() * 9));
            if (CF.TUPIAN_NEIRONG.contains("已获得奖励")) MouseUtils.mouseDianJi(1841 + (int) (Math.random() * 45), 30 + CF.SCRN_SIZE[2] + (int) (Math.random() * 9));

//            if (TUPIAN_NEIRONG.contains("贴花")) {
//                MouseUtils.mouseDianJi(880 + (int) (Math.random() * 74), TIEHUA_HIGH + (int) (Math.random() * 29));
//            }
//            if (TUPIAN_NEIRONG.contains("活动期间")) MouseUtils.mouseDianJi(880 + (int) (Math.random() * 74), 941 + (int) (Math.random() * 89));
            if (CF.TUPIAN_NEIRONG.contains("服务器连接已经断开") || CF.TUPIAN_NEIRONG.contains("健康游戏忠告抵制不良游戏")) {
                logger.debug("点击取消，重连服务器");
                MouseUtils.mouseDianJi(1049 + (int) (Math.random() * 90), 576 + (int) (Math.random() * 18));
                CF.robot.keyPress(KeyEvent.VK_ENTER);
                CF.robot.keyRelease(KeyEvent.VK_ENTER);
            }
            if (CF.TUPIAN_NEIRONG.contains("离开战斗") || CF.TUPIAN_NEIRONG.contains("返回车库")) ZhanDouFun.jihui();
            if (CF.TUPIAN_NEIRONG.contains("确认购买") || CF.TUPIAN_NEIRONG.contains("小队未准备好")) {
                CF.robot.keyPress(KeyEvent.VK_ESCAPE);
                Thread.sleep(50);
                CF.robot.keyRelease(KeyEvent.VK_ESCAPE);
            }

        }
//        MinMapLX.getXingJingLuXian(screenshot);       //收集数据
        //分析加载地图n
        if (ImgUtils.notJarStart) CF.LU_XIAN = MinMapLX.WU_MONG_XIONG_SHAN;

        // 是否战斗界面分析
        int zdStatus = ZhanDou.zhandouFX(screenshot);
        if (CF.LU_XIAN == null && zdStatus > 0){
            MinMapLX.getXingJingLuXian();
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


}
