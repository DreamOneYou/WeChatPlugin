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
import java.util.List;
import java.util.Map;

import cn.ieclipse.smartim.helper.FileStorage;
import cn.ieclipse.smartim.preferences.SettingsPerferencePage;

/**
 * 类/接口描述
 * 
 * @author peixu
 * @date 2019年3月23日
 *       
 */
public class IMHistoryManager {
    private Map<String, FileStorage> stores = new HashMap<>();
    private static final int max = 30;
    private static final int size = 500;
    private long ts = System.currentTimeMillis();
    
    private static IMHistoryManager instance = new IMHistoryManager();
    
    public static IMHistoryManager getInstance() {
        return instance;
    }	
    // 	文件存储
    private FileStorage get(SmartClient client, String uin) {
    	//get(Object key):返回指定键映射到的值，如果该映射不包含键的映射，则返回null。
        FileStorage fs = stores.get(uin);
        if (fs == null) {
        	// File(File parent, String child):从父抽象路径名和子路径名字符串创建一个新文件实例。
            File f = new File(client.getWorkDir("history"), uin);
            fs = new FileStorage(size, f.getAbsolutePath());
            /*
             * getBoolean(String name):返回具有给定名称的布尔值首选项的当前值
             * getPreferenceStore:返回此UI(User Interface)插件的首选项存储
             */
            boolean persistent = IMPlugin.getDefault().getPreferenceStore()
                    .getBoolean(SettingsPerferencePage.LOG_HISTORY);
            /*
             * setPersistent (boolean isPersistent): 设置活动是否长久有效，默认是
             * 	不保持的。如果设置了这个标志，可以防止系统在低内存下kill或者stop这个活动或者他的过程. 
             */
            fs.setPersistent(persistent);
            // 	保存文件信息
            stores.put(uin, fs);
        }
        return fs;
    }
    
    // 	加载用户数量
    public List<String> load(SmartClient client, String uin) {
        FileStorage fs = get(client, uin);
        return fs.getLast(max);
    }
    
    // 	保存当前聊天记录的信息
    public boolean save(SmartClient client, String uin, String rawMsg) {
        FileStorage fs = get(client, uin);
        boolean ret = fs.append(rawMsg);
        ret = ret && fs.isPersistent();
        if (System.currentTimeMillis() - ts > 1000 * 120) {
            flush();
            ts = System.currentTimeMillis();
        }
        return ret;
    }
    
    // 	释放文件所占空间
    public boolean clear(SmartClient client, String uin) {
        FileStorage fs = get(client, uin);
        fs.release();
        return true;
    }
    // 	将缓冲区中的信息全部清除
    public void flush() {
        if (!stores.isEmpty()) {
            for (FileStorage fs : stores.values()) {
                if (fs.isPersistent()) {
                    fs.flush();
                }
            }
            stores.clear();
        }
    }
}
