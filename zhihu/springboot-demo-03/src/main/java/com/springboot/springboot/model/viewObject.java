package com.springboot.springboot.model;

import java.util.HashMap;
import java.util.Map;

//用来传递freemarker与Controller之间的参数的
public class viewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();

     public void set(String key, Object values){
         objs.put(key, values);
     }

     public Object get(String key){
         return objs.get(key);
     }
}
