package de.olivermakesco.b10_01mod.mixin;

import de.olivermakesco.b10_01mod.MojangLookup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.LoginState;
import net.minecraft.world.World;
import net.minecraft.world.entity.ClientPlayerEntity;
import net.minecraft.world.entity.PlayablePlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayablePlayerEntity.class)
public class Mixin_PlayablePlayerEntity {
	@Inject(
			method = "<init>",
			at = @At("RETURN")
	)
	private void setSkin(World world, String username, CallbackInfo ci) {
		if (username == null) return;
		var uid = MojangLookup.getUid(username);
		if (uid == null) return;
		var skin = MojangLookup.getSkinFromUid(uid);
		if (skin == null) return;
		var self = ((PlayablePlayerEntity)(Object)this);
		self.skin = MojangLookup.getCroppedSkinUrl(skin);
		System.out.println("Patching skin - new: " + self.skin);
	}
}
