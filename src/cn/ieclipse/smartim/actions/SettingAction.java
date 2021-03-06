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

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import cn.ieclipse.smartim.preferences.HotKeyPreferencePage;
import cn.ieclipse.smartim.preferences.SettingsPerferencePage;
import cn.ieclipse.smartim.preferences.SmartWeChatPreferencePage;
import cn.ieclipse.smartim.views.IMContactView;

/**
 * 类/接口描述
 * 设置功能的action
 * 
 * @author peixu
 * @date 2019年3月20日
 *       
 */
public class SettingAction extends IMPanelAction {
    public SettingAction(IMContactView contactView) {
        super(contactView);
        setText("设置");
        setToolTipText("Settings & Helps");
    }
    
    @Override
    public void run() {
        open(null);
    }
    
    public static void open(String pageID) {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getShell();
        String pageId = pageID == null ? SmartWeChatPreferencePage.class.getName() : pageID;
        String[] ids = { pageId, 
                SmartWeChatPreferencePage.class.getName(),
                HotKeyPreferencePage.class.getName(),
                SettingsPerferencePage.class.getName()
                };
        PreferencesUtil.createPreferenceDialogOn(shell, pageId, ids, null)
                .open();
    }
}
