/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.persistenza;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import progettotlp.classes.Bene;
import progettotlp.interfaces.BeneInterface;
import progettotlp.test.AnnualTest;

/**
 *
 * @author vincenzo
 */
public class DdTManagerImplTest extends AnnualTest{

    DdTManagerImpl d=new DdTManagerImpl();

    public DdTManagerImplTest() {
    }

    @Test
    @Ignore
    public void testModificaDdT() throws Exception {

        Long idB1 = 1L;
        String codiceB1 = "0001";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        BigDecimal qtaB1 = new BigDecimal("15");
        Boolean protB1 = Boolean.FALSE;
        Boolean piazzB1 = Boolean.FALSE;
        Boolean pcB1 = Boolean.FALSE;
        Boolean campB1 = Boolean.FALSE;

        Long idB2 = 2L;
        String codiceB2 = "0002";
        String commessaB2 = "C0002";
        String descrizioneB2 = "Abito";
        BigDecimal qtaB2 = new BigDecimal("25");
        Boolean protB2 = Boolean.TRUE;
        Boolean piazzB2 = Boolean.TRUE;
        Boolean pcB2 = Boolean.FALSE;
        Boolean campB2 = Boolean.TRUE;

        Bene b1=new Bene();
        b1.setId(idB1);
        b1.setCodice(codiceB1);
        b1.setCommessa(commessaB1);
        b1.setDescrizione(descrizioneB1);
        b1.setQta(qtaB1);
        b1.setPrototipo(protB1);
        b1.setPiazzato(piazzB1);
        b1.setPrimoCapo(pcB1);
        b1.setCampionario(campB1);

        Bene b2=new Bene();
        b2.setId(idB2);
        b2.setCodice(codiceB2);
        b2.setCommessa(commessaB2);
        b2.setDescrizione(descrizioneB2);
        b2.setQta(qtaB2);
        b2.setPrototipo(protB2);
        b2.setPiazzato(piazzB2);
        b2.setPrimoCapo(pcB2);
        b2.setCampionario(campB2);

        Long idB3 = 1L;
        String codiceB3 = "0003";
        String commessaB3 = "C0003";
        String descrizioneB3 = "Abito";
        BigDecimal qtaB3 = new BigDecimal("35");
        Boolean protB3 = Boolean.TRUE;
        Boolean piazzB3 = Boolean.TRUE;
        Boolean pcB3 = Boolean.TRUE;
        Boolean campB3 = Boolean.TRUE;

        Long idB4 = 4L;
        String codiceB4 = "0004";
        String commessaB4 = "C0004";
        String descrizioneB4 = "Abito";
        BigDecimal qtaB4 = new BigDecimal("45");
        Boolean protB4 = Boolean.TRUE;
        Boolean piazzB4 = Boolean.TRUE;
        Boolean pcB4 = Boolean.FALSE;
        Boolean campB4 = Boolean.TRUE;

        Bene b1Modified=new Bene();
        b1Modified.setId(idB3);
        b1Modified.setCodice(codiceB3);
        b1Modified.setCommessa(commessaB3);
        b1Modified.setDescrizione(descrizioneB3);
        b1Modified.setQta(qtaB3);
        b1Modified.setPrototipo(protB3);
        b1Modified.setPiazzato(piazzB3);
        b1Modified.setPrimoCapo(pcB3);
        b1Modified.setCampionario(campB3);

        Bene b4=new Bene();
        b4.setId(idB4);
        b4.setCodice(codiceB4);
        b4.setCommessa(commessaB4);
        b4.setDescrizione(descrizioneB4);
        b4.setQta(qtaB4);
        b4.setPrototipo(protB4);
        b4.setPiazzato(piazzB4);
        b4.setPrimoCapo(pcB4);
        b4.setCampionario(campB4);

        List<Bene> oldBeni = new ArrayList<Bene>();
        oldBeni.add(b1);
        oldBeni.add(b2);
        List<Bene> newBeni = Arrays.asList(b1Modified, b4);
        fail("FAKE TEST");
    }

    @Test
    public void testGetBeneById() {
        BeneInterface b1=new Bene();
        b1.setId(1L);
        BeneInterface b2=new Bene();
        b2.setId(2L);
        BeneInterface b3=new Bene();
        b3.setId(3L);
        List<BeneInterface> asList = Arrays.asList(b1, b2, b3);

        assertEquals(b1, d.getBeneById(1L, asList));
        assertEquals(b2, d.getBeneById(2L, asList));
        assertEquals(b3, d.getBeneById(3L, asList));
    }

