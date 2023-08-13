package com.pt.gm.tank.map;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.jr.JiaRuZD;
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
            new int[]{156, 114}, new int[]{104, 200}, new int[]{100, 333}, new int[]{215, 347}, new int[]{239, 432});
    public static List<int[]> LA_SHI_WEI_LI = Arrays.asList(new int[]{140, 49}, new int[]{140, 440},
            new int[]{251, 54}, new int[]{338, 143}, new int[]{355, 300}, new int[]{367, 335}, new int[]{310, 400}, new int[]{213, 439});
    public static List<int[]> LU_BIE_KE = Arrays.asList(new int[]{206, 82}, new int[]{196, 426},
            new int[]{286, 88}, new int[]{350, 194}, new int[]{343, 329}, new int[]{280, 426});
    public static List<int[]> JI_DI_BING_YUAN = Arrays.asList(new int[]{78, 394}, new int[]{439, 100},
            new int[]{182, 419}, new int[]{335, 362}, new int[]{337, 344}, new int[]{354, 209}, new int[]{419, 167});
    public static List<int[]> AN_SI_KE = Arrays.asList(new int[]{260, 40}, new int[]{260, 448},
            new int[]{220, 64}, new int[]{220, 99}, new int[]{229, 193}, new int[]{219, 384}, new int[]{199, 426});
    public static List<int[]> KA_LEI_LI_A = Arrays.asList(new int[]{439, 47}, new int[]{47, 440},
            new int[]{275, 155}, new int[]{189, 257}, new int[]{145, 303});
    public static List<int[]> SHEN_LI_ZHI_MEN = Arrays.asList(new int[]{59, 62}, new int[]{413, 440},
            new int[]{57, 84}, new int[]{70, 119}, new int[]{94, 139}, new int[]{100, 178}, new int[]{148, 245}, new int[]{229, 355}, new int[]{308, 352}, new int[]{361, 401});
    public static List<int[]> HUANG_MO_XIAO_ZHEN = Arrays.asList(new int[]{45, 176}, new int[]{427, 331},
            new int[]{153, 475}, new int[]{355, 436}, new int[]{368, 344});
    public static List<int[]> HUANG_MAN_ZHI_DI = Arrays.asList(new int[]{200, 67}, new int[]{356, 412},
            new int[]{211, 147}, new int[]{330, 333});
    public static List<int[]> KAN_PA_NI_YA = Arrays.asList(new int[]{221, 95}, new int[]{242, 406},
            new int[]{264, 101}, new int[]{220, 375}, new int[]{167, 384});
    public static List<int[]> PU_LUO_HUO = Arrays.asList(new int[]{182, 25}, new int[]{269, 463},
            new int[]{56, 156}, new int[]{59, 330});
    public static List<int[]> MA_LI_NUO_FU_KA = Arrays.asList(new int[]{61, 191}, new int[]{281, 436},
            new int[]{66, 247}, new int[]{223, 405});
    public static List<int[]> MU_NI_HEI = Arrays.asList(new int[]{295, 46}, new int[]{176, 405},
            new int[]{255, 205}, new int[]{180, 376});
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
        }else if (fileContent.contains("卡累利阿")){
            StartMain.LU_XIAN = KA_LEI_LI_A;
        }else if (fileContent.contains("胜利之门")){
            StartMain.LU_XIAN = SHEN_LI_ZHI_MEN;
        }else if (fileContent.contains("荒漠小镇")){
            StartMain.LU_XIAN = HUANG_MO_XIAO_ZHEN;
        }else if (fileContent.contains("荒蛮之地")){
            StartMain.LU_XIAN = HUANG_MAN_ZHI_DI;
        }else if (fileContent.contains("坎帕尼亚")){
            StartMain.LU_XIAN = KAN_PA_NI_YA;
        }else if (fileContent.contains("普罗霍洛夫卡")){
            StartMain.LU_XIAN = PU_LUO_HUO;
        }else if (fileContent.contains("马利诺夫卡")){
            StartMain.LU_XIAN = MA_LI_NUO_FU_KA;
        }else if (fileContent.contains("慕尼黑")){
            StartMain.LU_XIAN = MU_NI_HEI;
        }else{
            return false;
        }
        JiaRuZD.mapName = fileContent;
        return true;
    }
}
