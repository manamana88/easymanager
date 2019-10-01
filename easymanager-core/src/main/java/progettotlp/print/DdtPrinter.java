package progettotlp.print;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import progettotlp.classes.Bene;
import progettotlp.exceptions.PrintException;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.ConfigurationManager.Property;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.facilities.DateUtils;
import progettotlp.print.events.PdfPCellBorder;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class DdtPrinter extends PdfPrinter {
	
	public static File printPage(AziendaInterface principale, DdTInterface ddt, boolean tempFile)
			throws PrintException {
		return printPage(principale, ddt, tempFile, false);
	}
	
	protected static File printPage(AziendaInterface principale, DdTInterface ddt, boolean tempFile, boolean deleteOnExit)
			throws PrintException {
		try {
			String folder = ConfigurationManager.getProperty(Property.DDT_FOLDER_PATH);
			File file;
			if (folder == null || tempFile){
				file = File.createTempFile("emem", ".pdf");
				if (deleteOnExit){
					file.deleteOnExit();
				}
			} else {
				Date data = ddt.getData();
				String directoryPath = folder+File.separator+DateUtils.getYear(data)+File.separator+DateUtils.getMonthString(data);
				File directory=new File(directoryPath);
                directory.mkdirs();
                String filepath=directoryPath+File.separator+ddt.getId()+" - "+ddt.getCliente().getNome()+".pdf";
                file=new File(filepath);
			}
			if ((!(file.exists())) && (!(file.createNewFile()))) {
				throw new PrintException("Impossibile creare il file");
			}
			FileOutputStream outputStream = new FileOutputStream(file);
			Document document = new Document(PageSize.A4);
			PdfWriter.getInstance(document, outputStream);
			
			document.addAuthor("C.R.Taglio");
			document.addCreator("EasyManager");
			document.addSubject("Ddt");
			document.addCreationDate();
			document.addTitle("Ddt numero "+ddt.getId()+" del "+DateUtils.formatDate(ddt.getData()));
			document.setMargins(15.0F, 15.0F, 15.0F, 15.0F);
			document.open();
			
			addFirstLine(principale, ddt, document);
			addSecondLine(ddt, document);
			addThirdLine(ddt, document);
			addMainTable(ddt, document);
			addFourthLine(ddt, document);
			addFifthLine(ddt, document);
			addSixthLine(ddt, document);
			addFooter(document);
			
			document.close();
			outputStream.flush();
			outputStream.close();
			
			return file;
		} catch (Exception ex) {
			throw new PrintException("Impossibile stampare la fattura", ex);
		}
	}

	private static void addFooter(Document document) throws DocumentException {
		document.add(new Paragraph("(1) Ai sensi dell'art 3, comma 2, del D.P.R. 441/97 e successive modificazioni",createNormalFont()));
		document.add(new Paragraph("PR=Prototipo, CP=Campionario, PC=Primo Capo, PZ=Piazzato, IA=Interamente Adesivato",createNormalFont()));
	}

	private static void addMainTable(DdTInterface ddt, Document document) throws Exception {
		PdfPTable table = new PdfPTable(16);
		table.setWidthPercentage(99.1F);
		table.setSpacingAfter(2.0F);
		table.setSpacingBefore(2.0F);
        table.addCell(createPdfPCell("Articolo", createSmallBoldFont(), 3));
        table.addCell(createPdfPCell("Commessa", createSmallBoldFont(), 3));
        table.addCell(createPdfPCell("Descrizione", createSmallBoldFont(), 4));
        table.addCell(createPdfPCell("PR", createSmallBoldFont()));
        table.addCell(createPdfPCell("CP", createSmallBoldFont()));
        table.addCell(createPdfPCell("PC", createSmallBoldFont()));
        table.addCell(createPdfPCell("PZ", createSmallBoldFont()));
        table.addCell(createPdfPCell("IA", createSmallBoldFont()));
        table.addCell(createPdfPCell("Capi", createSmallBoldFont()));
        
        List<BeneInterface> beni = ddt.getBeni();
        int totRows = 22; 
		for (int i = 0; i < totRows; i++) {
			boolean isLastRow = i == totRows-1;
			int borders = 12;
			int bordersImage = -1;
			if (isLastRow) {
	            borders += 2;
	            bordersImage = 2;
	        }
			BaseColor color = i%2 == 0 ? BaseColor.WHITE : new BaseColor(237, 237, 237);
			if (i<beni.size()){
				BeneInterface bene = beni.get(i);
				table.addCell(createPdfPCell(bene.getCodice(), createSmallFont(), color, borders, 3));
	        	table.addCell(createPdfPCell(bene.getCommessa(), createSmallFont(), color, borders, 3));
	        	table.addCell(createPdfPCell(bene.getDescrizione(), createSmallFont(), color, borders, 4));
	        	table.addCell(createImage(color, bordersImage, bene.getPrototipo()));
	        	table.addCell(createImage(color, bordersImage, bene.getCampionario()));
	        	table.addCell(createImage(color, bordersImage, bene.getPrimoCapo()));
	        	table.addCell(createImage(color, bordersImage, bene.getPiazzato()));
	        	table.addCell(createImage(color, bordersImage, bene.getInteramenteAdesivato()));
	        	table.addCell(createPdfPCell(bene.getQta()+"", createSmallFont(), color, borders, 1));
			} else {
				table.addCell(createPdfPCell(" ", createSmallFont(), color, borders, 3));
				table.addCell(createPdfPCell(" ", createSmallFont(), color, borders, 3));
				table.addCell(createPdfPCell(" ", createSmallFont(), color, borders, 4));
				table.addCell(createImage(color, bordersImage, false));
				table.addCell(createImage(color, bordersImage, false));
				table.addCell(createImage(color, bordersImage, false));
				table.addCell(createImage(color, bordersImage, false));
				table.addCell(createImage(color, bordersImage, false));
				table.addCell(createPdfPCell(" ", createSmallFont(), color, borders, 1));
			}
		}
        document.add(table);
		
	}
	
	private static PdfPCell createImage(BaseColor color, int borders,
			boolean enabled) throws Exception {
		PdfPCell result;
		if (enabled) {
			Image instance = Image.getInstance(DdtPrinter.class.getClassLoader().getResource("ok2.png"));
			instance.scaleAbsolute(7.0F, 7.0F);
			result = new PdfPCell(instance);
		} else {
			result = new PdfPCell();
		}
		result.setHorizontalAlignment(Element.ALIGN_CENTER);
		result.setVerticalAlignment(Element.ALIGN_MIDDLE);
		result.setFixedHeight(16.0F);
		result.setBorder(borders);
		result.setBackgroundColor(color);
		result.setUseAscender(true);
		return result;
	}
	
	private static PdfPCell createPdfPCell(String text, Font f) {
		return createPdfPCell(text, f, BaseColor.WHITE, -1, 1);
	}

	private static PdfPCell createPdfPCell(String text, Font f, int colspan) {
		return createPdfPCell(text, f, BaseColor.WHITE, -1, colspan);
	}

	private static PdfPCell createPdfPCell(String text, Font f,
			BaseColor color, int borders, int colspan) {
		PdfPCell result = new PdfPCell();
		if (borders > -1) {
			result.setBorder(borders);
		}
		result.setHorizontalAlignment(Element.ALIGN_CENTER);
		result.setVerticalAlignment(Element.ALIGN_MIDDLE);
		result.setPhrase(new Phrase(text, f));
		result.setBackgroundColor(color);
		result.setColspan(colspan);
		result.setUseAscender(true);
		return result;
	}
	
	private static void addSixthLine(DdTInterface ddt, Document document) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100.0F);
		table.setSpacingAfter(0.0F);
		
		PdfPCell pdfPCell = generateSimpleCellWithFieldset("Annotazioni e variazioni: ", ddt.getAnnotazioni());
		pdfPCell.setColspan(2);
		table.addCell(pdfPCell);
		
		
		Integer progressivo = ddt.getProgressivo();
		String progressivoString = progressivo != null && progressivo>0 ? progressivo+"" : " ";
		pdfPCell = generateSimpleCellWithFieldset("N Progressivo (1): ", progressivoString);
		table.addCell(pdfPCell);
		
		pdfPCell = generateSimpleCellWithFieldset("Firma del cessionario: ", " ");
		table.addCell(pdfPCell);
		
		document.add(table);
	}
	
	private static void addFifthLine(DdTInterface ddt, Document document) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100.0F);
		table.setSpacingAfter(0.0F);
		
		PdfPCell pdfPCell = generateSimpleCellWithFieldset("Consegna o inizio trasporto a mezzo: ", ddt.getMezzo());
		pdfPCell.setColspan(2);
		table.addCell(pdfPCell);
		
		pdfPCell = generateSimpleCellWithFieldset("Ora e data del ritiro: ", ddt.getRitiro());
		table.addCell(pdfPCell);
		
		pdfPCell = generateSimpleCellWithFieldset("Firma del conducente: ", " ");
		table.addCell(pdfPCell);
		
		document.add(table);
	}
	
	private static void addFourthLine(DdTInterface ddt, Document document) throws DocumentException {
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100.0F);
		table.setSpacingAfter(0.0F);
		
		PdfPCell pdfPCell = generateSimpleCellWithFieldset("Aspetto esteriore dei beni: ", ddt.getAspettoEsteriore());
		pdfPCell.setColspan(3);
		table.addCell(pdfPCell);
		
		Integer colli = ddt.getColli();
		String colliString = colli != null && colli>0 ? colli+"" : " ";
		pdfPCell = generateSimpleCellWithFieldset("Colli: ", colliString);
		table.addCell(pdfPCell);
		
		Double peso = ddt.getPeso();
		String pesoString = peso != null && peso>0 ? peso+"" : " ";
		pdfPCell = generateSimpleCellWithFieldset("Peso KG: ", pesoString);
		table.addCell(pdfPCell);
		
		pdfPCell = generateSimpleCellWithFieldset("Porto: ", ddt.getPorto());
		table.addCell(pdfPCell);

		document.add(table);
	}

	private static void addThirdLine(DdTInterface ddt, Document document) throws DocumentException {
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100.0F);
		table.setSpacingAfter(0.0F);
		
		PdfPCell pdfPCell = generateSimpleCellWithFieldset("Causale del trasporto: ", ddt.getCausale());
		pdfPCell.setColspan(3);
		table.addCell(pdfPCell);
		
		pdfPCell = generateSimpleCellWithFieldset("Vs ordine n°: ", ddt.getVostroOrdine());
		table.addCell(pdfPCell);
		
		pdfPCell = generateSimpleCellWithFieldset("Del: ", ddt.getVostroOrdineDel());
		table.addCell(pdfPCell);
		
		pdfPCell = generateSimpleCellWithFieldset("Tipo: ", ddt.getTipo());
		table.addCell(pdfPCell);
		
		document.add(table);
	}

	private static PdfPCell generateSimpleCellWithFieldset(String fieldset, String text) {
		PdfPCell pdfPCell = generateTopCellWithFieldset(fieldset);
		Paragraph paragraph = new Paragraph(text, createNormalFont());
		paragraph.setSpacingBefore(10f);
		pdfPCell.addElement(paragraph);
		return pdfPCell;
	}

	private static void addSecondLine(DdTInterface ddt, Document document) throws DocumentException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100.0F);
		table.setSpacingAfter(0.0F);
		
		PdfPCell pdfPCell = generateCessionarioCell(ddt, table);
		table.addCell(pdfPCell);
		
		pdfPCell = generateDestinazioneCell(ddt);
		table.addCell(pdfPCell);

		document.add(table);
	}

	private static PdfPCell generateDestinazioneCell(DdTInterface ddt) {
		PdfPCell generateTopCellWithFieldset = generateTopCellWithFieldset("Destinazione:");
		String destinazione = ddt.getDestinazione();
		Paragraph paragraph = new Paragraph(destinazione, createNormalFont());
		paragraph.setSpacingBefore(10f);
		generateTopCellWithFieldset.addElement(paragraph);
		return generateTopCellWithFieldset;
	}

	private static PdfPCell generateCessionarioCell(DdTInterface ddt, PdfPTable table) {
		PdfPCell generateTopCellWithFieldset = generateTopCellWithFieldset("Cessionario:");
		AziendaInterface cliente = ddt.getCliente();
		Paragraph paragraph = new Paragraph(cliente.getNome(),createNormalFont());
		paragraph.add(Chunk.NEWLINE);
		paragraph.add(new Chunk(generateVia(cliente)));
		paragraph.setSpacingBefore(10f);
		generateTopCellWithFieldset.addElement(paragraph);
		return generateTopCellWithFieldset;
	}

	private static void addFirstLine(AziendaInterface principale, DdTInterface ddt, Document document)
			throws DocumentException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100.0F);
		table.setSpacingAfter(0.0F);

		PdfPCell pdfPCell = generateCedenteCell(principale, table);
		table.addCell(pdfPCell);
		
		pdfPCell = generateDDTCell(ddt);
		table.addCell(pdfPCell);
		
		document.add(table);
	}

	private static PdfPCell generateDDTCell(DdTInterface ddt) {
		PdfPCell pdfPCell = generateTopCellWithFieldset("Documento Di Trasporto (D.d.T.)");
		Paragraph paragraph = new Paragraph("D.P.R. 472 del 14/08/1996", createNormalFont());
		paragraph.add(Chunk.NEWLINE);
		paragraph.add("D.P.R. 696 del 21/12/1996");
		pdfPCell.addElement(paragraph);
		paragraph = new Paragraph("",createNormalFont());
		paragraph.add(new Chunk("Numero "));
		paragraph.add(new Chunk(ddt.getId().toString()));
		paragraph.add(new Chunk(" del "));
		paragraph.add(new Chunk(DateUtils.formatDate(ddt.getData())));
		paragraph.setSpacingBefore(10f);
		pdfPCell.addElement(paragraph);

		paragraph = new Paragraph("A mezzo: ",createNormalFont());
		paragraph.setTabSettings(new TabSettings(56f));
		paragraph.add(Chunk.TABBING);
		paragraph.setSpacingBefore(10f);
		paragraph.add(new Chunk(ddt.getMezzo()));
		pdfPCell.addElement(paragraph);
		return pdfPCell;
	}

	private static PdfPCell generateCedenteCell(AziendaInterface principale, PdfPTable table) {
		PdfPCell pdfPCell = generateTopCellWithFieldset("Cedente:");
		Paragraph paragraph = new Paragraph(principale.getNome(), createNormalFont());
		pdfPCell.addElement(paragraph);
		String via = generateVia(principale);
		
		paragraph = new Paragraph(via, createNormalFont());
		paragraph.setSpacingBefore(10f);
		paragraph.setTabSettings(new TabSettings(56f));
		paragraph.add(Chunk.NEWLINE);
		paragraph.add(new Chunk("PI:"));
		paragraph.add(Chunk.TABBING);
		paragraph.add(principale.getPIva());
		paragraph.add(Chunk.NEWLINE);
		paragraph.add(new Chunk("Cod Fis:"));
		paragraph.add(Chunk.TABBING);
		paragraph.add(principale.getCodFis());
		paragraph.add(Chunk.NEWLINE);
		paragraph.add(new Chunk("Telefono:"));
		paragraph.add(Chunk.TABBING);
		paragraph.add(principale.getTelefono());
		paragraph.add(Chunk.NEWLINE);
		paragraph.add(new Chunk("Fax:"));
		paragraph.add(Chunk.TABBING);
		paragraph.add(principale.getFax());
		paragraph.add(Chunk.NEWLINE);
		paragraph.add(new Chunk("Email:"));
		paragraph.add(Chunk.TABBING);
		paragraph.add(principale.getMail());

		pdfPCell.addElement(paragraph);
		return pdfPCell;
	}

	private static String generateVia(AziendaInterface cliente) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Via ");
		stringBuilder.append(cliente.getVia());
		stringBuilder.append(",");
		stringBuilder.append(cliente.getCivico());
		stringBuilder.append(" ");
		stringBuilder.append(cliente.getCap());
		stringBuilder.append(" ");
		stringBuilder.append(cliente.getCitta());
		stringBuilder.append(" (");
		stringBuilder.append(cliente.getProvincia());
		stringBuilder.append(")");
		return stringBuilder.toString();
	}
	
	private static PdfPCell generateTopCellWithFieldset(String fieldset) {
		PdfPCell pdfPCell = new PdfPCell();
		pdfPCell.setBorder(Rectangle.NO_BORDER);
		pdfPCell.setCellEvent(new PdfPCellBorder());
		pdfPCell.setPaddingTop(5F);
		pdfPCell.setPaddingLeft(10F);
		pdfPCell.setPaddingBottom(15F);
		pdfPCell.setUseAscender(true);

		Paragraph element = new Paragraph(fieldset, createNormalBoldFont());
		pdfPCell.addElement(element);
		return pdfPCell;
	}
}
