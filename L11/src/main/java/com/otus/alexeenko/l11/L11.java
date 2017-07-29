package com.otus.alexeenko.l11;

import com.otus.alexeenko.l11.advance2.AdvanceBubblesSorter2;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class L11 {
    static {
        org.apache.log4j.BasicConfigurator.configure();
    }

    private static final Logger MAIN_LOGGER = getLogger("Main");

    public static void main(String[] args) {
        int[] array = {0, 9, 5, 6, 7, 1, 3, 8, 2, 4};
        Sorter sorter = new AdvanceBubblesSorter2(array);

        MAIN_LOGGER.info(sorter.toString());

        sorter.sort();

        MAIN_LOGGER.info(sorter.toString());
    }
}
