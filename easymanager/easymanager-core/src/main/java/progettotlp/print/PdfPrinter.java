package progettotlp.print;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

public class PdfPrinter {

	private static final Font HEADER_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 14.0F, 1, BaseColor.BLACK);
	private static final Font NORMAL_FONT = new Font(Font.FontFamily.COURIER, 9.0F, -1, BaseColor.BLACK);
	private static final Font NORMAL_BOLD_FONT = new Font(Font.FontFamily.COURIER, 9.0F, 1, BaseColor.BLACK);
	private static final Font SMALL_FONT = new Font(Font.FontFamily.COURIER, 7.0F, -1, BaseColor.BLACK);

	protected static Font createHeaderBoldFont() {
        return HEADER_FONT;
    }

    protected static Font createNormalFont() {
        return NORMAL_FONT;
    }

    protected static Font createNormalBoldFont() {
        return NORMAL_BOLD_FONT;
    }

    protected static Font createSmallBoldFont() {
        return NORMAL_BOLD_FONT;
    }

    protected static Font createSmallFont() {
        return SMALL_FONT;
    }
    
}
