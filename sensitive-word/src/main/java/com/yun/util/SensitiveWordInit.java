package com.yun.util;

import java.io.*;
import java.util.*;


/**
 * 敏感词库初始化
 * 
 * @author AlanLee
 *
 */
public class SensitiveWordInit
{
    private String ENCODING = "UTF-8";    //字符编码
    /**
     * 敏感词库
     */
    private  HashMap sensitiveWordMap;

    private final String SENSITIVE_WORD_PATH=this.getClass().getClassLoader().getResource("").getPath()+"/CensorWords.txt";



    /**
     * 初始化敏感词
     * 
     * @return
     */
    public Map initKeyWord()
    {
        try {
            // 从敏感词集合对象中取出敏感词并封装到Set集合中
            Set<String> keyWordSet = readWordFile();
            // 将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
        }catch (Exception e){
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    private Set<String> readWordFile() throws Exception {

        Set<String> set ;
        InputStreamReader inputStreamReader=null;
        try {
            File file = new File(SENSITIVE_WORD_PATH);
            inputStreamReader= new InputStreamReader(new FileInputStream(file),ENCODING);
            if(file.isFile() && file.exists()){
                //文件流是否存在
                 set = new HashSet<String>();
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                 String txt = null;
                 while((txt = bufferedReader.readLine()) != null){
                     //读取文件，将文件内容放入到set中
                     set.add(txt);
                 }
            } else{
                //不存在抛出异常信息
                throw new Exception("敏感词库文件不存在");
            }
        } catch (Exception e) {
           throw e;
        } finally {
            if (inputStreamReader!=null){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return set;
    }

    /**
     * 封装敏感词库
     * 
     * @param keyWordSet
     */
    @SuppressWarnings("rawtypes")
    private void addSensitiveWordToHashMap(Set<String> keyWordSet){
        // 初始化HashMap对象并控制容器的大小
        sensitiveWordMap = new HashMap(keyWordSet.size());
        // 敏感词
        String key = null;
        // 用来按照相应的格式保存敏感词库数据
        Map nowMap = null;
        // 用来辅助构建敏感词库
        Map<String, String> newWorMap = null;
        // 使用一个迭代器来循环敏感词集合
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext())
        {
            key = iterator.next();
            // 等于敏感词库，HashMap对象在内存中占用的是同一个地址，所以此nowMap对象的变化，sensitiveWordMap对象也会跟着改变
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++)
            {
                // 截取敏感词当中的字，在敏感词库中字为HashMap对象的Key键值
                char keyChar = key.charAt(i);

                // 判断这个字是否存在于敏感词库中
                Object wordMap = nowMap.get(keyChar);
                if (wordMap != null)
                {
                    nowMap = (Map) wordMap;
                }
                else
                {
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                // 如果该字是当前敏感词的最后一个字，则标识为结尾字
                if (i == key.length() - 1)
                {
                    nowMap.put("isEnd", "1");
                }
//                System.out.println("封装敏感词库过程："+sensitiveWordMap);
            }
//            System.out.println("查看敏感词库数据:" + sensitiveWordMap);
        }
        System.out.println(sensitiveWordMap);
    }
}