package org.luvx.coding.jdk;

import org.junit.jupiter.api.Test;
import org.luvx.coding.jdk.reflect.IntrospectorCase;
import org.luvx.coding.jdk.common.User;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * 内省
 */
public class IntrospectorCaseTest {

    @Test
    public void run() throws IntrospectionException {
        // 获取Bean的所有属性的PropertyDescriptor
        BeanInfo bi = Introspector.getBeanInfo(User.class);
        PropertyDescriptor[] pd = bi.getPropertyDescriptors();

        // 指定属性的PropertyDescriptor
        PropertyDescriptor pdAge = new PropertyDescriptor("age", User.class);

        Method setMethod = pdAge.getReadMethod();
        Method getMethod = pdAge.getWriteMethod();
    }


    @Test
    public void run01() throws Exception {
        User user = new User("foo", "1121", 20);

        String propertyName = "userName";

        Object obj = IntrospectorCase.getProperty(user, propertyName);
        System.out.println(obj);

        Object value = "bar";
        IntrospectorCase.setProperty(user, propertyName, value);
        System.out.println(user.getUserName());
    }
}
