package com.pt.gm.tank.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-25 11:04
 */
public class RunCmd {
    private static Logger logger = LoggerFactory.getLogger(RunCmd.class);
    private static Runtime run = Runtime.getRuntime();
    /**
     * 执行传入的cmd命令，返回Process类型
     * @param cmd
     * @return 执行结果
     */
    public static Process executeCmdGetProcess(Object cmd) {
        logger.info("准备执行命令" + cmd);
        String cmdStr = "";
        Process process = null;
        try {
            if (cmd instanceof String) {
                cmdStr = (String) cmd;
                process = run.exec(cmdStr);
            }else if (cmd instanceof String[]) {
                cmdStr = Arrays.toString((String[]) cmd);
                process = run.exec((String[]) cmd);
            }else {
                logger.error("传入类型无法执行");
                return null;
            }
            logger.info("命令执行成功：" + cmdStr);
            process.getOutputStream().close();
            return process;

        } catch (Exception e) {
            logger.error("执行命令" + cmdStr + "出错");
        }
        return null;
    }

}
