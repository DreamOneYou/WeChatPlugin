package cn.ieclipse.smartim.console;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.preferences.HotKeyFieldEditor;
import cn.ieclipse.smartim.preferences.HotKeyPreferencePage;
import cn.ieclipse.util.StringUtils;

public class TabComposite extends Composite {
    
    private ToolBar toolBar;
    private SashForm sashForm;
    private Browser browser;
    private Text text;
    private IMChatConsole console;
    private boolean prepared = false;
    
    public TabComposite(IMChatConsole console) {
        this(console.getParent());
        this.console = console;
    }
    
    /**
     * 	创建复合.
     * @param parent
     * @param style
     */
    public TabComposite(Composite parent) {
        super(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);
        
        toolBar = new ToolBar(this, SWT.FLAT | SWT.VERTICAL);
        toolBar.setBackground(
                SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        toolBar.setLayoutData(
                new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
                
        sashForm = new SashForm(this, SWT.VERTICAL);
        sashForm.setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        sashForm.setSashWidth(6);
        
        browser = new Browser(sashForm, SWT.NONE);
        
        text = new Text(sashForm, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                resize(true);
            }
        });
        
        sashForm.addControlListener(new ControlListener() {
            @Override
            public void controlResized(ControlEvent e) {
                resize(false);
            }
            
            @Override
            public void controlMoved(ControlEvent e) {
            }
        });
        sashForm.setWeights(new int[] { 100, 1 });
        sashForm.pack();
        sashForm.layout();
        setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        
        text.addKeyListener(inputListener);
        text.setBackground(
                SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
        // 	输入要聊天的信息
        text.setToolTipText("请在此输入内容");
        
        browser.setJavascriptEnabled(true);
        
        // 	用于控制历史信息是否会显示
        boolean f = browser.setText(
                StringUtils.file2string(IMChatConsole.class, "history.html"),
                true);

        browser.addProgressListener(new ProgressListener() {
            
            @Override
            public void completed(ProgressEvent event) {
                prepared = true;
            }
            
            @Override
            public void changed(ProgressEvent event) {
            
            }
        });
        browser.addLocationListener(new LocationListener() {
            
            @Override
            public void changing(LocationEvent event) {
                String url = event.location;
                if (url.startsWith("about:")) {
                    return;
                }
                event.doit = false;
                if (console != null) {
                    if (url.startsWith("user://") && url.endsWith("/")) {
                        url = url.substring(0, url.length() - 1);
                    }
                }
            }
            
            @Override
            public void changed(LocationEvent event) {
                System.out.println(event);
            }
        });
        
        browser.addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
                if (e.widget == browser) {
                    browser.forceFocus();
                }
            }
        });
        text.addMouseMoveListener(new MouseMoveListener() {
            
            @Override
            public void mouseMove(MouseEvent e) {
                text.forceFocus();
            }
        });
    }
    
    private void resize(boolean auto) {
        Point p = sashForm.getSize();
        int minHeight = Math.min(getInputHeight(), p.y - 20);
        sashForm.setWeights(new int[] { p.y - minHeight, minHeight });
        if (auto) {
            text.setTopIndex(Integer.MAX_VALUE);
        }
    }
    
    private int getInputHeight() {
        return text.getLineCount() * text.getLineHeight() + 2;
    }
    
    private KeyListener inputListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            String key = HotKeyFieldEditor.keyEvent2String(e);
            IPreferenceStore store = IMPlugin.getDefault().getPreferenceStore();
            if (key.equals(store.getString(HotKeyPreferencePage.KEY_SEND))) {
                e.doit = false;
                String input = text.getText();
                if (console != null && !input.isEmpty()) {
                    console.send(input);
                }
                text.setText("");
            }
            else if (key
                    .equals(store.getString(HotKeyPreferencePage.KEY_HIDE))) {
                e.doit = false;
            }
            else if (key.equals(
                    store.getString(HotKeyPreferencePage.KEY_HIDE_CLOSE))) {
                e.doit = false;
                if (console != null) {
                    console.dispose();
                }
            }
            else if (key.equals(
                    store.getString(HotKeyPreferencePage.KEY_INPUT_ESC))) {
                e.doit = false;
            }
        }
    };
    
    // 	监听是否按回车发送消息,然后将消息存储到历史信息里
    private KeyListener inputListener2 = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            String key = HotKeyFieldEditor.keyEvent2String(e);
            if (key.equals("CR")) {
                e.doit = false;
                String input = text.getText();
                if (!input.isEmpty()) {
                    addHistory(input, true);
                }
                text.setText("");
            }
        }
    };
      
    //	 用于操控图片和文件按钮，如果没有该方法，这两个按钮都将无法显示。
    public ToolBar getToolBar() {
        return toolBar;
    }  
    private boolean isReady() {
        if (prepared) {
            return true;
        }
        return false;
    }
    // 	检查当前是否有新的线程运行，如果有，将其暂停
    private void checkBrowser() {
        try {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    int count = 20;
                    while (!isReady()) {
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        count--;
                        if (count <= 0) {
                            prepared = true;
                            break;
                        }
                    }
                }
            };
            thread.start();
            thread.join(5000);
        } catch (InterruptedException e) {
            // 	TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //	 向历史信息记录里面添加新信息
    public void addHistory(final String msg, boolean scrollLock) {
        if (msg == null) {
            return;
        }
        if (!prepared) {
            checkBrowser();
        }
        String text = msg.replace("'", "&apos;");// .replaceAll("\r?\n",
                                                 // "<br>");
        final StringBuilder sb = new StringBuilder();
        sb.append("add_log('");
        sb.append(text);
        sb.append("'");
        
        if (!scrollLock) {
            sb.append(", true");
        }
        sb.append(");");
        // 	显示聊天记录,使其正常进行运行
        IMPlugin.runOnUI(new Runnable() {
            @Override
            public void run() {
                appendHistory(sb.toString(), msg);
            }
        });
    }
    // 	用于显示聊天记录,如果没有这个方法,你将无法看到你聊天的信息。
    public void appendHistory(String text, String msg) {
        if (!browser.execute(text)) {
            if (!browser.execute(
                    "add_log('<div class=\"error\">添加到聊天记录失败，可能是因为消息中包含某些特殊字符引</div>', true)")) {

            }
        }
    }
    // 	清除历史聊天记录
    public void clearHistory() {
        browser.execute("clear_log()");
    }
    
    public void doPaste() {
        addHistory("paste", true);
    }
    
    public static void main(String[] args) {
        try {
            Display display = Display.getDefault();
            Shell shell = new Shell(display);
            shell.setSize(new Point(400, 300));
            shell.setLayout(new FillLayout());
            TabComposite comp = new TabComposite(shell);
            shell.open();
            shell.layout();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
