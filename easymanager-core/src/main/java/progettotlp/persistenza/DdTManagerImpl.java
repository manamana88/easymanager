/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.persistenza;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.Utility;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;

/**
 *
 * @author vincenzo
 */
@ManagedBean(name="ddtManager")
@ApplicationScoped
public class DdTManagerImpl extends AbstractPersistenza implements DdTManager {

    private static Logger logger = LoggerFactory.getLogger(DdTManagerImpl.class);

    public DdTManagerImpl(Properties properties) {
        super(properties);
    }

    public DdTManagerImpl() {
        super();
    }

    public int getLastDdT() {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            int selectedAnno = Utility.getSelectedAnno();
            Object result=sessione.createQuery("select max(d.id) from DdT d where year(d.data)="+selectedAnno).uniqueResult();
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

    public List<Bene> getBeniDdT(Long realId){
        Session sessione=null;
        try{
            sessione=retrieveSession();
            int selectedAnno = Utility.getSelectedAnno();
            return sessione.createQuery("select b from DdT as d join d.beni as b where year(d.data)=" + selectedAnno+" and d.realId="+realId).list();
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

    public boolean isEmptyDdTListMese(int mese, AziendaInterface a) {
        return getAllDdT(a, mese,false,false).isEmpty();
    }

    public boolean existsDdT(Long realId) {
        DdTInterface ddt = getDdT(realId,false,false);
        return ddt!=null;
    }

    public boolean existsDdTById(int id) {
        DdTInterface ddt = getDdTById(id,false,false);
        return ddt!=null;
    }

    public void registraDdT(DdTInterface d) throws PersistenzaException {
        DdTInterface ddTById = getDdTById(d.getId(),false,false);
        if (ddTById!=null){
            throw new PersistenzaException("DdT with id: "+d.getId()+" yet exists in year: "+DateUtils.getYear(d.getData()));
        }
        save(d);
    }

    public DdT getDdT(Long id,boolean initializeBeni, boolean initializeFattura){
        Session sessione=null;
        try{
            sessione=retrieveSession();
            int selectedAnno = Utility.getSelectedAnno();
            Object result = sessione.createQuery("from DdT d where year(d.data)=" + selectedAnno+" and d.realId="+id).uniqueResult();
            if (result==null){
                return null;
            }
            DdT toReturn = (DdT)result;
            initializeDdT(toReturn, initializeBeni, initializeFattura);
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

    public DdTInterface getDdTById(int id,boolean initializeBeni, boolean initializeFattura) {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            int selectedAnno = Utility.getSelectedAnno();
            Object result = sessione.createQuery("from DdT d where year(d.data)=" + selectedAnno+" and d.id="+id).uniqueResult();
            if (result==null){
                return null;
            }
            DdTInterface toReturn = (DdTInterface)result;
            initializeDdT(toReturn, initializeBeni, initializeFattura);
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

    public List<DdT> getAllDdT(boolean initializeBeni, boolean initializeFattura) {
        return getAllDdT(initializeBeni, initializeFattura, -1, -1);
    }

    public List<DdT> getAllDdT(boolean initializeBeni, boolean initializeFattura, int offset, int limit) {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            int selectedAnno = Utility.getSelectedAnno();
            String queryString = "from DdT d where year(d.data)=" + selectedAnno + " order by d.id desc";
            Query query = sessione.createQuery(queryString);
            if (offset>-1 && limit >-1){
                query.setFirstResult(offset);
                query.setMaxResults(limit);
            }
            List<DdT> list = query.list();
            initializeDdT(list, initializeBeni, initializeFattura);
            return list;
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
    
    public List<DdT> getAllDdT(Long aziendaId) {
    	Session sessione=null;
    	try{
    		sessione=retrieveSession();
    		int selectedAnno = Utility.getSelectedAnno();
    		Query query = sessione.createQuery("from DdT d where "
    				+ "year(d.data)=" + selectedAnno+" and d.cliente.id="+aziendaId
    				+" order by d.id desc");
    		List<DdT> list = query.list();
    		return list;
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

    public List<DdTInterface> getAllDdT(AziendaInterface a, int mese,boolean initializeBeni, boolean initializeFattura) {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            int selectedAnno = Utility.getSelectedAnno();
            Query query = sessione.createQuery("from DdT d where "
                    + "year(d.data)=" + selectedAnno+" and d.cliente.id="+a.getId()+" and month(d.data)="+mese
                    +" order by d.id asc");
            List<DdTInterface> list = query.list();
            initializeDdT(list, initializeBeni, initializeFattura);
            return list;
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

	@Override
	public List<DdT> getAllDdT(AziendaInterface a, Date startDate, Date endDate) {
		Session sessione=null;
		try{
			sessione=retrieveSession();
			int selectedAnno = Utility.getSelectedAnno();
			Query query = sessione.createQuery("from DdT d where "
					+ "year(d.data)=" + selectedAnno+" and d.cliente.id="+a.getId()+" and d.data between :start and :end"
					+" order by d.id asc");
			query.setParameter("start", startDate);
			query.setParameter("end", endDate);
			List<DdT> list = query.list();
			initializeDdT(list, true, false);
			return list;
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
	
	@Override
	public List<DdTInterface> getAllDdTWithoutFattura(AziendaInterface a, Date startDate, Date endDate) {
		Session sessione=null;
		try{
			sessione=retrieveSession();
			int selectedAnno = Utility.getSelectedAnno();
			Query query = sessione.createQuery("from DdT d where "
					+ "year(d.data)=" + selectedAnno+" and d.cliente.id="+a.getId()+" and d.data between :start and :end and d.fattura=null and d.fatturabile=true"
					+" order by d.id asc");
			query.setParameter("start", startDate);
			query.setParameter("end", endDate);
			List<DdTInterface> list = query.list();
			initializeDdT(list, true, false);
			return list;
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

    public List<DdT> getAllDdTWithoutFattura(boolean initializeBeni, boolean initializeFattura) {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            int selectedAnno = Utility.getSelectedAnno();
            Query query = sessione.createQuery("from DdT d where "
                    + "year(d.data)=" + selectedAnno+" and d.fattura=null and d.fatturabile=true"
                    +" order by d.id desc");
            List<DdT> list = query.list();
            initializeDdT(list, initializeBeni, initializeFattura);
            return list;
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
    
    public void cancellaDdT(Long id) throws PersistenzaException{
        delete(getDdT(id,false,false));
    }

    public void modificaDdT(DdTInterface toModify) throws PersistenzaException{
        try{
            DdTInterface retrieved = getDdT(toModify.getRealId(),true,false);
            Utility.copyProperties(toModify, retrieved, Arrays.asList("beni"));
            List<BeneInterface> retrievedBeni = retrieved.getBeni();
            List<BeneInterface> beniModificati = toModify.getBeni();
            mergeBeni(retrievedBeni, beniModificati);
            update(retrieved);
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } catch (Exception e){
            throw new PersistenzaException("Unable to modificaDdT", e);
        }
    }
    
    protected BeneInterface getBeneById(Long id,List<BeneInterface> lst){
        if (id!=null){
            for (BeneInterface b: lst){
                if (id.equals(b.getId()))
                    return b;
            }
        }
        return null;
    }
    protected void mergeBeni(List<BeneInterface> toModify, List<BeneInterface> reference) throws PersistenzaException{
        try{
            if (reference==null || reference.isEmpty()){
                toModify.clear();
            } else{
                //delete or update
                Iterator<BeneInterface> iterator = toModify.iterator();
                while (iterator.hasNext()){
                    BeneInterface toModifyBene = iterator.next();
                    BeneInterface referenceBene = getBeneById(toModifyBene.getId(), reference);
                    if (referenceBene == null){
                        iterator.remove();
                    } else {
                        Utility.copyProperties(referenceBene, toModifyBene, Arrays.asList("prezzo","tot"));
                    }
                } 
                //add
                for (BeneInterface b : reference){
                    if (getBeneById(b.getId(), toModify)==null)
                        toModify.add(b);
                }
            }
        } catch (Exception e){
            throw new PersistenzaException("Unable to mergeBeni: ", e);
        }
    }

    public List<Bene> getBeniDdT(int id){
        Session sessione=null;
        try{
            sessione=retrieveSession();
            int selectedAnno = Utility.getSelectedAnno();
            return sessione.createQuery("select b from DdT as d join d.beni as b where year(d.data)=" + selectedAnno+" and d.id="+id).list();
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
