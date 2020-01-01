package cn.ieclipse.smartim.views;

import java.util.ArrayList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.ShowInContext;
import org.eclipse.ui.part.ViewPart;
import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.IMSendCallback;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.actions.BroadcastAction;
import cn.ieclipse.smartim.actions.DisconnectAction;
import cn.ieclipse.smartim.actions.LoginAction;
import cn.ieclipse.smartim.actions.SettingAction;
import cn.ieclipse.smartim.actions.ToggleContactsAction;
import cn.ieclipse.smartim.callback.ModificationCallback;
import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.console.ClosableTabHost;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;


public abstract class IMContactView extends ViewPart implements IShowInTarget {
    
    protected DrillDownAdapter drillDownAdapter;
    protected Action login;
    protected Action settings;
    protected Action exit;
    protected Action toggleLeft;
    protected Action broadcast;
    protected Action testAction;
    
    protected SmartClient client;
    
    protected IMContactContentProvider contentProvider;
    protected IMContactDoubleClicker doubleClicker;
    
    protected ReceiveCallback receiveCallback;
    protected IMSendCallback sendCallback;
    protected ModificationCallback modificationCallback;
    protected boolean updateContactsOnlyFocus = false;
    
    protected SashForm sashForm;
    protected CTabFolder tabFolder;
    protected CTabFolder tabbedChat;
    
    protected String viewId;
    
    /**
     * The constructor.
     */
    public IMContactView() {
    }
    
    /**
     * 	这是一个回调函数，他将允许我们去创建视图并且初始化他
     */
    public void createPartControl(Composite parent) {
    	// 	各个部分操作栏的视图显示
        makeActions();
        // 	添加工具栏
        contributeToActionBars();
        // 	SashForm:进行分割窗体
        sashForm = new SashForm(parent, SWT.SMOOTH);
        /**
         *	自定义选项卡
         * 	CTabFolder：构造该类的新实例，给出该类的父类和描述其行为和外观的样式值
         */
        tabFolder = new CTabFolder(sashForm, SWT.NONE);
        // 	边框是否可见
        tabFolder.setBorderVisible(true);
        /**
         shell.setBackgroundMode(int mode);  
		        参数mode有三个可选值: 
		   	WT.INHERIT_FORCE,SWT.INHERIT_DEFAULT,SWT.INHERIT_NONE 
		        其中前二个能达成背景透明效果,SWT.INHERIT_NONE则不行. 
		        猜测: Shell默认的setBackgroundMode()方法参数可能是SWT.INHERIT_NONE. 
        */  
        tabFolder.setBackgroundMode(SWT.INHERIT_FORCE);
        // 	选项卡的鼠标操作
        tabbedChat = new ClosableTabHost(sashForm);
        // 	选项卡边框是否可见，此处可见
        tabbedChat.setBorderVisible(true);
        // 	选项卡背景颜色透明
        tabbedChat.setBackgroundMode(SWT.INHERIT_FORCE);
        /**
         * 	在SashForm中指定每个控件的相对权重。这将决定每个控件占总宽度(如果SashForm具有水平方向)或
         * 	总高度(如果SashForm具有垂直方向)的百分比。权重必须为正值，并且对于SashForm的每个非sash子
         * 	元素必须有一个条目。
         */
        sashForm.setWeights(new int[] { 4, 10 });
        // 当SashForm中的控件布局好时，指定sash的宽度。
        sashForm.setSashWidth(7);
    }
    
    @Override
    public void dispose() {
        getClient().close();
        closeAllChat();
        super.dispose();
    }
    
    // 	初始化联系人
    public void initContacts() {
        new Thread() {
            public void run() {
            	// 	加载联系人
                doLoadContacts();
            }
        }.start();
    }
    
    protected abstract void doLoadContacts();
    
    protected abstract void onLoadContacts(boolean success);
    
    protected void notifyLoadContacts(final boolean success) {
        IMPlugin.runOnUI(new Runnable() {
            @Override
            public void run() {
                onLoadContacts(success);
            }
        });
    }
    // 	填充工具
    private void contributeToActionBars() {
    	// 	返回部分站点操作栏
        IActionBars bars = getViewSite().getActionBars();
        
        fillLocalToolBar(bars.getToolBarManager());
    }
    
    /*
     * 	设置菜单栏和子菜单
     * 	动态创建上下文菜单
     */
    protected void fillContextMenu(IMenuManager manager) {
        manager.add(login);
        manager.add(exit);
        manager.add(new Separator());
        manager.add(toggleLeft);
        
        manager.add(settings);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    /*
     * IToolBarManager:接口提供用于管理对工具栏的协议。
     *	工具栏按钮	
     */
    protected void fillLocalToolBar(IToolBarManager manager) {
        manager.add(login);
        manager.add(exit);
        manager.add(new Separator());
        manager.add(toggleLeft);
        manager.add(settings);
    }
    // 	界面视图显示
    protected void makeActions() {
        login = new LoginAction(this);
        settings = new SettingAction(this);
        exit = new DisconnectAction(this);
        toggleLeft = new ToggleContactsAction(this);
        broadcast = new BroadcastAction(this);
    }
    
    
    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
    
    }
    
