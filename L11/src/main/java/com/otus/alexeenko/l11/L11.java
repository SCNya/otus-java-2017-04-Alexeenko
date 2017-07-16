package com.otus.alexeenko.l11;

public class L11 {

    public static void main(String[] args) {
        int array[] = {0, 9, 5, 6, 7, 1, 3, 8, 2, 4};
        view(array);

        BubblesSorter sorter = new BubblesSorter(array);

        sorter.sort();

        view(array);
    }

    private static void view(int[] array) {
        for (int i = 0; i < array.length; i++)
            System.out.print(array[i]);

        System.out.println("\n");
    }
}
