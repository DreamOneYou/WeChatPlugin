package cn.ieclipse.smartim.views;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;

import cn.ieclipse.smartim.model.IContact;

//	 敲击两次，跳转到指定好友聊天界面，比如在好友列表随机双击一个好友，就会在另一个对话框跳出与其进行聊天
public class IMContactDoubleClicker implements IDoubleClickListener {
    
    protected IMContactView fView;
    
    public IMContactDoubleClicker(IMContactView view) {
        this.fView = view;
    }
    
    @Override
    public void doubleClick(DoubleClickEvent event) {
    	// 	判断敲击的好友是哪一个。
        IStructuredSelection isel = (IStructuredSelection) event.getSelection();
        Object obj = isel.getFirstElement();
        if (obj != null) {
            click(obj);
        }
    }
    // 	显示控制台
    public void click(Object obj) {
        if (obj instanceof IContact) {
            fView.openConsole((IContact) obj);
        }
    }
}
