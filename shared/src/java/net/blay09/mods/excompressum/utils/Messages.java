package net.blay09.mods.excompressum.utils;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nullable;

public class Messages {

    public static TranslatableComponent lang(String key, Object... args) {
        return styledLang(key, (Style) null, args);
    }

    public static TranslatableComponent styledLang(String key, @Nullable ChatFormatting formatting, Object... args) {
        return styledLang(key, formatting != null ? Style.EMPTY.applyFormat(formatting) : null, args);
    }

    public static TranslatableComponent styledLang(String key, @Nullable Style style, Object... args) {
        final TranslatableComponent text = new TranslatableComponent(ExCompressum.MOD_ID + "." + key, args);
        if (style != null) {
            text.setStyle(style);
        }
        return text;
    }

    public static String format(String key, Object... args) {
        return I18n.get(ExCompressum.MOD_ID + "." + key, args);
    }

    public static TextComponent styledString(String text, @Nullable ChatFormatting formatting) {
        return styledString(text, formatting != null ? Style.EMPTY.applyFormat(formatting) : null);
    }

    public static TextComponent styledString(String text, @Nullable Style style) {
        final TextComponent textComponent = new TextComponent(text);
        if (style != null) {
            textComponent.setStyle(style);
        }
        return textComponent;
    }
}
