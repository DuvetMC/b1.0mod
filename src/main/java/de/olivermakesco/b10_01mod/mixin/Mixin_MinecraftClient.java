package de.olivermakesco.b10_01mod.mixin;


import net.minecraft.client.MinecraftClient;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class Mixin_MinecraftClient {
	@Unique
	private boolean isMouseButtonSwapped = getMouseSwapArg();

	@Unique
	private boolean getMouseSwapArg() {
		String arg = System.getProperty("mc.mouseswap");
		if (arg == null) return false;
		return arg.equalsIgnoreCase("true");
	}

	@Redirect(
			method = "tickInput",
			at = @At(
					value = "INVOKE",
					target = "Lorg/lwjgl/input/Mouse;getEventButton()I"
			)
	)
	private int changeValue() {
		int button = Mouse.getEventButton();
		if (!isMouseButtonSwapped) return button;
		if (button == 0) return 1;
		if (button == 1) return 0;
		return button;
	}

	@ModifyArg(
			method = "tickInput",
			at = @At(
					value = "INVOKE",
					target = "Lorg/lwjgl/input/Mouse;isButtonDown(I)Z"
			)
	)
	private int changeValue(int button) {
		if (!isMouseButtonSwapped) return button;
		if (button == 0) return 1;
		if (button == 1) return 0;
		return button;
	}
}
