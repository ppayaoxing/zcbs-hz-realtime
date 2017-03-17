package com.zcbspay.platform.hz.realtime.transfer.message.util;

import java.util.ResourceBundle;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcbspay.platform.hz.realtime.common.utils.date.DateUtil;

/**
 * 定时加载properties文件参数
 * 
 * @author AlanMa
 *
 */
public class ParamsUtil {

    private static final Logger log = LoggerFactory.getLogger(ParamsUtil.class);

    /**
     * ZCBS——HZ私钥
     */
    private String privateKey;

    /*
     * 工具运行参数
     */
    private boolean canRun;
    private String refresh_interval;
    private static ParamsUtil constant;
    // private String path = "/home/web/trade/unionpay/";
    // private String fileName = "unionpay_params.properties";
    private static ResourceBundle RESOURCE;

    public static synchronized ParamsUtil getInstance() {
        if (constant == null) {
            constant = new ParamsUtil();
        }
        return constant;
    }

    private ParamsUtil() {
        refresh();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (canRun) {
                    try {
                        refresh();
                        int interval = NumberUtils.toInt(refresh_interval, 60) * 1000;// 刷新间隔，单位：秒
                        log.info("refresh Constant datetime:" + DateUtil.getCurrentDateTime());
                        Thread.sleep(interval);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void refresh() {
        RESOURCE = ResourceBundle.getBundle("hzqszx_params");
        privateKey = RESOURCE.getString("privateKey");

        canRun = true;
        refresh_interval = RESOURCE.getString("refresh_interval");
    }

    
    public String getPrivateKey() {
        return privateKey;
    }

    
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

   
}
