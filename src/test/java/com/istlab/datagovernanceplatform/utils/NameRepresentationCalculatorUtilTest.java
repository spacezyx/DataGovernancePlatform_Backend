package com.istlab.datagovernanceplatform.utils;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NameRepresentationCalculatorUtilTest {

    @Test
    void getCosineSimilarity() {
        // 准备测试标题内容数据
        List<String> titleList = new ArrayList<>();
        titleList.add("product");
        titleList.add("production");
        titleList.add("department");
        titleList.add("employee");
        titleList.add("有哪些");

        // 原始标题内容数据
        String originalTitle = "production";

        Map<String, Double> simHashMap = new HashMap<>();

        System.out.println("======================================");
        long startTime = System.currentTimeMillis();
        System.out.println("原始标题：" + originalTitle);

        NameRepresentationCalculatorUtil nameRepresentationCalculatorUtil = new NameRepresentationCalculatorUtil();
        System.out.println(nameRepresentationCalculatorUtil);

        for(String title : titleList) {
            Double similar = nameRepresentationCalculatorUtil.getCosineSimilarity(title, originalTitle);
            simHashMap.put(title, similar);
        }


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