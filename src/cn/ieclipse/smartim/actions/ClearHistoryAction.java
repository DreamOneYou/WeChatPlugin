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

import cn.ieclipse.smartim.console.IMChatConsole;

/**
 * 清除聊天记录
 * 
 * @author peixu
 * @date 2019年3月8日
 *       
 */
public class ClearHistoryAction extends IMChatAction {
    
    public ClearHistoryAction(IMChatConsole console) {
        super(console);
        setText("清除历史记录");
        setToolTipText("Clear history");
    }
    
    @Override
    public void run() {
        if (fConsole != null) {
            fConsole.clearHistories();
        }
    }
}
