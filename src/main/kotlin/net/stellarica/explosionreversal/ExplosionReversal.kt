package net.stellarica.explosionreversal

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.lifecycle.api.event.ServerTickEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ExplosionReversal : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("Explosion Reversal")

    val queue = mutableListOf<ExplosionReversalData>()

    var regenTime = 5000L
        private set


    override fun onInitialize(mod: ModContainer) {
        ServerTickEvents.START.register(ServerTickEvents.Start {
            queue.removeIf { data -> (data.regenTime < System.currentTimeMillis()).also { if (it) {data.revert()}} }
        })
    }
}
