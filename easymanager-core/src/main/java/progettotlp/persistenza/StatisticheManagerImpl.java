package progettotlp.persistenza;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import progettotlp.classes.DdT;
import progettotlp.classes.Fattura;
import progettotlp.interfaces.FatturaInterface;
import progettotlp.statistiche.StatisticheConfronto;
import progettotlp.statistiche.StatisticheFattura;

/**
 *
 * @author vincenzo
 */
public class StatisticheManagerImpl extends AbstractPersistenza implements StatisticheManager{

    private static Logger logger = LoggerFactory.getLogger(StatisticheManagerImpl.class);

    public StatisticheManagerImpl(Properties properties) {
        super(properties);
    }

    public StatisticheManagerImpl() {
        super();
    }
    
    @Override
    public Set<Integer> getAvailableYears() {
    	Session sessione=null;
    	try{
    		sessione=retrieveSession();
    		Criteria query= sessione.createCriteria(DdT.class);
    		query.setProjection(Projections.distinct(Projections.sqlProjection("year(data) as year", new String[] {"year"}, new Type[] {new IntegerType()})));
    		List<Integer> list = query.list();
    		Set<Integer> result = new TreeSet<>(Collections.reverseOrder());
    		result.addAll(list);
    		return result;
    	} catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally {
    	    if(sessione!=null) {
                sessione.close();
            }
    	}
    }

    public Map<Date,List<StatisticheFattura>> simpleSearch(Date startDateValue, Date endDateValue, List<String> nomiAziendeSelezionate) {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            Criteria query= sessione.createCriteria(Fattura.class);
            query.addOrder(Order.asc("emissione"));
            query.add(Restrictions.ge("emissione", startDateValue));
            query.add(Restrictions.le("emissione", endDateValue));
            Criterion aziende = createCriterionFromList("c.nome", nomiAziendeSelezionate, CONJUNCTION_TYPE.OR);
            if (aziende!=null){
                query.createAlias("cliente", "c").add(aziende);
            }
            List<Fattura> list = query.list();
            return adaptResult(list,sessione);
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally {
            if(sessione!=null) {
                sessione.close();
            }
        }
    }

    private Map<Date,List<StatisticheFattura>> adaptResult(List<Fattura> list, Session sessione){
        Map<Date, List<StatisticheFattura>> result = new HashMap<Date, List<StatisticheFattura>>();
        if (list != null) {
            for (FatturaInterface f : list) {
            	BigDecimal sumBeni = (BigDecimal) sessione.createQuery("select sum(b.qta) from Fattura as f join f.ddt as d join d.beni as b where f.realId=" + f.getRealId()).uniqueResult();
                List<StatisticheFattura> get = result.get(f.getEmissione());
                if (get == null) {
                    get = new ArrayList<StatisticheFattura>();
                    result.put(f.getEmissione(), get);
                }
                get.add(new StatisticheFattura(f, sumBeni == null ? new BigDecimal("0") : sumBeni));
            }
        }
        return result;
    }

    public StatisticheConfronto advancedSearch(Date startDateValue, Date endDateValue, Date startDateConfrontoValue, Date endDateConfrontoValue, List<String> nomiAziendeSelezionate) {
        return new StatisticheConfronto(
                simpleSearch(startDateValue, endDateValue, nomiAziendeSelezionate),
                simpleSearch(startDateConfrontoValue, endDateConfrontoValue, nomiAziendeSelezionate));
    }

    private Criterion retrieveSimpleEmissioneCriterion(Date startDateValue, Date endDateValue){
        return Restrictions.and(Restrictions.ge("emissione", startDateValue),Restrictions.le("emissione", endDateValue));
    }

    private Criterion retrieveAdvancedEmissioneCriterion(Date startDateValue, Date endDateValue,Date startDateConfrontoValue, Date endDateConfrontoValue){
        return Restrictions.or(retrieveSimpleEmissioneCriterion(startDateValue, endDateValue),retrieveSimpleEmissioneCriterion(startDateConfrontoValue, endDateConfrontoValue));
    }
}