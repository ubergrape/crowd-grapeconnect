package it.com.grape.crowd.plugin.grapeconnect;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.grape.crowd.plugin.grapeconnect.api.GrapeConnectComponent;
import com.atlassian.sal.api.ApplicationProperties;

import static org.junit.Assert.assertEquals;

@RunWith(AtlassianPluginsTestRunner.class)
public class GrapeComponentWiredTest
{
    private final ApplicationProperties applicationProperties;
    private final GrapeConnectComponent chatgrapeConnectComponent;

    public GrapeComponentWiredTest(ApplicationProperties applicationProperties, GrapeConnectComponent chatgrapeConnectComponent)
    {
        this.applicationProperties = applicationProperties;
        this.chatgrapeConnectComponent = chatgrapeConnectComponent;
    }

    @Test
    public void testMyName()
    {
        assertEquals("names do not match!", "component:" + applicationProperties.getDisplayName(), chatgrapeConnectComponent.getName());
    }
}