package com.miggo65.noverification.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

/**
 * Mixin to modify the Session class to allow offline mode.
 * This allows the client to work without proper authentication.
 */
@Mixin(Session.class)
public class SessionMixin {
    
    @Shadow
    private String username;

    /**
     * Inject into the getProfile method to return an offline profile.
     * This allows connecting to servers with online-mode=false.
     */
    @Inject(method = "getProfile", at = @At("HEAD"), cancellable = true)
    private void getOfflineProfile(CallbackInfoReturnable<GameProfile> cir) {
        // Create an offline UUID based on the username
        UUID offlineUuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes());
        GameProfile offlineProfile = new GameProfile(offlineUuid, username);
        cir.setReturnValue(offlineProfile);
    }
}
