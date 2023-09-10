/*
 * Copyright © Wynntils 2022-2023.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package me.kmaxi.vowcloud.text;

import com.google.gson.*;
import net.minecraft.ChatFormatting;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public class CustomColor {
    public static final CustomColor NONE = new CustomColor(-1, -1, -1, -1);

    private static final Pattern HEX_PATTERN = Pattern.compile("#?([0-9a-fA-F]{6})([0-9a-fA-F]{2})?");
    private static final Pattern STRING_PATTERN = Pattern.compile("rgba\\((\\d+),(\\d+),(\\d+),(\\d+)\\)");
    private static final Map<String, CustomColor> REGISTERED_HASHED_COLORS = new HashMap<>();

    public final int r;
    public final int g;
    public final int b;
    public final int a;


    public CustomColor(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public CustomColor(float r, float g, float b, float a) {
        this.r = (int) (r * 255);
        this.g = (int) (g * 255);
        this.b = (int) (b * 255);
        this.a = (int) (a * 255);
    }


    public CustomColor(CustomColor color, int alpha) {
        this(color.r, color.g, color.b, alpha);
    }

    public CustomColor(String toParse) {
        String noSpace = toParse.replace(" ", "");

        CustomColor parseTry = CustomColor.fromString(noSpace);

        if (parseTry == CustomColor.NONE) {
            parseTry = CustomColor.fromHexString(noSpace);

            if (parseTry == CustomColor.NONE) {
                throw new RuntimeException("Failed to parse CustomColor");
            }
        }

        this.r = parseTry.r;
        this.g = parseTry.g;
        this.b = parseTry.b;
        this.a = parseTry.a;
    }

    /** 0xAARRGGBB format */
    public static CustomColor fromInt(int num) {
        return new CustomColor(num >> 16 & 255, num >> 8 & 255, num & 255, num >> 24 & 255);
    }



    /** "#rrggbb(aa)" or "rrggbb(aa)" */
    public static CustomColor fromHexString(String hex) {
        Matcher hexMatcher = HEX_PATTERN.matcher(hex.trim());

        // invalid format
        if (!hexMatcher.matches()) return CustomColor.NONE;

        // parse hex
        if (hexMatcher.group(2) == null) {
            return fromInt(Integer.parseInt(hexMatcher.group(1), 16)).withAlpha(255);
        } else {
            return fromInt(Integer.parseInt(hexMatcher.group(1), 16))
                    .withAlpha(Integer.parseInt(hexMatcher.group(2), 16));
        }
    }

    /** "rgba(r,g,b,a)" format as defined in toString() */
    public static CustomColor fromString(String string) {
        Matcher stringMatcher = STRING_PATTERN.matcher(string.trim());

        // invalid format
        if (!stringMatcher.matches()) return CustomColor.NONE;

        return new CustomColor(
                Integer.parseInt(stringMatcher.group(1)),
                Integer.parseInt(stringMatcher.group(2)),
                Integer.parseInt(stringMatcher.group(3)),
                Integer.parseInt(stringMatcher.group(4)));
    }


    public CustomColor withAlpha(int a) {
        return new CustomColor(this, a);
    }


    /** 0xAARRGGBB format */
    public int asInt() {
        int a = Math.min(this.a, 255);
        int r = Math.min(this.r, 255);
        int g = Math.min(this.g, 255);
        int b = Math.min(this.b, 255);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /** #rrggbb(aa) format */
    public String toHexString() {
        String colorHex = String.format("%06x", (0xFFFFFF & (r << 16) | (g << 8) | b));

        // Only append alpha if it's not 255
        if (a != 255) {
            String alphaHex = String.format("%02x", (0xFF & a));
            colorHex += alphaHex;
        }

        colorHex = "#" + colorHex;

        return colorHex;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomColor color)) return false;

        // colors are equal as long as rgba values match
        return (this.r == color.r && this.g == color.g && this.b == color.b && this.a == color.a);
    }

    @Override
    public String toString() {
        return toHexString();
    }

}
