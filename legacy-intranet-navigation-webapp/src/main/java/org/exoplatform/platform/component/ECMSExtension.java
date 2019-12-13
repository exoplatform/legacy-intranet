package org.exoplatform.platform.component;

import org.exoplatform.wcm.webui.seo.UISEOToolbarForm;
import org.exoplatform.webui.core.UIComponent;

/**
 * This class will be used when ECMS addon is installed only. This will decouple
 * ECMS code part from principal class
 */
public class ECMSExtension {

  private ECMSExtension() {
  }

  public static void addSEO(UIAdminToolbarContainer uiAdminToolbarContainer) throws Exception {
    uiAdminToolbarContainer.addChild(UISEOToolbarForm.class, null, null);
  }

  public static boolean isSEO(UIComponent component) {
    return component instanceof UISEOToolbarForm;
  }

}
