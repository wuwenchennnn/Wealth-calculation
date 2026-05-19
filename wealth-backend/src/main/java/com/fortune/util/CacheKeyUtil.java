package com.fortune.util;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.fortune.dto.CalculateRequest;

public class CacheKeyUtil {

    private CacheKeyUtil() {
    }

    public static String calculateKey(CalculateRequest request) {
        String raw = JSON.toJSONString(request);
        return "calc:" + DigestUtil.md5Hex(raw);
    }
}
