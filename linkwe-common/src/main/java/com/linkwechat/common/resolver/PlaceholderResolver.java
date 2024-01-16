package com.linkwechat.common.resolver;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.linkwechat.common.utils.ReflectionUtils;
import org.aspectj.apache.bcel.classfile.Constant;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 占位符解析器
 *  https://blog.csdn.net/zidieq/article/details/125016123
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-13 1:42 PM
 */
public class PlaceholderResolver {
    /**
     * 默认前缀占位符
     */
    private static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * 默认后缀占位符
     */
    private static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    /**
     * 默认单例解析器
     */
    private static PlaceholderResolver defaultResolver = new PlaceholderResolver();

    /**
     * 占位符前缀
     */
    private String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;

    /**
     * 占位符后缀
     */
    private String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;


    private PlaceholderResolver() {
    }

    private PlaceholderResolver(String placeholderPrefix, String placeholderSuffix) {
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
    }

    /**
     * 获取默认的占位符解析器，即占位符前缀为"${", 后缀为"}"
     *
     * @return
     */
    public static PlaceholderResolver getDefaultResolver() {
        return defaultResolver;
    }

    public static PlaceholderResolver getResolver(String placeholderPrefix, String placeholderSuffix) {
        return new PlaceholderResolver(placeholderPrefix, placeholderSuffix);
    }

    /**
     * 解析带有指定占位符的模板字符串，默认占位符为前缀：${  后缀：}<br/><br/>
     * 如：template = category:${}:product:${}<br/>
     * values = {"1", "2"}<br/>
     * 返回 category:1:product:2<br/>
     *
     * @param content 要解析的带有占位符的模板字符串
     * @param values  按照模板占位符索引位置设置对应的值
     * @return
     */
    public String resolve(String content, String[] values) {
        int start = content.indexOf(this.placeholderPrefix);
        if (start == -1) {
            return content;
        }
        //值索引
        int valueIndex = 0;
        StringBuilder result = new StringBuilder(content);
        while (start != -1) {
            int end = result.indexOf(this.placeholderSuffix);
            String replaceContent = values[valueIndex++];
            result.replace(start, end + this.placeholderSuffix.length(), replaceContent);
            start = result.indexOf(this.placeholderPrefix, start + replaceContent.length());
        }
        return result.toString();
    }

    /**
     * 解析带有指定占位符的模板字符串，默认占位符为前缀：${  后缀：}<br/><br/>
     * 如：template = category:${}:product:${}<br/>
     * values = {"1", "2"}<br/>
     * 返回 category:1:product:2<br/>
     *
     * @param content 要解析的带有占位符的模板字符串
     * @param values  按照模板占位符索引位置设置对应的值
     * @return
     */
    public String resolve(String content, Object... values) {
        return resolve(content, Stream.of(values).map(String::valueOf).toArray(String[]::new));
    }

    /**
     * 根据替换规则来替换指定模板中的占位符值
     *
     * @param content 要解析的字符串
     * @param rule    解析规则回调
     * @return
     */
    public String resolveByRule(String content, Function<String, String> rule) {
        int start = content.indexOf(this.placeholderPrefix);
        if (start == -1) {
            return content;
        }
        StringBuilder result = new StringBuilder(content);
        while (start != -1) {
            int end = result.indexOf(this.placeholderSuffix, start);
            //获取占位符属性值，如${id}, 即获取id
            String placeholder = result.substring(start + this.placeholderPrefix.length(), end);
            //替换整个占位符内容，即将${id}值替换为替换规则回调中的内容
            String replaceContent = placeholder.trim().isEmpty() ? "" : rule.apply(placeholder);
            result.replace(start, end + this.placeholderSuffix.length(), replaceContent);
            start = result.indexOf(this.placeholderPrefix, start + replaceContent.length());
        }
        return result.toString();
    }

    /**
     * 替换模板中占位符内容，占位符的内容即为map key对应的值，key为占位符中的内容。<br/><br/>
     * 如：content = product:${id}:detail:${did}<br/>
     * valueMap = id -> 1; pid -> 2<br/>
     * 经过解析返回 product:1:detail:2<br/>
     *
     * @param content  模板内容。
     * @param valueMap 值映射
     * @return 替换完成后的字符串。
     */
    public String resolveByMap(String content, final Map<String, Object> valueMap) {
        return resolveByRule(content, placeholderValue -> String.valueOf(valueMap.get(placeholderValue)));
    }

    /**
     * 根据properties文件替换占位符内容
     *
     * @param content
     * @param properties
     * @return
     */
    public String resolveByProperties(String content, final Properties properties) {
        return resolveByRule(content, properties::getProperty);
    }

    /**
     * 根据对象中字段路径(即类似js访问对象属性值)替换模板中的占位符 <br/><br/>
     * 如 content = product:${id}:detail:${detail.id} <br/>
     * obj = Product.builder().id(1).detail(Detail.builder().id(2).build()).build(); <br/>
     * 经过解析返回 product:1:detail:2 <br/>
     *
     * @param content 要解析的内容
     * @param obj     填充解析内容的对象(如果是基本类型，则所有占位符替换为相同的值)
     * @return
     */
    @SuppressWarnings("unchecked")
    public String resolveByObject(String content, final Object obj) {
        if (obj instanceof Map) {
            return resolveByMap(content, (Map<String, Object>) obj);
        }
        ReflectUtil.getFieldValue("","");
        return resolveByRule(content, placeholderValue -> String.valueOf(ReflectionUtils.getValueByFieldPath(obj, placeholderValue)));
    }


    /**
     * 将List<Map<String,Object>>形式的对象中的下划线的键转成驼峰大小写的键的形式
     * @param obj
     * @return
     */
    public static List<Map<String, Object>> convertUnderlineToCamelCaseObject(Object obj) {
        List<Map<String, Object>> rList = new ArrayList<>();

        if (obj instanceof List) {
            List list = (List) obj;
            for (Object o : list) {
                if (o instanceof Map) {
                    Map m = (Map) o;
                    Map<String, Object> rMap = new HashMap<>();
                    Set<Map.Entry> set = m.entrySet();
                    for (Map.Entry entry : set) {
                        String key = StrUtil.toString(entry.getKey());
                        if (key != null && key.indexOf("_") > -1) {
                            String nkey = underlineToCamelCase(key);
                            rMap.put(nkey, entry.getValue());
                        } else {
                            rMap.put(key, entry.getValue());
                        }
                    }
                    rList.add(rMap);

                }
            }
        }
        return rList;
    }
    /**
     * 将下划线形式转成大小写的形式
     * @param text
     * @return
     */
    public static String underlineToCamelCase(String text) {
        String[] words = text.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toLowerCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }
        return builder.toString();
    }



    /**
     * 把long型的秒数转成localDateTime
     * @param seconds
     * @return
     */
    public static LocalDateTime convertLongSecondsToLocalDateTime(Long seconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(seconds*1000), ZoneId.systemDefault());
    }

}


