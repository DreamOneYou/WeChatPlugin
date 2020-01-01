/*
 * Copyright 2010 the original author or authors.
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
package cn.ieclipse.smartim.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import cn.ieclipse.smartim.IMPlugin;

/**
 * 	用于初始化默认首选项值。.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
    
    /*
     *	对设置内容进行默认设置
     */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = IMPlugin.getDefault().getPreferenceStore();
        
        store.setDefault(HotKeyPreferencePage.KEY_SEND, "CR"); //$NON-NLS-1$
        store.setDefault(HotKeyPreferencePage.KEY_HIDE, "Alt + H"); //$NON-NLS-1$
        store.setDefault(HotKeyPreferencePage.KEY_HIDE_CLOSE, "Alt + Tab"); //$NON-NLS-1$
        store.setDefault(SettingsPerferencePage.NOTIFY_ENABLE, true);
        store.setDefault(SettingsPerferencePage.NOTIFY_GROUP, false);
        store.setDefault(SettingsPerferencePage.NOTIFY_FRIEND, true);
        store.setDefault(SettingsPerferencePage.NOTIFY_DISMISS, 5);
        store.setDefault(SettingsPerferencePage.NOTIFY_UNREAD, true);
        store.setDefault(SettingsPerferencePage.NOTIFY_UNKNOWN, false);
        store.setDefault(SettingsPerferencePage.HIDE_MY_INPUT, true);
        store.setDefault(SettingsPerferencePage.LOG_HISTORY, true);
    }
    
}
