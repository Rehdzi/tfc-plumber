package ru.nnchan.tfc.plumber.block.custom;

import net.dries007.tfc.common.blocks.DirectionPropertyBlock;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.fluids.FluidProperty;
import net.dries007.tfc.common.fluids.IFluidLoggable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nnchan.tfc.plumber.PlumberTags;

import java.util.Objects;

public class PipeBlock extends HorizontalDirectionalBlock implements IFluidLoggable {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    private static final VoxelShape[] SHAPES = new VoxelShape[16];
    public static final FluidProperty FLUID = TFCBlockStateProperties.ALL_WATER;
    public static final IntegerProperty PRESSURE = PipeBlock.PRESSURE;
    //public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState>pBuilder){
        //pBuilder.add(FILLED);
        //pBuilder.add(PRESSURE);
        pBuilder.add(NORTH, EAST, WEST, SOUTH, FACING, getFluidProperty());
    }

    static {
        final VoxelShape north = box(5,2,0,11,8,16);
        final VoxelShape east = box(5,2,0,11,8,16);
        final VoxelShape south = box(5,2,0,11,8,16);
        final VoxelShape west = box(5,2,0,11,8,16);

        final VoxelShape[] directions = new VoxelShape[] {south, west, north, east};

        final VoxelShape base = Shapes.or(
                box(0, 0, 0, 16, 10, 16),
                box(0, 10, 0, 4, 16, 4),
                box(12, 10, 0, 16, 16, 4),
                box(0, 10, 12, 4, 16, 16),
                box(12, 10, 12, 16, 16, 16)
        );

        for (int i = 0; i < SHAPES.length; i++)
        {
            VoxelShape shape = base;
            for (Direction direction : Direction.Plane.HORIZONTAL)
            {
                if (((i >> direction.get2DDataValue()) & 1) == 0)
                {
                    shape = Shapes.or(shape, directions[direction.get2DDataValue()]);
                }
            }
            SHAPES[i] = shape;
        }
    }

    /*private boolean isSameFence(BlockState p_153255_) {
        return p_153255_.is(PlumberTags.TFC_PIPE) == this.defaultBlockState().is(BlockTags.WOODEN_FENCES);
    }
    */
    public PipeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(getStateDefinition().any()
                .setValue(NORTH, Boolean.FALSE)
                .setValue(EAST, Boolean.FALSE)
                .setValue(SOUTH, Boolean.FALSE)
                .setValue(WEST, Boolean.FALSE)
                .setValue(FACING, Direction.NORTH)
                .setValue(getFluidProperty(),getFluidProperty().keyFor(Fluids.EMPTY)));
                //.setValue(PRESSURE, 0));
    }

    //private static final VoxelShape SHAPE =  Block.box(5,2,0,11,8,16);

    /*@Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }*/

    private static BlockState updateOpenSides(LevelAccessor level, BlockPos pos, BlockState state)
    {
        int openSides = 0;
        @Nullable Direction openDirection = null;
        for (final Direction direction : Direction.Plane.HORIZONTAL)
        {
            final BlockPos adjacentPos = pos.relative(direction);
            final BlockState adjacentState = level.getBlockState(adjacentPos);
            final boolean adjacentAqueduct = adjacentState.getBlock() instanceof PipeBlock;
            if (adjacentAqueduct)
            {
                openSides++;
                openDirection = direction;
            }

            state = state.setValue(DirectionPropertyBlock.getProperty(direction), adjacentAqueduct);
        }

        if (openSides == 1)
        {
            // If we only have a single open side, then we always treat this as a straight aqueduct.
            state = state.setValue(DirectionPropertyBlock.getProperty(openDirection.getOpposite()), true);
        }

        return state;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return updateOpenSides(pContext.getLevel(), pContext.getClickedPos(), defaultBlockState());
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state)
    {
        return IFluidLoggable.super.getFluidState(state);
    }

    @Override
    public FluidProperty getFluidProperty()
    {
        return FLUID;
    }

}