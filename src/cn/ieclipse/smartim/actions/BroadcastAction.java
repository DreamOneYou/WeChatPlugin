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
package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.views.IMContactView;

/**
 * 类/接口描述
 * 
 * @author peixu
 * @date 2019年3月22日
 * @warn 群发功能还没实现   
 */
public class BroadcastAction extends IMPanelAction {
    public BroadcastAction(IMContactView contactView) {
        super(contactView);
        setText("&BroadCast");
        setToolTipText("Broadcast message to SmartWeChat group/discuss/friends");
    }
}
