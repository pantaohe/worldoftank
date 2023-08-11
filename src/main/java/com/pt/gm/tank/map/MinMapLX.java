package com.pt.gm.tank.map;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.util.ImgUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftankd
 * @description
 * @create 2023-08-09 10:25
 */
public class MinMapLX {
    private static Logger logger = LoggerFactory.getLogger(MinMapLX.class);
    public static final int QZB = 15;
    //旗子圈为 40 * 40 （半径20）      旗子 23 * 23（半径16左右）      自己 15 * 15（半径15）
    public static List<int[]> AI_LI_HA_LUO_FU = Arrays.asList(new int[]{392, 89}, new int[]{78, 401},
            new int[]{383, 115}, new int[]{390, 178}, new int[]{308, 313}, new int[]{172, 371});
    public static List<int[]> HU_BIAN_DE_JUE_ZHU = Arrays.asList(new int[]{99, 42}, new int[]{352, 431},
            new int[]{211, 132}, new int[]{167, 277}, new int[]{258, 427});
    public static List<int[]> LA_SHI_WEI_LI = Arrays.asList(new int[]{140, 49}, new int[]{140, 440},
            new int[]{294, 83}, new int[]{338, 143}, new int[]{355, 300}, new int[]{367, 335}, new int[]{310, 400});
    public static List<int[]> LU_BIE_KE = Arrays.asList(new int[]{206, 82}, new int[]{196, 426},
            new int[]{291, 116}, new int[]{276, 349}, new int[]{242, 411});
    public static List<int[]> JI_DI_BING_YUAN = Arrays.asList(new int[]{78, 394}, new int[]{439, 100},
            new int[]{182, 419}, new int[]{335, 362}, new int[]{337, 344}, new int[]{354, 209}, new int[]{419, 167});
    public static List<int[]> AN_SI_KE = Arrays.asList(new int[]{260, 40}, new int[]{260, 448},
            new int[]{220, 64}, new int[]{220, 99}, new int[]{229, 193}, new int[]{219, 384}, new int[]{199, 426});
//    public static List<int[]> MAN_HUANG_ZHI_DI = Arrays.asList(new int[]{392, 89}, new int[]{78, 401},
//            new int[]{383, 115}, new int[]{390, 178}, new int[]{308, 313}, new int[]{172, 371});



    public static void getXingJingLuXian(BufferedImage screenshot) throws IOException, InterruptedException {
        BufferedImage subimage = screenshot.getSubimage(460, 250, 456, 80);
        String fileContent = ImgUtils.getString(subimage);

        if (!luXian(fileContent)){
            StartMain.robot.keyPress(KeyEvent.VK_N);
            Thread.sleep(100);
            screenshot = ImgUtils.screenshot();
            StartMain.robot.keyRelease(KeyEvent.VK_N);
            subimage = screenshot.getSubimage(600, 40, 312, 100);
            fileContent = ImgUtils.getString(subimage);
            luXian("N-" + fileContent);
        }
    }

    private static boolean luXian(String fileContent) {
        logger.debug("读取的地图名：" + fileContent);
        if (fileContent.contains("埃里-哈罗夫")){
            StartMain.LU_XIAN = AI_LI_HA_LUO_FU;
        }else if (fileContent.contains("湖边的角逐")){
            StartMain.LU_XIAN = HU_BIAN_DE_JUE_ZHU;
        }else if (fileContent.contains("拉斯威利")){
            StartMain.LU_XIAN = LA_SHI_WEI_LI;
        }else if (fileContent.contains("鲁别克")){
            StartMain.LU_XIAN = LU_BIE_KE;
        }else if (fileContent.contains("极地冰原")){
            StartMain.LU_XIAN = JI_DI_BING_YUAN;
        }else if (fileContent.contains("安斯克")){
            StartMain.LU_XIAN = AN_SI_KE;
        }else{
            return false;
        }
        return true;
    }
}
