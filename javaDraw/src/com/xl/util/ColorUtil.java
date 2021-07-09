package com.xl.util;


public class ColorUtil {

    //将rgb565转rgb格式
    public static int rgb565ToColor(int rgb565) {
        int r = rgb565 >> 16 << 3;
        int g = rgb565 >> 8 << 2;
        int b = rgb565 << 3 & 0xff;
        return (r << 16) | (g << 8) | b;
    }

    //将rgb转rgb565格式
    public static int makeColor565(int r, int g, int b) {
        return ((r << 8) & 0x1f | (g << 3) & 0x3f | b >> 3);
    }

    //获取颜色灰度值
    public static int getBlack(int color) {
        int r = getR(color);
        int g = getG(color);
        int b = getB(color);
        return (r + g + b) / 3;
    }

    //在颜色中混合一层颜色(带透明度信息)
    public static int mixColor(int color1, int color2) {
        int a1 = color1 >> 24 & 0xff;
        int r1 = color1 >> 16 & 0xff;
        int g1 = color1 >> 8 & 0xff;
        int b1 = color1 & 0xff;

        int a2 = color2 >> 24 & 0xff;
        int r2 = color2 >> 16 & 0xff;
        int g2 = color2 >> 8 & 0xff;
        int b2 = color2 & 0xff;

        int draw_a = color2 >> 24;
        int a = a1 * (255 - draw_a) / 255 + a2 * draw_a / 255;
        int r = r1 * (255 - draw_a) / 255 + r2 * draw_a / 255;
        int g = g1 * (255 - draw_a) / 255 + g2 * draw_a / 255;
        int b = b1 * (255 - draw_a) / 255 + b2 * draw_a / 255;
        return (a1 << 24) | (r << 16) | (g << 8) | b;
    }

