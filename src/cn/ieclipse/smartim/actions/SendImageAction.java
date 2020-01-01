package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.console.IMChatConsole;

// 	进行图片发送这个action
public class SendImageAction extends SendFileAction {
    
    public SendImageAction(IMChatConsole console) {
        super(console);
        setText("发送图片");
        this.setToolTipText("send picture");
        filterNames = new String[] { "Image Files", "All Files (*)" };
        filterExtensions = new String[] { "*.gif;*.png;*.jpg;*.jpeg;*.bmp",
                "*" };
    }
    
}
