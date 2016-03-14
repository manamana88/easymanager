/*
 * ProgettoTLPView.java
 */

package progettotlp;

import java.awt.Dialog.ModalityType;
import java.util.Calendar;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import java.util.List;
import java.util.Properties;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import progettotlp.classes.*;
import progettotlp.exceptions.toprint.AbstractExceptionToPrint;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.NoSelectedRow;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.facilities.*;
import progettotlp.models.BeniTableModelUtils;
import progettotlp.models.FatturaTableModelUtils;
import progettotlp.models.SceltaFattureTableModelUtils;
import progettotlp.models.StatisticheRisultatiTableModelUtils;
import progettotlp.persistenza.*;
import progettotlp.statistiche.StatisticheConfronto;
import progettotlp.ui.FormAccountUtilities;
import progettotlp.ui.FormAccountUtilities.FormAccountType;

import progettotlp.ui.FormAziendaUtilities;
import progettotlp.ui.FormAziendaUtilities.FormAziendaType;
import progettotlp.ui.FormDdTUtilities;
import progettotlp.ui.FormDdTUtilities.FormDdTType;
import progettotlp.ui.FormEmailUtilities;
import progettotlp.ui.FormFatturaUtilities;
import progettotlp.ui.FormFatturaUtilities.FormFatturaType;
import progettotlp.ui.FormInvioEmailInCorsoUtilities;
import progettotlp.ui.FormPrezzoUtilities;
import progettotlp.ui.FormPrezzoUtilitiesInterface;
import progettotlp.ui.FormSceltaAccountUtilities;
import progettotlp.ui.FormSceltaAccountUtilities.FormSceltaAccountType;
import progettotlp.ui.FormSceltaAnnoUtilities;
import progettotlp.ui.FormSceltaAziendaUtilities;
import progettotlp.ui.FormSceltaAziendaUtilities.FormSceltaAziendaType;
import progettotlp.ui.FormSceltaDdTUtilities;
import progettotlp.ui.FormSceltaDdTUtilities.FormSceltaDdTType;
import progettotlp.ui.FormSceltaFatturaUtilities;
import progettotlp.ui.FormSceltaFatturaUtilities.FormSceltaFatturaType;
import progettotlp.ui.FormSceltaMeseUtilities;
import progettotlp.ui.FormStatisticheRisultatiUtilities;
import progettotlp.ui.FormStatisticheUtilities;
import progettotlp.ui.FormStatisticheUtilities.FormStatisticheType;
import progettotlp.ui.GeneralUtilities;

/**
 * The application's main frame.
 */
public class ProgettoTLPView extends FrameView {
    
    private static Logger logger = LoggerFactory.getLogger(ProgettoTLPView.class);
    public static String CURRENT_YEAR_PROPERTY="currentYear";

    private FormAccountUtilities formAccountUtilities;
    private FormAziendaUtilities formAziendaUtilities;
    private FormSceltaAziendaUtilities formSceltaAziendaUtilities;
    private FormDdTUtilities formDdTUtilities;
    private FormEmailUtilities formEmailUtilities;
    private FormFatturaUtilities formFatturaUtilities;
    private FormSceltaAccountUtilities formSceltaAccountUtilities;
    private FormSceltaAnnoUtilities formSceltaAnnoUtilities;
    private FormSceltaDdTUtilities formSceltaDdTUtilities;
    private FormSceltaMeseUtilities formSceltaMeseUtilities;
    private FormSceltaFatturaUtilities formSceltaFatturaUtilities;
    private FormPrezzoUtilitiesInterface formPrezzoUtilities;
    private FormStatisticheUtilities formStatisticheUtilities;
    private FormStatisticheRisultatiUtilities formStatisticheRisultatiUtilities;
    private FormInvioEmailInCorsoUtilities formInvioEmailInCorsoUtilities;
    private AccountManager accountManager;
    private AziendaManager aziendaManager;
    private DdTManager ddtManager;
    private FatturaManager fatturaManager;
    private StatisticheManager statisticsManager;

    private JDialog aboutBox;
    
    public ProgettoTLPView(SingleFrameApplication app,Properties p) {
        super(app);
        System.setProperty(CURRENT_YEAR_PROPERTY, Calendar.getInstance().get(Calendar.YEAR)+"");
        initComponents();
        
        if (p==null){
            accountManager=new AccountManagerImpl();
            aziendaManager=new AziendaManagerImpl();
            ddtManager=new DdTManagerImpl();
            fatturaManager=new FatturaManagerImpl();
            statisticsManager = new StatisticheManagerImpl();
        } else {
            accountManager=new AccountManagerImpl(p);
            aziendaManager=new AziendaManagerImpl(p);
            ddtManager=new DdTManagerImpl(p);
            fatturaManager=new FatturaManagerImpl(p);
            statisticsManager = new StatisticheManagerImpl(p);

        }
        formAccountUtilities=new FormAccountUtilities(formAccount, accountManager);
        formAziendaUtilities=new FormAziendaUtilities(formAzienda,aziendaManager);
        formSceltaAccountUtilities=new FormSceltaAccountUtilities(sceltaAccount, accountManager);
        formSceltaAnnoUtilities=new FormSceltaAnnoUtilities(sceltaAnno);
        formSceltaAziendaUtilities=new FormSceltaAziendaUtilities(sceltaAzienda, aziendaManager,ddtManager,fatturaManager);
        formDdTUtilities=new FormDdTUtilities(formDdT, aziendaManager,ddtManager);
        formInvioEmailInCorsoUtilities = new FormInvioEmailInCorsoUtilities(formInvioEmailInCorso);
        formEmailUtilities=new FormEmailUtilities(formEmail, formInvioEmailInCorsoUtilities, fatturaManager, aziendaManager, accountManager);
        formPrezzoUtilities = new FormPrezzoUtilities(formPrezzo);
        formFatturaUtilities=new FormFatturaUtilities(formFattura,formPrezzoUtilities,aziendaManager, ddtManager, fatturaManager);
        formSceltaDdTUtilities=new FormSceltaDdTUtilities(sceltaDdT, aziendaManager, ddtManager);
        formSceltaMeseUtilities=new FormSceltaMeseUtilities(sceltaMese);
        formSceltaFatturaUtilities=new FormSceltaFatturaUtilities(sceltaFattura, aziendaManager, fatturaManager);
        formStatisticheUtilities=new FormStatisticheUtilities(formStatistiche, statisticsManager,aziendaManager);
        formStatisticheRisultatiUtilities=new FormStatisticheRisultatiUtilities(formStatisticheRisultati);

        mese.setVisible(false);
        numCapiTot.setEditable(false);
        netto.setEditable(false);
        totIva.setEditable(false);
        totale.setEditable(false);
        cliente.setEditable(false);
        dataScadenza.setEditable(false);
        aziendaId.setVisible(false);
        aziendaPrincipale.setVisible(false);
        realIdDdT.setVisible(false);
        realIdFattura.setVisible(false);
        accountId.setVisible(false);
        
        if (aziendaManager.getNumAziende()==0){
            formAziendaUtilities.showForm(FormAziendaType.REGISTRATION,"Prima di iniziare, inserisci i dati della tua azienda");
        }
        else formAzienda.setModalityType(ModalityType.MODELESS);
    }
    @Action
    public void showAboutBox() {
        GeneralUtilities.showAboutBox(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        sceltaAzienda = new javax.swing.JInternalFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaAziende = new javax.swing.JList();
        aziendaScegliModifica = new javax.swing.JButton();
        aziendaScegliCancella = new javax.swing.JButton();
        aziendaScegliVisualizza = new javax.swing.JButton();
        aziendaScegliFattura = new javax.swing.JButton();
        mese = new javax.swing.JTextField();
        sceltaDdT = new javax.swing.JInternalFrame();
        ddtScegliModifica = new javax.swing.JButton();
        ddtScegliCancella = new javax.swing.JButton();
        ddtScegliVisualizza = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabellaDdT = new javax.swing.JTable();
        ddtScegliStampa = new javax.swing.JButton();
        sceltaFattura = new javax.swing.JInternalFrame();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabellaFatture = new javax.swing.JTable();
        fatturaScegliModifica = new javax.swing.JButton();
        fatturaScegliVisualizza = new javax.swing.JButton();
        fatturaScegliCancella = new javax.swing.JButton();
        fatturaScegliStampa = new javax.swing.JButton();
        sceltaMese = new javax.swing.JInternalFrame();
        jLabel27 = new javax.swing.JLabel();
        elencoMesi = new javax.swing.JComboBox();
        sceltaMeseOk = new javax.swing.JButton();
        sceltaAnno = new javax.swing.JInternalFrame();
        jLabel43 = new javax.swing.JLabel();
        anno = new javax.swing.JComboBox();
        sceltaAnnoOk = new javax.swing.JButton();
        sceltaAnnoAnnulla = new javax.swing.JButton();
        sceltaAccount = new javax.swing.JInternalFrame();
        jScrollPane9 = new javax.swing.JScrollPane();
        accountList = new javax.swing.JList();
        accountScegliModifica = new javax.swing.JButton();
        accountScegliCancella = new javax.swing.JButton();
        accountScegliAnnulla = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        menuFileGestioneAzienda = new javax.swing.JMenuItem();
        menuFileSelezionaAnno = new javax.swing.JMenuItem();
        menuFileEsci = new javax.swing.JMenuItem();
        menuClienti = new javax.swing.JMenu();
        menuClientiAggiungi = new javax.swing.JMenuItem();
        menuClientiModifica = new javax.swing.JMenuItem();
        menuClientiCancella = new javax.swing.JMenuItem();
        menuClientiVisualizza = new javax.swing.JMenuItem();
        menuDdT = new javax.swing.JMenu();
        menuDdTAggiungi = new javax.swing.JMenuItem();
        menuDdTModifica = new javax.swing.JMenuItem();
        menuDdTCancella = new javax.swing.JMenuItem();
        menuDdTVisualizza = new javax.swing.JMenuItem();
        menuDdtStampa = new javax.swing.JMenuItem();
        menuFattura = new javax.swing.JMenu();
        menuFattureAggiungi = new javax.swing.JMenuItem();
        menuFattureModifica = new javax.swing.JMenuItem();
        menuFattureCancella = new javax.swing.JMenuItem();
        menuFattureVisualizza = new javax.swing.JMenuItem();
        menuFattureStampa = new javax.swing.JMenuItem();
        menuStatistiche = new javax.swing.JMenu();
        menuRicercaSemplice = new javax.swing.JMenuItem();
        menuConfrontaPeriodi = new javax.swing.JMenuItem();
        menuMail = new javax.swing.JMenu();
        menuInviaMail = new javax.swing.JMenuItem();
        menuAggiungiAccount = new javax.swing.JMenuItem();
        menuModificaAccount = new javax.swing.JMenuItem();
        menuCancellaAccount = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        formAzienda = new javax.swing.JDialog();
        iva = new javax.swing.JTextField();
        nazione = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        provincia = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        citta = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cap = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        giuridico = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        cod_fis = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        telefono = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        fax = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        registra = new javax.swing.JButton();
        via = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        nomeAzienda = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        civico = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        esci = new javax.swing.JButton();
        annulla = new javax.swing.JButton();
        modificaAzienda = new javax.swing.JButton();
        bottoneOk = new javax.swing.JButton();
        aziendaId = new javax.swing.JTextField();
        aziendaPrincipale = new javax.swing.JTextField();
        tassabile = new javax.swing.JCheckBox();
        jLabel42 = new javax.swing.JLabel();
        formDdT = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        data = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        numero = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        azienda = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        beni = new javax.swing.JTable();
        DdTSalva = new javax.swing.JButton();
        DdTAnnulla = new javax.swing.JButton();
        DdTModifica = new javax.swing.JButton();
        DdTFatto = new javax.swing.JButton();
        tipo = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        vsordine = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        destinazione = new javax.swing.JTextArea();
        jLabel30 = new javax.swing.JLabel();
        causale = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        aspetto = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        vsdel = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        colli = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        peso = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        mezzo = new javax.swing.JComboBox();
        jLabel37 = new javax.swing.JLabel();
        porto = new javax.swing.JComboBox();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        ritiro = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        progressivo = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        annotazioni = new javax.swing.JTextField();
        realIdDdT = new javax.swing.JTextField();
        formFattura = new javax.swing.JDialog();
        jLabel17 = new javax.swing.JLabel();
        numFatt = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        cliente = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        dataEmissione = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        validita = new javax.swing.JComboBox();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabellaFattura = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        numCapiTot = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        netto = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        totIva = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        totale = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        dataScadenza = new javax.swing.JTextField();
        fatturaSalva = new javax.swing.JButton();
        fatturaAnnulla = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        fatturaModificaRiga = new javax.swing.JButton();
        fatturaFatto = new javax.swing.JButton();
        fatturaModifica = new javax.swing.JButton();
        realIdFattura = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        ivaPerc = new javax.swing.JTextField();
        formPrezzo = new javax.swing.JDialog();
        labelBene = new javax.swing.JLabel();
        unitario = new javax.swing.JRadioButton();
        tempo = new javax.swing.JRadioButton();
        prezzo = new javax.swing.JTextField();
        okPrezzo = new javax.swing.JButton();
        labelDdT = new javax.swing.JLabel();
        labelBene1 = new javax.swing.JLabel();
        labelCodice = new javax.swing.JLabel();
        labelBene2 = new javax.swing.JLabel();
        labelCommessa = new javax.swing.JLabel();
        labelBene3 = new javax.swing.JLabel();
        labelDescrizione = new javax.swing.JLabel();
        labelBene4 = new javax.swing.JLabel();
        labelProto = new javax.swing.JLabel();
        labelBene5 = new javax.swing.JLabel();
        labelCamp = new javax.swing.JLabel();
        labelBene6 = new javax.swing.JLabel();
        labelPC = new javax.swing.JLabel();
        label_bene7 = new javax.swing.JLabel();
        labelPiazz = new javax.swing.JLabel();
        labelBene8 = new javax.swing.JLabel();
        labelFatturaRef = new javax.swing.JLabel();
        okModificaPrezzo = new javax.swing.JButton();
        labelCapi = new javax.swing.JLabel();
        labelBene9 = new javax.swing.JLabel();
        labelFatturaOldPrice = new javax.swing.JLabel();
        labelBene10 = new javax.swing.JLabel();
        labelBene11 = new javax.swing.JLabel();
        labelFatturaOldPriceType = new javax.swing.JLabel();
        label_bene8 = new javax.swing.JLabel();
        labelIA = new javax.swing.JLabel();
        formStatistiche = new javax.swing.JDialog();
        confrontoLabel = new javax.swing.JLabel();
        startDate = new javax.swing.JTextField();
        endDate = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        listaAziendeStatistiche = new javax.swing.JList();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        endDateConfronto = new javax.swing.JTextField();
        startDateConfronto = new javax.swing.JTextField();
        capiTagliatiRadioButton = new javax.swing.JRadioButton();
        fatturatoConIvaRadioButton = new javax.swing.JRadioButton();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        resetForm = new javax.swing.JButton();
        avviaRicerca = new javax.swing.JButton();
        fatturatoNoIvaRadioButton = new javax.swing.JRadioButton();
        formStatisticheRisultati = new javax.swing.JDialog();
        jScrollPane8 = new javax.swing.JScrollPane();
        risultatiStatistiche = new javax.swing.JTable();
        formAccount = new javax.swing.JDialog();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        smtp = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        passwordConferma = new javax.swing.JPasswordField();
        annullaAccount = new javax.swing.JButton();
        salvaAccount = new javax.swing.JButton();
        modificaAccount = new javax.swing.JButton();
        accountId = new javax.swing.JTextField();
        formEmail = new javax.swing.JDialog();
        jLabel53 = new javax.swing.JLabel();
        from = new javax.swing.JComboBox();
        jLabel54 = new javax.swing.JLabel();
        altriDestinatari = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        allegato = new javax.swing.JComboBox();
        jLabel56 = new javax.swing.JLabel();
        oggetto = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        testo = new javax.swing.JTextArea();
        jLabel58 = new javax.swing.JLabel();
        destinatario = new javax.swing.JComboBox();
        annullaEmail = new javax.swing.JButton();
        inviaEmail = new javax.swing.JButton();
        formInvioEmailInCorso = new javax.swing.JDialog();
        annullaInvioEmail = new javax.swing.JButton();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sceltaAzienda.setClosable(true);
        sceltaAzienda.setIconifiable(true);
        sceltaAzienda.setTitle("Lista Clienti Registrati");
        sceltaAzienda.setMaximumSize(new java.awt.Dimension(360, 280));
        sceltaAzienda.setMinimumSize(new java.awt.Dimension(360, 280));
        sceltaAzienda.setName("sceltaAzienda"); // NOI18N
        sceltaAzienda.setPreferredSize(new java.awt.Dimension(360, 280));
        sceltaAzienda.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        listaAziende.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listaAziende.setName("listaAziende"); // NOI18N
        jScrollPane1.setViewportView(listaAziende);

        sceltaAzienda.getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 235, 150));

