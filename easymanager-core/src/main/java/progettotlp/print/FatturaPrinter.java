package progettotlp.print;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import progettotlp.exceptions.PrintException;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.FatturaUtilities;
import progettotlp.facilities.StringUtils;
import progettotlp.facilities.ConfigurationManager.Property;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.interfaces.FatturaInterface;

import com.itextpdf.text.BadElementException;
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
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class FatturaPrinter extends PdfPrinter
{
    private static final int MAX_ROWS = 34;

    private static Logger logger = LoggerFactory.getLogger(FatturaPrinter.class);

	private static Image imageInstance;

    public static File printPage(FatturaInterface f, AziendaInterface principale, boolean deleteOnExit)
                                                                                           throws PrintException
    {
        return printPage(f, principale, null, null, deleteOnExit);
    }

    public static File printPage(FatturaInterface f,
                                       AziendaInterface principale,
                                       String folder,
                                       String filePrefix,
                                       boolean deleteOnExit) throws PrintException {
        try {
        	System.out.println("Start"+System.currentTimeMillis());
            List<PdfPTable> tableBodies = getTableBodies(f.getDdt());
            System.out.println("After Bodies"+System.currentTimeMillis());
            File file;
            if ((folder == null) || (filePrefix == null))
            	file = File.createTempFile("emem", "emem");
            else {
            	file = new File(folder + File.separator + filePrefix + ".pdf");
            }
            if ((!(file.exists())) && (!(file.createNewFile()))) {
            	throw new PrintException("Impossibile creare il file");
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.addAuthor("C.R.Taglio");
            document.addCreator("EasyManager");
            document.addSubject("Fattura");
            document.addCreationDate();
            document.addTitle("Fattura per " + f.getCliente().getNome());
            document.setMargins(15.0F, 15.0F, 15.0F, 15.0F);
            document.open();
            for (int i = 0; i < tableBodies.size(); ++i)
            {
                printHeader(document, principale, f.getCliente());
                printTableHeader(document, f, i + 1, tableBodies.size());
                document.add(tableBodies.get(i));
                printTableFooter(document, f, i == tableBodies.size() - 1);
                printSubtitels(document, f);
                document.newPage();
            }
            document.close();
            outputStream.flush();
            outputStream.close();
            if (deleteOnExit) {
            	file.deleteOnExit();
            }
            System.out.println("End"+System.currentTimeMillis());
            return file;
        } catch (Exception ex) {
            throw new PrintException("Impossibile stampare la fattura", ex);
        }
    }
    
	private static PdfPCell createPdfPCell(String text, Font f) {
		return createPdfPCell(text, f, BaseColor.WHITE, -1);
	}

	private static PdfPCell createPdfPCell(String text, Font f,
			BaseColor color, int borders) {
		PdfPCell result = new PdfPCell();
		if (borders > -1) {
			result.setBorder(borders);
		}
		result.setHorizontalAlignment(Element.ALIGN_CENTER);
		result.setVerticalAlignment(Element.ALIGN_MIDDLE);
		result.setPhrase(new Phrase(text, f));
		result.setFixedHeight(16.0F);
		result.setBackgroundColor(color);
		return result;
	}

	private static PdfPCell createImage(BaseColor color, int borders,
			boolean enabled) throws Exception {
		PdfPCell result;
		if (enabled) {
			Image instance = getImageInstance();
			result = new PdfPCell(instance);
		} else {
			result = new PdfPCell();
		}
		result.setHorizontalAlignment(Element.ALIGN_CENTER);
		result.setVerticalAlignment(Element.ALIGN_MIDDLE);
		result.setFixedHeight(16.0F);
		result.setBorder(borders);
		result.setBackgroundColor(color);
		return result;
	}

	private static Image getImageInstance() throws BadElementException, MalformedURLException, IOException {
		if (imageInstance==null) {			
			imageInstance = Image.getInstance(ConfigurationManager.getProperty(Property.EXTERNAL_RESOURCES)+"/img/ok2.png");
			imageInstance.scaleAbsolute(7.0F, 7.0F);
		}
		return imageInstance;
	}

    private static void printHeader(Document d, AziendaInterface principale, AziendaInterface cliente) throws DocumentException {
        Paragraph p = new Paragraph(principale.getNome(), createHeaderBoldFont());
        p.setSpacingAfter(8.0F);
        d.add(p);
        Paragraph p2 = new Paragraph();
        p2.setTabSettings(new TabSettings(56f));
        p2.setLeading(12.0F);
        p2.setFont(createNormalFont());
        p2.add("Via:");
        p2.add(Chunk.TABBING);
        p2.add(principale.getVia());
        p2.add(Chunk.NEWLINE);
        p2.add("P. Iva:");
        p2.add(Chunk.TABBING);
        p2.add(principale.getPIva());
        p2.add(Chunk.NEWLINE);
        p2.add("Cod Fis:");
        p2.add(Chunk.TABBING);
        p2.add(principale.getCodFis());
        p2.add(Chunk.NEWLINE);
        p2.add("Telefono:");
        p2.add(Chunk.TABBING);
        p2.add(principale.getTelefono());
        p2.add(Chunk.NEWLINE);
        p2.add("Fax:");
        p2.add(Chunk.TABBING);
        p2.add(principale.getFax());
        p2.add(Chunk.NEWLINE);
        p2.add("Email:");
        p2.add(Chunk.TABBING);
        p2.add(principale.getMail());
        d.add(p2);
        p2 = new Paragraph();
        p2.setFont(createNormalFont());
        p2.setIndentationLeft(320.0F);
        p2.setLeading(12.0F);
        p2.setSpacingBefore(-48.0F);
        p2.add("Cliente");
        p2.add(Chunk.NEWLINE);
        p2.add(Chunk.NEWLINE);
        p2.add(cliente.getNome());
        p2.add(Chunk.NEWLINE);
        String indirizzo = cliente.getVia();
        String civico = cliente.getCivico();
        if ((civico != null) && (!(civico.trim().isEmpty()))) {
            indirizzo = indirizzo + ", " + civico;
        }
        String cap = cliente.getCap();
        if ((cap != null) && (!(cap.trim().isEmpty()))) {
            indirizzo = indirizzo + " " + cap;
        }
        String citta = cliente.getCitta();
        if ((citta != null) && (!(citta.trim().isEmpty()))) {
            indirizzo = indirizzo + " " + citta;
        }
        String provincia = cliente.getProvincia();
        if ((provincia != null) && (!(provincia.trim().isEmpty()))) {
            indirizzo = indirizzo + " (" + provincia + ")";
        }
        p2.add(indirizzo);
        d.add(p2);
    }

    private static void printTableHeader(Document d, FatturaInterface f, int page, int totPage) throws Exception {
        PdfPTable table = new PdfPTable(6);
        table.setSpacingBefore(20.0F);
        table.setWidthPercentage(100.0F);
        table.setSpacingAfter(0.0F);
        table.addCell(createPdfPCell("Partita Iva", createNormalBoldFont()));
        table.addCell(createPdfPCell("Codice Fiscale", createNormalBoldFont()));
        table.addCell(createPdfPCell("Fattura n°", createNormalBoldFont()));
        table.addCell(createPdfPCell("Emissione", createNormalBoldFont()));
        table.addCell(createPdfPCell("Regolamento", createNormalBoldFont()));
        table.addCell(createPdfPCell("Pagina", createNormalBoldFont()));
        table.addCell(createPdfPCell(f.getCliente().getPIva(), createNormalFont()));
        table.addCell(createPdfPCell(f.getCliente().getCodFis(), createNormalFont()));
        table.addCell(createPdfPCell(f.getId().toString(), createNormalFont()));
        table.addCell(createPdfPCell(DateUtils.formatDate(f.getEmissione()), createNormalFont()));
        table.addCell(createPdfPCell(DateUtils.getTimeFrame(f.getEmissione(), f.getScadenza()) + " gg.",
                                     createNormalFont()));
        table.addCell(createPdfPCell(page + "/" + totPage, createNormalFont()));

        d.add(table);
    }

    private static PdfPTable retrieveTableBodyWithHeader() {
        PdfPTable table =
            new PdfPTable(
                          new float[] { 0.14F, 0.2F, 0.2F, 0.1F, 0.03F, 0.03F, 0.03F, 0.03F, 0.03F, 0.05F, 0.08F, 0.08F });
        table.setSpacingBefore(5.0F);
        table.setWidthPercentage(100.0F);
        table.setSpacingAfter(0.0F);
        table.addCell(createPdfPCell("Articolo", createSmallBoldFont()));
        table.addCell(createPdfPCell("Commessa", createSmallBoldFont()));
        table.addCell(createPdfPCell("Descrizione", createSmallBoldFont()));
        table.addCell(createPdfPCell("DDT", createSmallBoldFont()));
        table.addCell(createPdfPCell("PR", createSmallBoldFont()));
        table.addCell(createPdfPCell("CP", createSmallBoldFont()));
        table.addCell(createPdfPCell("PC", createSmallBoldFont()));
        table.addCell(createPdfPCell("PZ", createSmallBoldFont()));
        table.addCell(createPdfPCell("IA", createSmallBoldFont()));
        table.addCell(createPdfPCell("Capi", createSmallBoldFont()));
        table.addCell(createPdfPCell("Prezzo", createSmallBoldFont()));
        table.addCell(createPdfPCell("Netto", createSmallBoldFont()));
        return table;
    }

    private static void addEmptyRow(PdfPTable table, int startingRow, boolean isLastRow) throws Exception
    {
        int borders = 12;
        int bordersImage = -1;
        if (isLastRow) {
            borders += 2;
            bordersImage = 2;
        }
        BaseColor color;
        if (startingRow % 2 > 0)
            color = new BaseColor(237, 237, 237);
        else
            color = BaseColor.WHITE;
        table.addCell(createPdfPCell("", createSmallFont(), color, borders));
        table.addCell(createPdfPCell("", createSmallFont(), color, borders));
        table.addCell(createPdfPCell("", createSmallFont(), color, borders));
        table.addCell(createPdfPCell("", createSmallFont(), color, borders));
        table.addCell(createImage(color, bordersImage, false));
        table.addCell(createImage(color, bordersImage, false));
        table.addCell(createImage(color, bordersImage, false));
        table.addCell(createImage(color, bordersImage, false));
        table.addCell(createImage(color, bordersImage, false));
        table.addCell(createPdfPCell("", createSmallFont(), color, borders));
        table.addCell(createPdfPCell("", createSmallFont(), color, borders));
        table.addCell(createPdfPCell("", createSmallFont(), color, borders));
    }

    private static void addRow(BeneFattura b, PdfPTable table, int startingRow, boolean isLastRow) throws Exception {
        int borders = 12;
        int bordersImage = -1;
        if (isLastRow) {
            borders += 2;
            bordersImage = 2;
        }
        BaseColor color;
        if (startingRow % 2 > 0)
            color = new BaseColor(237, 237, 237);
        else
            color = BaseColor.WHITE;
        table.addCell(createPdfPCell(b.getBene().getCodice(), createSmallFont(), color, borders));
        table.addCell(createPdfPCell(b.getBene().getCommessa(), createSmallFont(), color, borders));
        table.addCell(createPdfPCell(b.getBene().getDescrizione(), createSmallFont(), color, borders));
        table.addCell(createPdfPCell(b.generateRif(), createSmallFont(), color, borders));
        table.addCell(createImage(color, bordersImage, b.getBene().getPrototipo().booleanValue()));
        table.addCell(createImage(color, bordersImage, b.getBene().getCampionario().booleanValue()));
        table.addCell(createImage(color, bordersImage, b.getBene().getPrimoCapo().booleanValue()));
        table.addCell(createImage(color, bordersImage, b.getBene().getPiazzato().booleanValue()));
        table.addCell(createImage(color, bordersImage, b.getBene().getInteramenteAdesivato().booleanValue()));
        table.addCell(createPdfPCell(b.getBene().getQta().toString(), createSmallFont(), color, borders));
        BigDecimal prezzo = b.getBene().getPrezzo();
        if (prezzo == null)
            table.addCell(createPdfPCell("", createSmallFont(), color, borders));
        else {
            table.addCell(createPdfPCell(StringUtils.formatNumber(prezzo), createSmallFont(), color, borders));
        }
        table.addCell(createPdfPCell(StringUtils.formatNumber(b.getBene().getTot()), createSmallFont(), color, borders));
    }

    private static void addDdT(DdTInterface d, PdfPTable table, int startingRow) throws Exception {
        List<BeneInterface> beni = d.getBeni();
        for (int i = 0; i < beni.size(); ++i)
            addRow(new BeneFattura(beni.get(i), d.getData(), d.getId()), table, startingRow++, false);
    }

    private static List<PdfPTable> getTableBodies(List<DdTInterface> list) throws Exception {
        List<PdfPTable> result = new ArrayList<PdfPTable>();
        PdfPTable t = retrieveTableBodyWithHeader();
        int currentRow = 0;
//        boolean insertFattura = false;
        for (int i = 0; i < list.size(); ++i) {
        	System.out.println("StartFor"+System.currentTimeMillis());
            DdTInterface get = (DdTInterface) list.get(i);
            int beniSize = get.getBeni().size();
			if (currentRow+beniSize<=MAX_ROWS){
                addDdT(get, t, currentRow);
            } else {
            	fillWithEmptyRows(t, currentRow);
                result.add(t);
                t = retrieveTableBodyWithHeader();
                currentRow=0;
                addDdT(get, t, currentRow);
            }
			currentRow += beniSize;
        }
        fillWithEmptyRows(t, currentRow);
        result.add(t);
        return result;
    }

	private static void fillWithEmptyRows(PdfPTable t, int currentRow) throws Exception {
		for (int i=currentRow; i<MAX_ROWS; i++){
        	addEmptyRow(t, i, i==MAX_ROWS-1);
        }
	}

    private static void printTableFooter(Document d, FatturaInterface f, boolean isLast) throws Exception {
        PdfPTable table = new PdfPTable(5);
        table.setSpacingBefore(5.0F);
        table.setWidthPercentage(100.0F);
        table.setSpacingAfter(0.0F);
        table.addCell(createPdfPCell("Totale Capi", createSmallBoldFont()));
        table.addCell(createPdfPCell("Imponibile", createSmallBoldFont()));
        table.addCell(createPdfPCell("Importo IVA "+f.getIvaPerc().intValue()+"%", createSmallBoldFont()));
        table.addCell(createPdfPCell("Totale Fattura", createSmallBoldFont()));
        table.addCell(createPdfPCell("Scadenza", createSmallBoldFont()));
        if (!(isLast)) {
            table.addCell(createPdfPCell("", createSmallFont()));
            table.addCell(createPdfPCell("", createSmallFont()));
            table.addCell(createPdfPCell("", createSmallFont()));
            table.addCell(createPdfPCell("", createSmallFont()));
            table.addCell(createPdfPCell("SEGUE", createSmallFont()));
        } else {
            table.addCell(createPdfPCell(FatturaUtilities.getTotCapi(f) + "", createSmallFont()));
            table.addCell(createPdfPCell(StringUtils.formatNumber(f.getNetto()), createSmallFont()));
            table.addCell(createPdfPCell(StringUtils.formatNumber(f.getIva()), createSmallFont()));
            table.addCell(createPdfPCell(StringUtils.formatNumber(f.getTotale()), createSmallFont()));
            table.addCell(createPdfPCell(DateUtils.formatDate(f.getScadenza()), createSmallFont()));
        }

        d.add(table);
    }

    private static void printSubtitels(Document document, FatturaInterface f) throws Exception {
        Paragraph p =
            new Paragraph("PR=Prototipo, CP=Campionario, PC=Primo Capo, PZ=Piazzato, IA=Interamente Adesivato",
                          createSmallFont());
        p.setAlignment(1);
        document.add(p);
        String rifBollo = "I Prezzi sono comprensivi di preadesivazione e rifilo";
        String law = "Operazione con iva per cassa ai sensi dell'art. 32 bis del D.L. n.83/2012";
        AziendaInterface azienda = f.getCliente();
        logger.info(azienda.getNome());
        logger.info(azienda.isTassabile().toString());
        if (!azienda.isTassabile()){
            String nazione = azienda.getNazione();
            String toLowerCase = nazione.toLowerCase();
            if(toLowerCase.startsWith("it")){
            	law = "Operazione non imponibile IVA ai sensi dell'art. 8 comma 1 lett. C del D.P.R. 633/72";
                //law = "Non imponibile art.8 comma 1 lettera C DPR 633-1972";
            } else {
                law = "Operazione non imponibile IVA ai sensi dell'art. 7/ter comma 1 del DPR 633/1972";
            }
            law+="\n Come da vs. autorizzazione n. "+azienda.getNumeroAutorizzazione()+" del "+DateUtils.formatDate(azienda.getDataAutorizzazione());
            law+=" da noi registrata al n. "+azienda.getNumeroRegistrazione()+" del "+DateUtils.formatDate(azienda.getDataRegistrazione());
			if (ConfigurationManager.getBolloLimit().compareTo(f.getTotale())<0){
            	rifBollo = "Bollo assolto ai sensi del decreto MEF 17 giugno 2014 (art. 6)";
            }
        }
        p = new Paragraph(rifBollo, createSmallFont());
        p.setAlignment(1);
        document.add(p);
        p = new Paragraph(law, createSmallFont());
        p.setAlignment(1);
        document.add(p);
        p = new Paragraph("BANCA BLS - IBAN: IT93A0538715400000000534628 - SWIFT: BPM0IT22", createSmallBoldFont());
        p.setAlignment(1);
        document.add(p);
    }
}