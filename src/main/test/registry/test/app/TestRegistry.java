package registry.test.app;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import registry.RegistryManager;

@Mod(modid="test", name="Test Mod", version="1.0")
public class TestRegistry {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        RegistryManager.INSTANCE.addRegistry(AppPlugin.class);
    }
}
