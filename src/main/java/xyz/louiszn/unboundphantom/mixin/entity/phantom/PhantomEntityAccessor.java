package xyz.louiszn.unboundphantom.mixin.entity.phantom;

import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PhantomEntity.class)
public interface PhantomEntityAccessor {
    @Accessor("targetPosition")
    void setTargetPosition(Vec3d position);

    @Accessor("targetPosition")
    Vec3d getTargetPosition();

    @Accessor("circlingCenter")
    BlockPos getCirclingCenter();
}
