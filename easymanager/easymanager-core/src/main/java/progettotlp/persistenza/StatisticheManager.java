/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.persistenza;

import java.util.Date;
import java.util.List;
import java.util.Map;
import progettotlp.statistiche.StatisticheConfronto;
import progettotlp.statistiche.StatisticheFattura;

/**
 *
 * @author vincenzo
 */
public interface StatisticheManager {

    public Map<Date,List<StatisticheFattura>> simpleSearch(Date startDateValue, Date endDateValue, List<String> nomiAziendeSelezionate);

    public StatisticheConfronto advancedSearch(Date startDateValue, Date endDateValue, Date startDateConfrontoValue, Date endDateConfrontoValue, List<String> nomiAziendeSelezionate);
}
