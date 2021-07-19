package de.flxnet.pixelwall.panels;

import java.awt.Image;
import java.util.Arrays;

import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapFont;
import org.bukkit.map.MapFont.CharacterSprite;

import lombok.Getter;
import lombok.Setter;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2020 by FLXnet
 * @author Felix
 */
public class PanelMapCanvas {

	@Getter
    private final byte[] buffer;
	
	@Getter @Setter
    private byte[] base;
	
	@Getter @Setter
    private MapCursorCollection cursors;
    
    protected PanelMapCanvas() {
    	this.buffer = new byte[128 * 128];
        Arrays.fill(buffer, (byte) -1);
        this.cursors = new MapCursorCollection();
    }

    public void setPixel(int x, int y, byte color) {
        if (x < 0 || y < 0 || x >= 128 || y >= 128) return;
        if (buffer[y * 128 + x] != color) {
            buffer[y * 128 + x] = color;
        }
    }

    public byte getPixel(int x, int y) {
        if (x < 0 || y < 0 || x >= 128 || y >= 128) return 0;
        return buffer[y * 128 + x];
    }

    public byte getBasePixel(int x, int y) {
        if (x < 0 || y < 0 || x >= 128 || y >= 128) return 0;
        return base[y * 128 + x];
    }

    public void drawImage(int x, int y, Image image) {
        byte[] bytes = PanelPalette.imageToBytes(image);
        for (int x2 = 0; x2 < image.getWidth(null); ++x2) {
            for (int y2 = 0; y2 < image.getHeight(null); ++y2) {
                setPixel(x + x2, y + y2, bytes[y2 * image.getWidth(null) + x2]);
            }
        }
    }

    public void drawText(int x, int y, MapFont font, String text) {
        int xStart = x;
        byte color = PanelPalette.DARK_GRAY;
        if (!font.isValid(text)) {
            throw new IllegalArgumentException("text contains invalid characters");
        }

        for (int i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                x = xStart;
                y += font.getHeight() + 1;
                continue;
            } else if (ch == '\u00A7') {
                int j = text.indexOf(';', i);
                if (j >= 0) {
                    try {
                        color = Byte.parseByte(text.substring(i + 1, j));
                        i = j;
                        continue;
                    } catch (NumberFormatException ex) {
                    }
                }
                throw new IllegalArgumentException("Text contains unterminated color string");
            }

            CharacterSprite sprite = font.getChar(text.charAt(i));
            for (int r = 0; r < font.getHeight(); ++r) {
                for (int c = 0; c < sprite.getWidth(); ++c) {
                    if (sprite.get(r, c)) {
                        setPixel(x + c, y + r, color);
                    }
                }
            }
            x += sprite.getWidth() + 1;
        }
    }

}
