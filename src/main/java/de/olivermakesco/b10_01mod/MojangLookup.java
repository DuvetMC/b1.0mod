package de.olivermakesco.b10_01mod;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;

public class MojangLookup {
	public static String MOJANG_API_BASE = "https://api.mojang.com/";
	public static String MOJANG_SESSION_BASE = "https://sessionserver.mojang.com/";
	public static Gson gson = new Gson();

	public static @Nullable String getUid(@NotNull String username) {
		try {
			var url = new URL(MOJANG_API_BASE+"users/profiles/minecraft/"+username);
			var reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder responseBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line);
			}
			reader.close();
			String response = responseBuilder.toString();
			var json = gson.fromJson(response, JsonObject.class);
			if (json.has("errorMessage")) return null;
			return json.get("id").getAsString();
		} catch (IOException e) {
			return null;
		}
	}

	public static @Nullable String getSkinFromUid(@NotNull String uid) {
		try {
			var url = new URL(MOJANG_SESSION_BASE+"session/minecraft/profile/"+uid);
			var reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder responseBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line);
			}
			reader.close();
			String response = responseBuilder.toString();
			var json = gson.fromJson(response, JsonObject.class);
			if (json.has("errorMessage")) return null;
			var b64Encode = json.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
			var skinData = gson.fromJson(new String(Base64.getDecoder().decode(b64Encode)), JsonObject.class).getAsJsonObject().get("textures").getAsJsonObject().get("SKIN").getAsJsonObject();
			var slim = false;
			if (skinData.has("metadata")) {
				var meta = skinData.get("metadata").getAsJsonObject();
				if (meta.has("model")) {
					slim = Objects.equals(meta.get("model").getAsString(), "slim");
				}
			}
			return getCroppedSkinUrl(skinData.get("url").getAsString()) + (slim ? "?slim" : "");
		} catch (IOException e) {
			return null;
		}
	}

	public static @NotNull String getCroppedSkinUrl(@NotNull String url) {
		return url.replace("http://textures.minecraft.net/texture/", "https://skin-oldifier.deno.dev/");
	}
}
