package com.fortune.util;

import cn.hutool.core.util.StrUtil;

public class ReportUtil {

    private ReportUtil() {
    }

    public static String summary(String fullReport) {
        if (StrUtil.isBlank(fullReport)) {
            return "测算结果生成中，本测算仅为趣味娱乐参考。";
        }
        int length = Math.max(180, fullReport.length() * 30 / 100);
        return fullReport.substring(0, Math.min(length, fullReport.length())) + "\n\n完整6大模块报告待解锁后查看。";
    }

    public static String sanitize(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("(?i)荐股|稳赚|保本|风水|算命|鬼神|命理", "娱乐参考");
    }
}
