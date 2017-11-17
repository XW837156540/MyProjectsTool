package com.xu.baselib.http;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2017/11/13.
 */
public class HttpBaseParams implements IHttpParams{



    @Override
    public Map<String, String> getParams(Map<String, String> params) {

        if (params == null){
            params = new HashMap<>();
        }

        // TODO: 2017/11/13  根据各个公司的不同，设置不同的请求头
        String time = System.currentTimeMillis() / 1000 + "";
        params.put("appid", "appid");
        params.put("appkey", "appkey");
        params.put("sign", getSign(params));
        return null;
    }

    @Override
    public Map<String, String> getHeaders(Map<String, String> headers) {
        return headers;
    }

    /**
     * 使用 Map按key进行升序排序后,取出value进行MD5加密
     * @param map
     * @return
     */
    public  String getSign(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<>(new MapKeyComparator());
        sortMap.putAll(map);

        StringBuffer sb = new StringBuffer();
        for(String key : sortMap.keySet()) {
            sb.append(sortMap.get(key));
        }
        sb.append("141c82c01a02cae775552033e8f575bb");
        return md5Encode(sb.toString());
    }

    //比较器类
    class MapKeyComparator implements Comparator<String> {

        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    /**
     * 将摘要信息转换成MD5编码
     *
     * @param message 摘要信息
     * @return MD5编码之后的字符串
     */
    public static String md5Encode(String message) {
        return encode("MD5", message);
    }

    /**
     * 将摘要信息转换为相应的编码
     *
     * @param code    编码类型
     * @param message 摘要信息
     * @return 相应的编码字符串
     */
    private static String encode(String code, String message) {
        MessageDigest md;
        String encode = null;
        try {
            md = MessageDigest.getInstance(code);
            encode = byteArrayToHexString(md.digest(message.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encode;
    }
    /**
     * 将字节数组转换为16进制的字符串
     *
     * @param byteArray 字节数组
     * @return 16进制的字符串
     */
    private static String byteArrayToHexString(byte[] byteArray) {
        StringBuffer sb = new StringBuffer();
        for (byte byt : byteArray) {
            appendHexPair(byt, sb);
        }
        return sb.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换
        // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    private final static char hexDigits[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
}
