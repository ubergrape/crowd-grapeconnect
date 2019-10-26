package ut.com.grape.crowd.plugin.grapeconnect;

import org.junit.Test;
import com.grape.crowd.plugin.grapeconnect.api.GrapeConnectComponent;
import com.grape.crowd.plugin.grapeconnect.impl.GrapeConnectComponentImpl;

import static org.junit.Assert.assertEquals;

public class GrapeComponentUnitTest
{
    @Test
    public void testMyName()
    {
        GrapeConnectComponent component = new GrapeConnectComponentImpl(null);
        assertEquals("names do not match!", "GrapeComponent",component.getName());
    }
}