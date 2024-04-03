package me.kmaxi.vowcloud.Audio;

import me.kmaxi.vowcloud.Loggers;
import me.kmaxi.vowcloud.VowCloud;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;

import static me.kmaxi.vowcloud.Loggers.logALError;
import static org.lwjgl.openal.AL11.alSource3i;
import static org.lwjgl.openal.EXTEfx.*;

public class SoundEffects {

    private int maxAuxSends;
    private int sourceID;

    private int[] auxFXSlots = new int[4];
    private int[] reverbs = new int[4];

    private static final float PHI = 1.618033988F;

    private int directFilter;


    private Minecraft mc;

    private int[] sendFilters = new int[4];


    private final float[] sendGains = new float[4];
    private final float[] sendCutoffs = new float[4];

    /*
    * Adjusts the cutoff frequency of the low-pass filter. Min: 0.0, Max: 1.0
     */
    private float hfCutoff = 1f;

    /*
    * Adjusts the volume of the sound. Min: 0.0, Max: 1.0
     */
    private float gain = 1f;


    public SoundEffects(int sourceId) {

        mc = Minecraft.getInstance();
        this.sourceID = sourceId;

        long currentContext = ALC10.alcGetCurrentContext();
        long currentDevice = ALC10.alcGetContextsDevice(currentContext);

        if (ALC10.alcIsExtensionPresent(currentDevice, "ALC_EXT_EFX")) {
            Loggers.log("EFX Extension recognized");
        } else {
            Loggers.log("EFX Extension not found on current device. Aborting.");
            return;
        }

        this.maxAuxSends = ALC10.alcGetInteger(currentDevice, EXTEfx.ALC_MAX_AUXILIARY_SENDS);
        Loggers.log("Max auxiliary sends: " + maxAuxSends);


        setupEFX();
    }


