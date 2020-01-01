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
package cn.ieclipse.smartim.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.ieclipse.smartim.IMPlugin;

/**
 *	 类/接口描述
 * 	对smartWeChat插件进行介绍及开发人员联系方式
 * @author peixu
 * @date 2019年3月22日
 *       
 */
public class SmartWeChatPreferencePage extends PreferencePage
        implements IWorkbenchPreferencePage {
        
    public SmartWeChatPreferencePage() {
        setPreferenceStore(IMPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.SmartWeChatPreferencePage_desc);
    }

    @Override
    protected Control createContents(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new GridLayout(1, false));
        
        Link l = new Link(comp, SWT.ITALIC);
        l.setText(Messages.SmartWeChatPreferencePage_Email);

        return comp;
    }
    
    @Override
    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub
        
    }
}
