/*
 * Copyright 2014-2017 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.smartim.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import cn.ieclipse.util.StringUtils;

/**
 * 类/接口描述
 * 
 * @author peixu
 * @date 2019年3月14日
 *       
 */
public class IMUtils {
    
    /**
     * 通过文件路径获取文件的名字
     *
     * @param path
     *            file path
     *            
     * @return file name
     */
    public static String getName(String path) {
        File f = new File(path);
        String name = f.getName();
        return name;
    }
    // 	确定文件大小
    public static String formatFileSize(long length) {
        if (length > (1 << 20)) {
            return length / (1 << 20) + "M";
        }
        else if (length > (1 << 10)) {
            return length / (1 << 10) + "K";
        }
        return length + "B";
    }
    // 	文件大小是空的情况下
    public static boolean isEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }
    // 	相关列表集合为空的情况，比如好友列表集合为空
    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }
    
    //	 将信息编码为html格式
    public static String encodeHtml(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return "";
        }
        else {
            return StringUtils.encodeXml(msg);
        }
    }
    //	判断是不是我发送的消息
    public static boolean isMySendMsg(String raw) {
        return raw.matches("^\\d{2}:\\d{2}:\\d{2} [.\\s\\S]*")
                || raw.startsWith("<div");
    }
    //	将信息编码为html
    public static String formatHtmlMsg(String msg, boolean encodeHtml) {
        // TODO only replace the non-html tag space;
        String m = encodeHtml(msg);
        m = m.replaceAll("\r?\n", "<br/>");
        String content = encodeHtml ? autoLink(autoReviewLink(m)) : m;
        return content;
    }
    
    //	接收来的信息
    public static String formatHtmlMsg(long time, String name,
            CharSequence msg) {
        return formatHtmlMsg(false, true, time, name, msg.toString());
    }
    //	我发送的消息
    public static String formatHtmlMyMsg(long time, String name,
            CharSequence msg) {
        return formatHtmlMsg(true, true, time, name, msg.toString());
    }
    
    //	对获取来的信息进行整理
    public static String formatHtmlMsg(boolean my, boolean encodeHtml,
            long time, String name, String msg) {
        String t = new SimpleDateFormat("HH:mm:ss").format(time);
        String clz = my ? "sender my" : "sender";
        String content = formatHtmlMsg(msg, encodeHtml);
        return String.format(DIV_ROW_FORMAT, clz, t, name, name, content);
    }
    
    private static String autoReviewLink(String input) {
        return input;
    }
    
    private static String autoLink(String input) {
        return input;
    }
    
    public static final String DIV_SENDER_FORMAT = "<div class=\"%s\"><span class=\"time\">%s</span> <a href=\"user://%s\">%s</a>: </div>";
    public static final String DIV_CONTENT_FORMAT = "<div class=\"content\">%s</div>";
    public static final String DIV_ROW_FORMAT = String.format("<div>%s%s</div>",
            DIV_SENDER_FORMAT, DIV_CONTENT_FORMAT);
}
