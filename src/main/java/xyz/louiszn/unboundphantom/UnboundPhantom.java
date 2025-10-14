package xyz.louiszn.unboundphantom;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class UnboundPhantom implements ModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("UnboundPhantom");

    @Override
    public void onInitialize() {
        BiomeModifications.addSpawn(
                BiomeSelectors.foundInTheEnd(),
                SpawnGroup.CREATURE,
                EntityType.PHANTOM,
                5,
                1,
                3
        );

        LOGGER.info("Mod loaded successfully");
    }
}
