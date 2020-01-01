package cn.ieclipse.smartim.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

public class ClosableTabHost extends CTabFolder {
    IMChatConsole currentItem;
    
    /**
     * 	创建复合
     * @param parent
     * @param style
     */
    public ClosableTabHost(Composite parent) {
        super(parent, SWT.CLOSE | SWT.FLAT);
        // 	指定图像是否出现在未选择的选项卡上
        setUnselectedImageVisible(true);
        //	 指定当用户将鼠标悬停在未选中的选项卡上时，是否出现关闭按钮。此处是不关闭
        setUnselectedCloseVisible(false);
        /** 
         * 	将侦听器添加到侦听器集合中，当用户更改接收方的选择时，将通过向其发送
         * SelectionListenerinterface中定义的消息之一来通知侦听器。
         */
        addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (e.item instanceof IMChatConsole) {
                    IMChatConsole item = (IMChatConsole) e.item;
                    item.setImage(item.IMG_NORMAL);
                    currentItem = item;
                }
            }
        });
    }
    


}
