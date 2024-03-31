package me.kmaxi.vowcloud.Audio.blocksound;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;


public abstract class BlockDefinition {

    public abstract String getConfigString();

    @Nullable
    public abstract String getConfigComment();

    public abstract Component getName();

}
