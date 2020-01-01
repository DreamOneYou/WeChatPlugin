package cn.ieclipse.smartim.dialogs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.callback.LoginCallback;

/*
 *	登录对话框
 */
public class LoginDialog extends Dialog {
    private SmartClient client;
    private Text text;
    private Label qrcode;
    
    /*
     * 	创建一个会话框
     */
    public LoginDialog(Shell parentShell) {
        super(parentShell);
    }
    
    /**
     * 	为按钮栏上面的对话框创建并返回内容区域，子类一般调用超类的方法。然后添加控件至返回的复合组件
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(1, false));
        /*
         * QR:Quick Response 是一个近几年来移动设备上超流行的一种编码方式，
         *	 它比传统的Bar Code条形码能存更多的信息，也能表示更多的数据类型
         * 
         * 	二维条码/二维码（2-dimensional bar code）是用某种特定的几何图形按一定规律在平面
         * 	（二维方向上）分布的黑白相间的图形记录数据符号信息的；在代码编制上巧妙地利用构成计算机内部逻辑基础
         * 	的“0”、“1”比特流的概念，使用若干个与二进制相对应的几何形体来表示文字数值信息，通过图象输入设备或
         * 	光电扫描设备自动识读以实现信息自动处理
         * 
         * 	此处就是将一串登录数据信息转换为可扫描的二维码
         */
        qrcode = new Label(container, SWT.NONE);
        qrcode.setAlignment(SWT.CENTER);
        qrcode.setImage(SWTResourceManager.getImage(LoginDialog.class,
                "/icons/full/progress/waiting.gif"));
        qrcode.setLayoutData(
                new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
        new Thread() {
            public void run() {
            	// 	登录
                doLogin();
            }
        }.start();
        
        return container;
    }
    
    /**
     * 	在按钮栏中创建按钮，默认实现在右下角 创建OK，Cancle按钮。
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                true);
        createButton(parent, IDialogConstants.CANCEL_ID,
                IDialogConstants.CANCEL_LABEL, false);
    }
    
    /**
     *  	在二维码还未出现时，显示最初界面大小
     */
    @Override
    protected Point getInitialSize() {
        return new Point(500, 350);
    }
    
    public void setClient(SmartClient client) {
        this.client = client;
    }
    
    // 	登录
    protected void doLogin() {
        if (client == null) {
            return;
        }
        LoginCallback callback = new LoginCallback() {
            @Override
            public void onQrcode(final String path) {
                Display.getDefault().asyncExec(new Runnable() {
                    
                	// 	用于进行二维码显示,之后进行扫码登录
                    @Override
                    public void run() {
                        if (qrcode == null || qrcode.isDisposed()) {
                            return;
                        }
                        // 	此处用于二维码加载
                        Image image;
                        try {
                            image = new Image(Display.getDefault(),
                            		new FileInputStream(path));
                            qrcode.setImage(image);
                            
                            getShell().pack();
                        } catch (FileNotFoundException e) {
                            if (text == null || text.isDisposed()) {
                                return;
                            }
                            text.setText("二维码图片不存在，请确认" + path + "目录可读写");
                        }
                    }
                });
            }
            // 	如果在没有网络或其他无法登陆的情况下，会报以下错误
            @Override
            public void onLogin(final boolean ok, final Exception e) {
                Display.getDefault().asyncExec(new Runnable() {
                    
                    @Override
                    public void run() {
                        if (ok) {
                            close();
                        }
                        else {
                            if (text == null || text.isDisposed()) {
                                return;
                            }
                            text.setText(
                                    e == null ? ""
                                            : e.getMessage() == null
                                                    ? e.toString()
                                                    : e.getMessage());
                            MessageDialog.openWarning(text.getShell(), "登录失败",
                                    e.toString() + "\n请在Error Log中查看详情");
                            IMPlugin.getDefault().log("登录失败", e);
                        }
                    }
                });
            }
        };
        /*
         * 	回调函数不是由该函数的实现方直接调用,
         *	而是在特定的事件或条件发生时由另外的一方调用的,
         *	用于对该事件或条件进行响应。
         */
        client.setLoginCallback(callback);
        // 	此处用于二维码显示之后扫码登录
        client.login();
    }
}
