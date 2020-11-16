package com.codecool.dungeoncrawl.logic.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum TreeType {
    VERTICAL_FULL,
    VERTICAL_HALF,
    VERTICAL_TOP_ONLY;

    public static TreeType getRandomTreeType(){
        List<TreeType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        int SIZE = VALUES.size();
        Random random = new Random();
        return VALUES.get(random.nextInt(SIZE));
    }
}
