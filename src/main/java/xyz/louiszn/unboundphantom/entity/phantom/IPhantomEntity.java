package xyz.louiszn.unboundphantom.entity.phantom;

import net.minecraft.entity.mob.Monster;
import xyz.louiszn.unboundphantom.entity.phantom.PhantomMovementType;

public interface IPhantomEntity extends Monster {
    PhantomMovementType unbound$getMovementType();
    void unbound$setMovementType(PhantomMovementType type);
}
