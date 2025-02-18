package net.lunade.copper.mixin;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VibrationListener.class)
public class VibrationListenerMixin {

    @Inject(at = @At("RETURN"), method = "handleGameEvent")
    public void handleGameEvent(ServerLevel serverLevel, GameEvent.Message message, CallbackInfoReturnable<Boolean> infoReturnable) {
        if (infoReturnable.getReturnValue()) {
            BlockPos checkPos = new BlockPos(message.source());
            BlockEntity blockEntity = serverLevel.getBlockEntity(checkPos);
            if (blockEntity instanceof CopperPipeEntity pipeEntity) {
                if (pipeEntity.inputGameEventPos != null && pipeEntity.gameEventNbtVec3 != null && pipeEntity.noteBlockCooldown <= 0) {
                    serverLevel.sendParticles(new VibrationParticleOption(new BlockPositionSource(pipeEntity.inputGameEventPos), 5), pipeEntity.gameEventNbtVec3.x(), pipeEntity.gameEventNbtVec3.y(), pipeEntity.gameEventNbtVec3.z(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    pipeEntity.inputGameEventPos = null;
                    pipeEntity.gameEventNbtVec3 = null;
                }
            }
        }
    }

}
