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
package cn.ieclipse.smartim;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import io.github.biezhi.wechat.api.WechatClient;

/**
 * 类/接口描述
 * 
 * @author peixu
 * @date 2019年3月14日
 *       
 */
public class IMClientFactory {
    public static final String TYPE_WECHAT = "Wechat";
    private Map<String, SmartClient> clients = null;
    
    private static IMClientFactory instance = new IMClientFactory();
    
    // 	检查用户是否为空，如果为空，为其new一个新的哈希表用于存储用户
    private void checkClients() {
        if (clients == null) {
            clients = new HashMap<>();
        }
    }
    
    /*
     * equalsIgnoreCase：用于字符串比对，忽略大小写
     * 	判断是否是进行微信操作，如果用户是微信操作，则按照微信协议进行操作，否则报出错误，没有这中类型的操作协议
     */
    private SmartClient create(String type) {
        if (TYPE_WECHAT.equalsIgnoreCase(type)) {
            return new WechatClient();
        }
        throw new UnsupportedOperationException("No client type " + type);
    }
    
    /*
     * 	得到用户,并且确定是微信用户还是其他用户
     * get:返回指定键映射到的值，如果该映射不包含键的映射，则返回null。
     * getAbsoluteFile:返回此抽象路径名的绝对形式
     */
    public SmartClient getClient(String type) {
        checkClients();
        SmartClient client = clients.get(type);
        if (client == null || client.isClose()) {
            client = create(type);
            // 	得到文件的绝对路径
            File dir = IMPlugin.getDefault().getStateDir().getAbsoluteFile();
            client.setWorkDir(new File(dir, type));
            // 	put(K key, V value):将指定值与此映射中的指定键关联
            clients.put(type, client);
        }
        return client;
    }
     
    // 	得到微信用户信息
    public WechatClient getWechatClient() {
        return (WechatClient) getClient(TYPE_WECHAT);
    }
    
    // 	返回当前这个类实例可供其他类使用
    public static IMClientFactory getInstance() {
        return instance;
    }
}
