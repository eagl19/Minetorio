package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.util.CachedBlockPos;

public interface IGeneratorBlockEntity {
    CachedBlockPos getCachedTargets();
    void initializedTargets();
}
