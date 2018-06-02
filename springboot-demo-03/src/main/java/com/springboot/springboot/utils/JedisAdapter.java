package com.springboot.springboot.utils;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

/**
 * @author WilsonSong
 * @date 2018/6/1
 */
public class JedisAdapter {
    public static void print(int index, Object object){
        System.out.println(String.format("%d, %s",index,object.toString()));
    }

    public static void main(String[] args){
        Jedis jedis = new Jedis("redis://localhost:6379/9");      //连接6379端口的9号数据库

        jedis.flushDB();  //把DB的数据删除

        //文本 get  set
        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello","newHello");
        print(1,jedis.get("newHello"));
        jedis.setex("hello2",15,"world");  //15秒后过期 验证码，短信验证等等

        //数值加减
        jedis.set("pv", "100");
        jedis.incr("pv");      //加1
        print(2, jedis.get("pv"));
        jedis.incrBy("pv",5);  //加5
        print(2,jedis.get("pv"));
        jedis.decr("pv");
        print(2,jedis.get("pv"));
        jedis.decrBy("pv", 5);
        print(2, jedis.get("pv"));

        print(3,jedis.keys("*"));

        //list命令
        String listName = "list";
        jedis.del(listName);  //存在的话就删掉
        for (int i=0; i<10; ++i){
            jedis.lpush(listName, "a"+String.valueOf(i));
        }
        print(4, jedis.lrange(listName, 0 , 9));   //取出从0到9个
        print(4, jedis.lrange(listName, 1 , 6));   //取出从0到9个
        print(4, jedis.llen(listName));         //list 的长度
        print(4, jedis.lpop(listName));        //list中的值弹出来，弹出一个，弹出之后就没有这个弹出的值了
        print(4, jedis.llen(listName));    //弹出来之后长度改变
        print(4, jedis.lindex(listName ,3));  //取第三个值
        print(4, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a5", "aaa") ); //在a5之后插入 输出插入后的大小
        print(4, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a5","qqq"));   //在a5之前插入
        print(4, jedis.lrange(listName, 0, 10));

        //hash
        String userKey = "user1";
        jedis.hset(userKey, "name", "a");
        jedis.hset(userKey,"age", "1");
        jedis.hset(userKey, "id","1");
        print(5,jedis.hget(userKey,"name"));
        print(5,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"id");
        print(5,jedis.hgetAll(userKey));
        print(5,jedis.hexists(userKey,"age"));
        print(5,jedis.hkeys(userKey)); //key
        print(5,jedis.hvals(userKey));  //value
        jedis.hsetnx(userKey, "school", "seu");  //不存在才写入
        jedis.hsetnx(userKey, "name","b");  //name 已经存在，无效
        print(5,jedis.hgetAll(userKey));

        //set  集合中是没有重复元素的
        String  setKey1 = "commentLike1";
        String  setKey2 = "commentLike2";

        for (int i = 0 ;i<10; ++i){
            jedis.sadd(setKey1, String.valueOf(i));
            jedis.sadd(setKey2, String.valueOf(i*i));
        }

        print(6, jedis.smembers(setKey1));
        print(6,jedis.smembers(setKey2));
        print(6,jedis.sunion(setKey1,setKey2));   //求集合的并集
        print(6,jedis.sdiff(setKey1,setKey2));    //求集合的差异值
        print(6,jedis.sinter(setKey1, setKey2));  //求集合的交集
        print(6,jedis.sismember(setKey1, "12"));   //查是不是集合中的元素
        jedis.srem(setKey1,"5");   //把5删除
        print(6, jedis.smembers(setKey1));
        jedis.smove(setKey2, setKey1, "25"); //从setKey2中把25移动到setKey1,2中就没有25，1中有
        print(6,jedis.smembers(setKey1));
        print(6, jedis.smembers(setKey2));
        print(6, jedis.scard(setKey1));
        print(6,jedis.srandmember(setKey1,3));  //从setKey1中随机的取三个值

        //优先队列
        String rankKey = "rankKry";
        jedis.zadd(rankKey,1, "a");
        jedis.zadd(rankKey,10,"b");
        jedis.zadd(rankKey,5,"c");
        jedis.zadd(rankKey,12,"d");
        jedis.zadd(rankKey,100,"e");
        print(7,jedis.zrange(rankKey,0,4));


    }
}
