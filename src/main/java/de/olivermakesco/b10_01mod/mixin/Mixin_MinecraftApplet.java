package de.olivermakesco.b10_01mod.mixin;

import net.minecraft.client.MinecraftApplet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(MinecraftApplet.class)
public class Mixin_MinecraftApplet {
	@Redirect(
			method = "init",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/MinecraftApplet;getParameter(Ljava/lang/String;)Ljava/lang/String;",
					remap = false
			)
	)
	private String setUsername(MinecraftApplet instance, String s) {
		String username = System.getProperty("mc.username");
		if (username == null) instance.getParameter(s);
		if (Objects.equals(s, "username")) return username;
		return instance.getParameter(s);
	}
}
