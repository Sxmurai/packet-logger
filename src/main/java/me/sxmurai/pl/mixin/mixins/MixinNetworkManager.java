package me.sxmurai.pl.mixin.mixins;

import io.netty.channel.ChannelHandlerContext;
import me.sxmurai.pl.PacketLogger;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @Inject(method = "channelRead0", at = @At("HEAD"))
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo info) {
        PacketLogger.packetManager.onReceived(packet);
    }

    @Inject(method = "sendPacket", at = @At("HEAD"))
    public void onSendPacket(Packet<?> packet, CallbackInfo info) {
        PacketLogger.packetManager.onSent(packet);
    }
}
