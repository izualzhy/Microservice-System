package cn.izualzhy.gateway.util;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.util.zip.Inflater;

@Log4j2
public class Utils {
    public static String tryDecompress(byte[] data) {
//        try {
//            return new String(data, StandardCharsets.UTF_8); // 先尝试直接解析
//        } catch (Exception e) {
//            log.warn("Direct UTF-8 parsing failed, trying decompression...", e);
//        }

        try {
            Inflater inflater = new Inflater(true); // WebSocket deflate 需要 `nowrap=true`
            inflater.setInput(data);

            byte[] buffer = new byte[1024];
            StringBuilder sb = new StringBuilder();

            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                System.out.println("count : " + count);
                sb.append(new String(buffer, 0, count, StandardCharsets.UTF_8));
            }

            inflater.end();
            String resultStr = sb.toString();
            System.out.println("sb : " + resultStr);

            return sb.toString();
        } catch (Exception e) {
            log.error("Decompression failed, returning raw bytes", e);
            return "[INVALID DATA]";
        }
    }

    public static String tryDecompress2(byte[] compressedData) {
        try {
            Inflater inflater = new Inflater(true); // true 表示使用 ZLIB 头部
            inflater.setInput(compressedData);
            byte[] decompressedData = new byte[1024];
            int resultLength = inflater.inflate(decompressedData);
            inflater.end();
            return new String(decompressedData, 0, resultLength, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Failed to decompress message: ", e);
            return null;
        }
    }

}
