/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.facilities;

import org.junit.Ignore;
import progettotlp.test.AnnualTest;

/**
 *
 * @author vincenzo
 */
@Ignore
public class ReflectionClassTest {
 
    private Integer id;
    private Boolean ok;
    private Boolean hidden;

    public ReflectionClassTest(Boolean hidden) {
        this.hidden = hidden;
    }
    
    public Integer getId(){
        return id;
    }
    
    public void setId(Integer id){
        this.id=id;
    }
    
    public Boolean getOk(){
        return true;
    }
    
    public void setOk(Boolean ok){
        this.ok=ok;
    }

    public Boolean getHidden() {
        return hidden;
    }

    private void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }
    
}
