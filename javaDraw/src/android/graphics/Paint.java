package android.graphics;

import java.awt.Color;
import java.awt.Font;

/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import android.graphics.Paint.Style;

/**
 * The Paint class holds the style and color information about how to draw
 * geometries, text and bitmaps.
 */
public class Paint {



	Color mColor;
	boolean mAntiAlias;
	float mTextSize;
	Style mStyle;
	float mStrokeWidth;
	float mStrokeMiter;
	Align mTextAlign;
	Font mFont;
	
	
	

    private static final Object sCacheLock = new Object();

    /**
     * Cache for the Minikin language list ID.
     *
     * A map from a string representation of the LocaleList to Minikin's language list ID.
     */
 
    private static final HashMap<String, Integer> sMinikinLocaleListIdCache = new HashMap<String, Integer>();

    /**
     * @hide
     */
    public  int         mBidiFlags = BIDI_DEFAULT_LTR;

    static final Style[] sStyleArray = {
        Style.FILL, Style.STROKE, Style.FILL_AND_STROKE
    };
    static final Cap[] sCapArray = {
        Cap.BUTT, Cap.ROUND, Cap.SQUARE
    };
    static final Join[] sJoinArray = {
        Join.MITER, Join.ROUND, Join.BEVEL
    };
    static final Align[] sAlignArray = {
        Align.LEFT, Align.CENTER, Align.RIGHT
    };

    /**
     * Paint flag that enables antialiasing when drawing.
     *
     * <p>Enabling this flag will cause all draw operations that support
     * antialiasing to use it.</p>
     *
     * @see #Paint(int)
     * @see #setFlags(int)
     */
    public static final int ANTI_ALIAS_FLAG     = 0x01;
    /**
     * Paint flag that enables bilinear sampling on scaled bitmaps.
     *
     * <p>If cleared, scaled bitmaps will be drawn with nearest neighbor
     * sampling, likely resulting in artifacts. This should generally be on
     * when drawing bitmaps, unless performance-bound (rendering to software
     * canvas) or preferring pixelation artifacts to blurriness when scaling
     * significantly.</p>
     *
     * <p>If bitmaps are scaled for device density at creation time (as
     * resource bitmaps often are) the filtering will already have been
     * done.</p>
     *
     * @see #Paint(int)
     * @see #setFlags(int)
     */
    public static final int FILTER_BITMAP_FLAG  = 0x02;
    /**
     * Paint flag that enables dithering when blitting.
     *
     * <p>Enabling this flag applies a dither to any blit operation where the
     * target's colour space is more constrained than the source.
     *
     * @see #Paint(int)
     * @see #setFlags(int)
     */
    public static final int DITHER_FLAG         = 0x04;
    /**
     * Paint flag that applies an underline decoration to drawn text.
     *
     * @see #Paint(int)
     * @see #setFlags(int)
     */
    public static final int UNDERLINE_TEXT_FLAG = 0x08;
    /**
     * Paint flag that applies a strike-through decoration to drawn text.
     *
     * @see #Paint(int)
     * @see #setFlags(int)
     */
    public static final int STRIKE_THRU_TEXT_FLAG = 0x10;
    /**
     * Paint flag that applies a synthetic bolding effect to drawn text.
     *
     * <p>Enabling this flag will cause text draw operations to apply a
     * simulated bold effect when drawing a {@link Typeface} that is not
     * already bold.</p>
     *
     * @see #Paint(int)
     * @see #setFlags(int)
     */
    public static final int FAKE_BOLD_TEXT_FLAG = 0x20;
    /**
     * Paint flag that enables smooth linear scaling of text.
     *
     * <p>Enabling this flag does not actually scale text, but rather adjusts
     * text draw operations to deal gracefully with smooth adjustment of scale.
     * When this flag is enabled, font hinting is disabled to prevent shape
     * deformation between scale factors, and glyph caching is disabled due to
     * the large number of glyph images that will be generated.</p>
     *
     * <p>{@link #SUBPIXEL_TEXT_FLAG} should be used in conjunction with this
     * flag to prevent glyph positions from snapping to whole pixel values as
     * scale factor is adjusted.</p>
     *
     * @see #Paint(int)
     * @see #setFlags(int)
     */
    public static final int LINEAR_TEXT_FLAG    = 0x40;
    /**
     * Paint flag that enables subpixel positioning of text.
     *
     * <p>Enabling this flag causes glyph advances to be computed with subpixel
     * accuracy.</p>
     *
     * <p>This can be used with {@link #LINEAR_TEXT_FLAG} to prevent text from
     * jittering during smooth scale transitions.</p>
     *
     * @see #Paint(int)
     * @see #setFlags(int)
     */
    public static final int SUBPIXEL_TEXT_FLAG  = 0x80;
    /** Legacy Paint flag, no longer used. */
    public static final int DEV_KERN_TEXT_FLAG  = 0x100;
    /** @hide bit mask for the flag enabling subpixel glyph rendering for text */
    public static final int LCD_RENDER_TEXT_FLAG = 0x200;
    /**
     * Paint flag that enables the use of bitmap fonts when drawing text.
     *
     * <p>Disabling this flag will prevent text draw operations from using
     * embedded bitmap strikes in fonts, causing fonts with both scalable
     * outlines and bitmap strikes to draw only the scalable outlines, and
     * fonts with only bitmap strikes to not draw at all.</p>
     *
     * @see #Paint(int)
     * @see #setFlags(int)
     */
    public static final int EMBEDDED_BITMAP_TEXT_FLAG = 0x400;
    /** @hide bit mask for the flag forcing freetype's autohinter on for text */
    public static final int AUTO_HINTING_TEXT_FLAG = 0x800;
    /** @hide bit mask for the flag enabling vertical rendering for text */
    public static final int VERTICAL_TEXT_FLAG = 0x1000;

