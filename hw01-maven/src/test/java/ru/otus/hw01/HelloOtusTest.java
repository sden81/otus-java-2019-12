package ru.otus.hw01;

import org.junit.Test;

import static org.junit.Assert.*;

public class HelloOtusTest {

    @Test
    public void trim() {
        assertEquals("Trim error", "Hello", HelloOtus.trim("  Hello  "));
    }
}