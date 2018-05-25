package registry.test;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.component.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import registry.test.app.CDMRegister;

import javax.annotation.Nullable;

@CDMRegister(modid="test", appId = "test_app")
public class TestApp extends Application{

    @Override
    public void init(@Nullable NBTTagCompound intent) {
        String message = "Some test application thingy!";
        Text text = new Text(message, 5, 5, Minecraft.getMinecraft().fontRenderer.getStringWidth(message));
        this.addComponent(text);
    }

    @Override
    public void load(NBTTagCompound tagCompound) {

    }

    @Override
    public void save(NBTTagCompound tagCompound) {

    }
}
