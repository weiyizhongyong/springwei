package com.aura.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONUtils {
	
	/**
	 * Bean对象转JSON
	 * 
	 * @param object
	 * @param dataFormatString
	 * @return
	 */
	public static String beanToJson(Object object, String dataFormatString) {
		if (object != null) {
			if (StringUtils.isEmpty(dataFormatString)) {
				return JSONObject.toJSONString(object);
			}
			return JSON.toJSONStringWithDateFormat(object, dataFormatString);
		} else {
			return null;
		}
	}
    /**
     * json 转 List<T>
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }
	/**
	 * Bean对象转JSON数组
	 * 
	 * @param object
	 * @param
	 * @return
	 */
	public static String beanToJsons(Object object) {
		if (object != null) {
			List list=new ArrayList();
			list.add(object);
			Map map =new HashMap();
			map.put("model", list);
			return JSON.toJSONString(map,SerializerFeature.WriteMapNullValue);
		} else {
			return null;
		}
	}
	/**
	 * Bean对象转JSON
	 * @param object
	 * @return
	 */
	public static String beanToJson(Object object) {
		if (object != null) {
			String s=JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
			return JSON.toJSONString(object,SerializerFeature.WriteMapNullValue);
		} else {
			return null;
		}
	}
	/**
	 * Bean对象转JSON,带日期-时间格式数据
	 * 
	 * @param object
	 * @return
	 */
	public static String beanToJsonWithDateFormat(Object object) {
		if (object != null) {
			return JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
		} else {
			return null;
		}
	}
	/**
	 * String转JSON字符串
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String stringToJsonByFastjson(String key, String value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>(16);
		map.put(key, value);
		return beanToJson(map, null);
	}
	  /**
     * JSON 转 POJO
     */
     public static <T> T getObject(String pojo, Class<T> tclass) {
    	 Logger logger = LoggerFactory.getLogger(JSONUtils.class);
            try {
                return JSONObject.parseObject(pojo, tclass);
            } catch (Exception e) {
            	logger.error(tclass + "转 JSON 失败");
            }
            return null;
     }
	/**
	 * 将json字符串转换成对象
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static Object jsonToBean(String json, Object clazz) {
		if (StringUtils.isEmpty(json) || clazz == null) {
			return null;
		}
		return JSON.parseObject(json, clazz.getClass());
	}
	/**
	 * json字符串转map
	 * 
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String json) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		return JSON.parseObject(json, Map.class);
	}
}
