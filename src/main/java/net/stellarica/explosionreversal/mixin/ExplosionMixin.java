package net.stellarica.explosionreversal.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import kotlin.random.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.stellarica.explosionreversal.ExplosionReversal;
import net.stellarica.explosionreversal.ExplosionReversalData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Explosion.class)
public class ExplosionMixin {

	@Final
	@Shadow
	private World world;
	@SuppressWarnings("rawtypes")
	@Inject(
			method = "affectWorld",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/Block;onDestroyedByExplosion(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/explosion/Explosion;)V",
					shift = At.Shift.BY,
					by = -9
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	void onBlockExplode(boolean particles, CallbackInfo ci, boolean a, ObjectArrayList b, boolean c, ObjectListIterator d, BlockPos blockPos, BlockState state, Block block) {
		if (state.isAir()) return;
		ExplosionReversal.INSTANCE.getQueue().add(new ExplosionReversalData(
				System.currentTimeMillis() + ExplosionReversal.INSTANCE.getRegenTime() + Random.Default.nextInt(30000),
				blockPos,
				world,
				state,
				state.hasBlockEntity() ? world.getBlockEntity(blockPos).toIdentifiedLocatedNbt() : null
		));
		if (state.hasBlockEntity()) world.removeBlockEntity(blockPos); // easy way to get them to not drop items
	}


	@Redirect(
			method = "affectWorld",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/Block;shouldDropItemsOnExplosion(Lnet/minecraft/world/explosion/Explosion;)Z"
			)
	)
	boolean preventItemDrops(Block instance, Explosion explosion) {
		return false;
	}
}
