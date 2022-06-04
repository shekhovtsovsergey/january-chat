package ru.geekbrains.january_chat.chat_server.Lesson6;

import java.util.Arrays;

public class Lesson6 {

    public static void main(String[] args) {

        System.out.println(Arrays.toString(testArrayOperation(new int[]{1, 2, 4, 2, 5, 8, 8, 6, 7, 5})));
        System.out.println(testArray(new int[] {1,1,1,1,4,4,4,4,1,4,4}));

    }



    public static int[] testArrayOperation(int[] arr) {
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 4) {
                return Arrays.copyOfRange(arr, i + 1, arr.length);
            }
        }
        throw new RuntimeException("There is no four in the array");
    }



    public static boolean testArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 4 || array[i] == 1) {
                System.out.println("true");
                return true;
            }
        }
        System.out.println("false");
        return false;
    }





}