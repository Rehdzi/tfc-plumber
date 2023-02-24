package ru.nnchan.tfc.plumber;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import ru.nnchan.tfc.plumber.block.PlumberBlocks;
import ru.nnchan.tfc.plumber.item.PlumberItems;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Plumber.MOD_ID)
public class Plumber
{
    public static final String MOD_ID = "plumber";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Plumber()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        PlumberItems.register(eventBus);
        PlumberBlocks.register(eventBus);

        // Register the setup method for modloading
        eventBus.addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

}
