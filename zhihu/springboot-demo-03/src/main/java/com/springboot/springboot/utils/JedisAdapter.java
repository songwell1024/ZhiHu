package com.springboot.springboot.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author WilsonSong
 * @date 2018/6/1
 */
@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool;

    public static void print(int index, Object object) {
        System.out.println(String.format("%d, %s", index, object.toString()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }


    //增加
    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("Redis添加数据异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("Redis删除数据异常");
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    //查询数量
    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("Redis统计数量异常" + e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("Redis查询异常" + e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return false;
    }

    public long lpush(String key, String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e){
            logger.error("Redis队列添加异常" + e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public List<String> lrange(String key, int start, int end){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        }catch (Exception e){
            logger.error("取值发生异常" + e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }



    public List<String>  brpop(int timeout, String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.brpop(timeout,key);
        }catch (Exception e){
            logger.error("Redis队列弹出数据异常" + e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    //从线程池里获取一个jedis的线程
    public Jedis getJedis(){
        return pool.getResource();
    }
    //jedis的事务  transaction是jedis中的事务管理对象
    public Transaction multi(Jedis jedis){
        try{
            return jedis.multi();  //开启事务
        }catch (Exception e){
            logger.error("事务开启异常" + e.getMessage());
        }
        return null;
    }

    //multi开启事务执行之后再zsort中返回的是list
    public List<Object> exec(Transaction tx, Jedis jedis){
        try{
            return tx.exec();   //执行事务
        }catch (Exception e){
            logger.error("事务启动异常" +  e.getMessage());
        }finally {
            //当事务不为空的时候要将其关闭
            if (tx != null){
                try{
                    tx.close();
                }catch (IOException ioe){
                    logger.error("发生异常" + ioe.getMessage());
                }
            }
            //把jedis关闭
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    //把用户加到关注的列表中
    public long zadd(String key, double score, String value){
        Jedis jedis  = null;
        try{
            jedis  = pool.getResource();
            return jedis.zadd(key,score,value);
        }catch (Exception e){
            logger.error("添加用户关注失败" + e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public Set<String> zrevrange(String key, int start, int end){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        }catch (Exception e){
            logger.error("发生异常" +  e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key, String member){       //Double是类，是double的实现类，double是属性
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }

//    public static void main(String[] args){
//        Jedis jedis = new Jedis("redis://localhost:6379/9");      //连接6379端口的9号数据库
//
//        jedis.flushDB();  //把DB的数据删除
//
//        //文本 get  set
//        jedis.set("hello","world");
//        print(1,jedis.get("hello"));
//        jedis.rename("hello","newHello");
//        print(1,jedis.get("newHello"));
//        jedis.setex("hello2",15,"world");  //15秒后过期 验证码，短信验证等等
//
//        //数值加减
//        jedis.set("pv", "100");
//        jedis.incr("pv");      //加1
//        print(2, jedis.get("pv"));
//        jedis.incrBy("pv",5);  //加5
//        print(2,jedis.get("pv"));
//        jedis.decr("pv");
//        print(2,jedis.get("pv"));
//        jedis.decrBy("pv", 5);
//        print(2, jedis.get("pv"));
//
//        print(3,jedis.keys("*"));
//
//        //list命令
//        String listName = "list";
//        jedis.del(listName);  //存在的话就删掉
//        for (int i=0; i<10; ++i){
//            jedis.lpush(listName, "a"+String.valueOf(i));
//        }
//        print(4, jedis.lrange(listName, 0 , 9));   //取出从0到9个
//        print(4, jedis.lrange(listName, 1 , 6));   //取出从0到9个
//        print(4, jedis.llen(listName));         //list 的长度
//        print(4, jedis.lpop(listName));        //list中的值弹出来，弹出一个，弹出之后就没有这个弹出的值了
//        print(4, jedis.llen(listName));    //弹出来之后长度改变
//        print(4, jedis.lindex(listName ,3));  //取第三个值
//        print(4, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a5", "aaa") ); //在a5之后插入 输出插入后的大小
//        print(4, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a5","qqq"));   //在a5之前插入
//        print(4, jedis.lrange(listName, 0, 10));
//
//        //hash
//        String userKey = "user1";
//        jedis.hset(userKey, "name", "a");
//        jedis.hset(userKey,"age", "1");
//        jedis.hset(userKey, "id","1");
//        print(5,jedis.hget(userKey,"name"));
//        print(5,jedis.hgetAll(userKey));
//        jedis.hdel(userKey,"id");
//        print(5,jedis.hgetAll(userKey));
//        print(5,jedis.hexists(userKey,"age"));
//        print(5,jedis.hkeys(userKey)); //key
//        print(5,jedis.hvals(userKey));  //value
//        jedis.hsetnx(userKey, "school", "seu");  //不存在才写入
//        jedis.hsetnx(userKey, "name","b");  //name 已经存在，无效
//        print(5,jedis.hgetAll(userKey));
//
//        //set  集合中是没有重复元素的
//        String  setKey1 = "commentLike1";
//        String  setKey2 = "commentLike2";
//
//        for (int i = 0 ;i<10; ++i){
//            jedis.sadd(setKey1, String.valueOf(i));
//            jedis.sadd(setKey2, String.valueOf(i*i));
//        }
//
//        print(6, jedis.smembers(setKey1));
//        print(6,jedis.smembers(setKey2));
//        print(6,jedis.sunion(setKey1,setKey2));   //求集合的并集
//        print(6,jedis.sdiff(setKey1,setKey2));    //求集合的差异值
//        print(6,jedis.sinter(setKey1, setKey2));  //求集合的交集
//        print(6,jedis.sismember(setKey1, "12"));   //查是不是集合中的元素
//        jedis.srem(setKey1,"5");   //把5删除
//        print(6, jedis.smembers(setKey1));
//        jedis.smove(setKey2, setKey1, "25"); //从setKey2中把25移动到setKey1,2中就没有25，1中有
//        print(6,jedis.smembers(setKey1));
//        print(6, jedis.smembers(setKey2));
//        print(6, jedis.scard(setKey1));
//        print(6,jedis.srandmember(setKey1,3));  //从setKey1中随机的取三个值
//
//        //优先队列 默认从小到大排序的 Sorted set
//        String rankKey = "rankKey";
//        jedis.zadd(rankKey,1, "a");
//        jedis.zadd(rankKey,10,"b");
//        jedis.zadd(rankKey,5,"c");
//        jedis.zadd(rankKey,12,"d");
//        jedis.zadd(rankKey,100,"e");
//        print(7,jedis.zrange(rankKey,0,4));    //打印的是member
//        print(7, jedis.zcount(rankKey,20, 100));    //打印20-100之间有多少个人
//        print(7, jedis.zscore(rankKey,"a"));
//        jedis.zincrby(rankKey,98,"a");
//        print(7,jedis.zscore(rankKey,"a"));
//        jedis.zincrby(rankKey,10,"aa");       //当不存在的时候也会添加进去的
//        print(7,jedis.zscore(rankKey,"aa"));
//        print(7,jedis.zrange(rankKey,0, 100));
//        print(7,jedis.zrange(rankKey,1,3)); //默认从小到大取前1到3
//        print(7, jedis.zrevrange(rankKey,1,3));   //从大到小取第1到3
//        for(Tuple tuple :jedis.zrangeByScoreWithScores(rankKey,20,100)){
//            print(7, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
//        }
//
//        print(7,jedis.zrank(rankKey,"a"));   //当前排名
//        print(7,jedis.zrevrank(rankKey,"a")); //反向的当前排名
//
//
//        String setKey = "zset";
//        jedis.zadd(setKey,1,"a");
//        jedis.zadd(setKey,1,"b");
//        jedis.zadd(setKey,1,"c");
//        jedis.zadd(setKey,1,"d");
//        jedis.zadd(setKey,1,"e");
//        print(7,jedis.zlexcount(setKey,"-","+"));  //集合中的所有元素
//        print(7,jedis.zlexcount(setKey,"[b","+"));  //集合中的从b开始到集合结束的所有元素
//        print(7,jedis.zlexcount(setKey,"(b","(d"));  //集合中的从b开始到d结束的所有元素不包含边界
//        jedis.zrem(setKey,"b");
//        print(7,jedis.zrange(setKey,0,10));
//        jedis.zremrangeByLex(setKey,"(c","+");     //把C以上的全部删除
//        print(7,jedis.zrange(setKey,0,10));
//
//
//        //连接池
//        JedisPool pool = new JedisPool();
//        for (int i = 0; i<2; ++i){
//            Jedis j = pool.getResource();
//            j.set("pp", "100");
//            print(8,j.get("pp"));
//            j.close();     //如果关掉还给连接池的话就卡死了，只输出8个，因为你自己独占连接池了
//        }
//
//        //缓存实例
//        User user = new User();
//        user.setName("aaq");
//        user.setPassword("11");
//        user.setHead_url("a.png");
//        user.setSalt("salt");
//        user.setId(1);
//        jedis.set("user1", JSONObject.toJSONString(user));     //把对象序列化然后放在json串中
//        print(9, jedis.get("user1"));
//
//        String value  = jedis.get("user1");
//        User user2 = JSON.parseObject(value,User.class);   //user2是可以读取到user1的值的，相当于从缓存中取出来的
//        print(9, user2);
//
//    }
}
