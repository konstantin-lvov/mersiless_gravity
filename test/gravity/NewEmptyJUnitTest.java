/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravity;

import java.util.ArrayList;
import junit.framework.*;

/**
 *
 * @author fromchaos
 */
public class NewEmptyJUnitTest extends TestCase{
    
    public void testNewEmptyJUnitTest() {
        ArrayList cY = new ArrayList();
        CalPath cp = new CalPath();
        System.out.println(cY.size());
        for(Object x: cY){
            System.out.println(x.toString());
        }
    }
    
}
