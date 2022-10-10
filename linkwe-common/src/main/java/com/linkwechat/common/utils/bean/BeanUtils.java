package com.linkwechat.common.utils.bean;

import com.esotericsoftware.reflectasm.MethodAccess;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bean 工具类
 * 
 * @author ruoyi
 */
public class BeanUtils extends org.springframework.beans.BeanUtils
{
    /** Bean方法名中属性名开始的下标 */
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    /** * 匹配getter方法的正则表达式 */
    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /** * 匹配setter方法的正则表达式 */
    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    /**
     * Bean属性复制工具方法。
     * 
     * @param dest 目标对象
     * @param src 源对象
     */
    public static void copyBeanProp(Object dest, Object src)
    {
        try
        {
            copyProperties(src, dest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象的setter方法。
     * 
     * @param obj 对象
     * @return 对象的setter方法列表
     */
    public static List<Method> getSetterMethods(Object obj)
    {
        // setter方法列表
        List<Method> setterMethods = new ArrayList<Method>();

        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();

        // 查找setter方法

        for (Method method : methods)
        {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1))
            {
                setterMethods.add(method);
            }
        }
        // 返回setter方法列表
        return setterMethods;
    }

    /**
     * 获取对象的getter方法。
     * 
     * @param obj 对象
     * @return 对象的getter方法列表
     */

    public static List<Method> getGetterMethods(Object obj)
    {
        // getter方法列表
        List<Method> getterMethods = new ArrayList<Method>();
        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();
        // 查找getter方法
        for (Method method : methods)
        {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0))
            {
                getterMethods.add(method);
            }
        }
        // 返回getter方法列表
        return getterMethods;
    }

    /**
     * 检查Bean方法名中的属性名是否相等。<br>
     * 如getName()和setName()属性名一样，getName()和setAge()属性名不一样。
     * 
     * @param m1 方法名1
     * @param m2 方法名2
     * @return 属性名一样返回true，否则返回false
     */

    public static boolean isMethodPropEquals(String m1, String m2)
    {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }



    /**
     * 通过 ASM反射 速度比 Spring BeanUtils.copyProperties(source,target) 快一倍
     * 类型不同可以转换
     * 大小写可以忽略
     * 下划线 _ 被忽略
     *
     * @param source 数据源
     * @param target  目标是数据
     * @param <T>
     * @return
     */
    public static <T> T copyPropertiesASM( Object source, Object target ) {
        MethodAccess sourceMethodAccess = CacheMethodAccess.getMethodAccess(source.getClass());
        MethodAccess targetMethodAccess = CacheMethodAccess.getMethodAccess(target.getClass());
        Map<String, String> sourceGet = CacheAsmFiledMethod.getMethod("get", source.getClass());
        Map<String, String> targetSet = CacheAsmFiledMethod.getMethod("set", target.getClass());
        CacheFieldMap.getFieldMap(target.getClass()).keySet().forEach((it) -> {
            String sourceIndex = sourceGet.get(it);
            if (sourceIndex != null) {
                Object value = sourceMethodAccess.invoke(source, sourceIndex);
                String setIndex = targetSet.get(it);
                targetMethodAccess.invoke(target, setIndex, value);
            }
        });
        return (T) target;
    }

    /**
     *  模仿Spring中 BeanUtils.copyProperties(source,target)
     *类 型不同不可以转换
     *  但是
     * 大小写可以忽略
     * 下划线 _ 被忽略
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T copyPropertiesignoreOther(Object source, Object target) {
        Map<String, Field> sourceMap = CacheFieldMap.getFieldMap(source.getClass());
        CacheFieldMap.getFieldMap(target.getClass()).values().forEach((it) -> {
            Field field = sourceMap.get(it.getName().toLowerCase().replace("_", ""));
            if (field != null) {
                it.setAccessible(true);
                field.setAccessible(true);
                try {
                    it.set(target,field.get(source));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return (T) target;
    }

    private static class CacheAsmFiledMethod {
        private static Map<String, Map<String, String>> cacheGetMethod = new HashMap<>();
        private static Map<String, Map<String, String>> cacheSetMethod = new HashMap<>();

        private static Map<String, String> getMethod(String type, Class clazz) {
            MethodAccess methodAccess = CacheMethodAccess.getMethodAccess(clazz);
            Map<String, Field> allFields = CacheFieldMap.getFieldMap(clazz);
            Map<String, String> result = null;
            if (type.equals("get")) {
                result = cacheGetMethod.get(clazz.getName());
            } else if (type.equals("set")) {
                result = cacheSetMethod.get(clazz.getName());
            }
            if (result == null) {
                synchronized (CacheAsmFiledMethod.class) {
                    if (result == null) {
                        Map<String, String> set = new HashMap<>();
                        Map<String, String> get = new HashMap<>();
                        allFields.values().forEach((it) -> {
                            //判断是否是静态
                            if (!Modifier.isStatic(it.getModifiers())) {
                                //首字母大写
                                char[] f = it.getName().toCharArray();
                                f[0] -= 32;
                                String fieldName = new String(f);
                                get.put(fieldName.toLowerCase().replace("_", ""), "get" + fieldName);
                                set.put(fieldName.toLowerCase().replace("_", ""), "set" + fieldName);
                            }
                        });
                        cacheGetMethod.put(clazz.getName(), get);
                        cacheSetMethod.put(clazz.getName(), set);
                        if (type.equals("get")) {
                            result = cacheGetMethod.get(clazz.getName());
                        } else if (type.equals("set")) {
                            result = cacheSetMethod.get(clazz.getName());
                        }
                    }
                }
            }
            return result;
        }
    }

    private static class SingleClass {
        private static Map<String, Object> cacheObject = new HashMap<>();

        private SingleClass() {
        }

        private static <T> T getObject(Class<T> clazz) {
            T result = (T) cacheObject.get(clazz.getName());
            if (result == null) {
                synchronized (SingleClass.class) {
                    if (result == null) {
                        try {
                            cacheObject.put(clazz.getName(), clazz.newInstance());
                            result = (T) cacheObject.get(clazz.getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return result;
        }
    }

    private static class CacheMethodAccess {

        private CacheMethodAccess() {
        }

        private static Map<String, MethodAccess> cache = new HashMap<>();

        private static MethodAccess getMethodAccess(Class clazz) {
            MethodAccess result = cache.get(clazz.getName());
            if (result == null) {
                synchronized (CacheMethodAccess.class) {
                    if (result == null) {
                        cache.put(clazz.getName(), MethodAccess.get(clazz));
                        result = cache.get(clazz.getName());
                    }
                }
            }
            return result;
        }
    }

    private static class CacheFieldMap {
        private static Map<String, Map<String, Field>> cacheMap = new HashMap<>();

        private static Map<String, Field> getFieldMap(Class clazz) {
            Map<String, Field> result = cacheMap.get(clazz.getName());
            if (result == null) {
                synchronized (CacheFieldMap.class) {
                    if (result == null) {
                        Map<String, Field> fieldMap = new HashMap<>();
                        for (Field field : clazz.getDeclaredFields()) {
                            fieldMap.put(field.getName().toLowerCase().replace("_", ""), field);
                        }
                        cacheMap.put(clazz.getName(), fieldMap);
                        result = cacheMap.get(clazz.getName());
                    }
                }
            }
            return result;
        }
    }


}
