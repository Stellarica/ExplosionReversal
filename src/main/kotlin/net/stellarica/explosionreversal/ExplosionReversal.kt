package net.stellarica.explosionreversal

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ExplosionReversal : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("Explosion Reversal")

    override fun onInitialize(mod: ModContainer) {
        LOGGER.info("Hello Quilt world from {}!", mod.metadata()?.name())

    }
}