package com.jean.database.redis;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author jinshubao
 * @date 2016/11/25
 */
public class RedisConstant {

    public static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    public static final long KEY_SCAN_SIZE = 100;

    public static final long VALUE_SCAN_SIZE = 100;


    public static class KeyType {

        /**
         * key不存在
         */
        public static final String NONE = "none";

        /**
         * 字符串
         */
        public static final String STRING = "string";

        /**
         * 列表
         */
        public static final String LIST = "list";

        /**
         * 集合
         */
        public static final String SET = "set";

        /**
         * 有序集
         */
        public static final String ZSET = "zset";

        /**
         * 哈希表
         */
        public static final String HASH = "hash";

    }


}
