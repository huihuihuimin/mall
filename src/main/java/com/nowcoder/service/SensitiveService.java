/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: SensitiveService
 * Author:   LSN
 * Date:     2019/6/13 15:59
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/13
 * @since 1.0.0
 */
@Service
public class SensitiveService implements InitializingBean
{
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    private TrieNode rootNode = new TrieNode();

    @Override
    public void afterPropertiesSet() throws Exception
    {
        try
        {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null)
            {
                addWord(lineTxt.trim());
            }
            read.close();
        } catch (Exception e)
        {
            logger.error("读取异常" + e.getMessage());
        }
    }

    //过滤器
    public String filter(String text)
    {
        if (StringUtils.isBlank(text))
            return text;
        String replacement = "***";
        StringBuffer sb = new StringBuffer();
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;

        while (position < text.length())
        {
            char c = text.charAt(position);

            if(isSymbol(c))
            {
                if (tempNode == rootNode)
                {
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(c);
            if (tempNode == null)
            {
                sb.append(text.charAt(begin));
                begin++;
                position = begin;
                tempNode = rootNode;
            } else if (tempNode.isKeyWordEnd())
            {
                sb.append(replacement);
                position++;
                begin = position;
                tempNode = rootNode;
            } else
            {
                position++;
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();
    }

    //增加关键词
    private void addWord(String lineTxt)
    {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineTxt.length(); ++i)
        {
            Character c = lineTxt.charAt(i);
            TrieNode node = tempNode.getSubNode(c);
            if (node == null)
            {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;

            if (i == lineTxt.length() - 1)
            {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    private boolean isSymbol(char c)
    {
        int ic = (int) c;
        //东亚文字0x2E80-0x9FFF
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    private class TrieNode
    {
        //关键词的结尾
        private boolean end = false;
        //当前节点下所有子节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character key, TrieNode node)
        {
            subNodes.put(key, node);
        }

        TrieNode getSubNode(Character key)
        {
            return subNodes.get(key);
        }

        boolean isKeyWordEnd()
        {
            return end;
        }

        void setKeyWordEnd(boolean end)
        {
            this.end = end;
        }
    }

    public static void main(String[] args)
    {
        SensitiveService sensitiveService = new SensitiveService();
        sensitiveService.addWord("色情");
        sensitiveService.addWord("强奸");
        System.out.println(sensitiveService.filter("强 奸和色 情"));

    }

}
