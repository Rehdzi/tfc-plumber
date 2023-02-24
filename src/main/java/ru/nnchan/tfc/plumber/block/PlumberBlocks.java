package ru.nnchan.tfc.plumber.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.nnchan.tfc.plumber.Plumber;
import ru.nnchan.tfc.plumber.item.PlumberItems;
import ru.nnchan.tfc.plumber.block.custom.*;

import java.util.function.Supplier;

public class PlumberBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Plumber.MOD_ID);

    public static final RegistryObject<Block> COPPER_PIPE = registerBlock("copper_pipe",
            () -> new PipeBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).noOcclusion()),
            CreativeModeTab.TAB_MISC);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){

        return PlumberItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }

    //public BlockState getStateForPlacement(BlockItemUseContext context)
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
