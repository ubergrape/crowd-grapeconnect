package com.grape.crowd.plugin.grapeconnect;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URI;
import java.util.Map;

import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;

import javax.inject.Inject;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.google.common.collect.Maps;

@Scanned
public class GrapeAdminServlet extends HttpServlet {
    public static final String PLUGIN_STORAGE_KEY = "com.grape.crowd.plugin.grapeconnect";
    public static final String CONFIG_URL = "url";
    public static final String CONFIG_TOKEN = "token";
    @ComponentImport
    private final UserManager userManager;
    @ComponentImport
    private final LoginUriProvider loginUriProvider;
    @ComponentImport
    private final TemplateRenderer renderer;
    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    @Inject
    public GrapeAdminServlet(UserManager userManager, LoginUriProvider loginUriProvider, TemplateRenderer renderer, PluginSettingsFactory pluginSettingsFactory) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.renderer = renderer;
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        renderPage(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //save config from post req
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        if (request.getParameter(CONFIG_URL) != null && request.getParameter(CONFIG_TOKEN) != null) {
            pluginSettings.put(PLUGIN_STORAGE_KEY + "." + CONFIG_URL, request.getParameter(CONFIG_URL));
            pluginSettings.put(PLUGIN_STORAGE_KEY + "." + CONFIG_TOKEN, request.getParameter(CONFIG_TOKEN));

            //test url & token
            GrapeLogout.logout(request.getParameter(CONFIG_URL), request.getParameter(CONFIG_TOKEN), "NULL");
        }

        renderPage(request, response);
    }

    private void renderPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserKey userKey = userManager.getRemoteUserKey();
        if (userKey == null || !userManager.isSystemAdmin(userKey)) {
            redirectToLogin(request, response);
            return;
        }

        //get config
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        Object urlObj = pluginSettings.get(PLUGIN_STORAGE_KEY + "." + CONFIG_URL);
        Object tokenObj = pluginSettings.get(PLUGIN_STORAGE_KEY + "." + CONFIG_TOKEN);
        String url = (urlObj != null) ? (String) urlObj : "";
        String token = (tokenObj != null) ? (String) tokenObj : "";

        //variables for velocity page
        Map<String, Object> context = Maps.newHashMap();
        context.put("input_url", url);
        context.put("input_token", token);

        //render page
        response.setContentType("text/html;charset=utf-8");
        renderer.render("admin.vm", context, response.getWriter());
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
    }

    private URI getUri(HttpServletRequest request) {
        StringBuffer builder = request.getRequestURL();
        if (request.getQueryString() != null) {
            builder.append("?");
            builder.append(request.getQueryString());
        }
        return URI.create(builder.toString());
    }
}
