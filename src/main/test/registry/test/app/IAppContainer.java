package registry.test.app;

import com.mrcrayfish.device.api.app.Application;
import net.minecraft.util.ResourceLocation;
import registry.RegistryItem;

public interface IAppContainer extends RegistryItem<Class<? extends Application>> {
    ResourceLocation getAppId();
    boolean isDebug();
}