    // These flags are always set on a new/reset paint, even if flags 0 is passed.
    static final int HIDDEN_DEFAULT_PAINT_FLAGS = DEV_KERN_TEXT_FLAG | EMBEDDED_BITMAP_TEXT_FLAG;

    /**
     * Font hinter option that disables font hinting.
     *
     * @see #setHinting(int)
     */
    public static final int HINTING_OFF = 0x0;

    /**
     * Font hinter option that enables font hinting.
     *
     * @see #setHinting(int)
     */
    public static final int HINTING_ON = 0x1;

    /**
     * Bidi flag to set LTR paragraph direction.
     *
     * @hide
     */
    public static final int BIDI_LTR = 0x0;

    /**
     * Bidi flag to set RTL paragraph direction.
     *
     * @hide
     */
    public static final int BIDI_RTL = 0x1;

    /**
     * Bidi flag to detect paragraph direction via heuristics, defaulting to
     * LTR.
     *
     * @hide
     */
    public static final int BIDI_DEFAULT_LTR = 0x2;

    /**
     * Bidi flag to detect paragraph direction via heuristics, defaulting to
     * RTL.
     *
     * @hide
     */
    public static final int BIDI_DEFAULT_RTL = 0x3;

    /**
     * Bidi flag to override direction to all LTR (ignore bidi).
     *
     * @hide
     */
    public static final int BIDI_FORCE_LTR = 0x4;

    /**
     * Bidi flag to override direction to all RTL (ignore bidi).
     *
     * @hide
     */
    public static final int BIDI_FORCE_RTL = 0x5;

    /**
     * Maximum Bidi flag value.
     * @hide
     */
    private static final int BIDI_MAX_FLAG_VALUE = BIDI_FORCE_RTL;

    /**
     * Mask for bidi flags.
     * @hide
     */
    private static final int BIDI_FLAG_MASK = 0x7;

    /**
     * Flag for getTextRunAdvances indicating left-to-right run direction.
     * @hide
     */
    public static final int DIRECTION_LTR = 0;

    /**
     * Flag for getTextRunAdvances indicating right-to-left run direction.
     * @hide
     */
    public static final int DIRECTION_RTL = 1;

    /**
     * Option for getTextRunCursor to compute the valid cursor after
     * offset or the limit of the context, whichever is less.
     * @hide
     */
    public static final int CURSOR_AFTER = 0;

    /**
     * Option for getTextRunCursor to compute the valid cursor at or after
     * the offset or the limit of the context, whichever is less.
     * @hide
     */
    public static final int CURSOR_AT_OR_AFTER = 1;

     /**
     * Option for getTextRunCursor to compute the valid cursor before
     * offset or the start of the context, whichever is greater.
     * @hide
     */
    public static final int CURSOR_BEFORE = 2;

   /**
     * Option for getTextRunCursor to compute the valid cursor at or before
     * offset or the start of the context, whichever is greater.
     * @hide
     */
    public static final int CURSOR_AT_OR_BEFORE = 3;

