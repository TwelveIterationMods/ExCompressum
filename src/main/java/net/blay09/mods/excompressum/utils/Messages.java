package net.blay09.mods.excompressum.utils;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class Messages {

    public static TranslationTextComponent lang(String key, Object... args) {
        return styledLang(key, (Style) null, args);
    }

    public static TranslationTextComponent styledLang(String key, @Nullable TextFormatting formatting, Object... args) {
        return styledLang(key, formatting != null ? Style.EMPTY.applyFormatting(formatting) : null, args);
    }

    public static TranslationTextComponent styledLang(String key, @Nullable Style style, Object... args) {
        final TranslationTextComponent text = new TranslationTextComponent(ExCompressum.MOD_ID + "." + key, args);
        if (style != null) {
            text.setStyle(style);
        }
        return text;
    }

    public static String format(String key, Object... args) {
        return I18n.format(ExCompressum.MOD_ID + "." + key, args);
    }

    public static StringTextComponent styledString(String text, @Nullable TextFormatting formatting) {
        return styledString(text, formatting != null ? Style.EMPTY.applyFormatting(formatting) : null);
    }

    public static StringTextComponent styledString(String text, @Nullable Style style) {
        final StringTextComponent textComponent = new StringTextComponent(text);
        if (style != null) {
            textComponent.setStyle(style);
        }
        return textComponent;
    }
}
