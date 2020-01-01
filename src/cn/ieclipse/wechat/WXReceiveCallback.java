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

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.IMReceiveCallback;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.smartim.preferences.SettingsPerferencePage;
import io.github.biezhi.wechat.model.Contact;
import io.github.biezhi.wechat.model.GroupFrom;
import io.github.biezhi.wechat.model.UserFrom;
import io.github.biezhi.wechat.model.WechatMessage;

/**
 *	 类/接口描述
 * 
 * @author peixu
 * @date 2019年3月14日
 *       
 */
public class WXReceiveCallback extends IMReceiveCallback {
    
    public WXReceiveCallback(WXContactView fContactView) {
        super(fContactView);
    }
    
    //	 用于接收发送来的消息
    @Override
    public void onReceiveMessage(AbstractMessage message, AbstractFrom from) {
        if (from != null && from.getContact() != null) {
            boolean unknown = false;
            /*
             * getBoolean:返回具有给定名称的布尔值首选项的当前值。如果没有指定名称的首选项，或者当前值
             * 	不能视为布尔值，则返回默认默认值(false)。
             */
            boolean notify = IMPlugin.getDefault().getPreferenceStore()
                    .getBoolean(SettingsPerferencePage.NOTIFY_FRIEND);
            Contact contact = (Contact) from.getContact();
            if (from instanceof GroupFrom) {
                GroupFrom gf = (GroupFrom) from;
                unknown = gf.getMember() == null || gf.getMember().isUnknown();
                notify = IMPlugin.getDefault().getPreferenceStore()
                        .getBoolean(SettingsPerferencePage.NOTIFY_GROUP);
            }
            else {
                unknown = from.getMember() == null;
            }
            // 	对获取来的信息进行处理
            handle(unknown, notify, message, from, contact);
        }
    }
    
    // 	加载获取信息的内容
    @Override
    protected String getMsgContent(AbstractMessage message, AbstractFrom from) {
        String name = from.isOut() ? from.getTarget().getName()
                : from.getName();
        String msg = null;
        /*
         *  instanceof 运算符与 typeof 运算符相似，用于识别正在处理的对象的类型.
         * 	 与 typeof 方法不同的是，instanceof 方法要求开发者明确地确认对象为某特定类型。
         */
        if (message instanceof WechatMessage) {
            WechatMessage m = (WechatMessage) message;
            // 	判断获取的内容是否为空
            String text = m.getText() == null ? null : m.getText().toString();
            boolean encodeHtml = true;
            boolean my = from.isOut() ? true : false;
            // 	对获取来的信息进行编码
            // 	判断还类型数据在微信协议里面是否可以找到匹配，如果不能，编码失败，不然进行下一步。
            if (m.MsgType != WechatMessage.MSGTYPE_TEXT) {
                encodeHtml = false;
            }
            else {
                if (from instanceof UserFrom) {
                    Contact c = (Contact) from.getContact();
                    encodeHtml = !c.isPublic();
                }
            }
            msg = IMUtils.formatHtmlMsg(my, encodeHtml, m.CreateTime, name,
                    text);
        }
        return msg;
    }

	@Override
	public void onReceiveError(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}
}
