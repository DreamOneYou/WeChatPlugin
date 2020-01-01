package cn.ieclipse.smartim.console;

import java.util.List;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;

import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.actions.ClearHistoryAction;
import cn.ieclipse.smartim.actions.SendFileAction;
import cn.ieclipse.smartim.actions.SendImageAction;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMContactView;
import cn.ieclipse.util.EncodeUtils;

/**
 * Created by peixu on 2019/73/1.
 */
public abstract class IMChatConsole extends CTabItem {
    public static final String ENTER_KEY = "\r\n";
    public Image IMG_NORMAL = null;
    public Image IMG_SELECTED = null;
    protected IContact contact;
    protected String uin;
    protected IMContactView imPanel;
    
    public IMChatConsole(IContact target, IMContactView imPanel) {
        super(imPanel.getTabbedChat(), SWT.NONE);
        this.contact = target;
        this.imPanel = imPanel;
        this.uin = target.getUin();
        setText(contact.getName());
        initUI();
        new Thread() {
            public void run() {
                loadHistories();
            };
        }.start();
    }
    
    public String getName() {
        return getText();
    }
    
    
    public SmartClient getClient() {
        return imPanel.getClient();
    }
    
    public abstract void loadHistory(String raw);
    
    public abstract void post(final String msg);
    
    public String getHistoryFile() {
        return EncodeUtils.getMd5(contact.getName());
    }
    
    public String getUin() {
        return uin;
    }
    
    public String trimMsg(String msg) {
        if (msg.endsWith(ENTER_KEY)) {
            return msg;
        }
        return msg + ENTER_KEY;
    }
    
    public void loadHistories() {
        SmartClient client = getClient();
        if (client != null) {
            List<String> ms = IMHistoryManager.getInstance().load(client,
                    getHistoryFile());
            int size = ms.size();
            for (int i = 0; i < size; i++) {
                String raw = ms.get(i);
                if (!IMUtils.isEmpty(raw)) {
                    try {
                        loadHistory(raw);
                    } catch (Exception e) {
                        error("历史消息记录：" + raw);
                    }
                }
            }
        }
    }
    // 	清除历史聊天记录
    public void clearHistories() {
        IMHistoryManager.getInstance().clear(getClient(), getHistoryFile());
        composite.clearHistory();
    }
    
    public boolean hideMyInput() {
        return false;
    }
    
    public boolean checkClient(SmartClient client) {
        if (client == null || client.isClose()) {
            error("连接已关闭");
            return false;
        }
        if (!client.isLogin()) {
            error("请先登录");
            return false;
        }
        return true;
    }
    // 	发送聊天信息
    public void send(final String input) {
        SmartClient client = getClient();
        if (!checkClient(client)) {
            return;
        }
        String name = client.getAccount().getName();
        //	输出格式：目前时间，发送人姓名，输入的内容
        String msg = IMUtils.formatHtmlMyMsg(System.currentTimeMillis(), name,
                input);
        if (!hideMyInput()) {
            insertDocument(msg);
            IMHistoryManager.getInstance().save(client, getHistoryFile(), msg);
        }
        // 	启动发送消息的线程
        new Thread() {
            @Override
            public void run() {
                post(input);
            }
        }.start();
    }
    
    public void sendFile(final String file) {
        new Thread() {
            public void run() {
                uploadLock = true;
                try {
                    sendFileInternal(file);
                } catch (Exception e) {
                    IMPlugin.getDefault().log("发送文件失败", e);
                    error(String.format("发送文件失败：%s(%s)", file, e.getMessage()));
                } finally {
                    uploadLock = false;
                }
            }
        }.start();
    }
    
    protected void sendFileInternal(final String file) throws Exception {
    
    }
    
    public void error(Throwable e) {
        error(e == null ? "null" : e.toString());
    }
    
    public void error(final String msg) {
        insertDocument(String.format("<div class=\"error\">%s</div>", msg));
    }
      
    public void write(final String msg) {
        insertDocument(msg);
    }
    
    protected Browser historyWidget;
    protected Text inputWidget;
    protected Button btnSend;
    TabComposite composite;
    
    public void initUI() {
        composite = new TabComposite(this);
        // 	发送图片和文件按钮。
        ToolBar toolBar = composite.getToolBar();
        initToolBar(toolBar);
        setControl(composite);
    }
    
    protected void initToolBar(ToolBar toolBar) {
        ToolBarManager manager = new ToolBarManager(toolBar);
        manager.add(new SendImageAction(this));
        manager.add(new SendFileAction(this));
        manager.add(new ClearHistoryAction(this));
        manager.update(true);
    }
    
    /*
     * 	此处语句用来显示对话框选项栏和输入框的功能。
     */
    // 	此条语句是控制图片和文件按钮是否可操作。
    protected boolean scrollLock = false;
    // 	用于控制图片和文件按钮是否可发送
    protected boolean uploadLock = false;
    
    public boolean enableUpload() {
        return !uploadLock;
    }
    

    
    protected void insertDocument(String msg) {
        try {
        	//	如果该按钮功能已被处理，则返回true，否则返回false。
            if (!composite.isDisposed()) {
            	//	将其信息添加到历史记录
                composite.addHistory(msg, scrollLock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
