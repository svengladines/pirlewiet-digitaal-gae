
package be.pirlewiet.registrations.utils;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.log4j.Logger;

public class PropertyChecker {
    
    private static final Logger LOGGER = Logger.getLogger(PropertyChecker.class.getName());
    
    //NestedNullException gecatchet om een eigen gekozen waarde te kunnen teruggeven
    //in geval van null objecten
    public static String getAsString(Object bean, String property, String nullReplace){
        String propertyValue = nullReplace;
        try {
            LOGGER.debug("Property " + property + " is null? " + (BeanUtils.getNestedProperty(bean, property)==null));
            if(BeanUtils.getNestedProperty(bean, property)!=null){
                propertyValue = BeanUtils.getNestedProperty(bean, property);
            }            
        } catch (NestedNullException ex) {
            LOGGER.info("propertyValue: " + propertyValue);
            LOGGER.info("Er is een NULL-waarde in het nested path van property " + property + " voor " + bean.toString() + ". De property kreeg daarom een lege String toegekend.");
        } catch (IllegalAccessException ex) {
            LOGGER.debug(ex.getCause());
        } catch (InvocationTargetException ex) {
            LOGGER.debug(ex.getCause());
        } catch (NoSuchMethodException ex) {
            LOGGER.debug(ex.getCause());;
        }                   
        return propertyValue;
     }   
}