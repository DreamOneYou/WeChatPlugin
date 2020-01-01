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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.VirtualCategory;
import cn.ieclipse.smartim.views.IMContactContentProvider;
import cn.ieclipse.smartim.views.IMContactView;
import io.github.biezhi.wechat.api.WechatClient;
import io.github.biezhi.wechat.model.Contact;

/**
 * 	类/接口描述
 * 	继承好友列表内容提供者
 * @author Jamling
 * @date 2017年10月14日
 *       
 */
public class WXContactContentProvider extends IMContactContentProvider {
    	
    public WXContactContentProvider(IMContactView view, boolean check) {
        super(view, check);
    }
    
    // 	获取全部节点。用于操控好友列表栏目的功能:chats,contacts,publics.操控这些栏目下好友的显示。
    @Override
    public Object[] getElements(Object inputElement) {
        WechatClient client = (WechatClient) fView.getClient();
        if ("recent".equals(inputElement)) {
            List<Contact> list = client.getRecentList();
            synchronized (this) {
                Collections.sort(list);
            }
            return list.toArray();
        }
        else if ("group".equals(inputElement)) {
            List<Contact> groups = client.getGroupList();
            if (check) {
                return new Object[] { new VirtualCategory<>("Group", groups) };
            }
            return groups == null ? null : groups.toArray();
        }
        else if ("friend".equals(inputElement)) {
            List<Contact> list = client.getMemberList();
            if (check) {
                return getContactGroup(list).toArray();
            }
            List<VirtualCategory<Contact>> cates = new ArrayList<>();
            cates.add(new VirtualCategory<>("groups", client.getGroupList()));
            cates.addAll(getContactGroup(list));
            
            return cates.toArray();
        }
        else if ("public".equals(inputElement)) {
            List<Contact> list = client.getPublicUsersList();
            return list == null ? null : list.toArray();
        }
        return null;
    }
    
    //	获取节点的孩子节点。 用于显示每个分组下的好友。
    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof Contact) {
            Contact contact = (Contact) parentElement;
            return contact.MemberList.toArray();
        }
        else if (parentElement instanceof VirtualCategory) {
            return ((VirtualCategory<?>) parentElement).getChildren();
        }
        return null;
    }
    
   
    // 	判断是否有孩子节点。用于显示每个好友分组的好友的展示图标，比如A组好友 >。
    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof Contact) {
        	// 	返回所有好友
            return false;
        }
        else if (element instanceof VirtualCategory) {
        	// 	只返回正在聊天的人员
            return ((VirtualCategory<?>) element).hasChildren();
        }
        return false;
    }
    
    // 	用于操控好友contacts列表，比如A，B，C...Z
    public List<VirtualCategory<Contact>> getContactGroup(List<Contact> list) {
        List<VirtualCategory<Contact>> cates = new ArrayList<>();
        if (!IMUtils.isEmpty(list)) {
            List<Contact> unA = new ArrayList<>();
            TreeMap<String, List<Contact>> maps = new TreeMap<>();
            for (Contact c : list) {
                String py = c.getPYInitial();
                char A = IMUtils.isEmpty(py) ? '#' : py.charAt(0);
                // 	按照英文字母大小为好友进行分组
                if (A >= 'A' && A <= 'Z' || A >= 'a' && A <= 'z') {
                	// 	将该字母变为大写
                    String a = String.valueOf(A).toUpperCase();
                    List<Contact> values = maps.get(a);
                    if (values == null) {
                        values = new ArrayList<>();
                        //	建立映射
                        maps.put(a, values);
                    }
                    values.add(c);
                }
                else {
                    unA.add(c);
                }
            }
            for (String n : maps.keySet()) {
                cates.add(new VirtualCategory<>(n, maps.get(n)));
            }
            if (!IMUtils.isEmpty(unA)) {
                cates.add(new VirtualCategory<>("#", unA));
            }
        }
        return cates;
    }
    
    @Override
    public Object getParent(Object element) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