    /**
     * Option for getTextRunCursor to return offset if the cursor at offset
     * is valid, or -1 if it isn't.
     * @hide
     */
    public static final int CURSOR_AT = 4;

    /**
     * Maximum cursor option value.
     */
    private static final int CURSOR_OPT_MAX_VALUE = CURSOR_AT;

    /**
     * Mask for hyphen edits that happen at the end of a line. Keep in sync with the definition in
     * Minikin's Hyphenator.h.
     * @hide
     */
    public static final int HYPHENEDIT_MASK_END_OF_LINE = 0x07;

    /**
     * Mask for hyphen edits that happen at the start of a line. Keep in sync with the definition in
     * Minikin's Hyphenator.h.
     * @hide
     */
    public static final int HYPHENEDIT_MASK_START_OF_LINE = 0x03 << 3;

    /**
     * The Style specifies if the primitive being drawn is filled, stroked, or
     * both (in the same color). The default is FILL.
     */
    public enum Style {
        /**
         * Geometry and text drawn with this style will be filled, ignoring all
         * stroke-related settings in the paint.
         */
        FILL            (0),
        /**
         * Geometry and text drawn with this style will be stroked, respecting
         * the stroke-related fields on the paint.
         */
        STROKE          (1),
        /**
         * Geometry and text drawn with this style will be both filled and
         * stroked at the same time, respecting the stroke-related fields on
         * the paint. This mode can give unexpected results if the geometry
         * is oriented counter-clockwise. This restriction does not apply to
         * either FILL or STROKE.
         */
        FILL_AND_STROKE (2);

        Style(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }

    /**
     * The Cap specifies the treatment for the beginning and ending of
     * stroked lines and paths. The default is BUTT.
     */
    public enum Cap {
        /**
         * The stroke ends with the path, and does not project beyond it.
         */
        BUTT    (0),
        /**
         * The stroke projects out as a semicircle, with the center at the
         * end of the path.
         */
        ROUND   (1),
        /**
         * The stroke projects out as a square, with the center at the end
         * of the path.
         */
        SQUARE  (2);

        private Cap(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }

    /**
     * The Join specifies the treatment where lines and curve segments
     * join on a stroked path. The default is MITER.
     */
    public enum Join {
        /**
         * The outer edges of a join meet at a sharp angle
         */
        MITER   (0),
        /**
         * The outer edges of a join meet in a circular arc.
         */
        ROUND   (1),
        /**
         * The outer edges of a join meet with a straight line
         */
        BEVEL   (2);

        private Join(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }

    /**
     * Align specifies how drawText aligns its text relative to the
     * [x,y] coordinates. The default is LEFT.
     */
    public enum Align {
        /**
         * The text is drawn to the right of the x,y origin
         */
        LEFT    (0),
        /**
         * The text is drawn centered horizontally on the x,y origin
         */
        CENTER  (1),
        /**
         * The text is drawn to the left of the x,y origin
         */
        RIGHT   (2);

        private Align(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }

    /**
     * Create a new paint with default settings.
     */
    public Paint() {
        this.mStyle = Style.FILL;
        this.mTextAlign = Align.LEFT;
        this.mStrokeWidth = 1;
        this.mTextSize = 14;
        this.mColor = Color.BLACK;
    }

    /**
     * Create a new paint with the specified flags. Use setFlags() to change
     * these after the paint is created.
     *
     * @param flags initial flag bits, as if they were passed via setFlags().
     */
//    public Paint(int flags) {
//       
//    
//       
//    }

    /**
     * Create a new paint, initialized with the attributes in the specified
     * paint parameter.
     *
     * @param paint Existing paint used to initialized the attributes of the
     *              new paint.
     */
    public Paint(Paint paint) {
       
        
        
    }

    /** Restores the paint to its default settings. */
    public void reset() {
        
    	this.mTextSize = 12;
        this.mAntiAlias = false;
        this.mColor = new Color(0);
        this.mStyle = Style.FILL;
    }

    /**
     * Copy the fields from src into this paint. This is equivalent to calling
     * get() on all of the src fields, and calling the corresponding set()
     * methods on this.
     */
    public void set(Paint src) {
    	this.mTextSize = src.mTextSize;
        this.mAntiAlias = src.mAntiAlias;
        this.mColor = src.mColor;
        this.mStyle = src.mStyle;
    }




    /**
     * Return the paint's color. Note that the color is a 32bit value
     * containing alpha as well as r,g,b. This 32bit value is not premultiplied,
     * meaning that its alpha can be any value, regardless of the values of
     * r,g,b. See the Color class for more details.
     *
     * @return the paint's color (and alpha).
     */
    
