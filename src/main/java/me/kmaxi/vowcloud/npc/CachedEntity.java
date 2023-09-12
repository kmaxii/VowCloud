package me.kmaxi.vowcloud.npc;


import net.minecraft.world.entity.Entity;

public class CachedEntity {

    public Entity name;
    public Entity child;
    public double distance;
    public boolean isArmourStand = false;

    public CachedEntity(Entity nameEntity, Entity childEntity){
        name = nameEntity;
        child = childEntity;
        isArmourStand = NPCHandler.isArmourStand(childEntity);
        distance = nameEntity.distanceTo(child);
    }

    public CachedEntity(){}
}
