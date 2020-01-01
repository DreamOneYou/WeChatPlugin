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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.IMSendCallback;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMContactDoubleClicker;
import cn.ieclipse.smartim.views.IMContactView;
import io.github.biezhi.wechat.api.WechatClient;

/**
 * 	微信联系人视图
 * 
 * @author peixu
 * @date 2019年3月14日
 *       
 */
public class WXContactView extends IMContactView {
    
    /**
     *	 扩展指定的视图ID
     */
    public static final String ID = "cn.ieclipse.wechat.views.WXContactView";
    
    private TreeViewer ftvFriend;
    private TreeViewer ftvGroup;
    private TreeViewer ftvPublic;

    
    public WXContactView() {
        viewId = ID;
        contentProvider = new WXContactContentProvider(this, false);
        // 	敲击两次,进行跳转
        doubleClicker = new IMContactDoubleClicker(this);
        receiveCallback = new WXReceiveCallback(this);
        sendCallback = new IMSendCallback(this);
    }
    
    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        // 创建按钮
        ftvFriend = createTab("聊天", tabFolder);
        ftvGroup = createTab("联系人分组", tabFolder);
        ftvPublic = createTab("公众号", tabFolder);
        tabFolder.setSelection(0);
        initTrees(ftvFriend, ftvGroup, ftvPublic);
    }
    
    //	 加载微信的相关好友列表等信息
    @Override
    public void doLoadContacts() {
        WechatClient client = (WechatClient) getClient();
        // 	判断微信是否登录
        if (client.isLogin()) {
            try {
            	/*
            	 *	 加载微信的相关回调函数
            	 *	接受信息，发送信息，添加接受，修改等回调函数
            	 *	启动发送，接受等相关功能
            	 */
                client.init();
                notifyLoadContacts(true);
                client.setReceiveCallback(receiveCallback);
                client.setSendCallback(sendCallback);
                client.start();
            } catch (Exception e) {
            	/*
            	 *	相关回调函数加载失败
            	 */
                IMPlugin.getDefault().log("微信初始化失败", e);
            }
        }
        else {
            notifyLoadContacts(false);
        }
    }
    
    // 	用于加载左侧列表内容，用于视图显示
    @Override
    protected void onLoadContacts(boolean success) {
        if (success) {
            ftvFriend.setInput("recent");
            ftvGroup.setInput("friend");
            ftvPublic.setInput("public");
        }
        else {
            ftvFriend.setInput(null);
            ftvGroup.setInput(null);
            ftvPublic.setInput(null);
        }
    }
    
    @Override
    public WechatClient getClient() {
        return (WechatClient) IMClientFactory.getInstance().getWechatClient();
    }
    
    @Override
    public IMContactView createContactsUI() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public IMChatConsole createConsoleUI(IContact contact) {
        return new WXChatConsole(contact, this);
    }
}
