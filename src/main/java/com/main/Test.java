package com.main;

import com.classes.GenerateObjects;

public class Test {

    public static void main(String[] args) {
        GenerateObjects generateObjects = new GenerateObjects();
        generateObjects.createTestProject(args[0], args[1]);
    }
}
