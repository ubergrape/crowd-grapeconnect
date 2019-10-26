package com.grape.crowd.plugin.grapeconnect.impl;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.grape.crowd.plugin.grapeconnect.api.GrapeConnectComponent;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService({GrapeConnectComponent.class})
@Named("GrapeConnectComponent")
public class GrapeConnectComponentImpl implements GrapeConnectComponent {
    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @Inject
    public GrapeConnectComponentImpl(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getName() {
        if (null != applicationProperties) {
            return "component:" + applicationProperties.getDisplayName();
        }
        return "GrapeComponent";
    }
}