package de.skystrike;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class SkyStrikeClient implements ClientModInitializer {
    private static KeyBinding callLightningKey;

    @Override
    public void onInitializeClient() {
        callLightningKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.skystrike.call_lightning",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "key.category.skystrike"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (callLightningKey.wasPressed()) {
                summonSkyLightning(client);
            }
        });
    }

    /** Creates cosmetic lightning high in the sky, never at the targeted block. */
    private static void summonSkyLightning(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        Vec3d playerPos = client.player.getPos();
        Vec3d look = client.player.getRotationVec(1.0F);
        Vec3d horizontalLook = new Vec3d(look.x, 0.0, look.z);
        if (horizontalLook.lengthSquared() < 0.0001) {
            horizontalLook = new Vec3d(0.0, 0.0, 1.0);
        } else {
            horizontalLook = horizontalLook.normalize();
        }

        // Keep the effect well above the terrain and a short distance ahead.
        double x = playerPos.x + horizontalLook.x * 28.0;
        double z = playerPos.z + horizontalLook.z * 28.0;
        double y = Math.min(playerPos.y + 30.0, client.world.getTopY() - 8.0);
        y = Math.max(y, playerPos.y + 12.0);

        // Several nearby cosmetic bolts give the sky flash a forked silhouette.
        spawnSkyBolt(client, x, y, z);
        spawnSkyBolt(client, x + 2.4, y - 1.5, z - 1.8);
        spawnSkyBolt(client, x - 2.0, y + 1.0, z + 2.2);
        spawnSkyBolt(client, x + 0.8, y + 2.0, z + 3.0);

        client.world.playSound(x, y, z,
                SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
                SoundCategory.WEATHER, 8.0F, 0.88F, false);
        client.world.playSound(x, y, z,
                SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT,
                SoundCategory.WEATHER, 5.0F, 1.0F, false);
    }

    private static void spawnSkyBolt(MinecraftClient client, double x, double y, double z) {
        LightningEntity bolt = new LightningEntity(EntityType.LIGHTNING_BOLT, client.world);
        bolt.refreshPositionAfterTeleport(x, y, z);
        bolt.setCosmetic(true); // no damage, fire, or block interaction
        client.world.addEntity(bolt);
    }
}
