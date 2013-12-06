package com.socialdonut.spriteworld.gfx;

public class Font {

    private static String chars = "" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ      "
    		+ "abcdefghijklmnopqrstuvqxyz      "
    		+ "1234567890~`@#$%^&*()_-+=!?.,/:;";

    public static void render(String msg, Screen screen, int x, int y, int scale) {
        for (int i = 0; i < msg.length(); i++) {
            int charIndex = chars.indexOf(msg.charAt(i));
            if (charIndex >= 0)
                screen.render(x + (i * 16), y, charIndex + 29 * 32, 0x00, scale);
        }
    }
}
