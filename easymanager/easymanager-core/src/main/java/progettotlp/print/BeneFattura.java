/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.print;

import java.util.Date;

import progettotlp.facilities.DateUtils;
import progettotlp.interfaces.BeneInterface;

/**
 *
 * @author Vincenzo
 */
public class BeneFattura implements Comparable<BeneFattura>{
    private BeneInterface bene;
    private Date data;
    private Integer id;

    public BeneFattura(BeneInterface bene, Date data, Integer id) {
        this.bene = bene;
        this.data = data;
        this.id = id;
    }

    public BeneInterface getBene() {
        return bene;
    }

    public void setBene(BeneInterface bene) {
        this.bene = bene;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String generateRif(){
        int day = DateUtils.getDay(data);
        int month = DateUtils.getMonth(data);
        return id+" del "+day+"/"+month;
    }

    public int compareTo(BeneFattura o) {
        throw new UnsupportedOperationException();/*
        SimpleDateFormat parser=new SimpleDateFormat("dd/MM/yyyy");
        try {
            return parser.parse(zeroFill(data.toString())).compareTo(parser.parse(zeroFill(o.toString())));
        } catch (ParseException ex) {
            throw new ClassCastException();
        }*/
    }

    
}
