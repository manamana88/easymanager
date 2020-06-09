/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.persistenza;

import static progettotlp.facilities.Conversioni.boolToYN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import progettotlp.classes.Fattura;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.Utility;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.FatturaInterface;

/**
 *
 * @author vincenzo
 */
public class FatturaManagerImpl extends AbstractPersistenza implements FatturaManager {

    private static Logger logger = LoggerFactory.getLogger(FatturaManagerImpl.class);

    public FatturaManagerImpl(Properties properties) {
        super(properties);
    }

    public FatturaManagerImpl() {
        super();
    }

    public boolean existsFattura(int mese, AziendaInterface a) {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            int selectedAnno = Utility.getSelectedAnno();
            List<Fattura> fatture= sessione.createQuery("from Fattura f where "
                    + "year(f.emissione)=" + selectedAnno+" and month(f.emissione)="+mese+" and f.cliente.id="+a.getId()).list();
            return !fatture.isEmpty();
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally{
            if (sessione!=null){
                sessione.close();
            }
        }
    }

    public List<Fattura> getAllFatture(boolean initializeDdT, boolean initializeBeni) {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            int selectedAnno = Utility.getSelectedAnno();
            Query query = sessione.createQuery("from Fattura f where "
                    + "year(f.emissione)=" + selectedAnno+" order by f.id desc");
            List<Fattura> list= query.list();
            initializeFattura(list, initializeDdT, initializeBeni);
            return list;
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally {
            if (sessione!=null) {
                sessione.close();
            }
        }
    }

    public void cancellaFattura(int id) throws PersistenzaException {
        delete(getFattura(id,false,false));
    }

    public boolean existsFattura(int id) {
        FatturaInterface fattura = getFattura(id,false,false);
        return fattura!=null;
    }

    public void modificaFattura(FatturaInterface f) throws PersistenzaException {
        update(f);
    }

    public Fattura getFattura(int id,boolean initializeDdT, boolean initializeBeni) {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            int selectedAnno = Utility.getSelectedAnno();
            Object result = sessione.createQuery("from Fattura f "
                    + "where f.id="+id+" and year(f.emissione)=" + selectedAnno+" order by f.id desc").uniqueResult();
            if (result==null){
                return null;
            }
            Fattura toReturn = (Fattura)result;
            initializeFattura(toReturn, initializeDdT, initializeBeni);
            return toReturn;
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally {
            if (sessione!=null) {
                sessione.close();
            }
        }
    }

    public int getLastFattura() {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            int selectedAnno = Utility.getSelectedAnno();
            Object result=sessione.createQuery("select max(f.id) from Fattura f where year(f.emissione)=" + selectedAnno).uniqueResult();
            return result==null?0:(Integer)result;
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally{
            if (sessione!=null) {
                sessione.close();
            }
        }
    }

    public void registraFattura(FatturaInterface f) throws PersistenzaException {
        try {
            FatturaInterface fatturaById = getFattura(f.getId(),false,false);
            if (fatturaById!=null){
                throw new PersistenzaException("Fattura with id: "+f.getId()+" yet exists in year: "+DateUtils.getYear(f.getEmissione()));
            }
            save(f);
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } catch (Exception e){
            throw new PersistenzaException("Unable to registraFattura: ",e);
        }
    }

    public LastSameBeneFatturatoInfos getLastSameBeneFatturatoInfos(BeneInterface b) {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            List<Object[]> results= sessione.createQuery("select f.id,f.emissione,b from Fattura as f join f.ddt as d join d.beni as b where "
                    + "b.codice='"+b.getCodice()+"'"
                    + " and b.campionario='"+boolToYN(b.getCampionario())+"'"
                    + " and b.primoCapo='"+boolToYN(b.getPrimoCapo())+"'"
                    + " and b.piazzato='"+boolToYN(b.getPiazzato())+"'"
                    + " and b.prototipo='"+boolToYN(b.getPrototipo())+"'"
                    + " order by f.emissione desc").list();
            if (results.isEmpty()){
                return null;
            }
            Object[] first = results.get(0);
            return new LastSameBeneFatturatoInfos((Integer)first[0], (Date)first[1], (BeneInterface)first[2]);
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally{
            if (sessione!=null) {
                sessione.close();
            }
        }
    }
    
    public List<Fattura> getFattureByAzienda(Long aziendaId, boolean initializeDdT, boolean initializeBeni) {
    	Session sessione=null;
    	try{
    		int selectedAnno = Utility.getSelectedAnno();
    		sessione=retrieveSession();
    		List<Fattura> list = sessione.createQuery("select f from Fattura as f join f.cliente as c where "
    				+ "c.id='"+aziendaId+"' "
    				+ " and year(f.emissione)=" + selectedAnno
    				+ " order by f.emissione desc").list();
    		initializeFattura(list, initializeDdT, initializeBeni);
    		return list==null?new ArrayList<Fattura>():list;
    	} catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally {
            if (sessione!=null) {
                sessione.close();
            }
    	}
    }

    public List<Fattura> getFattureByAziendaName(String aziendaName) {
        Session sessione=null;
        try{
            int selectedAnno = Utility.getSelectedAnno();
            sessione=retrieveSession();
            List<Fattura> list = sessione.createQuery("select f from Fattura as f join f.cliente as c where "
                    + "c.nome='"+aziendaName+"' "
                    + " and year(f.emissione)=" + selectedAnno
                    + " order by f.emissione desc").list();
            return list==null?new ArrayList<Fattura>():list;
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally {
            if (sessione!=null) {
                sessione.close();
            }
        }
    }

}
