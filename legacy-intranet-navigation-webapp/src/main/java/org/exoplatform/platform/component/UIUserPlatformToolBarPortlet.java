/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.platform.component;

import java.util.Collection;
import java.util.Collections;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.portal.config.UserPortalConfig;
import org.exoplatform.portal.mop.SiteKey;
import org.exoplatform.portal.mop.navigation.Scope;
import org.exoplatform.portal.mop.user.*;
import org.exoplatform.portal.webui.util.NavigationURLUtils;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.service.LinkProvider;
import org.exoplatform.social.webui.Utils;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

/**
 * Portlet manages profile.<br>
 */
@ComponentConfig(lifecycle = UIApplicationLifecycle.class,

template = "app:/groovy/platformNavigation/portlet/UIUserPlatformToolBarPortlet/UIUserPlatformToolBarPortlet.gtmpl")
public class UIUserPlatformToolBarPortlet extends UIPortletApplication {

    private static final Log LOG = ExoLogger.getLogger(UIUserPlatformToolBarPortlet.class);
    private String currentPortalName = null;
    private boolean socialPortal = false;
    private static final String USER = "/user/";
    private static final String WIKI_HOME = "/WikiHome";
    private static final String WIKI_REF = "wiki";

  public UIUserPlatformToolBarPortlet() throws Exception {

  }
    public User getUser() throws Exception {
        OrganizationService service = getApplicationComponent(OrganizationService.class);
        String userName = Util.getPortalRequestContext().getRemoteUser();
        User user = service.getUserHandler().findUserByName(userName);
        return user;
    }

  private String getCurrentPortalName() {
    return Util.getPortalRequestContext().getPortalOwner();
  }

  public boolean hasSystemProfile(String profile) {
    return ExoContainer.hasProfile(profile);
  }

  public static UserPortal getUserPortal() {
    UserPortalConfig portalConfig = Util.getPortalRequestContext().getUserPortalConfig();
    return portalConfig.getUserPortal();
  }

  public Collection<UserNode> getUserNodes(UserNavigation nav) {
    UserPortal userPortall = getUserPortal();
    if (nav != null) {
      try {
        UserNode rootNode = userPortall.getNode(nav, Scope.CHILDREN, null, null);
        return rootNode.getChildren();
      } catch (Exception exp) {
        log.warn(nav.getKey().getName() + " has been deleted");
      }
    }
    return Collections.emptyList();
  }

    public String getAvatarURL() {
      Identity identity = Utils.getIdentityManager()
                               .getOrCreateIdentity(OrganizationIdentityProvider.NAME,
                                                    Util.getPortalRequestContext().getRemoteUser());
      String ownerAvatar = identity.getProfile().getAvatarUrl();
      if (ownerAvatar == null || ownerAvatar.isEmpty()) {
        ownerAvatar = LinkProvider.PROFILE_DEFAULT_AVATAR_URL;
      }
      return ownerAvatar;
    }

    public String getWikiURL() {
        return NavigationURLUtils.getURLInCurrentPortal(WIKI_REF) + USER + Util.getPortalRequestContext().getRemoteUser() + WIKI_HOME;
    }
}