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

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.ieclipse.smartim.IMPlugin;

public class HotKeyPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
        // 非本地语言语言支持(NLS),：这一行的第一个字符串是不需要国际化的
    public static final String KEY_SEND = IMPlugin.PLUGIN_ID + ".key.send"; //$NON-NLS-1$
    public static final String KEY_INPUT = IMPlugin.PLUGIN_ID + ".key.input"; //$NON-NLS-1$
    public static final String KEY_NEXT = IMPlugin.PLUGIN_ID + ".key.next"; //$NON-NLS-1$
    public static final String KEY_PREV = IMPlugin.PLUGIN_ID + ".key.prev"; //$NON-NLS-1$
    public static final String KEY_INPUT_ESC = IMPlugin.PLUGIN_ID
            + ".key.input.esc"; //$NON-NLS-1$
    public static final String KEY_HIDE = IMPlugin.PLUGIN_ID + ".key.hide"; //$NON-NLS-1$
    public static final String KEY_HIDE_CLOSE = IMPlugin.PLUGIN_ID
            + ".key.hide_close"; //$NON-NLS-1$
            
    public HotKeyPreferencePage() {
        super(GRID);
        setPreferenceStore(IMPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.HotKeyPreferencePage_desc);
    }
    
    /**
     *	 创建字段编辑器。字段编辑器是操作各种类型首选项所需的常见GUI块的抽象。
     * 	每个字段编辑器都知道如何保存和恢复自己。
     */
    public void createFieldEditors() {
        HotKeyFieldEditor send = new HotKeyFieldEditor(KEY_SEND,
                Messages.HotKeyPreferencePage_send, getFieldEditorParent());
        addField(send);
        
        addField(new HotKeyFieldEditor(KEY_HIDE,
                Messages.HotKeyPreferencePage_hide_chat,
                getFieldEditorParent()));
        addField(new HotKeyFieldEditor(KEY_HIDE_CLOSE,
                Messages.HotKeyPreferencePage_close_chat,
                getFieldEditorParent()));
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }
}