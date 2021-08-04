/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.client;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.LongPredicate;
import net.angle.rusticregen.client.LeafModelLoader.LeafCoveredGeometry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.geometry.IModelGeometry;

/**
 *
 * @author angle
 */
public class LeafModelLoader implements IModelLoader<LeafCoveredGeometry> {

    @Override
    public LeafCoveredGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
		// not needed at the moment, consider using if caches need to be cleared
    }
    
    public static class LeafCoveredGeometry implements IModelGeometry<LeafCoveredGeometry> {

        @Override
        public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    public static class LeafCoveredBakedModel implements IDynamicBakedModel {

        public static BakedModel getFallbackModel() {
            return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getModelManager().getMissingModel();
        }

        @Override
        public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData) {
            if (state == null)
                return Lists.newArrayList();
            BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
            return getQuads(state, model, side, rand);
        }

        public List<BakedQuad> getQuads(BlockState state, BakedModel model, Direction side, Random rand) {
            Block block = state.getBlock();
            if (state == null)
                return Lists.newArrayList();
    //        IModelData tileData = state.hasBlockEntity() ? Minecraft.getInstance().level.getBlockEntity().getModelData() : EmptyModelData.INSTANCE;
    //        IModelData modelData = model.getModelData(block.getWorld(), block.getPos(), state, tileData);
    //        return getQuadsForModel(model, state, side, rand, modelData, block.isPositive());
            return Lists.newArrayList();
        }

        @Override
        public boolean useAmbientOcclusion() {
            return getFallbackModel().useAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return getFallbackModel().isGui3d();
        }

        @Override
        public boolean usesBlockLight() {
            return getFallbackModel().usesBlockLight();
        }

        @Override
        public boolean isCustomRenderer() {
            return getFallbackModel().isCustomRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return getFallbackModel().getParticleIcon();
        }

        @Override
        public ItemOverrides getOverrides() {
            return ItemOverrides.EMPTY;
        }
    }
    
    public static class LeafCoveredModelData implements IModelData {
        public static final ModelProperty<LeafCoveredModelData> PROPERTY = new ModelProperty<>();

        public LeafCoveredModelData() {}

        @Override
        public boolean hasProperty(ModelProperty<?> prop) {
            return prop == PROPERTY;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T getData(ModelProperty<T> prop) {
            if (prop == PROPERTY)
                return (T) this;
            else
                return null;
        }

        @Override
        public <T> T setData(ModelProperty<T> prop, T data) {
            return data;
        }

    }
}
