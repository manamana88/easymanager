/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.statistiche;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vincenzo
 */
public class StatisticheConfronto {

    private Map<Date,List<StatisticheFattura>> statistichePrimoPeriodo;
    private Map<Date,List<StatisticheFattura>> statisticheSecondoPeriodo;

    public StatisticheConfronto() {
    }

    public StatisticheConfronto(Map<Date, List<StatisticheFattura>> statistichePrimoPeriodo, Map<Date, List<StatisticheFattura>> statisticheSecondoPeriodo) {
        this.statistichePrimoPeriodo = statistichePrimoPeriodo;
        this.statisticheSecondoPeriodo = statisticheSecondoPeriodo;
    }

    public Map<Date, List<StatisticheFattura>> getStatistichePrimoPeriodo() {
        return statistichePrimoPeriodo;
    }

    public void setStatistichePrimoPeriodo(Map<Date, List<StatisticheFattura>> statistichePrimoPeriodo) {
        this.statistichePrimoPeriodo = statistichePrimoPeriodo;
    }

    public Map<Date, List<StatisticheFattura>> getStatisticheSecondoPeriodo() {
        return statisticheSecondoPeriodo;
    }

    public void setStatisticheSecondoPeriodo(Map<Date, List<StatisticheFattura>> statisticheSecondoPeriodo) {
        this.statisticheSecondoPeriodo = statisticheSecondoPeriodo;
    }
}
