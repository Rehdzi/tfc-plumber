package ru.nnchan.tfc.plumber;

import net.dries007.tfc.util.Helpers;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class PlumberTags {
    public static class Blocks {
        public static final TagKey<Block> COPPER_PIPE = create("copper_pipe");

        private static TagKey<Block> create(String id){
            return TagKey.create(Registry.BLOCK_REGISTRY, Helpers.identifier(id));
        }
    }

    public static class Items {
        public static final TagKey<Item> CAN_MANIPULATE_PIPES = create("can_manipulate_pipes");

        private static TagKey<Item> create(String id){
            return TagKey.create(Registry.ITEM_REGISTRY, Helpers.identifier(id));
        }
    }
}
