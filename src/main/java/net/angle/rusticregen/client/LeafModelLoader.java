/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix4f;
import com.mojang.math.Transformation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.geometry.IModelGeometry;

/**
 *
 * @author angle
 */
public class LeafModelLoader implements IModelLoader<LeafCoveredGeometry> {
    
    public static LeafModelLoader INSTANCE = new LeafModelLoader();

    @Override
    public LeafCoveredGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new LeafCoveredGeometry();
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
		// not needed at the moment, consider using if caches need to be cleared
    }
    
    public static class LeafCoveredGeometry implements IModelGeometry<LeafCoveredGeometry> {

        @Override
        public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
            Material particleLocation = owner.resolveTexture("particle");
            TextureAtlasSprite particle = spriteGetter.apply(particleLocation);
            return new LeafCoveredBakedModel(owner.useSmoothLighting(), owner.isShadedInGui(), owner.isSideLit(), particle);
        }

        @Override
        public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
            return Lists.newArrayList();
        }
        
    }
    
    public static class LeafCoveredBakedModel implements IDynamicBakedModel {
        
        private final boolean useAmbientOcclusion;
        private final boolean isGui3d;
        private final boolean usesBlockLight;
        private final TextureAtlasSprite particle;
        
        public LeafCoveredBakedModel(boolean useAmbientOcclusion, boolean isGui3d, boolean usesBlockLight, TextureAtlasSprite particle) {
            this.useAmbientOcclusion = useAmbientOcclusion;
            this.isGui3d = isGui3d;
            this.usesBlockLight = usesBlockLight;
            this.particle = particle;
        }

        @Override
        public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData) {
            LeafCoveredModelData data = extraData.getData(LeafCoveredModelData.PROPERTY);
            
            List<BakedQuad> quads = new ArrayList<>();
            
            if (data == null)
                return quads;
            
            if (data.leafState != null) {
                Matrix4f matrix = Matrix4f.createScaleMatrix(1.001f, 1.001f, 1.001f);
                matrix.add(Matrix4f.createTranslateMatrix(-0.0005f, -0.0005f, -0.0005f));
                QuadTransformer transformer = new QuadTransformer(new Transformation(matrix));
                quads.addAll(transformer.processMany(Minecraft.getInstance().getBlockRenderer().getBlockModel(data.leafState).getQuads(data.leafState, side, rand, data)));
            }
            
            if (data.internalBlockState != null)
                quads.addAll(Minecraft.getInstance().getBlockRenderer().getBlockModel(data.internalBlockState).getQuads(data.internalBlockState, side, rand, data));
            
            return quads;
        }

        @Override
        public boolean useAmbientOcclusion() {
            return useAmbientOcclusion;
        }

        @Override
        public boolean isGui3d() {
            return isGui3d;
        }

        @Override
        public boolean usesBlockLight() {
            return usesBlockLight;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return particle;
        }

        @Override
        public ItemOverrides getOverrides() {
            return ItemOverrides.EMPTY;
        }
        
    }
    
    public static class LeafCoveredModelData implements IModelData {
        public static final ModelProperty<LeafCoveredModelData> PROPERTY = new ModelProperty<>();

        public final BlockState leafState;
        public final BlockState internalBlockState;
        
        public LeafCoveredModelData(BlockState leafState) {
            this(leafState, null);
        }
        
        public LeafCoveredModelData(BlockState leafState, BlockState internalBlockState) {
            this.leafState = leafState;
            this.internalBlockState = internalBlockState;
        }

        @Override
        public boolean hasProperty(ModelProperty<?> prop) {
            return prop == PROPERTY;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T getData(ModelProperty<T> prop) {
            if (prop == PROPERTY)
                return (T) this;
            else {
                return null;
            }
        }

        @Override
        public <T> T setData(ModelProperty<T> mp, T t) {
            throw new UnsupportedOperationException("Not supported.");
        }
    }
}