        aziendaScegliModifica.setText("Modifica");
        aziendaScegliModifica.setName("aziendaScegliModifica"); // NOI18N
        aziendaScegliModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aziendaScegliModificaActionPerformed(evt);
            }
        });
        sceltaAzienda.getContentPane().add(aziendaScegliModifica, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, -1, -1));

        aziendaScegliCancella.setText("Cancella");
        aziendaScegliCancella.setName("aziendaScegliCancella"); // NOI18N
        aziendaScegliCancella.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aziendaScegliCancellaActionPerformed(evt);
            }
        });
        sceltaAzienda.getContentPane().add(aziendaScegliCancella, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, -1, -1));

        aziendaScegliVisualizza.setText("Vedi");
        aziendaScegliVisualizza.setName("aziendaScegliVisualizza"); // NOI18N
        aziendaScegliVisualizza.setPreferredSize(new java.awt.Dimension(97, 29));
        aziendaScegliVisualizza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aziendaScegliVisualizzaActionPerformed(evt);
            }
        });
        sceltaAzienda.getContentPane().add(aziendaScegliVisualizza, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, -1, -1));

        aziendaScegliFattura.setText("Emetti fattura");
        aziendaScegliFattura.setName("aziendaScegliFattura"); // NOI18N
        aziendaScegliFattura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aziendaScegliFatturaActionPerformed(evt);
            }
        });
        sceltaAzienda.getContentPane().add(aziendaScegliFattura, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, -1, -1));

        mese.setName("mese"); // NOI18N
        sceltaAzienda.getContentPane().add(mese, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 40, -1, -1));

        mainPanel.add(sceltaAzienda, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 360, 280));

        sceltaDdT.setClosable(true);
        sceltaDdT.setIconifiable(true);
        sceltaDdT.setMaximumSize(new java.awt.Dimension(550, 400));
        sceltaDdT.setMinimumSize(new java.awt.Dimension(550, 400));
        sceltaDdT.setName("sceltaDdT"); // NOI18N
        sceltaDdT.setNormalBounds(new java.awt.Rectangle(0, 0, 550, 400));
        sceltaDdT.setPreferredSize(new java.awt.Dimension(550, 400));
        sceltaDdT.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ddtScegliModifica.setText("Modifica");
        ddtScegliModifica.setName("ddtScegliModifica"); // NOI18N
        ddtScegliModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddtScegliModificaActionPerformed(evt);
            }
        });
        sceltaDdT.getContentPane().add(ddtScegliModifica, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 270, -1, -1));

        ddtScegliCancella.setText("Cancella");
        ddtScegliCancella.setName("ddtScegliCancella"); // NOI18N
        ddtScegliCancella.setPreferredSize(new java.awt.Dimension(98, 29));
        ddtScegliCancella.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddtScegliCancellaActionPerformed(evt);
            }
        });
        sceltaDdT.getContentPane().add(ddtScegliCancella, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 270, -1, -1));

        ddtScegliVisualizza.setText("Vedi");
        ddtScegliVisualizza.setName("ddtScegliVisualizza"); // NOI18N
        ddtScegliVisualizza.setPreferredSize(new java.awt.Dimension(98, 29));
        ddtScegliVisualizza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddtScegliVisualizzaActionPerformed(evt);
            }
        });
        sceltaDdT.getContentPane().add(ddtScegliVisualizza, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 270, -1, -1));

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        tabellaDdT.setModel(progettotlp.models.SceltaDdTTableModelUtils.getDefaultTableModel());
        tabellaDdT.setGridColor(new java.awt.Color(0, 0, 0));
        tabellaDdT.setName("tabellaDdT"); // NOI18N
        tabellaDdT.setRowHeight(22);
        jScrollPane3.setViewportView(tabellaDdT);

        sceltaDdT.getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 500, 240));

        ddtScegliStampa.setLabel("Stampa");
        ddtScegliStampa.setName("ddtScegliStampa"); // NOI18N
        ddtScegliStampa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ddtScegliStampaMouseClicked(evt);
            }
        });
        sceltaDdT.getContentPane().add(ddtScegliStampa, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 270, -1, -1));

        mainPanel.add(sceltaDdT, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 120, 550, 400));

        sceltaFattura.setClosable(true);
        sceltaFattura.setIconifiable(true);
        sceltaFattura.setMaximumSize(new java.awt.Dimension(430, 425));
        sceltaFattura.setMinimumSize(new java.awt.Dimension(430, 425));
        sceltaFattura.setName("sceltaFattura"); // NOI18N
        sceltaFattura.setNormalBounds(new java.awt.Rectangle(0, 0, 430, 425));
        sceltaFattura.setPreferredSize(new java.awt.Dimension(430, 425));
        sceltaFattura.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        tabellaFatture.setModel(SceltaFattureTableModelUtils.getDefaultTableModel());
        tabellaFatture.setName("tabellaFatture"); // NOI18N
        jScrollPane5.setViewportView(tabellaFatture);

        sceltaFattura.getContentPane().add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 365, 322));

        fatturaScegliModifica.setText("Modifica");
        fatturaScegliModifica.setName("fatturaScegliModifica"); // NOI18N
        fatturaScegliModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fatturaScegliModificaActionPerformed(evt);
            }
        });
        sceltaFattura.getContentPane().add(fatturaScegliModifica, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 350, 97, 29));

        fatturaScegliVisualizza.setLabel("Vedi");
        fatturaScegliVisualizza.setName("fatturaScegliVisualizza"); // NOI18N
        fatturaScegliVisualizza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fatturaScegliVisualizzaActionPerformed(evt);
            }
        });
        sceltaFattura.getContentPane().add(fatturaScegliVisualizza, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 350, 97, 29));

        fatturaScegliCancella.setText("Cancella");
        fatturaScegliCancella.setName("fatturaScegliCancella"); // NOI18N
        fatturaScegliCancella.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fatturaScegliCancellaActionPerformed(evt);
            }
        });
        sceltaFattura.getContentPane().add(fatturaScegliCancella, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 350, 97, 29));

        fatturaScegliStampa.setText("Stampa");
        fatturaScegliStampa.setName("fatturaScegliStampa"); // NOI18N
        fatturaScegliStampa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fatturaScegliStampaMouseClicked(evt);
            }
        });
        sceltaFattura.getContentPane().add(fatturaScegliStampa, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 350, -1, -1));

        mainPanel.add(sceltaFattura, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 430, 425));

        sceltaMese.setClosable(true);
        sceltaMese.setName("sceltaMese"); // NOI18N
        sceltaMese.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setText("Scegli il mese di fatturazione:");
        jLabel27.setName("jLabel27"); // NOI18N
        sceltaMese.getContentPane().add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

        elencoMesi.setName("elencoMesi"); // NOI18N
        sceltaMese.getContentPane().add(elencoMesi, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 16, 150, -1));

        sceltaMeseOk.setText("OK");
        sceltaMeseOk.setName("sceltaMeseOk"); // NOI18N
        sceltaMeseOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sceltaMeseOkActionPerformed(evt);
            }
        });
        sceltaMese.getContentPane().add(sceltaMeseOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, -1, -1));

        mainPanel.add(sceltaMese, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 150, 450, 135));

        sceltaAnno.setTitle("Seleziona anno");
        sceltaAnno.setMaximumSize(new java.awt.Dimension(300, 200));
        sceltaAnno.setMinimumSize(new java.awt.Dimension(300, 200));
        sceltaAnno.setName("sceltaAnno"); // NOI18N
        sceltaAnno.setPreferredSize(new java.awt.Dimension(300, 200));
        sceltaAnno.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setText("Seleziona anno:");
        jLabel43.setName("jLabel43"); // NOI18N
        sceltaAnno.getContentPane().add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 80, -1));

        anno.setName("anno"); // NOI18N
        sceltaAnno.getContentPane().add(anno, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, -1, -1));

        sceltaAnnoOk.setText("OK");
        sceltaAnnoOk.setName("sceltaAnnoOk"); // NOI18N
        sceltaAnnoOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sceltaAnnoOkActionPerformed(evt);
            }
        });
        sceltaAnno.getContentPane().add(sceltaAnnoOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        sceltaAnnoAnnulla.setText("Annulla");
        sceltaAnnoAnnulla.setName("sceltaAnnoAnnulla"); // NOI18N
        sceltaAnnoAnnulla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sceltaAnnoAnnullaActionPerformed(evt);
            }
        });
        sceltaAnno.getContentPane().add(sceltaAnnoAnnulla, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, -1, -1));

        mainPanel.add(sceltaAnno, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 210, 140));

        sceltaAccount.setName("sceltaAccount"); // NOI18N
        sceltaAccount.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane9.setName("jScrollPane9"); // NOI18N

        accountList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        accountList.setName("accountList"); // NOI18N
        jScrollPane9.setViewportView(accountList);

        sceltaAccount.getContentPane().add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 220, -1));

        accountScegliModifica.setText("Modifica");
        accountScegliModifica.setName("accountScegliModifica"); // NOI18N
        accountScegliModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountScegliModificaActionPerformed(evt);
            }
        });
        sceltaAccount.getContentPane().add(accountScegliModifica, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 180, -1, -1));

        accountScegliCancella.setText("Cancella");
        accountScegliCancella.setName("accountScegliCancella"); // NOI18N
        accountScegliCancella.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountScegliCancellaActionPerformed(evt);
            }
        });
        sceltaAccount.getContentPane().add(accountScegliCancella, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 180, -1, -1));

        accountScegliAnnulla.setText("Annulla");
        accountScegliAnnulla.setName("accountScegliAnnulla"); // NOI18N
        accountScegliAnnulla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountScegliAnnullaActionPerformed(evt);
            }
        });
        sceltaAccount.getContentPane().add(accountScegliAnnulla, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, -1, -1));

        mainPanel.add(sceltaAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 320, 270));

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText("File");
        fileMenu.setName("fileMenu"); // NOI18N

        menuFileGestioneAzienda.setText("Gestione azienda");
        menuFileGestioneAzienda.setName("menuFileGestioneAzienda"); // NOI18N
        menuFileGestioneAzienda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileGestioneAziendaActionPerformed(evt);
            }
        });
        fileMenu.add(menuFileGestioneAzienda);

        menuFileSelezionaAnno.setText("Seleziona Anno");
        menuFileSelezionaAnno.setName("menuFileSelezionaAnno"); // NOI18N
        menuFileSelezionaAnno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileSelezionaAnnoActionPerformed(evt);
            }
        });
        fileMenu.add(menuFileSelezionaAnno);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(progettotlp.ProgettoTLPApp.class).getContext().getActionMap(ProgettoTLPView.class, this);
        menuFileEsci.setAction(actionMap.get("quit")); // NOI18N
        menuFileEsci.setName("menuFileEsci"); // NOI18N
        menuFileEsci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileEsciActionPerformed(evt);
            }
        });
        fileMenu.add(menuFileEsci);

        menuBar.add(fileMenu);

        menuClienti.setText("Clienti");
        menuClienti.setName("menuClienti"); // NOI18N

        menuClientiAggiungi.setText("Registra");
        menuClientiAggiungi.setName("menuClientiAggiungi"); // NOI18N
        menuClientiAggiungi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuClientiAggiungiActionPerformed(evt);
            }
        });
        menuClienti.add(menuClientiAggiungi);

        menuClientiModifica.setText("Modifica");
        menuClientiModifica.setName("menuClientiModifica"); // NOI18N
        menuClientiModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuClientiModificaActionPerformed(evt);
            }
        });
        menuClienti.add(menuClientiModifica);

        menuClientiCancella.setText("Cancella");
        menuClientiCancella.setName("menuClientiCancella"); // NOI18N
        menuClientiCancella.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuClientiCancellaActionPerformed(evt);
            }
        });
        menuClienti.add(menuClientiCancella);

        menuClientiVisualizza.setText("Visualizza");
        menuClientiVisualizza.setName("menuClientiVisualizza"); // NOI18N
        menuClientiVisualizza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuClientiVisualizzaActionPerformed(evt);
            }
        });
        menuClienti.add(menuClientiVisualizza);

        menuBar.add(menuClienti);

        menuDdT.setText("DdT");

        menuDdTAggiungi.setText("Aggiungi");
        menuDdTAggiungi.setName("menuDdTAggiungi"); // NOI18N
        menuDdTAggiungi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDdTAggiungiActionPerformed(evt);
            }
        });
        menuDdT.add(menuDdTAggiungi);

        menuDdTModifica.setText("Modifica");
        menuDdTModifica.setName("menuDdTModifica"); // NOI18N
        menuDdTModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDdTModificaActionPerformed(evt);
            }
        });
        menuDdT.add(menuDdTModifica);

        menuDdTCancella.setText("Cancella");
        menuDdTCancella.setName("menuDdTCancella"); // NOI18N
        menuDdTCancella.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDdTCancellaActionPerformed(evt);
            }
        });
        menuDdT.add(menuDdTCancella);

        menuDdTVisualizza.setText("Visualizza");
        menuDdTVisualizza.setName("menuDdTVisualizza"); // NOI18N
        menuDdTVisualizza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDdTVisualizzaActionPerformed(evt);
            }
        });
        menuDdT.add(menuDdTVisualizza);

        menuDdtStampa.setText("Stampa");
        menuDdtStampa.setName("menuDdtStampa"); // NOI18N
        menuDdtStampa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDdtStampaActionPerformed(evt);
            }
        });
        menuDdT.add(menuDdtStampa);

        menuBar.add(menuDdT);

        menuFattura.setText("Fatture");
        menuFattura.setName("menuFattura"); // NOI18N

        menuFattureAggiungi.setText("Emetti");
        menuFattureAggiungi.setName("menuFattureAggiungi"); // NOI18N
        menuFattureAggiungi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFattureAggiungiActionPerformed(evt);
            }
        });
        menuFattura.add(menuFattureAggiungi);

        menuFattureModifica.setText("Modifica");
        menuFattureModifica.setName("menuFattureModifica"); // NOI18N
        menuFattureModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFattureModificaActionPerformed(evt);
            }
        });
        menuFattura.add(menuFattureModifica);

        menuFattureCancella.setText("Cancella");
        menuFattureCancella.setName("menuFattureCancella"); // NOI18N
        menuFattureCancella.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFattureCancellaActionPerformed(evt);
            }
        });
        menuFattura.add(menuFattureCancella);

        menuFattureVisualizza.setText("Visualizza");
        menuFattureVisualizza.setName("menuFattureVisualizza"); // NOI18N
        menuFattureVisualizza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFattureVisualizzaActionPerformed(evt);
            }
        });
        menuFattura.add(menuFattureVisualizza);

        menuFattureStampa.setText("Stampa");
        menuFattureStampa.setName("menuFattureStampa"); // NOI18N
        menuFattureStampa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFattureStampaActionPerformed(evt);
            }
        });
        menuFattura.add(menuFattureStampa);

        menuBar.add(menuFattura);

        menuStatistiche.setText("Statistiche");
        menuStatistiche.setToolTipText("");
        menuStatistiche.setName("menuStatistiche"); // NOI18N

        menuRicercaSemplice.setText("Ricerca semplice");
        menuRicercaSemplice.setName("menuRicercaSemplice"); // NOI18N
        menuRicercaSemplice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRicercaSempliceActionPerformed(evt);
            }
        });
        menuStatistiche.add(menuRicercaSemplice);
        menuRicercaSemplice.getAccessibleContext().setAccessibleName("Ricerca Semplice");

        menuConfrontaPeriodi.setText("Confronta periodi");
        menuConfrontaPeriodi.setToolTipText("");
        menuConfrontaPeriodi.setName("menuConfrontaPeriodi"); // NOI18N
        menuConfrontaPeriodi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConfrontaPeriodiActionPerformed(evt);
            }
        });
        menuStatistiche.add(menuConfrontaPeriodi);

        menuBar.add(menuStatistiche);
        menuStatistiche.getAccessibleContext().setAccessibleName("menuStatistiche");

        menuMail.setText("Mail");
        menuMail.setName("menuMail"); // NOI18N

        menuInviaMail.setText("Invia email");
        menuInviaMail.setName("menuInviaMail"); // NOI18N
        menuInviaMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuInviaMailActionPerformed(evt);
            }
        });
        menuMail.add(menuInviaMail);

        menuAggiungiAccount.setText("Aggiungi account");
        menuAggiungiAccount.setName("menuAggiungiAccount"); // NOI18N
        menuAggiungiAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAggiungiAccountActionPerformed(evt);
            }
        });
        menuMail.add(menuAggiungiAccount);

        menuModificaAccount.setText("Modifica account");
        menuModificaAccount.setName("menuModificaAccount"); // NOI18N
        menuModificaAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuModificaAccountActionPerformed(evt);
            }
        });
        menuMail.add(menuModificaAccount);

        menuCancellaAccount.setText("Cancella Account");
        menuCancellaAccount.setName("menuCancellaAccount"); // NOI18N
        menuCancellaAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCancellaAccountActionPerformed(evt);
            }
        });
        menuMail.add(menuCancellaAccount);

        menuBar.add(menuMail);

        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        formAzienda.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        formAzienda.setTitle("Prima di iniziare, inserisci i dati della tua azienda");
        formAzienda.setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        formAzienda.setName("formAzienda"); // NOI18N
        formAzienda.setResizable(false);
        formAzienda.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iva.setName("iva"); // NOI18N
        iva.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ivacopiaPIva(evt);
            }
        });
        formAzienda.getContentPane().add(iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 83, 437, -1));

        nazione.setName("nazione"); // NOI18N
        formAzienda.getContentPane().add(nazione, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 227, 357, -1));

        jLabel9.setText("P. IVA:");
        jLabel9.setName("jLabel9"); // NOI18N
        formAzienda.getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 89, -1, -1));

        provincia.setName("provincia"); // NOI18N
        formAzienda.getContentPane().add(provincia, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 227, 61, -1));

        jLabel8.setText("Nazione:");
        jLabel8.setName("jLabel8"); // NOI18N
        formAzienda.getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 233, -1, -1));

        citta.setName("citta"); // NOI18N
        formAzienda.getContentPane().add(citta, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 191, 357, -1));

        jLabel7.setText("Provincia:");
        jLabel7.setName("jLabel7"); // NOI18N
        formAzienda.getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 233, -1, -1));

        cap.setName("cap"); // NOI18N
        formAzienda.getContentPane().add(cap, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 191, 61, -1));

        jLabel6.setText("Città:");
        jLabel6.setName("jLabel6"); // NOI18N
        formAzienda.getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 197, -1, -1));

        jLabel10.setText("Persona Giuridica:");
        jLabel10.setName("jLabel10"); // NOI18N
        formAzienda.getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 59, -1, -1));

        giuridico.setSelected(true);
        giuridico.setName("giuridico"); // NOI18N
        giuridico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                giuridicoabilita(evt);
            }
        });
        formAzienda.getContentPane().add(giuridico, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 55, -1, -1));

        jLabel11.setText("Codice Fiscale:");
        jLabel11.setName("jLabel11"); // NOI18N
        formAzienda.getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 125, -1, -1));

        cod_fis.setEnabled(false);
        cod_fis.setName("cod_fis"); // NOI18N
        formAzienda.getContentPane().add(cod_fis, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 119, 437, -1));

        jLabel12.setText("Telefono:");
        jLabel12.setName("jLabel12"); // NOI18N
        formAzienda.getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 269, -1, -1));

        telefono.setName("telefono"); // NOI18N
        formAzienda.getContentPane().add(telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 263, 106, -1));

        jLabel13.setText("Fax:");
        jLabel13.setName("jLabel13"); // NOI18N
        formAzienda.getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(238, 269, -1, -1));

        fax.setName("fax"); // NOI18N
        formAzienda.getContentPane().add(fax, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 263, 106, -1));

        jLabel14.setText("e-mail:");
        jLabel14.setName("jLabel14"); // NOI18N
        formAzienda.getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 305, -1, -1));

        email.setName("email"); // NOI18N
        formAzienda.getContentPane().add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 299, 493, -1));

        jLabel5.setText("CAP:");
        jLabel5.setName("jLabel5"); // NOI18N
        formAzienda.getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 197, -1, -1));

        registra.setAction(actionMap.get("register_close")); // NOI18N
        registra.setName("registra"); // NOI18N
        registra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registraActionPerformed(evt);
            }
        });
        formAzienda.getContentPane().add(registra, new org.netbeans.lib.awtextra.AbsoluteConstraints(238, 334, -1, -1));

        via.setName("via"); // NOI18N
        formAzienda.getContentPane().add(via, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 155, 399, -1));

        jLabel3.setText("Via:");
        jLabel3.setName("jLabel3"); // NOI18N
        formAzienda.getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 161, -1, -1));

        nomeAzienda.setName("nomeAzienda"); // NOI18N
        formAzienda.getContentPane().add(nomeAzienda, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 20, 437, -1));

        jLabel2.setText("Nome Azienda:");
        jLabel2.setName("jLabel2"); // NOI18N
        formAzienda.getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 26, -1, -1));

        civico.setName("civico"); // NOI18N
        formAzienda.getContentPane().add(civico, new org.netbeans.lib.awtextra.AbsoluteConstraints(571, 155, 44, -1));

        jLabel4.setText("n°");
        jLabel4.setName("jLabel4"); // NOI18N
        formAzienda.getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(547, 161, -1, -1));

        esci.setText("Esci");
        esci.setName("esci"); // NOI18N
        esci.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                esciMouseClicked(evt);
            }
        });
        formAzienda.getContentPane().add(esci, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 334, 96, -1));

        annulla.setText("Annulla");
        annulla.setName("annulla"); // NOI18N
        annulla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annullaActionPerformed(evt);
            }
        });
        formAzienda.getContentPane().add(annulla, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 334, -1, -1));

        modificaAzienda.setText("Salva");
        modificaAzienda.setName("modificaAzienda"); // NOI18N
        modificaAzienda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificaAziendaActionPerformed(evt);
            }
        });
        formAzienda.getContentPane().add(modificaAzienda, new org.netbeans.lib.awtextra.AbsoluteConstraints(238, 334, -1, -1));

        bottoneOk.setText("Fatto!");
        bottoneOk.setName("bottoneOk"); // NOI18N
        bottoneOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bottoneOkActionPerformed(evt);
            }
        });
        formAzienda.getContentPane().add(bottoneOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 334, -1, -1));

        aziendaId.setName("aziendaId"); // NOI18N
        formAzienda.getContentPane().add(aziendaId, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        aziendaPrincipale.setName("aziendaPrincipale"); // NOI18N
        formAzienda.getContentPane().add(aziendaPrincipale, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        tassabile.setName("tassabile"); // NOI18N
        formAzienda.getContentPane().add(tassabile, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 55, -1, -1));

        jLabel42.setText("Tassabile:");
        jLabel42.setName("jLabel42"); // NOI18N
        formAzienda.getContentPane().add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 59, -1, -1));

        formDdT.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formDdT.setMinimumSize(new java.awt.Dimension(706, 606));
        formDdT.setName("formDdT"); // NOI18N
        formDdT.setResizable(false);
        formDdT.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Data:");
        jLabel1.setName("jLabel1"); // NOI18N
        formDdT.getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 26, -1, -1));

        data.setName("data"); // NOI18N
        formDdT.getContentPane().add(data, new org.netbeans.lib.awtextra.AbsoluteConstraints(84, 20, 90, -1));

        jLabel15.setText("n°");
        jLabel15.setName("jLabel15"); // NOI18N
        formDdT.getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(199, 26, -1, -1));

        numero.setName("numero"); // NOI18N
        formDdT.getContentPane().add(numero, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 90, -1));
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(progettotlp.ProgettoTLPApp.class).getContext().getResourceMap(ProgettoTLPView.class);
        numero.getAccessibleContext().setAccessibleName(resourceMap.getString("causale.AccessibleContext.accessibleName")); // NOI18N

        jLabel16.setText("Azienda:");
        jLabel16.setName("jLabel16"); // NOI18N
        formDdT.getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 26, -1, -1));

        azienda.setName("azienda"); // NOI18N
        formDdT.getContentPane().add(azienda, new org.netbeans.lib.awtextra.AbsoluteConstraints(419, 22, 287, -1));

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        beni.setModel(BeniTableModelUtils.getDefaultTableModel());
        beni.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        beni.setGridColor(new java.awt.Color(0, 0, 0));
        beni.setName("beni"); // NOI18N
        beni.setRowHeight(22);
        jScrollPane2.setViewportView(beni);

        formDdT.getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 62, 683, 240));

        DdTSalva.setText("Salva");
        DdTSalva.setName("DdTSalva"); // NOI18N
        DdTSalva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DdTSalvaActionPerformed(evt);
            }
        });
        formDdT.getContentPane().add(DdTSalva, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 510, 97, -1));

        DdTAnnulla.setText("Annulla");
        DdTAnnulla.setName("DdTAnnulla"); // NOI18N
        DdTAnnulla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DdTAnnullaActionPerformed(evt);
            }
        });
        formDdT.getContentPane().add(DdTAnnulla, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 510, -1, -1));

        DdTModifica.setText("Modifica");
        DdTModifica.setName("DdTModifica"); // NOI18N
        DdTModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DdTModificaActionPerformed(evt);
            }
        });
        formDdT.getContentPane().add(DdTModifica, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 510, -1, -1));

        DdTFatto.setText("Fatto");
        DdTFatto.setName("DdTFatto"); // NOI18N
        DdTFatto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DdTFattoActionPerformed(evt);
            }
        });
        formDdT.getContentPane().add(DdTFatto, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 510, -1, -1));

        tipo.setMaximumRowCount(3);
        tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "in conto", "a saldo" }));
        tipo.setName("tipo"); // NOI18N
        formDdT.getContentPane().add(tipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 440, 100, -1));

        jLabel28.setText("Causale:");
        jLabel28.setName("jLabel28"); // NOI18N
        formDdT.getContentPane().add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 330, -1, 20));

        jLabel29.setText("Vs. ordine:");
        jLabel29.setName("jLabel29"); // NOI18N
        formDdT.getContentPane().add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 440, 60, 20));

        vsordine.setName("vsordine"); // NOI18N
        formDdT.getContentPane().add(vsordine, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 440, 100, -1));

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        destinazione.setColumns(20);
        destinazione.setRows(5);
        destinazione.setName("destinazione"); // NOI18N
        jScrollPane4.setViewportView(destinazione);

        formDdT.getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 360, 190, 60));

        jLabel30.setText("Mezzo:");
        jLabel30.setName("jLabel30"); // NOI18N
        formDdT.getContentPane().add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, -1, 20));

        causale.setName("causale"); // NOI18N
        formDdT.getContentPane().add(causale, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 330, 180, -1));

        jLabel31.setText("* se diversa dalla ditta specificata");
        jLabel31.setName("jLabel31"); // NOI18N
        formDdT.getContentPane().add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, -1, 20));

        jLabel32.setText("Porto:");
        jLabel32.setName("jLabel32"); // NOI18N
        formDdT.getContentPane().add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 400, -1, 20));

        aspetto.setName("aspetto"); // NOI18N
        formDdT.getContentPane().add(aspetto, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 360, 200, -1));

        jLabel33.setText("del");
        jLabel33.setName("jLabel33"); // NOI18N
        formDdT.getContentPane().add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 440, 20, 20));

        vsdel.setName("vsdel"); // NOI18N
        formDdT.getContentPane().add(vsdel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 440, 90, -1));

        jLabel34.setText("n° colli:");
        jLabel34.setName("jLabel34"); // NOI18N
        formDdT.getContentPane().add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 330, 40, 20));

        colli.setName("colli"); // NOI18N
        formDdT.getContentPane().add(colli, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 330, 50, -1));

        jLabel35.setText("Peso (KG):");
        jLabel35.setName("jLabel35"); // NOI18N
        formDdT.getContentPane().add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 330, 60, 20));

        peso.setName("peso"); // NOI18N
        formDdT.getContentPane().add(peso, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 330, 70, -1));

        jLabel36.setText("Aspetto esteriore dei beni:");
        jLabel36.setName("jLabel36"); // NOI18N
        formDdT.getContentPane().add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 360, -1, 20));

        mezzo.setMaximumRowCount(2);
        mezzo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cessionario", "Cedente", "Vettore" }));
        mezzo.setName("mezzo"); // NOI18N
        formDdT.getContentPane().add(mezzo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, -1, -1));

        jLabel37.setText("tipo");
        jLabel37.setName("jLabel37"); // NOI18N
        formDdT.getContentPane().add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 440, 20, 20));

        porto.setMaximumRowCount(3);
        porto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Destinazione", "Franco" }));
        porto.setName("porto"); // NOI18N
        formDdT.getContentPane().add(porto, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 400, 130, -1));

        jLabel38.setText("Destinazione* :");
        jLabel38.setName("jLabel38"); // NOI18N
        formDdT.getContentPane().add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, -1, 20));

        jLabel39.setText("Data ritiro:");
        jLabel39.setName("jLabel39"); // NOI18N
        formDdT.getContentPane().add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 400, 60, 20));

        ritiro.setName("ritiro"); // NOI18N
        formDdT.getContentPane().add(ritiro, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 400, 80, -1));

        jLabel40.setText("n° progressivo:");
        jLabel40.setName("jLabel40"); // NOI18N
        formDdT.getContentPane().add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 440, 80, 20));

        progressivo.setName("progressivo"); // NOI18N
        formDdT.getContentPane().add(progressivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 440, 70, -1));

        jLabel41.setText("Annotazioni:");
        jLabel41.setName("jLabel41"); // NOI18N
        formDdT.getContentPane().add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 470, -1, 20));

        annotazioni.setName("annotazioni"); // NOI18N
        formDdT.getContentPane().add(annotazioni, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 470, 310, -1));

        realIdDdT.setName("realIdDdT"); // NOI18N
        formDdT.getContentPane().add(realIdDdT, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        formFattura.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        formFattura.setMinimumSize(new java.awt.Dimension(1000, 550));
        formFattura.setName("formFattura"); // NOI18N
        formFattura.setResizable(false);
        formFattura.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setText("N° fattura:");
        jLabel17.setName("jLabel17"); // NOI18N
        formFattura.getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 26, -1, -1));

        numFatt.setName("numFatt"); // NOI18N
        formFattura.getContentPane().add(numFatt, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 77, -1));

        jLabel18.setText("Cliente:");
        jLabel18.setName("jLabel18"); // NOI18N
        formFattura.getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 26, -1, -1));

        cliente.setName("cliente"); // NOI18N
        formFattura.getContentPane().add(cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(271, 20, 201, -1));

        jLabel19.setText("Data emissione:");
        jLabel19.setName("jLabel19"); // NOI18N
        formFattura.getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(501, 26, -1, -1));

        dataEmissione.setName("dataEmissione"); // NOI18N
        formFattura.getContentPane().add(dataEmissione, new org.netbeans.lib.awtextra.AbsoluteConstraints(612, 20, 99, -1));

        jLabel20.setText("Valida per");
        jLabel20.setName("jLabel20"); // NOI18N
        formFattura.getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(738, 26, -1, -1));

        validita.setName("validita"); // NOI18N
        validita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ValiditaChangeValueHandler(evt);
            }
        });
        formFattura.getContentPane().add(validita, new org.netbeans.lib.awtextra.AbsoluteConstraints(805, 22, -1, -1));

        jScrollPane6.setMaximumSize(new java.awt.Dimension(900, 550));
        jScrollPane6.setMinimumSize(new java.awt.Dimension(900, 550));
        jScrollPane6.setName("jScrollPane6"); // NOI18N
        jScrollPane6.setPreferredSize(new java.awt.Dimension(900, 550));

        tabellaFattura.setModel(FatturaTableModelUtils.getDefaultTableModel());
        tabellaFattura.setGridColor(new java.awt.Color(0, 0, 0));
        tabellaFattura.setName("tabellaFattura"); // NOI18N
        jScrollPane6.setViewportView(tabellaFattura);

        formFattura.getContentPane().add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 67, 950, 373));

        jLabel21.setText("N° capi tot:");
        jLabel21.setName("jLabel21"); // NOI18N
        formFattura.getContentPane().add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, -1, -1));

        numCapiTot.setName("numCapiTot"); // NOI18N
        formFattura.getContentPane().add(numCapiTot, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 460, 76, -1));

        jLabel22.setText("Netto:");
        jLabel22.setName("jLabel22"); // NOI18N
        formFattura.getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 460, -1, -1));

        netto.setName("netto"); // NOI18N
        formFattura.getContentPane().add(netto, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 460, 112, -1));

        jLabel23.setText("IVA:");
        jLabel23.setName("jLabel23"); // NOI18N
        formFattura.getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 460, -1, -1));

        totIva.setName("totIva"); // NOI18N
        formFattura.getContentPane().add(totIva, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 460, 98, -1));

        jLabel24.setText("Tot:");
        jLabel24.setName("jLabel24"); // NOI18N
        formFattura.getContentPane().add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 460, -1, -1));

        totale.setName("totale"); // NOI18N
        formFattura.getContentPane().add(totale, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 460, 95, -1));

        jLabel25.setText("Scadenza:");
        jLabel25.setName("jLabel25"); // NOI18N
        formFattura.getContentPane().add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 460, -1, -1));

        dataScadenza.setName("dataScadenza"); // NOI18N
        formFattura.getContentPane().add(dataScadenza, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 460, 125, -1));

        fatturaSalva.setText("Salva");
        fatturaSalva.setName("fatturaSalva"); // NOI18N
        fatturaSalva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fatturaSalvaActionPerformed(evt);
            }
        });
        formFattura.getContentPane().add(fatturaSalva, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 500, 88, -1));

        fatturaAnnulla.setText("Annulla");
        fatturaAnnulla.setName("fatturaAnnulla"); // NOI18N
        fatturaAnnulla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fatturaAnnullaActionPerformed(evt);
            }
        });
        formFattura.getContentPane().add(fatturaAnnulla, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 500, -1, -1));

        jLabel26.setText("giorni.");
        jLabel26.setName("jLabel26"); // NOI18N
        formFattura.getContentPane().add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(882, 26, -1, -1));

        fatturaModificaRiga.setText("Modifica prezzo");
        fatturaModificaRiga.setName("fatturaModificaRiga"); // NOI18N
        fatturaModificaRiga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fatturaModificaRigaActionPerformed(evt);
            }
        });
        formFattura.getContentPane().add(fatturaModificaRiga, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, -1, -1));

        fatturaFatto.setText("Fatto!");
        fatturaFatto.setName("fatturaFatto"); // NOI18N
        fatturaFatto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fatturaFattoActionPerformed(evt);
            }
        });
        formFattura.getContentPane().add(fatturaFatto, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 500, -1, -1));

        fatturaModifica.setText("Modifica");
        fatturaModifica.setName("fatturaModifica"); // NOI18N
        fatturaModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fatturaModificaActionPerformed(evt);
            }
        });
        formFattura.getContentPane().add(fatturaModifica, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 500, 88, -1));

        realIdFattura.setName("realIdFattura"); // NOI18N
        formFattura.getContentPane().add(realIdFattura, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel59.setText("IVA %:");
        jLabel59.setName("jLabel59"); // NOI18N
        formFattura.getContentPane().add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 460, -1, -1));

        ivaPerc.setEnabled(false);
        ivaPerc.setName("ivaPerc"); // NOI18N
        formFattura.getContentPane().add(ivaPerc, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 460, 50, -1));

        formPrezzo.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        formPrezzo.setTitle("Inserisci il relativo prezzo");
        formPrezzo.setMinimumSize(new java.awt.Dimension(350, 530));
        formPrezzo.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        formPrezzo.setName("formPrezzo"); // NOI18N
        formPrezzo.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelBene.setText("DdT:");
        labelBene.setName("labelBene"); // NOI18N
        formPrezzo.getContentPane().add(labelBene, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 40, 20));

        unitario.setText("Costo unitario");
        unitario.setName("unitario"); // NOI18N
        unitario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitarioActionPerformed(evt);
            }
        });
        formPrezzo.getContentPane().add(unitario, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 430, -1, -1));

        tempo.setText("Costo totale a tempo");
        tempo.setName("tempo"); // NOI18N
        tempo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempoActionPerformed(evt);
            }
        });
        formPrezzo.getContentPane().add(tempo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 410, -1, -1));

        prezzo.setName("prezzo"); // NOI18N
        formPrezzo.getContentPane().add(prezzo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 94, -1));

        okPrezzo.setText("OK");
        okPrezzo.setName("okPrezzo"); // NOI18N
        okPrezzo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okPrezzoActionPerformed(evt);
            }
        });
        formPrezzo.getContentPane().add(okPrezzo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 460, -1, -1));

        labelDdT.setName("labelDdT"); // NOI18N
        formPrezzo.getContentPane().add(labelDdT, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 190, 20));

        labelBene1.setText("Codice:");
        labelBene1.setName("labelBene1"); // NOI18N
        formPrezzo.getContentPane().add(labelBene1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 50, 20));

        labelCodice.setName("labelCodice"); // NOI18N
        formPrezzo.getContentPane().add(labelCodice, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 190, 20));

        labelBene2.setText("Commessa:");
        labelBene2.setName("labelBene2"); // NOI18N
        formPrezzo.getContentPane().add(labelBene2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 80, 20));

        labelCommessa.setName("labelCommessa"); // NOI18N
        formPrezzo.getContentPane().add(labelCommessa, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 190, 20));

        labelBene3.setText("Descrizione:");
        labelBene3.setName("labelBene3"); // NOI18N
        formPrezzo.getContentPane().add(labelBene3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 80, 20));

        labelDescrizione.setName("labelDescrizione"); // NOI18N
        formPrezzo.getContentPane().add(labelDescrizione, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 190, 20));

        labelBene4.setText("Prototipo:");
        labelBene4.setName("labelBene4"); // NOI18N
        formPrezzo.getContentPane().add(labelBene4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 80, 20));

        labelProto.setName("labelProto"); // NOI18N
        formPrezzo.getContentPane().add(labelProto, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 190, 20));

        labelBene5.setText("Campionario:");
        labelBene5.setName("labelBene5"); // NOI18N
        formPrezzo.getContentPane().add(labelBene5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 90, 20));

        labelCamp.setName("labelCamp"); // NOI18N
        formPrezzo.getContentPane().add(labelCamp, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 170, 190, 20));

        labelBene6.setText("Primo capo:");
        labelBene6.setName("labelBene6"); // NOI18N
        formPrezzo.getContentPane().add(labelBene6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 80, 20));

        labelPC.setName("labelPC"); // NOI18N
        formPrezzo.getContentPane().add(labelPC, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 190, 20));

        label_bene7.setText("Piazzato:");
        label_bene7.setName("label_bene7"); // NOI18N
        formPrezzo.getContentPane().add(label_bene7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 80, 20));

        labelPiazz.setName("labelPiazz"); // NOI18N
        formPrezzo.getContentPane().add(labelPiazz, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 190, 20));

        labelBene8.setText("Rif. ultima fattura:");
        labelBene8.setName("labelBene8"); // NOI18N
        formPrezzo.getContentPane().add(labelBene8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, 90, 20));

        labelFatturaRef.setName("labelFatturaRef"); // NOI18N
        formPrezzo.getContentPane().add(labelFatturaRef, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 320, 190, 20));

        okModificaPrezzo.setText("OK");
        okModificaPrezzo.setName("okModificaPrezzo"); // NOI18N
        okModificaPrezzo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okModificaPrezzoActionPerformed(evt);
            }
        });
        formPrezzo.getContentPane().add(okModificaPrezzo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 460, -1, -1));

        labelCapi.setName("labelCapi"); // NOI18N
        formPrezzo.getContentPane().add(labelCapi, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 290, 190, 20));

        labelBene9.setText("N° capi:");
        labelBene9.setName("labelBene9"); // NOI18N
        formPrezzo.getContentPane().add(labelBene9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 80, 20));

        labelFatturaOldPrice.setName("labelFatturaOldPrice"); // NOI18N
        formPrezzo.getContentPane().add(labelFatturaOldPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 350, 190, 20));

        labelBene10.setText("Ultimo prezzo:");
        labelBene10.setName("labelBene10"); // NOI18N
        formPrezzo.getContentPane().add(labelBene10, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 350, 80, 20));

        labelBene11.setText("Tipo di prezzo:");
        labelBene11.setName("labelBene11"); // NOI18N
        formPrezzo.getContentPane().add(labelBene11, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, 80, 20));

        labelFatturaOldPriceType.setName("labelFatturaOldPriceType"); // NOI18N
        formPrezzo.getContentPane().add(labelFatturaOldPriceType, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 380, 190, 20));

        label_bene8.setText("Int. Adesivato:");
        label_bene8.setName("label_bene8"); // NOI18N
        formPrezzo.getContentPane().add(label_bene8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, 80, 20));

        labelIA.setName("labelIA"); // NOI18N
        formPrezzo.getContentPane().add(labelIA, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 190, 20));

        formStatistiche.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formStatistiche.setTitle("Aggiungi i filtri necessari");
        formStatistiche.setName("formStatistiche"); // NOI18N
        formStatistiche.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        confrontoLabel.setText("Periodo 2");
        confrontoLabel.setName("confrontoLabel"); // NOI18N
        formStatistiche.getContentPane().add(confrontoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, -1, 20));

        startDate.setName("startDate"); // NOI18N
        formStatistiche.getContentPane().add(startDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 70, -1));

        endDate.setName("endDate"); // NOI18N
        formStatistiche.getContentPane().add(endDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 70, -1));

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        listaAziendeStatistiche.setToolTipText("Tieni premuto CTRL e clicca sulle varie aziende ");
        listaAziendeStatistiche.setName("listaAziendeStatistiche"); // NOI18N
        jScrollPane7.setViewportView(listaAziendeStatistiche);

        formStatistiche.getContentPane().add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 160, 120));

        jLabel46.setText("Scegli data di fine: ");
        jLabel46.setName("jLabel46"); // NOI18N
        formStatistiche.getContentPane().add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 20));

        jLabel47.setText("Cosa vuoi vedere: ");
        jLabel47.setName("jLabel47"); // NOI18N
        formStatistiche.getContentPane().add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, 20));

        endDateConfronto.setName("endDateConfronto"); // NOI18N
        formStatistiche.getContentPane().add(endDateConfronto, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 70, -1));

        startDateConfronto.setName("startDateConfronto"); // NOI18N
        formStatistiche.getContentPane().add(startDateConfronto, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 70, -1));

        capiTagliatiRadioButton.setText("Capi tagliati");
        capiTagliatiRadioButton.setName("capiTagliatiRadioButton"); // NOI18N
        formStatistiche.getContentPane().add(capiTagliatiRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, -1, -1));

        fatturatoConIvaRadioButton.setText("Fatturato con iva");
        fatturatoConIvaRadioButton.setName("fatturatoConIvaRadioButton"); // NOI18N
        formStatistiche.getContentPane().add(fatturatoConIvaRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, -1, -1));

        jLabel49.setText("Scegli le aziende: ");
        jLabel49.setName("jLabel49"); // NOI18N
        formStatistiche.getContentPane().add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, 20));

        jLabel50.setText("Scegli data di partenza: ");
        jLabel50.setName("jLabel50"); // NOI18N
        formStatistiche.getContentPane().add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, 20));

        jLabel51.setText("Periodo 1");
        jLabel51.setName("jLabel51"); // NOI18N
        formStatistiche.getContentPane().add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, 20));

        resetForm.setText("Reset");
        resetForm.setName("resetForm"); // NOI18N
        resetForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetFormActionPerformed(evt);
            }
        });
        formStatistiche.getContentPane().add(resetForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 340, 90, -1));

        avviaRicerca.setText("Avvia Ricerca");
        avviaRicerca.setName("avviaRicerca"); // NOI18N
        avviaRicerca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                avviaRicercaActionPerformed(evt);
            }
        });
        formStatistiche.getContentPane().add(avviaRicerca, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 340, -1, -1));

        fatturatoNoIvaRadioButton.setText("Fatturato senza iva");
        fatturatoNoIvaRadioButton.setName("fatturatoNoIvaRadioButton"); // NOI18N
        formStatistiche.getContentPane().add(fatturatoNoIvaRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 280, -1, -1));

        formStatisticheRisultati.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formStatisticheRisultati.setTitle("Risultati ricerca");
        formStatisticheRisultati.setName("formStatisticheRisultati"); // NOI18N
        formStatisticheRisultati.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane8.setName("jScrollPane8"); // NOI18N

        risultatiStatistiche.setModel(StatisticheRisultatiTableModelUtils.getDefaultTableModel());
        risultatiStatistiche.setName("risultatiStatistiche"); // NOI18N
        jScrollPane8.setViewportView(risultatiStatistiche);

        formStatisticheRisultati.getContentPane().add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 610, 220));

        formAccount.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        formAccount.setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        formAccount.setName("formAccount"); // NOI18N
        formAccount.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel44.setText("SMTP host:");
        jLabel44.setName("jLabel44"); // NOI18N
        formAccount.getContentPane().add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, 30));

        jLabel45.setText("Username:");
        jLabel45.setName("jLabel45"); // NOI18N
        formAccount.getContentPane().add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, 30));

        jLabel48.setText("Password:");
        jLabel48.setName("jLabel48"); // NOI18N
        formAccount.getContentPane().add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, 30));

        jLabel52.setText("Conferma password:");
        jLabel52.setName("jLabel52"); // NOI18N
        formAccount.getContentPane().add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, 30));

        username.setName("username"); // NOI18N
        formAccount.getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 180, -1));

        smtp.setName("smtp"); // NOI18N
        formAccount.getContentPane().add(smtp, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 150, 180, -1));

        password.setName("password"); // NOI18N
        formAccount.getContentPane().add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, 180, -1));

        passwordConferma.setName("passwordConferma"); // NOI18N
        formAccount.getContentPane().add(passwordConferma, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 180, -1));

        annullaAccount.setText("Annulla");
        annullaAccount.setName("annullaAccount"); // NOI18N
        annullaAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annullaAccountActionPerformed(evt);
            }
        });
        formAccount.getContentPane().add(annullaAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 210, 70, -1));

        salvaAccount.setText("Registra");
        salvaAccount.setName("salvaAccount"); // NOI18N
        salvaAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvaAccountActionPerformed(evt);
            }
        });
        formAccount.getContentPane().add(salvaAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, 70, -1));

        modificaAccount.setText("Salva");
        modificaAccount.setName("modificaAccount"); // NOI18N
        modificaAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificaAccountActionPerformed(evt);
            }
        });
        formAccount.getContentPane().add(modificaAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, 70, -1));

        accountId.setName("accountId"); // NOI18N
        formAccount.getContentPane().add(accountId, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 20, -1));

        formEmail.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        formEmail.setTitle("Invia email");
        formEmail.setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        formEmail.setName("formEmail"); // NOI18N
        formEmail.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel53.setText("Testo:");
        jLabel53.setName("jLabel53"); // NOI18N
        formEmail.getContentPane().add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, -1, 30));

        from.setName("from"); // NOI18N
        formEmail.getContentPane().add(from, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 350, 30));

        jLabel54.setText("Da:");
        jLabel54.setName("jLabel54"); // NOI18N
        formEmail.getContentPane().add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, 30));

        altriDestinatari.setName("altriDestinatari"); // NOI18N
        formEmail.getContentPane().add(altriDestinatari, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 350, 30));

        jLabel55.setText("Altri destinatari:");
        jLabel55.setName("jLabel55"); // NOI18N
        formEmail.getContentPane().add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, 30));

        allegato.setName("allegato"); // NOI18N
        formEmail.getContentPane().add(allegato, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 350, 30));

        jLabel56.setText("Allega:");
        jLabel56.setName("jLabel56"); // NOI18N
        formEmail.getContentPane().add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, -1, 30));

        oggetto.setName("oggetto"); // NOI18N
        formEmail.getContentPane().add(oggetto, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 350, 30));

        jLabel57.setText("Oggetto:");
        jLabel57.setName("jLabel57"); // NOI18N
        formEmail.getContentPane().add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, -1, 30));

        jScrollPane10.setName("jScrollPane10"); // NOI18N

        testo.setColumns(20);
        testo.setRows(5);
        testo.setName("testo"); // NOI18N
        jScrollPane10.setViewportView(testo);

        formEmail.getContentPane().add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, 410, 160));

        jLabel58.setText("A:");
        jLabel58.setName("jLabel58"); // NOI18N
        formEmail.getContentPane().add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, 30));

        destinatario.setName("destinatario"); // NOI18N
        destinatario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                destinatarioChangeValueHandler(evt);
            }
        });
        formEmail.getContentPane().add(destinatario, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 350, 30));

        annullaEmail.setText("Annulla");
        annullaEmail.setName("annullaEmail"); // NOI18N
        annullaEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annullaEmailActionPerformed(evt);
            }
        });
        formEmail.getContentPane().add(annullaEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 450, 70, -1));

        inviaEmail.setText("Invia");
        inviaEmail.setName("inviaEmail"); // NOI18N
        inviaEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inviaEmailActionPerformed(evt);
            }
        });
        formEmail.getContentPane().add(inviaEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 450, 70, -1));

        formInvioEmailInCorso.setName("formInvioEmailInCorso"); // NOI18N
        formInvioEmailInCorso.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        annullaInvioEmail.setText("Annulla");
        annullaInvioEmail.setName("annullaInvioEmail"); // NOI18N
        formInvioEmailInCorso.getContentPane().add(annullaInvioEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 80, 40));

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void ivacopiaPIva(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ivacopiaPIva
        try{
            formAziendaUtilities.ivaCopiaPIva();
        } catch (Throwable e){
            logger.error("Unable to ivacopiaPIva",e);
            Dialogs.showErrorDialog(formAzienda, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
}//GEN-LAST:event_ivacopiaPIva

    private void giuridicoabilita(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_giuridicoabilita
        try{
        formAziendaUtilities.giuridicoAbilita();
        } catch (Throwable e){
            logger.error("Unable to giuridicoabilita",e);
            Dialogs.showErrorDialog(formAzienda, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
}//GEN-LAST:event_giuridicoabilita

    private void esciMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_esciMouseClicked
        try{
            formAziendaUtilities.esciSenzaSalvareAzienda();
        } catch (Throwable e){
            logger.error("Unable to esciMouseClicked",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_esciMouseClicked

    private void registraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registraActionPerformed
        try{
            formAziendaUtilities.registraAzienda();
            Dialogs.showOkDialog(formAzienda,"Azienda inserita correttamente","OK");
            formAziendaUtilities.hideForm();
        } catch (AbstractExceptionToPrint e){
            Dialogs.showErrorDialog(formAzienda, e.getBody(), e.getHeader());
        } catch (Throwable e){
            logger.error("Unable to registraActionPerformed",e);
            Dialogs.showErrorDialog(formAzienda, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_registraActionPerformed

    private void menuClientiAggiungiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuClientiAggiungiActionPerformed
        try{
            formAziendaUtilities.showForm(FormAziendaType.NEW_AZIENDA,"Aggiungi una nuova azienda");
        } catch (Throwable e){
            logger.error("Unable to menuClientiAggiungiActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuClientiAggiungiActionPerformed

    private void annullaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annullaActionPerformed
        try{
            formAziendaUtilities.hideForm();
        } catch (Throwable e){
            logger.error("Unable to annullaActionPerformed",e);
            Dialogs.showErrorDialog(formAzienda, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_annullaActionPerformed

    private void aziendaScegliModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aziendaScegliModificaActionPerformed
        try{
            String selectedAzienda = formSceltaAziendaUtilities.getSelectedAzienda();
            Azienda scelta=aziendaManager.getAziendaPerNome(selectedAzienda);
            formAziendaUtilities.compilaFormAzienda(scelta);
            formAziendaUtilities.showForm(FormAziendaType.MODIFICA, "Modifica "+scelta.getNome());
            formSceltaAziendaUtilities.hideForm();
        } catch (NoSelectedRow e){
            Dialogs.showErrorDialog(sceltaAzienda, e.getBody(), e.getHeader());
        } catch (Throwable e){
            logger.error("Unable to aziendaScegliModificaActionPerformed",e);
            Dialogs.showErrorDialog(sceltaAzienda, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_aziendaScegliModificaActionPerformed

    private void menuClientiModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuClientiModificaActionPerformed
        try{
            formSceltaAziendaUtilities.showForm(FormSceltaAziendaType.MODIFICA);
        } catch (Throwable e){
            logger.error("Unable to menuClientiModificaActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuClientiModificaActionPerformed

    private void menuClientiCancellaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuClientiCancellaActionPerformed
        try{
            formSceltaAziendaUtilities.showForm(FormSceltaAziendaType.CANCELLA);
        } catch (Throwable e){
            logger.error("Unable to menuClientiCancellaActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuClientiCancellaActionPerformed

    private void menuClientiVisualizzaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuClientiVisualizzaActionPerformed
        try{
            formSceltaAziendaUtilities.showForm(FormSceltaAziendaType.VISUALIZZA);
        } catch (Throwable e){
            logger.error("Unable to menuClientiVisualizzaActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuClientiVisualizzaActionPerformed

    private void modificaAziendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificaAziendaActionPerformed
        try{
            formAziendaUtilities.modificaAzienda();
            Dialogs.showOkDialog(formAzienda,"Azienda modificata correttamente","OK");
            formAziendaUtilities.hideForm();
        } catch (AbstractExceptionToPrint e){
            Dialogs.showErrorDialog(formAzienda, e.getBody(), e.getHeader());
            logger.error("Unable to modificaAziendaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to modificaAziendaActionPerformed",e);
            Dialogs.showErrorDialog(formAzienda, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_modificaAziendaActionPerformed

    private void aziendaScegliCancellaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aziendaScegliCancellaActionPerformed
        try{
            formSceltaAziendaUtilities.cancellaAzienda();
            Dialogs.showOkDialog(sceltaAzienda,"Azienda cancellata correttamente!","Cancellazione avvenuta");
        } catch (AbstractExceptionToPrint e){
            Dialogs.showErrorDialog(sceltaAzienda, e.getBody(), e.getHeader());
            logger.error("Unable to aziendaScegliCancellaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to aziendaScegliCancellaActionPerformed",e);
            Dialogs.showErrorDialog(sceltaAzienda, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_aziendaScegliCancellaActionPerformed

    private void aziendaScegliVisualizzaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aziendaScegliVisualizzaActionPerformed
        try{
            String selectedAzienda = formSceltaAziendaUtilities.getSelectedAzienda();
            Azienda scelta=aziendaManager.getAziendaPerNome(selectedAzienda);
            formAziendaUtilities.compilaFormAzienda(scelta);
            formAziendaUtilities.showForm(FormAziendaType.VISUALIZZA, scelta.getNome());
            formSceltaAziendaUtilities.hideForm();
        } catch (NoSelectedRow e){
            Dialogs.showErrorDialog(sceltaAzienda, e.getBody(), e.getHeader());
        } catch (Throwable e){
            logger.error("Unable to aziendaScegliVisualizzaActionPerformed",e);
            Dialogs.showErrorDialog(sceltaAzienda, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_aziendaScegliVisualizzaActionPerformed

    private void bottoneOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bottoneOkActionPerformed
        try{
            formAziendaUtilities.hideForm();
        } catch (Throwable e){
            logger.error("Unable to bottoneOkActionPerformed",e);
            Dialogs.showErrorDialog(formAzienda, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_bottoneOkActionPerformed

    private void menuFileGestioneAziendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileGestioneAziendaActionPerformed
        try{
            Azienda scelta=aziendaManager.getAziendaPrincipale();
            formAziendaUtilities.compilaFormAzienda(scelta);
            formAziendaUtilities.showForm(FormAziendaType.MODIFICA, "Modifica i dati della tua azienda");
        } catch (Throwable e){
            logger.error("Unable to menuFileGestioneAziendaActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuFileGestioneAziendaActionPerformed

    private void menuFileEsciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileEsciActionPerformed
        try{
            ProgettoTLPApp.getApplication().quit(evt);
        } catch (Throwable e){
            logger.error("Unable to menuFileEsciActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuFileEsciActionPerformed

    private void menuDdTAggiungiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDdTAggiungiActionPerformed
        try{
            List<Azienda> aziendeNonPrincipali = aziendaManager.getAziendeNonPrincipali();
            if (!aziendeNonPrincipali.isEmpty()){
                formDdTUtilities.showForm(FormDdTUtilities.FormDdTType.AGGIUNGI, "Aggiungi un nuovo Documento di Trasporto");
            }
            else {
                Dialogs.showErrorDialog(mainPanel, "Non ci sono aziende memorizzate, "
                        + "inserire l'azienda di riferimento del DdT prima di continuare", "Non ci sono aziende");
            }
        } catch (Throwable e){
            logger.error("Unable to menuDdTAggiungiActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuDdTAggiungiActionPerformed

    private void DdTSalvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DdTSalvaActionPerformed
        try{
            String result = formDdTUtilities.salvaDdT();
            Dialogs.showOkDialog(formDdT, "DdT n°"+result+" salvato correttamente.", "OK");
            formDdTUtilities.hideForm();
        } catch (AbstractExceptionToPrint e){
            Dialogs.showErrorDialog(formDdT, e.getBody(), e.getHeader());
            logger.error("Unable to DdTSalvaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to DdTSalvaActionPerformed",e);
            Dialogs.showErrorDialog(formDdT, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_DdTSalvaActionPerformed
    
    private void DdTAnnullaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DdTAnnullaActionPerformed
        try{
            if (Dialogs.showYesCancelDialog(formDdT, "Siete sicuri di voler annullare l'operazione?", "Annullamento operazione")==1)
                formDdTUtilities.hideForm();
        } catch (Throwable e){
            logger.error("Unable to DdTAnnullaActionPerformed",e);
            Dialogs.showErrorDialog(formDdT, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_DdTAnnullaActionPerformed

    private void menuDdTModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDdTModificaActionPerformed
        try{
            formSceltaDdTUtilities.showForm(FormSceltaDdTType.MODIFICA);
        } catch (Throwable e){
            logger.error("Unable to menuDdTModificaActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuDdTModificaActionPerformed

    private void menuDdTCancellaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDdTCancellaActionPerformed
        try{
            formSceltaDdTUtilities.showForm(FormSceltaDdTType.CANCELLA);
        } catch (Throwable e){
            logger.error("Unable to menuDdTCancellaActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuDdTCancellaActionPerformed

    private void menuDdTVisualizzaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDdTVisualizzaActionPerformed
        try{
            formSceltaDdTUtilities.showForm(FormSceltaDdTType.VISUALIZZA);
        } catch (Throwable e){
            logger.error("Unable to menuDdTVisualizzaActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuDdTVisualizzaActionPerformed

    private void ddtScegliModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddtScegliModificaActionPerformed
        try{
            Integer selectedDdTId = formSceltaDdTUtilities.getSelectedDdTId();
            DdT selectedDdT = ddtManager.getDdTById(selectedDdTId,true,false);
            formDdTUtilities.compilaFormDdT(selectedDdT);
            formDdTUtilities.showForm(FormDdTType.MODIFICA, "Modifica DdT n°"+selectedDdT.getId());
            formSceltaDdTUtilities.hideForm();
        } catch (AbstractExceptionToPrint e){
            Dialogs.showErrorDialog(sceltaDdT, e.getBody(), e.getHeader());
            logger.error("Unable to ddtScegliModificaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to ddtScegliModificaActionPerformed",e);
            Dialogs.showErrorDialog(sceltaDdT, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_ddtScegliModificaActionPerformed

    private void ddtScegliCancellaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddtScegliCancellaActionPerformed
        try{
            formSceltaDdTUtilities.cancellaSelectedDdT();
            Dialogs.showOkDialog(sceltaDdT, "Cancellazione DdT avvenuta con successo.", "Cancellazione avvenuta");
        } catch (AbstractExceptionToPrint e){
            Dialogs.showErrorDialog(sceltaDdT, e.getBody(), e.getHeader());
            logger.error("Unable to ddtScegliCancellaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to ddtScegliCancellaActionPerformed",e);
            Dialogs.showErrorDialog(sceltaDdT, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_ddtScegliCancellaActionPerformed

    private void ddtScegliVisualizzaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddtScegliVisualizzaActionPerformed
        try{
            Integer selectedDdTId = formSceltaDdTUtilities.getSelectedDdTId();
            DdT ddt = ddtManager.getDdTById(selectedDdTId,true,false);
            formDdTUtilities.compilaFormDdT(ddt);
            formDdTUtilities.showForm(FormDdTType.VISUALIZZA, "DdT n° "+ddt.getId());
            formSceltaDdTUtilities.hideForm();
        } catch (AbstractExceptionToPrint e){
            Dialogs.showErrorDialog(sceltaDdT, e.getBody(), e.getHeader());
            logger.error("Unable to ddtScegliVisualizzaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to ddtScegliVisualizzaActionPerformed",e);
            Dialogs.showErrorDialog(sceltaDdT, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_ddtScegliVisualizzaActionPerformed

    private void DdTModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DdTModificaActionPerformed
        try{
            formDdTUtilities.modificaDdT();
            Dialogs.showOkDialog(formDdT, "Modifica DdT avvenuta con successo.", "Modifica avvenuta");
            formDdTUtilities.hideForm();
        } catch (AbstractExceptionToPrint e){
            Dialogs.showErrorDialog(formDdT, e.getBody(), e.getHeader());
            logger.error("Unable to DdTModificaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to DdTModificaActionPerformed",e);
            Dialogs.showErrorDialog(formDdT, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_DdTModificaActionPerformed

    private void menuFattureAggiungiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFattureAggiungiActionPerformed
        try{
            formSceltaMeseUtilities.showForm();
        } catch (Throwable e){
            logger.error("Unable to menuFattureAggiungiActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuFattureAggiungiActionPerformed

    private void sceltaMeseOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sceltaMeseOkActionPerformed
        try{
            int selectedMeseInt = formSceltaMeseUtilities.getSelectedMeseInt();
            String selectedMeseString = formSceltaMeseUtilities.getSelectedMeseString();
            formSceltaAziendaUtilities.caricaListaPerEmissioneFattura(selectedMeseInt, selectedMeseString);
            formSceltaAziendaUtilities.showForm(FormSceltaAziendaType.FATTURA);
            formSceltaMeseUtilities.hideForm();
        } catch (Throwable e){
            logger.error("Unable to sceltaMeseOkActionPerformed",e);
            Dialogs.showErrorDialog(sceltaMese, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_sceltaMeseOkActionPerformed

    private void DdTFattoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DdTFattoActionPerformed
        try{
            formDdTUtilities.hideForm();
        } catch (Throwable e){
            logger.error("Unable to DdTFattoActionPerformed",e);
            Dialogs.showErrorDialog(formDdT, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_DdTFattoActionPerformed

    private void fatturaSalvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fatturaSalvaActionPerformed
        try{
            formFatturaUtilities.salvaFattura();
            Dialogs.showOkDialog(formFattura, "Registrazione della fattura n° "+numFatt.getText()+" avvenuta correttamente.", "OK");
            formFatturaUtilities.hideForm();
            formSceltaAziendaUtilities.removeSelectedAzienda();
            formSceltaAziendaUtilities.showForm(FormSceltaAziendaType.FATTURA);
        } catch (AbstractExceptionToPrint e){
            Dialogs.showErrorDialog(formFattura, e.getBody(), e.getHeader());
            logger.error("Unable to fatturaSalvaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to fatturaSalvaActionPerformed",e);
            Dialogs.showErrorDialog(formFattura, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_fatturaSalvaActionPerformed

    private void aziendaScegliFatturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aziendaScegliFatturaActionPerformed
        try{
            String nameAzienda = formSceltaAziendaUtilities.getSelectedAzienda();
            Azienda selectedAzienda = aziendaManager.getAziendaPerNome(nameAzienda);
            int intMese = formSceltaAziendaUtilities.getHiddenSelectedMeseInt();
            logger.debug("Chiamata in compilaFormFattura, p.getAllDdT("+nameAzienda+","+ intMese+")");
            formFatturaUtilities.resetFormFattura();
            formFatturaUtilities.compilaFormFatturaAggiungi(selectedAzienda, intMese);
            formFatturaUtilities.showForm(FormFatturaType.AGGIUNGI);
            formSceltaAziendaUtilities.hideForm();
        } catch (NoSelectedRow e){
            Dialogs.showErrorDialog(sceltaAzienda, e.getBody(), e.getHeader());
        } catch (ValidationException e){
            Dialogs.showErrorDialog(sceltaAzienda, e.getBody(), e.getHeader());
        } catch (Throwable e){
            logger.error("Unable to aziendaScegliFatturaActionPerformed",e);
            Dialogs.showErrorDialog(sceltaAzienda, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_aziendaScegliFatturaActionPerformed

    private void okPrezzoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okPrezzoActionPerformed
        try {
            formPrezzoUtilities.okAction();
        } catch (ValidationException e) {
            Dialogs.showErrorDialog(formPrezzo, e.getBody(), e.getHeader());
        } catch (Throwable e){
            logger.error("Unable to okPrezzoActionPerformed",e);
            Dialogs.showErrorDialog(formPrezzo, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_okPrezzoActionPerformed

    private void tempoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempoActionPerformed
        try{
            formPrezzoUtilities.tempoAction();
        } catch (Throwable e){
            logger.error("Unable to tempoActionPerformed",e);
            Dialogs.showErrorDialog(formPrezzo, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_tempoActionPerformed

    private void unitarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitarioActionPerformed
        try{
            formPrezzoUtilities.unitarioAction();
        } catch (Throwable e){
            logger.error("Unable to unitarioActionPerformed",e);
            Dialogs.showErrorDialog(formPrezzo, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_unitarioActionPerformed

    private void fatturaAnnullaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fatturaAnnullaActionPerformed
        try{
            formFatturaUtilities.annullaAggiunta();
        } catch (Throwable e){
            logger.error("Unable to fatturaAnnullaActionPerformed",e);
            Dialogs.showErrorDialog(formFattura, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_fatturaAnnullaActionPerformed

    private void fatturaModificaRigaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fatturaModificaRigaActionPerformed
        try{
            formFatturaUtilities.modificaRiga();
        } catch (NoSelectedRow e){
            Dialogs.showErrorDialog(formFattura, e.getBody(), e.getHeader());
        } catch (ValidationException e){
            Dialogs.showErrorDialog(formFattura, e.getBody(), e.getHeader());
            logger.error("Unable to fatturaModificaRigaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to fatturaModificaRigaActionPerformed",e);
            Dialogs.showErrorDialog(formFattura, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_fatturaModificaRigaActionPerformed

    private void okModificaPrezzoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okModificaPrezzoActionPerformed
        try{
            formPrezzoUtilities.okModificaPrezzoAction();
        } catch (ValidationException e){
            Dialogs.showErrorDialog(formPrezzo, e.getBody(), e.getHeader());
            logger.error("Unable to okModificaPrezzoActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to okModificaPrezzoActionPerformed",e);
            Dialogs.showErrorDialog(formPrezzo, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_okModificaPrezzoActionPerformed

    private void menuFattureCancellaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFattureCancellaActionPerformed
        try{
            formSceltaFatturaUtilities.showForm(FormSceltaFatturaType.CANCELLA);
        } catch (Throwable e){
            logger.error("Unable to menuFattureCancellaActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuFattureCancellaActionPerformed

    private void menuFattureModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFattureModificaActionPerformed
        try{
            formSceltaFatturaUtilities.showForm(FormSceltaFatturaType.MODIFICA);
        } catch (Throwable e){
            logger.error("Unable to menuFattureModificaActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuFattureModificaActionPerformed

    private void menuFattureVisualizzaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFattureVisualizzaActionPerformed
        try{
            formSceltaFatturaUtilities.showForm(FormSceltaFatturaType.VISUALIZZA);
        } catch (Throwable e){
            logger.error("Unable to menuFattureVisualizzaActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_menuFattureVisualizzaActionPerformed

    private void fatturaScegliCancellaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fatturaScegliCancellaActionPerformed
        try{
            formSceltaFatturaUtilities.cancellaFattura();
            Dialogs.showOkDialog(sceltaFattura, "Fattura cancellata correttamente.", "OK");
        } catch (AbstractExceptionToPrint e){
            Dialogs.showErrorDialog(sceltaFattura, e.getBody(), e.getHeader());
            logger.error("Unable to fatturaScegliCancellaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to fatturaScegliCancellaActionPerformed",e);
            Dialogs.showErrorDialog(sceltaFattura, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_fatturaScegliCancellaActionPerformed

    private void fatturaScegliVisualizzaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fatturaScegliVisualizzaActionPerformed
        try{
            Fattura selectedFattura = formSceltaFatturaUtilities.getSelectedFattura(true,true);
            formFatturaUtilities.compilaFormFattura(selectedFattura);
            formFatturaUtilities.showForm(FormFatturaType.VISUALIZZA);
            formSceltaFatturaUtilities.hideForm();
        } catch (AbstractExceptionToPrint e){
            Dialogs.showErrorDialog(sceltaFattura, e.getBody(), e.getHeader());
            logger.error("Unable to fatturaScegliVisualizzaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to fatturaScegliVisualizzaActionPerformed",e);
            Dialogs.showErrorDialog(sceltaFattura, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_fatturaScegliVisualizzaActionPerformed

    private void fatturaFattoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fatturaFattoActionPerformed
        try{
            formFattura.dispose();
            sceltaFattura.setVisible(true);
        } catch (Throwable e){
            logger.error("Unable to fatturaFattoActionPerformed",e);
            Dialogs.showErrorDialog(formFattura, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_fatturaFattoActionPerformed

    private void fatturaScegliModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fatturaScegliModificaActionPerformed
        try{
            Fattura selectedFattura = formSceltaFatturaUtilities.getSelectedFattura(true,true);
            formFatturaUtilities.compilaFormFattura(selectedFattura);
            formFatturaUtilities.showForm(FormFatturaType.MODIFICA);
            formSceltaFatturaUtilities.hideForm();
        } catch (AbstractExceptionToPrint e){
            Dialogs.showErrorDialog(sceltaFattura, e.getBody(), e.getHeader());
            logger.error("Unable to fatturaScegliModificaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to fatturaScegliModificaActionPerformed",e);
            Dialogs.showErrorDialog(sceltaFattura, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_fatturaScegliModificaActionPerformed

    private void fatturaModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fatturaModificaActionPerformed
        try{
            String result=formFatturaUtilities.modificaFattura();
            Dialogs.showOkDialog(formFattura, "Registrazione della fattura n° "+result+" avvenuta correttamente.", "OK");
            formFatturaUtilities.hideForm();
            formSceltaFatturaUtilities.showForm(FormSceltaFatturaType.MODIFICA);
        } catch (GenericExceptionToPrint e){
            Dialogs.showErrorDialog(formFattura, e.getBody(), e.getHeader());
            logger.error("Unable to fatturaModificaActionPerformed",e);
        } catch (Throwable e){
            logger.error("Unable to fatturaModificaActionPerformed",e);
            Dialogs.showErrorDialog(formFattura, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
    }//GEN-LAST:event_fatturaModificaActionPerformed

private void ddtScegliStampaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddtScegliStampaMouseClicked
    try{
        String directoryPath=formSceltaDdTUtilities.stampaSelectedDdT();
        Dialogs.showOkDialog(sceltaDdT, "DdT Stampato nella cartella "+directoryPath, "DdT Stampato");
    } catch (AbstractExceptionToPrint e){
        Dialogs.showErrorDialog(sceltaDdT, e.getBody(), e.getHeader());
            logger.error("Unable to ddtScegliStampaMouseClicked",e);
    } catch (Throwable e){
            logger.error("Unable to ddtScegliStampaMouseClicked",e);
            Dialogs.showErrorDialog(sceltaDdT, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
}//GEN-LAST:event_ddtScegliStampaMouseClicked

private void menuDdtStampaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDdtStampaActionPerformed
    try{
        formSceltaDdTUtilities.showForm(FormSceltaDdTType.STAMPA);
    } catch (Throwable e){
            logger.error("Unable to menuDdtStampaActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_menuDdtStampaActionPerformed

private void menuFattureStampaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFattureStampaActionPerformed
    try{
        formSceltaFatturaUtilities.showForm(FormSceltaFatturaType.STAMPA);
    } catch (Throwable e){
            logger.error("Unable to menuFattureStampaActionPerformed",e);
            Dialogs.showErrorDialog(mainPanel, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
}//GEN-LAST:event_menuFattureStampaActionPerformed

private void fatturaScegliStampaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fatturaScegliStampaMouseClicked
    try{
        String directoryPath=formSceltaFatturaUtilities.stampaFattura();
        Dialogs.showOkDialog(sceltaFattura, "Fattura Stampata nella cartella "+directoryPath, "Fattura Stampata");
    } catch (AbstractExceptionToPrint e){
        Dialogs.showErrorDialog(sceltaFattura, e.getBody(), e.getHeader());
            logger.error("Unable to fatturaScegliStampaMouseClicked",e);
    } catch (Throwable e){
            logger.error("Unable to fatturaScegliStampaMouseClicked",e);
            Dialogs.showErrorDialog(sceltaFattura, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
}//GEN-LAST:event_fatturaScegliStampaMouseClicked

private void ValiditaChangeValueHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ValiditaChangeValueHandler
    try{
        if (formFatturaUtilities !=null){
            formFatturaUtilities.ricalcolaData();
        }
    } catch (Throwable e){
            logger.error("Unable to ValiditaChangeValueHandler",e);
            Dialogs.showErrorDialog(formFattura, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
        }
}//GEN-LAST:event_ValiditaChangeValueHandler

private void menuFileSelezionaAnnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileSelezionaAnnoActionPerformed
    try{
        formSceltaAnnoUtilities.showForm();
    } catch (Throwable e){
        logger.error("Unable to menuFileSelezionaAnnoActionPerformed",e);
        Dialogs.showErrorDialog(sceltaAnno, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_menuFileSelezionaAnnoActionPerformed

private void sceltaAnnoAnnullaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sceltaAnnoAnnullaActionPerformed
    try{
        formSceltaAnnoUtilities.hideForm();
    } catch (Throwable e){
        logger.error("Unable to sceltaAnnoAnnullaActionPerformed",e);
        Dialogs.showErrorDialog(sceltaAnno, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_sceltaAnnoAnnullaActionPerformed

private void sceltaAnnoOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sceltaAnnoOkActionPerformed
    try{
        formSceltaAnnoUtilities.setAnno();
        formSceltaAnnoUtilities.hideForm();
    } catch (Throwable e){
        logger.error("Unable to sceltaAnnoOkActionPerformed",e);
        Dialogs.showErrorDialog(sceltaAnno, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_sceltaAnnoOkActionPerformed

private void menuRicercaSempliceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRicercaSempliceActionPerformed
    try{
        formStatisticheUtilities.show(FormStatisticheType.SEMPLICE);
    } catch (Throwable e){
        logger.error("Unable to menuRicercaSempliceActionPerformed",e);
        Dialogs.showErrorDialog(formStatistiche, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_menuRicercaSempliceActionPerformed

private void resetFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetFormActionPerformed
    try{
        formStatisticheUtilities.resetForm();
    } catch (Throwable e){
        logger.error("Unable to resetFormActionPerformed",e);
        Dialogs.showErrorDialog(formStatistiche, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_resetFormActionPerformed

private void avviaRicercaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_avviaRicercaActionPerformed
    try{
        StatisticheConfronto risultati = formStatisticheUtilities.avviaRicerca();
        formStatisticheRisultatiUtilities.show(formStatisticheUtilities.getRicercaType(), risultati,formStatisticheUtilities.isRicercaSemplice());
    } catch (AbstractExceptionToPrint e){
        Dialogs.showErrorDialog(sceltaFattura, e.getBody(), e.getHeader());
        logger.error("Unable to avviaRicercaActionPerformed",e);
    } catch (Throwable e){
        logger.error("Unable to avviaRicercaActionPerformed",e);
        Dialogs.showErrorDialog(formStatistiche, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_avviaRicercaActionPerformed

private void menuConfrontaPeriodiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConfrontaPeriodiActionPerformed
    try{
        formStatisticheUtilities.show(FormStatisticheType.CONFRONTA_PERIODI);
    } catch (Throwable e){
        logger.error("Unable to menuConfrontaPeriodiActionPerformed",e);
        Dialogs.showErrorDialog(formStatistiche, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_menuConfrontaPeriodiActionPerformed

private void menuAggiungiAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAggiungiAccountActionPerformed
    try{
        formAccountUtilities.showForm(FormAccountType.NEW_ACCOUNT);
    } catch (Throwable e){
        logger.error("Unable to menuAggiungiAccountActionPerformed",e);
        Dialogs.showErrorDialog(formAccount, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_menuAggiungiAccountActionPerformed

private void menuModificaAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuModificaAccountActionPerformed
    try{
        formSceltaAccountUtilities.showForm(FormSceltaAccountType.MODIFICA);
    } catch (Throwable e){
        logger.error("Unable to menuModificaAccountActionPerformed",e);
        Dialogs.showErrorDialog(sceltaAccount, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_menuModificaAccountActionPerformed

private void menuCancellaAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCancellaAccountActionPerformed
    try{
        formSceltaAccountUtilities.showForm(FormSceltaAccountType.CANCELLA);
    } catch (Throwable e){
        logger.error("Unable to menuCancellaAccountActionPerformed",e);
        Dialogs.showErrorDialog(sceltaAccount, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_menuCancellaAccountActionPerformed

private void accountScegliModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountScegliModificaActionPerformed
    try{
        String selectedAccount = formSceltaAccountUtilities.getSelectedAccount();
        AccountEmail accountByUsername = accountManager.getAccountByUsername(selectedAccount);
        formAccountUtilities.compilaFormAccount(accountByUsername);
        formAccountUtilities.showForm(FormAccountType.MODIFICA);
        formSceltaAccountUtilities.hideForm();
    } catch (AbstractExceptionToPrint e){
        Dialogs.showErrorDialog(sceltaAccount, e.getBody(), e.getHeader());
        logger.error("Unable to accountScegliModificaActionPerformed",e);
    } catch (Throwable e){
        logger.error("Unable to accountScegliModificaActionPerformed",e);
        Dialogs.showErrorDialog(sceltaAccount, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_accountScegliModificaActionPerformed

private void accountScegliCancellaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountScegliCancellaActionPerformed
    try{
        formSceltaAccountUtilities.cancellaAccount();
    } catch (AbstractExceptionToPrint e){
        Dialogs.showErrorDialog(sceltaAccount, e.getBody(), e.getHeader());
        logger.error("Unable to accountScegliCancellaActionPerformed",e);
    } catch (Throwable e){
        logger.error("Unable to accountScegliCancellaActionPerformed",e);
        Dialogs.showErrorDialog(sceltaAccount, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_accountScegliCancellaActionPerformed

private void annullaAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annullaAccountActionPerformed
    try{
        formAccountUtilities.hideForm();
    } catch (Throwable e){
        logger.error("Unable to annullaAccountActionPerformed",e);
        Dialogs.showErrorDialog(formAccount, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_annullaAccountActionPerformed

private void salvaAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvaAccountActionPerformed
    try{
        formAccountUtilities.salvaAccount();
        Dialogs.showOkDialog(formAccount, "Account salvato correttamente.", "OK");
        formAccountUtilities.hideForm();
    } catch (AbstractExceptionToPrint e){
        Dialogs.showErrorDialog(formAccount, e.getBody(), e.getHeader());
        logger.error("Unable to salvaAccountActionPerformed",e);
    } catch (Throwable e){
        logger.error("Unable to salvaAccountActionPerformed",e);
        Dialogs.showErrorDialog(formAccount, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_salvaAccountActionPerformed

private void modificaAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificaAccountActionPerformed
    try{
        formAccountUtilities.modificaAccount();
        Dialogs.showOkDialog(formAccount, "Account modificato correttamente.", "OK");
        formAccountUtilities.hideForm();
        formSceltaAccountUtilities.showForm(FormSceltaAccountType.MODIFICA);
    } catch (AbstractExceptionToPrint e){
        Dialogs.showErrorDialog(formAccount, e.getBody(), e.getHeader());
        logger.error("Unable to modificaAccountActionPerformed",e);
    } catch (Throwable e){
        logger.error("Unable to modificaAccountActionPerformed",e);
        Dialogs.showErrorDialog(formAccount, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_modificaAccountActionPerformed

private void accountScegliAnnullaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountScegliAnnullaActionPerformed
    try{
        formSceltaAccountUtilities.hideForm();
    } catch (Throwable e){
        logger.error("Unable to accountScegliAnnullaActionPerformed",e);
        Dialogs.showErrorDialog(sceltaAccount, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_accountScegliAnnullaActionPerformed

private void menuInviaMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuInviaMailActionPerformed
    try{
        formEmailUtilities.showForm();
    } catch (AbstractExceptionToPrint e){
        Dialogs.showErrorDialog(formEmail, e.getBody(), e.getHeader());
        logger.error("Unable to menuInviaMailActionPerformed",e);
    } catch (Throwable e){
        logger.error("Unable to menuInviaMailActionPerformed",e);
        Dialogs.showErrorDialog(formEmail, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_menuInviaMailActionPerformed

private void annullaEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annullaEmailActionPerformed
    try{
        formEmailUtilities.hideForm();
    } catch (Throwable e){
        logger.error("Unable to annullaEmailActionPerformed",e);
        Dialogs.showErrorDialog(formEmail, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_annullaEmailActionPerformed

private void destinatarioChangeValueHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destinatarioChangeValueHandler
    try{
        System.out.println(evt.getActionCommand());
        System.out.println(evt.paramString());
        formEmailUtilities.changeDestinatarioListener();
    } catch (Throwable e){
        logger.error("Unable to destinatarioChangeValueHandler",e);
        Dialogs.showErrorDialog(formEmail, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_destinatarioChangeValueHandler

private void inviaEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inviaEmailActionPerformed
    try{
        MimeMessage message = this.formEmailUtilities.inviaEmail();
        this.formInvioEmailInCorsoUtilities.sendMail(message);
        Dialogs.showOkDialog(this.formEmail, "Email inviata correttamente",
					"OK");
    } catch (AbstractExceptionToPrint e){
        Dialogs.showErrorDialog(formEmail, e.getBody(), e.getHeader());
        logger.error("Unable to inviaEmailActionPerformed",e);
    } catch (Throwable e){
        logger.error("Unable to inviaEmailActionPerformed",e);
        Dialogs.showErrorDialog(formEmail, "Siamo spiacenti si è verificato un errore imprevisto", "Errore");
    }
}//GEN-LAST:event_inviaEmailActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton DdTAnnulla;
    protected javax.swing.JButton DdTFatto;
    protected javax.swing.JButton DdTModifica;
    protected javax.swing.JButton DdTSalva;
    protected javax.swing.JTextField accountId;
    protected javax.swing.JList accountList;
    protected javax.swing.JButton accountScegliAnnulla;
    protected javax.swing.JButton accountScegliCancella;
    protected javax.swing.JButton accountScegliModifica;
    protected javax.swing.JComboBox allegato;
    protected javax.swing.JTextField altriDestinatari;
    protected javax.swing.JComboBox anno;
    protected javax.swing.JTextField annotazioni;
    protected javax.swing.JButton annulla;
    protected javax.swing.JButton annullaAccount;
    protected javax.swing.JButton annullaEmail;
    protected javax.swing.JButton annullaInvioEmail;
    protected javax.swing.JTextField aspetto;
    protected javax.swing.JButton avviaRicerca;
    protected javax.swing.JComboBox azienda;
    protected javax.swing.JTextField aziendaId;
    protected javax.swing.JTextField aziendaPrincipale;
    protected javax.swing.JButton aziendaScegliCancella;
    protected javax.swing.JButton aziendaScegliFattura;
    protected javax.swing.JButton aziendaScegliModifica;
    protected javax.swing.JButton aziendaScegliVisualizza;
    protected javax.swing.JTable beni;
    protected javax.swing.JButton bottoneOk;
    protected javax.swing.JTextField cap;
    protected javax.swing.JRadioButton capiTagliatiRadioButton;
    protected javax.swing.JTextField causale;
    protected javax.swing.JTextField citta;
    protected javax.swing.JTextField civico;
    protected javax.swing.JTextField cliente;
    protected javax.swing.JTextField cod_fis;
    protected javax.swing.JTextField colli;
    protected javax.swing.JLabel confrontoLabel;
    protected javax.swing.JTextField data;
    protected javax.swing.JTextField dataEmissione;
    protected javax.swing.JTextField dataScadenza;
    protected javax.swing.JButton ddtScegliCancella;
    protected javax.swing.JButton ddtScegliModifica;
    protected javax.swing.JButton ddtScegliStampa;
    protected javax.swing.JButton ddtScegliVisualizza;
    protected javax.swing.JComboBox destinatario;
    protected javax.swing.JTextArea destinazione;
    protected javax.swing.JComboBox elencoMesi;
    protected javax.swing.JTextField email;
    protected javax.swing.JTextField endDate;
    protected javax.swing.JTextField endDateConfronto;
    protected javax.swing.JButton esci;
    protected javax.swing.JButton fatturaAnnulla;
    protected javax.swing.JButton fatturaFatto;
    protected javax.swing.JButton fatturaModifica;
    protected javax.swing.JButton fatturaModificaRiga;
    protected javax.swing.JButton fatturaSalva;
    protected javax.swing.JButton fatturaScegliCancella;
    protected javax.swing.JButton fatturaScegliModifica;
    protected javax.swing.JButton fatturaScegliStampa;
    protected javax.swing.JButton fatturaScegliVisualizza;
    private javax.swing.JRadioButton fatturatoConIvaRadioButton;
    protected javax.swing.JRadioButton fatturatoNoIvaRadioButton;
    protected javax.swing.JTextField fax;
    protected javax.swing.JMenu fileMenu;
    protected javax.swing.JDialog formAccount;
    protected javax.swing.JDialog formAzienda;
    protected javax.swing.JDialog formDdT;
    protected javax.swing.JDialog formEmail;
    protected javax.swing.JDialog formFattura;
    private javax.swing.JDialog formInvioEmailInCorso;
    protected javax.swing.JDialog formPrezzo;
    protected javax.swing.JDialog formStatistiche;
    protected javax.swing.JDialog formStatisticheRisultati;
    protected javax.swing.JComboBox from;
    protected javax.swing.JCheckBox giuridico;
    protected javax.swing.JMenu helpMenu;
    protected javax.swing.JButton inviaEmail;
    protected javax.swing.JTextField iva;
    protected javax.swing.JTextField ivaPerc;
    protected javax.swing.JLabel jLabel1;
    protected javax.swing.JLabel jLabel10;
    protected javax.swing.JLabel jLabel11;
    protected javax.swing.JLabel jLabel12;
    protected javax.swing.JLabel jLabel13;
    protected javax.swing.JLabel jLabel14;
    protected javax.swing.JLabel jLabel15;
    protected javax.swing.JLabel jLabel16;
    protected javax.swing.JLabel jLabel17;
    protected javax.swing.JLabel jLabel18;
    protected javax.swing.JLabel jLabel19;
    protected javax.swing.JLabel jLabel2;
    protected javax.swing.JLabel jLabel20;
    protected javax.swing.JLabel jLabel21;
    protected javax.swing.JLabel jLabel22;
    protected javax.swing.JLabel jLabel23;
    protected javax.swing.JLabel jLabel24;
    protected javax.swing.JLabel jLabel25;
    protected javax.swing.JLabel jLabel26;
    protected javax.swing.JLabel jLabel27;
    protected javax.swing.JLabel jLabel28;
    protected javax.swing.JLabel jLabel29;
    protected javax.swing.JLabel jLabel3;
    protected javax.swing.JLabel jLabel30;
    protected javax.swing.JLabel jLabel31;
    protected javax.swing.JLabel jLabel32;
    protected javax.swing.JLabel jLabel33;
    protected javax.swing.JLabel jLabel34;
    protected javax.swing.JLabel jLabel35;
    protected javax.swing.JLabel jLabel36;
    protected javax.swing.JLabel jLabel37;
    protected javax.swing.JLabel jLabel38;
    protected javax.swing.JLabel jLabel39;
    protected javax.swing.JLabel jLabel4;
    protected javax.swing.JLabel jLabel40;
    protected javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    protected javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    protected javax.swing.JLabel jLabel59;
    protected javax.swing.JLabel jLabel6;
    protected javax.swing.JLabel jLabel7;
    protected javax.swing.JLabel jLabel8;
    protected javax.swing.JLabel jLabel9;
    protected javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    protected javax.swing.JScrollPane jScrollPane2;
    protected javax.swing.JScrollPane jScrollPane3;
    protected javax.swing.JScrollPane jScrollPane4;
    protected javax.swing.JScrollPane jScrollPane5;
    protected javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    protected javax.swing.JLabel labelBene;
    protected javax.swing.JLabel labelBene1;
    protected javax.swing.JLabel labelBene10;
    protected javax.swing.JLabel labelBene11;
    protected javax.swing.JLabel labelBene2;
    protected javax.swing.JLabel labelBene3;
    protected javax.swing.JLabel labelBene4;
    protected javax.swing.JLabel labelBene5;
    protected javax.swing.JLabel labelBene6;
    protected javax.swing.JLabel labelBene8;
    protected javax.swing.JLabel labelBene9;
    protected javax.swing.JLabel labelCamp;
    protected javax.swing.JLabel labelCapi;
    protected javax.swing.JLabel labelCodice;
    protected javax.swing.JLabel labelCommessa;
    protected javax.swing.JLabel labelDdT;
    protected javax.swing.JLabel labelDescrizione;
    protected javax.swing.JLabel labelFatturaOldPrice;
    protected javax.swing.JLabel labelFatturaOldPriceType;
    protected javax.swing.JLabel labelFatturaRef;
    protected javax.swing.JLabel labelIA;
    protected javax.swing.JLabel labelPC;
    protected javax.swing.JLabel labelPiazz;
    protected javax.swing.JLabel labelProto;
    protected javax.swing.JLabel label_bene7;
    protected javax.swing.JLabel label_bene8;
    protected javax.swing.JList listaAziende;
    protected javax.swing.JList listaAziendeStatistiche;
    protected javax.swing.JPanel mainPanel;
    private javax.swing.JMenuItem menuAggiungiAccount;
    protected javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuCancellaAccount;
    protected javax.swing.JMenu menuClienti;
    protected javax.swing.JMenuItem menuClientiAggiungi;
    protected javax.swing.JMenuItem menuClientiCancella;
    protected javax.swing.JMenuItem menuClientiModifica;
    protected javax.swing.JMenuItem menuClientiVisualizza;
    protected javax.swing.JMenuItem menuConfrontaPeriodi;
    protected javax.swing.JMenu menuDdT;
    protected javax.swing.JMenuItem menuDdTAggiungi;
    protected javax.swing.JMenuItem menuDdTCancella;
    protected javax.swing.JMenuItem menuDdTModifica;
    protected javax.swing.JMenuItem menuDdTVisualizza;
    protected javax.swing.JMenuItem menuDdtStampa;
    protected javax.swing.JMenu menuFattura;
    protected javax.swing.JMenuItem menuFattureAggiungi;
    protected javax.swing.JMenuItem menuFattureCancella;
    protected javax.swing.JMenuItem menuFattureModifica;
    protected javax.swing.JMenuItem menuFattureStampa;
    protected javax.swing.JMenuItem menuFattureVisualizza;
    protected javax.swing.JMenuItem menuFileEsci;
    protected javax.swing.JMenuItem menuFileGestioneAzienda;
    private javax.swing.JMenuItem menuFileSelezionaAnno;
    private javax.swing.JMenuItem menuInviaMail;
    private javax.swing.JMenu menuMail;
    private javax.swing.JMenuItem menuModificaAccount;
    protected javax.swing.JMenuItem menuRicercaSemplice;
    protected javax.swing.JMenu menuStatistiche;
    protected javax.swing.JTextField mese;
    protected javax.swing.JComboBox mezzo;
    protected javax.swing.JButton modificaAccount;
    protected javax.swing.JButton modificaAzienda;
    protected javax.swing.JTextField nazione;
    protected javax.swing.JTextField netto;
    protected javax.swing.JTextField nomeAzienda;
    protected javax.swing.JTextField numCapiTot;
    protected javax.swing.JTextField numFatt;
    protected javax.swing.JTextField numero;
    protected javax.swing.JTextField oggetto;
    protected javax.swing.JButton okModificaPrezzo;
    protected javax.swing.JButton okPrezzo;
    protected javax.swing.JPasswordField password;
    protected javax.swing.JPasswordField passwordConferma;
    protected javax.swing.JTextField peso;
    protected javax.swing.JComboBox porto;
    protected javax.swing.JTextField prezzo;
    protected javax.swing.JTextField progressivo;
    protected javax.swing.JTextField provincia;
    protected javax.swing.JTextField realIdDdT;
    protected javax.swing.JTextField realIdFattura;
    protected javax.swing.JButton registra;
    protected javax.swing.JButton resetForm;
    protected javax.swing.JTable risultatiStatistiche;
    protected javax.swing.JTextField ritiro;
    protected javax.swing.JButton salvaAccount;
    protected javax.swing.JInternalFrame sceltaAccount;
    protected javax.swing.JInternalFrame sceltaAnno;
    protected javax.swing.JButton sceltaAnnoAnnulla;
    protected javax.swing.JButton sceltaAnnoOk;
    protected javax.swing.JInternalFrame sceltaAzienda;
    protected javax.swing.JInternalFrame sceltaDdT;
    protected javax.swing.JInternalFrame sceltaFattura;
    protected javax.swing.JInternalFrame sceltaMese;
    protected javax.swing.JButton sceltaMeseOk;
    protected javax.swing.JTextField smtp;
    protected javax.swing.JTextField startDate;
    protected javax.swing.JTextField startDateConfronto;
    protected javax.swing.JTable tabellaDdT;
    protected javax.swing.JTable tabellaFattura;
    protected javax.swing.JTable tabellaFatture;
    protected javax.swing.JCheckBox tassabile;
    protected javax.swing.JTextField telefono;
    protected javax.swing.JRadioButton tempo;
    protected javax.swing.JTextArea testo;
    protected javax.swing.JComboBox tipo;
    protected javax.swing.JTextField totIva;
    protected javax.swing.JTextField totale;
    protected javax.swing.JRadioButton unitario;
    protected javax.swing.JTextField username;
    protected javax.swing.JComboBox validita;
    protected javax.swing.JTextField via;
    protected javax.swing.JTextField vsdel;
    protected javax.swing.JTextField vsordine;
    // End of variables declaration//GEN-END:variables
}