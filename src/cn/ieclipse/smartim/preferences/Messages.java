package cn.ieclipse.smartim.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "cn.ieclipse.smartim.preferences.messages"; //$NON-NLS-1$
    public static String HotKeyPreferencePage_close_chat;
    public static String HotKeyPreferencePage_desc;
    public static String HotKeyPreferencePage_hide_chat;
    public static String HotKeyPreferencePage_hide_input;
    public static String HotKeyPreferencePage_input;
    public static String HotKeyPreferencePage_next_chat;
    public static String HotKeyPreferencePage_prev_chat;
    public static String HotKeyPreferencePage_send;

    public static String SettingsPerferencePage_desc;
    public static String SettingsPerferencePage_hide_my_input;
    public static String SettingsPerferencePage_log_history;
    public static String SettingsPerferencePage_notify_dismiss;
    public static String SettingsPerferencePage_notify_enable;
    public static String SettingsPerferencePage_notify_friend;
    public static String SettingsPerferencePage_notify_group;
    public static String SettingsPerferencePage_notify_unknown;
    public static String SettingsPerferencePage_notify_unread;
    public static String SmartWeChatPreferencePage_desc;
    public static String SmartWeChatPreferencePage_Email;
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
    
    private Messages() {
    }
}
