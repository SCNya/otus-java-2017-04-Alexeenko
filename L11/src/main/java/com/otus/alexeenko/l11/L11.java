package com.otus.alexeenko.l11;

import com.otus.alexeenko.l11.advance.AdvanceBubblesSorter;

public class L11 {

    public static void main(String[] args) {
        int[] array = {0, 9, 5, 6, 7, 1, 3, 8, 2, 4};
        view(array);

        BubbleSorter sorter = new AdvanceBubblesSorter(array);

        sorter.sort();
        view(array);
    }

    private static void view(int[] array) {
        for (int element : array)
            System.out.print(element + " ");

        System.out.println("\n");
    }
}
