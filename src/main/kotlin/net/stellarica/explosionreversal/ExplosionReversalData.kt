package net.stellarica.explosionreversal

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

data class ExplosionReversalData(
	val regenTime: Long,
	val pos: BlockPos,
	val world: World,
	val state: BlockState,
	val entity: NbtCompound?
) {
	fun revert() {
		if (!world.getBlockState(pos).isAir) return
		world.setBlockState(pos, state, Block.UPDATE_NONE)
		entity?.let {
			world.addBlockEntity(BlockEntity.createFromNbt(pos, state, entity))
		}
	}
}