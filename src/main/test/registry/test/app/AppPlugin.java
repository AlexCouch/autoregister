package registry.test.app;

import com.mrcrayfish.device.MrCrayfishDeviceMod;
import com.mrcrayfish.device.api.ApplicationManager;
import com.mrcrayfish.device.api.app.Application;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import registry.Registry;
import registry.RegistryItem;

import java.util.Set;

public class AppPlugin extends Registry<IAppContainer, Application, CDMRegister> {
    @NotNull
    @Override
    public String getAnnotationName() {
        return CDMRegister.class.getCanonicalName();
    }

    @NotNull
    @Override
    public Class<Application> getRequiredSuperclass() {
        return Application.class;
    }

    @NotNull
    @Override
    public ResourceLocation getRegistryId() {
        return new ResourceLocation("cdm", "app_registry");
    }

    @Override
    public void addItem(CDMRegister annotation, @NotNull Class<? extends Application> annotatedClass) {
        final IAppContainer app = newAppContainer(annotation, annotatedClass);
        this.getRegistry().add(app);
    }

    @Override
    public void registerItems(@NotNull Set<? extends RegistryItem<?>> items) {
        for(RegistryItem<?> item : items){
            if(item instanceof IAppContainer){
                final IAppContainer app = (IAppContainer)item;
                if(app.getType() != null){
                    Class clazz = app.getType();
                    if(Application.class.isAssignableFrom(clazz)){
                        Class<? extends Application> cls = (Class<? extends Application>)clazz;
                        if(app.isDebug()){
                            if(MrCrayfishDeviceMod.DEVELOPER_MODE){
                                ApplicationManager.registerApplication(app.getAppId(), cls);
                            }
                        }else{
                            ApplicationManager.registerApplication(app.getAppId(), cls);
                        }
                    }
                }
            }
        }
    }

    private static IAppContainer newAppContainer(CDMRegister app, Class<? extends Application> clazz){
        MrCrayfishDeviceMod.getLogger().info("Constructing new apps container...");
        final IAppContainer ret = new IAppContainer(){

            @Override
            public Class<? extends Application> getType() {
                return clazz;
            }

            @Override
            public ResourceLocation getAppId() {
                return new ResourceLocation(app.modid(), app.appId());
            }

            @Override
            public boolean isSystem() {
                return app.isSystem();
            }

            @Override
            public boolean isDebug(){
                return app.isDebug();
            }
        };
        MrCrayfishDeviceMod.getLogger().info("\tDone!");
        return ret;
    }
}
