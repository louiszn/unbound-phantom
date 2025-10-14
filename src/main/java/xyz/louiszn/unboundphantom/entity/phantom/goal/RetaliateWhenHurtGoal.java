package xyz.louiszn.unboundphantom.entity.phantom.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import xyz.louiszn.unboundphantom.entity.phantom.IPhantomEntity;
import xyz.louiszn.unboundphantom.entity.phantom.PhantomMovementType;

import java.util.List;

public class RetaliateWhenHurtGoal extends Goal {
    private final PhantomEntity phantom;
    private LivingEntity lastAttacker;

    public RetaliateWhenHurtGoal(PhantomEntity phantom) {
        this.phantom = phantom;
    }

    @Override
    public boolean canStart() {
        LivingEntity attacker = phantom.getAttacker();
        return attacker != null && attacker != lastAttacker;
    }

    @Override
    public void start() {
        if (phantom.getTarget() != null) {
            return;
        }

        lastAttacker = phantom.getAttacker();
        phantom.setTarget(lastAttacker);

        if (!(phantom.getEntityWorld() instanceof ServerWorld serverWorld)) {
            return;
        }

        List<PhantomEntity> nearbyPhantoms = serverWorld.getEntitiesByClass(
                PhantomEntity.class,
                new Box(phantom.getBlockPos()).expand(128),
                other -> other != phantom && other.getTarget() == null
        );

        if (nearbyPhantoms.isEmpty()) {
            return;
        }

        int amount = 1 + phantom.getRandom().nextInt(Math.min(3, 6));
        int count = Math.min(6, amount);

        for (int i = 0; i < count; i++) {
            PhantomEntity ally = nearbyPhantoms.get(i);

            ally.setTarget(lastAttacker);

            if (ally instanceof IPhantomEntity ipAlly) {
                ipAlly.unbound$setMovementType(PhantomMovementType.SWOOP);
            }
        }
    }
}
