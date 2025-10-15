package xyz.louiszn.unboundphantom.mixin.entity.phantom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
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

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        PhantomEntity self = (PhantomEntity) (Object) this;

        boolean result = super.damage(world, source, amount);

        if (!self.isDead()) {
            tryTeleportAway(self);
        }

        return result;
    }

    @Unique
    private boolean tryTeleportAway(PhantomEntity phantom) {
        var world = phantom.getEntityWorld();
        var random = world.getRandom();

        double x = phantom.getX();
        double y = phantom.getY();
        double z = phantom.getZ();

        for (int i = 0; i < 16; ++i) {
            double offsetX = x + (random.nextDouble() - 0.5) * 64.0;
            double offsetY = y + (random.nextInt(32) - 16);
            double offsetZ = z + (random.nextDouble() - 0.5) * 64.0;

            if (offsetY < world.getBottomY() + 5) continue;

            // ensure it won't teleport into a block
            if (world.isAir(BlockPos.ofFloored(offsetX, offsetY, offsetZ))
                    && world.isAir(BlockPos.ofFloored(offsetX, offsetY + 1, offsetZ))) {

                phantom.teleport(offsetX, offsetY, offsetZ, true);
                world.emitGameEvent(GameEvent.TELEPORT, this.getEntityPos(), GameEvent.Emitter.of(this));
                phantom.playSound(net.minecraft.sound.SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

                return true;
            }
        }

        return false;
    }
}
