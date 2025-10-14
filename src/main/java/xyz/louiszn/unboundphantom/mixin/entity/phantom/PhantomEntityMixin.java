package xyz.louiszn.unboundphantom.mixin.entity.phantom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.louiszn.unboundphantom.entity.phantom.IPhantomEntity;
import xyz.louiszn.unboundphantom.entity.phantom.PhantomMovementType;
import xyz.louiszn.unboundphantom.entity.phantom.goal.RetaliateWhenHurtGoal;
import xyz.louiszn.unboundphantom.entity.phantom.goal.StartAttackGoal;
import xyz.louiszn.unboundphantom.entity.phantom.goal.SwoopMovementGoal;
import xyz.louiszn.unboundphantom.entity.phantom.goal.WanderAroundGoal;

@Mixin(PhantomEntity.class)
public abstract class PhantomEntityMixin extends MobEntity implements IPhantomEntity {
    protected PhantomEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    PhantomMovementType movementType = PhantomMovementType.WANDER;

    @Override
    public PhantomMovementType unbound$getMovementType() {
        return this.movementType;
    }

    @Override
    public void unbound$setMovementType(PhantomMovementType movementType) {
        this.movementType = movementType;
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void replaceGoals(CallbackInfo ci) {
        goalSelector.clear(goal -> true);
        targetSelector.clear(goal -> true);

        PhantomEntity self = (PhantomEntity) (Object) this;

        goalSelector.add(1, new StartAttackGoal(self));
        goalSelector.add(2, new SwoopMovementGoal(self));
        goalSelector.add(3, new WanderAroundGoal(self));

        targetSelector.add(1, new RetaliateWhenHurtGoal(self));
    }
}
