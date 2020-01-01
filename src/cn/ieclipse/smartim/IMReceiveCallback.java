package cn.ieclipse.smartim;

import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.impl.AbstractContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.smartim.preferences.SettingsPerferencePage;
import cn.ieclipse.smartim.views.IMContactView;
import cn.ieclipse.util.EncodeUtils;

public abstract class IMReceiveCallback implements ReceiveCallback {
    protected IMChatConsole lastConsole;
    protected IMContactView fContactView;
    
    public IMReceiveCallback(IMContactView fContactView) {
        this.fContactView = fContactView;
    }
            
    protected abstract String getMsgContent(AbstractMessage message,
    		AbstractFrom from);
    //	处理获取来的信息  
    protected void handle(boolean unknown, boolean notify,
            AbstractMessage message, AbstractFrom from,
            AbstractContact contact) {
        SmartClient client = fContactView.getClient();
        String msg = getMsgContent(message, from);
        if (!unknown) {
            IMHistoryManager.getInstance().save(client,
                    EncodeUtils.getMd5(contact.getName()), msg);
        }
        
        if (notify) {
            boolean hide = unknown
                    && !IMPlugin.getDefault().getPreferenceStore()
                            .getBoolean(SettingsPerferencePage.NOTIFY_UNKNOWN);
            try {
                hide = hide || from.getMember().getUin()
                        .equals(fContactView.getClient().getAccount().getUin());
            } catch (Exception e) {
                IMPlugin.getDefault().log("", e);
            }
        }
        
        //	得到聊天控制台的ID
        IMChatConsole console = fContactView
                .findConsoleById(contact.getUin(), false);
        // 	判断是否为空如果不为空，记录按钮操作
        if (console != null) {
            lastConsole = console;
            console.write(msg);
        }
    }
}
