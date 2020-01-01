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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import cn.ieclipse.smartim.dialogs.LoginDialog;
import cn.ieclipse.smartim.views.IMContactView;

/**
 * 类/接口描述
 * 
 * @author peixu
 * @date 2019年3月20日
 *       
 */
public class LoginAction extends IMPanelAction {
    
    public LoginAction(IMContactView view) {
        super(view);
        setText("登录");
        setToolTipText("Click to show qrcode");
    }
    
    @Override
    public void run() {
        if (fContactView != null) {
            Shell shell = fContactView.getSite().getShell();
            boolean ok = true;
            // 	判断是否处于登录状态
            if (fContactView.getClient().isLogin()) {
            	// openConfirm:如果用于按的是OK按钮，则返回true，否则返回false。
                ok = MessageDialog.openConfirm(shell, null,
                        "您已处于登录状态，确定要重新登录吗？");
            }
            if (ok) {
            	// 	如果重新登录.则重新弹出登录框.进行扫码登录
                LoginDialog dialog = new LoginDialog(shell);
                dialog.setClient(fContactView.getClient());
                dialog.open();
            }
            fContactView.initContacts();
        }
    }
}