    //将两个颜色混合(比例1:1)
    public static int mergerColor(int color1, int color2) {
        int a1 = color1 >> 24 & 0xff;
        int r1 = color1 >> 16 & 0xff;
        int g1 = color1 >> 8 & 0xff;
        int b1 = color1 & 0xff;

        int a2 = color2 >> 24 & 0xff;
        int r2 = color2 >> 16 & 0xff;
        int g2 = color2 >> 8 & 0xff;
        int b2 = color2 & 0xff;

        int draw_a = 128;
        int a = a1 * (255 - draw_a) / 255 + a2 * draw_a / 255;
        int r = r1 * (255 - draw_a) / 255 + r2 * draw_a / 255;
        int g = g1 * (255 - draw_a) / 255 + g2 * draw_a / 255;
        int b = b1 * (255 - draw_a) / 255 + b2 * draw_a / 255;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
    //改变颜色亮度 参数：原始颜色，亮度值(0~255)

    //将颜色变亮 参数：原始颜色，亮度增量(0～100)
    public static int lightColor(int color, int size) {
        int a1 = color >> 24 & 0xff;
        int r1 = color >> 16 & 0xff;
        int g1 = color >> 8 & 0xff;
        int b1 = color & 0xff;

        int a = a1 + (255 - a1) * size / 100;
        int r = r1 + (255 - r1) * size / 100;
        int g = g1 + (255 - g1) * size / 100;
        int b = b1 + (255 - b1) * size / 100;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    //将颜色变暗 参数：原始颜色，亮度(0-100)
    public static int blackColor(int color, int size) {
        int a1 = color >> 24 & 0xff;
        int r1 = color >> 16 & 0xff;
        int g1 = color >> 8 & 0xff;
        int b1 = color & 0xff;

        int a = a1 * size / 100;
        int r = r1 * size / 100;
        int g = g1 * size / 100;
        int b = b1 * size / 100;
        return (a << 24) | (r << 16) | (g << 8) | b;

    }

    //将颜色值#ffffffff转int
    //读取颜色值
    public static int getColor(String text) {
        int color = 0;
        int argb[] = new int[4];
        int start = 0;
        int i = 0;
        int hex = 0; //颜色位数 有3 4 6 8
        for (i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '#') {
                start = i + 1;
                hex = text.length() - start;
            }

        }
        if (hex == 3) {
            for (i = 0; i < 3; i++) {
                char c = text.charAt(start + i);
                argb[0] = 0xff;
                if (c >= 'A' && c <= 'F') {
                    argb[i + 1] = (c - 'A' + 10) * 16;
                } else if (c >= 'a' && c <= 'f') {
                    argb[i + 1] = (c - 'a' + 10) * 16;
                } else if (c >= '0' && c <= '9') {
                    argb[i + 1] = (c - '0') * 16;
                }
            }
        } else if (hex == 6) {
            argb[0] = 0xff;
            for (i = 0; i < 3; i++) {
                char c = text.charAt(start + i * 2);
                char c2 = text.charAt(start + i * 2 + 1);

                if (c >= 'A' && c <= 'F') {
                    argb[i + 1] = (c - 'A' + 10) << 4;
                } else if (c >= 'a' && c <= 'f') {
                    argb[i + 1] = (c - 'a' + 10) << 4;
                } else if (c >= '0' && c <= '9') {
                    argb[i + 1] = (c - '0') << 4;
                }
                if (c2 >= 'A' && c2 <= 'F') {
                    argb[i + 1] |= (c2 - 'A' + 10);
                } else if (c2 >= 'a' && c2 <= 'f') {
                    argb[i + 1] |= (c2 - 'a' + 10);
                } else if (c2 >= '0' && c2 <= '9') {
                    argb[i + 1] |= (c2 - '0');
                }
            }
        } else if (hex == 4) {
            for (i = 0; i < 4; i++) {
                char c = text.charAt(start + i);
                if (c >= 'A' && c <= 'Z') {
                    argb[i] = ((c - 'A') + 10) * 16;
                } else if (c >= 'a' && c <= 'z') {
                    argb[i] = (c - 'a' + 10) * 16;
                } else if (c >= '0' && c <= '9') {
                    argb[i] = (c - '0') * 16;
                }
            }
        } else if (hex == 8) {
            for (i = 0; i < 4; i++) {
                char c = text.charAt(start + i * 2);
                char c2 = text.charAt(start + i * 2 + 1);
                if (c >= 'A' && c <= 'F') {
                    argb[i] = (c - 'A' + 10) << 4;
                } else if (c >= 'a' && c <= 'f') {
                    argb[i] = (c - 'a' + 10) << 4;
                } else if (c >= '0' && c <= '9') {
                    argb[i] = (c - '0') << 4;
                }
                if (c2 >= 'A' && c2 <= 'F') {
                    argb[i] |= (c2 - 'A' + 10);
                } else if (c2 >= 'a' && c2 <= 'f') {
                    argb[i] |= (c2 - 'a' + 10);
                } else if (c2 >= '0' && c2 <= '9') {
                    argb[i] |= (c2 - '0');
                }
            }
        }
        color = (argb[0] << 24) | (argb[1] << 16) | (argb[2] << 8) | argb[3];

        return color;
    }

    //将int转颜色值
    public static String colorToString(int color) {
        return String.format("%08x", color);
    }

    //获取颜色的alpha信息
    public static int getAlpha(int color) {
        return color >> 24 & 0xff;
    }

    //获取颜色的r值
    public static int getR(int color) {
        return color >> 16 & 0xff;
    }

    //获取颜色的g值
    public static int getG(int color) {
        return color >> 8 & 0xff;
    }

    //获取颜色的b值
    public static int getB(int color) {
        return color & 0xff;
    }

    //将argb转换成int颜色值
    public static int getColor(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    //重新设置颜色的透明度
    public static int getAlphaColor(int color, int alpha) {
        return (color & 0xffffff) | ((alpha << 24));
    }

//    //获取主题属性 例如 android.R.attr.textColorSecondary
//    public static int getColorAttr(Context context, int attr) {
//        int defaultColor = 0xFF000000;
//        int[] attrsArray = {attr};
//        TypedArray typedArray = context.obtainStyledAttributes(attrsArray);
//        int accentColor = typedArray.getColor(0, defaultColor);
//
//// don't forget the resource recycling  
//        typedArray.recycle();
//        return accentColor;
//    }
//
//
//    public static ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
//        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
//        int[][] states = new int[6][];
//        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};  //按下
//        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};  //焦点
//        states[2] = new int[]{android.R.attr.state_enabled};
//        states[3] = new int[]{android.R.attr.state_focused};
//        states[4] = new int[]{android.R.attr.state_window_focused};
//        states[5] = new int[]{};
//        ColorStateList colorList = new ColorStateList(states, colors);
//        return colorList;
//    }
//
//    public static ColorStateList createCheckColorStateList(int check, int focused, int normal) {
//        int[] colors = new int[]{check, focused, normal};
//        int[][] states = new int[2][];
//        states[0] = new int[]{android.R.attr.state_checked, android.R.attr.state_enabled};  //按下
//        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}; //焦点
//        states[1] = new int[]{};
//        ColorStateList colorList = new ColorStateList(states, colors);
//        return colorList;
//    }
}
