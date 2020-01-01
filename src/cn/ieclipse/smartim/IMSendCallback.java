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

import cn.ieclipse.smartim.callback.SendCallback;
import cn.ieclipse.smartim.views.IMContactView;

/**
 *	 类/接口描述
 * 
 * @author peixu
 * @date 2019年3月16日
 *       
 */
public class IMSendCallback implements SendCallback {
    protected IMContactView fContactView;
    public IMSendCallback(IMContactView contactView) {
        this.fContactView = contactView;
    }
    
    @Override
    public void onSendResult(int type, String targetId, CharSequence msg,
            boolean success, Throwable t) {
    }
}
