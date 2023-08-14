package com.istlab.datagovernanceplatform.utils;

import java.util.Random;

public class MacaronColorGenerator {

    private static final String[] COLORS = {
            "#FFC3A0", "#AEEEEE", "#98FF98", "#DC8DDA", "#FFD700",
            "#C3FF68", "#E6A8D7", "#E0B0FF", "#FFFF99", "#BDFCC9",
            "#FFB6C1", "#00FFFF", "#FF4500", "#9370DB", "#00FF00",
            "#FF1493", "#ADFF2F", "#8A2BE2", "#7FFF00", "#20B2AA"
    };

    private static final Random random = new Random();

    public static String generateRandomColor() {
        int index = random.nextInt(COLORS.length);
        return COLORS[index];
    }
}
