package cn.ieclipse.smartim.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

/*
 *	内容太提供者
 * 	好友列表提供者
 */
public abstract class IMContactContentProvider
        implements IStructuredContentProvider, ITreeContentProvider {
    protected boolean check = false;
    protected IMContactView fView;
    
    public IMContactContentProvider(IMContactView view, boolean check) {
        this.fView = view;
        this.check = check;
    }
}