package com.grape.crowd.plugin.grapeconnect;

import com.atlassian.crowd.event.user.*;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

import javax.inject.Inject;
import java.util.Date;

public class GrapeEventListener {

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    @Inject
    public GrapeEventListener(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @com.atlassian.event.api.EventListener
    public void printUserCredentialUpdatedEvent(UserCredentialUpdatedEvent event) {
        System.out.println(new Date().toString() + " Grape Connect: User " + event.getUsername() + " updated credential.");
        logoutUser(event.getUsername());
    }

    @com.atlassian.event.api.EventListener
    public void printUserCredentialUpdatedEvent(UsersDeletedEvent event) {
        String[] usernames = new String[event.getUsernames().size()];
        usernames = event.getUsernames().toArray(usernames);
        for (int i = 0; i < usernames.length; i++) {
            System.out.println(new Date().toString() + " Crape Connect: User " + usernames[i] + " deleted.");
            logoutUser(usernames[i]);
        }
    }

    private void logoutUser(String username) {
        //get config
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        Object urlObj = pluginSettings.get(GrapeAdminServlet.PLUGIN_STORAGE_KEY + "." + GrapeAdminServlet.CONFIG_URL);
        Object tokenObj = pluginSettings.get(GrapeAdminServlet.PLUGIN_STORAGE_KEY + "." + GrapeAdminServlet.CONFIG_TOKEN);
        String url = (urlObj != null) ? (String) urlObj : null;
        String token = (tokenObj != null) ? (String) tokenObj : null;

        //logout
        if (null != url && null != token) {
            GrapeLogout.logout(url, token, username);
        }
    }
}
