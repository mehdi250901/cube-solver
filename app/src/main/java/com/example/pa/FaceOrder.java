package com.example.pa;

public class FaceOrder {
    public static final String[][] FACE_ORDER = {
            {"U", "White -> Up"},    // Up
            {"R", "Orange -> Right"}, // Right
            {"F", "Green -> Front"}, // Front
            {"D", "Yellow -> Down"},   // Down
            {"L", "Red -> Left"},// Left
            {"B", "Blue -> Back"}     // Back
    };

    public static String getPhraseForStep(int step) {
        return FACE_ORDER[step - 1][1];
    }

    public static String getFaceCodeForStep(int step) {
        return FACE_ORDER[step - 1][0];
    }
}