    public int getColor() {
    	int color = (mColor.getAlpha()<<24) | (mColor.getRed()<<16) | (mColor.getGreen()<<8) | mColor.getBlue();
        return color;
    }

    /**
     * Set the paint's color. Note that the color is an int containing alpha
     * as well as r,g,b. This 32bit value is not premultiplied, meaning that
     * its alpha can be any value, regardless of the values of r,g,b.
     * See the Color class for more details.
     *
     * @param color The new color (including alpha) to set in the paint.
     */
    public void setColor( int color) {
        mColor = new Color((color>>16)&0xff, (color>>8)&0xff, ((color))&0xff, (color>>24)&0xff);
    }

    /**
     * Helper to getColor() that just returns the color's alpha value. This is
     * the same as calling getColor() >>> 24. It always returns a value between
     * 0 (completely transparent) and 255 (completely opaque).
     *
     * @return the alpha component of the paint's color.
     */
    public int getAlpha() {
        return mColor.getAlpha();
    }

    /**
     * Helper to setColor(), that only assigns the color's alpha value,
     * leaving its r,g,b values unchanged. Results are undefined if the alpha
     * value is outside of the range [0..255]
     *
     * @param a set the alpha component [0..255] of the paint's color.
     */
    public void setAlpha(int a) {
        this.mColor = new Color(mColor.getRed(), mColor.getGreen(), mColor.getBlue(), a);
    }

    /**
     * Helper to setColor(), that takes a,r,g,b and constructs the color int
     *
     * @param a The new alpha component (0..255) of the paint's color.
     * @param r The new red component (0..255) of the paint's color.
     * @param g The new green component (0..255) of the paint's color.
     * @param b The new blue component (0..255) of the paint's color.
     */
    public void setARGB(int a, int r, int g, int b) {
        setColor((a << 24) | (r << 16) | (g << 8) | b);
    }

    /**
     * Return the width for stroking.
     * <p />
     * A value of 0 strokes in hairline mode.
     * Hairlines always draws a single pixel independent of the canva's matrix.
     *
     * @return the paint's stroke width, used whenever the paint's style is
     *         Stroke or StrokeAndFill.
     */
    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    /**
     * Set the width for stroking.
     * Pass 0 to stroke in hairline mode.
     * Hairlines always draws a single pixel independent of the canva's matrix.
     *
     * @param width set the paint's stroke width, used whenever the paint's
     *              style is Stroke or StrokeAndFill.
     */
    public void setStrokeWidth(float width) {
        this.mStrokeWidth = width;
        
    }

    /**
     * Return the paint's stroke miter value. Used to control the behavior
     * of miter joins when the joins angle is sharp.
     *
     * @return the paint's miter limit, used whenever the paint's style is
     *         Stroke or StrokeAndFill.
     */
    public float getStrokeMiter() {
        return this.mStrokeMiter;
    }

    /**
     * Set the paint's stroke miter value. This is used to control the behavior
     * of miter joins when the joins angle is sharp. This value must be >= 0.
     *
     * @param miter set the miter limit on the paint, used whenever the paint's
     *              style is Stroke or StrokeAndFill.
     */
    public void setStrokeMiter(float miter) {
        this.mStrokeMiter = miter;
    }






    /**
     * Return the paint's Align value for drawing text. This controls how the
     * text is positioned relative to its origin. LEFT align means that all of
     * the text will be drawn to the right of its origin (i.e. the origin
     * specifieds the LEFT edge of the text) and so on.
     *
     * @return the paint's Align value for drawing text.
     */
    public Align getTextAlign() {
        return this.mTextAlign;
    }

    /**
     * Set the paint's text alignment. This controls how the
     * text is positioned relative to its origin. LEFT align means that all of
     * the text will be drawn to the right of its origin (i.e. the origin
     * specifieds the LEFT edge of the text) and so on.
     *
     * @param align set the paint's Align value for drawing text.
     */
    public void setTextAlign(Align align) {
        this.mTextAlign = align;
    }











    /**
     * Return the paint's text size.
     *
     * @return the paint's text size in pixel units.
     */
    public float getTextSize() {
        return this.mTextSize;
    }

    /**
     * Set the paint's text size. This value must be > 0
     *
     * @param textSize set the paint's text size in pixel units.
     */
    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
        if(mFont!=null){
        	this.mFont = new Font(mFont.getName(), mFont.getStyle(), (int)textSize);
        }
        else
        this.mFont = new Font("", Font.PLAIN, (int)textSize);
    }


 










