package de.olivermakesco.b10_01mod.mixin;

import de.olivermakesco.b10_01mod.MojangLookup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.LoginState;
import net.minecraft.world.World;
import net.minecraft.world.entity.ClientPlayerEntity;
import net.minecraft.world.entity.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class Mixin_ClientPlayerEntity {

	@Inject(
			method = "<init>",
			at = @At("RETURN")
	)
	private void setSkin(MinecraftClient world, World loginState, LoginState i, int par4, CallbackInfo ci) {
		if (i == null) return;
		if (i.username == null) return;
		var uid = MojangLookup.getUid(i.username);
		if (uid == null) return;
		var skin = MojangLookup.getSkinFromUid(uid);
		if (skin == null) return;
		var self = ((ClientPlayerEntity)(Object)this);
		self.skin = skin;
		System.out.println("Patching skin - new: " + self.skin);
	}
}
