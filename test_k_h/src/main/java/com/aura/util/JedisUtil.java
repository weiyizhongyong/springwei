package com.aura.util;

import java.util.Collection;
import java.util.Set;
import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//@Component
public class JedisUtil {
    
    private static JedisPool jedisPool = null;

    //volatile修饰的变量对于其他线程是可见的，但是变量的操作不是原子操作。只有基本类型操作(赋值，读取)才是原子性操作。其他类型变量或基本类型的运算操作都是非原子操作
    private static  Jedis jedis = null;//volatile

    /**
     * 初始化非切片池
     */
    //private void initialPool()
    static {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(false);

        jedisPool = new JedisPool(config,"172.16.22.244",6379);
    }

    
    public JedisUtil(){}

    public static Jedis getJedis(){
        if (jedis ==null){
            synchronized (Jedis.class){
                if (jedis ==null){
                    jedis = jedisPool.getResource();
                }
            }
        }
        return jedis;
    }

    /*public static JedisPool jedisPool{
        if (jedisPool ==null){
            synchronized (JedisPool.class){
                if (jedisPool==null){
                    jedisPool = applicationContext.getBean(JedisPool.class);
                }
            }
        }
        return jedisPool;
    }*/

   /* @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(JedisUtil.applicationContext == null){
            JedisUtil.applicationContext  = applicationContext; //初始化 spring applicationContext
        }
    }*/

    /**
     * 根据key查看是否存在
     * @param key
     * @return
     */
    public static boolean hasKey(String key){
    	try {
            jedis = jedisPool.getResource();
            jedis.exists(key);
            return true;
        } catch (Exception e) {
        	if (jedis != null) {
        		jedisPool.returnBrokenResource(jedis);
        		jedis = null;
        	}
            e.printStackTrace();
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * 向缓存中设置字符串内容
     * @param key key
     * @param value value
     * @return
     * @throws Exception
     */
    public static boolean  set(String key,String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            return true;
        } catch (Exception e) {
        	if (jedis != null) {
        		jedisPool.returnBrokenResource(jedis);
        		jedis = null;
        	}
            e.printStackTrace();
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * 向缓存中设置对象
     * @param key
     * @param value
     * @return
     */
    public static boolean  set(String key,Object value){
        Jedis jedis = null;
        try {
            String objectJson = JSON.toJSONString(value);
            jedis = jedisPool.getResource();
            jedis.set(key, objectJson);
            return true;
        } catch (Exception e) {
            if (jedis != null) {
            	jedisPool.returnBrokenResource(jedis);
            	jedis = null;
            }
            e.printStackTrace();
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }
    /**
     * 设置key -value 形式数据
     * @param key
     * @param value
     * @return
     */
//    public static String set(String key,String value){
//        String result =  getJedis().set(key,value);
//        return result;
//    }

    /**
     * 设置 一个过期时间
     * @param key
     * @param value
     * @param timeOut 单位秒
     * @return
     */
    public static String set(String key,String value,int timeOut){
    	try {
            jedis = jedisPool.getResource();
            return jedis.setex(key,timeOut,value);
        } catch (Exception e) {
        	if (jedis != null) {
        		jedisPool.returnBrokenResource(jedis);
        		jedis = null;
        	}
            e.printStackTrace();
            return "";
        }finally{
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 根据key获取value
     * @param key
     * @return
     */
//    public static String getByKey(String key){
//        return getJedis().get(key);
//    }

    /**
     * 根据key 获取内容
     * @param key
     * @return
     */
    public static String getByKey(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            return value;
        } catch (Exception e) {
            if (jedis != null) {
            	jedisPool.returnBrokenResource(jedis);
            	jedis = null;
            }
            return "";
        }finally{
            jedisPool.returnResource(jedis);
        }
    }


    /**
     * 根据key 获取对象
     * @param key
     * @return
     */
    public static <T> T get(String key,Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            return JSON.parseObject(value, clazz);
        } catch (Exception e) {
            if (jedis != null) {
            	jedisPool.returnBrokenResource(jedis);
            	jedis = null;
            }
            e.printStackTrace();
            return null;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }
    /**
     * 根据通配符获取所有匹配的key
     * @param pattern
     * @return
     */
    public static Set<String> getKesByPattern(String pattern){
    	Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.keys(pattern);
        } catch (Exception e) {
            if (jedis != null) {
            	jedisPool.returnBrokenResource(jedis);
            	jedis = null;
            }
        }finally{
            jedisPool.returnResource(jedis);
        }
        return null;
    }

    /**
     * 删除缓存中得对象，根据key
     * @param key
     * @return
     */
    public static boolean del(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
            return true;
        } catch (Exception e) {
            if (jedis != null) {
            	jedisPool.returnBrokenResource(jedis);
            	jedis = null;
            }
            e.printStackTrace();
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 模糊匹配KEY，获取key集合后批量删除缓存中得对象
     * @param
     * @return
     */
    public static boolean deleteKeys(String keys){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<String> stringSet = jedis.keys(keys);
            if(stringSet.size()==0){
            	del(keys);
            }
            for(String key:stringSet){
            	jedis.del(key);
            }
            return true;
        } catch (Exception e) {
            if (jedis != null) {
            	jedisPool.returnBrokenResource(jedis);
            	jedis = null;
            }
            e.printStackTrace();
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * 根据key获取过期时间
     * @param key
     * @return
     */
    public static long getTimeOutByKey(String key){
    	Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.ttl(key);
        } catch (Exception e) {
            if (jedis != null) {
            	jedisPool.returnBrokenResource(jedis);
            	jedis = null;
            }
            e.printStackTrace();
        }finally{
            jedisPool.returnResource(jedis);
        }
        return 0L;
    }
    
    /**
     * 清空数据 【慎用啊！】
     */
    public static void flushDB(){
        getJedis().flushDB();
    }
    /**
     * 返回所有的keys集合
     * @return
     */
    public  static Set<String> getkeys(){
    	Set  set=getJedis().keys("*");
    	return set;
    } 
    /**
     * 刷新过期时间
     * @param key
     * @param timeOut
     * @return
     */
    public static long refreshLiveTime(String key,int timeOut){
        return getJedis().expire(key,timeOut);
    }
    
}
