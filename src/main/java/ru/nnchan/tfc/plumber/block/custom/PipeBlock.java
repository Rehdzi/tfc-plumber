package ru.nnchan.tfc.plumber.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import ru.nnchan.tfc.plumber.PlumberTags;

import java.util.Objects;

public class PipeBlock extends CrossCollisionBlock {
    private final VoxelShape[] occlusionByIndex;
    public static final BooleanProperty FILLED = PipeBlock.FILLED;
    public static final IntegerProperty PRESSURE = PipeBlock.PRESSURE;
    //public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState>pBuilder){
        //pBuilder.add(FILLED);
        //pBuilder.add(PRESSURE);
        pBuilder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return this.occlusionByIndex[this.getAABBIndex(pState)];
    }

    @Override
    public @NotNull VoxelShape getVisualShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return this.getShape(pState, pLevel, pPos, pContext);
    }

    public boolean connectsTo(BlockState pState, boolean pBoolean, Direction pDirection) {
        Block block = pState.getBlock();
        //boolean flag = this.isSameFence(block);
        //boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(pState, pDirection);
        return !isExceptionForConnection(pState) && pBoolean;
    }

    /*private boolean isSameFence(BlockState p_153255_) {
        return p_153255_.is(PlumberTags.TFC_PIPE) == this.defaultBlockState().is(BlockTags.WOODEN_FENCES);
    }
    */
    public PipeBlock(BlockBehaviour.Properties properties) {
        super(16,16,16,16,8, properties);
        this.registerDefaultState(getStateDefinition().any()
                .setValue(NORTH, Boolean.FALSE)
                .setValue(EAST, Boolean.FALSE)
                .setValue(SOUTH, Boolean.FALSE)
                .setValue(WEST, Boolean.FALSE)
                .setValue(WATERLOGGED, Boolean.FALSE));
                //.setValue(FILLED, Boolean.FALSE)
                //.setValue(PRESSURE, 0));
        this.occlusionByIndex = this.makeShapes(2.0F, 1.0F, 16.0F, 6.0F, 15.0F);
    }

    //private static final VoxelShape SHAPE =  Block.box(5,2,0,11,8,16);

    /*@Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }*/



    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return false;
    }

    /* FACING */

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockGetter blockgetter = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockState blockstate = blockgetter.getBlockState(blockpos1);
        BlockState blockstate1 = blockgetter.getBlockState(blockpos2);
        BlockState blockstate2 = blockgetter.getBlockState(blockpos3);
        BlockState blockstate3 = blockgetter.getBlockState(blockpos4);
        return Objects.requireNonNull(super.getStateForPlacement(pContext))
                .setValue(NORTH, this.connectsTo(blockstate, blockstate.isFaceSturdy(blockgetter, blockpos1, Direction.SOUTH), Direction.SOUTH))
                .setValue(EAST, this.connectsTo(blockstate1, blockstate1.isFaceSturdy(blockgetter, blockpos2, Direction.WEST), Direction.WEST))
                .setValue(SOUTH, this.connectsTo(blockstate2, blockstate2.isFaceSturdy(blockgetter, blockpos3, Direction.NORTH), Direction.NORTH))
                .setValue(WEST, this.connectsTo(blockstate3, blockstate3.isFaceSturdy(blockgetter, blockpos4, Direction.EAST), Direction.EAST))
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

}