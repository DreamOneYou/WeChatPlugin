package cn.ieclipse.smartim;

import java.io.File;

import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import cn.ieclipse.smartim.preferences.SettingsPerferencePage;

/**
 * 这个激活类来控制插件的生命周期
 */
public class IMPlugin extends AbstractUIPlugin {
    
    // 	插件的唯一ID号
    public static final String PLUGIN_ID = "cn.ieclipse.smartWeChat"; 
    
    // 	公共接口
    private static IMPlugin plugin;
    
    /**
     * 	构造函数
     */
    public IMPlugin() {
    }
    
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }
    
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }
    
    /**
     * 	返回一个公共接口
     * @return the shared instance
     */
    public static IMPlugin getDefault() {
        if (plugin == null) {
            plugin = new IMPlugin();
        }
        return plugin;
    }
    
    /**
     *
     *	返回一个基于这个插件相对位置的图像文件的图片描述
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
    
    public static ImageDescriptor getSharedImage(String name) {
        return PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(name);
    }
    
    public static Color getSystemColor(int colorId) {
        return SWTResourceManager.getColor(colorId);
    }
    
    // IProxyService:管理代理数据和相关信息.代理服务注册为OSGi(面向Java的动态模型系统)服务。
    //	 客户端可以从其上下文包或服务跟踪器中获取服务的实例。
    public IProxyService proxyService;
    
    public IProxyService getProxyService() {
        if (proxyService == null) {
        	/*
        	 * getServiceReference:基于这个服务名称注册的类，
        	 * 	返回一个工程的集合，如果在没有登记任何符合要求的服务，返回可以为空
        	 * getBundleContext：返回此包的签名者的证书以及这些签名者的证书链。
        	 * getBundle：返回与此插件有关联的包
        	 */
            ServiceReference<IProxyService> ref = getBundle().getBundleContext()
                    .getServiceReference(IProxyService.class);
            /*
             * getService: ref参考的服务是否被使用。如果场景包对服务的使用计数为零，或者服务未注册，
             *	 则返回false，否则返回true
             */
            proxyService = getBundle().getBundleContext().getService(ref);
        }
        
        return proxyService;
    }
   /*
    *	 得到文件的命令状态
    * 
    * toFile:返回这个文件的相对路径
    * 
    * makeAbsolute：返回具有此路径的段和设备id的相对路径。绝对路径以路径分隔符开始，而相对路径没有。
    * 	如果此路径是相对的，则只简单的返回它。
    * 
    * getStateLocation：返回这个插件的首选项
    */
    public File getStateDir() {
        return getStateLocation().makeAbsolute().toFile();
    }
    
    /*
     * 	登记日志，插件的ID号
     * getLog:返回这个插件的日志
     * log：记录给定的状态
     */
    
    public void log(String msg, Throwable e) {
        if (e == null) {
            IStatus info = new Status(IStatus.INFO, IMPlugin.PLUGIN_ID, msg);
            getLog().log(info);
        }
        else {
            IStatus info = new Status(IStatus.ERROR, IMPlugin.PLUGIN_ID, msg,
                    e);
            getLog().log(info);
        }
    }
    
    public void log(String msg) {
        log(msg, null);
    }
    
    // 	登记警告日志
    public void warn(String msg) {
        IStatus info = new Status(IStatus.WARNING, IMPlugin.PLUGIN_ID, msg);
        getLog().log(info);
    }
    
    //	默认界面展示
    public static void runOnUI(Runnable runnable) {
        Display display = Display.getDefault();
        if (display != null) {
            display.asyncExec(runnable);
        }
    }
    
    public boolean enable = true;
    
    public static void setEnable(boolean enable) {
        getDefault().enable = enable;
    }
    /*
     * 	设置通知
     * setValue:使用给定名称设置布尔值首选项的当前值。
     * getPreferenceStore:返回此UI（User Interface：用户界面）插件的首选项存储
     */
    public static void setNotify(boolean enable) {
        getDefault().getPreferenceStore()
                .setValue(SettingsPerferencePage.NOTIFY_ENABLE, enable);
    }
}
