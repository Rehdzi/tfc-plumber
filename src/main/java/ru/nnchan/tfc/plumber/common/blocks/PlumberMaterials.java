package ru.nnchan.tfc.plumber.common.blocks;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;

public class PlumberMaterials {
    public static final Material PIPE = new Builder(MaterialColor.METAL).build();

    public static class Builder
    {
        private final MaterialColor color;
        private PushReaction pushReaction = PushReaction.NORMAL;
        private boolean blocksMotion = true;
        private boolean flammable;
        private boolean liquid;
        private boolean replaceable;
        private boolean solid = true;
        private boolean solidBlocking = true;

        public Builder(MaterialColor colorIn)
        {
            this.color = colorIn;
        }

        public Builder liquid()
        {
            this.liquid = true;
            return this;
        }

        public Builder nonSolid()
        {
            this.solid = false;
            return this;
        }

        public Builder noCollider()
        {
            this.blocksMotion = false;
            return this;
        }

        public Builder notSolidBlocking()
        {
            this.solidBlocking = false;
            return this;
        }

        public Builder flammable()
        {
            this.flammable = true;
            return this;
        }

        public Builder replaceable()
        {
            this.replaceable = true;
            return this;
        }

        public Builder destroyOnPush()
        {
            this.pushReaction = PushReaction.DESTROY;
            return this;
        }

        public Builder notPushable()
        {
            this.pushReaction = PushReaction.BLOCK;
            return this;
        }

        public Material build()
        {
            return new Material(color, liquid, solid, blocksMotion, solidBlocking, flammable, replaceable, pushReaction);
        }
    }

}
