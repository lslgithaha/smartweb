package com.lsl.smartweb.core;


import com.lsl.smartweb.annotion.Inter;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Create by LSL on 2018\5\9 0009
 * 描述：赖赖注入实现类
 * 版本：1.0.0
 */
public final class InterHelper {
    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (beanMap != null && !beanMap.isEmpty()) {
            for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
                Class<?> key = entry.getKey();
                Object value = entry.getValue();
                Field[] fields = key.getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Inter.class)) {
                            Class<?> type = field.getType();
                            Object o = beanMap.get(type);
                            if (o != null) {
                                BeanFactory.setFiled(value, field, o);
                            }
                        }
                    }
                }
            }
        }
    }

}
