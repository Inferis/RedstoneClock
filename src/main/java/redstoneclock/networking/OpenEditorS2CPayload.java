package redstoneclock.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import redstoneclock.RedstoneClock;

public record OpenEditorS2CPayload(BlockPos blockPos, int activeInterval, int inactiveInterval) implements CustomPayload {
    public static final CustomPayload.Id<OpenEditorS2CPayload> ID = new CustomPayload.Id<>(Identifier.of(RedstoneClock.MODID, "open_editor_s2c_payload"));

    public static final PacketCodec<RegistryByteBuf, OpenEditorS2CPayload> CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, OpenEditorS2CPayload::blockPos, 
        PacketCodecs.INTEGER, OpenEditorS2CPayload::activeInterval,
        PacketCodecs.INTEGER, OpenEditorS2CPayload::inactiveInterval, 
        OpenEditorS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