    @Test
    public void testMergeBeni() throws Exception {

        Long idB1 = 1L;
        String codiceB1 = "0001";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        BigDecimal qtaB1 = new BigDecimal("15");
        Boolean protB1 = Boolean.FALSE;
        Boolean piazzB1 = Boolean.FALSE;
        Boolean pcB1 = Boolean.FALSE;
        Boolean campB1 = Boolean.FALSE;

        Long idB2 = 2L;
        String codiceB2 = "0002";
        String commessaB2 = "C0002";
        String descrizioneB2 = "Abito";
        BigDecimal qtaB2 = new BigDecimal("25");
        Boolean protB2 = Boolean.TRUE;
        Boolean piazzB2 = Boolean.TRUE;
        Boolean pcB2 = Boolean.FALSE;
        Boolean campB2 = Boolean.TRUE;

        Bene b1=new Bene();
        b1.setId(idB1);
        b1.setCodice(codiceB1);
        b1.setCommessa(commessaB1);
        b1.setDescrizione(descrizioneB1);
        b1.setQta(qtaB1);
        b1.setPrototipo(protB1);
        b1.setPiazzato(piazzB1);
        b1.setPrimoCapo(pcB1);
        b1.setCampionario(campB1);

        Bene b2=new Bene();
        b2.setId(idB2);
        b2.setCodice(codiceB2);
        b2.setCommessa(commessaB2);
        b2.setDescrizione(descrizioneB2);
        b2.setQta(qtaB2);
        b2.setPrototipo(protB2);
        b2.setPiazzato(piazzB2);
        b2.setPrimoCapo(pcB2);
        b2.setCampionario(campB2);
        
        Long idB3 = 1L;
        String codiceB3 = "0003";
        String commessaB3 = "C0003";
        String descrizioneB3 = "Abito";
        BigDecimal qtaB3 = new BigDecimal("35");
        Boolean protB3 = Boolean.TRUE;
        Boolean piazzB3 = Boolean.TRUE;
        Boolean pcB3 = Boolean.TRUE;
        Boolean campB3 = Boolean.TRUE;

        Long idB4 = 4L;
        String codiceB4 = "0004";
        String commessaB4 = "C0004";
        String descrizioneB4 = "Abito";
        BigDecimal qtaB4 = new BigDecimal("45");
        Boolean protB4 = Boolean.TRUE;
        Boolean piazzB4 = Boolean.TRUE;
        Boolean pcB4 = Boolean.FALSE;
        Boolean campB4 = Boolean.TRUE;

        BeneInterface b1Modified=new Bene();
        b1Modified.setId(idB3);
        b1Modified.setCodice(codiceB3);
        b1Modified.setCommessa(commessaB3);
        b1Modified.setDescrizione(descrizioneB3);
        b1Modified.setQta(qtaB3);
        b1Modified.setPrototipo(protB3);
        b1Modified.setPiazzato(piazzB3);
        b1Modified.setPrimoCapo(pcB3);
        b1Modified.setCampionario(campB3);

        BeneInterface b4=new Bene();
        b4.setId(idB4);
        b4.setCodice(codiceB4);
        b4.setCommessa(commessaB4);
        b4.setDescrizione(descrizioneB4);
        b4.setQta(qtaB4);
        b4.setPrototipo(protB4);
        b4.setPiazzato(piazzB4);
        b4.setPrimoCapo(pcB4);
        b4.setCampionario(campB4);

        List<BeneInterface> oldBeni = new ArrayList<>();
        oldBeni.add(b1);
        oldBeni.add(b2);
        List<BeneInterface> newBeni = new ArrayList<>(Arrays.asList(b1Modified, b4));

        d.mergeBeni(oldBeni, newBeni);

        assertEquals(2,oldBeni.size());
        for (BeneInterface b:oldBeni){
            Long id = b.getId();
            if (id.equals(1L)){
                assertEquals(b1Modified,b);
            } else if (id.equals(4L)){
                assertEquals(b4, b);
            } else {
                fail();
            }
        }

        newBeni.add(new Bene());
        d.mergeBeni(oldBeni, newBeni);

        assertEquals(3,oldBeni.size());

        d.mergeBeni(oldBeni, new ArrayList<BeneInterface>());
        assertTrue(oldBeni.isEmpty());

    }

}