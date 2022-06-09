package ru.geekbrains.january_chat.chat_server.Lesson6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Lesson6Test {


    @Test
    void runtimeException() {
        Assertions.assertThrows(RuntimeException.class, () -> Lesson6.testArrayOperation(new int[]{1, 2, 3, 0, 5, 6, 7, 8, 9, 10}));
        Assertions.assertThrows(RuntimeException.class, () -> Lesson6.testArrayOperation(new int[]{1, 2, 3, 3, 5, 6, 7, 8, 9, 10}));
        Assertions.assertThrows(RuntimeException.class, () -> Lesson6.testArrayOperation(new int[]{1, 2, 3, 3, 5, 6, 7, 8, 9, 10}));
        Assertions.assertThrows(RuntimeException.class, () -> Lesson6.testArrayOperation(new int[]{1, 2, 3, 3, 5, 6, 7, 8, 9, 0}));
    }




    @Test
    void numberTest() {
        Assertions.assertTrue(Lesson6.testArray(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        Assertions.assertTrue(Lesson6.testArray(new int[]{1, 2, 3, 3, 5, 6, 7, 8, 9, 10}));
        Assertions.assertTrue(Lesson6.testArray(new int[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        Assertions.assertTrue(Lesson6.testArray(new int[]{2, 2, 3, 4, 5, 6, 7, 8, 9, 1}));

    }


}