package net.stellarica.explosionreversal

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.lifecycle.api.event.ServerTickEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ExplosionReversal : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("Explosion Reversal")

    data class ExplosionReversalData(
        val regenTime: Long,
        val pos: BlockPos,
        val world: World,
        val state: BlockState,
        val entity: BlockEntity?
    )

    val queue = mutableListOf<ExplosionReversalData>()

    var regenTime = 5000L
        private set


    override fun onInitialize(mod: ModContainer) {
        LOGGER.info("Hello Quilt world from {}!", mod.metadata()?.name())

        ServerTickEvents.START.register(ServerTickEvents.Start {
            queue.removeIf { data ->
                if (data.regenTime < System.currentTimeMillis()) {
                    data.world.setBlockState(data.pos, data.state)
                    data.entity?.let { data.world.addBlockEntity(it) }
                    return@removeIf true
                }
                false
            }
        })
    }
}