    private void setupEFX() {
        for (int i = 0; i < 4; i++) {
            auxFXSlots[i] = EXTEfx.alGenAuxiliaryEffectSlots();
        }

        for (int i = 0; i < 4; i++) {
            int reverbSlot = EXTEfx.alGenEffects();
            reverbs[i] = reverbSlot;
            EXTEfx.alEffecti(reverbSlot, EXTEfx.AL_EFFECT_TYPE, AL_EFFECT_EAXREVERB);
            ReverbParams params = ReverbParams.getReverbParams(i);

            EXTEfx.alEffectf(reverbSlot, AL_EAXREVERB_DENSITY, params.density);
            EXTEfx.alEffectf(reverbSlot, AL_EAXREVERB_DIFFUSION, params.diffusion);
            EXTEfx.alEffectf(reverbSlot, AL_EAXREVERB_GAIN, params.gain);
            EXTEfx.alEffectf(reverbSlot, AL_EAXREVERB_GAINHF, params.gainHF);
            EXTEfx.alEffectf(reverbSlot, AL_EAXREVERB_DECAY_TIME, params.decayTime);
            EXTEfx.alEffectf(reverbSlot, AL_EAXREVERB_DECAY_HFRATIO, params.decayHFRatio);
            EXTEfx.alEffectf(reverbSlot, AL_EAXREVERB_REFLECTIONS_GAIN, params.reflectionsGain);
            EXTEfx.alEffectf(reverbSlot, AL_EAXREVERB_LATE_REVERB_GAIN, params.lateReverbGain);
            EXTEfx.alEffectf(reverbSlot, AL_EAXREVERB_LATE_REVERB_DELAY, params.lateReverbDelay);
            EXTEfx.alEffectf(reverbSlot, AL_EAXREVERB_AIR_ABSORPTION_GAINHF, params.airAbsorptionGainHF);
            EXTEfx.alEffectf(reverbSlot, AL_REVERB_ROOM_ROLLOFF_FACTOR, params.roomRolloffFactor);

            EXTEfx.alAuxiliaryEffectSloti(auxFXSlots[i], EXTEfx.AL_EFFECTSLOT_EFFECT, reverbSlot);

        }
        directFilter = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(directFilter, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);

        for (int i = 0; i < 4; i++) {
            sendFilters[i] = EXTEfx.alGenFilters();
            EXTEfx.alFilteri(sendFilters[i], EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
        }
    }

    public void applyEffects() {
        if (!VowCloud.CONFIG.reverbEnabled.get()) {
            return;
        }

        for (int i = 0; i < Math.min(maxAuxSends, 4); i++) {
            setFilter(i, 3 - i);
        }

        EXTEfx.alFilterf(directFilter, EXTEfx.AL_LOWPASS_GAIN, gain);
        EXTEfx.alFilterf(directFilter, EXTEfx.AL_LOWPASS_GAINHF, hfCutoff);
        AL11.alSourcei(sourceID, EXTEfx.AL_DIRECT_FILTER, directFilter);
        logALError("Set environment directFilter0:");

        AL11.alSourcef(sourceID, EXTEfx.AL_AIR_ABSORPTION_FACTOR, VowCloud.CONFIG.airAbsorption.get());
        logALError("Set environment airAbsorption:");
    }

    private void setFilter(int id, int auxSlot){
        EXTEfx.alFilterf(sendFilters[id], EXTEfx.AL_LOWPASS_GAIN, sendGains[id]);
        EXTEfx.alFilterf(sendFilters[id], EXTEfx.AL_LOWPASS_GAINHF, sendCutoffs[id]);
        AL11.alSource3i(sourceID, EXTEfx.AL_AUXILIARY_SEND_FILTER, auxFXSlots[id], auxSlot, sendFilters[0]);
        logALError("Set environment filter0:");
    }

    private void resetFilters(){
        for (int i = 0; i < 4; i++) {
            sendGains[i] = 0;
            sendCutoffs[i] = 1;
        }
        gain = 1f;
        hfCutoff = 1f;
    }


    public void evaluateEnvironment(double posX, double posY, double posZ) {

        resetFilters();
        if (mc.player == null || mc.level == null || (posX == 0D && posY == 0D && posZ == 0D)) {
            applyEffects();
            return;
        }

        float absorptionCoeff = (float) (VowCloud.CONFIG.blockAbsorption.get() * 3D);

        Vec3 playerPos = mc.gameRenderer.getMainCamera().getPosition();
        Vec3 soundPos = new Vec3(posX, posY, posZ);
        BlockPos soundBlockPos = new BlockPos((int) soundPos.x, (int) soundPos.y, (int) soundPos.z);

        double occlusionAccumulation = calculateOcclusion(soundPos, playerPos);

        hfCutoff = (float) Math.exp(-occlusionAccumulation * absorptionCoeff);
        gain = (float) Math.pow(hfCutoff, 0.1D);

        if (mc.player.isUnderWater()) {
            hfCutoff *= 1F - VowCloud.CONFIG.underwaterFilter.get();
        }

        // Shoot rays around sound
        float maxDistance = 30F;

        int numRays = VowCloud.CONFIG.environmentEvaluationRayCount.get();
        int rayBounces = VowCloud.CONFIG.environmentEvaluationRayBounces.get();

        ReflectedAudio audioDirection = new ReflectedAudio(occlusionAccumulation);

        float[] bounceReflectivityRatio = new float[rayBounces];

        float rcpTotalRays = 1F / (numRays * rayBounces);

        float gAngle = PHI * (float) Math.PI * 2F;

        Vec3 directSharedAirspaceVector = getSharedAirspace(soundPos, playerPos);
        if (directSharedAirspaceVector != null) {
            audioDirection.addDirectAirspace(directSharedAirspaceVector);
        }

        for (int i = 0; i < numRays; i++) {
            float fiN = (float) i / numRays;
            float longitude = gAngle * (float) i * 1F;
            float latitude = (float) Math.asin(fiN * 2F - 1F);

            Vec3 rayDir = new Vec3(Math.cos(latitude) * Math.cos(longitude), Math.cos(latitude) * Math.sin(longitude), Math.sin(latitude));

            Vec3 rayEnd = new Vec3(soundPos.x + rayDir.x * maxDistance, soundPos.y + rayDir.y * maxDistance, soundPos.z + rayDir.z * maxDistance);

            BlockHitResult rayHit = raycast(soundPos, rayEnd, soundBlockPos);

            if (rayHit.getType() == HitResult.Type.BLOCK) {
                double rayLength = soundPos.distanceTo(rayHit.getLocation());

                // Additional bounces
                BlockPos lastHitBlock = rayHit.getBlockPos();
                Vec3 lastHitPos = rayHit.getLocation();
                Vec3 lastHitNormal = new Vec3(rayHit.getDirection().step());
                Vec3 lastRayDir = rayDir;

                float totalRayDistance = (float) rayLength;

                RaycastRenderer.addSoundBounceRay(soundPos, rayHit.getLocation(), ChatFormatting.GREEN.getColor());

                Vec3 firstSharedAirspaceVector = getSharedAirspace(rayHit, playerPos);
                if (firstSharedAirspaceVector != null) {
                    audioDirection.addSharedAirspace(firstSharedAirspaceVector, totalRayDistance);
                }

                // Secondary ray bounces
                for (int j = 0; j < rayBounces; j++) {
                    Vec3 newRayDir = reflect(lastRayDir, lastHitNormal);
                    Vec3 newRayStart = lastHitPos;
                    Vec3 newRayEnd = new Vec3(newRayStart.x + newRayDir.x * maxDistance, newRayStart.y + newRayDir.y * maxDistance, newRayStart.z + newRayDir.z * maxDistance);

                    BlockHitResult newRayHit = raycast(newRayStart, newRayEnd, lastHitBlock);

                    float blockReflectivity = getBlockReflectivity(lastHitBlock);
                    float energyTowardsPlayer = 0.25F * (blockReflectivity * 0.75F + 0.25F);

                    if (newRayHit.getType() == HitResult.Type.MISS) {
                        totalRayDistance += lastHitPos.distanceTo(playerPos);

                        RaycastRenderer.addSoundBounceRay(newRayStart, newRayEnd, ChatFormatting.RED.getColor());
                    } else {
                        Vec3 newRayHitPos = newRayHit.getLocation();

                        RaycastRenderer.addSoundBounceRay(newRayStart, newRayHitPos, ChatFormatting.BLUE.getColor());

                        double newRayLength = lastHitPos.distanceTo(newRayHitPos);

                        bounceReflectivityRatio[j] += blockReflectivity;

                        totalRayDistance += newRayLength;

                        lastHitPos = newRayHitPos;
                        lastHitNormal = new Vec3(newRayHit.getDirection().step());
                        lastRayDir = newRayDir;
                        lastHitBlock = newRayHit.getBlockPos();

                        Vec3 sharedAirspaceVector = getSharedAirspace(newRayHit, playerPos);
                        if (sharedAirspaceVector != null) {
                            audioDirection.addSharedAirspace(sharedAirspaceVector, totalRayDistance);
                        }
                    }

                    float reflectionDelay = (float) Math.max(totalRayDistance, 0D) * 0.12F * blockReflectivity;

                    float[] cross = new float[]{1F - Mth.clamp(Math.abs(reflectionDelay - 0F), 0F, 1F),
                            1F - Mth.clamp(Math.abs(reflectionDelay - 1F), 0F, 1F),
                            1F - Mth.clamp(Math.abs(reflectionDelay - 2F), 0F, 1F),
                            Mth.clamp(reflectionDelay - 2F, 0F, 1F)
                    };

                    sendGains[0] += cross[0] * energyTowardsPlayer * 6.4F * rcpTotalRays;
                    for (int y = 1; y < 4; y++){
                        sendGains[y] += cross[y] * energyTowardsPlayer * 12.8F * rcpTotalRays;
                    }

                    // Nowhere to bounce off of, stop bouncing!
                    if (newRayHit.getType() == HitResult.Type.MISS) {
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < bounceReflectivityRatio.length; i++) {
            bounceReflectivityRatio[i] = bounceReflectivityRatio[i] / numRays;
            Loggers.log("Bounce reflectivity {}: {}", i, bounceReflectivityRatio[i]);
        }

        @Nullable Vec3 newSoundPos = audioDirection.evaluateSoundPosition(soundPos, playerPos);
        if (newSoundPos != null) {
            setSoundPos(sourceID, newSoundPos);
        }

        float sharedAirspace = audioDirection.getSharedAirspaces() * 64F * rcpTotalRays;

        Loggers.log("Shared airspace: {} ({})", sharedAirspace, audioDirection.getSharedAirspaces());

        float[] sharedAirspaceWeights = new float[]{Mth.clamp(sharedAirspace / 20F, 0F, 1F),
                Mth.clamp(sharedAirspace / 15F, 0F, 1F),
                Mth.clamp(sharedAirspace / 10F, 0F, 1F),
                Mth.clamp(sharedAirspace / 10F, 0F, 1F)};

        for (int i = 0; i <4; i++ ){
            sendCutoffs[i] = (float) Math.exp(-occlusionAccumulation * absorptionCoeff * 1F) * (1F - sharedAirspaceWeights[i]) + sharedAirspaceWeights[i];
        }


        // Attempt to preserve directionality when airspace is shared by allowing some of the dry signal through but filtered
        float averageSharedAirspace = (sharedAirspaceWeights[0] + sharedAirspaceWeights[1] + sharedAirspaceWeights[2] + sharedAirspaceWeights[3]) * 0.25F;
        hfCutoff = Math.max((float) Math.pow(averageSharedAirspace, 0.5D) * 0.2F, hfCutoff);

        gain = (float) Math.pow(hfCutoff, 0.1D);

        sendGains[1] *= bounceReflectivityRatio[1];
        if (bounceReflectivityRatio.length > 2) {
            sendGains[2] *= (float) Math.pow(bounceReflectivityRatio[2], 3D);
        }
        if (bounceReflectivityRatio.length > 3) {
            sendGains[3] *= (float) Math.pow(bounceReflectivityRatio[3], 4D);
        }

        sendGains[0] = Mth.clamp(sendGains[0], 0F, 1F);
        sendGains[1] = Mth.clamp(sendGains[1], 0F, 1F);
        sendGains[2] = Mth.clamp(sendGains[2] * 1.05F - 0.05F, 0F, 1F);
        sendGains[3] = Mth.clamp(sendGains[3] * 1.05F - 0.05F, 0F, 1F);

        for (int i = 0; i <4; i++){
            sendGains[i] = (float) Math.pow(sendCutoffs[i], 0.1D);
        }

        assert mc.player != null;
        if (mc.player.isUnderWater()) {
            for (int i = 0; i <4; i++){
                sendCutoffs[i] *= 0.4F;
            }
        }
        applyEffects();
    }

    private double calculateOcclusion(Vec3 soundPos, Vec3 playerPos) {
        if (VowCloud.CONFIG.strictOcclusion.get()) {
            return Math.min(runOcclusion(soundPos, playerPos), VowCloud.CONFIG.maxOcclusion.get());
        }
        double variationFactor = VowCloud.CONFIG.occlusionVariation.get();

        double occlusionAccMin = Double.MAX_VALUE;

        occlusionAccMin = Math.min(occlusionAccMin, runOcclusion(soundPos, playerPos));
        if (variationFactor > 0D) {
            for (int x = -1; x <= 1; x += 2) {
                for (int y = -1; y <= 1; y += 2) {
                    for (int z = -1; z <= 1; z += 2) {
                        Vec3 offset = new Vec3(x, y, z).scale(variationFactor);
                        occlusionAccMin = Math.min(occlusionAccMin, runOcclusion(soundPos.add(offset), playerPos.add(offset)));
                    }
                }
            }
        }

        return Math.min(occlusionAccMin, VowCloud.CONFIG.maxOcclusion.get());
    }

    private double runOcclusion(Vec3 soundPos, Vec3 playerPos) {
        double occlusionAccumulation = 0D;
        Vec3 rayOrigin = soundPos;
        BlockPos lastBlockPos = new BlockPos((int) soundPos.x, (int) soundPos.y, (int) soundPos.z);
        for (int i = 0; i < VowCloud.CONFIG.maxOcclusionRays.get(); i++) {
            BlockHitResult rayHit = raycast(rayOrigin, playerPos, lastBlockPos);

            lastBlockPos = rayHit.getBlockPos();

            if (rayHit.getType() == HitResult.Type.MISS) {
                RaycastRenderer.addOcclusionRay(rayOrigin, playerPos.add(0D, -0.1D, 0D), Mth.hsvToRgb(1F / 3F * (1F - Math.min(1F, (float) occlusionAccumulation / 12F)), 1F, 1F));
                break;
            }
            RaycastRenderer.addOcclusionRay(rayOrigin, rayHit.getLocation(), Mth.hsvToRgb(1F / 3F * (1F - Math.min(1F, (float) occlusionAccumulation / 12F)), 1F, 1F));

            BlockPos blockHitPos = rayHit.getBlockPos();
            rayOrigin = rayHit.getLocation();
            BlockState blockHit = mc.level.getBlockState(blockHitPos);
            float blockOcclusion = VowCloud.OCCLUSION_CONFIG.getBlockDefinitionValue(blockHit);

            // Regardless to whether we hit from inside or outside
            Vec3 dirVec = rayOrigin.subtract(blockHitPos.getX() + 0.5D, blockHitPos.getY() + 0.5D, blockHitPos.getZ() + 0.5D);
            Direction sideHit = Direction.getNearest(dirVec.x, dirVec.y, dirVec.z);

            if (!blockHit.isFaceSturdy(mc.level, rayHit.getBlockPos(), sideHit)) {
                blockOcclusion *= VowCloud.CONFIG.nonFullBlockOcclusionFactor.get();
            }

            Loggers.log("{} \t{},{},{}", blockHit.getBlock().getDescriptionId(), rayOrigin.x, rayOrigin.y, rayOrigin.z);

            //Accumulate density
            occlusionAccumulation += blockOcclusion;

            if (occlusionAccumulation > VowCloud.CONFIG.maxOcclusion.get()) {
                Loggers.log("Max occlusion reached after {} steps", i + 1);
                break;
            }
        }

        return occlusionAccumulation;
    }

    /**
     * Checks if the hit shares the same airspace with the listener
     *
     * @param hit              the hit position
     * @param listenerPosition the position of the listener
     * @return the vector between the hit and the listener or null if there is no shared airspace
     */
    @Nullable
    private Vec3 getSharedAirspace(BlockHitResult hit, Vec3 listenerPosition) {
        Vector3f hitNormal = hit.getDirection().step();
        Vec3 rayStart = new Vec3(hit.getLocation().x + hitNormal.x() * 0.001D, hit.getLocation().y + hitNormal.y() * 0.001D, hit.getLocation().z + hitNormal.z() * 0.001D);
        return getSharedAirspace(rayStart, listenerPosition);
    }

    /**
     * Checks if the hit shares the same airspace with the listener
     *
     * @param soundPosition    the sound position
     * @param listenerPosition the position of the listener
     * @return the vector between the hit and the listener or null if there is no shared airspace
     */
    @Nullable
    private Vec3 getSharedAirspace(Vec3 soundPosition, Vec3 listenerPosition) {
        BlockHitResult finalRayHit = raycast(soundPosition, listenerPosition, null);
        if (finalRayHit.getType() == HitResult.Type.MISS) {
            RaycastRenderer.addSoundBounceRay(soundPosition, listenerPosition.add(0D, -0.1D, 0D), ChatFormatting.WHITE.getColor());
            return soundPosition.subtract(listenerPosition);
        }
        return null;
    }


    public BlockHitResult raycast(Vec3 start, Vec3 end, @Nullable BlockPos ignore) {

        if (mc.level != null) {
            return mc.level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, mc.player));
        }

        Vec3 dir = end.subtract(start);
        return BlockHitResult.miss(end, Direction.getNearest(dir.x, dir.y, dir.z), new BlockPos((int) end.x, (int) end.y, (int) end.z));
    }

    private static void setSoundPos(int sourceID, Vec3 pos) {
        AL11.alSource3f(sourceID, AL11.AL_POSITION, (float) pos.x, (float) pos.y, (float) pos.z);
    }

    private float getBlockReflectivity(BlockPos blockPos) {
        if (mc.level == null) {
            return VowCloud.CONFIG.defaultBlockReflectivity.get();
        }
        BlockState blockState = mc.level.getBlockState(blockPos);
        return VowCloud.REFLECTIVITY_CONFIG.getBlockDefinitionValue(blockState);
    }

    private Vec3 reflect(Vec3 dir, Vec3 normal) {
        //dir - 2.0 * dot(normal, dir) * normal
        double dot = dir.dot(normal) * 2D;

        double x = dir.x - dot * normal.x;
        double y = dir.y - dot * normal.y;
        double z = dir.z - dot * normal.z;

        return new Vec3(x, y, z);
    }


    private int echoSlot;
    private int reverbSlot;
    private int echoEffect;
    private int reverbEffect;
    private int normalReverbSlot;
    private int normalReverbEffect;
    private boolean hasSetUp;

    public void setEcho() {

        if (!hasSetUp) {
            reverbSlot = alGenAuxiliaryEffectSlots();
            echoSlot = alGenAuxiliaryEffectSlots();
            normalReverbSlot = alGenAuxiliaryEffectSlots();
            reverbEffect = alGenEffects();
            echoEffect = alGenEffects();
            normalReverbEffect = alGenEffects();

            // Set up the effect parameters, for example, reverb
            alEffecti(reverbEffect, AL_EFFECT_TYPE, AL_EFFECT_EAXREVERB);
            alEffecti(echoEffect, AL_EFFECT_TYPE, AL_EFFECT_ECHO);
            alEffecti(normalReverbEffect, AL_EFFECT_TYPE, AL_EFFECT_REVERB);

            //Set up the reverb effect
            EXTEfx.alEffectf(normalReverbEffect, AL_REVERB_DENSITY, 1.0f);
            EXTEfx.alEffectf(normalReverbEffect, AL_REVERB_DIFFUSION, 1.0f);
            EXTEfx.alEffectf(normalReverbEffect, AL_REVERB_GAIN, 1.0f);
            EXTEfx.alEffectf(normalReverbEffect, AL_REVERB_GAINHF, 1.0f);
            EXTEfx.alEffectf(normalReverbEffect, AL_REVERB_DECAY_TIME, 5f);
            EXTEfx.alEffectf(normalReverbEffect, AL_REVERB_DECAY_HFRATIO, 1f);
            EXTEfx.alEffectf(normalReverbEffect, AL_REVERB_REFLECTIONS_GAIN, 5f);
            EXTEfx.alEffectf(normalReverbEffect, AL_REVERB_LATE_REVERB_GAIN, 6f);
            EXTEfx.alEffectf(normalReverbEffect, AL_REVERB_LATE_REVERB_DELAY, 0.1f);


            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_DENSITY, 1.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_DIFFUSION, 1.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_GAIN, 1.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_GAINHF, 1.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_DECAY_TIME, 20.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_DECAY_HFRATIO, 2.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_DECAY_LFRATIO, 2.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_REFLECTIONS_GAIN, 3.16f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_REFLECTIONS_DELAY, 0.3f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_LATE_REVERB_GAIN, 10.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_LATE_REVERB_DELAY, 0.1f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_ECHO_TIME, 0.25f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_ECHO_DEPTH, 1.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_MODULATION_TIME, 4.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_MODULATION_DEPTH, 1.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_AIR_ABSORPTION_GAINHF, 1.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_HFREFERENCE, 20000.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_LFREFERENCE, 1000.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_ROOM_ROLLOFF_FACTOR, 10.0f);


            EXTEfx.alEffectf(echoEffect, AL_ECHO_DELAY, 0.1f); // Adjust delay for the desired echo effect
            EXTEfx.alEffectf(echoEffect, AL_ECHO_LRDELAY, 0.1f); // Adjust left/right delay for stereo effect
            EXTEfx.alEffectf(echoEffect, AL_ECHO_DAMPING, 0.5f); // Adjust damping for the echo effect

            alAuxiliaryEffectSloti(reverbSlot, AL_EFFECTSLOT_EFFECT, reverbEffect);
            alAuxiliaryEffectSloti(echoSlot, AL_EFFECTSLOT_EFFECT, echoEffect);
            alAuxiliaryEffectSloti(normalReverbSlot, AL_EFFECTSLOT_EFFECT, normalReverbEffect);


            hasSetUp = true;
        }

        alSource3i(sourceID, AL_AUXILIARY_SEND_FILTER, reverbSlot, 0, AL_FILTER_NULL);
        alSource3i(sourceID, AL_AUXILIARY_SEND_FILTER, echoSlot, 1, AL_FILTER_NULL);
        alSource3i(sourceID, AL_AUXILIARY_SEND_FILTER, normalReverbSlot, 2, AL_FILTER_NULL);

    }


}
