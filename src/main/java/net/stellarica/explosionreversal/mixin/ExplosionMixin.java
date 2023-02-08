package net.stellarica.explosionreversal.mixin;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.stellarica.explosionreversal.ExplosionReversal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Explosion.class)
public class ExplosionMixin {

	@Final
	@Shadow
	private World world;
	@Inject(
			method = "affectWorld",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z",
					shift = At.Shift.BEFORE
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	void onBlockExplode(boolean particles, CallbackInfo ci, boolean idk, ObjectListIterator idc, BlockPos blockPos) {
		ExplosionReversal.INSTANCE.getLOGGER().warn("Exploded: " + world.getBlockState(blockPos));
	}
}
