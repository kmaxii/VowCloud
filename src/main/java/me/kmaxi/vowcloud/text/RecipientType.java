/*
 * Copyright © Wynntils 2022-2023.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package me.kmaxi.vowcloud.text;

import java.util.regex.Pattern;

public enum RecipientType {
    INFO(null, "Info"),
    // https://regexr.com/7b14s
    NPC("^§7\\[\\d+\\/\\d+\\](?:§.)? ?§[25] ?.+: ?§..*$", "NPC");

    private final Pattern foregroundPattern;

    RecipientType(String foregroundPattern, String name) {
        this.foregroundPattern = (foregroundPattern == null ? null : Pattern.compile(foregroundPattern));

    }

    public boolean matchPattern(StyledText msg) {
        Pattern pattern = foregroundPattern;
        if (pattern == null) return false;
        return msg.getMatcher(pattern).find();
    }

}
