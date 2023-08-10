package com.istlab.datagovernanceplatform.utils;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SimHashUtilTest {

    @Test
    void getSimilar() {
        // 准备测试标题内容数据
        List<String> titleList = new ArrayList<>();
        titleList.add("product");
        titleList.add("production");
        titleList.add("department");
        titleList.add("employee");
        titleList.add("employeeproject");

        // 原始标题内容数据
        String originalTitle = "employee";

        Map<String, Double> simHashMap = new HashMap<>(16, 0.75F);

        System.out.println("======================================");
        long startTime = System.currentTimeMillis();
        System.out.println("原始标题：" + originalTitle);

        // 计算相似度
        titleList.forEach(title -> {
            SimHashUtil mySimHash_1 = new SimHashUtil(title, 64);
            SimHashUtil mySimHash_2 = new SimHashUtil(originalTitle, 64);

            Double similar = mySimHash_1.getSimilar(mySimHash_2);

            simHashMap.put(title, similar);
        });


        // 按相标题内容排序输出控制台
        Set<String> titleSet = simHashMap.keySet();
        Object[] titleArrays = titleSet.toArray();
        Arrays.sort(titleArrays, Collections.reverseOrder());

        System.out.println("-------------------------------------");
        for (Object title : titleArrays) {
            System.out.println("标题：" + title + "-----------相似度：" + simHashMap.get(title));
        }

        // 求得运算时长（单位：毫秒）
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("\n本次运算总耗时" + totalTime + "毫秒");

        System.out.println("======================================");
    }
}