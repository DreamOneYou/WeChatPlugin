/*
 * Copyright 2014-2015 ieclipse.cn.
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
package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.views.IMContactView;


/**
 * 	退出登录
 * @author peixu
 * @date 2019年3月20日      
 */
public class DisconnectAction extends IMPanelAction {
    
    public DisconnectAction(IMContactView contactView) {
        super(contactView);
        setText("下线");
        setToolTipText("Disconnect from server");
    }
    
    @Override
    public void run() {
        if (fContactView != null) {
        	// 	退出登录，关闭所有聊天记录
            fContactView.close();
        }
    }
}
