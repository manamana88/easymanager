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

import org.hibernate.Query;
import org.hibernate.Session;

import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.Utility;

/**
 *
 * @author vincenzo
 */
@ManagedBean(name="ddtManager")
@ApplicationScoped
public class DdTManagerImpl extends AbstractPersistenza implements DdTManager {

    public DdTManagerImpl(Properties properties) {
        super(properties);
    }

    public DdTManagerImpl() {
        super();
    }

    public int getLastDdT() {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            int selectedAnno = Utility.getSelectedAnno();
            Object result=sessione.createQuery("select max(d.id) from DdT d where year(d.data)="+selectedAnno).uniqueResult();
            return result==null?0:(Integer)result;
        } finally{
            sessione.close();
        }
    }

    public List<Bene> getBeniDdT(Long realId){
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            int selectedAnno = Utility.getSelectedAnno();
            return sessione.createQuery("select b from DdT as d join d.beni as b where year(d.data)=" + selectedAnno+" and d.realId="+realId).list();
        } finally {
            sessione.close();
        }
    }

    public boolean isEmptyDdTListMese(int mese, Azienda a) {
        return getAllDdT(a, mese,false,false).isEmpty();
    }

    public boolean existsDdT(Long realId) {
        DdT ddt = getDdT(realId,false,false);
        return ddt!=null;
    }

    public boolean existsDdTById(int id) {
        DdT ddt = getDdTById(id,false,false);
        return ddt!=null;
    }

    public void registraDdT(DdT d) throws PersistenzaException {
        DdT ddTById = getDdTById(d.getId(),false,false);
        if (ddTById!=null){
            throw new PersistenzaException("DdT with id: "+d.getId()+" yet exists in year: "+DateUtils.getYear(d.getData()));
        }
        save(d);
    }

    public DdT getDdT(Long id,boolean initializeBeni, boolean initializeFattura){
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            int selectedAnno = Utility.getSelectedAnno();
            Object result = sessione.createQuery("from DdT d where year(d.data)=" + selectedAnno+" and d.realId="+id).uniqueResult();
            if (result==null){
                return null;
            }
            DdT toReturn = (DdT)result;
            initializeDdT(toReturn, initializeBeni, initializeFattura);
            return toReturn;
        } finally {
            sessione.close();
        }
    }

    public DdT getDdTById(int id,boolean initializeBeni, boolean initializeFattura) {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            int selectedAnno = Utility.getSelectedAnno();
            Object result = sessione.createQuery("from DdT d where year(d.data)=" + selectedAnno+" and d.id="+id).uniqueResult();
            if (result==null){
                return null;
            }
            DdT toReturn = (DdT)result;
            initializeDdT(toReturn, initializeBeni, initializeFattura);
            return toReturn;
        } finally {
            sessione.close();
        }
    }

    public List<DdT> getAllDdT(boolean initializeBeni, boolean initializeFattura) {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            int selectedAnno = Utility.getSelectedAnno();
            Query query = sessione.createQuery("from DdT d where year(d.data)=" + selectedAnno+" order by d.id desc");
            List<DdT> list = query.list();
            initializeDdT(list, initializeBeni, initializeFattura);
            return list;
        } finally{
            sessione.close();
        }
    }
    
    public List<DdT> getAllDdT(Long aziendaId) {
    	Session sessione=null;
    	try{
    		sessione=sessionFactory.openSession();
    		int selectedAnno = Utility.getSelectedAnno();
    		Query query = sessione.createQuery("from DdT d where "
    				+ "year(d.data)=" + selectedAnno+" and d.cliente.id="+aziendaId
    				+" order by d.id desc");
    		List<DdT> list = query.list();
    		return list;
    	} finally{
    		sessione.close();
    	}
    }

    public List<DdT> getAllDdT(Azienda a, int mese,boolean initializeBeni, boolean initializeFattura) {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            int selectedAnno = Utility.getSelectedAnno();
            Query query = sessione.createQuery("from DdT d where "
                    + "year(d.data)=" + selectedAnno+" and d.cliente.id="+a.getId()+" and month(d.data)="+mese
                    +" order by d.id asc");
            List<DdT> list = query.list();
            initializeDdT(list, initializeBeni, initializeFattura);
            return list;
        } finally{
            sessione.close();
        }
    }

	@Override
	public List<DdT> getAllDdT(Azienda a, Date startDate, Date endDate) {
		Session sessione=null;
		try{
			sessione=sessionFactory.openSession();
			int selectedAnno = Utility.getSelectedAnno();
			Query query = sessione.createQuery("from DdT d where "
					+ "year(d.data)=" + selectedAnno+" and d.cliente.id="+a.getId()+" and d.data between :start and :end"
					+" order by d.id asc");
			query.setParameter("start", startDate);
			query.setParameter("end", endDate);
			List<DdT> list = query.list();
			initializeDdT(list, true, false);
			return list;
		} finally{
			sessione.close();
		}
	}
	
	@Override
	public List<DdT> getAllDdTWithoutFattura(Azienda a, Date startDate, Date endDate) {
		Session sessione=null;
		try{
			sessione=sessionFactory.openSession();
			int selectedAnno = Utility.getSelectedAnno();
			Query query = sessione.createQuery("from DdT d where "
					+ "year(d.data)=" + selectedAnno+" and d.cliente.id="+a.getId()+" and d.data between :start and :end and d.fattura=null"
					+" order by d.id asc");
			query.setParameter("start", startDate);
			query.setParameter("end", endDate);
			List<DdT> list = query.list();
			initializeDdT(list, true, false);
			return list;
		} finally{
			sessione.close();
		}
	}

    public List<DdT> getAllDdTWithoutFattura(boolean initializeBeni, boolean initializeFattura) {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            int selectedAnno = Utility.getSelectedAnno();
            Query query = sessione.createQuery("from DdT d where "
                    + "year(d.data)=" + selectedAnno+" and d.fattura=null"
                    +" order by d.id desc");
            List<DdT> list = query.list();
            initializeDdT(list, initializeBeni, initializeFattura);
            return list;
        } finally{
            sessione.close();
        }
    }
    
    public void cancellaDdT(Long id) throws PersistenzaException{
        delete(getDdT(id,false,false));
    }

    public void modificaDdT(DdT toModify) throws PersistenzaException{
        try{
            DdT retrieved = getDdT(toModify.getRealId(),true,false);
            Utility.copyProperties(toModify, retrieved, Arrays.asList("beni"));
            List<Bene> retrievedBeni = retrieved.getBeni();
            List<Bene> beniModificati = toModify.getBeni();
            mergeBeni(retrievedBeni, beniModificati);
            update(retrieved);
        } catch (Exception e){
            throw new PersistenzaException("Unable to modificaDdT", e);
        }
    }
    
    protected Bene getBeneById(Long id,List<Bene> lst){
        if (id!=null){
            for (Bene b: lst){
                if (id.equals(b.getId()))
                    return b;
            }
        }
        return null;
    }
    protected void mergeBeni(List<Bene> toModify, List<Bene> reference) throws PersistenzaException{
        try{
            if (reference==null || reference.isEmpty()){
                toModify.clear();
            } else{
                //delete or update
                Iterator<Bene> iterator = toModify.iterator();
                while (iterator.hasNext()){
                    Bene toModifyBene = iterator.next();
                    Bene referenceBene = getBeneById(toModifyBene.getId(), reference);
                    if (referenceBene == null){
                        iterator.remove();
                    } else {
                        Utility.copyProperties(referenceBene, toModifyBene, Arrays.asList("prezzo","tot"));
                    }
                } 
                //add
                for (Bene b : reference){
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
            sessione=sessionFactory.openSession();
            int selectedAnno = Utility.getSelectedAnno();
            return sessione.createQuery("select b from DdT as d join d.beni as b where year(d.data)=" + selectedAnno+" and d.id="+id).list();
        } finally {
            sessione.close();
        }
    }

}
