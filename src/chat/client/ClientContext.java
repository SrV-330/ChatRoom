package chat.client;

import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

public class ClientContext {
	

	public static final void init(){
		try {
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			BeautyEyeLNFHelper.frameBorderStyle=BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			UIManager.put("RootPane.setupButtonVisible", false);
			BeautyEyeLNFHelper.debug=false;
			BeautyEyeLNFHelper.translucencyAtFrameInactive=false;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			
		} catch (Exception e) {
			
		}
	}
	

}
