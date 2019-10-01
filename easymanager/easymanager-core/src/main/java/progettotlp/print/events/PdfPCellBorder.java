package progettotlp.print.events;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfPCellBorder implements PdfPCellEvent {

	@Override
	public void cellLayout(PdfPCell cell, Rectangle position,
			PdfContentByte[] canvases) {
		float x1 = position.getLeft() + 2;
		float x2 = position.getRight() - 2;
		float y1 = position.getTop() - 2;
		float y2 = position.getBottom() + 2;
		PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
//		canvas.rectangle(x1, y1, x2 - x1, y2 - y1);
		canvas.roundRectangle(x1, y1, x2 - x1, y2 - y1, 4);
		canvas.setLineWidth(0.5F);
		canvas.stroke();
		canvas.resetRGBColorStroke();
	}

}