    @Override
    public boolean show(ShowInContext context) {
    	//	返回选择项，或null表示没有选择项。
        ISelection sel = context.getSelection();
        /*
         * instanceof:它的作用是判断其左边对象是否为其右边类的实例，返回boolean类型的数据。可以用来判断
         *	 继承中的子类的实例是否为父类的实现。
         */
        if (sel instanceof IStructuredSelection) {
        	// getFirstElement():返回此选择项中的第一个元素，如果选项中为空，则返回null。
            Object obj = ((IStructuredSelection) sel).getFirstElement();
            return show(obj);
        }
        return false;
    }
    
    protected boolean show(Object selection) {
        return false;
    }
    
    // 	创建一个按钮
    protected TreeViewer createTab(String name, CTabFolder tabFolder) {
    	/*
    	 *	 构造这个类的一个新实例，给出它的父类(必须是一个CTabFolder)和一个描述其行为和外观的样式值。
    	 *	 这个实例项添加到其父项维护的项的末尾。
    	 */
        CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
        // 	设置要在选项卡上显示的文本。
        tabItem.setText(name);
        /*
         * 	构造该类的新实例，给出该类的父类和描述其行为和外观的样式值。
         */
        Composite composite = new Composite(tabFolder, SWT.NONE);
        //	 设置当用户选择选项卡项时用于填充选项卡文件夹的客户端区域的控件。
        tabItem.setControl(composite);
        // 	TreeColumnLayout():创建一个新的树列布局
        composite.setLayout(new TreeColumnLayout());
        // 	在给定父控件下一个新创建的树控件上创建树视图
        TreeViewer tv = new TreeViewer(composite, SWT.NONE);
        return tv;
    }
    
    public CTabFolder getTabbedChat() {
        return tabbedChat;
    }
    
    
    // 	初始化选项卡视图，比如是聊天、联系人分组等选项卡
    protected void initTrees(TreeViewer... treeViewers) {
        if (treeViewers != null) {
            for (TreeViewer tv : treeViewers) {
                initTree(tv);
            }
        }
    }
    
    // 	详细初始化选项卡
    protected void initTree(TreeViewer tv) {
    	//	设置此TreeViewer使用的内容提供者
        tv.setContentProvider(contentProvider);
        // 	在此查看器中添加用于双击的侦听器。如果已经有一个相同的侦听器，则没有效果
        tv.addDoubleClickListener(doubleClicker);
    };
    
    public abstract SmartClient getClient();
    
    public abstract IMContactView createContactsUI();
    
    public abstract IMChatConsole createConsoleUI(IContact contact);
    
    
    public void toggleContacts() {
        String key = "old_weights";
        Object old = sashForm.getData(key);
        // 	若果old是之前定的分配比例，则执行else中的程序，按照原来分配，否则按照if中的程序执行分配比例。
        if (old == null) {
            sashForm.setData(key, sashForm.getWeights());
            // 	确定点击toggle Contacts按钮时，好友界面与聊天界面的分配比例
            sashForm.setWeights(new int[] { 0, 1 });
        }
        else {
            sashForm.setData(key, null);
            sashForm.setWeights((int[]) old);
        }
        sashForm.layout();
    }
    // 	通过选项卡的ID号找到他的控制台
    public IMChatConsole findConsoleById(String id, boolean show) {
        int count = tabbedChat.getItemCount();
        for (int i = 0; i < count; i++) {
            if (tabbedChat.getItem(i) instanceof IMChatConsole) {
            	//	得到一个指定索引的选项卡
                IMChatConsole t = (IMChatConsole) tabbedChat.getItem(i);
                //	判断ID号找到的选项卡与索引找到的选项卡是否一致
                if (id.equals(t.getUin())) {
                	// 	如果展示这个选项卡，则执行if，否则不执行
                    if (show) {
                        tabbedChat.setSelection(i);
                    }
                    //	返回索引得到的选项卡id
                    return t;
                }
            }
        }
        return null;
    }
    
    // 	打开控制台
    public IMChatConsole openConsole(IContact contact) {
        IMChatConsole console = findConsoleById(contact.getUin(), true);
        if (console == null) {
        	// 	创建控制台图形化界面
            console = createConsoleUI(contact);
            //	 将选择项设置为指定项的选项卡。
            tabbedChat.setSelection(console);
        }
        return console;
    }
    // 	退出登录，关闭所有聊天界面
    public void close() {
        getClient().close();
        closeAllChat();
    }
    
    public void closeAllChat() {
        CTabItem[] items = tabbedChat.getItems();
        if (items != null) {
            for (CTabItem item : items) {
                item.dispose();
            }
        }
        // 	清除所有的聊天记录
        IMHistoryManager.getInstance().flush();
    }
    
    public java.util.List<IMChatConsole> getConsoleList() {
        java.util.List<IMChatConsole> list = new ArrayList<>();
        if (tabbedChat != null) {
            int count = tabbedChat.getItemCount();
            for (int i = 0; i < count; i++) {
                if (tabbedChat.getItem(i) instanceof IMChatConsole) {
                    IMChatConsole t = (IMChatConsole) tabbedChat.getItem(i);
                    list.add(t);
                }
            }
        }
        return list;
    }
}