    /**
     * Class that describes the various metrics for a font at a given text size.
     * Remember, Y values increase going down, so those values will be positive,
     * and values that measure distances going up will be negative. This class
     * is returned by getFontMetrics().
     */
    public static class FontMetrics {
        /**
         * The maximum distance above the baseline for the tallest glyph in
         * the font at a given text size.
         */
        public float   top;
        /**
         * The recommended distance above the baseline for singled spaced text.
         */
        public float   ascent;
        /**
         * The recommended distance below the baseline for singled spaced text.
         */
        public float   descent;
        /**
         * The maximum distance below the baseline for the lowest glyph in
         * the font at a given text size.
         */
        public float   bottom;
        /**
         * The recommended additional space to add between lines of text.
         */
        public float   leading;
    }




    /**
     * Convenience method for callers that want to have FontMetrics values as
     * integers.
     */
    public static class FontMetricsInt {
        /**
         * The maximum distance above the baseline for the tallest glyph in
         * the font at a given text size.
         */
        public int   top;
        /**
         * The recommended distance above the baseline for singled spaced text.
         */
        public int   ascent;
        /**
         * The recommended distance below the baseline for singled spaced text.
         */
        public int   descent;
        /**
         * The maximum distance below the baseline for the lowest glyph in
         * the font at a given text size.
         */
        public int   bottom;
        /**
         * The recommended additional space to add between lines of text.
         */
        public int   leading;

        @Override public String toString() {
            return "FontMetricsInt: top=" + top + " ascent=" + ascent +
                    " descent=" + descent + " bottom=" + bottom +
                    " leading=" + leading;
        }
    }




    /**
     * Return the width of the text.
     *
     * @param text  The text to measure. Cannot be null.
     * @param index The index of the first character to start measuring
     * @param count THe number of characters to measure, beginning with start
     * @return      The width of the text
     */
    public float measureText(char[] text, int index, int count) {
        return 0;
    }

    /**
     * Return the width of the text.
     *
     * @param text  The text to measure. Cannot be null.
     * @param start The index of the first character to start measuring
     * @param end   1 beyond the index of the last character to measure
     * @return      The width of the text
     */
    public float measureText(String text, int start, int end) {
        return 0;
    }

    /**
     * Return the width of the text.
     *
     * @param text  The text to measure. Cannot be null.
     * @return      The width of the text
     */
    public float measureText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        return measureText(text, 0, text.length());
    }





    /**
     * Return the advance widths for the characters in the string.
     *
     * @param text     The text to measure. Cannot be null.
     * @param index    The index of the first char to to measure
     * @param count    The number of chars starting with index to measure
     * @param widths   array to receive the advance widths of the characters.
     *                 Must be at least a large as count.
     * @return         the actual number of widths returned.
     */
    public int getTextWidths(char[] text, int index, int count,
                             float[] widths) {
        
        return count;
    }


    /**
     * Return the advance widths for the characters in the string.
     *
     * @param text   The text to measure. Cannot be null.
     * @param start  The index of the first char to to measure
     * @param end    The end of the text slice to measure
     * @param widths array to receive the advance widths of the characters.
     *               Must be at least a large as the text.
     * @return       the number of code units in the specified text.
     */
    public int getTextWidths(String text, int start, int end, float[] widths) {
       
        return end - start;
    }

    /**
     * Return the advance widths for the characters in the string.
     *
     * @param text   The text to measure
     * @param widths array to receive the advance widths of the characters.
     *               Must be at least a large as the text.
     * @return       the number of code units in the specified text.
     */
    public int getTextWidths(String text, float[] widths) {
        return getTextWidths(text, 0, text.length(), widths);
    }

   
    /**
     * Set or clear the typeface object.
     * <p />
     * Pass null to clear any previous typeface.
     * As a convenience, the parameter passed is also returned.
     *
     * @param typeface May be null. The typeface to be installed in the paint
     * @return         typeface
     */
    public Typeface setTypeface(Typeface typeface) {
        this.mFont = typeface.mFont;
        return typeface;
    }

	public void setAntiAlias(boolean b) {
		this.mAntiAlias = b;
		
	}
	
	public boolean isAntiAlias(){
		return this.mAntiAlias;
	}

	public void setStyle(Style stroke) {
		
		this.mStyle = stroke;
	}




















}
