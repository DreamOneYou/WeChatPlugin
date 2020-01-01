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
package cn.ieclipse.wechat;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;
import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.views.IMContactView;
import cn.ieclipse.util.FileUtils;
import cn.ieclipse.util.StringUtils;
//	微信消息，封装了原始消息
import io.github.biezhi.wechat.api.WechatClient;
import io.github.biezhi.wechat.model.UploadInfo;
import io.github.biezhi.wechat.model.WechatMessage;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月16日
 *       
 */
public class WXChatConsole extends IMChatConsole {
    
    public WXChatConsole(IContact target, IMContactView imPanel) {
        super(target, imPanel);
    }
    
    @Override
    public WechatClient getClient() {
        return (WechatClient) IMClientFactory.getInstance().getWechatClient();
    }
    
    //	加载历史信息
    @Override
    public void loadHistory(String raw) {
    	//	如果是我发送的信息，将其写入到历史信息里
        if (IMUtils.isMySendMsg(raw)) {
            write(raw);
            return;
        }
        //	如果不是，则进行查询该用户，之后将记录存入到该用户下
        WechatMessage m = (WechatMessage) getClient().handleMessage(raw);
        AbstractFrom from = getClient().getFrom(m);
        /**
        *	getName: 获取发送人姓名如果有备注则优先显示备注，否则显示昵称
        */
        String name = from == null ? "未知用户" : from.getName();
        String msg = IMUtils.formatHtmlMsg(m.getTime(), name, m.getText());
        write(msg);
    }
    
    @Override
    public boolean hideMyInput() {
        return false;
    }
    //	发送信息
    @Override
    public void post(String msg) {
        WechatClient client = getClient();
        if (client.isLogin() && contact != null) {
            WechatMessage m = client.createMessage(0, msg, contact);
            client.sendMessage(m, contact);
        }
        else {
            error("发送失败，客户端异常（可能已断开连接或找不到联系人）");
        }
    }
     
    @Override
    public void sendFileInternal(final String file) {
        final File f = new File(file);
        final WechatClient client = getClient();
        if (!checkClient(client)) {
            return;
        }
        
        String ext = FileUtils.getExtension(f.getPath()).toLowerCase();
        String mimeType = URLConnection.guessContentTypeFromName(f.getName());
        String media = "pic";
        int type = WechatMessage.MSGTYPE_IMAGE;
        String content = "";
        if (Arrays.asList("png", "jpg", "jpeg", "bmp").contains(ext)) {
            type = WechatMessage.MSGTYPE_IMAGE;
            media = "pic";
        }
        else if ("gif".equals(ext)) {
            type = WechatMessage.MSGTYPE_EMOTICON;
            media = "doc";
        }
        else {
            type = WechatMessage.MSGTYPE_FILE;
            media = "doc";
        }
        
        final UploadInfo uploadInfo = client.uploadMedia(f, mimeType, media);
        
        if (uploadInfo == null) {
            error("上传失败");
            return;
        }
        String link = StringUtils.file2url(file);
        String label = file.replace('\\', '/');
        String input = null;
        if (type == WechatMessage.MSGTYPE_EMOTICON
                || type == WechatMessage.MSGTYPE_IMAGE) {
            input = String.format("<img src=\"%s\" border=\"0\" alt=\"%s\"",
                    link, label);
            if (uploadInfo.CDNThumbImgWidth > 0) {
                input += " width=\"" + uploadInfo.CDNThumbImgWidth + "\"";
            }
            if (uploadInfo.CDNThumbImgHeight > 0) {
                input += " height=\"" + uploadInfo.CDNThumbImgHeight + "\"";
            }
            input = String.format("<a href=\"%s\" title=\"%s\">%s</a>", link,
                    link, input);
        }
        else {
            input = String.format("<a href=\"%s\" title=\"%s\">%s</a>", link,
                    label, label);
            content = client.createFileMsgContent(f, uploadInfo.MediaId);
        }
        
        final WechatMessage m = client.createMessage(type, content, contact);
        m.text = input;
        m.MediaId = uploadInfo.MediaId;
        
        client.sendMessage(m, contact);
        
        if (!hideMyInput()) {
            String name = client.getAccount().getName();
            String msg = IMUtils.formatHtmlMsg(true, false,
                    System.currentTimeMillis(), name, m.text);
            insertDocument(msg);
            IMHistoryManager.getInstance().save(client, getHistoryFile(), msg);
        }
    }
}
